package net.protsenko.notificationbot.service.manager;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import net.protsenko.notificationbot.bot.Bot;
import net.protsenko.notificationbot.entity.Notification;
import net.protsenko.notificationbot.entity.Status;
import net.protsenko.notificationbot.repo.NotificationRepo;
import net.protsenko.notificationbot.repo.UserRepo;
import net.protsenko.notificationbot.service.contract.AbstractManager;
import net.protsenko.notificationbot.service.contract.CommandListener;
import net.protsenko.notificationbot.service.contract.MessageListener;
import net.protsenko.notificationbot.service.contract.QueryListener;
import net.protsenko.notificationbot.service.factory.KeyboardFactory;
import net.protsenko.notificationbot.service.handler.CallbackQueryHandler;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static net.protsenko.notificationbot.data.CallbackData.*;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class NotificationManager extends AbstractManager
        implements QueryListener, CommandListener, MessageListener {
    KeyboardFactory keyboardFactory;
    NotificationRepo notificationRepo;
    UserRepo userRepo;
    @Override
    public BotApiMethod<?> mainMenu(Message message, Bot bot) {
        return null;
    }

    @Override
    public BotApiMethod<?> mainMenu(CallbackQuery query, Bot bot) {
        return EditMessageText.builder()
                .chatId(query.getMessage().getChatId())
                .messageId(query.getMessage().getMessageId())
                .text("###")
                .replyMarkup(keyboardFactory.createInlineKeyboard(
                        List.of("Добавить уведомление"),
                        List.of(1),
                        List.of(notification_new.name())
                ))
                .build();
    }

    @Override
    public BotApiMethod<?> answerCommand(Message message, Bot bot) {
        return null;
    }

    @Override
    public BotApiMethod<?> answerMessage(Message message, Bot bot) {
        return null;
    }

    @Override
    public BotApiMethod<?> answerQuery(CallbackQuery query, String[] words,  Bot bot) {
        switch (words.length) {
            case 2 -> {
                switch (words[1]) {
                    case "main" -> {
                        return mainMenu(query, bot);
                    }
                    case "new" -> {
                        return newNotification(query, bot);
                    }
                }
            }
        }
        return null;
    }

    private BotApiMethod<?> newNotification(CallbackQuery query, Bot bot) {

        var user = userRepo.findByChatId(query.getMessage().getChatId());

        String id = String.valueOf(notificationRepo.save(
                Notification.builder()
                        .user(user)
                        .status(Status.BUILDING)
                        .build()
        ).getId());

        return EditMessageText.builder()
                .chatId(query.getMessage().getChatId())
                .messageId(query.getMessage().getMessageId())
                .text("Настройте уведомление")
                .replyMarkup(editNotificationReplyMarkup(id))
                .build();

    }

    private InlineKeyboardMarkup editNotificationReplyMarkup(String id) {

        List<String> text = new ArrayList<>();
        List<Integer> cfg = new ArrayList<>();
        List<String> data = new ArrayList<>();

        var notification = notificationRepo.findById(UUID.fromString(id)).orElseThrow();

        if (notification.getTitle() != null && !notification.getTitle().isBlank()) {
            text.add("+ Заголовок");
        } else {
            text.add("- Заголовок");
        }

        if (notification.getSeconds() != null && notification.getSeconds() != 0) {
            text.add("+ Время");
        } else {
            text.add("- Время");
        }

        if (notification.getDescription() != null && !notification.getDescription().isBlank()) {
            text.add("+ Описание");
        } else {
            text.add("- Описание");
        }

        text.add("\uD83D\uDD19 Главная");
        text.add("\uD83D\uDD50 Готово");

         return keyboardFactory.createInlineKeyboard(
                 text,
                 List.of(2, 1, 2),
                 List.of(notification_edit_title_.name() + id,
                         notification_edit_time_.name() + id,
                         notification_edit_description_.name() + id, main.name(),
                         notification_done_.name() + id)
         );
    }
}
