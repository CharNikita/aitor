package ru.goncharenko.aitor.bot.handlers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ai.document.Document;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.objects.Update;
import ru.goncharenko.aitor.bot.TelegramUpdateHandler;

import java.util.Collections;
import java.util.Map;

@Component
public class EmbeddingHandler implements TelegramUpdateHandler {
    private final static Logger logger = LoggerFactory.getLogger(EmbeddingHandler.class);

    private final VectorStore store;

    public EmbeddingHandler(VectorStore store) {
        this.store = store;
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
        logger.info("Update handled in EmbeddingHandler");

        final var message = update.getMessage();
        final var text = message.getText();
        final var userName = message.getFrom().getUserName();
        final var date = message.getDate();

        final var userId = message.getFrom().getId().toString();
        final var chatId = message.getChatId().toString();

        store.accept(
            Collections.singletonList(
                new Document(
                    text,
                    Map.of(
                        "userName", userName,
                        "userId", userId,
                        "chatId", chatId,
                        "date", date
                    )
                )
            )
        );
        return null;
    }
}
