package ru.yandex.practicum;

import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

/*
этот класс содержит в себе список слов List<String>
    его методы похожи на методы списка, но учитывают особенности игры
    также этот класс может содержать рутинные функции по сравнению слов, букв и т.д.
 */
public class WordleDictionary {

    private final List<String> words;
    private final PrintWriter logFile;

    public WordleDictionary(PrintWriter logFile) {
        this.logFile = logFile;
        this.words = new ArrayList<>();
    }

    public boolean contains(String word) {
        return words.contains(word);
    }

    public void add(String word) {
        if (word.length() == 5) {
            words.add(word.replaceAll("ё", "е").toLowerCase());
        }
    }

    public int size() {
        return words.size();
    }

    public String get(int wordIndex) {
        return words.get(wordIndex);
    }

    public List<String> getWords() {
        return words;
    }
}
