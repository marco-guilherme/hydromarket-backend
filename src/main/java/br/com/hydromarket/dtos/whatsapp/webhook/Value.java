package br.com.hydromarket.dtos.whatsapp.webhook;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public record Value(@JsonProperty("messaging_product") String messagingProduct,
                    Metadata metadata,
                    List<Contact> contacts,
                    List<Message> messages,
                    List<Status> statuses) {

}
