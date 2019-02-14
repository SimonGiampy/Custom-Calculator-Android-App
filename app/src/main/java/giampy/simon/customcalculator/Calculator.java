package giampy.simon.customcalculator;

import android.util.Log;

import com.udojava.evalex.Expression; //find the library at: https://github.com/uklimaschewski/EvalEx
//this library is used for evaluate math String expression in a BigDecimal result

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.List;
import java.util.Random;

public class Calculator {       //classe principale di istruzioni che fanno funzionare la calcolatrice;

    public static final BigDecimal e = Constants.e;
    public static String pattern;
    public static boolean thousandsEnabled;
    public static String point;
    public static String separ;
    public static char cPoint;
    public static char cSepar;
    public static int DRG;

    public static boolean factorial = false;

    public Calculator(Constants constants, int drg) {
        point = constants.getPoint();
        pattern = constants.getPattern();
        separ = constants.getSeparator();
        cPoint = constants.getcPoint();
        cSepar = constants.getcSeparator();
        thousandsEnabled = constants.isEnabledThousands();
        DRG = drg;
    }

    public static void setDRG(int newDRG) {
        DRG = newDRG;
    }

    public static String write_numbers(String txt, String number_pressed) { //chiamato quando vengono premuti i numeri nella calcolatrice
        int length = txt.length() - 1;

        //caso particolare
        if (txt.equals("0")) {
            txt = "";
        }
        if (!txt.equals("")) {
            if (txt.charAt(length) == '0') {
                if (txt.charAt(txt.length() - 2) == '%' || txt.charAt(txt.length() - 2) == '+' || txt.charAt(txt.length() - 2) == '×' || txt.charAt(txt.length() - 2) == '÷' || txt.charAt(txt.length() - 2) == '−' || txt.charAt(txt.length() - 2) == '(') {
                    //simbolo succeduto da uno zero e poi un numero premuto: va rimpiazzato lo zero
                    txt = txt.substring(0, length);
                }
            } else if (txt.length() >= 1) {
                if (txt.charAt(txt.length() - 1) == ')') {
                    txt = txt.substring(0, txt.lastIndexOf(")") + 1).concat("×");
                }
                if (txt.charAt(txt.length() - 1) == '!') {
                    txt = txt.substring(0, txt.lastIndexOf("!") + 1).concat("×");
                }
                if (txt.charAt(txt.length() - 1) == 'e') {
                    txt = txt.substring(0, txt.lastIndexOf("e") + 1).concat("×");
                }
                if (txt.charAt(txt.length() - 1) == 'π') {
                    txt = txt.substring(0, txt.lastIndexOf("π") + 1).concat("×");
                }
            }
        }
        txt += number_pressed;
        String output = format(txt);
        if (output == null) {
            output = txt;
        }

        return output;
    }

    public static String write_comma(String txt) { //chiamato quando viene premuta la virgola
        String output;
        int length = txt.length() - 1;

        if (txt.length() == 0) {
            //controlla se viene scritta come primo carattere
            output = "0" + point;
        } else if (txt.charAt(length) == '×' || txt.charAt(length) == '÷' || txt.charAt(length) == '%' || txt.charAt(length) == '−' || txt.charAt(length) == '+' || txt.charAt(length) == '(') {
            //vede prima è stato scritto un simbolo
            output = txt + "0" + point;
        } else if (txt.charAt(length) == ')' || txt.charAt(length) == '!' || txt.charAt(length) == 'e' || txt.charAt(length) == 'π') {
            output = txt;
        } else {
            //controlla che non venga premuta per due volte in uno stesso numero la virgola
            String confronto = "";
            boolean find = false;
            int i = txt.length();
            while (!find && confronto.equals("")) {
                i -= 1;
                if (txt.charAt(i) == '×' || txt.charAt(i) == '÷' || txt.charAt(i) == '%' || txt.charAt(i) == '−' || txt.charAt(i) == '+' || txt.charAt(i) == '(') {
                    find = true;
                }
                if (i == 0 && !find) {
                    confronto = txt;
                }
            }

            if (confronto.equals("")) {
                confronto = txt.substring(i + 1, length + 1);
            }
            find = false;
            i = 0;
            while (!find && i < confronto.length()) {
                if (confronto.charAt(i) == cPoint) {
                    find = true;
                }
                i++;
            }
            if (!find) {
                output = txt + point;
            } else {
                output = txt;
            }
        }

        return output;
    }

