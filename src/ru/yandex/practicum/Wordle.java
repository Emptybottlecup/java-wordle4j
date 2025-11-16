package ru.yandex.practicum;

import ru.yandex.practicum.UserExceptions.DictionaryNotContainsThisWord;
import ru.yandex.practicum.UserExceptions.IsOnlyBlanks;
import ru.yandex.practicum.UserExceptions.WordLengthNotEqual5;

import java.io.FileNotFoundException;
import java.io.PrintWriter;

/*
в главном классе нам нужно:
    создать лог-файл (он должен передаваться во все классы)
    создать загрузчик словарей WordleDictionaryLoader
    загрузить словарь WordleDictionary с помощью класса WordleDictionaryLoader
    затем создать игру WordleGame и передать ей словарь
    вызвать игровой метод в котором в цикле опрашивать пользователя и передавать информацию в игру
    вывести состояние игры и конечный результат
 */
public class Wordle {

    public static void main(String[] args) {
        try (PrintWriter logFile = new PrintWriter("log.txt")){

            WordleDictionaryLoader loader = new WordleDictionaryLoader(logFile);
            WordleDictionary dictionary = loader.createDictionary("words_ru.txt");
            WordleGame wordleGame = new WordleGame(dictionary, logFile);

            while (wordleGame.getGameIsContinue()) {
                try {

                    wordleGame.gameStep();

                    if (wordleGame.getGiveAdvice()) {
                       System.out.println(wordleGame.getAdvice());
                    }

                    System.out.println(wordleGame.getSymbols());

                } catch (DictionaryNotContainsThisWord | WordLengthNotEqual5 | IsOnlyBlanks e) {
                    System.out.println(e.getMessage());
                }
            }

            if(wordleGame.getUserWin()) {
                System.out.println("Вы победили!");
            } else {
                System.out.println("Вы проиграли.");
            }

        } catch (FileNotFoundException e) {
            System.out.println("Не был найден корректный файл ");
        }
    }

}
