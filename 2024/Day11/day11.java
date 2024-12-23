import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

public class day11 {

    public static void main(String[] args) {
        new day11().run();
    }

    public void run(){
        StringBuilder sb = new StringBuilder();
        try {
            Scanner scanner = new Scanner(new File("input"));
            while (scanner.hasNextLine()) {
                sb.append(scanner.nextLine());
            }
            scanner.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        String inputString = sb.toString();
        List<Long> stones = new ArrayList<>();
        for (String s : inputString.split(" ")) stones.add(Long.parseLong(s));

        long sum = 0;

        long time = System.currentTimeMillis();

        //First solution
        
        for (Long stone : stones) {
            List<Long> test = List.of(stone);
            for (int i = 0; i < 25; i++) {
                test = Iterate(test);
            }
            sum += test.size();
            
        }
        System.out.println(sum + "   |   Time: " + (System.currentTimeMillis() - time) + "ms");
        

        //PART 2
        sum = 0;
        for (Long stone : stones) {
            sum += recIterate(stone, 75, new HashMap<>()); 
        }

        System.out.println(sum + "   |   Time: " + (System.currentTimeMillis() - time) + "ms");

    }

    public List<Long> Iterate(List<Long> stones){
        List<Long> next = new ArrayList<>();

        for (int stone_index = 0; stone_index < stones.size(); stone_index++) {

            long       current_value     = stones.get(stone_index);
            String     current_as_string = Long.toString(current_value);
            List<Long> next_values;

            if      (current_value == 0)                  next_values = List.of((long)1);
            else if (current_as_string.length() % 2 == 0) next_values = splitInHalf(current_value);
            else                                          next_values = List.of(current_value * 2024);

            next.addAll(next_values);
        }

        return next;
        
    }

    public long recIterate(long value, int iterations, Map<String, Long> mem){
        long   return_value    = 0;
        int    next_iterations = iterations - 1;
        String memKey          = value + "|" + iterations;

        if      (mem.containsKey(memKey)) return mem.get(memKey);
        else if (iterations == 0)          return_value = 1;
        else if (value == 0)              return_value = recIterate(1, next_iterations, mem);
        else if (Long.toString(value).length() % 2 == 0) {
            List<Long> split = splitInHalf(value);
            return_value = recIterate(split.get(0), next_iterations, mem);
            return_value += recIterate(split.get(1), next_iterations, mem);
        }
        else return_value = recIterate(value * 2024, next_iterations, mem);

        mem.put(memKey, return_value);
        return return_value;
    }




    public List<Long> splitInHalf(long value){
        String asString = Long.toString(value);
        if (asString.length() % 2 == 1) throw new IllegalArgumentException();
        else{
            long a = Long.parseLong(asString.substring(0, asString.length()/2));
            long b = Long.parseLong(asString.substring(asString.length()/2, asString.length()));
            return List.of(a, b);
        }
    }
}