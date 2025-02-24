import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.util.StringTokenizer;
import javax.swing.*;
import java.util.*;

public class Calculator extends JFrame {
    JButton digits[] = {
            new JButton(" 0 "),
            new JButton(" 1 "),
            new JButton(" 2 "),
            new JButton(" 3 "),
            new JButton(" 4 "),
            new JButton(" 5 "),
            new JButton(" 6 "),
            new JButton(" 7 "),
            new JButton(" 8 "),
            new JButton(" 9 ")
    };

    JButton operators[] = {
            new JButton(" + "),
            new JButton(" - "),
            new JButton(" * "),
            new JButton(" / "),
            new JButton(" = "),
            new JButton(" C "),
            new JButton(" ( "),
            new JButton(" ) ")
    };

    String oper_values[] = {"+", "-", "*", "/", "=", "" , "(", ")"};

    String value;
    char operator;

    JTextArea area = new JTextArea(3, 5);

    public static void main(String[] args) {
        Calculator calculator = new Calculator();
        calculator.setSize(230, 240);
        calculator.setTitle(" Java-Calc, PP Lab1 ");
        calculator.setResizable(false);
        calculator.setVisible(true);
        calculator.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    public Calculator() {
        add(new JScrollPane(area), BorderLayout.NORTH);
        JPanel buttonpanel = new JPanel();
        buttonpanel.setLayout(new FlowLayout());

        for (int i=0;i<10;i++)
            buttonpanel.add(digits[i]);

        for (int i=0;i<8;i++)
            buttonpanel.add(operators[i]);

        add(buttonpanel, BorderLayout.CENTER);
        area.setForeground(Color.BLACK);
        area.setBackground(Color.WHITE);
        area.setLineWrap(true);
        area.setWrapStyleWord(true);
        area.setEditable(false);

        for (int i=0;i<10;i++) {
            int finalI = i;
            digits[i].addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent actionEvent) {
                    area.append(Integer.toString(finalI));
                }
            });
        }

        for (int i=0;i<8;i++){
            int finalI = i;
            operators[i].addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent actionEvent) {
                    if (finalI == 5)
                        area.setText("");
                    else
                    if (finalI == 4) {
                        String expression=area.getText();
                        try {
                            String postfix = infixToPostfix(expression);
                            System.out.println("Forma postfix: " + postfix);
                            double result = evaluatePostfix(postfix);
                            System.out.println("Rezultatul este: " + result);
                            area.setText(" = "+result);
                        } catch (Exception e) {
                            area.setText(" !!!Probleme!!! ");
                        }
                    }
                    else {
                        area.append(oper_values[finalI]);
                        operator = oper_values[finalI].charAt(0);
                    }
                }
            });
        }

    }
    
    public static String infixToPostfix (String expression) throws Exception {
        Stack<Character> operators = new Stack<>();
        StringBuilder postfix = new StringBuilder();
        StringTokenizer tokenizer = new StringTokenizer(expression, "+-*/() ", true);

        while (tokenizer.hasMoreTokens()) {
            String token = tokenizer.nextToken().trim();
            if (token.isEmpty()) continue;

            if (isNumber(token)) {
                postfix.append(token).append(" ");
            } else if (isOperator(token)) {
                while (!operators.isEmpty() && precedence(operators.peek()) >= precedence(token.charAt(0))) {
                    postfix.append(operators.pop()).append(" ");
                }
                operators.push(token.charAt(0));
            } else if (token.equals("(")) {
                operators.push('(');
            } else if (token.equals(")")) {
                while (!operators.isEmpty() && operators.peek() != '(') {
                    postfix.append(operators.pop()).append(" ");
                }
                if (operators.isEmpty() || operators.pop() != '(') {
                    throw new Exception("Paranteze neechilibrate.");
                }
            } else {
                throw new Exception("Token necunoscut: " + token);
            }
        }

        while (!operators.isEmpty()) {
            char op = operators.pop();
            if (op == '(' || op == ')') throw new Exception("Paranteze neechilibrate.");
            postfix.append(op).append(" ");
        }

        return postfix.toString().trim();
    }

    public static double evaluatePostfix (String expression) throws Exception {
        Stack<Double> stack = new Stack<>();
        String[] tokens = expression.split(" ");

        for (String token : tokens) {
            if (isNumber(token)) {
                stack.push(Double.parseDouble(token));
            } else if (isOperator(token)) {
                double b = stack.pop();
                double a = stack.pop();
                stack.push(applyOperator(token.charAt(0), a, b));
            } else {
                throw new Exception("Token necunoscut: " + token);
            }
        }

        return stack.pop();
    }

    private static boolean isNumber (String token){
        try {
            Double.parseDouble(token);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    private static boolean isOperator (String token){
        return token.equals("+") || token.equals("-") || token.equals("*") || token.equals("/");
    }


    private static int precedence ( char operator){
        switch (operator) {
            case '+':
            case '-':
                return 1;
            case '*':
            case '/':
                return 2;
            default:
                return -1;
        }
    }

    private static double applyOperator ( char operator, double a, double b) throws Exception {
        switch (operator) {
            case '+':
                return a + b;
            case '-':
                return a - b;
            case '*':
                return a * b;
            case '/':
                if (b == 0) throw new Exception("Împărțirea la zero nu este permisă.");
                return a / b;
            default:
                throw new Exception("Operator necunoscut: " + operator);
        }
    }
}


