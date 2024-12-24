package by.VeranikaFiliptsova.quizer.generators.math;

import by.VeranikaFiliptsova.quizer.TaskGenerator;
import by.VeranikaFiliptsova.quizer.tasks.math.MathTask;


public interface MathTaskGenerator<T extends MathTask> extends TaskGenerator<T> {

    boolean isNotValid();
    int getMinNumber();
    int getMaxNumber();

    /**
     * @return разница между максимальным и минимальным возможным числом
     */
    default int getDiffNumber()
    {
        return getMaxNumber() - getMinNumber();
    }

    int randUsual();

}

