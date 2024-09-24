package com.egt.currencygateway.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class RequestLog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String serviceName;
    private String requestId;
    private String clientId;
    private LocalDateTime timestamp;

    public RequestLog(String serviceName, String requestId, String clientId, LocalDateTime timestamp) {
        this.serviceName = serviceName;
        this.requestId = requestId;
        this.clientId = clientId;
        this.timestamp = timestamp;
    }
}
