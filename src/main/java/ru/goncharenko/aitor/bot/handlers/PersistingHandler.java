package ru.goncharenko.aitor.bot.handlers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.objects.Update;
import ru.goncharenko.aitor.model.TelegramMessage;
import ru.goncharenko.aitor.bot.TelegramUpdateHandler;

@Component
public class PersistingHandler implements TelegramUpdateHandler {
    private final static Logger logger = LoggerFactory.getLogger(PersistingHandler.class);

    private final MongoOperations mongoOperations;

    public PersistingHandler(MongoOperations mongoOperations) {
        this.mongoOperations = mongoOperations;
    }

    @Override
    public boolean isAccept(Update update) {
        if (update.hasMessage() || update.getMessage().hasText()) {
            logger.info("Update has no message");
            return false;
        }
        return !update.getMessage().getText().startsWith("/");
    }

    @Override
    public BotApiMethod<?> handle(Update update) {
        logger.info("Update handled in PersistingHandler");

        final var message = update.getMessage();

        mongoOperations.save(
            new TelegramMessage(
                null,
                message.getMessageId().toString(),
                message.getChatId().toString(),
                message.getFrom().getId().toString(),
                message.getFrom().getUserName(),
                message.getFrom().getFirstName(),
                message.getFrom().getLastName(),
                message.getDate(),
                message.getText()
            )
        );

        return null;
    }
}