    public static String write_symbol(String txt, String symbol_pressed) {
        String output;
        int i = txt.length() - 1;
        while (txt.length() > 0) {
            if (txt.charAt(i) == '%' || txt.charAt(i) == '+' || txt.charAt(i) == '×' || txt.charAt(i) == '÷' || txt.charAt(i) == '√' || txt.charAt(i) == '∛'
                    || txt.charAt(i) == '−' || txt.charAt(i) == cPoint || txt.charAt(i) == '(' || txt.charAt(i) == '^' || txt.charAt(i) == 'E' || txt.charAt(i) == 's' || txt.charAt(i) == 'i' || txt.charAt(i) == 'n' || txt.charAt(i) == 'c' || txt.charAt(i) == 'o' || txt.charAt(i) == 't' || txt.charAt(i) == 'a' || txt.charAt(i) == 'h') {
                txt = txt.substring(0, i);
                i -= 1;
            } else {
                if (txt.length() != 0) {
                    txt = txt + symbol_pressed;
                }
                break;
            }
        }
        output = txt;
        return output;
    }

    public static String write_minus(String txt) {
        String output;
        if (txt.length() == 0) {
            output = "−";
        } else if (txt.equals("−")) {
            output = "";
        } else if (txt.charAt(txt.length() - 1) == '−') {
            output = txt.substring(0, txt.length() - 1);
        } else if (txt.charAt(txt.length() - 1) == cPoint) {
            output = txt.substring(0, txt.length() - 1) + "−";
        } else {
            output = txt + "−"; //default
        }

        return output;
    }

    public static String write_parenthesis(String txt) {
        String output = "";
        int len = txt.length();
        //scrive le parentesi necessarie
        if (len == 0) {
            output = "(";
        } else if (txt.charAt(len - 1) == '(' || txt.charAt(len - 1) == '+' || txt.charAt(len - 1) == '×' || txt.charAt(len - 1) == '÷' || txt.charAt(len - 1) == '−' || txt.charAt(len - 1) == '%') {
            txt += "(";
            output = txt;
        } else if (txt.charAt(len - 1) == cPoint || txt.charAt(len - 1) == 'E') {
            txt = txt.substring(0, len - 1);
            txt += "×(";
            output = txt;
        } else {
            //calcola la differenza di quantità tra parentesi aperte e chiuse
            int open = 0;
            int close = 0;
            for (int i = 0; i <= len - 1; i++) {
                if (txt.charAt(i) == '(') {
                    open += 1;
                } else if (txt.charAt(i) == ')') {
                    close += 1;
                }
            }
            int dif = open - close;
            if (dif == 0) {
                txt += "×(";
                output = txt;
            } else if (dif > 0) {
                txt += ")";
                output = txt;
            }
        }

        return output;
    }

    public static String write_root(String txt, String root_pressed) {
        String output;
        if (txt.length() == 0) {
            output = root_pressed + "(";
        } else {
            if (txt.charAt(txt.length() - 1) == cPoint) {
                output = txt.substring(0, txt.length() - 1).concat("×").concat(root_pressed).concat("(");
            } else if (txt.charAt(txt.length() - 1) == '0' || txt.charAt(txt.length() - 1) == '1' || txt.charAt(txt.length() - 1) == '2' || txt.charAt(txt.length() - 1) == '3' || txt.charAt(txt.length() - 1) == '4' || txt.charAt(txt.length() - 1) == '5' ||
                    txt.charAt(txt.length() - 1) == '6' || txt.charAt(txt.length() - 1) == '7' || txt.charAt(txt.length() - 1) == '8' || txt.charAt(txt.length() - 1) == '9' || txt.charAt(txt.length() - 1) == ')' || txt.charAt(txt.length() - 1) == '!' || txt.charAt(txt.length() - 1) == 'π' || txt.charAt(txt.length() - 1) == 'e') {
                output = txt.concat("×").concat(root_pressed).concat("(");
            } else {
                output = txt.concat(root_pressed).concat("(");
            }
        }
        return output;
    }

