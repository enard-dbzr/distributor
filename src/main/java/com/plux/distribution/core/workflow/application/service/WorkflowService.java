package com.plux.distribution.core.workflow.application.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.plux.distribution.core.chat.domain.ChatId;
import com.plux.distribution.core.workflow.application.frame.utils.RootFrame;
import com.plux.distribution.core.workflow.application.frame.utils.RootFrame.RootFrameFactory;
import com.plux.distribution.core.workflow.application.port.in.WorkflowUseCase;
import com.plux.distribution.core.workflow.application.port.out.ContextRepositoryPort;
import com.plux.distribution.core.workflow.domain.DataSnapshot;
import com.plux.distribution.core.workflow.domain.FrameContext;
import com.plux.distribution.core.workflow.domain.FrameContextManager;
import com.plux.distribution.core.workflow.domain.ObjectPool;
import com.plux.distribution.core.workflow.domain.SerializerRegistry;
import com.plux.distribution.core.workflow.domain.TextProvider;
import java.util.Map;
import java.util.UUID;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jetbrains.annotations.NotNull;

public class WorkflowService implements WorkflowUseCase {

    private static final Logger log = LogManager.getLogger(WorkflowService.class);
    private final ContextRepositoryPort repository;
    private final FrameContextManager frameContextManager;
    private final TextProvider textProvider;
    private final SerializerRegistry serializerRegistry;

    private final ObjectMapper mapper = new ObjectMapper();

    private Map<UUID, DataSnapshot> dumpHolder = null;
    private JsonNode rootSnapshot = null;
    private RootFrameFactory rootFrameFactory = new RootFrameFactory();

    public WorkflowService(ContextRepositoryPort repository,
            FrameContextManager frameContextManager, TextProvider textProvider, SerializerRegistry serializerRegistry) {
        this.repository = repository;
        this.frameContextManager = frameContextManager;
        this.textProvider = textProvider;
        this.serializerRegistry = serializerRegistry;


    }

    public void save(@NotNull FrameContext frameContext) {
        rootSnapshot = rootFrameFactory.serialize(frameContext, frameContext.getRoot());
        var objectPool = frameContext.getObjectPool();
        dumpHolder = objectPool.dump();
//        var snapshot = frameContext.save();
//
//        // node saving
//        var node = snapshot.node();
//        Map<String, DataEntryHolder> serializedData = new ConcurrentHashMap<>();
//        for (var dataEntry : node.entrySet()) {
//            var key = dataRegistry.keyByType(dataEntry.getKey());
//            var value = dataEntry.getValue();
//
//            serializedData.put(key.id(), new DataEntryHolder(serializeDataEntry(key, value)));
//        }
//
//        // context saving
//        List<FrameEntryHolder> stack = new ArrayList<>();
//        for (var frameEntry : snapshot.stack()) {
//            stack.addLast(new FrameEntryHolder(
//                    frameRegistry.idByType(frameEntry.frame().getClass()),
//                    frameEntry.execute()
//            ));
//        }
//
//        var mainHolder = new ContextSnapshotHolder(stack, serializedData);
//
//        try {
//            var serializedValue = mapper.writeValueAsString(mainHolder);
//
//            repository.save(frameContext.getChatId(), serializedValue);
//        } catch (JsonProcessingException e) {
//            throw new RuntimeException(e);
//        }
    }

    public @NotNull FrameContext load(@NotNull ChatId chatId) {

        var objectPool = new ObjectPool(serializerRegistry);
        if (dumpHolder != null) {
            objectPool.load(dumpHolder);
        }

        var context = new FrameContext(frameContextManager, textProvider, chatId, objectPool);
        RootFrame root;
        if (rootSnapshot == null) {
            root = new RootFrame(null);
        } else {
            root = rootFrameFactory.create(context, rootSnapshot);
        }

        context.setRoot(root);

        return context;

//        var serializedSnapshot = repository.getSnapshot(chatId);

//        if (serializedSnapshot == null || serializedSnapshot.isEmpty()) {
//            return new FrameContext(frameContextManager, textProvider, chatId);
//        }

//        ContextSnapshotHolder snapshotHolder;
//        try {
//            snapshotHolder = mapper.readValue(serializedSnapshot, ContextSnapshotHolder.class);
//        } catch (JsonProcessingException e) {
//            log.error("Failed to deserialize frame context, returning empty", e);
//            return new FrameContext(frameContextManager, textProvider, chatId);
//        }
//
//        Map<Class<?>, Object> node = new HashMap<>();
//        for (var dataHolder : snapshotHolder.node().entrySet()) {
//            var key = dataRegistry.keyById(dataHolder.getKey());
//
//            if (key == null) {
//                log.error("Not found serializer with id={}", dataHolder.getKey());
//                continue;
//            }
//
//            try {
//                var value = key.serializer().deserialize(dataHolder.getValue().value());
//                node.put(key.type(), value);
//            } catch (DataProcessingException e) {
//                log.error("Failed to deserialize node entry: {}", key);
//            }
//
//
//        }
//
//        List<FrameEntry> stack = new ArrayList<>();
//        for (var frameHolder : snapshotHolder.stack()) {
//            stack.addLast(new FrameEntry(
//                    frameRegistry.get(frameHolder.frameId()),
//                    frameHolder.execute()
//            ));
//        }
//
//        var context = new FrameContext(frameContextManager, textProvider, chatId);
//        context.restore(new ContextSnapshot(node, stack));
//
//        return context;
    }

}
