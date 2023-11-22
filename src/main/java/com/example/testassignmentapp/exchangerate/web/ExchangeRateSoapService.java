package com.example.testassignmentapp.exchangerate.web;

import com.example.testassignmentapp.common.DateTimeUtils;
import com.example.testassignmentapp.exchangerate.ExchangeRateDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.oxm.jaxb.Jaxb2Marshaller;
import org.springframework.stereotype.Service;
import org.springframework.ws.client.core.support.WebServiceGatewaySupport;
import org.springframework.ws.soap.client.core.SoapActionCallback;
//import ru.sbr.dailyinfo.wsdl.GetCursOnDateXML;
//import ru.sbr.dailyinfo.wsdl.GetCursOnDateXMLResponse;

import javax.xml.datatype.DatatypeConfigurationException;
import java.util.List;

@Service("soap")
public class ExchangeRateSoapService extends WebServiceGatewaySupport implements ExchangeRateWebService {

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
}
