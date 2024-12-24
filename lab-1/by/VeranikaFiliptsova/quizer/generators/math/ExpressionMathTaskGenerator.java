package by.VeranikaFiliptsova.quizer.generators.math;

import by.VeranikaFiliptsova.quizer.exceptions.GeneratorNotValidException;
import by.VeranikaFiliptsova.quizer.tasks.math.ExpressionMathTask;
import by.VeranikaFiliptsova.quizer.tasks.math.MathTask.Operation;

import java.util.EnumSet;
import java.util.Random;
import java.util.function.Function;


public class ExpressionMathTaskGenerator<T extends ExpressionMathTask> extends AbstractMathTaskGenerator<T>{

    private final Function<Params, T> typeOfTask;

    public static class Params {
        public final int n1;
        public final Operation op;
        public final int n2;

        public Params(int n1, Operation op, int n2) {
            this.n1 = n1;
            this.op = op;
            this.n2 = n2;
        }
    }

    public ExpressionMathTaskGenerator(
            int minNumber,
            int maxNumber,
            EnumSet<Operation> operations,
            Function<Params, T> typeOfTask
    ) {
        super(minNumber, maxNumber, operations);
        this.typeOfTask = typeOfTask;
    }

    public T generate() {
        if (isNotValid()) {
            throw new GeneratorNotValidException("Set of allowed operations is empty or minimum value is greater than maximum value");
        }
        if (maxNumb == 0 && minNumb == 0 && operationAllowed.size() == 1 && operationAllowed.contains(Operation.DIV)) {
            throw new GeneratorNotValidException("impossible to generate task without division on 0");
        }
        Random rand = new Random();
        Operation op = operationAllowed.stream().toList().get(rand.nextInt(operationAllowed.size()));
        if (op.equals(Operation.DIV)) {
            int n2 = generateNonZero();
            int n1 = n2 > 0 ? generateInScopePositive(n2) : generateInScopeNegative(n2);
            return typeOfTask.apply(new Params(n1, op, n2));
        }
        return typeOfTask.apply(new Params(randUsual(), op, randUsual()));
    }
}
