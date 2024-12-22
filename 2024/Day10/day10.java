import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class day10 {

    public static void main(String[] args) {
        new day10().run();
    }

    public void run(){
        StringBuilder sb = new StringBuilder();
        try {
            Scanner scanner = new Scanner(new File("input"));
            while (scanner.hasNextLine()) {
                sb.append(scanner.nextLine()).append("\n");
            }
            scanner.close();
        } catch (Exception e) {
            System.out.println("error");
            e.printStackTrace();
        }


        int[][] input = parseInput(sb.toString());
        p1(input);
        p2(input);


    }
    void p1(int[][] input){
        int sum = 0;
        for (int row = 0; row < input.length; row++) {
            for (int col = 0; col < input[0].length; col++) {
                if (input[row][col] == 9) {
                    for (int[] is : filterDuplicates(recursiveMethod(new int[]{row, col}, 9, input))) {
                        sum++;
                    }
                }
            }
        }
        System.out.println("Part 1:");
        System.out.println(sum);
    }

    void p2(int[][] input){
        int sum = 0;
        for (int row = 0; row < input.length; row++) {
            for (int col = 0; col < input[0].length; col++) {
                if (input[row][col] == 9) {
                    for (int[] is : recursiveMethod(new int[]{row, col}, 9, input)) {
                        sum++;
                    }

                }
            }
        }
        System.out.println("Part 2:");
        System.out.println(sum);
    }

    List<int[]> recursiveMethod(int[] pos, int value, int[][] input){
        if (value == 0) return List.of(pos);
        List<int[]> trailheads = 
            IntStream
                .range(0, 4)
                .mapToObj(i -> rotate45(new int[]{1, 0}, i))
                .map(dir -> new int[]{dir[0] + pos[0], dir[1] + pos[1]})
                .filter(x -> isInside(x, input.length, input[0].length))
                .filter(new_position -> valueAt(new_position, input) == value-1)
                .flatMap(new_position -> recursiveMethod(new_position, value-1, input).stream())
                .collect(Collectors.toList());
        return trailheads;
    }

    int valueAt(int[] coordinates, int[][] input){
        return input[coordinates[0]][coordinates[1]];
    }

    List<int[]> filterDuplicates(List<int[]> list) {
        List<int[]> copy = new ArrayList<>();        
        for (int[] js : list) {
            if (getArrayFromList(js, copy) == null) copy.add(js);
        }
        return copy;
    }


    int[] getArrayFromList(int[] array, Collection<int[]> list){
        for (int[] is : list) {
            if (positions_equals(array, is)) return is;
        }
        return null;
    }
    
    boolean positions_equals(int[] a, int[] b){
        return a[0] == b[0] && a[1] == b[1];
    }

    boolean isInside(int[] pos, int rows, int cols){
        return pos[0] >= 0 && pos[0] < rows && pos[1] >= 0 && pos[1] < cols;
    }

    int[] rotate45(int[] position, int times) {
        int[] copy1 = position.clone();
        for (int i = 0; i < times; i++) {
            int[] copy2 = copy1.clone();
            copy1[0] = copy2[1];
            copy1[1] = -copy2[0];
        }
        return copy1;
    }

    int[][] parseInput(String input) {
        return Arrays.stream(input.split("\n"))
                     .map(x -> x.chars()
                                .map(i -> Character.getNumericValue(i))
                                .toArray())
                     .toArray(int[][]::new);
    }
}