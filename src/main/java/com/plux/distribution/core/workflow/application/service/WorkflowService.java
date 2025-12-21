package com.plux.distribution.core.workflow.application.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.plux.distribution.core.chat.domain.ChatId;
import com.plux.distribution.core.workflow.application.frame.utils.RootFrame;
import com.plux.distribution.core.workflow.application.frame.utils.RootFrame.RootFrameFactory;
import com.plux.distribution.core.workflow.application.port.in.WorkflowUseCase;
import com.plux.distribution.core.workflow.application.port.out.ContextRepositoryPort;
import com.plux.distribution.core.workflow.domain.FrameContext;
import com.plux.distribution.core.workflow.domain.FrameContextManager;
import com.plux.distribution.core.workflow.domain.FrameRegistry;
import com.plux.distribution.core.workflow.domain.FrameSnapshot;
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
    private final FrameRegistry frameRegistry;
    private final ContextRepositoryPort repository;
    private final FrameContextManager frameContextManager;
    private final TextProvider textProvider;
    private final SerializerRegistry serializerRegistry;

    private final ObjectMapper mapper = new ObjectMapper();

    private Map<UUID, JsonNode> dumpHolder = null;
    private FrameSnapshot rootSnapshot = null;
    private RootFrameFactory rootFrameFactory = new RootFrameFactory();

    public WorkflowService(FrameRegistry frameRegistry, ContextRepositoryPort repository,
            FrameContextManager frameContextManager, TextProvider textProvider, SerializerRegistry serializerRegistry) {
        this.frameRegistry = frameRegistry;
        this.repository = repository;
        this.frameContextManager = frameContextManager;
        this.textProvider = textProvider;
        this.serializerRegistry = serializerRegistry;


    }

    public void save(@NotNull FrameContext frameContext) {
        rootSnapshot = rootFrameFactory.save(frameContext, frameContext.getRoot());
        var objectPool = frameContext.getObjectPool();
        dumpHolder = objectPool.dump();
//        var snapshot = frameContext.save();
//
//        // data saving
//        var data = snapshot.data();
//        Map<String, DataEntryHolder> serializedData = new ConcurrentHashMap<>();
//        for (var dataEntry : data.entrySet()) {
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

        var context = new FrameContext(frameContextManager, textProvider, frameRegistry, chatId, objectPool);
        RootFrame root;
        if (rootSnapshot == null) {
            root = new RootFrame(context);
        } else {
            root = rootFrameFactory.create(context);
            rootFrameFactory.restore(context, root, rootSnapshot);
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
//        Map<Class<?>, Object> data = new HashMap<>();
//        for (var dataHolder : snapshotHolder.data().entrySet()) {
//            var key = dataRegistry.keyById(dataHolder.getKey());
//
//            if (key == null) {
//                log.error("Not found serializer with id={}", dataHolder.getKey());
//                continue;
//            }
//
//            try {
//                var value = key.serializer().deserialize(dataHolder.getValue().value());
//                data.put(key.type(), value);
//            } catch (DataProcessingException e) {
//                log.error("Failed to deserialize data entry: {}", key);
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
//        context.restore(new ContextSnapshot(data, stack));
//
//        return context;
    }

}
