package net.protsenko.notificationbot.service.manager;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import net.protsenko.notificationbot.bot.Bot;
import net.protsenko.notificationbot.service.contract.AbstractManager;
import net.protsenko.notificationbot.service.contract.CommandListener;
import net.protsenko.notificationbot.service.contract.QueryListener;
import net.protsenko.notificationbot.service.factory.KeyboardFactory;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.Message;

import java.util.List;

import static net.protsenko.notificationbot.data.CallbackData.notification_main;


@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class MainManager extends AbstractManager implements CommandListener, QueryListener {
    KeyboardFactory keyboardFactory;

    @Override
    public BotApiMethod<?> mainMenu(Message message, Bot bot) {
        return null;
    }

    @Override
    public BotApiMethod<?> mainMenu(CallbackQuery query, Bot bot) {
        return null;
    }

    @Override
    public BotApiMethod<?> answerCommand(Message message, Bot bot) {
        return greetings(message.getChatId());
    }

    @Override
    public BotApiMethod<?> answerQuery(CallbackQuery query, String[] words, Bot bot) {
        return null;
    }


    private BotApiMethod<?> greetings(Long chatId) {
        return SendMessage.builder()
                .chatId(chatId)
                .text("Hello from manager!")
                .replyMarkup(
                        keyboardFactory.createInlineKeyboard(
                                List.of("Напоминалки"),
                                List.of(1),
                                List.of(notification_main.name())
                        )
                )
                .build();
    }
}
