package ru.goncharenko.aitor.bot;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ai.document.Document;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.objects.Update;

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
        if (update.getMessage() == null && update.getMessage().getText() == null) {
            logger.info("Update has no message");
            return false;
        }
        return true;
    }

    @Override
    public BotApiMethod<?> handle(Update update) {
        logger.info("Update handled in EmbeddingHandler");

        final var message = update.getMessage();
        final var text = message.getText();
        final var userName = message.getFrom().getUserName();
        final var result = "%s: %s".formatted(userName, text);

        final var userId = message.getFrom().getId().toString();
        final var chatId = message.getChatId().toString();

        store.accept(
            Collections.singletonList(
                new Document(
                    result,
                    Map.of(
                        "userName", userName,
                        "userId", userId,
                        "chatId", chatId
                    )
                )
            )
        );
        return null;
    }
}
