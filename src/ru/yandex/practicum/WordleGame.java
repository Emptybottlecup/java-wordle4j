package ru.yandex.practicum;

import ru.yandex.practicum.UserExceptions.CorrectAnswerAlreadyDone;
import ru.yandex.practicum.UserExceptions.DictionaryNotContainsThisWord;
import ru.yandex.practicum.UserExceptions.IsOnlyBlanks;
import ru.yandex.practicum.UserExceptions.WordLengthNotEqual5;

import java.io.PrintWriter;
import java.util.*;


/*
в этом классе хранится словарь и состояние игры
    текущий шаг
    всё что пользователь вводил
    правильный ответ

в этом классе нужны методы, которые
    проанализируют совпадение слова с ответом
    предложат слово-подсказку с учётом всего, что вводил пользователь ранее

не забудьте про специальные типы исключений для игровых и неигровых ошибок
 */
public class WordleGame {

    private String answer;

    private String advice;

    private int steps = 6;

    private final WordleDictionary dictionary;

    private final PrintWriter logFile;

    private boolean gameIsContinue;

    private boolean userWin;

    private boolean giveAdvice;

    private final Scanner scanner;

    private String symbols;

    private final HashSet<String> alreadyUsedWords;

    private final boolean[] letterOnCorrectPos;

    private final HashSet<String> wrongLetters;

    private final HashSet<String> correctLetters;

    private Random rand;

    public WordleGame(WordleDictionary dictionary, PrintWriter logFile) {
        this.logFile = logFile;
        this.dictionary = dictionary;
        this.scanner = new Scanner(System.in);
        this.gameIsContinue = true;
        this.userWin = false;
        this.rand = new Random();
        this.alreadyUsedWords = new LinkedHashSet<>();
        this.letterOnCorrectPos = new boolean[5];
        this.wrongLetters = new LinkedHashSet<>();
        this.correctLetters = new LinkedHashSet<>();
        createAnswer();
    }

    public void gameStep() throws WordLengthNotEqual5, IsOnlyBlanks, DictionaryNotContainsThisWord, CorrectAnswerAlreadyDone {
        giveAdvice = false;

        while (true) {
            String userAnswer = scanner.nextLine();
            if (!userAnswer.isEmpty()) {
                if (userAnswer.length() != 5) {
                    throw new WordLengthNotEqual5("Слово состоит меньше чем из 5 символов");
                } else if (userAnswer.isBlank()) {
                    throw new IsOnlyBlanks("Введенное слово состоит только из пробелов.");
                }
                userAnswer = userAnswer.toLowerCase().replaceAll("ё", "е");
                if (!dictionary.contains(userAnswer)) {
                    throw new DictionaryNotContainsThisWord("Слово введенное пользователем не содержится в словаре");
                }

                if (userAnswer.equals(answer)) {
                    userWin = true;
                }
                alreadyUsedWords.add(userAnswer);
                createSymbols(userAnswer);
                --steps;
            } else {
                giveAdvice = true;
                findAdvice(userAnswer);
                createSymbols(advice);
            }
            break;
        }

        if (steps == 0 || userWin) {
            gameIsContinue = false;
        }
    }

    public boolean getGameIsContinue() {
        return gameIsContinue;
    }

    public boolean getUserWin() {
        return userWin;
    }

    public boolean getGiveAdvice() {
        return giveAdvice;
    }

    public String getAdvice() {
        return advice;
    }

    public void findAdvice(String userAnswer) throws CorrectAnswerAlreadyDone {
        ArrayList<String> wordsForAdvice = new ArrayList<>();

        for (String s : dictionary.getWords()) {
            if (checkWordForAdvice(s)) {
                wordsForAdvice.add(s);
            }
        }
        if (!wordsForAdvice.isEmpty()) {
            advice = wordsForAdvice.get(rand.nextInt(wordsForAdvice.size()));
            alreadyUsedWords.add(advice);
        } else {
            throw new CorrectAnswerAlreadyDone("Компьютер уже выдал правильный ответ");
        }
    }

    public void createSymbols(String userAnswer) {
        StringBuilder sb = new StringBuilder();

        for (int i = 0; i < userAnswer.length(); i++) {
            if (userAnswer.charAt(i) == answer.charAt(i)) {
                sb.append("+");
                letterOnCorrectPos[i] = true;
                correctLetters.add(Character.toString(userAnswer.charAt(i)));
            } else if (answer.contains(Character.toString(userAnswer.charAt(i)))) {
                sb.append("^");
                correctLetters.add(Character.toString(userAnswer.charAt(i)));
            } else {
                sb.append("-");
                wrongLetters.add(Character.toString(userAnswer.charAt(i)));
            }
        }

        symbols = sb.toString();
    }

    public String getSymbols() {
        return symbols;
    }

    private void createAnswer() {
        answer = dictionary.get(rand.nextInt(dictionary.size() - 1));
    }

    private boolean checkWordForAdvice(String word) {
        if (!alreadyUsedWords.contains(word)) {
            for (int i = 0; i < word.length(); i++) {
                if (letterOnCorrectPos[i]) {
                    if (!(word.charAt(i) == answer.charAt(i))) {
                        return false;
                    }
                }
                if (wrongLetters.contains(Character.toString(word.charAt(i)))) {
                    return false;
                }
            }

            for (String letter : correctLetters) {
                if (!word.contains(letter)) {
                    return false;
                }
            }

            return true;
        } else {
            return false;
        }
    }
}
