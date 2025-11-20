package ru.yandex.practicum.UserExceptions;

public class DictionaryNotContainsThisWord extends Exception {
    public DictionaryNotContainsThisWord(String message) {
        super(message);
    }
}
