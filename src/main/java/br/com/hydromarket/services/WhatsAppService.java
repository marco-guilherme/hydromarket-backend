package br.com.hydromarket.services;

import br.com.hydromarket.components.WhatsAppProperties;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import java.util.Map;

@Service
public class WhatsAppService {
    private final RestClient restClient;
    private final WhatsAppProperties whatsAppProperties;

    public WhatsAppService(RestClient.Builder builder, WhatsAppProperties whatsAppProperties) {
        this.restClient = builder.build();
        this.whatsAppProperties = whatsAppProperties;
    }

    public void sendMessage(String toTelephoneNumber, String message) {
        Map<String, Object> body = Map.of(
                "messaging_product", "whatsapp",
                "recipient_type", "individual",
                "to", toTelephoneNumber,
                "type", "text",
                "text", Map.of(
                        "body", message
                )
        );

        restClient.post()
                .uri(this.whatsAppProperties.getSendMessageEndpoint())
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + this.whatsAppProperties.getSystemUserAccessToken())
                .contentType(MediaType.APPLICATION_JSON)
                .body(body)
                .retrieve()
                .toBodilessEntity();
    }
}
