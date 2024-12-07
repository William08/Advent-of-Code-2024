import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.stream.Collectors;

public class day5 {

    File input = new File("input.txt");
    //List<int[]> ruleList = new ArrayList<>();
    List<int[]> pageList = new ArrayList<>();
    Map<Integer, ArrayList<Integer>> rules = new HashMap<>();
    int sum = 0;

    public static void main(String[] args) {
        System.out.println(Arrays.asList(15).toString());
        new day5().run();
    }

    void run(){
        gatherData(input);
        System.out.println(rules.get(29));

        for (int i = 0; i < pageList.size(); i++) {
            List<Integer> pages = Arrays.stream(pageList.get(i)).boxed().collect(Collectors.toList());
            List<Integer> pagesCopy = new ArrayList<>(pages);

            pages.sort(new Comparator<Integer>() {
                @Override
                public int compare(Integer o1, Integer o2) {
                    if (rules.containsKey(o1) && rules.get(o1).contains(o2)) return -1;
                    if (rules.containsKey(o2) && rules.get(o2).contains(o1)) return 1;
                    else return 0;
                }
            });

            if (pages.stream().anyMatch(x -> x != pagesCopy.get(pages.indexOf(x)))){
                int middleValue = pages.get((pages.size() / 2));
                sum += middleValue;

            }

        }

        
        System.out.println(sum);
    }

    void gatherData(File input){
        try {
            boolean paseRules = true;
            Scanner scanner = new Scanner(input);
            while (scanner.hasNext()) {
                String line = scanner.nextLine();
                if (line.isEmpty()) paseRules = false;
                else if (paseRules) {
                    int[] i = ParseRule(line);
                    if (!rules.containsKey(i[0])) {
                        rules.put(i[0], new ArrayList<Integer>(Arrays.asList(i[1])));
                    }
                    else{
                        rules.get(i[0]).add(i[1]);
                    }
                }
                else pageList.add(ParsePages(line));
            }
            scanner.close();
        }
        catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    int[] ParseRule(String s){
        return Arrays.stream(s.split("\\|")).mapToInt(Integer::parseInt).toArray(); 
    }

    int[] ParsePages(String s){
        return Arrays.stream(s.split(",")).mapToInt(Integer::parseInt).toArray();
    }
}