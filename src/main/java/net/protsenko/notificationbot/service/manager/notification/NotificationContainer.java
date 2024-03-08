package net.protsenko.notificationbot.service.manager.notification;

import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import net.protsenko.notificationbot.bot.Bot;
import net.protsenko.notificationbot.entity.Notification;
import net.protsenko.notificationbot.entity.Status;
import net.protsenko.notificationbot.repo.NotificationRepo;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

@Slf4j
@FieldDefaults(level = AccessLevel.PROTECTED, makeFinal = true)
public class NotificationContainer implements Runnable {

    Bot bot;
    Long chatId;
    Notification notification;
    NotificationRepo notificationRepo;

    public NotificationContainer(
            Bot bot,
            Long chatId,
            Notification notification,
            NotificationRepo notificationRepo
    ) {
        this.bot = bot;
        this.chatId = chatId;
        this.notification = notification;
        this.notificationRepo = notificationRepo;
    }

    @Override
    public void run() {
        try {
            Thread.sleep(notification.getSeconds() * 1000);
        } catch (InterruptedException e) {
            log.error(e.getMessage());
        }
        try {
            bot.execute(
                    sendNotification()
            );
        } catch (TelegramApiException e) {
            log.error(e.getMessage());
        }
        notification.setStatus(Status.FINISHED);
        notificationRepo.save(notification);
    }

    private BotApiMethod<?> sendNotification() {
        return SendMessage.builder()
                .chatId(chatId)
                .text(
                        "⚡\uFE0F Напоминание: " + notification.getTitle() + "\n"
                                + "❗\uFE0F " + notification.getDescription() + "\n\n"
                )
                .build();
    }

}
