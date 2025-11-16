package ru.yandex.practicum;

import java.io.*;
import java.nio.charset.StandardCharsets;

/*
этот класс содержит в себе всю рутину по работе с файлами словарей и с кодировками
    ему нужны методы по загрузке списка слов из файла по имени файла
    на выходе должен быть класс WordleDictionary
 */
public class WordleDictionaryLoader {

    private final PrintWriter logFile;

    public WordleDictionaryLoader(PrintWriter logFile) {
        this.logFile = logFile;
    }

    public WordleDictionary createDictionary(String fileName) throws FileNotFoundException {
        WordleDictionary dictionary = new WordleDictionary(logFile);
        try (BufferedReader br = new BufferedReader(new FileReader(fileName, StandardCharsets.UTF_8))) {
            while (br.ready()) {
                dictionary.add(br.readLine());
            }
        } catch (IOException e) {
            if (!(e.getMessage().isEmpty())) {
                logFile.println(e.getMessage());
                logFile.println("/n");
            } else {
                logFile.println(e.getStackTrace().toString());
                logFile.println("/n");
            }
            throw new FileNotFoundException();
        }
        return dictionary;
    }
}
