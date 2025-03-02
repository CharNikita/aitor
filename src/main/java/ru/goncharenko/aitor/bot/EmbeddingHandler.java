package ru.goncharenko.aitor.bot;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ai.document.Document;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.Collections;

@Component
public class EmbeddingHandler implements TelegramUpdateHandler {
    private final static Logger logger = LoggerFactory.getLogger(EmbeddingHandler.class);

    private final VectorStore store;

    public EmbeddingHandler(VectorStore store) {
        this.store = store;
    }

    @Override
    public Boolean isAccept(Update update) {
        if (update.getMessage() == null && update.getMessage().getText() == null) {
            logger.info("Update has no message");
            return false;
        }
        return true;
    }

    @Override
    public BotApiMethod<?> handle(Update update) {
        final var text = update.getMessage().getText();
        store.accept(
            Collections.singletonList(
                new Document(text)
            )
        );
        return null;
    }
}
