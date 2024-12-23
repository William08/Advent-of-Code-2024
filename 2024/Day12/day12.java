import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.Scanner;
import java.util.Set;
import java.io.File;

public class day12 {

    public static void main(String[] args) {
        new day12().run();
    }

    public void run(){
        String      input   = getInputString("input");
        char[][]    map     = Arrays.stream(input.split("\n")).map(x -> x.toCharArray()).toArray(char[][]::new);
        
        List<Set<String>> all_regions = collectAllRegions(map);

        int cost = 0;

        for (Set<String> region : all_regions) {
            cost += area(region) * perimiter(region);
        }

        System.out.println("Part 1:");
        System.out.println(cost);


        //PART 2
        cost = 0;

        for (Set<String> region : all_regions) {
            cost += area(region) * sides(region);

        }

        System.out.println("Part 2:");
        System.out.println(cost);

    }

    int area(Set<String> region){
        return region.size();
    }

    int perimiter(Set<String> region){
        int perimiter = 0;
        for (String key : region) {
            for (String neighbour : getNeighbours(key)) {
                if (!region.contains(neighbour)) perimiter++;
            }
        }
        return perimiter;
    }

    <T> int sides(Set<String> region){
        int sides = 0;
        for (int[] direction : Arrays.stream(getNeighbours("0,0")).map(x -> toArray(x)).toList()) {
            List<String> all_sides = new ArrayList<>();
            for (String key : region) {
                String other_key = (toArray(key)[0] + direction[0]) + "," + (toArray(key)[1] + direction[1]);
                if (!region.contains(other_key)) all_sides.add(key);
            }


            all_sides.sort(new Comparator<String>() {

                @Override
                public int compare(String o1, String o2) {
                    int[] pos1 = toArray(o1);
                    int[] pos2 = toArray(o2);
                    if      (pos1[1] < pos2[1]) return -1;
                    else if (pos1[1] > pos2[1]) return 1;
                    else if (pos1[0] < pos2[0]) return -1;
                    else if (pos1[0] > pos2[0]) return 1;
                    else                        return 0;
                }
                
            });

            Set<String> true_sides = new HashSet<>();

            for (String key : all_sides) {
                if (Arrays.stream(getNeighbours(key)).allMatch(neighbour -> !true_sides.contains(neighbour))) sides++;
                true_sides.add(key);
            }
        }
        return sides;
    }


    private List<Set<String>> collectAllRegions(char[][] map) {
        List<Set<String>> to_return = new ArrayList<>();
        Set<String> visited = new HashSet<>();
        for (int row = 0; row < map.length; row++) {
            for (int col = 0; col < map[0].length; col++) {
                int[]  position = new int[]{row, col};
                String key      = toKey(position);

                if (visited.contains(key)) continue;

                Set<String> region = getRegion(position, map);
                visited.addAll(region);
                to_return.add(region);

            }
        }
        return to_return;
    }

    public Set<String> getRegion(int[] start_position, char[][] map){
        Set<String>   visited     = new HashSet<>();
        Queue<String> to_visit    = new LinkedList<>();
        char          region_type = map[start_position[0]][start_position[1]];

        to_visit.add(toKey(start_position));

        while (!to_visit.isEmpty()) {
            String   key        = to_visit.poll();
            String[] neighbours = getNeighbours(key);

            visited.add(key);

            for (String neighbour : neighbours) {
                int[] position = toArray(neighbour);

                if (!isInside(position, map))                     continue;
                if (visited.contains(neighbour))                  continue;
                if (to_visit.contains(neighbour))                 continue;
                if (map[position[0]][position[1]] != region_type) continue;
                
                to_visit.add(neighbour);
            }
            
        }

        return visited;
    }

    public boolean isInside(int[] position, char[][] map){
        int[] size = new int[]{map.length, map[0].length};
        return position[0] >= 0 && position[0] < size[0] && position[1] >= 0 && position[1] < size[1];
    }

    public String toKey(int[] array){
        return array[0] + "," + array[1];
    }

    public int[] toArray(String string){
        return Arrays.stream(string.split(","))
            .mapToInt(s -> Integer.parseInt(s)).toArray();
    }

    public String[] getNeighbours(String key){
        String[] to_return = new String[4];
        int[] position = toArray(key);
        to_return[0] = toKey(new int[]{position[0] + 1, position[1]});
        to_return[1] = toKey(new int[]{position[0] - 1, position[1]});
        to_return[2] = toKey(new int[]{position[0], position[1] + 1});
        to_return[3] = toKey(new int[]{position[0], position[1] - 1});
        return to_return;
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