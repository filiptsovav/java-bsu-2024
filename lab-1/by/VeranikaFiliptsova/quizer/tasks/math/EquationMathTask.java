package by.VeranikaFiliptsova.quizer.tasks.math;

import by.VeranikaFiliptsova.quizer.Result;

public class EquationMathTask extends AbstractMathTask {
    boolean xStart;


    public EquationMathTask(int n1, Operation op, int n2, boolean xSt) {
        super(n1, op, n2);
        xStart = xSt;
    }

    public int calculate() {
        return switch(operation) {
            case SUM -> num2 - num1;
            case DIFF -> xStart ? num2 + num1 : num1 - num2;
            case MUL -> num2 / num1;
            case DIV -> xStart ? num1 * num2 : num1 / num2;
        };
    }

    @Override
    public String getText() {
        if (xStart) return "x"+operation.getSymbol()+myValueOf(num1)+"="+num2;
        return myValueOf(num1)+operation.getSymbol()+"x"+"="+num2;
    }

    @Override
    public Result validate(String answer) {
        return super.validate(answer);
    }

}