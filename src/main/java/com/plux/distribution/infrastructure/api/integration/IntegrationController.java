package com.plux.distribution.infrastructure.api.integration;

import com.plux.distribution.application.port.in.integration.CrudIntegrationUseCase;
import com.plux.distribution.domain.service.ServiceId;
import com.plux.distribution.infrastructure.api.integration.request.IntegrationRequest;
import com.plux.distribution.infrastructure.api.integration.response.CreateIntegrationResponse;
import com.plux.distribution.infrastructure.api.integration.response.IntegrationsResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/integrations")
@Tag(name = "Integration")
public class IntegrationController {

    private final CrudIntegrationUseCase crudIntegrationUseCase;

    @SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
    public IntegrationController(CrudIntegrationUseCase crudIntegrationUseCase) {
        this.crudIntegrationUseCase = crudIntegrationUseCase;
    }

    @PostMapping
    @Operation(
            summary = "Create integration",
            description = "Create new service and return its token",
            responses = {
                    @ApiResponse(
                            responseCode = "201",
                            description = "Service was successful created"
                    )
            }
    )
    public ResponseEntity<CreateIntegrationResponse> createIntegration(@RequestBody IntegrationRequest request) {
        var result = crudIntegrationUseCase.create(request.toCommand());
        return ResponseEntity.status(HttpStatus.CREATED).body(CreateIntegrationResponse.of(result));
    }

    @GetMapping
    @Operation(
            summary = "Get all integrations",
            description = "Return all registered services"
    )
    public ResponseEntity<IntegrationsResponse> getAllIntegrations() {
        var result = crudIntegrationUseCase.getAll();
        return ResponseEntity.ok(new IntegrationsResponse(
                result.stream().map(IntegrationView::of).toList()
        ));
    }

    @DeleteMapping("/{id}")
    @Operation(
            summary = "Delete integration",
            description = "Delete specified service",
            responses = {
                    @ApiResponse(
                            responseCode = "204",
                            description = "Service was deleted"
                    )
            }
    )
    public ResponseEntity<Void> deleteIntegration(@PathVariable("id") Long id) {
        crudIntegrationUseCase.delete(new ServiceId(id));
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{id}")
    @Operation(
            summary = "Update integration",
            description = "Update(PUT) specified service info",
            responses = {
                    @ApiResponse(
                            responseCode = "204",
                            description = "Service was updated"
                    )
            }
    )
    public ResponseEntity<Void> putIntegration(
            @PathVariable("id") Long id,
            @RequestBody IntegrationRequest request
    ) {
        crudIntegrationUseCase.put(new ServiceId(id), request.toCommand());
        return ResponseEntity.noContent().build();
    }
}
