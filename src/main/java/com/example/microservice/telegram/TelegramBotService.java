package com.example.microservice.telegram;

import com.example.microservice.config.bot.BotConfig;
import com.example.microservice.entity.Student;
import com.example.microservice.entity.TelegramGroup;
import com.example.microservice.entity.University;
import com.example.microservice.exception.UniversityCrmException;
import com.example.microservice.repository.StudentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.commands.SetMyCommands;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.commands.BotCommand;
import org.telegram.telegrambots.meta.api.objects.commands.scope.BotCommandScopeDefault;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class TelegramBotService extends TelegramLongPollingBot {

    private final StudentRepository studentRepository;

    private final BotConfig config;

    @Override
    public String getBotUsername() {
        return config.getBotName();
    }

    @Override
    public String getBotToken() {
        return config.getToken();
    }

    public void sendMessage(Long id, String text) {
        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(id);
        sendMessage.setText(text);

        ReplyKeyboardMarkup keyboardMarkup = new ReplyKeyboardMarkup();
        keyboardMarkup.setSelective(true);
        keyboardMarkup.setResizeKeyboard(true);
        keyboardMarkup.setOneTimeKeyboard(true);

        List<KeyboardRow> keyboardRows = new ArrayList<>();

        KeyboardRow row = new KeyboardRow();

        row.add("Проверить данные о группе и институте.");
        keyboardRows.add(row);

        keyboardMarkup.setKeyboard(keyboardRows);

        sendMessage.setReplyMarkup(keyboardMarkup);

        try {
            execute(sendMessage);
        } catch (TelegramApiException e) {
            throw new UniversityCrmException(e.getMessage());
        }
    }

    @Override
    public void onUpdateReceived(Update update) {
        Long chatId = update.getMessage().getChatId();
        Long studentId = update.getMessage().getFrom().getId();
        String messageText = update.getMessage().getText();
        Student student = studentRepository.findStudentByTelegram(studentId);
        if (update.getMessage().hasText()) {
            createMenu();
            switch (messageText) {
                case "/start":
                case "/start@MadelaTelegramBot":
                    if (String.valueOf(chatId).matches("^[-]\\d+") && student != null) {
                        student.setTelegramGroup(Set.of(new TelegramGroup().setId(chatId)));
                        studentRepository.save(student);
                        sendMessage(chatId, "Привет " + student.getUser().getName());
                    } else if (student != null && student.getTelegram() != null) {
                        sendMessage(chatId, "Ошибка. Вы уже есть в базе.");
                    } else {
                        sendHelloMessage(chatId);
                    }
                    return;
                case "Указать номер телефона(пример +79123456789):":
                    sendMessage(chatId, "Введите номер телефона:");
                    return;
                case "/delete":
                    if (student != null) {
                        student.setTelegram(null);
                        studentRepository.save(student);
                        sendMessage(chatId, "Пользователь удален.");
                    } else {
                        sendMessage(chatId, "Пользователь не найден");
                    }
                    return;
                case "/delete@MadelaTelegramBot":
                    if (student != null) {
                        student.setTelegramGroup(null);
                        studentRepository.save(student);
                        sendMessage(chatId, "Пользователь удален.");
                    } else {
                        sendMessage(chatId, "Пользователь не найден");
                    }
                    return;
                case "Проверить данные о группе и институте.":
                    if (student.getUniversity() != null) {
                        University university = student.getUniversity();
                        sendMessage(chatId, "Вы учитесь в " + university.getName() + " в городе " + university.getCity());
                    } else {
                        sendMessage(chatId,"Ошибка. У вас нет института");
                    }
                    return;
            }
        }
        try {
            student = getStudent(update.getMessage());
            if (student != null && student.getTelegram() == null) {
                student.setTelegram(studentId);
                studentRepository.save(student);
                sendMessage(chatId, "Приветствуем тебя, " + student.getUser().getName());
            } else if (student != null && student.getTelegram() != null){
                sendMessage(chatId, "Ошибка. Вы уже есть в базе");
            } else {
                sendMessage(chatId, "Ошибка. Пользователь не был найден");
            }
        } catch (Exception e) {
            throw new UniversityCrmException(e.getMessage());
        }
    }

    private void sendHelloMessage(Long id) {
        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(id);
        sendMessage.setText("Доброго времени суток, прошу выбрать вариант поиска твоих данный в базе: ");

        ReplyKeyboardMarkup keyboardMarkup = new ReplyKeyboardMarkup();
        keyboardMarkup.setSelective(true);
        keyboardMarkup.setResizeKeyboard(true);
        keyboardMarkup.setOneTimeKeyboard(true);

        List<KeyboardRow> keyboardRows = new ArrayList<>();

        KeyboardRow row = new KeyboardRow();

        KeyboardButton keyboardButton = new KeyboardButton();
        keyboardButton.setText("Дать доступ к контакту через телеграм >");
        keyboardButton.setRequestContact(true);
        row.add(keyboardButton);
        keyboardRows.add(row);

        row = new KeyboardRow();
        row.add("Указать номер телефона(пример +79123456789): ");
        keyboardRows.add(row);

        row = new KeyboardRow();
        row.add("Указать никнейм gitlab: ");
        keyboardRows.add(row);

        keyboardMarkup.setKeyboard(keyboardRows);

        sendMessage.setReplyMarkup(keyboardMarkup);

        try {
            execute(sendMessage);
        } catch (TelegramApiException e) {
            throw new UniversityCrmException(e.getMessage());
        }
    }

    private Student getStudent(Message message) {
        if (message.getContact() == null && message.getText().matches("^((\\+?[7-8])([0 -9]{10}))$")) {
            return studentRepository.findStudentByUser_Phone(message.getText());
        }
        if (message.getContact() != null) {
            return studentRepository.findStudentByUser_Phone(message.getContact().getPhoneNumber());
        }
        return null;
    }

    private void createMenu() {
        List<BotCommand> listofCommands = new ArrayList<>();
        listofCommands.add(new BotCommand("/start", "Начать работу с ботом."));
        listofCommands.add(new BotCommand("/me", "Получить информацию о себе."));
        listofCommands.add(new BotCommand("/delete", "Удалить пользователя в боте."));
        try {
            this.execute(new SetMyCommands(listofCommands, new BotCommandScopeDefault(), null));
        } catch (TelegramApiException e) {
            throw new UniversityCrmException(e.getMessage());
        }
    }
}