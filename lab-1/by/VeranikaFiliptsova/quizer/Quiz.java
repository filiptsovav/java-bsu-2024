package by.VeranikaFiliptsova.quizer;

import by.VeranikaFiliptsova.quizer.exceptions.QuizNotFinishedException;


public class Quiz {
    int counter;
    int counterWrong;
    int counterOK;
    int counterIncInput;
    Task currentTask;
    TaskGenerator<? extends Task> currentGenerator;
    Result currentRes;

    Quiz(TaskGenerator<? extends Task> generator, int taskCount) {
        currentGenerator = generator;
        counter = taskCount;
        currentRes = Result.OK;
    }


    Task nextTask() {
        if (currentRes != Result.INCORRECT_INPUT) {
            currentTask = currentGenerator.generate();
        }
        return currentTask;
    }


    Result provideAnswer(String answer) {
        currentRes = currentTask.validate(answer);
        if (currentRes == Result.OK) {
            counterOK++;
            counter--;
        } else if (currentRes == Result.WRONG) {
            counterWrong++;
            counter--;
        } else {
            counterIncInput++;
        }
        return currentRes;
    }


    boolean isFinished() {
        return counter == 0;
    }


    int getCorrectAnswerNumber() {
        return counterOK;
    }


    int getWrongAnswerNumber() {
        return counterWrong;
    }


    int getIncorrectInputNumber() {
        return counterIncInput;
    }

    double getMark() {
        if (isFinished()) {
            return 10 * (double) getCorrectAnswerNumber() /(getCorrectAnswerNumber() + getWrongAnswerNumber());
        }
        throw new QuizNotFinishedException();
    }

}