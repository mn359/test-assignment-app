package com.example.testassignmentapp.cbr;

import com.example.testassignmentapp.exchangerate.ExchangeRateDTO;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class CbrXmlParserTest {
    @Test
    public void testParseExchangeRateXml() {

        String resXml = """
                <?xml version="1.0" encoding="utf-8"?>
                <soap:Envelope
                	xmlns:soap="http://schemas.xmlsoap.org/soap/envelope/"
                	xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
                	xmlns:xsd="http://www.w3.org/2001/XMLSchema">
                	<soap:Body>
                		<GetCursOnDateXMLResponse
                			xmlns="http://web.cbr.ru/">
                			<GetCursOnDateXMLResult>
                				<ValuteData OnDate="20231124"
                					xmlns="">
                					<ValuteCursOnDate>
                						<Vname>Австралийский доллар                                                                                                                                                                                                                                          </Vname>
                						<Vnom>1</Vnom>
                						<Vcurs>57.7542</Vcurs>
                						<Vcode>36</Vcode>
                						<VchCode>AUD</VchCode>
                						<VunitRate>57.7542</VunitRate>
                					</ValuteCursOnDate>
                					<ValuteCursOnDate>
                						<Vname>Азербайджанский манат                                                                                                                                                                                                                                         </Vname>
                						<Vnom>1</Vnom>
                						<Vcurs>51.8356</Vcurs>
                						<Vcode>944</Vcode>
                						<VchCode>AZN</VchCode>
                						<VunitRate>51.8356</VunitRate>
                					</ValuteCursOnDate>
                				</ValuteData>
                			</GetCursOnDateXMLResult>
                		</GetCursOnDateXMLResponse>
                	</soap:Body>
                </soap:Envelope>
                """;
        String soapActionName = "GetCursOnDateXML";
        LocalDate date = LocalDate.of(2023, 11, 20);
        List<ExchangeRateDTO> expectedRes = new ArrayList<>();
        expectedRes.add(new ExchangeRateDTO("AUD", "57.7542", date.atStartOfDay()));
        expectedRes.add(new ExchangeRateDTO("AZN", "51.8356", date.atStartOfDay()));

        CbrXmlParser cbrXmlParser = new CbrXmlParser();
        List<ExchangeRateDTO> actualRes = cbrXmlParser.parseExchangeRateXml(resXml, soapActionName, date.atStartOfDay());

        Assertions.assertEquals(expectedRes.size(), actualRes.size());
        for (int i = 0; i < expectedRes.size(); i++) {
            Assertions.assertEquals(expectedRes.get(i).currency(), actualRes.get(i).currency());
            Assertions.assertEquals(expectedRes.get(i).rate(), actualRes.get(i).rate());
            Assertions.assertEquals(expectedRes.get(i).dateTime(), actualRes.get(i).dateTime());
        }
    }

    @Test
    public void testParseExchangeRateInPeriod() {
        String resXml = """
            <?xml version="1.0" encoding="utf-8"?>
                    <soap:Envelope
                        xmlns:soap="http://schemas.xmlsoap.org/soap/envelope/"
                        xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
                        xmlns:xsd="http://www.w3.org/2001/XMLSchema">
                        <soap:Body>
                            <GetCursDynamicXMLResponse
                                xmlns="http://web.cbr.ru/">
                                <GetCursDynamicXMLResult>
                                    <ValuteData
                                        xmlns="">
                                        <ValuteCursDynamic>
                                            <CursDate>2023-11-01T00:00:00+03:00</CursDate>
                                            <Vcode>R01235</Vcode>
                                            <Vnom>1</Vnom>
                                            <Vcurs>92.0226</Vcurs>
                                            <VunitRate>92.0226</VunitRate>
                                        </ValuteCursDynamic>
                                        <ValuteCursDynamic>
                                            <CursDate>2023-11-02T00:00:00+03:00</CursDate>
                                            <Vcode>R01235</Vcode>
                                            <Vnom>1</Vnom>
                                            <Vcurs>93.2801</Vcurs>
                                            <VunitRate>93.2801</VunitRate>
                                        </ValuteCursDynamic>
                                    </ValuteData>
                                </GetCursDynamicXMLResult>
                            </GetCursDynamicXMLResponse>
                        </soap:Body>
                    </soap:Envelope>
                """;
        String soapActionName = "GetCursDynamicXML";
        String currencyCode = "USD";
        List<ExchangeRateDTO> expectedRes = new ArrayList<>();
        expectedRes.add(new ExchangeRateDTO("USD", "92.0226", LocalDateTime.parse("2023-11-01T00:00:00")));
        expectedRes.add(new ExchangeRateDTO("USD", "93.2801", LocalDateTime.parse("2023-11-02T00:00:00")));

        CbrXmlParser cbrXmlParser = new CbrXmlParser();
        List<ExchangeRateDTO> actualRes = cbrXmlParser.parseExchangeRateInPeriod(resXml, soapActionName, currencyCode);

        Assertions.assertEquals(expectedRes.size(), actualRes.size());
        for (int i = 0; i < expectedRes.size(); i++) {
            Assertions.assertEquals(expectedRes.get(i).currency(), actualRes.get(i).currency());
            Assertions.assertEquals(expectedRes.get(i).rate(), actualRes.get(i).rate());
            Assertions.assertEquals(expectedRes.get(i).dateTime(), actualRes.get(i).dateTime());
        }
    }

    @Test
    public void testParseDailyCurrencies() {
        // Arrange
        String resXml = """
                <?xml version="1.0" encoding="utf-8"?>
                <soap:Envelope
                	xmlns:soap="http://schemas.xmlsoap.org/soap/envelope/"
                	xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
                	xmlns:xsd="http://www.w3.org/2001/XMLSchema">
                	<soap:Body>
                		<EnumValutesXMLResponse
                			xmlns="http://web.cbr.ru/">
                			<EnumValutesXMLResult>
                				<ValuteData
                					xmlns="">
                					<EnumValutes>
                						<Vcode>R01235              </Vcode>
                						<Vname>Доллар США                                                                                                                                                                                                                                                    </Vname>
                						<VEngname>US Dollar                                                                                                                                                                                                                                                     </VEngname>
                						<Vnom>1</Vnom>
                						<VcommonCode>R01235    </VcommonCode>
                						<VnumCode>840</VnumCode>
                						<VcharCode>USD</VcharCode>
                					</EnumValutes>
                					<EnumValutes>
                     						<Vcode>R01239              </Vcode>
                     						<Vname>Евро                                                                                                                                                                                                                                                          </Vname>
                     						<VEngname>Euro                                                                                                                                                                                                                                                          </VEngname>
                     						<Vnom>1</Vnom>
                     						<VcommonCode>R01239    </VcommonCode>
                     						<VnumCode>978</VnumCode>
                     						<VcharCode>EUR</VcharCode>
                     					</EnumValutes>
                				</ValuteData>
                			</EnumValutesXMLResult>
                		</EnumValutesXMLResponse>
                	</soap:Body>
                </soap:Envelope>
                """;
        String soapActionName = "EnumValutesXML";
        List<CbrCurrency> expectedRes = new ArrayList<>();
        expectedRes.add(new CbrCurrency("USD", "R01235"));
        expectedRes.add(new CbrCurrency("EUR", "R01239"));

        CbrXmlParser cbrXmlParser = new CbrXmlParser();
        List<CbrCurrency> actualRes = cbrXmlParser.parseDailyCurrencies(resXml, soapActionName);

        Assertions.assertEquals(expectedRes.size(), actualRes.size());
        for (int i = 0; i < expectedRes.size(); i++) {
            Assertions.assertEquals(expectedRes.get(i).code(), actualRes.get(i).code());
            Assertions.assertEquals(expectedRes.get(i).internalCbrCode(), actualRes.get(i).internalCbrCode());
        }
    }
}