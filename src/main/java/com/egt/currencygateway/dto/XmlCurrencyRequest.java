package com.egt.currencygateway.dto;


import lombok.Data;

import javax.xml.bind.annotation.*;

@XmlRootElement(name = "command")
@XmlAccessorType(XmlAccessType.FIELD)
@Data
public class XmlCurrencyRequest {

    @XmlAttribute(name = "id")
    private String requestId;

    @XmlElement(name = "get")
    private GetCurrency get;

    @XmlElement(name = "history")
    private HistoryCurrency history;

    // Inner classes for 'get' and 'history'
    @XmlAccessorType(XmlAccessType.FIELD)
    public static class GetCurrency {
        @XmlAttribute(name = "consumer")
        private String client;
        private String currency;
    }

    @XmlAccessorType(XmlAccessType.FIELD)
    public static class HistoryCurrency {
        @XmlAttribute(name = "consumer")
        private String client;
        private String currency;
        private int period;
    }
}
