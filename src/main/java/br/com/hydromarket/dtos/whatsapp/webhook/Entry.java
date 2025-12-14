package br.com.hydromarket.dtos.whatsapp.webhook;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public record Entry(@JsonProperty("id") String identifier, List<Change> changes) {

}
