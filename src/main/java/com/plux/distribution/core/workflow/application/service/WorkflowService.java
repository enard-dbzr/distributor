package com.plux.distribution.core.workflow.application.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.plux.distribution.core.workflow.application.exception.DataProcessingException;
import com.plux.distribution.core.workflow.application.port.in.WorkflowUseCase;
import com.plux.distribution.core.workflow.application.port.out.ContextRepositoryPort;
import com.plux.distribution.core.workflow.application.port.out.DataKey;
import com.plux.distribution.core.workflow.application.port.out.DataRegistry;
import com.plux.distribution.core.workflow.application.port.out.FrameRegistry;
import com.plux.distribution.core.workflow.domain.FrameContext;
import com.plux.distribution.core.workflow.domain.FrameContext.ContextSnapshot;
import com.plux.distribution.core.workflow.domain.FrameContext.FrameEntry;
import com.plux.distribution.core.workflow.domain.FrameContextManager;
import com.plux.distribution.core.chat.domain.ChatId;
import com.plux.distribution.core.workflow.domain.TextProvider;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jetbrains.annotations.NotNull;

public class WorkflowService implements WorkflowUseCase {

    private static final Logger log = LogManager.getLogger(WorkflowService.class);
    private final FrameRegistry frameRegistry;
    private final DataRegistry dataRegistry;
    private final ContextRepositoryPort repository;
    private final FrameContextManager frameContextManager;
    private final TextProvider textProvider;

    private final ObjectMapper mapper = new ObjectMapper();

    public WorkflowService(FrameRegistry frameRegistry, DataRegistry dataRegistry, ContextRepositoryPort repository,
            FrameContextManager frameContextManager, TextProvider textProvider) {
        this.frameRegistry = frameRegistry;
        this.dataRegistry = dataRegistry;
        this.repository = repository;
        this.frameContextManager = frameContextManager;
        this.textProvider = textProvider;
    }

    public void save(@NotNull FrameContext frameContext) {
        var snapshot = frameContext.save();

        // data saving
        var data = snapshot.data();
        Map<String, DataEntryHolder> serializedData = new ConcurrentHashMap<>();
        for (var dataEntry : data.entrySet()) {
            var key = dataRegistry.keyByType(dataEntry.getKey());
            var value = dataEntry.getValue();

            serializedData.put(key.id(), new DataEntryHolder(serializeDataEntry(key, value)));
        }

        // context saving
        List<FrameEntryHolder> stack = new ArrayList<>();
        for (var frameEntry : snapshot.stack()) {
            stack.addLast(new FrameEntryHolder(
                    frameRegistry.idByType(frameEntry.frame().getClass()),
                    frameEntry.execute()
            ));
        }

        var mainHolder = new ContextSnapshotHolder(stack, serializedData);

        try {
            var serializedValue = mapper.writeValueAsString(mainHolder);

            repository.save(frameContext.getChatId(), serializedValue);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    public @NotNull FrameContext load(@NotNull ChatId chatId) {
        var serializedSnapshot = repository.getSnapshot(chatId);

        if (serializedSnapshot == null || serializedSnapshot.isEmpty()) {
            return new FrameContext(frameContextManager, textProvider, chatId);
        }

        ContextSnapshotHolder snapshotHolder;
        try {
            snapshotHolder = mapper.readValue(serializedSnapshot, ContextSnapshotHolder.class);
        } catch (JsonProcessingException e) {
            log.error("Failed to deserialize frame context, returning empty", e);
            return new FrameContext(frameContextManager, textProvider, chatId);
        }

        Map<Class<?>, Object> data = new HashMap<>();
        for (var dataHolder : snapshotHolder.data().entrySet()) {
            var key = dataRegistry.keyById(dataHolder.getKey());
            try {
                var value = key.serializer().deserialize(dataHolder.getValue().value());
                data.put(key.type(), value);
            } catch (DataProcessingException e) {
                log.error("Failed to deserialize data entry: {}", key, e);
            }


        }

        List<FrameEntry> stack = new ArrayList<>();
        for (var frameHolder : snapshotHolder.stack()) {
            stack.addLast(new FrameEntry(
                    frameRegistry.get(frameHolder.frameId()),
                    frameHolder.execute()
            ));
        }

        var context = new FrameContext(frameContextManager, textProvider, chatId);
        context.restore(new ContextSnapshot(data, stack));

        return context;
    }

    private <T> JsonNode serializeDataEntry(DataKey<T> key, Object value) {
        return key.serializer().serialize(key.type().cast(value));
    }

    private record FrameEntryHolder(@NotNull String frameId, boolean execute) {

    }

    private record DataEntryHolder(
            @NotNull JsonNode value
    ) {

    }

    private record ContextSnapshotHolder(
            @NotNull List<FrameEntryHolder> stack,
            @NotNull Map<String, DataEntryHolder> data
    ) {

    }
}
