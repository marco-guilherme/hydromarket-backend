package br.com.hydromarket.controllers;

import br.com.hydromarket.services.WhatsAppService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestController {
    private final WhatsAppService whatsAppService;

    public TestController(WhatsAppService whatsAppService) {
        this.whatsAppService = whatsAppService;
    }

    @PostMapping("/send")
    public void sendMessage() {
        System.out.println("SEND MESSAGE");

        whatsAppService.sendMessage("5516993482561", "lorem ipsum dolor sit amet");
    }
}
