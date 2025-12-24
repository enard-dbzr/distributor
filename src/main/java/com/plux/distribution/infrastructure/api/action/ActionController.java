package com.plux.distribution.infrastructure.api.action;

import com.plux.distribution.core.message.application.port.in.ExecuteActionUseCase;
import com.plux.distribution.infrastructure.api.action.request.ExecuteChatActionRequest;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/actions")
@Tag(name = "Action")
public class ActionController {
    private final ExecuteActionUseCase executeActionUseCase;

    @SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
    public ActionController(ExecuteActionUseCase executeActionUseCase) {
        this.executeActionUseCase = executeActionUseCase;
    }

    @PostMapping
    @Operation(
            summary = "Execute action for chat"
    )
    public ResponseEntity<Void> executeAction(@RequestBody ExecuteChatActionRequest request) {

        executeActionUseCase.execute(request.toModel());

        return ResponseEntity.ok().build();
    }
}
