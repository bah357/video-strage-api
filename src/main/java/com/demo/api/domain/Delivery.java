package com.demo.api.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.Instant;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Document("delivery")
@Data
public class Delivery {
    private String deliveryId;
    private Instant eventTimestamp;
}
