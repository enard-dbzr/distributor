package com.plux.distribution;

import com.plux.distribution.application.port.in.integration.CrudIntegrationUseCase;
import com.plux.distribution.application.port.in.integration.SendServiceMessageUseCase;
import com.plux.distribution.infrastructure.api.integration.IntegrationController;
import com.plux.distribution.infrastructure.api.message.MessageController;
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
    public MessageController messageController(SendServiceMessageUseCase useCase) {
        return new MessageController(useCase);
    }

    @Bean
    @SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
    public IntegrationController integrationController(CrudIntegrationUseCase createUseCase) {
        return new IntegrationController(createUseCase);
    }

}
