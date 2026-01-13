package com.plux.distribution.infrastructure.telegram.sender;

import com.plux.distribution.core.chat.domain.ChatId;
import com.plux.distribution.core.interaction.application.port.out.InteractionSenderPort;
import com.plux.distribution.core.interaction.domain.InteractionId;
import com.plux.distribution.core.interaction.domain.Participant;
import com.plux.distribution.core.interaction.domain.Participant.ChatParticipant;
import com.plux.distribution.core.interaction.domain.content.ButtonClickContent;
import com.plux.distribution.core.interaction.domain.content.InteractionContent;
import com.plux.distribution.core.interaction.domain.content.MessageAttachment.ButtonAttachment;
import com.plux.distribution.core.interaction.domain.content.MessageAttachment.MediaAttachment;
import com.plux.distribution.core.interaction.domain.content.MessageAttachment.MediaAttachment.DisplayType;
import com.plux.distribution.core.interaction.domain.content.ReplyMessageContent;
import com.plux.distribution.core.interaction.domain.content.SimpleMessageContent;
import com.plux.distribution.core.mediastorage.application.dto.StoredMedia;
import com.plux.distribution.core.mediastorage.application.exception.MediaNotFoundException;
import com.plux.distribution.core.mediastorage.application.port.in.DownloadMediaUseCase;
import com.plux.distribution.infrastructure.telegram.port.TgChatLinker;
import com.plux.distribution.infrastructure.telegram.port.TgMessageGlobalId;
import com.plux.distribution.infrastructure.telegram.port.TgMessageLinker;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import org.jetbrains.annotations.NotNull;
import org.telegram.telegrambots.meta.api.methods.send.SendDocument;
import org.telegram.telegrambots.meta.api.methods.send.SendMediaGroup;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto;
import org.telegram.telegrambots.meta.api.objects.InputFile;
import org.telegram.telegrambots.meta.api.objects.media.InputMedia;
import org.telegram.telegrambots.meta.api.objects.media.InputMediaDocument;
import org.telegram.telegrambots.meta.api.objects.media.InputMediaPhoto;
import org.telegram.telegrambots.meta.api.objects.message.Message;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardRow;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.meta.generics.TelegramClient;

public class TelegramInteractionSender implements InteractionSenderPort {

    private static final int MAX_MEDIA_GROUP_SIZE = 10;

    private final @NotNull TelegramClient client;
    private final @NotNull TgChatLinker tgChatLinker;
    private final @NotNull TgMessageLinker tgMessageLinker;
    private final @NotNull DownloadMediaUseCase downloadMediaUseCase;


    public TelegramInteractionSender(@NotNull TelegramClient client,
            @NotNull TgChatLinker tgChatLinker, @NotNull TgMessageLinker tgMessageLinker,
            @NotNull DownloadMediaUseCase downloadMediaUseCase) {
        this.client = client;
        this.tgChatLinker = tgChatLinker;
        this.tgMessageLinker = tgMessageLinker;
        this.downloadMediaUseCase = downloadMediaUseCase;
    }

    @Override
    public void send(@NotNull InteractionId interactionId, @NotNull Participant recipient,
            @NotNull InteractionContent interactionContent) {
        if (!(recipient instanceof ChatParticipant(ChatId chatId))) {
            throw new IllegalArgumentException("Not chat participants is not allowed here");
        }

        var tgChatId = tgChatLinker.getTgChatId(chatId);

        var context = new RenderingContext(tgChatId);

        constructContent(context, interactionContent);
        validateContent(context);

        try {
            Message result = sendMessage(context);
            tgMessageLinker.link(interactionId, new TgMessageGlobalId(result.getMessageId(), tgChatId));
        } catch (TelegramApiException e) {
            throw new RuntimeException(e);
        }
    }

    private void constructContent(@NotNull RenderingContext context, InteractionContent content) {
        switch (content) {
            case SimpleMessageContent c -> {
                context.text = c.text();

                for (var attachment : c.attachments()) {
                    switch (attachment) {
                        case ButtonAttachment a -> context.buttons.add(a);
                        case MediaAttachment a -> context.mediaAttachments.add(a);
                    }
                }
            }
            case ReplyMessageContent c -> {
                var tgReplyMessageId = tgMessageLinker.getTgMessageId(c.replyTo());
                context.replyToMessageId = tgReplyMessageId.messageId();
                constructContent(context, c.original());
            }
            case ButtonClickContent _ -> throw new UnsupportedOperationException();
        }
    }

    private void validateContent(RenderingContext context) {
        if (context.mediaAttachments.size() > MAX_MEDIA_GROUP_SIZE) {
            throw new UnsupportedOperationException(
                    "Cannot send more than " + MAX_MEDIA_GROUP_SIZE + " media attachments"
            );
        }

        if (context.mediaAttachments.size() > 1 && !context.buttons.isEmpty()) {
            throw new UnsupportedOperationException(
                    "Cannot send media with buttons in a grouped Telegram message"
            );
        }

        if (context.mediaAttachments.size() > 1 && hasMixedMediaTypes(context.mediaAttachments)) {
            throw new UnsupportedOperationException(
                    "Cannot mix photos and documents in a single Telegram message"
            );
        }
    }

