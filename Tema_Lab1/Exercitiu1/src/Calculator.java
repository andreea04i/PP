import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.*;
import java.util.Stack;

public class Calculator extends JFrame{
    JButton digits[] ={
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

    JButton operators[] ={
            new JButton(" + "),
            new JButton(" - "),
            new JButton(" * "),
            new JButton(" / "),
            new JButton(" = "),
            new JButton(" C "),
            new JButton(" ( "),
            new JButton(" ) ") //am adaugat 2 butoane pentru paranteze
    };

    String oper_values[] ={"+", "-", "*", "/", "=", "", "(", ")"};

    JTextArea area = new JTextArea(3, 5);

    public static void main(String[] args){
        Calculator calculator = new Calculator();
        calculator.setSize(300, 250);
        calculator.setTitle(" Java-Calc, PP Lab1 ");
        calculator.setResizable(false);
        calculator.setVisible(true);
        calculator.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    public Calculator(){
        add(new JScrollPane(area), BorderLayout.NORTH);
        JPanel buttonpanel = new JPanel();
        buttonpanel.setLayout(new FlowLayout());

        for (int i = 0; i < 10; i++)
            buttonpanel.add(digits[i]);

        for (int i = 0; i <8; i++)//dimensiunea vectorului a devenit 8
            buttonpanel.add(operators[i]);

        add(buttonpanel, BorderLayout.CENTER);
        area.setForeground(Color.BLACK);
        area.setBackground(Color.WHITE);
        area.setLineWrap(true);
        area.setWrapStyleWord(true);
        area.setEditable(false);

        for (int i = 0; i < 10; i++){
            int fI = i;
            digits[i].addActionListener(new ActionListener(){
                @Override
                public void actionPerformed(ActionEvent actionEvent){
                    area.append(Integer.toString(fI));
                }
            });
        }

        for (int i = 0; i < 8; i++){
            int fI = i;
            operators[i].addActionListener(new ActionListener(){
                @Override
                public void actionPerformed(ActionEvent actionEvent){
                    if (fI == 5)
                        area.setText("");
                    else if (fI == 4){
                        try{
                            String expression = area.getText();
                            String postfix = InfixtoPostfix(expression);
                            double solution = Finish(postfix);
                            area.append(" = " + solution);
                        } catch (Exception e){
                            area.setText(" !!!Probleme!!! ");
                        }
                    } else{
                        area.append(oper_values[fI]);
                    }
                }
            });
        }
    }

    public boolean isOperator(char operator)
    {
        return operator=='+' || operator=='-' || operator=='*' || operator=='/';
    }

    public int priority(char operator)
    {
        if(operator=='*' || operator=='/')
            return 2;
        if(operator=='+' || operator=='-')
            return 1;
        return 0;
    }

    public String InfixtoPostfix(String expression)
    {
        StringBuilder postfix = new StringBuilder(); // Stocam expresia convertita
        Stack<Character> operator = new Stack<>(); // Stocheaza operatorii + parantezele
        for(int i=0; i< expression.length(); i++)
        {
            char c= expression.charAt(i); //parcurgem expresia caracter cu caracter
            if(Character.isDigit(c))
            {
                StringBuilder number =new StringBuilder();
                while(i< expression.length() && Character.isDigit(expression.charAt(i)))
                {
                    number.append(expression.charAt(i));
                    i++;
                }
                i--;
                postfix.append(number.toString()).append(" ");
            }
            else if(c=='(')
            {
                operator.push(c);
            }
            else if(c==')')
            {
                while(!operator.isEmpty() && operator.peek()!='(')
                {
                    postfix.append(operator.pop()).append(" ");
                }
                operator.pop();
            }
            else if(isOperator(c))
            {
                while(!operator.isEmpty() && operator.peek()!='(' && priority(operator.peek())>=priority(c))
                {
                    postfix.append(operator.pop()).append(" ");//in cazul in care avem operatori de prioritate mai mare in Stack acestia sunt adaugati inaintea operatorului c in forma postfix
                }
                operator.push(c);
            }
        }
        while(!operator.isEmpty())
        {
            postfix.append(operator.pop()).append(" ");
        }
        return postfix.toString();
    }

    public double Finish(String expression){
        Stack<Double> result = new Stack<>();
        String[] p = expression.split(" ");
        for (String subs : p) {
            if (subs.isEmpty()) {
                continue; //
            }
            if (Character.isDigit(subs.charAt(0))) {
                result.push(Double.parseDouble(subs));
            } else if (isOperator(subs.charAt(0))) {
                double op2 = result.pop(); //
                double op1 = result.pop(); //
                switch (subs.charAt(0)) {
                    case '+':
                        result.push(op1 + op2);
                        break;
                    case '-':
                        result.push(op1 - op2);
                        break;
                    case '*':
                        result.push(op1 * op2);
                        break;
                    case '/':
                        result.push(op1 / op2);
                        break;
                }
            }
        }
        return result.pop();
    }
}
