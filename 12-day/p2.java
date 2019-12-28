import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;


class SpaceScanner {

    private static final String path = "pzzinput.txt";
    private static final String REGEX_PATTERN = "<x=(-?\\d+), y=(-?\\d+), z=(-?\\d+)>";
    List<List<Integer>> positions = new ArrayList<>();
    List<List<Integer>> velocity = new ArrayList<>();
    int timesteps;

    public List<String> read_all_lines(){
        List<String> all_lines = new ArrayList<>();
        try {
            all_lines = Files.readAllLines(Paths.get(SpaceScanner.path), StandardCharsets.UTF_8);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return all_lines;

    }

    public List<List<Integer>> init_moons_position(){
        List<String> all_lines = read_all_lines();
        List<List<Integer>> moons_position = new ArrayList<>();

        for(String line: all_lines){
            Pattern pattern = Pattern.compile(SpaceScanner.REGEX_PATTERN);
            Matcher matcher = pattern.matcher(line);
    
            if(matcher.matches()){
                moons_position.add(
                    new ArrayList<>(
                            Arrays.asList(
                                Integer.parseInt(matcher.group(1)), 
                                Integer.parseInt(matcher.group(2)), 
                                Integer.parseInt(matcher.group(3))
                            )
                        )
                    );
            }
        }

        return moons_position;
    }

    private List<List<Integer>> init_moons_velocity(){
        List<List<Integer>> moons_velocity = new ArrayList<>();
        moons_velocity.add(new ArrayList<>(Arrays.asList(0,0,0)));
        moons_velocity.add(new ArrayList<>(Arrays.asList(0,0,0)));
        moons_velocity.add(new ArrayList<>(Arrays.asList(0,0,0)));
        moons_velocity.add(new ArrayList<>(Arrays.asList(0,0,0)));
        return moons_velocity;
    }

    public void reset(){
        this.positions = init_moons_position();
        this.velocity = init_moons_velocity();
        this.timesteps = 0;
    }

    private void variations_velocity(int c){
        for (int i = 0; i < 4; i++){
            for (int h = 0; h < 4; h++){
                int m1 = this.positions.get(i).get(c);
                int m2 = this.positions.get(h).get(c);
                if(m1 > m2)
                    this.velocity.get(i).set(c, this.velocity.get(i).get(c)-1);
                else if (m2 > m1)
                    this.velocity.get(i).set(c, this.velocity.get(i).get(c)+1);
            }
        }

        for(int i = 0; i < 4; i++)
            this.positions.get(i).set(c, this.positions.get(i).get(c) + this.velocity.get(i).get(c));
    }

    public void calcule_motions(){
        for(int c = 0; c < 3; c++)
            variations_velocity(c);
        this.timesteps++;
    }

    public Integer calcule_total_energy(){
        List<Integer> energy = new ArrayList<>();
        for(int i = 0; i < 4; i++){
            int sum_position = Math.abs(this.positions.get(i).get(0)) + Math.abs(this.positions.get(i).get(1)) + Math.abs(this.positions.get(i).get(2));
            int sum_velocity = Math.abs(this.velocity.get(i).get(0)) + Math.abs(this.velocity.get(i).get(1)) + Math.abs(this.velocity.get(i).get(2));
            energy.add(sum_position * sum_velocity);
        }

        return energy.stream().mapToInt(x -> x).sum();
    }

    public Long find(int c, List<Integer> initial){
        reset();
        Long i = 0L;

        while(true){
            variations_velocity(c);
            i++;
            List<Integer> position_c = this.positions.stream().map(x -> x.get(c)).collect(Collectors.toList());
            List<Integer> velocity_c = this.velocity.stream().map(x -> x.get(c)).collect(Collectors.toList());
            if(position_c.equals(initial) && velocity_c.equals(Arrays.asList(0,0,0,0)))
                return i;
        }

    }

}

public class p2 {

    public static Long gcd(Long a, Long b) {
        if (b == 0) return a;
        else return (gcd (b, a % b));
    }

    public static Long lcm(Long a, Long b){
        return (a*b) / gcd(a, b);
    }

    

    public static void main(String[] args) {

        SpaceScanner spaceScanner = new SpaceScanner();
        spaceScanner.reset();
        List<Integer> x0 = new ArrayList<>(spaceScanner.positions.stream().map(x -> x.get(0)).collect(Collectors.toList()));
        List<Integer> y0 = new ArrayList<>(spaceScanner.positions.stream().map(x -> x.get(1)).collect(Collectors.toList()));
        List<Integer> z0 = new ArrayList<>(spaceScanner.positions.stream().map(x -> x.get(2)).collect(Collectors.toList()));

        Long x = spaceScanner.find(0, x0);
        Long y = spaceScanner.find(1, y0);
        Long z = spaceScanner.find(2, z0);
        
        Long steps = lcm(lcm(x,y),z);
        System.out.println(steps);

    
    }
}

