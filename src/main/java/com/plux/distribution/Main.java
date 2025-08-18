package com.plux.distribution;

import com.plux.distribution.application.service.FlowFeedbackProcessor;
import com.plux.distribution.application.service.MessageDeliveryService;
import com.plux.distribution.application.service.RegisterFeedbackService;
import com.plux.distribution.application.workflow.DefaultContextManager;
import com.plux.distribution.application.workflow.frame.FrameRegistry;
import com.plux.distribution.application.workflow.core.FrameFactory;
import com.plux.distribution.application.workflow.frame.utils.SequenceFrame;
import com.plux.distribution.infrastructure.inmemory.MemoryFrameLinker;
import com.plux.distribution.infrastructure.persistence.DbFeedbackRepository;
import com.plux.distribution.infrastructure.persistence.DbMessageRepository;
import com.plux.distribution.infrastructure.inmemory.MemoryMessageLinker;
import com.plux.distribution.infrastructure.inmemory.MemoryUserLinker;
import com.plux.distribution.infrastructure.inmemory.MemoryUserRepository;
import com.plux.distribution.infrastructure.persistence.config.HibernateConfig;
import com.plux.distribution.infrastructure.telegram.TelegramHandler;
import com.plux.distribution.infrastructure.telegram.sender.TelegramMessageSender;
import java.util.List;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.telegram.telegrambots.client.okhttp.OkHttpTelegramClient;
import org.telegram.telegrambots.longpolling.TelegramBotsLongPollingApplication;

public class Main {

    private static final Logger log = LogManager.getLogger(Main.class);

    public static void main(String[] args) {
        var botToken = System.getenv("TG_TOKEN");

        var hibernateConfig = new HibernateConfig(
                System.getenv("DB_URL"),
                System.getenv("DB_USER"),
                System.getenv("DB_PASSWORD")
        );

        var tgUserLinker = new MemoryUserLinker();
        var tgMessageLinker = new MemoryMessageLinker();

        var tgClient = new OkHttpTelegramClient(botToken);

        var sender = new TelegramMessageSender(tgClient, tgUserLinker, tgMessageLinker);

        var messageRepo = new DbMessageRepository(hibernateConfig.getSessionFactory());
        var feedbackRepo = new DbFeedbackRepository(hibernateConfig.getSessionFactory());
        var userRepo = new MemoryUserRepository();

        var messageDeliveryService = new MessageDeliveryService(sender, messageRepo, messageRepo);

        var frameFactory = makeFrameFactory();
        var frameLinker = new MemoryFrameLinker(frameFactory);
        var frameContextManager = new DefaultContextManager(frameLinker, messageDeliveryService);
        frameLinker.setManager(frameContextManager);

        var mainFeedbackProcessor = new FlowFeedbackProcessor(frameLinker);

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

    private static FrameFactory makeFrameFactory() {
        var pin = System.getenv("BOT_PIN");

        var factory = new FrameRegistry();

        factory.register(new com.plux.distribution.application.workflow.frame.registration.hello.HelloFrame());
        factory.register(new com.plux.distribution.application.workflow.frame.registration.hello.PostHelloFrame());
        factory.register(new com.plux.distribution.application.workflow.frame.registration.pin.CheckPasswordFrame(pin));
        factory.register(new com.plux.distribution.application.workflow.frame.registration.pin.CorrectPasswordFrame());
        factory.register(new com.plux.distribution.application.workflow.frame.registration.pin.InorrectPasswordFrame());

        factory.register(new SequenceFrame("flow.registration", List.of(
                factory.get("registration.hello_frame"),
                factory.get("registration.check_pin")
        )));

        return factory;
    }
}