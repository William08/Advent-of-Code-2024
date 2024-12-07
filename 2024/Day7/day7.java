import java.io.BufferedReader;
import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.stream.Stream;

public class day7 {

    static String INPUT_FILE = "input";

    public static void main(String[] args) {
        new day7().run();
    }

    public void run(){
        StringBuilder stringBuilder = new StringBuilder();
        try {
            Scanner scanner = new Scanner(new File(INPUT_FILE));
            while (scanner.hasNextLine()) {
                stringBuilder.append(scanner.nextLine()).append("\n");
        }
        scanner.close();
        } catch (Exception e) {
            // TODO: handle exception
        }

        String[] expressions = stringBuilder.toString().split("\n");
        List<Long> results = new ArrayList<>();
        for (String expression : expressions) {
            if(canCalculate(ParseExpression(expression))) results.add(ParseExpression(expression)[0]);
        }
        System.out.println(results.stream().mapToLong(i -> i).sum());

    }

    long[] ParseExpression(String expression){
        return Arrays.stream(expression.replaceAll(":", "").split(" "))
                .map(s -> s.trim())
                .mapToLong(s -> Long.parseLong(s))
                .toArray();
    }

    boolean canCalculate(long[] expression){
        long result = expression[0];
        long[] operands = Arrays.stream(expression).skip(1).toArray();
        return recursiveCalculate(result, operands);

    }


    boolean recursiveCalculate(long result, long operands[]){
        if (operands[0] > result) return false;
        //int op2 = rest[0];
        if (operands.length > 1){
            long x = operands[0];
            long y = operands[1];
            long[] multiRest = Arrays.stream(operands).skip(1).toArray();
            long[] addRest   = multiRest.clone();
            long[] concatRest = multiRest.clone();
            concatRest[0] = concat(x, concatRest[0]);
            multiRest[0] *= x;
            addRest[0] += x;

            if     (recursiveCalculate(result, multiRest))  return true;
            else if(recursiveCalculate(result, concatRest)) return true;
            else if(recursiveCalculate(result, addRest))    return true;
            else                                            return false;
        }
        else{
            if (operands[0] == result) return true;
            else                       return false;
        }
    }

    long concat(long x, long y){
        return Long.parseLong("" + x + y);
    }

    /* 
    z = x * y
    if too much return false
    if there are more values: if(recursion(x*y, rest)) return true else return recursion(x+y)
    if no more values  
    if x*y = z then return true
    else            return false
    */

}