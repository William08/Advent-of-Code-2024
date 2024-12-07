package Day6;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Scanner;
import java.util.Set;
import java.util.stream.Stream;
import java.util.HashSet;

public class day6 {


    Point direction = new Point(0, -1);
    Point position;
    Point startPosition;

    boolean outOfscreen = false;

    long time;


    public static void main(String[] args) {
        new day6().run();
    }

    public void run(){
        long start_time = System.currentTimeMillis();
        File file = new File("input");
        List<Point> walls = new ArrayList<>();
        Set<Point> obsticle_spots = new HashSet<>();

        addWalls(walls, file);
        System.out.println(getSize(walls));

        Set<Position> s = new HashSet<>();
        Position s2 = new Position(new Point(1, 2), new Point(3, 4));
        s.add(s2);
        System.out.println(s.contains(new Position(new Point(1, 2), new Point(3, 4))));
        //System.out.println(Objects.equals(s2, ));


        Point size = getSize(walls);
        Set<Position> positionSet = new HashSet<>();
        simulate(positionSet, walls, position, direction);
        Set<Point> path = new HashSet<>();
        positionSet.stream().map(pos -> pos.pos).forEach(pos -> path.add(pos));
        int counter = 0;
        for (int y = 0; y < size.y + 1; y++) {
            System.out.println((y+1) + "/" + (size.y + 1));
            for (int x = 0; x < size.x + 1; x++) {
                Point p = new Point(x, y);
                if(walls.stream().anyMatch(wall -> wall.equals(p))) continue;
                if(!path.contains(p)) continue;
                if(new Point(x, y).equals(position)) continue;
                List<Point> newWalls = new ArrayList<>(walls);
                newWalls.add(p);
    
                //betterPath = new HashMap<>();
                if (!simulate(new HashSet<Position>(), newWalls, position, direction)) {
                    obsticle_spots.add(p);
                    counter++;
                }
            }
        }
/*
        Set<Point> pathSet = new HashSet<>();
        for (Point[] pointArray : pathList) {
            pathSet.add(pointArray[0]);
        }
            */
        System.out.println(path.size());
        System.out.println(obsticle_spots.size());
        System.out.println(counter);
        //System.out.println(betterPath.size());
        long end_time = System.currentTimeMillis();
        System.out.println("Time (millis): " + (end_time - start_time));
        
    }

    public boolean simulate(Set<Position> path, List<Point> walls, Point startPosition, Point startDirection){
        //System.out.println(".");
        Point position = startPosition;
        Point direction = startDirection;
        Position pos = new Position(startPosition, startDirection);
        while (!OutOfScreen(pos.pos, walls)) {
            if (path.contains(pos)) return false;
        
            path.add(pos);

            Position nextPos = stepForward(pos);
            Point newDir = pos.dir;
            //while (walls.contains(nextPos.pos)) {
            //    pos.dir = turn(pos.dir);
            //    nextPos = stepForward(pos);
            //}
            if (walls.contains(nextPos.pos)) {
                pos.dir = turn(pos.dir);
            }
            else pos = nextPos;
        }
        return true;
    }

    public Point getSize(List<Point> walls){
        return new Point(walls.stream().mapToInt(wall -> wall.x).max().getAsInt(), walls.stream().mapToInt(wall -> wall.y).max().getAsInt());
    }

    public Point stepForward(Point current, Point direction){
        return new Point(current.x + direction.x, current.y + direction.y);

    }    
    public Position stepForward(Position pos){
        Point currentPos = pos.pos;
        Point currentDir = pos.dir;
        Point newPos = new Point(currentPos.x + currentDir.x, currentPos.y + currentDir.y);
        return new Position(newPos, currentDir);

    }

    public boolean OutOfScreen(Point position, List<Point> walls){
        return walls.stream().allMatch(wall -> position.x > wall.x)
            || walls.stream().allMatch(wall -> position.y > wall.y)
            || position.x < 0
            || position.y < 0;
    }

    public Point turn(Point direction){
        return new Point(-direction.y, direction.x);
    }

    public void addWalls(List<Point> walls, File file){
        Scanner scanner;
        try {
            scanner = new Scanner(file);
        } catch (FileNotFoundException e) {
            System.out.println("failed to open file");
            return;
        }
        int y = 0;
        int x = 0;
        while (scanner.hasNext()) {
            String line = scanner.nextLine();
            for (char character : line.toCharArray()) {
                switch (character) {
                    case '#':
                        walls.add(new Point(x, y));
                        break;
                    case '^':
                        System.out.println("player found");
                        position = new Point(x, y);
                        startPosition = position;
                        break;
                    default:
                        break;
                }
                x++;
            }
            x = 0;
            y++;
        }
        scanner.close();
    }
}

class Point {
    int x, y;

    public Point(int x, int y){
        this.x = x;
        this.y = y;
    }
    @Override
    public int hashCode(){
        return Objects.hash(x, y);
    }
    @Override
    public boolean equals(Object o){
        if (!(o instanceof Point)) return false;
        if (((Point)o).x != x)     return false;
        if (((Point)o).y != y)     return false;
        else                       return true;
    }

    @Override
    public String toString(){
        return "[" + x + ", " + y + "]";
    }

}

class Position {
    Point pos;
    Point dir;

    public Position(Point pos, Point dir){
        this.pos = pos;
        this.dir = dir;
    }
    @Override
    public int hashCode(){
        return Objects.hash(pos.x, pos.y, dir.x, dir.y);
    }
    @Override
    public boolean equals(Object o){
        if (!(o instanceof Position))       return false;
        if (!((Position)o).pos.equals(pos)) return false;
        if (!((Position)o).dir.equals(dir)) return false;
        else                                return true;
    }

    
}