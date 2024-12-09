import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;
import java.util.stream.IntStream;

public class day9{

    static String FILE_NAME = "input";


    public static void main(String[] args) {
        new day9().run();
    }

    public void run(){
        StringBuilder string_builder = new StringBuilder();
        try {
            Scanner scanner = new Scanner(new File(FILE_NAME));
            while (scanner.hasNext()) {
                string_builder.append(scanner.next());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        String input_string = string_builder.toString().trim().strip().replace("\n", "");
        long[] input = input_string.chars().mapToLong(x -> Character.getNumericValue(x)).toArray();

        long[] block_size = new long[input.length/2+1];
        long[] free_space = new long[input.length/2];
        for (int index = 0; index < input.length; index++) {
            if (index%2==0) {block_size[index/2] = input[index];}
            else            free_space[index/2] = input[index];
        }

        /*       long take_index = block_size.length - 1;
        for (int index = 0; index < block_size.length; index++) {
            for (int i = 0; i < block_size[index]; i++) result.add(index);
            if (take_index <= index) break;
            for (int i = 0; i < free_space[index]; i++) {
                result.add(take_index);
            }
            take_index--;
        }
            */
        List<Long> result = new ArrayList<>();
        List<Long> compressedList = new ArrayList<>();
        for (long i = 0; i < block_size.length; i++) {
            for (long j = 0; j < block_size[(int)i]; j++) {compressedList.add(i);};
        }
        System.out.println(compressedList.size());
        long index_forward = 0;
        long index_backward = compressedList.size() - 1;
        int i = 0;
        int j = 0;
        boolean forwards = true;
        while (index_backward >= index_forward) {
            if (forwards) {
                result.add(compressedList.get((int)index_forward++));
            }
            else{
                result.add(compressedList.get((int)index_backward--));
            }
            if (i == input[j] - 1) {
                j++;
                i = 0;
                if (forwards) forwards = false;
                else          forwards = true;
            }
            else i++;
        }

        long sum = 0;
        System.out.println(result.get(3));
        for (long a = 0; a < result.size(); a++) {
            //System.out.print(result.get(a));
            sum += a * result.get((int)a);
        }
        System.err.println("");
        System.out.println(sum);


        
        


    }
}