package com.example.testassignmentapp.cbr;

import com.example.testassignmentapp.common.DateTimeUtils;
import com.example.testassignmentapp.exchangerate.ExchangeRateDTO;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.RuntimeJsonMappingException;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Service("http")
public class CbrHttpService implements CbrWebService {
    private static Logger logger = LoggerFactory.getLogger(CbrHttpService.class);

    private final WebClient webClient;

    @Autowired
    public CbrHttpService(WebClient.Builder webClientBuilder) {
        this.webClient = webClientBuilder
                .baseUrl("http://www.cbr.ru/")
                .build();
    }

    @Override
    public List<ExchangeRateDTO> getCurrentExchangeRates() {
        return getExchangeRate(DateTimeUtils.today());
    }

    public List<ExchangeRateDTO> getExchangeRate(LocalDate date) {
        String soapActionName = "GetCursOnDateXML";

        var xmlString = sendRequest(
                getExchangeRateOnDateRequestBody(date),
                "http://web.cbr.ru/" + soapActionName);

        var mapper =  new XmlMapper();
        JsonNode nodeTree = null;
        try {
            nodeTree = mapper.readTree(xmlString);
        } catch (JsonProcessingException e) {
            throw new RuntimeJsonMappingException("Error while mapping exchangeRateData");
        }

        List<ExchangeRateDTO> res = new ArrayList<>();

        getArrayNode(mapper, nodeTree, soapActionName).forEach(o -> res.add(
                    new ExchangeRateDTO(
                            o.get("VchCode").asText().strip(),
                            o.get("VunitRate").asText().strip(),
                            date.atStartOfDay()
                    )
        ));

        return res;
    }

    /*** "CursDate" -> {TextNode@13227} ""2023-11-18T00:00:00+03:00""
         "Vcode" -> {TextNode@13229} ""R01235""
         "Vnom" -> {TextNode@13231} ""1""
         "Vcurs" -> {TextNode@13233} ""89.1237""
         "VunitRate" -> {TextNode@13235} ""89.1237""
     ***/
    @Override
    public List<ExchangeRateDTO> getExchangeRatesForCurrencyInPeriod(LocalDate from,
                                                                     LocalDate to,
                                                                     String currencyCode,
                                                                     String internalCbrCurrencyCode)  {
        String soapActionName = "GetCursDynamicXML";

        var xmlString = sendRequest(
                getDynamicExchangeRateRequestBody(from, to, internalCbrCurrencyCode),
                "http://web.cbr.ru/" + soapActionName
        );
        var mapper = new XmlMapper();
        JsonNode nodeTree = null;
        try {
            nodeTree = mapper.readTree(xmlString);
        } catch (JsonProcessingException e) {
            throw new RuntimeJsonMappingException("Error while mapping exchange rates data");
        }

        var arr = getArrayNode(mapper, nodeTree, soapActionName);

        List<ExchangeRateDTO> res = new ArrayList<>();
        arr.forEach(
                o -> res.add(new ExchangeRateDTO(
                        currencyCode,
                        o.get("VunitRate").asText().strip(),
                        OffsetDateTime.parse(o.get("CursDate").asText().strip()).toLocalDateTime()
                ))
        );

        return res;
    }

    /***
        "Vcode" -> {TextNode@13289} ""R01010              ""
        "Vname" -> {TextNode@13291} ""Австралийский доллар                                                                                                                                                                                                                                          ""
        "VEngname" -> {TextNode@13293} ""Australian Dollar                                                                                                                                                                                                                                             ""
        "Vnom" -> {TextNode@13295} ""1""
        "VcommonCode" -> {TextNode@13297} ""R01010    ""
        "VnumCode" -> {TextNode@13299} ""36""
        "VcharCode" -> {TextNode@13301} ""AUD""
    ***/
    @Override
    public List<CbrCurrency> getDailyCurrencies()  {
        logger.info("Receiving daily currencies from cbr.ru");

        String soapActionName = "EnumValutesXML";

        var resXmlString = sendRequest(
                getDailyCurrenciesRequestBody(),
                "http://web.cbr.ru/" + soapActionName
        );
        var mapper = new XmlMapper();

        JsonNode resNodeTree = null;
        try {
            resNodeTree = mapper.readTree(resXmlString);
        } catch (JsonProcessingException e) {
            throw new RuntimeJsonMappingException("Error while mapping currency data");
        }

        List<CbrCurrency> res = new ArrayList<>();

        var nodeArray = getArrayNode(mapper, resNodeTree, soapActionName);

        for (JsonNode jsonNode : nodeArray) {
            if (jsonNode.has("Vcode") && jsonNode.has("VcharCode")) {
                res.add(new CbrCurrency(
                        jsonNode.get("VcharCode").asText().strip(),
                        jsonNode.get("Vcode").asText().strip()
                ));
            }
        }
        return res;
    }

