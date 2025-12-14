package br.com.hydromarket.dtos.whatsapp.webhook;

import com.fasterxml.jackson.annotation.JsonProperty;

public record Status(@JsonProperty("id") String identifier,
                     String status,
                     String timestamp,
                     @JsonProperty("recipient_id") String recipientIdentifier,
                     Pricing pricing) {

}
