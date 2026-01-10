package com.plux.distribution.infrastructure.persistence;

import com.plux.distribution.core.storage.application.exception.ObjectAlreadyExistsException;
import com.plux.distribution.core.storage.application.port.out.StoragePort;
import com.plux.distribution.core.storage.domain.FileMetadata;
import io.minio.GetObjectArgs;
import io.minio.GetPresignedObjectUrlArgs;
import io.minio.MinioClient;
import io.minio.PutObjectArgs;
import io.minio.RemoveObjectArgs;
import io.minio.StatObjectArgs;
import io.minio.StatObjectResponse;
import io.minio.errors.ErrorResponseException;
import io.minio.http.Method;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.net.URI;
import java.net.URL;
import java.time.Duration;
import java.util.Map;
import org.jetbrains.annotations.NotNull;

public class MinioStorage implements StoragePort {

    private final @NotNull MinioClient client;
    private final @NotNull String bucket;

    public MinioStorage(@NotNull String endpoint, @NotNull String accessKey,
            @NotNull String secretKey, @NotNull String bucket) {
        this.client = MinioClient.builder()
                .endpoint(endpoint)
                .credentials(accessKey, secretKey)
                .build();
        this.bucket = bucket;
    }

    @Override
    public void save(@NotNull String key, @NotNull InputStream data, @NotNull String contentType, long size)
            throws ObjectAlreadyExistsException {
        try {
            client.putObject(PutObjectArgs.builder()
                    .bucket(bucket)
                    .object(key)
                    .stream(data, size, -1)
                    .contentType(contentType)
                    .headers(Map.of("If-None-Match", "*"))
                    .build());
        } catch (ErrorResponseException e) {
            if (e.response().code() == 412) {
                throw new ObjectAlreadyExistsException("Object %s already exists".formatted(key));
            }
            throw new RuntimeException(e);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public @NotNull InputStream load(@NotNull String key) throws FileNotFoundException {
        try {
            return client.getObject(GetObjectArgs.builder()
                    .bucket(bucket)
                    .object(key)
                    .build());
        } catch (ErrorResponseException e) {
            if (e.response().code() == 404) {
                throw new FileNotFoundException("File not found: " + key);
            }
            throw new RuntimeException(e);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public @NotNull FileMetadata loadMetadata(@NotNull String key) throws FileNotFoundException {
        try {
            StatObjectResponse stat = client.statObject(StatObjectArgs.builder()
                    .bucket(bucket)
                    .object(key)
                    .build());

            return new FileMetadata(
                    stat.contentType(),
                    stat.size()
            );
        } catch (ErrorResponseException e) {
            if (e.response().code() == 404) {
                throw new FileNotFoundException("File not found: " + key);
            }
            throw new RuntimeException(e);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void delete(@NotNull String key) {
        try {
            client.removeObject(RemoveObjectArgs.builder()
                    .bucket(bucket)
                    .object(key)
                    .build());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public @NotNull URL generateUrl(String key, Duration ttl) {
        try {
            return URI.create(client.getPresignedObjectUrl(GetPresignedObjectUrlArgs.builder()
                    .bucket(bucket)
                    .object(key)
                    .method(Method.GET)
                    .expiry((int) ttl.getSeconds())
                    .build())).toURL();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
