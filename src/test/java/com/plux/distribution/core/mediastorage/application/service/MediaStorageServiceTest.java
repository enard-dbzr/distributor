package com.plux.distribution.core.mediastorage.application.service;

import com.plux.distribution.core.mediastorage.application.exception.DuplicateMediaIdException;
import com.plux.distribution.core.mediastorage.application.exception.MediaNotFoundException;
import com.plux.distribution.core.mediastorage.application.port.out.MediaRepositoryPort;
import com.plux.distribution.core.mediastorage.domain.Media;
import com.plux.distribution.core.mediastorage.domain.MediaId;
import com.plux.distribution.core.storage.application.dto.StoredFile;
import com.plux.distribution.core.storage.application.dto.StoredFileUrl;
import com.plux.distribution.core.storage.application.port.in.DeleteFileUseCase;
import com.plux.distribution.core.storage.application.port.in.GenerateFileUrlUseCase;
import com.plux.distribution.core.storage.application.port.in.LoadFileUseCase;
import com.plux.distribution.core.storage.application.port.in.StoreFileUseCase;
import com.plux.distribution.core.storage.domain.FileMetadata;
import com.plux.distribution.core.storage.domain.StorageKey;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.ByteArrayInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class MediaStorageServiceTest {

    private static final String TEST_CONTENT = "test content";
    private static final String CONTENT_TYPE_TEXT = "text/plain";
    private static final long TEST_FILE_SIZE = 12L;

    private static final String STORAGE_KEY = "media/default/file.txt";
    private static final String BASE_PATH_DEFAULT = "media/default";

    @Mock
    private StoreFileUseCase storeFileUseCase;

    @Mock
    private LoadFileUseCase loadFileUseCase;

    @Mock
    private DeleteFileUseCase deleteFileUseCase;

    @Mock
    private GenerateFileUrlUseCase generateFileUrlUseCase;

    @Mock
    private MediaRepositoryPort mediaRepository;

    private MediaStorageService mediaStorageService;

    @BeforeEach
    void setUp() {
        mediaStorageService = new MediaStorageService(
                storeFileUseCase,
                loadFileUseCase,
                deleteFileUseCase,
                generateFileUrlUseCase,
                mediaRepository
        );
    }

    @Test
    void upload_withValidData_shouldReturnMediaId() {
        // Arrange
        InputStream data = new ByteArrayInputStream(TEST_CONTENT.getBytes());
        String contentType = CONTENT_TYPE_TEXT;
        long size = TEST_FILE_SIZE;
        String scope = "documents";

        StorageKey storageKey = new StorageKey(STORAGE_KEY);
        String basePathDocuments = "media/documents";

        when(storeFileUseCase.store(anyString(), eq(data), eq(contentType), eq(size)))
                .thenReturn(storageKey);

        // Act
        MediaId result = mediaStorageService.upload(data, contentType, size, scope);

        // Assert
        assertNotNull(result);
        verify(storeFileUseCase).store(eq(basePathDocuments), eq(data), eq(contentType), eq(size));
        verify(mediaRepository).save(any(Media.class));
    }

    @Test
    void upload_withNullScope_shouldUseDefaultScope() {
        // Arrange
        InputStream data = new ByteArrayInputStream(TEST_CONTENT.getBytes());
        String contentType = CONTENT_TYPE_TEXT;
        long size = TEST_FILE_SIZE;
        String scope = null;

        StorageKey storageKey = new StorageKey(STORAGE_KEY);

        when(storeFileUseCase.store(anyString(), eq(data), eq(contentType), eq(size)))
                .thenReturn(storageKey);

        // Act
        MediaId result = mediaStorageService.upload(data, contentType, size, scope);

        // Assert
        assertNotNull(result);
        verify(storeFileUseCase).store(eq(BASE_PATH_DEFAULT), eq(data), eq(contentType), eq(size));
        verify(mediaRepository).save(any(Media.class));
    }

    @Test
    void upload_withEmptyScope_shouldUseDefaultScope() {
        // Arrange
        InputStream data = new ByteArrayInputStream(TEST_CONTENT.getBytes());
        String contentType = CONTENT_TYPE_TEXT;
        long size = TEST_FILE_SIZE;
        String scope = "";

        StorageKey storageKey = new StorageKey(STORAGE_KEY);

        when(storeFileUseCase.store(anyString(), eq(data), eq(contentType), eq(size)))
                .thenReturn(storageKey);

        // Act
        MediaId result = mediaStorageService.upload(data, contentType, size, scope);

        // Assert
        assertNotNull(result);
        verify(storeFileUseCase).store(eq(BASE_PATH_DEFAULT), eq(data), eq(contentType), eq(size));
        verify(mediaRepository).save(any(Media.class));
    }

    @Test
    void upload_withDuplicateMediaId_shouldRetry() {
        // Arrange
        InputStream data = new ByteArrayInputStream(TEST_CONTENT.getBytes());
        String contentType = CONTENT_TYPE_TEXT;
        long size = TEST_FILE_SIZE;
        StorageKey storageKey = new StorageKey(STORAGE_KEY);
        MediaId firstId = MediaId.generate();

        when(storeFileUseCase.store(anyString(), eq(data), eq(contentType), eq(size)))
                .thenReturn(storageKey);
        doThrow(new DuplicateMediaIdException(firstId))
                .doNothing()
                .when(mediaRepository).save(any(Media.class));

        // Act
        MediaId result = mediaStorageService.upload(data, contentType, size, null);

        // Assert
        assertNotNull(result);
        verify(storeFileUseCase).store(eq(BASE_PATH_DEFAULT), eq(data), eq(contentType), eq(size));
        verify(mediaRepository, times(2)).save(any(Media.class));
    }

    @Test
    void delete_withExistingMedia_shouldDeleteSuccessfully() throws MediaNotFoundException {
        // Arrange
        MediaId mediaId = MediaId.generate();
        StorageKey storageKey = new StorageKey(STORAGE_KEY);
        Media media = new Media(mediaId, storageKey);

        when(mediaRepository.findById(mediaId)).thenReturn(Optional.of(media));

        // Act
        mediaStorageService.delete(mediaId);

        // Assert
        verify(mediaRepository).findById(mediaId);
        verify(deleteFileUseCase).delete(storageKey);
        verify(mediaRepository).delete(mediaId);
    }

    @Test
    void delete_withNonExistingMedia_shouldThrowException() {
        // Arrange
        MediaId mediaId = MediaId.generate();

        when(mediaRepository.findById(mediaId)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(MediaNotFoundException.class, () -> mediaStorageService.delete(mediaId));
        verify(mediaRepository).findById(mediaId);
        verify(deleteFileUseCase, never()).delete(any());
        verify(mediaRepository, never()).delete(any());
    }

    @Test
    void download_withExistingMedia_shouldReturnStoredFile() throws MediaNotFoundException, FileNotFoundException {
        // Arrange
        MediaId mediaId = MediaId.generate();
        StorageKey storageKey = new StorageKey(STORAGE_KEY);
        Media media = new Media(mediaId, storageKey);

        InputStream expectedData = new ByteArrayInputStream(TEST_CONTENT.getBytes());
        FileMetadata expectedMetadata = new FileMetadata(CONTENT_TYPE_TEXT, TEST_FILE_SIZE);
        StoredFile expectedStoredFile = new StoredFile(expectedData, expectedMetadata);

        when(mediaRepository.findById(mediaId)).thenReturn(Optional.of(media));
        when(loadFileUseCase.load(storageKey)).thenReturn(expectedStoredFile);

        // Act
        StoredFile result = mediaStorageService.download(mediaId);

        // Assert
        assertNotNull(result);
        assertEquals(expectedStoredFile, result);
        verify(mediaRepository).findById(mediaId);
        verify(loadFileUseCase).load(storageKey);
    }

    @Test
    void download_withNonExistingMedia_shouldThrowException() throws IOException {
        // Arrange
        MediaId mediaId = MediaId.generate();

        when(mediaRepository.findById(mediaId)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(MediaNotFoundException.class, () -> mediaStorageService.download(mediaId));
        verify(mediaRepository).findById(mediaId);
        verify(loadFileUseCase, never()).load(any());
    }

    @Test
    void download_withFileNotFound_shouldThrowMediaNotFoundException() throws IOException {
        // Arrange
        MediaId mediaId = MediaId.generate();
        StorageKey storageKey = new StorageKey(STORAGE_KEY);
        Media media = new Media(mediaId, storageKey);

        when(mediaRepository.findById(mediaId)).thenReturn(Optional.of(media));
        when(loadFileUseCase.load(storageKey)).thenThrow(new FileNotFoundException(""));

        // Act & Assert
        assertThrows(MediaNotFoundException.class, () -> mediaStorageService.download(mediaId));
        verify(mediaRepository).findById(mediaId);
        verify(loadFileUseCase).load(storageKey);
    }

    @Test
    void generateUrl_withExistingMedia_shouldReturnStoredFileUrl() throws MediaNotFoundException, FileNotFoundException, MalformedURLException {
        // Arrange
        MediaId mediaId = MediaId.generate();
        StorageKey storageKey = new StorageKey(STORAGE_KEY);
        Media media = new Media(mediaId, storageKey);
        URL expectedUrl = URI.create("http://test-url.com/file").toURL();
        FileMetadata expectedMetadata = new FileMetadata(CONTENT_TYPE_TEXT, TEST_FILE_SIZE);
        StoredFileUrl expectedStoredFileUrl = new StoredFileUrl(expectedUrl, expectedMetadata);

        when(mediaRepository.findById(mediaId)).thenReturn(Optional.of(media));
        when(generateFileUrlUseCase.generateUrl(storageKey)).thenReturn(expectedStoredFileUrl);

        // Act
        StoredFileUrl result = mediaStorageService.generateUrl(mediaId);

        // Assert
        assertNotNull(result);
        assertEquals(expectedStoredFileUrl, result);
        verify(mediaRepository).findById(mediaId);
        verify(generateFileUrlUseCase).generateUrl(storageKey);
    }

    @Test
    void generateUrl_withNonExistingMedia_shouldThrowException() throws IOException {
        // Arrange
        MediaId mediaId = MediaId.generate();

        when(mediaRepository.findById(mediaId)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(MediaNotFoundException.class, () -> mediaStorageService.generateUrl(mediaId));
        verify(mediaRepository).findById(mediaId);
        verify(generateFileUrlUseCase, never()).generateUrl(any());
    }

    @Test
    void generateUrl_withFileNotFound_shouldThrowMediaNotFoundException() throws IOException {
        // Arrange
        MediaId mediaId = MediaId.generate();
        StorageKey storageKey = new StorageKey(STORAGE_KEY);
        Media media = new Media(mediaId, storageKey);

        when(mediaRepository.findById(mediaId)).thenReturn(Optional.of(media));
        when(generateFileUrlUseCase.generateUrl(storageKey)).thenThrow(new FileNotFoundException(""));

        // Act & Assert
        assertThrows(MediaNotFoundException.class, () -> mediaStorageService.generateUrl(mediaId));
        verify(mediaRepository).findById(mediaId);
        verify(generateFileUrlUseCase).generateUrl(storageKey);
    }
}
