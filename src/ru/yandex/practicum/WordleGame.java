package ru.yandex.practicum;

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

    private final boolean[] forAdvice;

    private final HashSet<String> alreadyUsedWords;

    private Random rand;

    private boolean isFirstWord;

    public WordleGame(WordleDictionary dictionary, PrintWriter logFile) {
        this.logFile = logFile;
        this.dictionary = dictionary;
        this.scanner = new Scanner(System.in);
        this.gameIsContinue = true;
        this.userWin = false;
        this.rand = new Random();
        this.alreadyUsedWords = new LinkedHashSet<>();
        this.forAdvice = new boolean[5];
        isFirstWord = true;
        createAnswer();
    }

    public void gameStep() throws WordLengthNotEqual5, IsOnlyBlanks, DictionaryNotContainsThisWord {
        giveAdvice = false;

        while (true) {
            try {
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
                    if (isFirstWord) {
                        isFirstWord = false;
                    }
                } else {
                    giveAdvice = true;
                    findAdvice(userAnswer);
                    createSymbols(advice);
                }
                break;
            } catch (WordLengthNotEqual5 e) {
                logFile.println(e.getMessage());
                throw new WordLengthNotEqual5(e.getMessage());
            } catch (IsOnlyBlanks e) {
                logFile.println(e.getMessage());
                throw new IsOnlyBlanks(e.getMessage());
            } catch (DictionaryNotContainsThisWord e) {
                logFile.println(e.getMessage());
                throw new DictionaryNotContainsThisWord(e.getMessage());
            }
        }

        --steps;

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

    public void findAdvice(String userAnswer) {
        ArrayList<String> wordsForAdvice = new ArrayList<>();

        if (!isFirstWord) {
            for (int i = 0; i < 5; i++) {
                if (!forAdvice[i]) {
                    forAdvice[i] = true;
                    break;
                }
            }
        } else {
            isFirstWord = false;
        }

        for (String s : dictionary.getWords()) {
            if (!alreadyUsedWords.contains(s)) {
                boolean found = true;
                for (int i = 0; i < s.length(); i++) {
                    if (!((answer.charAt(i) == s.charAt(i)) == forAdvice[i])) {
                        found = false;
                        break;
                    }
                }
                if (found) {
                    wordsForAdvice.add(s);
                }
            }
        }
        if (!wordsForAdvice.isEmpty()) {
            advice = wordsForAdvice.get(rand.nextInt(wordsForAdvice.size()));
            alreadyUsedWords.add(advice);
        } else {
            advice = "";
        }
    }

    public void createSymbols(String userAnswer) {
        StringBuilder sb = new StringBuilder();

        for (int i = 0; i < userAnswer.length(); i++) {
            if (userAnswer.charAt(i) == answer.charAt(i)) {
                sb.append("+");
                forAdvice[i] = true;
            } else if (answer.contains(Character.toString(userAnswer.charAt(i)))) {
                sb.append("^");
            } else {
                sb.append("-");
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
}