    public static String write_power(String txt, String power_pressed) {
        String output = "";
        if (txt.length() != 0) {
            if (txt.equals("−")) {
                output = txt;
            } else {
                while (txt.length() > 0) {
                    if (!(txt.charAt(txt.length() - 1) == '0' || txt.charAt(txt.length() - 1) == '1' || txt.charAt(txt.length() - 1) == '2' || txt.charAt(txt.length() - 1) == '3' || txt.charAt(txt.length() - 1) == '4' || txt.charAt(txt.length() - 1) == '5' ||
                            txt.charAt(txt.length() - 1) == '6' || txt.charAt(txt.length() - 1) == '7' || txt.charAt(txt.length() - 1) == '8' || txt.charAt(txt.length() - 1) == '9' || txt.charAt(txt.length() - 1) == ')' || txt.charAt(txt.length() - 1) == 'π' || txt.charAt(txt.length() - 1) == 'e')) {
                        txt = txt.substring(0, txt.length() - 1);
                    } else {
                        break;
                    }
                }
                if (!txt.equals("")) {//default
                    if (power_pressed.equals("x")) {
                        power_pressed = "(";
                    }
                    output = txt + "^" + power_pressed;
                }
            }
        }
        return output;
    }

    public static String write_fact(String txt) {
        String output;
        int i = txt.length() - 1;
        while (txt.length() > 0) {
            if (txt.charAt(i) == '%' || txt.charAt(i) == '+' || txt.charAt(i) == '×' || txt.charAt(i) == '÷' || txt.charAt(i) == '√' || txt.charAt(i) == '∛' || txt.charAt(i) == '!'
                    || txt.charAt(i) == '−' || txt.charAt(i) == cPoint || txt.charAt(i) == '(' || txt.charAt(i) == '^' || txt.charAt(i) == 'E' || txt.charAt(i) == 's' || txt.charAt(i) == 'i' || txt.charAt(i) == 'n' || txt.charAt(i) == 'c' || txt.charAt(i) == 'o' || txt.charAt(i) == 't' || txt.charAt(i) == 'a' || txt.charAt(i) == 'h') {
                txt = txt.substring(0, i);
                i -= 1;
            } else {
                txt += "!";
                break;
            }
        }
        output = txt;

        return output;
    }

    public static String write_SCTHLOGSPIE(String txt, String button) {
        String output;

        if (txt.length() > 0) {
            if (txt.charAt(txt.length() - 1) == '0' || txt.charAt(txt.length() - 1) == '1' || txt.charAt(txt.length() - 1) == '2' || txt.charAt(txt.length() - 1) == '3' || txt.charAt(txt.length() - 1) == '4' || txt.charAt(txt.length() - 1) == '5' ||
                    txt.charAt(txt.length() - 1) == '6' || txt.charAt(txt.length() - 1) == '7' || txt.charAt(txt.length() - 1) == '8' || txt.charAt(txt.length() - 1) == '9' || txt.charAt(txt.length() - 1) == ')' || txt.charAt(txt.length() - 1) == 'e' || txt.charAt(txt.length() - 1) == 'π' || txt.charAt(txt.length() - 1) == '!') {
                txt += "×";
            } else if (txt.charAt(txt.length() - 1) == cPoint) {
                txt = txt.substring(0, txt.length() - 1) + "×";
            }
        }
        if (button.equals("π") || button.equals("e")) {
            output = txt + button;
        } else if (button.length() > 6) {
            output = txt + button;
        } else {
            output = txt + button + "(";
        }

        return output;
    }

