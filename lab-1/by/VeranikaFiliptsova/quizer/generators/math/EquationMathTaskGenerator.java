package by.VeranikaFiliptsova.quizer.generators.math;

import by.VeranikaFiliptsova.quizer.exceptions.GeneratorNotValidException;
import by.VeranikaFiliptsova.quizer.tasks.math.MathTask.Operation;
import by.VeranikaFiliptsova.quizer.tasks.math.EquationMathTask;

import java.util.EnumSet;
import java.util.Random;
import java.util.function.Function;


public class EquationMathTaskGenerator<T extends EquationMathTask> extends AbstractMathTaskGenerator<T>{
    private final Function<Params, T> typeOfTask;

    public static class Params {
        public final int n1;
        public final Operation op;
        public final int n2;
        public final boolean xStart;

        public Params(int n1, Operation op, int n2, boolean xStart) {
            this.n1 = n1;
            this.op = op;
            this.n2 = n2;
            this.xStart = xStart;
        }
    }

    public EquationMathTaskGenerator(
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
        if (maxNumb == 0 && minNumb == 0
                && !operationAllowed.contains(Operation.SUM)
                && !operationAllowed.contains(Operation.DIFF)) {
            throw new GeneratorNotValidException("impossible to generate task without division on 0");
        }
        Random rand = new Random();
        boolean xStart = rand.nextBoolean();
        Operation op = operationAllowed.stream().toList().get(rand.nextInt(operationAllowed.size()));
        if (op.equals(Operation.MUL)) {
            int n1 = generateNonZero();
            int n2 = n1 > 0 ? generateInScopePositive(n1) :generateInScopeNegative(n1);
            return typeOfTask.apply(new Params(n1, op, n2, xStart));
            //return new EquationMathTask(n1, op, n2, xStart);
        }
        if (op.equals(Operation.DIV) && !xStart) {
            int n2 = generateNonZero();
            int n1;
            do {
                n1 = n2 > 0 ? generateInScopePositive(n2) : generateInScopeNegative(n2);
            } while (n1 == 0);
            return typeOfTask.apply(new Params(n1, op, n2, false));
            //return new EquationMathTask(n1, op, n2, false);
        }
        int n1 = op.equals(Operation.DIV) ? generateNonZero() : randUsual();
        int n2 = randUsual();
        return typeOfTask.apply(new Params(n1, op, n2, false));
        //return new EquationMathTask(n1, op, n2, xStart);
    }
}
