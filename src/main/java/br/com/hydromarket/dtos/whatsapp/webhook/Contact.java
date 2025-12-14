package br.com.hydromarket.dtos.whatsapp.webhook;

import com.fasterxml.jackson.annotation.JsonProperty;

public record Contact(@JsonProperty("wa_id") String whatsAppIdentifier, Profile profile) {

}
