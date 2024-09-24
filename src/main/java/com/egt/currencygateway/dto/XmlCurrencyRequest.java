package com.egt.currencygateway.dto;

import lombok.Data;

import javax.xml.bind.annotation.*;

@Data
@XmlRootElement(name = "command")
@XmlAccessorType(XmlAccessType.FIELD)
public class XmlCurrencyRequest {

    @XmlAttribute(name = "id")
    private String requestId;

    @XmlElement(name = "get")
    public GetCurrency get;

    @XmlElement(name = "history")
    public HistoryCurrency history;

    // Inner classes for 'get' and 'history'
    @Data
    @XmlAccessorType(XmlAccessType.FIELD)
    public static class GetCurrency {
        @XmlAttribute(name = "consumer")
        private String client;
        private String currency;
    }

    @Data
    @XmlAccessorType(XmlAccessType.FIELD)
    public static class HistoryCurrency {
        @XmlAttribute(name = "consumer")
        private String client;
        private String currency;
        private int period;
    }
}
