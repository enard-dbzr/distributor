package com.plux.distribution;

import com.plux.distribution.core.integration.application.port.in.CrudIntegrationUseCase;
import com.plux.distribution.core.integration.application.port.in.SendServiceInteractionUseCase;
import com.plux.distribution.core.mediastorage.application.port.in.CrudMediaUseCase;
import com.plux.distribution.infrastructure.api.integration.IntegrationController;
import com.plux.distribution.infrastructure.api.mediastorage.MediaStorageController;
import com.plux.distribution.infrastructure.api.message.InteractionController;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@EnableWebMvc
@ComponentScan(basePackages = {
        "com.plux.distribution.infrastructure.api",
})
public class AppConfig implements WebMvcConfigurer {

    @Bean
    @SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
    public InteractionController interactionController(SendServiceInteractionUseCase useCase) {
        return new InteractionController(useCase);
    }

    @Bean
    @SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
    public IntegrationController integrationController(CrudIntegrationUseCase createUseCase) {
        return new IntegrationController(createUseCase);
    }

    @Bean
    @SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
    public MediaStorageController mediaStorageController(CrudMediaUseCase crudMediaUseCase) {
        return new MediaStorageController(crudMediaUseCase);
    }

}