    public static String write_exp(String txt) {
        String output = txt;
        if (txt.length() > 0) {
            if (txt.charAt(txt.length() - 1) == '0' || txt.charAt(txt.length() - 1) == '1' || txt.charAt(txt.length() - 1) == '2' || txt.charAt(txt.length() - 1) == '3' || txt.charAt(txt.length() - 1) == '4' || txt.charAt(txt.length() - 1) == '5' ||
                    txt.charAt(txt.length() - 1) == '6' || txt.charAt(txt.length() - 1) == '7' || txt.charAt(txt.length() - 1) == '8' || txt.charAt(txt.length() - 1) == '9' || txt.charAt(txt.length() - 1) == ')' || txt.charAt(txt.length() - 1) == 'e' || txt.charAt(txt.length() - 1) == 'π') {
                output = txt + "E";
            }
        }
        return output;
    }

    public static String check(String txt) {
        String output;
        int len = txt.length();
        //controlla che l' espressione aritmetica sia scritta correttamente
        try {
            boolean cond = true;
            while (cond) {
                if (txt.charAt(len - 1) == '+' || txt.charAt(len - 1) == '×' || txt.charAt(len - 1) == '÷' || txt.charAt(len - 1) == '−' || txt.charAt(len - 1) == '%' || txt.charAt(len - 1) == '(' || txt.charAt(len - 1) == cPoint || txt.charAt(len - 1) == '√' || txt.charAt(len - 1) == '^' || txt.charAt(len - 1) == '∛' || txt.charAt(len - 1) == 'E' || txt.charAt(len - 1) == 's' || txt.charAt(len - 1) == 'i' || txt.charAt(len - 1) == 'n' || txt.charAt(len - 1) == 'c' || txt.charAt(len - 1) == 'o' || txt.charAt(len - 1) == 't' || txt.charAt(len - 1) == 'a' || txt.charAt(len - 1) == 'h' || txt.charAt(len - 1) == 'l' || txt.charAt(len - 1) == 'g') {
                    txt = txt.substring(0, len - 1);
                } else {
                    cond = false;
                }
                len = txt.length();
            }
        } catch (StringIndexOutOfBoundsException ex) {
            txt = "null";
        }
        //aggiunge parentesi omesse
        int open = 0;
        int close = 0;
        for (int i = 0; i <= len - 1; i++) {
            if (txt.charAt(i) == '(') {
                open += 1;
            } else if (txt.charAt(i) == ')') {
                close += 1;
            }
        }
        int dif = open - close;
        if (dif > 0) {
            for (int i = 0; i < dif; i++) {
                txt += ")";
            }
        }

        if (txt.length() >= 2) {
            String[] temp = new String[]{"sin", "cos", "tan", "ln", "log", "sinh", "cosh", "tanh"};
            boolean cond = false;
            for (String aTemp : temp) {
                if (txt.equals(aTemp)) {
                    cond = true;
                }
            }
            if (cond) {
                txt = "0";
            }
        }

        output = txt;
        return output;
    }

