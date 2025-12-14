package br.com.hydromarket.dtos.whatsapp.webhook;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public record Text(String body) {

}
