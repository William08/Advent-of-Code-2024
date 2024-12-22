import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;
import java.util.Stack;
import java.util.stream.IntStream;

public class day9{

    static String FILE_NAME = "input";


    public static void main(String[] args) {
        new day9().run("input");
        //new day9().run("input");
    }

    public void run(String FILE_NAME){
        StringBuilder string_builder = new StringBuilder();
        try {
            Scanner scanner = new Scanner(new File(FILE_NAME));
            while (scanner.hasNext()) {
                string_builder.append(scanner.next());
            }
            scanner.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        

        String input_string = string_builder.toString().trim().strip().replace("\n", "");
        long[] input = input_string.chars().mapToLong(x -> Character.getNumericValue(x)).toArray();

        System.out.println(input);

        part22((Arrays.stream(input).mapToInt(i -> (int)i).toArray()));
        if (true) return;

        long[] block_size = new long[input.length/2+1];
        long[] free_space = new long[input.length/2];
        for (int index = 0; index < input.length; index++) {
            if (index%2==0) {block_size[index/2] = input[index];}
            else            free_space[index/2] = input[index];
        }
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
            if (i == input[j]) {
                j++;
                i = 0;
                forwards = !forwards;
                continue;
            }
            if (forwards) {
                result.add(compressedList.get((int)index_forward++));
            }
            else{
                result.add(compressedList.get((int)index_backward--));
            }
            i++;
        }

        long sum = 0;
        //System.out.println(result.get(3));
        for (long a = 0; a < result.size(); a++) {
            //System.out.print(result.get((int)a));
            sum += a * result.get((int)a);
        }
        System.err.println("");
        System.out.println(sum);


        
        


    }

    void part2(int[] input){

        List<Stack<Integer>> cL = new ArrayList<>();

        long[] block_size = new long[input.length/2+1];
        long[] free_space = new long[input.length/2];
        for (int index = 0; index < input.length; index++) {
            if (index%2==0) {block_size[index/2] = input[index];}
            else            free_space[index/2] = input[index];
        }
        List<Integer> result = new ArrayList<>();
        List<Long> compressedList = new ArrayList<>();
        for (long i = 0; i < block_size.length; i++) {
            cL.add(new Stack<>());
            for (long j = 0; j < block_size[(int)i]; j++) {
                compressedList.add(i);
                cL.get((int)i).push((int)i);
            }
        }
        System.out.println(compressedList.size());

        for (int index = cL.size() - 1; index >= 0; index--) {
            for (int spaceIndex = 0; spaceIndex < index; spaceIndex++) {
                if (cL.get(index).size() <= free_space[spaceIndex]){

                    long[] copy = free_space.clone();
                    free_space[spaceIndex] = 0;
                    if(spaceIndex+1 < free_space.length) free_space[spaceIndex+1] = copy[spaceIndex]-cL.get(index).size();
                    if (index < free_space.length) free_space[index] += cL.get(index).size() + copy[index-1];
                    for(int i = index-1; i > spaceIndex + 1; i--){
                        free_space[i] = copy[i-1];
                    }   
                    move(index, spaceIndex+1, cL);
                    index++;
                    break;  
                }
            }

        }
        for (int i = 0; i < free_space.length; i++) {
            Stack<Integer> stack = new Stack<Integer>();
            for (int j = 0; j < free_space[i]; j++) {
                stack.push(0);
            }
            cL.add(i*2+1, stack);
        }

        for (Stack<Integer> stack : cL) {
            while (!stack.isEmpty()) {
                result.add(stack.pop());
                //System.out.println(".");
            }
        }
        long sum = 0;
        //System.out.println(result.get(3));
        for (long a = 0; a < result.size(); a++) {
            if(a < 2000) System.out.print(result.get((int)a) + " ");
            sum += a * result.get((int)a);
        }
        System.err.println("");
        System.out.println(sum);
    }

    void part22(int[] input){
        List<List<Integer>> cL = new ArrayList<>();
        for (int index = 0; index < input.length; index++) {
            List<Integer> list = new ArrayList<>();
            int value = index / 2;
            if (index%2 == 1) value = -1;
            for (long j = 0; j < input[index]; j++) list.add(value);
            cL.add(list);
        }
        //System.out.println(cL.toString());

        for (int index = cL.size() - 1; index >= 0; index--) {
            if (cL.get(index).isEmpty()) continue;
            if (cL.get(index).get(0) == -1) continue;
            for (int spaceIndex = 0; spaceIndex < index - 1; spaceIndex++) {
                if (cL.get(spaceIndex).isEmpty()) continue;
                if (cL.get(spaceIndex).get(0) != -1) continue;
                if (cL.get(index).size() <= cL.get(spaceIndex).size()) {
                    List<Integer> space = cL.get(spaceIndex);
                    int size = space.size();
                    space.removeAll(space);
                    for (int i : cL.get(index)) space.add(i);
                    List<Integer> new_space = new ArrayList<>();
                    while (new_space.size() < size - space.size()) {
                        new_space.add(-1);
                    }
                    cL.get(index).replaceAll(x -> -1);
                    cL.add(spaceIndex+1, new_space);
                    break;
                }
            }
        }
        //System.out.println(cL.toString());

        int[] result = cL.stream().flatMapToInt(x -> x.stream().mapToInt(y -> Math.max(0, y))).toArray();

        long sum = 0;
        //System.out.println(result.get(3));
        for (int a = 0; a < result.length; a++) {
            if(a < 2000) System.out.print(result[a] + " ");
            sum += a * result[a];
        }
        System.out.println("\n");
        System.out.println(sum);
        
    }

    <T> void move(int from, int to, List<T> list){
        list.add(to, list.get(from));
        list.remove(from+1);
    }

}