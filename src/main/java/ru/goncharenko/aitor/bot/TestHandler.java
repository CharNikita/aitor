package ru.goncharenko.aitor.bot;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;

@Component
public class TestHandler implements TelegramUpdateHandler {
    private final static Logger logger = LoggerFactory.getLogger(TestHandler.class);

    @Override
    public Boolean isAccept(Update update) {
        logger.info("Update is accepted in TestHandler");
        if (update.getMessage() == null) {
            logger.info("Update has no message");
            return false;
        }
        return false;
    }

    @Override
    public BotApiMethod<?> handle(Update update) {
        return SendMessage.builder()
            .chatId(update.getMessage().getChatId())
            .text(update.getMessage().getText())
            .build();
    }
}
