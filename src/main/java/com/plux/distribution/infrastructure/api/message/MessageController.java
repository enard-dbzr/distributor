package com.plux.distribution.infrastructure.api.message;

import com.plux.distribution.application.dto.integration.SendServiceMessageCommand;
import com.plux.distribution.application.port.exception.InvalidToken;
import com.plux.distribution.application.port.in.integration.SendServiceMessageUseCase;
import com.plux.distribution.infrastructure.api.message.request.SendMessageRequest;

import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping({"/messages"})
public class MessageController {
    private final SendServiceMessageUseCase sendServiceMessageUseCase;

    public MessageController(
            @SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection") SendServiceMessageUseCase sendServiceMessageUseCase) {
        this.sendServiceMessageUseCase = sendServiceMessageUseCase;
    }

    @PostMapping
    @SecurityRequirement(name = "serviceToken")
    public ResponseEntity<SendMessageResult> sendMessage(
            @Parameter(hidden = true)
            @RequestHeader("Authorization") String authHeader,
            @Valid @RequestBody SendMessageRequest request
    ) {
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        String token = authHeader.substring("Bearer ".length()).trim();

        try {
            var messageId = sendServiceMessageUseCase.send(
                    new SendServiceMessageCommand(token, request.chatId(), request.content().toModel()));

            return ResponseEntity.status(HttpStatus.CREATED).body(new SendMessageResult(messageId.value()));
        } catch (InvalidToken e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }
}
