package com.plux.distribution.core.workflow.domain;

public interface SerializerRegistry {

    <T> SerializerWithId<T> get(Class<T> type);

    <T> DataSerializer<T> findById(String id, Class<T> type);

     record SerializerWithId<T>(DataSerializer<T> serializer, String id) {}

}
