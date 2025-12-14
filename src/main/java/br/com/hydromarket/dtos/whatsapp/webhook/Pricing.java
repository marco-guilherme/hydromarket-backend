package br.com.hydromarket.dtos.whatsapp.webhook;

import com.fasterxml.jackson.annotation.JsonProperty;

public record Pricing(String billable,
                      @JsonProperty("pricing_model") String pricingModel,
                      String category,
                      String type) {

}
