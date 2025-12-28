package com.plux.distribution.core.workflow.application.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.plux.distribution.core.chat.domain.ChatId;
import com.plux.distribution.core.workflow.application.frame.utils.RootFrame;
import com.plux.distribution.core.workflow.application.frame.utils.RootFrame.RootFrameFactory;
import com.plux.distribution.core.workflow.application.port.in.CheckWorkflowIsEmptyUseCase;
import com.plux.distribution.core.workflow.application.port.in.WorkflowUseCase;
import com.plux.distribution.core.workflow.application.port.out.ContextRepositoryPort;
import com.plux.distribution.core.workflow.domain.FrameContext;
import com.plux.distribution.core.workflow.domain.FrameContextManager;
import com.plux.distribution.core.workflow.domain.frame.TextProvider;
import com.plux.distribution.core.workflow.domain.objectpool.DataSnapshot;
import com.plux.distribution.core.workflow.domain.objectpool.ObjectPool;
import com.plux.distribution.core.workflow.domain.objectpool.SerializerRegistry;
import java.util.Map;
import java.util.UUID;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jetbrains.annotations.NotNull;

public class WorkflowService implements WorkflowUseCase, CheckWorkflowIsEmptyUseCase {

    private static final Logger log = LogManager.getLogger(WorkflowService.class);
    private final ContextRepositoryPort repository;
    private final FrameContextManager frameContextManager;
    private final TextProvider textProvider;
    private final SerializerRegistry serializerRegistry;

    private final ObjectMapper mapper = new ObjectMapper();

    private final RootFrameFactory rootFrameFactory = new RootFrameFactory();

    public WorkflowService(ContextRepositoryPort repository,
            FrameContextManager frameContextManager, TextProvider textProvider, SerializerRegistry serializerRegistry) {
        this.repository = repository;
        this.frameContextManager = frameContextManager;
        this.textProvider = textProvider;
        this.serializerRegistry = serializerRegistry;


    }

    public void save(@NotNull FrameContext frameContext) {
        var rootSnapshot = rootFrameFactory.serialize(frameContext, frameContext.getRoot());
        var objectPool = frameContext.getObjectPool();
        var objectPoolDump = objectPool.dump();

        MachineSnapshot machineSnapshot = new MachineSnapshot(rootSnapshot, objectPoolDump);

        try {
            var serializedValue = mapper.writeValueAsString(machineSnapshot);

            repository.save(frameContext.getChatId(), serializedValue);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    public @NotNull FrameContext load(@NotNull ChatId chatId) {

        ObjectPool objectPool = new ObjectPool(serializerRegistry);

        FrameContext context = new FrameContext(frameContextManager, textProvider, chatId, objectPool);

        var serializedSnapshot = repository.getSnapshot(chatId);

        if (serializedSnapshot == null || serializedSnapshot.isEmpty()) {
            context.setRoot(rootFrameFactory.create(context));

            return context;
        }

        MachineSnapshot machineSnapshot;
        try {
            machineSnapshot = mapper.readValue(serializedSnapshot, MachineSnapshot.class);
        } catch (JsonProcessingException e) {
            log.error("Failed to deserialize frame context, returning empty", e);
            context.setRoot(rootFrameFactory.create(context));
            return context;
        }

        objectPool.load(machineSnapshot.poolDump());

        RootFrame root = rootFrameFactory.create(context, machineSnapshot.rootSnapshot());

        context.setRoot(root);

        return context;
    }

    @Override
    public boolean isEmpty(@NotNull ChatId chatId) {
        return load(chatId).getRoot().isEmpty();
    }

    private record MachineSnapshot(JsonNode rootSnapshot, Map<UUID, DataSnapshot> poolDump) {}

}
