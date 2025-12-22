package com.plux.distribution.core.workflow.domain;

import com.fasterxml.jackson.databind.JsonNode;

public record DataSnapshot(String serializerId, JsonNode node) {}
