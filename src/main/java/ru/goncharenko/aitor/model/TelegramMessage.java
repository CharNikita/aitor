package ru.goncharenko.aitor.model;


import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

@Document("messages")
public record TelegramMessage(
    @Id
    String id,
    @Field("messageId")
    String messageId,
    @Field("chatId")
    String chatId,
    @Field("userId")
    String userId,
    @Field("userName")
    String userName,
    @Field("firstName")
    String firstName,
    @Field("lastName")
    String lastName,
    @Field("date")
    Integer date,
    @Field("text")
    String text
) {
}
