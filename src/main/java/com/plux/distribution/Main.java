package com.plux.distribution;

import com.plux.distribution.application.service.MessageDeliveryService;
import com.plux.distribution.application.service.RegisterFeedbackService;
import com.plux.distribution.application.service.SequenceFeedbackProcessor;
import com.plux.distribution.infrastructure.persistence.DbFeedbackRepository;
import com.plux.distribution.infrastructure.persistence.DbMessageRepository;
import com.plux.distribution.infrastructure.persistence.MemoryMessageLinker;
import com.plux.distribution.infrastructure.persistence.MemoryUserLinker;
import com.plux.distribution.infrastructure.persistence.MemoryUserRepository;
import com.plux.distribution.infrastructure.persistence.config.HibernateConfig;
import com.plux.distribution.infrastructure.telegram.TelegramHandler;
import com.plux.distribution.infrastructure.telegram.sender.TelegramMessageSender;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.telegram.telegrambots.client.okhttp.OkHttpTelegramClient;
import org.telegram.telegrambots.longpolling.TelegramBotsLongPollingApplication;

public class Main {

    private static final Logger log = LogManager.getLogger(Main.class);

    public static void main(String[] args) {
        var botToken = System.getenv("TG_TOKEN");
        var tgUserId = Long.parseLong(System.getenv("TG_USER_ID"));

        var hibernateConfig = new HibernateConfig(
                System.getenv("DB_URL"),
                System.getenv("DB_USER"),
                System.getenv("DB_PASSWORD")
        );

        var tgUserLinker = new MemoryUserLinker();
        var tgMessageLinker = new MemoryMessageLinker();

        var tgClient = new OkHttpTelegramClient(botToken);

        var sender = new TelegramMessageSender(tgClient, tgUserLinker, null);

        var messageRepo = new DbMessageRepository(hibernateConfig.getSessionFactory());
        var feedbackRepo = new DbFeedbackRepository(hibernateConfig.getSessionFactory());
        var userRepo = new MemoryUserRepository();

        var mainFeedbackProcessor = new SequenceFeedbackProcessor();

        var messageDeliveryService = new MessageDeliveryService(sender, messageRepo, messageRepo);
        var registerFeedbackService = new RegisterFeedbackService(messageRepo, feedbackRepo,
                userRepo, mainFeedbackProcessor);

        var tgHandler = new TelegramHandler(registerFeedbackService, tgMessageLinker,
                tgMessageLinker, tgUserLinker, tgUserLinker);

        try (var botsApplication = new TelegramBotsLongPollingApplication()) {
            botsApplication.registerBot(botToken, tgHandler);

            System.out.println("Bot successfully started!");
            Thread.currentThread().join();
        } catch (Exception e) {
            log.error("e: ", e);
        }

    }
}