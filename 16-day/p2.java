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

public class p2 {

    private static List<String> read_all_lines(String path){
        List<String> all_lines = new ArrayList<>();
        try {
            all_lines = Files.readAllLines(Paths.get(path), StandardCharsets.UTF_8);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return all_lines;
    }

    private static String calcule_element(String signal){
        List<Integer> signal_parsed = signal.chars().mapToObj(x -> Character.getNumericValue(x)).collect(Collectors.toList());
        List<Integer> new_signal = new ArrayList<>();
        
        Collections.reverse(signal_parsed);

        for(int i = 0; i < signal_parsed.size(); i++){
            if(i == 0){
                new_signal.add(signal_parsed.get(0));
            }else{
                new_signal.add((signal_parsed.get(i) + new_signal.get(i-1)) % 10);
            }
        }

        Collections.reverse(new_signal);
        return new_signal.stream().map(String::valueOf).collect(Collectors.joining(""));
    }

    
    private static String decode_signal(String signal, int steps){

        for(int i = 0; i < steps; i++){
            System.out.println(i + "/" + steps);
            signal = calcule_element(signal);
        }

        return signal.substring(0, 8);
    }
    
    public static void main(String[] args) {
        String path = "pzzinput.txt";
        String input = read_all_lines(path).get(0);
        String signal = "";
        for(int i = 0; i < 10000; i++)
            signal = signal.concat(input);
        int message_offset = Integer.parseInt(signal.substring(0, 7));
        String signal_substring = signal.substring(message_offset, signal.length());
        System.out.println(decode_signal(signal_substring, 100));

    }
}