    public static String makeExpression(String txt) {
        //impostazione stringa da risolvere
        String expression = txt;
        if (expression.contains("÷")) {
            expression = expression.replaceAll("÷", "/");
        }
        if (expression.contains("×")) {
            expression = expression.replaceAll("×", "*");
        }
        if (expression.contains("−")) {
            expression = expression.replaceAll("−", "-");
        }
        if (expression.contains("E+")) {
            expression = expression.replace("E+", "$");
        }
        if (expression.contains("E")) {
            expression = expression.replace("E", "$");
        }
        if (thousandsEnabled) {
            if (expression.contains(", ")) {
                expression = expression.replaceAll(", ", "K");
            }
            if (expression.contains(separ)) {
                if (separ.equals(".")) {
                    expression = expression.replaceAll("\\.", "");
                } else {
                    expression = expression.replaceAll(separ, "");
                }
            }

            if (expression.contains(point) && !point.equals(".")) {
                expression = expression.replaceAll(point, "\\.");
            }

            if (expression.contains("K")) {
                expression = expression.replaceAll("K", ", ");
            }
        }
        if (expression.contains("√")) {
            expression = expression.replaceAll("√", "SQRT");
        }
        if (expression.contains("sinh")) {
            expression = expression.replaceAll("sinh", "SINH");
        }
        if (expression.contains("cosh")) {
            expression = expression.replaceAll("cosh", "COSH");
        }
        if (expression.contains("tanh")) {
            expression = expression.replaceAll("tanh", "TANH");
        }
        if (expression.contains("sin")) {
            expression = expression.replaceAll("sin", "SIN");
        }
        if (expression.contains("cos")) {
            expression = expression.replaceAll("cos", "COS");
        }
        if (expression.contains("tan")) {
            expression = expression.replaceAll("tan", "TAN");
        }
        if (expression.contains("ln")) {
            expression = expression.replaceAll("ln", "LOG");
        }
        if (expression.contains("log")) {
            expression = expression.replaceAll("log", "LOG10");
        }
        if (expression.contains("π")) {
            expression = expression.replaceAll("π", "PI");
        }
        if (expression.contains("e")) {
            expression = expression.replaceAll("e", e.toString());
        }
        if (expression.contains("∛")) {
            expression = expression.replaceAll("∛", "CBRT");
        }
        if (expression.contains("!")) {
            int i;
            while (expression.contains("!")) {
                int x = -1;
                i = expression.indexOf("!") - 2;
                while (x == -1) {
                    if (i == -1) {
                        x = 0;
                    } else if (expression.charAt(i) == '*' || expression.charAt(i) == '-' || expression.charAt(i) == '/' || expression.charAt(i) == '+' || expression.charAt(i) == '%' || expression.charAt(i) == '(' || expression.charAt(i) == '^') {
                        x = i + 1;
                    }
                    i--;
                }
                String n = expression.substring(x, expression.indexOf("!"));
                String post = expression.substring(expression.indexOf("!") + 1);
                expression = expression.replaceFirst("!", "");
                expression = expression.substring(0, x).concat("F(").concat(n).concat(")").concat(post);
            }
        }

        //imposta i radianti e i gradi centesimali per le funzioni trigonometriche
        int i = 0;
        int p;
        int h = 1;
        int ind = 0;
        int f;
        int k = 0;
        String s = "(rad";
        if (DRG == 1 || DRG == 2) {
            if (DRG == 2) {
                k = 1;
                s = "(grad";
            }
            if (expression.contains("SIN") || expression.contains("COS") || expression.contains("TAN")) {
                if (expression.length() > 3) {
                    while (i <= expression.length() - 6) {
                        if (expression.substring(i, i + 4).equals("SIN(") || expression.substring(i, i + 4).equals("COS(") || expression.substring(i, i + 4).equals("TAN(")) {
                            expression = expression.substring(0, i + 3) + s + expression.substring(i + 3);
                            p = i + 8 + k;
                            f = i + 8 + k;
                            while (h != 0 && p < expression.length()) {
                                if (expression.charAt(p) == '(') {
                                    h += 1;
                                } else if (expression.charAt(p) == ')') {
                                    h -= 1;
                                }
                                if (h == 0) {
                                    ind = p;
                                }
                                p++;
                            }
                            expression = expression.substring(0, ind) + ")" + expression.substring(ind);
                            i = f - 1;
                            h = 1;
                        }
                        i++;
                    }
                }
            }
        }

        //tricky error: -(nums and symbols) blabla ---> EmptyStackException
        //like: -(123*35*(-2))/12.34
        //become  -->  (-123*35*(-2))/12.34
        String tricky = expression;
        if (tricky.length() >= 2) {
            if (tricky.substring(0, 2).equals("-(")) {
                tricky = "(-" + tricky.substring(2, tricky.length());
                if (tricky.contains("--")) {
                    tricky = tricky.replace("--", "");
                }
                expression = tricky;
            }
        }
        return expression;
    }

