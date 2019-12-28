import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

class Velocity {
    int x, y, z;

    public Velocity(){
        this.x = 0;
        this.y = 0;
        this.z = 0;
    }

    @Override
    public String toString() {
        return "Velocity [x=" + x + ", y=" + y + ", z=" + z + "]";
    }

}


class Position {
    int x, y, z;

    public Position(int x, int y, int z){
        this.x = x;
        this.y = y;
        this.z = z;
    }

    @Override
    public String toString() {
        return "Position [x=" + x + ", y=" + y + ", z=" + z + "]";
    }

}


class Moon {
    String name;
    Position position;
    Velocity velocity;

    List<Position> position_history = new ArrayList<>();
    List<Velocity> velocity_history = new ArrayList<>();

    public Moon(String name, Position position){
        this.name = name;
        this.position = position;
        this.velocity = new Velocity();
        position_history.add(this.position);
        velocity_history.add(this.velocity);
    }

    @Override
    public String toString() {
        return "Moon [name=" + name + ", position=" + position + ", position_history=" + position_history
                + ", velocity=" + velocity + ", velocity_history=" + velocity_history + "]";
    }
    
}

class SpaceScanner {
    int steps;
    List<Moon> moons;

    public SpaceScanner(List<Moon> moons, int steps){
        this.moons = moons;
        this.steps = steps;
    }

    public void calcule_motions(){
        for(int i = 0; i < this.steps; i++){
            for(Moon pivot: moons){
                for(Moon ady: moons){
                    if(pivot != ady){
                        if(pivot.position.x > ady.position.x)
                            pivot.velocity.x--;
                        else if(pivot.position.x < ady.position.x)
                            pivot.velocity.x++;
                        if(pivot.position.y > ady.position.y)
                            pivot.velocity.y--;
                        else if(pivot.position.y < ady.position.y)
                            pivot.velocity.y++;
                        if(pivot.position.z > ady.position.z)
                            pivot.velocity.z--;
                        else if(pivot.position.z < ady.position.z)
                            pivot.velocity.z++;
                        
                        pivot.velocity_history.add(pivot.velocity);
                    }
                }

            }

            for(Moon moon: moons){
                moon.position.x += moon.velocity.x;
                moon.position.y += moon.velocity.y;
                moon.position.z += moon.velocity.z;

                moon.position_history.add(moon.position);
            }
        }
    }

    public int calcule_total_energy(){
        return this.moons.stream().map(
            moon -> (
                (Math.abs(moon.position.x) + Math.abs(moon.position.y) + Math.abs(moon.position.z))
                *(Math.abs(moon.velocity.x) + Math.abs(moon.velocity.y) + Math.abs(moon.velocity.z))))
            .collect(Collectors.toList())
            .stream().mapToInt(x -> x).sum();

    }

}

public class p1 {

    private static final String REGEX_PATTERN = "<x=(-?\\d+), y=(-?\\d+), z=(-?\\d+)>";

    public static List<String> read_all_lines(String path){
        List<String> all_lines = new ArrayList<>();
        try {
            all_lines = Files.readAllLines(Paths.get(path), StandardCharsets.UTF_8);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return all_lines;

    }

    public static Moon init_moon(String line, String moon_name){
        Pattern pattern = Pattern.compile(REGEX_PATTERN);
        Matcher matcher = pattern.matcher(line);

        Moon moon = null;
        if(matcher.matches()){
            moon = new Moon(moon_name, new Position(
                Integer.parseInt(matcher.group(1)), 
                Integer.parseInt(matcher.group(2)), 
                Integer.parseInt(matcher.group(3))));
        }

        return moon;
    }

    public static List<Moon> all_moons_initialized(List<String> init_positions){
        List<Moon> moons = new ArrayList<>();
        moons.add(init_moon(init_positions.get(0), "Io"));
        moons.add(init_moon(init_positions.get(1), "Europa"));
        moons.add(init_moon(init_positions.get(2), "Ganymede"));
        moons.add(init_moon(init_positions.get(3), "Callisto"));
        return moons;
    }


    public static void main(String[] args) {
        String path = "pzzinput.txt";

        List<String> init_positions = read_all_lines(path);

        List<Moon> moons = all_moons_initialized(init_positions);

        int steps = 1000;

        SpaceScanner spaceScanner = new SpaceScanner(moons, steps);

        spaceScanner.calcule_motions();

        int result = spaceScanner.calcule_total_energy();

        System.out.println(result);
    
    }
}