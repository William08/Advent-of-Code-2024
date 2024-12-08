import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.io.File;

public class day8{
    public static void main(String[] args) {
        new day8().run();
    }

    public void run(){
        String inputString = readFile("input");
        int[] size = new int[2];
        Map<Character, List<int[]>> antennaMap = GetAntennaMap(inputString, size);
        System.out.println(getAntinodes(antennaMap, size, new int[]{1}).size());
        System.out.println(getAntinodes(antennaMap, size, IntStream.range(0, size[0]).toArray()).size());
    }

    public Set<int[]> getAntinodes(Map<Character, List<int[]>> antennaMap, int[] size, int[] pattern){
        Set<int[]> antinodes = new HashSet<>();

        for (Map.Entry<Character, List<int[]>> frequency : antennaMap.entrySet()) {
            for (int index = 0; index < frequency.getValue().size(); index++) {
                int[] position = frequency.getValue().get(index);
                List<int[]> other_antennas = allExceptIndex(frequency.getValue(), index);
                for (int[] other_position : other_antennas) {
                    //int[] new_antinode = AntinodePosition(position, other_position);
                    for (int i : pattern) {
                        int[] new_antinode = new int[]{((position[0] - other_position[0]) * i) + position[0], ((position[1] - other_position[1]) * i) + position[1]};
                        if(!ContainsPosition(antinodes, new_antinode)) antinodes.add(new_antinode);
                    }
                    //if (!ContainsPosition(antinodes, new_antinode)) antinodes.add(new_antinode);
                }
                
            }
        }

        return antinodes.stream().filter(node -> InBounds(node, size)).collect(Collectors.toSet());
    }
    public boolean InBounds(int[] position, int[] size){
        return position[0] >= 0 && position[0] < size[0] && position[1] >= 0 && position[1] < size[1];
    }

    public List<int[]> allExceptIndex(List<int[]> list, int index){
        return IntStream.range(0, list.size())
            .filter(i -> i != index)
            .mapToObj(i -> list.get(i))
            .collect(Collectors.toList());
    }

    public boolean ContainsPosition(Set<int[]> col, int[] position){
        boolean already_exists = false;
        for (int[] other : col) {
            if (position[0] == other[0] && position[1] == other[1]) {
                already_exists = true; 
            }
        }
        return already_exists;
    }

    public Map<Character, List<int[]>> GetAntennaMap(String inputString, int[] size){
        Map<Character, List<int[]>> antennaMap = new HashMap<>();
        int x = 0, y = 0;
        for (char symbol : inputString.toCharArray()) {
            if (symbol == '.') {x++; continue;}
            if (symbol == '\n') {y++; size[0] = x; x=0; continue;}
            if (antennaMap.containsKey(symbol)) antennaMap.get(symbol).add(new int[]{x, y});
            else antennaMap.put(symbol, new ArrayList<>(List.of(new int[]{x, y})));
            x++;
        }
        size[1] = y;
        return antennaMap;
    }

    public int[] AntinodePosition(int[] node, int[] other){
        return new int[]{Reflection(node[0], other[0]), Reflection(node[1], other[1])};
    }

    public int Reflection(int mirror, int object){
        return (2*mirror) - object;
    }

    public String readFile(String file_name){
        StringBuilder string_builder = new StringBuilder();
        try {
            Scanner scanner = new Scanner(new File(file_name));
            while (scanner.hasNext()) {
                string_builder.append(scanner.nextLine()).append("\n");
            }
            scanner.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return string_builder.toString();
    }
}