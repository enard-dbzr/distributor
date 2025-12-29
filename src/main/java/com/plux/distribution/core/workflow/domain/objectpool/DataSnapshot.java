package com.plux.distribution.core.workflow.domain.objectpool;

import com.fasterxml.jackson.databind.JsonNode;

public record DataSnapshot(String serializerId, JsonNode node) {}