    public static String equal(String txt) {//risolve l' espressione finale
        String output;

        String expression = makeExpression(txt);
        Log.d("tag", "expression: " + expression);
        //trasforma la stringa di testo in un espressione matematica e la risolve
        int errorControl = 0;
        final BigDecimal bigResult;
        String result = null;
        try {
            Expression value = new Expression(expression);
            value.addOperator(value.new Operator("%", 40, true) {
                @Override
                public BigDecimal eval(BigDecimal v1, BigDecimal v2) {
                    return v2.divide(new BigDecimal(100)).multiply(v1);
                }
            });
            value.addOperator(value.new Operator("$", 50, true) {
                @Override
                public BigDecimal eval(BigDecimal v1, BigDecimal v2) {
                    return v1.multiply(BigDecimal.valueOf(Math.pow(10, v2.doubleValue())));
                }
            });
            value.addFunction(value.new Function("F", 1) {
                @Override
                public BigDecimal eval(List<BigDecimal> parameters) {
                    BigDecimal res = BigDecimal.ONE;
                    int val = parameters.get(0).intValue();
                    if (parameters.get(0).doubleValue() > 100) {
                        val = 100;
                        factorial = true;
                    }
                    for (int i = 1; i <= val; i++) {
                        res = res.multiply(BigDecimal.valueOf(i));
                    }
                    return res;
                }
            });
            value.addFunction(value.new Function("CBRT", 1) {
                @Override
                public BigDecimal eval(List<BigDecimal> parameters) {
                    return BigDecimal.valueOf(Math.cbrt(parameters.get(0).doubleValue()));
                }
            });
            value.addFunction(value.new Function("rad", 1) {
                @Override
                public BigDecimal eval(List<BigDecimal> parameters) {
                    BigDecimal val = new BigDecimal(180);
                    val = val.divide(new BigDecimal(3.141592654), 10, BigDecimal.ROUND_CEILING);
                    val = parameters.get(0).multiply(val);
                    if (val.toString().contains("9.999999")) {
                        val = BigDecimal.ZERO;
                    }
                    return val;
                }
            });
            value.addFunction(value.new Function("grad", 1) {
                @Override
                public BigDecimal eval(List<BigDecimal> parameters) {
                    return parameters.get(0).divide(new BigDecimal(9), 9, BigDecimal.ROUND_HALF_UP).multiply(new BigDecimal(10));
                }
            });
            value.addFunction(value.new Function("random", 2) {
                @Override
                public BigDecimal eval(List<BigDecimal> parameters) {
                    long min = (long) Math.floor(parameters.get(0).longValue());
                    long max = (long) Math.floor(parameters.get(1).longValue());

                    long result = (long) (Math.random() * (max - min + 1) + min);
                    return BigDecimal.valueOf(result);
                }
            });
            value.setPrecision(12);
            if (!expression.equals("")) {
                //formattazione risultato
                DecimalFormatSymbols decimalFormatSymbols = new DecimalFormatSymbols();
                if (thousandsEnabled) {
                    decimalFormatSymbols.setGroupingSeparator(cSepar);
                }
                decimalFormatSymbols.setDecimalSeparator(cPoint);
                DecimalFormat formatter = new DecimalFormat(pattern, decimalFormatSymbols);

                bigResult = value.eval();

                if (bigResult.toString().contains("E+")) {
                    if (bigResult.toString().substring(bigResult.toString().indexOf("+"), bigResult.toString().length() - 1).length() >= 2) {
                        result = bigResult.toString();
                        if (!separ.equals(",") && thousandsEnabled) {
                            result = result.replace(separ, point);
                        }
                    }
                }

                if (bigResult.toString().length() > 15) {
                    if (bigResult.toString().contains("E+")) {
                        if (bigResult.toString().substring(2, bigResult.toString().indexOf("+")).length() >= 10) {
                            result = bigResult.toString().substring(0, 12).concat("E").concat(bigResult.toString().substring(bigResult.toString().indexOf("+")));
                        }
                    }
                }

                Log.d("tag", "bigresult:" + bigResult.toString());
                if (result == null) {
                    result = formatter.format(bigResult.doubleValue());
                    Log.d("tag", "result:" + result);
                }
                Log.d("tag", "result:" + result);
            } else {
                errorControl = 1;
            }
        } catch (ArithmeticException | Expression.ExpressionException | NumberFormatException ex) {
            errorControl = 2;
            Log.d("tag", ex.toString());
        } finally {
            output = "=" + result;
            if (factorial) {
                output += "toast";
                factorial = false;
            }
        }
        //controllo errori matematici
        switch (errorControl) {
            case 1: {
                output = "Azzera";
            }
            break;
            case 2: {
                output = "Error";
            }
            break;
        }

        return output;
    }

