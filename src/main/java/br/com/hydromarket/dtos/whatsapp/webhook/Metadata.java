package br.com.hydromarket.dtos.whatsapp.webhook;

import com.fasterxml.jackson.annotation.JsonProperty;

public record Metadata(@JsonProperty("display_phone_number") String displayTelephoneNumber,
                       @JsonProperty("phone_number_id") String telephoneNumberIdentifier) {

}
