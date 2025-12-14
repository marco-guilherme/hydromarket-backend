package br.com.hydromarket.components;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "whatsapp")
@Getter
@Setter
public class WhatsAppProperties {
    private String systemUserAccessToken;
    private String verifyToken;
    private String telephoneNumberIdentifier;
    private String APIVersion;
    private String sendMessageEndpoint;
}
