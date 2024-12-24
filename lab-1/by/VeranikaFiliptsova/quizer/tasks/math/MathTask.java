package by.VeranikaFiliptsova.quizer.tasks.math;

import by.VeranikaFiliptsova.quizer.Task;

public interface MathTask extends Task {
    enum Operation {

        SUM("+"),
        DIFF("-"),
        MUL("*"),
        DIV("/");

        Operation(String symbol) {
            this.symbol = symbol;
        }

        public String getSymbol(){
            return symbol;
        }
        private final String symbol;
    }
    String myValueOf(int a);
    /**
     @return правильный ответ
     */
    int calculate();

}
