package com.example.testassignmentapp.cbr;

import com.example.testassignmentapp.exchangerate.ExchangeRateDTO;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.RuntimeJsonMappingException;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;

@Component
public class CbrXmlParser {
    public List<ExchangeRateDTO> parseExchangeRateXml(String resXml,
                                                      String soapActionName,
                                                      LocalDate date) {

        var mapper =  new XmlMapper();
        JsonNode nodeTree = readTree(mapper, resXml);

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

    List<ExchangeRateDTO> parseExchangeRateInPeriod(String resXml,
                                                    String soapActionName,
                                                    String currencyCode) {
        var mapper = new XmlMapper();
        JsonNode resNodeTree = readTree(mapper, resXml);

        var arr = getArrayNode(mapper, resNodeTree, soapActionName);

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

    List<CbrCurrency> parseDailyCurrencies(String resXml,
                                           String soapActionName) {
        var mapper = new XmlMapper();
        JsonNode resNodeTree = readTree(mapper, resXml);

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

    JsonNode readTree(XmlMapper mapper, String resXml) {
        JsonNode resNodeTree = null;
        try {
            resNodeTree = mapper.readTree(resXml);
        } catch (JsonProcessingException e) {
            throw new RuntimeJsonMappingException("Error while mapping currency data");
        }
        return resNodeTree;
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
}
