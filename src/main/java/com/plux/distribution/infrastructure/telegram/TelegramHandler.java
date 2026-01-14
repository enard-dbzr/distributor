package com.plux.distribution.infrastructure.telegram;

import com.plux.distribution.core.chat.application.port.in.CreateChatUseCase;
import com.plux.distribution.core.interaction.application.command.DeliverInteractionCommand;
import com.plux.distribution.core.interaction.application.port.in.InteractionDeliveryUseCase;
import com.plux.distribution.core.interaction.domain.Participant.BotParticipant;
import com.plux.distribution.core.interaction.domain.Participant.ChatParticipant;
import com.plux.distribution.core.interaction.domain.content.ButtonClickContent;
import com.plux.distribution.core.interaction.domain.content.InteractionContent;
import com.plux.distribution.core.interaction.domain.content.MessageAttachment;
import com.plux.distribution.core.interaction.domain.content.MessageAttachment.MediaAttachment;
import com.plux.distribution.core.interaction.domain.content.MessageAttachment.MediaAttachment.DisplayType;
import com.plux.distribution.core.interaction.domain.content.ReplyMessageContent;
import com.plux.distribution.core.interaction.domain.content.SimpleMessageContent;
import com.plux.distribution.core.mediastorage.application.port.in.UploadMediaUseCase;
import com.plux.distribution.core.mediastorage.domain.MediaId;
import com.plux.distribution.infrastructure.telegram.port.TgChatLinker;
import com.plux.distribution.infrastructure.telegram.port.TgMessageGlobalId;
import com.plux.distribution.infrastructure.telegram.port.TgMessageLinker;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import org.jetbrains.annotations.NotNull;
import org.telegram.telegrambots.longpolling.util.LongPollingSingleThreadUpdateConsumer;
import org.telegram.telegrambots.meta.api.methods.GetFile;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.Document;
import org.telegram.telegrambots.meta.api.objects.File;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.message.Message;
import org.telegram.telegrambots.meta.api.objects.photo.PhotoSize;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.meta.generics.TelegramClient;

public class TelegramHandler implements LongPollingSingleThreadUpdateConsumer {

    private final @NotNull TelegramClient telegramClient;
    private final @NotNull InteractionDeliveryUseCase interactionDeliveryUseCase;
    private final @NotNull CreateChatUseCase createChatUseCase;
    private final @NotNull UploadMediaUseCase uploadMediaUseCase;
    private final @NotNull TgMessageLinker tgMessageLinker;
    private final @NotNull TgChatLinker tgChatLinker;

    public TelegramHandler(@NotNull TelegramClient telegramClient,
            @NotNull InteractionDeliveryUseCase interactionDeliveryUseCase,
            @NotNull CreateChatUseCase createChatUseCase, @NotNull UploadMediaUseCase uploadMediaUseCase,
            @NotNull TgMessageLinker tgMessageLinker, @NotNull TgChatLinker tgChatLinker) {
        this.telegramClient = telegramClient;
        this.interactionDeliveryUseCase = interactionDeliveryUseCase;
        this.createChatUseCase = createChatUseCase;
        this.uploadMediaUseCase = uploadMediaUseCase;
        this.tgMessageLinker = tgMessageLinker;
        this.tgChatLinker = tgChatLinker;
    }


    @Override
    public void consume(Update update) {

        if (update.hasMessage()) {
            processMessage(update.getMessage());

            return;
        }

        if (update.hasCallbackQuery()) {
            processCallback(update.getCallbackQuery());
        }
    }

    private void processMessage(Message message) {
        var chatId = tgChatLinker.getChatId(message.getChatId());

        if (chatId == null) {
            chatId = createChatUseCase.create().id();

            tgChatLinker.link(chatId, message.getChatId());
        }

        List<MessageAttachment> attachments = new ArrayList<>();
        attachments.addAll(processPhotos(message));
        attachments.addAll(processDocument(message));

        InteractionContent content = new SimpleMessageContent(
                Optional.ofNullable(message.getText()).orElse(""),
                attachments
        );

        if (message.isReply()) {
            var replyTo = tgMessageLinker.getInteractionId(
                    new TgMessageGlobalId(
                            message.getReplyToMessage().getMessageId(),
                            message.getChatId()
                    )
            );

            content = new ReplyMessageContent(content, replyTo);
        }

        var command = new DeliverInteractionCommand(
                new ChatParticipant(chatId),
                new BotParticipant(),
                content
        );

        var messageId = interactionDeliveryUseCase.deliver(command);

        tgMessageLinker.link(messageId, new TgMessageGlobalId(message.getMessageId(), message.getChatId()));
    }

    private List<MessageAttachment> processPhotos(Message message) {
        if (message.getPhoto() == null || message.getPhoto().isEmpty()) {
            return List.of();
        }

        var largestPhoto = message.getPhoto().stream()
                .max(Comparator.comparingInt(PhotoSize::getFileSize))
                .orElseThrow();

        MediaId mediaId = downloadAndUpload(
                largestPhoto.getFileId(),
                "image/jpeg",
                largestPhoto.getFileSize()
        );

        return List.of(new MediaAttachment(mediaId, DisplayType.PHOTO));
    }

    private List<MessageAttachment> processDocument(Message message) {
        if (!message.hasDocument()) {
            return List.of();
        }

        Document doc = message.getDocument();

        MediaId mediaId = downloadAndUpload(
                doc.getFileId(),
                Optional.ofNullable(doc.getMimeType()).orElse("application/octet-stream"),
                doc.getFileSize()
        );

        return List.of(new MediaAttachment(mediaId, DisplayType.DOCUMENT));
    }

    private MediaId downloadAndUpload(String fileId, String mimeType, long fileSize) {
        try {
            File file = telegramClient.execute(new GetFile(fileId));

            try (InputStream inputStream = telegramClient.downloadFileAsStream(file)) {
                System.out.println(file.getFilePath());
                return uploadMediaUseCase.upload(
                        inputStream,
                        mimeType,
                        file.getFilePath(),
                        fileSize,
                        "in_chat"
                );
            }

        } catch (TelegramApiException | IOException e) {
            throw new RuntimeException(e);
        }

    }

    private void processCallback(CallbackQuery callbackQuery) {
        var tag = callbackQuery.getData();

        var tgChatId = callbackQuery.getMessage().getChatId();
        var tgMessageId = callbackQuery.getMessage().getMessageId();

        var chatId = Optional.ofNullable(tgChatLinker.getChatId(tgChatId)).orElseThrow();
        var interactionId = tgMessageLinker.getInteractionId(new TgMessageGlobalId(tgMessageId, tgChatId));

        interactionDeliveryUseCase.deliver(new DeliverInteractionCommand(
                new ChatParticipant(chatId),
                new BotParticipant(),
                new ButtonClickContent(interactionId, tag)
        ));
    }
}
