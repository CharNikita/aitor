package ru.goncharenko.aitor.bot;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.QuestionAnswerAdvisor;
import org.springframework.ai.vectorstore.SearchRequest;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.Map;

@Component
public class LoreHandler implements TelegramUpdateHandler {
    private static final Logger logger = LoggerFactory.getLogger(LoreHandler.class);
    private static final String USER_TEXT_ADVISE = """
        Ниже представлен контекст, окружённый ---------------------
        
        ---------------------
        {question_answer_context}
        ---------------------
        
        Учитывая контекст и предоставленную историю, но без использования предварительных знаний,
        ответьте на комментарий пользователя о лоре данного диалога. Если ответ не содержится в контексте, сообщите
        пользователю, что по поводу вопроса пользователя нет в лоре диалога.
        """;

    private final ChatClient chatClient;
    private final VectorStore vectorStore;
    private static final String COMMAND = "/lore";

    public LoreHandler(
        ChatClient chatClient,
        VectorStore vectorStore
    ) {
        this.chatClient = chatClient;
        this.vectorStore = vectorStore;
    }

    @Override
    public boolean isAccept(Update update) {
        if (update.getMessage() == null || update.getMessage().getText() == null) {
            logger.info("Update has no message");
            return false;
        }
        return update.getMessage().getText().startsWith(COMMAND);
    }

    @Override
    public BotApiMethod<?> handle(Update update) {
        final var command = update.getMessage().getText();
        final var chatId = update.getMessage().getChatId().toString();

        final var modelResponse = chatClient
            .prompt()
            .toolContext(Map.of("chatId", chatId))
            .advisors(
                new QuestionAnswerAdvisor(
                    vectorStore,
                    SearchRequest.builder()
                        .filterExpression("chatId == '%s' ".formatted(chatId))
                        .build(),
                    USER_TEXT_ADVISE
                )
            )
            .user(command.substring(COMMAND.length()))
            .call()
            .content();

        return SendMessage.builder()
            .chatId(update.getMessage().getChatId())
            .replyToMessageId(update.getMessage().getMessageId())
            .text(modelResponse)
            .build();
    }
}
