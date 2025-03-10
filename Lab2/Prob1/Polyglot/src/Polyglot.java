
import org.graalvm.polyglot.*;
import java.util.*;

class Polyglot {
    //conversia in majuscule
    private static String RToUpper(String token){
        //creeaza un context polyglot
        Context polyglot = Context.newBuilder().allowAllAccess(true).build();
        Value rez = polyglot.eval("R", "toupper(\""+token+"\");");
        //extrage rezultatul ca sir de caractere
        String resultString = rez.asString();
        polyglot.close();
        return resultString;
    }

    //functie pentru suma de control cu Python
    private static int SumCRC(String token){
        //elimina prima si ultima litera din cuvant
        String tokenWithoutFirstAndLast=token.substring(1,token.length()-1);
        Context polyglot = Context.newBuilder().allowAllAccess(true).build();
        // i -  reprezinta pozitia caracterului in sir ; ord(ch) - codul ASCII pentru caracter
        Value result = polyglot.eval("python","sum((5 * i**5 + 4 * i**4 + 3 * i**3 + 2* i**2 + 1*i +7) * ord(ch)" + "for i, ch in enumerate('"+tokenWithoutFirstAndLast+"'))");
        //suma de control este salvata sub forma de intreg
        int resultInt = result.asInt();
        polyglot.close();

        return resultInt;
    }

    public static void main(String[] args) {
        //creeaza un context Polyglot
        Context polyglot = Context.create();
        //construim un array de string-uri
        Value array = polyglot.eval("js", "[\"If\",\"we\",\"run\",\"the\",\"java\",\"jhi\",\"tun\",\"the\",\"avaa\"]");
        List<String> cuvinte =new ArrayList<>();//lista pentru stocarea cuvintelor
        List<Integer> sume_de_cntrl= new ArrayList<>();//lista pentru stocarea sumelor de control
        for (int i = 0; i < array.getArraySize();i++){
            String element = array.getArrayElement(i).asString();
            String upper = RToUpper(element);
            int crc = SumCRC(upper);
            cuvinte.add(element);
            sume_de_cntrl.add(crc);
        }
        //parcurge cele doua liste si afiseaza cuvintele cu aceasi suma de control
        for(int i=0;i<sume_de_cntrl.size();i++)
        {
            if(cuvinte.get(i)!=null) {
                int crc = sume_de_cntrl.get(i);
                //afiseaza suma de control si cuvintele ce o detin
                System.out.printf(sume_de_cntrl.get(i)+ ": "+cuvinte.get(i) + " ");
                for (int j = i + 1; j < sume_de_cntrl.size(); j++) {
                    if (sume_de_cntrl.get(j) == crc) {
                        System.out.printf(cuvinte.get(j) + " ");
                        //opreste afisarea duplicata a cuvintelor
                        cuvinte.set(j, null);
                    }
                }
                System.out.println();
            }
        }
        polyglot.close();
    }
}

