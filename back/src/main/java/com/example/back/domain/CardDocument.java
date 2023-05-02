package com.example.back.domain;

import com.example.back.es.Currency;
import lombok.Builder;
import lombok.Data;
import org.bson.codecs.pojo.annotations.BsonId;
import org.bson.codecs.pojo.annotations.BsonProperty;
import org.springframework.data.mongodb.core.mapping.Document;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Builder
@Document(collection = "card")
public class CardDocument {

    @BsonId
    String id;

    @BsonProperty(value = "aggregate_id")
    String aggregateId;

    @BsonProperty(value = "currency")
    Currency currency;

    @BsonProperty(value = "expiration_date")
    LocalDateTime expirationDate;

    @BsonProperty(value = "CVC")
    String CVC;

    @BsonProperty(value = "balance")
    BigDecimal balance;

    @BsonProperty(value = "customer_id")
    String customerId;
}