    public static String format(String txt) {//formattazione numero
        String output = null;
        if (thousandsEnabled) {
            int count = txt.length();
            int index = count - 1;
            if (count >= 4 && (txt.charAt(index) == '0' || txt.charAt(index) == '1' || txt.charAt(index) == '2' || txt.charAt(index) == '3' || txt.charAt(index) == '4' || txt.charAt(index) == '5' || txt.charAt(index) == '6' ||
                    txt.charAt(index) == '7' || txt.charAt(index) == '8' || txt.charAt(index) == '9' || txt.charAt(index) == cPoint)) {
                boolean find = false;
                int pos = 0;
                while (!find) {
                    if (index == -1) {
                        find = true;
                    } else {
                        if (txt.charAt(index) != '0' && txt.charAt(index) != '1' && txt.charAt(index) != '2' && txt.charAt(index) != '3' && txt.charAt(index) != '4' && txt.charAt(index) != '5' && txt.charAt(index) != '6' &&
                                txt.charAt(index) != '7' && txt.charAt(index) != '8' && txt.charAt(index) != '9' && txt.charAt(index) != cPoint && txt.charAt(index) != cSepar) {
                            find = true;
                        } else {
                            pos += 1;
                        }
                        index -= 1;
                    }
                }
                //controllo finale
                String number = txt.substring(count - pos, count);
                if (number.contains(separ)) {
                    if (separ.equals(".")) {
                        number = number.replaceAll("\\.", "");
                    } else {
                        number = number.replaceAll(separ, "");
                    }
                }
                String pre = txt.substring(0, count - pos);
                if (number.contains(point)) {
                    if (!point.equals(".")) {
                        number = number.replaceAll(point, ".");
                    }
                }

                //formatta le cifre non decimali (vengono aggiunte per evitare qualche bug)
                String post = null;
                if (pos >= 4) { //separs
                    if (number.contains(".")) {
                        if (number.charAt(number.length() - 1) == '0') {
                            post = number.substring(number.indexOf(".") + 1, number.length());
                            number = number.substring(0, number.indexOf("."));
                        }
                    }
                    if (post != null) {
                        if (post.length() > 9) {
                            post = post.substring(0, post.length() - 1);
                        }
                    }

                    double num = Double.parseDouble(number);
                    //formattazione del numero trovato
                    DecimalFormatSymbols decimalFormatSymbols = new DecimalFormatSymbols();
                    decimalFormatSymbols.setGroupingSeparator(cSepar);
                    decimalFormatSymbols.setDecimalSeparator(cPoint);
                    DecimalFormat formatter = new DecimalFormat(pattern, decimalFormatSymbols);
                    String formatted = formatter.format(num);
                    //output del risultato
                    output = pre.concat(formatted);
                    if (post != null) { //controlla la formattazione dopo la virgola
                        output = output.concat(point).concat(post);
                    }
                }

            }
        }
        return output;
    }
}