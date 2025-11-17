package ru.yandex.practicum;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Assertions;
import ru.yandex.practicum.UserExceptions.DictionaryNotContainsThisWord;
import ru.yandex.practicum.UserExceptions.IsOnlyBlanks;
import ru.yandex.practicum.UserExceptions.WordLengthNotEqual5;
import ru.yandex.practicum.UserExceptions.CorrectAnswerAlreadyDone;

import java.io.PrintWriter;
import java.io.FileNotFoundException;
import java.util.Random;

class WordleTest {
    private static WordleDictionaryLoader loader;
    private static WordleDictionary dictionary;
    private static WordleGame wordleGame;
    private static PrintWriter logFile;
    private static Random rand;

    @BeforeAll
    public static void init() throws FileNotFoundException {
        logFile = new PrintWriter(System.out);
        rand = new Random();
        loader = new WordleDictionaryLoader(logFile);
        dictionary = loader.createDictionary("words_ru.txt");
    }

    @BeforeEach
    public void createWordleGameClass() throws FileNotFoundException {
            wordleGame = new WordleGame(dictionary, logFile);
    }

    @Test
    public void testGameCycleLose() {
        String wrongAnswer = wordleGame.getAnswer();

        while (wordleGame.getAnswer().equals(wrongAnswer)) {
            wrongAnswer = dictionary.getWords().get(rand.nextInt(dictionary.getWords().size() - 1));
        }

        Assertions.assertFalse(winOrNotWin(wrongAnswer));
    }

    @Test
    public void testGameCycleWin() {
        String correctAnswer = wordleGame.getAnswer();

        Assertions.assertTrue(winOrNotWin(correctAnswer));
    }

    @Test public void testGameIssues() {
        String wrongAnswer = "eeee";
        try {
            wordleGame.gameStep(wrongAnswer);
        } catch (WordLengthNotEqual5 | IsOnlyBlanks | DictionaryNotContainsThisWord | CorrectAnswerAlreadyDone e) {
            Assertions.assertEquals("Слово состоит меньше чем из 5 символов", e.getMessage());
        }

        wrongAnswer = "eeeee";
        try {
            wordleGame.gameStep(wrongAnswer);
        } catch (WordLengthNotEqual5 | IsOnlyBlanks | DictionaryNotContainsThisWord | CorrectAnswerAlreadyDone e) {
            Assertions.assertEquals("Слово введенное пользователем не содержится в словаре", e.getMessage());
        }

        wrongAnswer = "     ";
        try {
            wordleGame.gameStep(wrongAnswer);
        } catch (WordLengthNotEqual5 | IsOnlyBlanks | DictionaryNotContainsThisWord | CorrectAnswerAlreadyDone e) {
            Assertions.assertEquals("Введенное слово состоит только из пробелов.", e.getMessage());
        }

        try {
            while(true) {
                wordleGame.gameStep("");
            }
        } catch (WordLengthNotEqual5 | IsOnlyBlanks | DictionaryNotContainsThisWord | CorrectAnswerAlreadyDone e) {
            Assertions.assertEquals("Компьютер уже выдал правильный ответ", e.getMessage());
        }
    }

    @Test
    public void lastAdviceIsAnswer() {
        try {
            while(true) {
                wordleGame.gameStep("");
            }
        } catch (WordLengthNotEqual5 | IsOnlyBlanks | DictionaryNotContainsThisWord | CorrectAnswerAlreadyDone e) {
            Assertions.assertEquals(wordleGame.getAnswer(), wordleGame.getAdvice());
        }
    }

    private boolean winOrNotWin(String answer) {

        while (wordleGame.getGameIsContinue()) {
            try {

                wordleGame.gameStep(answer);

            } catch (DictionaryNotContainsThisWord | WordLengthNotEqual5 | IsOnlyBlanks | CorrectAnswerAlreadyDone e) {
                System.out.println(e.getMessage());
            }
        }

        return wordleGame.getUserWin();
    }
}