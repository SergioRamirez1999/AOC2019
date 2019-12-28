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

public class p1 {

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

    private static List<Integer> deal_new_stack(List<Integer> deck){
        List<Integer> stack = new ArrayList<>(deck);
        Collections.reverse(stack);
        return stack;
    }

    private static List<Integer> cut_n_cards(List<Integer> deck, int n){
        List<Integer> cutted = new ArrayList<>();
        List<Integer> new_deck = new ArrayList<>();
        if(n < 0){
            cutted = deck.subList(deck.size()-Math.abs(n), deck.size());
            new_deck = deck.subList(0, deck.size()-Math.abs(n));
            new_deck.addAll(0, cutted);
        }else {
            cutted = deck.subList(0, n);
            new_deck = deck.subList(n, deck.size());
            new_deck.addAll(cutted);
        }
        return new_deck;
    }

    private static List<Integer> deal_with_increment_n(List<Integer> deck, int n){
        int[] new_deck = new int[deck.size()];
        new_deck[0] = deck.get(0);  //insertamos la carta del tope del maso en el tope del nuevo maso
        int card_position = 1;      //indice de carta a insertar
        int actual_position = 0;    //indice a la posicion a insertar
        while(card_position != deck.size()){
            actual_position = (actual_position + n) % new_deck.length;
            if(new_deck[actual_position] == 0 && actual_position != 0){
                new_deck[actual_position] = deck.get(card_position);
                card_position++;
            }
        }
        return IntStream.of(new_deck).boxed().collect(Collectors.toList());
    }

    private static List<Integer> execute_technique(List<Integer> deck, SHUFFLING_TECHNIQUE st){
        List<Integer> new_deck = new ArrayList<>();
        switch(st){
            case DEAL_NEW_STACK:
                new_deck = deal_new_stack(deck);
                break;
            case CUT_N:
                new_deck = cut_n_cards(deck, st.getValue());
                break;
            case DEAL_WITH_INCREMENT_N:
                new_deck = deal_with_increment_n(deck, st.getValue());
                break;
        }
        return new_deck;
    }

    private static List<Integer> card_shuffle(List<String> techniques, List<Integer> deck){
        int actual = 1;
        int total = techniques.size();

        for(String tech: techniques){
            SHUFFLING_TECHNIQUE st = parse_line(tech);
            deck = execute_technique(deck, st);
            System.out.println(actual++ + "/" + total);
        }

        return deck;
    }



    public static void main(String[] args) {
        String path = "pzzinput.txt";
        List<String> techniques = read_all_lines(path);
        List<Integer> deck = IntStream.range(0, 10).boxed().collect(Collectors.toList());
        deck = card_shuffle(techniques, deck);
        System.out.println(deck.indexOf(4));
    }

}