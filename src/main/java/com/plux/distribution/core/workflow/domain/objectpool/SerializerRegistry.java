package com.plux.distribution.core.workflow.domain.objectpool;

import com.plux.distribution.core.workflow.domain.frame.DataSerializer;
import org.jetbrains.annotations.NotNull;

public interface SerializerRegistry {

    <T> SerializerWithId<T> get(@NotNull Class<T> type);

    <T> DataSerializer<T> findById(@NotNull String id, @NotNull Class<T> type);

     record SerializerWithId<T>(DataSerializer<T> serializer, String id) {}

}
