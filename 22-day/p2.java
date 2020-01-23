import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class p2 {

    enum SHUFFLING_TECHNIQUE {
        DEAL_WITH_INCREMENT_N(0), DEAL_NEW_STACK(0), CUT_N(0);
        private int value;
        SHUFFLING_TECHNIQUE(int value){
            this.value = value;
        }

        public int getValue(){
            return this.value;
        }
        
        public void setValue(int value){
            this.value = value;
        }
    }

    private static final String REGEX_DEAL_WITH_INCREMENT = "deal with increment (\\d+)";
    private static final String REGEX_DEAL_NEW_STACK = "deal into new stack";
    private static final String REGEX_CUT = "cut (-?\\d+)";


    private static List<String> read_all_lines(String path){
        List<String> all_lines = new ArrayList<>();
        try {
            all_lines = Files.readAllLines(Paths.get(path), StandardCharsets.UTF_8);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return all_lines;
    }

    private static SHUFFLING_TECHNIQUE parse_line(String line){
        Pattern rgx_deal_with_increment = Pattern.compile(REGEX_DEAL_WITH_INCREMENT);
        Pattern rgx_deal_new_stack = Pattern.compile(REGEX_DEAL_NEW_STACK);
        Pattern rgx_cut = Pattern.compile(REGEX_CUT);
        Matcher match_deal_with_increment = rgx_deal_with_increment.matcher(line);
        Matcher match_deal_new_stack = rgx_deal_new_stack.matcher(line);
        Matcher match_cut = rgx_cut.matcher(line);

        SHUFFLING_TECHNIQUE st = null;
        if(match_deal_with_increment.matches()){
            st = SHUFFLING_TECHNIQUE.DEAL_WITH_INCREMENT_N;
            st.setValue(Integer.parseInt(match_deal_with_increment.group(1)));
        }
        else if(match_cut.matches()){
            st = SHUFFLING_TECHNIQUE.CUT_N;
            st.setValue(Integer.parseInt(match_cut.group(1)));
        }
        else if(match_deal_new_stack.matches())
            st = SHUFFLING_TECHNIQUE.DEAL_NEW_STACK;

        return st;

    }

    private static Long[] parse_shuffle(List<String> lines, Long deck_size){
        Long[] data = {0L,1L};

        for(String line: lines){
            SHUFFLING_TECHNIQUE technique = parse_line(line);
            switch(technique){
                case DEAL_NEW_STACK:
                    data[1] *= -1L;
                    data[0] = data[0] * -1 + deck_size - 1;
                    break;

                case CUT_N:
                    data[0] -= technique.getValue();
                    break;
                    
                case DEAL_WITH_INCREMENT_N:
                    data[1] = Math.floorMod(data[1] * technique.getValue(), deck_size);
                    data[0] = Math.floorMod(data[0] * technique.getValue(), deck_size);
                    break; 
            }
        }
        return data;
    }

    private static Long[] repeat_mul(Long[] data, BigInteger times, BigInteger deck_size){
        Long[] s_m = new Long[2];
        s_m[0] = BigInteger.valueOf(data[0])
                    .multiply(BigInteger.valueOf(data[1]).modPow(times, deck_size)
                    .add(BigInteger.ONE.negate())
                    .multiply(BigInteger.valueOf(data[1]-1).modPow(deck_size.add(BigInteger.valueOf(-2L)), deck_size))
                ).mod(deck_size).longValue();
        s_m[1] = BigInteger.valueOf(data[1]).modPow(times, deck_size).longValue();
        return s_m;
    }

    public static void main(String[] args) {
        String path = "pzzinput.txt";
        List<String> lines = read_all_lines(path);

        BigInteger deck_size = BigInteger.valueOf(119315717514047L);
        BigInteger times = BigInteger.valueOf(101741582076661L);
        BigInteger position = BigInteger.valueOf(2020);

        Long[] data = parse_shuffle(lines, deck_size.longValue());
        data = repeat_mul(data, times, deck_size);

        Long result = position.add(BigInteger.valueOf(data[0]).negate())
                                .multiply(BigInteger.valueOf(data[1]).modPow(deck_size.add(BigInteger.valueOf(-2L)), deck_size))
                                .mod(deck_size).longValue();
        System.out.println(result);


    }

}
