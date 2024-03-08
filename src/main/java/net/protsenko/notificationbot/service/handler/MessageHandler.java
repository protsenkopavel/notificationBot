package net.protsenko.notificationbot.service.handler;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import net.protsenko.notificationbot.bot.Bot;
import net.protsenko.notificationbot.repo.UserRepo;
import net.protsenko.notificationbot.service.contract.AbstractHandler;
import net.protsenko.notificationbot.service.manager.notification.NotificationManager;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.interfaces.BotApiObject;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class MessageHandler extends AbstractHandler {
    UserRepo userRepo;
    NotificationManager notificationManager;
    @Override
    public BotApiMethod<?> answer(BotApiObject object, Bot bot) throws TelegramApiException {
        var message = (Message) object;
        var user = userRepo.findByChatId(message.getChatId());
        switch (user.getAction()) {
            case FREE -> {
                return null;
            }
            case SENDING_TIME, SENDING_DESCRIPTION, SENDING_TITLE -> {
                return notificationManager.answerMessage(message, bot);
            }
        }
        throw new UnsupportedOperationException();
    }
}