    private boolean hasMixedMediaTypes(List<MediaAttachment> attachments) {
        boolean hasPhoto = attachments.stream().anyMatch(a -> a.displayType() == DisplayType.PHOTO);
        boolean hasDocument = attachments.stream().anyMatch(a -> a.displayType() == DisplayType.DOCUMENT);
        return hasPhoto && hasDocument;
    }

    private Message sendMessage(RenderingContext context) throws TelegramApiException {
        if (context.mediaAttachments.isEmpty()) {
            return sendTextMessage(context);
        } else if (context.mediaAttachments.size() == 1) {
            return sendSingleMedia(context);
        } else {
            return sendMediaGroup(context);
        }
    }

    private Message sendTextMessage(RenderingContext context) throws TelegramApiException {
        var builder = SendMessage.builder()
                .chatId(context.tgChatId)
                .text(Objects.requireNonNull(context.text))
                .parseMode("Markdown");

        if (context.replyToMessageId != null) {
            builder.replyToMessageId(context.replyToMessageId);
        }

        if (!context.buttons.isEmpty()) {
            builder.replyMarkup(buildKeyboard(context.buttons));
        }

        return client.execute(builder.build());
    }

    private Message sendSingleMedia(RenderingContext context) throws TelegramApiException {
        var media = context.mediaAttachments.getFirst();
        var storedFile = getMediaFile(media);

        return switch (media.displayType()) {
            case PHOTO -> {
                var builder = SendPhoto.builder()
                        .chatId(context.tgChatId)
                        .photo(new InputFile(storedFile.file().data(), storedFile.metadata().filename()))
                        .caption(context.text)
                        .parseMode("Markdown");

                if (context.replyToMessageId != null) {
                    builder.replyToMessageId(context.replyToMessageId);
                }

                if (!context.buttons.isEmpty()) {
                    builder.replyMarkup(buildKeyboard(context.buttons));
                }

                yield client.execute(builder.build());
            }
            case DOCUMENT -> {
                var builder = SendDocument.builder()
                        .chatId(context.tgChatId)
                        .document(new InputFile(storedFile.file().data(), storedFile.metadata().filename()))
                        .caption(context.text)
                        .parseMode("Markdown");

                if (context.replyToMessageId != null) {
                    builder.replyToMessageId(context.replyToMessageId);
                }

                if (!context.buttons.isEmpty()) {
                    builder.replyMarkup(buildKeyboard(context.buttons));
                }

                yield client.execute(builder.build());
            }
        };
    }

    private Message sendMediaGroup(RenderingContext context) throws TelegramApiException {
        List<InputMedia> inputMediaList = new ArrayList<>();

        for (int i = 0; i < context.mediaAttachments.size(); i++) {
            var attachment = context.mediaAttachments.get(i);
            var storedFile = getMediaFile(attachment);

            InputMedia inputMedia = switch (attachment.displayType()) {
                case PHOTO -> InputMediaPhoto.builder()
                        .media(storedFile.file().data(), storedFile.metadata().filename())
                        .build();
                case DOCUMENT -> InputMediaDocument.builder()
                        .media(storedFile.file().data(), storedFile.metadata().filename())
                        .build();
            };

            if (i == 0 && context.text != null && !context.text.isBlank()) {
                inputMedia.setCaption(context.text);
                inputMedia.setParseMode("Markdown");
            }

            inputMediaList.add(inputMedia);
        }

        var builder = SendMediaGroup.builder()
                .chatId(context.tgChatId)
                .medias(inputMediaList);

        if (context.replyToMessageId != null) {
            builder.replyToMessageId(context.replyToMessageId);
        }

        if (!context.buttons.isEmpty()) {
            builder.replyMarkup(buildKeyboard(context.buttons));
        }

        List<Message> messages = client.execute(builder.build());
        return messages.getFirst();
    }

    private InlineKeyboardMarkup buildKeyboard(List<ButtonAttachment> buttons) {
        var rows = buttons.stream()
                .map(b -> new InlineKeyboardRow(
                        InlineKeyboardButton.builder()
                                .text(b.text())
                                .callbackData(b.tag())
                                .build()
                ))
                .toList();

        return InlineKeyboardMarkup.builder()
                .keyboard(rows)
                .build();
    }

    private StoredMedia getMediaFile(MediaAttachment attachment) {
        try {
            return downloadMediaUseCase.download(attachment.mediaId());
        } catch (MediaNotFoundException e) {
            throw new RuntimeException(e);
        }
    }
}