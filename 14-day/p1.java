import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

class Chemical {
    String name;
    int quantity;

    public Chemical(String name, int quantity) {
        this.name = name;
        this.quantity = quantity;
    }

    @Override
    public String toString() {
        return "Chemical [name=" + name + ", quantity=" + quantity + "]";
    }

    
}


class Reaction {
    List<Chemical> chemicals_reactions;
    Chemical chemical_reacted;

    public Reaction(List<Chemical> chemicals_reactions, Chemical chemical_reacted) {
        this.chemicals_reactions = chemicals_reactions;
        this.chemical_reacted = chemical_reacted;
    }

    @Override
    public String toString() {
        return "Reaction [chemical_reacted=" + chemical_reacted + ", chemicals_reactions=" + chemicals_reactions + "]";
    }

   

}

class Factory {
    static final String path = "pzzinput.txt";
    static final String REGEX_PATTERN = "((\\d+) (\\w+))";
    List<Reaction> reactions;
    Map<String, Integer> inventory;
    Map<String, Reaction> chemical_reaction;

    public Factory() {
        this.chemical_reaction = new HashMap<>();
        this.reactions = init_reactions();
        this.inventory = init_inventory();
    }


    private List<String> read_all_lines(){
        List<String> all_lines = new ArrayList<>();

        try {
            all_lines = Files.readAllLines(Paths.get(Factory.path), StandardCharsets.UTF_8);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return all_lines;
    }

    private void create_inventory(String line, Map<String, Integer> inventory){

        String[] p1 = line.split("=>");
        String[] p2 = p1[0].split(",");

        for(int i = 0; i < p2.length; i++){
            inventory.put(p2[i].trim().split(" ")[1], 0);
        }

        inventory.put(p1[1].trim().split(" ")[1], 0);

        
    }

    private Map<String, Integer> init_inventory(){
        List<String> all_lines = read_all_lines();
        Map<String, Integer> inventory = new HashMap<>();

        for(String line: all_lines){
            create_inventory(line, inventory);
        }

        return inventory;
    }

    private Reaction create_reaction(String line){

        List<Chemical> chemicals_reactions = new ArrayList<>();
        String[] p1 = line.split("=>");
        String[] p2 = p1[0].split(",");

        for(int i = 0; i < p2.length; i++){
            chemicals_reactions.add(new Chemical(p2[i].trim().split(" ")[1], Integer.parseInt(p2[i].trim().split(" ")[0])));
        }
        Reaction reaction = new Reaction(chemicals_reactions, new Chemical(p1[1].trim().split(" ")[1], Integer.parseInt(p1[1].trim().split(" ")[0])));
        this.chemical_reaction.put(reaction.chemical_reacted.name, reaction);

        return reaction;
        
    }

    private List<Reaction> init_reactions(){
        List<String> all_lines = read_all_lines();
        List<Reaction> reactions = new ArrayList<>();

        for(String line: all_lines){
            reactions.add(create_reaction(line));
        }

        return reactions;
    }


    public void produce_chemical(String chemical_name, int quantity){
        if(this.inventory.get(chemical_name)<quantity){
            Reaction reaction = this.chemical_reaction.get(chemical_name);
            for(Chemical chemical : reaction.chemicals_reactions){
                if(chemical.name.equals("ORE")){
                    this.inventory.put(chemical.name, this.inventory.get(chemical.name) + chemical.quantity);
                }else{

                    while(this.inventory.get(chemical.name)<chemical.quantity){
                        produce_chemical(chemical.name, chemical.quantity);
                        Reaction r_chemical = this.chemical_reaction.get(chemical.name);
                        this.inventory.put(chemical.name, this.inventory.get(chemical.name) + r_chemical.chemical_reacted.quantity);
                    }

                    this.inventory.put(chemical.name, this.inventory.get(chemical.name)-chemical.quantity);
                }
            }

        }else {
            this.inventory.put(chemical_name, this.inventory.get(chemical_name)-quantity);
        }
    }

}


public class p1 {


    

    public static void main(String[] args) {


        Factory factory = new Factory();

        // factory.reactions.forEach(System.out::println);
        // System.out.println(factory.inventory);
        // factory.chemical_reaction.entrySet().forEach(System.out::println);

        factory.produce_chemical("FUEL", 1);

        
        System.out.println(factory.inventory);
        // System.out.println(factory.inventory);
        // factory.chemical_reaction.entrySet().forEach(System.out::println);
    }
}
