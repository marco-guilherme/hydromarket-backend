package br.com.hydromarket.controllers;

import br.com.hydromarket.components.WhatsAppProperties;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
public class WhatsAppWebhook {
    private final WhatsAppProperties whatsAppProperties;

    public WhatsAppWebhook(WhatsAppProperties whatsAppProperties) {
        this.whatsAppProperties = whatsAppProperties;
    }

    @PostMapping("/")
    public ResponseEntity<Void> messageReceived(@RequestBody(required = false) Map<String, Object> body) {
        System.out.println("BODY " + body);

        return ResponseEntity.ok().build();
    }

    @GetMapping("/")
    public ResponseEntity<String> verifyWebhook(
            @RequestParam("hub.mode") String mode,
            @RequestParam("hub.challenge") String challenge,
            @RequestParam("hub.verify_token") String verifyToken
    ) {
        if("subscribe".equals(mode) && this.whatsAppProperties.getVerifyToken().equals(verifyToken)) {
            return ResponseEntity.ok(challenge);
        }

        return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
    }
}
