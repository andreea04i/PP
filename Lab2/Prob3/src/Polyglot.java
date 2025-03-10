import org.graalvm.polyglot.*;

public class Polyglot {
    public static void main(String[] args) {
        //se creeaza un context polyglot
        Context context = Context.newBuilder().allowAllAccess(true).build();
        String py_code =
                "def f1():\n" +
                        "n=input('Introduceti numarul de aruncari: ')\n" +
                        "x=input('Introduceti numarul x: ')\n" +
                        "return n,x\n" +
                        "f1()";
        Value pyResult=context.eval("python", py_code);
        //extrage cele doua elemente din pyResult sub forma de intreg
        int n=pyResult.getArrayElement(0).asInt();
        int x=pyResult.getArrayElement(1).asInt();
        //functia pentru calculul probabilitatii unei distributii binomiale de obtine pajura de cel mult x ori
        String R_code=
                "f2 <- function(n, x) {\n" +
                        "prob <- pbinom(x,size = n,prob = 0.5)\n" +//prob=0.5 - probabilitatea unei  monede de a obtine pajura/cap
                        "return(prob)\n" +
                        "}\n" +
                        "f2(" + n + ", " + x + ")";
        //extragem rezultatul din R
        Value rez=context.eval("R",R_code);
        double prob=rez.asDouble();
        System.out.println("Probabilitatea este: "+ prob);

    }
}