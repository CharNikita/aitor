package ru.goncharenko.aitor.tool;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ai.chat.model.ToolContext;
import org.springframework.ai.document.Document;
import org.springframework.ai.tool.annotation.Tool;
import org.springframework.ai.tool.annotation.ToolParam;
import org.springframework.ai.vectorstore.SearchRequest;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class UserInformationTool {
    private static final Logger logger = LoggerFactory.getLogger(UserInformationTool.class);
    private final VectorStore store;

    public UserInformationTool(VectorStore store) {
        this.store = store;
    }

    @Tool(description = "Получить информацию или лор по участнику чата")
    public List<String> getUserInfo(
        @ToolParam(description = "никнейм пользователя") String userName,
        ToolContext toolContext
    ) {
        logger.info("Get user info");
        final var context = toolContext.getContext();
        final String chatId = context.get("chatId").toString();

        final var searchRequest = SearchRequest.builder()
            .filterExpression("chatId == '%s' && userName =='%s'".formatted(chatId, userName))
            .topK(3)
            .build();

        return store
            .similaritySearch(searchRequest)
            .stream()
            .map(Document::getText)
            .toList();
    }
}
