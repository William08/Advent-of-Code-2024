import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Scanner;
import java.io.File;

public class day1 {
    public static void main(String[] args) {
        new day1().run();
    }

    public void run(){
        String input_string = getInputString("input");

        List<Integer> l1 = new ArrayList<>();
        List<Integer> l2 = new ArrayList<>();

        for (String input : input_string.split("\n")) {
            String[] s = input.split("   ");
            l1.add(Integer.parseInt(s[0]));
            l2.add(Integer.parseInt(s[1]));
        }

        l1.sort(Comparator.naturalOrder());
        l2.sort(Comparator.naturalOrder());

        //PART 1
        int sum = 0;
        for (int i = 0; i < l1.size(); i++) {
            sum += Math.abs(l1.get(i) - l2.get(i));
        }

        System.out.println(sum);

        //PART 2
        sum = 0;
        for (int i : l1) {
            sum += i * l2.stream().filter(x -> x == i).toArray().length;
        }

        System.out.println(sum);


    }
    
    public String getInputString(String path){
        StringBuilder sb = new StringBuilder();
        try {
            Scanner scanner = new Scanner(new File(path));
            while (scanner.hasNextLine()) sb.append(scanner.nextLine()).append("\n");
            scanner.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return sb.toString();
    }
}
