package com.example.testassignmentapp.cbr;

import com.example.testassignmentapp.exchangerate.ExchangeRateDTO;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.stereotype.Service;
import org.springframework.ws.client.core.support.WebServiceGatewaySupport;
//import ru.sbr.dailyinfo.wsdl.GetCursOnDateXML;
//import ru.sbr.dailyinfo.wsdl.GetCursOnDateXMLResponse;

import java.time.LocalDate;
import java.util.List;

@Service("soap")
public class CbrSoapService extends WebServiceGatewaySupport implements CbrWebService {

    // todo the request return 500; sending it manually works

//    @Autowired
//    public ExchangeRateSoapService(Jaxb2Marshaller marshaller) {
//        this.setMarshaller(marshaller);
//        this.setUnmarshaller(marshaller);
//    }

//    public List<Object> updateExchangeRate() throws DatatypeConfigurationException {
//        GetCursOnDateXML request = new GetCursOnDateXML ();
//        request.setOnDate(DateTimeUtils.todayInXmlGregorianCalendar());
//
//        var wst = getWebServiceTemplate();
//        // wst.setInterceptors(new ClientInterceptor[]{new MySOAPInterceptor(), new MyInterceptor()});
//
//        GetCursOnDateXMLResponse response = (GetCursOnDateXMLResponse) wst
//                .marshalSendAndReceive(
//                        "http://www.cbr.ru/DailyInfoWebServ/DailyInfo.asmx",
//                        request,new SoapActionCallback("http://web.cbr.ru/GetCursOnDateXML"));;
//       return response.getGetCursOnDateXMLResult().getContent();
//    }

    @Override
    public List<ExchangeRateDTO> getCurrentExchangeRates() {
        //return updateExchangeRate();
        return null;
    }

    @Override
    public List<ExchangeRateDTO> getExchangeRatesForCurrencyInPeriod(LocalDate from, LocalDate to, String currencyCode, String internalCurrencyCode) throws JsonProcessingException {
        return null;
    }

    @Override
    public List<CbrCurrency> getDailyCurrencies() throws JsonProcessingException {
        return null;
    }
}
