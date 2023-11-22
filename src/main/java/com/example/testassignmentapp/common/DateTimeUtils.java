package com.example.testassignmentapp.common;

import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;
import java.time.LocalDate;

public class DateTimeUtils {
    public static LocalDate today() {
        return LocalDate.now();
    }

    public static XMLGregorianCalendar todayInXmlGregorianCalendar() throws DatatypeConfigurationException {
        return localDateToXmlGregorianCalendar(today());
    }

    public static XMLGregorianCalendar localDateToXmlGregorianCalendar(LocalDate localDate) throws DatatypeConfigurationException {
        return DatatypeFactory.newInstance()
                .newXMLGregorianCalendar(localDate.toString());
    }
}
