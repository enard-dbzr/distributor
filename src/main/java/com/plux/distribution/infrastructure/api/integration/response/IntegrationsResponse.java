package com.plux.distribution.infrastructure.api.integration.response;

import com.plux.distribution.infrastructure.api.integration.IntegrationView;
import java.util.List;

public record IntegrationsResponse(
        List<IntegrationView> integrations
) {

}
