package br.com.hydromarket.controllers;

import br.com.hydromarket.dtos.whatsapp.webhook.WhatsAppWebhookDTO;
import br.com.hydromarket.services.WhatsAppService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@Slf4j
public class WhatsAppWebhook {
    private final WhatsAppService whatsAppService;

    public WhatsAppWebhook(WhatsAppService whatsAppService) {
        this.whatsAppService = whatsAppService;
    }

    @PostMapping("/")
    public ResponseEntity<Void> messageReceived(@RequestBody WhatsAppWebhookDTO body) {
        log.debug("Messages webhook called");

        this.whatsAppService.messageReceived(body);

        return ResponseEntity.ok().build();
    }

    @GetMapping("/")
    public ResponseEntity<String> verifyWebhook(
            @RequestParam("hub.mode") String mode,
            @RequestParam("hub.challenge") String challenge,
            @RequestParam("hub.verify_token") String verifyToken
    ) {
        log.info("Webhook verification (subscribe)");

        if(this.whatsAppService.isVerifyTokenCorrect(mode, verifyToken)) {
            return ResponseEntity.ok(challenge);
        }

        log.error("Webhook verification failed");

        return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
    }
}
