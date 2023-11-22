package com.example.testassignmentapp.exchangerate.web;

import com.example.testassignmentapp.common.DateTimeUtils;
import com.example.testassignmentapp.exchangerate.ExchangeRateDTO;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Service("http")
public class ExchangeRateHttpService implements ExchangeRateWebService {

    private final WebClient webClient;

    @Autowired
    public ExchangeRateHttpService(WebClient.Builder webClientBuilder) {
        this.webClient = webClientBuilder
                .baseUrl("http://www.cbr.ru/")
                .build();
    }

    public List<ExchangeRateDTO> getCurrentExchangeRates() throws JsonProcessingException {
        return getExchangeRate(DateTimeUtils.today());
    }

    public List<ExchangeRateDTO> getExchangeRate(LocalDate date) throws JsonProcessingException {
        var xmlString = requestExchangeRatesAsXmlString(date);

        XmlMapper xmlMapper = new XmlMapper();
        var nodeTree = xmlMapper.readTree(xmlString);

        List<ExchangeRateDTO> res = new ArrayList<>();

        nodeTree.get("Body")
            .get("GetCursOnDateXMLResponse")
            .get("GetCursOnDateXMLResult")
            .get("ValuteData")
            .get("ValuteCursOnDate").forEach(o -> res.add(
                    new ExchangeRateDTO(
                            o.get("VchCode").asText(),
                            o.get("VunitRate").asText()
                    )
            ));

        return res;
    }

    public String requestExchangeRatesAsXmlString(LocalDate date) {
        return this.webClient
                .post()
                .uri("DailyInfoWebServ/DailyInfo.asmx")
                .header("Content-Type", "text/xml; charset=utf-8")
                .header("SOAPAction", "http://web.cbr.ru/GetCursOnDateXML")
                .body(Mono.just(getRequestXmlString(date)), String.class)
                .retrieve()
                .bodyToMono(String.class)
                .block();
    }

    private String getRequestXmlString(LocalDate date) {
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
}
