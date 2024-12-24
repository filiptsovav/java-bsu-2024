package by.VeranikaFiliptsova.quizer;

import by.VeranikaFiliptsova.quizer.generators.GroupTaskGenerator;
import by.VeranikaFiliptsova.quizer.generators.PoolTaskGenerator;
import by.VeranikaFiliptsova.quizer.generators.math.EquationMathTaskGenerator;
import by.VeranikaFiliptsova.quizer.generators.math.ExpressionMathTaskGenerator;
import by.VeranikaFiliptsova.quizer.tasks.TextTask;
import by.VeranikaFiliptsova.quizer.tasks.math.EquationMathTask;
import by.VeranikaFiliptsova.quizer.tasks.math.ExpressionMathTask;
import by.VeranikaFiliptsova.quizer.tasks.math.MathTask;
import by.VeranikaFiliptsova.quizer.tasks.math.TextExpressionMathTask;

import java.util.*;

public class Main {

    static Map<String, Quiz> getQuizMap() {
        HashMap<String, Quiz> myQuizMap = new HashMap<>();
        EnumSet<MathTask.Operation> operationAllowed = EnumSet.allOf(MathTask.Operation.class);
        EnumSet<MathTask.Operation> operationAllowedDIV = EnumSet.of(MathTask.Operation.DIV);
        ExpressionMathTaskGenerator<ExpressionMathTask> exprGen = new ExpressionMathTaskGenerator<>(-20, 20, operationAllowed,
                params -> new ExpressionMathTask(params.n1, params.op, params.n2));
        ExpressionMathTaskGenerator<ExpressionMathTask> exprGen2 = new ExpressionMathTaskGenerator<>(1, 100, operationAllowed,
                params -> new ExpressionMathTask(params.n1, params.op, params.n2));
        ExpressionMathTaskGenerator<ExpressionMathTask> exprGen5 = new ExpressionMathTaskGenerator<>(-10, 10, operationAllowedDIV,
                params -> new ExpressionMathTask(params.n1, params.op, params.n2));
        EquationMathTaskGenerator<EquationMathTask> eqvGen = new EquationMathTaskGenerator<>(-10, 10, operationAllowed,
                params -> new EquationMathTask(params.n1, params.op, params.n2, params.xStart));
        EquationMathTaskGenerator<EquationMathTask> eqvGen2 = new EquationMathTaskGenerator<>(1, 100, operationAllowed,
                params -> new EquationMathTask(params.n1, params.op, params.n2, params.xStart));
        EquationMathTaskGenerator<EquationMathTask> eqvGen5 = new EquationMathTaskGenerator<>(-20, 20, operationAllowedDIV,
                params -> new EquationMathTask(params.n1, params.op, params.n2, params.xStart));
        ExpressionMathTaskGenerator<TextExpressionMathTask> textGen = new ExpressionMathTaskGenerator<>(1, 20, operationAllowed,
                params -> new TextExpressionMathTask(params.n1, params.op, params.n2));


        Quiz test1  =  new Quiz(exprGen, 10);
        myQuizMap.put("Expressions", test1);

        Quiz test2 = new Quiz(eqvGen, 10);
        myQuizMap.put("Equations", test2);

        TextTask text1 = new TextTask("Если 7 человек встретятся и все пожмут друг другу руку по одному разу," +
                " сколько рукопожатий произойдет? ", "21");
        TextTask text2 = new TextTask("Какая цифра чаще всего встречается между 1 и 1000 включительно? ", "1");
        TextTask text3 = new TextTask("Сказочному гному каждую ночь нужна одна свеча. Он может сделать 1 новую свечу из 5 огарков. " +
                "Если у него 25 огарков, на сколько ночей хватит? ", "6");
        TextTask text4 = new TextTask("Возможно ли, что позавчера Алисе было 5 лет, а в следующем году исполнится 8? ", "Да");
        PoolTaskGenerator pool1 = new PoolTaskGenerator(false, text1, text2, text3, text4);
        GroupTaskGenerator groupGen = new GroupTaskGenerator(textGen, pool1);
        Quiz test3 = new Quiz(groupGen, 10);
        myQuizMap.put("TextTasks", test3);

        GroupTaskGenerator groupGen2 = new GroupTaskGenerator(exprGen2, eqvGen2, textGen,pool1);
        Quiz test4 = new Quiz(groupGen2, 10);
        myQuizMap.put("Everything", test4);

        GroupTaskGenerator groupGen5 = new GroupTaskGenerator(exprGen5, eqvGen5, textGen);
        Quiz test5 = new Quiz(groupGen5, 10);
        myQuizMap.put("EverythingWithDivision", test5);
        return myQuizMap;

    }

    public static void main(String[] args) {
        Map<String, Quiz> quizzes = getQuizMap();
        Scanner console = new Scanner(System.in);
        String name;
        System.out.println("Список доступных тестов: ");
        quizzes.keySet().forEach(System.out::println);
        do {
            System.out.println("Введите название теста...");
            name = console.next();
        } while (!quizzes.containsKey(name));
        Quiz currentQuiz = quizzes.get(name);
        while (!currentQuiz.isFinished()) {
            System.out.println(currentQuiz.nextTask().getText());
            String answer = console.next();
            Result result = currentQuiz.provideAnswer(answer);
            System.out.println(switch (result) {
                case OK -> "Верно";
                case WRONG -> "Неверно";
                case INCORRECT_INPUT -> "Неккоректный ввод";
            });
        }
        System.out.println(currentQuiz.getCorrectAnswerNumber() +" -верные ответы\n"
                + currentQuiz.getWrongAnswerNumber() + " -неверные ответы\n" +
                "Ваша оценка - "+currentQuiz.getMark()+"/10.0");
    }
}