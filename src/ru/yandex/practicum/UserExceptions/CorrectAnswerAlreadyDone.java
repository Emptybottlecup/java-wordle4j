package ru.yandex.practicum.UserExceptions;

public class CorrectAnswerAlreadyDone extends Exception {
    public CorrectAnswerAlreadyDone(String message) {
        super(message);
    }
}
