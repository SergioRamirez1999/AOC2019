import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class p1 {

    private static List<String> read_all_lines(String path){
        List<String> all_lines = new ArrayList<>();
        try {
            all_lines = Files.readAllLines(Paths.get(path), StandardCharsets.UTF_8);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return all_lines;
    }

    private static List<Integer> repeat_pattern(List<Integer> pattern, int step){
        List<Integer> new_pattern = new ArrayList<>();
        for(Integer value: pattern){
            for(int i = 0; i < step+1; i++)
                new_pattern.add(value);
        }
        new_pattern.add(0); //se aniade al final el cero que se quita al principio
        return new_pattern.subList(1, new_pattern.size());
    }

    private static int calcule_element(List<Integer> signal, List<Integer> pattern){
        int result = 0;
        for(int i = 0; i < signal.size(); i++){
            result += signal.get(i) * pattern.get(i%pattern.size());
        }
        return Math.abs(result%10);
    }


    private static List<Integer> realize_phase(List<Integer> signal, List<Integer> pattern){
        List<Integer> new_signal = new ArrayList<>();
        for(int i = 0; i < signal.size(); i++){
            List<Integer> new_pattern = repeat_pattern(pattern, i);
            int result = calcule_element(signal, new_pattern);
            new_signal.add(result);
        }
        return new_signal;
    }
    
    public static void main(String[] args) {
        String path = "pzzinput.txt";
        List<String> input = read_all_lines(path);
        List<Integer> signal_base = input.get(0).chars().mapToObj(x -> Character.getNumericValue(x)).collect(Collectors.toList());
        List<Integer> pattern_base = Arrays.asList(0,1,0,-1);
        
        for(int i = 0; i < 100; i++){
            signal_base = realize_phase(signal_base, pattern_base);
        }

        signal_base.subList(0, 8).forEach(System.out::print);

    }
}