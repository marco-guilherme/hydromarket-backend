package br.com.hydromarket.controllers;

import br.com.hydromarket.dtos.whatsapp.webhook.WhatsAppWebhookDTO;
import br.com.hydromarket.services.WhatsAppFlow;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@Slf4j
public class WhatsAppWebhook {
    private final WhatsAppFlow whatsAppFlow;

    public WhatsAppWebhook(WhatsAppFlow whatsAppFlow) {
        this.whatsAppFlow = whatsAppFlow;
    }

    @PostMapping("/")
    public ResponseEntity<Void> messageReceived(@RequestBody WhatsAppWebhookDTO body) {
        log.debug("Messages webhook called");

        this.whatsAppFlow.messageReceived(body);

        return ResponseEntity.ok().build();
    }

    @GetMapping("/")
    public ResponseEntity<String> verifyWebhook(
            @RequestParam("hub.mode") String mode,
            @RequestParam("hub.challenge") String challenge,
            @RequestParam("hub.verify_token") String verifyToken
    ) {
        log.info("Webhook verification (subscribe)");

        if(this.whatsAppFlow.isVerifyTokenCorrect(mode, verifyToken)) {
            return ResponseEntity.ok(challenge);
        }

        log.error("Webhook verification failed");

        return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
    }
}
