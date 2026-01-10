package com.plux.distribution.core.storage.application.service;

import com.plux.distribution.core.storage.application.dto.StoredFile;
import com.plux.distribution.core.storage.application.dto.StoredFileUrl;
import com.plux.distribution.core.storage.application.exception.ObjectAlreadyExistsException;
import com.plux.distribution.core.storage.application.port.out.StoragePort;
import com.plux.distribution.core.storage.domain.FileMetadata;
import com.plux.distribution.core.storage.domain.StorageKey;
import java.net.URI;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.ByteArrayInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.time.Duration;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class StorageServiceTest {

    @Mock
    private StoragePort storagePort;

    private StorageService storageService;
    private Duration defaultUrlTtl;

    @BeforeEach
    void setUp() {
        defaultUrlTtl = Duration.ofHours(1);
        storageService = new StorageService(storagePort, defaultUrlTtl);
    }

    @Test
    void store_withValidData_shouldReturnStorageKey() {
        // Arrange
        String basePath = "uploads";
        InputStream data = new ByteArrayInputStream("test content".getBytes());
        String contentType = "text/plain";
        long size = 12L;

        // Act
        StorageKey result = storageService.store(basePath, data, contentType, size);

        // Assert
        assertNotNull(result);
        assertTrue(result.path().startsWith("uploads/"));
        assertTrue(result.path().endsWith(".txt"));
        verify(storagePort).save(anyString(), eq(data), eq(contentType), eq(size));
    }

    @Test
    void store_withJsonContentType_shouldGenerateCorrectExtension() {
        // Arrange
        String basePath = "data";
        InputStream data = new ByteArrayInputStream("{\"key\": \"value\"}".getBytes());
        String contentType = "application/json";
        long size = 16L;

        // Act
        StorageKey result = storageService.store(basePath, data, contentType, size);

        // Assert
        assertNotNull(result);
        assertTrue(result.path().startsWith("data/"));
        assertTrue(result.path().endsWith(".json"));
        verify(storagePort).save(anyString(), eq(data), eq(contentType), eq(size));
    }

    @Test
    void store_withBinaryContentType_shouldWork() {
        // Arrange
        String basePath = "files";
        InputStream data = new ByteArrayInputStream("binary data".getBytes());
        String contentType = "application/octet-stream";
        long size = 11L;

        // Act
        StorageKey result = storageService.store(basePath, data, contentType, size);

        // Assert
        assertNotNull(result);
        assertTrue(result.path().startsWith("files/"));
        verify(storagePort).save(anyString(), eq(data), eq(contentType), eq(size));
    }

    @Test
    void store_withObjectAlreadyExistsException_shouldRetry() {
        // Arrange
        String basePath = "test";
        InputStream data = new ByteArrayInputStream("content".getBytes());
        String contentType = "text/plain";
        long size = 7L;

        doThrow(new ObjectAlreadyExistsException("Object already exists"))
            .doNothing()
            .when(storagePort).save(anyString(), any(InputStream.class), anyString(), anyLong());

        // Act
        StorageKey result = storageService.store(basePath, data, contentType, size);

        // Assert
        assertNotNull(result);
        verify(storagePort, times(2)).save(anyString(), eq(data), eq(contentType), eq(size));
    }

    @Test
    void store_withPathNormalization_shouldHandleLeadingTrailingSlashes() {
        // Arrange
        String basePath = "/uploads/";
        InputStream data = new ByteArrayInputStream("test".getBytes());
        String contentType = "text/plain";
        long size = 4L;

        // Act
        StorageKey result = storageService.store(basePath, data, contentType, size);

        // Assert
        assertNotNull(result);
        assertTrue(result.path().startsWith("uploads/"));
        assertFalse(result.path().startsWith("/"));
        verify(storagePort).save(anyString(), eq(data), eq(contentType), eq(size));
    }

    @Test
    void store_withEmptyBasePath_shouldGenerateValidPath() {
        // Arrange
        String basePath = "";
        InputStream data = new ByteArrayInputStream("content".getBytes());
        String contentType = "text/plain";
        long size = 7L;

        // Act
        StorageKey result = storageService.store(basePath, data, contentType, size);

        // Assert
        assertNotNull(result);
        assertTrue(result.path().startsWith("/"));
        verify(storagePort).save(anyString(), eq(data), eq(contentType), eq(size));
    }

    @Test
    void load_withExistingKey_shouldReturnStoredFile() throws FileNotFoundException {
        // Arrange
        String key = "existing/file.txt";
        StorageKey storageKey = new StorageKey(key);
        InputStream expectedData = new ByteArrayInputStream("test content".getBytes());
        FileMetadata expectedMetadata = new FileMetadata("text/plain", 12L);

        when(storagePort.load(key)).thenReturn(expectedData);
        when(storagePort.loadMetadata(key)).thenReturn(expectedMetadata);

        // Act
        StoredFile result = storageService.load(storageKey);

        // Assert
        assertNotNull(result);
        assertEquals(expectedData, result.data());
        assertEquals(expectedMetadata, result.metadata());
        //noinspection resource
        verify(storagePort).load(key);
        verify(storagePort).loadMetadata(key);
    }

    @Test
    void load_withNonExistingKey_shouldThrowException() throws IOException {
        // Arrange
        String key = "nonexistent/file.txt";
        StorageKey storageKey = new StorageKey(key);

        when(storagePort.load(key)).thenThrow(new FileNotFoundException("File not found"));

        // Act & Assert
        assertThrows(FileNotFoundException.class, () -> storageService.load(storageKey));
    }

    @Test
    void delete_withKey_shouldCallStoragePort() {
        // Arrange
        String key = "file/to/delete.txt";
        StorageKey storageKey = new StorageKey(key);

        // Act
        storageService.delete(storageKey);

        // Assert
        verify(storagePort).delete(key);
    }

    @Test
    void generateUrl_withExistingKey_shouldReturnFileUrl() throws Exception {
        // Arrange
        String key = "file/with/url.txt";
        StorageKey storageKey = new StorageKey(key);
        URL expectedUrl = URI.create("http://test-url.com/file").toURL();
        FileMetadata expectedMetadata = new FileMetadata("text/plain", 100L);

        when(storagePort.generateUrl(key, defaultUrlTtl)).thenReturn(expectedUrl);
        when(storagePort.loadMetadata(key)).thenReturn(expectedMetadata);

        // Act
        StoredFileUrl result = storageService.generateUrl(storageKey);

        // Assert
        assertNotNull(result);
        assertEquals(expectedUrl, result.url());
        assertEquals(expectedMetadata, result.metadata());
        verify(storagePort).generateUrl(key, defaultUrlTtl);
        verify(storagePort).loadMetadata(key);
    }
}