    public ArrayNode getArrayNode(ObjectMapper mapper, JsonNode nodeTree, String soapActionName) {
        var res = nodeTree.get("Body")
                .get(soapActionName + "Response")
                .get(soapActionName + "Result")
                .get("ValuteData")
                .get(soapActionName.contains("Valute")
                        ? soapActionName.replaceAll("Get|XML","")
                        : "Valute" + soapActionName.replaceAll("Get|XML",""));
        if (!ArrayNode.class.isInstance(res)) {
            return mapper.createArrayNode().add(res);
        }
        return (ArrayNode) res;
    }

    public String sendRequest(String xmlBody, String soapAction) {
        return this.webClient
                .post()
                .uri("DailyInfoWebServ/DailyInfo.asmx")
                .header("Content-Type", "text/xml; charset=utf-8")
                .header("SOAPAction", soapAction)
                .body(Mono.just(xmlBody), String.class)
                .retrieve()
                .bodyToMono(String.class)
                .block();
    }

    private String getExchangeRateOnDateRequestBody(LocalDate date) {
        return
        """
        <?xml version="1.0" encoding="utf-8"?>
        <soap:Envelope xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" xmlns:xsd="http://www.w3.org/2001/XMLSchema" xmlns:soap="http://schemas.xmlsoap.org/soap/envelope/">
          <soap:Body>
            <GetCursOnDateXML xmlns="http://web.cbr.ru/">
              <On_date>YYYY-MM-DD</On_date>
            </GetCursOnDateXML>
          </soap:Body>
        </soap:Envelope>
        """.replace("YYYY-MM-DD", date.format(DateTimeFormatter.ofPattern("yyyy-MM-dd")));
    }

    public String getDynamicExchangeRateRequestBody(LocalDate from, LocalDate to, String internalCurrencyCode) {
        return """
                <?xml version="1.0" encoding="utf-8"?>
                <soap:Envelope xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" xmlns:xsd="http://www.w3.org/2001/XMLSchema" xmlns:soap="http://schemas.xmlsoap.org/soap/envelope/">
                  <soap:Body>
                    <GetCursDynamicXML xmlns="http://web.cbr.ru/">
                      <FromDate>%FROM_DATE_PARAM%</FromDate>
                      <ToDate>%TO_DATE_PARAM%</ToDate>
                      <ValutaCode>%VALUTA_CODE_PARAM%</ValutaCode>
                    </GetCursDynamicXML>
                  </soap:Body>
                </soap:Envelope>
                """.replace("%FROM_DATE_PARAM%", from.format(DateTimeFormatter.ofPattern("yyyy-MM-dd")))
                .replace("%TO_DATE_PARAM%", to.format(DateTimeFormatter.ofPattern("yyyy-MM-dd")))
                .replace("%VALUTA_CODE_PARAM%", internalCurrencyCode);
    }

    public String getDailyCurrenciesRequestBody() {
        return """
        <?xml version="1.0" encoding="utf-8"?>
        <soap:Envelope xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" xmlns:xsd="http://www.w3.org/2001/XMLSchema" xmlns:soap="http://schemas.xmlsoap.org/soap/envelope/">
          <soap:Body>
            <EnumValutesXML xmlns="http://web.cbr.ru/">
              <Seld>false</Seld>
            </EnumValutesXML>
          </soap:Body>
        </soap:Envelope>
        """;
    }
}
