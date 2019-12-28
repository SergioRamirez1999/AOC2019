import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

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

    private static List<List<Tile>> init_grid(List<String> input){
        List<List<Tile>> grid = new ArrayList<>();
        int index_row = 0;
        for(String line: input){
            grid.add(new ArrayList<>());
            for(char c: line.toCharArray()){
                grid.get(index_row).add(new Tile(c == '.'?false:true));
            }
            index_row++;
        }

        return grid;
    }

    private static List<List<Tile>> calcule_adjacents(List<List<Tile>> grid){

        List<List<Tile>> grid_clone = new ArrayList<>();

        int index = 0;
        for(List<Tile> tiles: grid){
            grid_clone.add(new ArrayList<>());
            for(Tile tile: tiles){
                grid_clone.get(index).add(new Tile(tile.isInfected()?true:false));
            }
            index++;
        }

        for(int r = 0; r < grid.size(); r++){
            for(int c = 0; c < grid.get(r).size(); c++){
                int adyacents_bugs = 0;
                
                //derecha
                if(c<grid.get(r).size()-1 && grid.get(r).get(c+1).isInfected()){
                    adyacents_bugs++;
                }
                
                //izquierda
                if(c>0 && grid.get(r).get(c-1).isInfected()){
                    adyacents_bugs++;
                }
                
                //abajo
                if(r<grid.size()-1 && grid.get(r+1).get(c).isInfected()){
                    adyacents_bugs++;
                }
                
                //arriba
                if(r>0 && grid.get(r-1).get(c).isInfected()){
                    adyacents_bugs++;
                }
                

                if(grid.get(r).get(c).isInfected()){
                    if(adyacents_bugs != 1){
                        grid_clone.get(r).get(c).setInfected(false);
                    }
                }else {
                    if(adyacents_bugs == 1 || adyacents_bugs == 2){
                        grid_clone.get(r).get(c).setInfected(true);
                    }
                }
            }

        }
        return grid_clone;
    }

    private static int calcule_biodiversity_rating(List<List<Tile>> grid){
        int rating = 0;
        for(int i = 0; i < grid.size(); i++){
            for(int h = 0; h < grid.get(i).size(); h++){
                rating += grid.get(i).get(h).isInfected()?Math.pow(2, i*5+h):0;
            }
        }
        return rating;
    }

    private static List<List<Tile>> simulate_reaction(List<List<Tile>> grid){
        List<List<Tile>> first_twice_layout = new ArrayList<>();
        Set<List<List<Tile>>> repetitions_layout = new HashSet<>();
        while(true){
            grid = calcule_adjacents(grid);
            if(!repetitions_layout.contains(grid)){
                repetitions_layout.add(grid);
            }else {
                first_twice_layout = grid;
                break;
            }
        }

        return first_twice_layout;
    }
    public static void main(String[] args) {
        String path = "pzzinput.txt";
        List<String> input = read_all_lines(path);

        List<List<Tile>> grid = init_grid(input);

        List<List<Tile>> first_twice_layout = simulate_reaction(grid);

        int result = calcule_biodiversity_rating(first_twice_layout);

        System.out.println(result);
    }
}



class Tile {
    
    private boolean isInfected;
    private List<Tile> tilesAdjacents;

    public Tile() {
        this(false, new ArrayList<>());
    }
    
    public Tile(boolean isInfected) {
        this(isInfected, new ArrayList<>());
    }
    

    public Tile(boolean isInfected, List<Tile> tilesAdjacents) {
        this.isInfected = isInfected;
        this.tilesAdjacents = tilesAdjacents;
    }

    public boolean isInfected() {
        return isInfected;
    }

    public void setInfected(boolean isInfected) {
        this.isInfected = isInfected;
    }

    public List<Tile> getTilesAdjacents() {
        return tilesAdjacents;
    }

    public void setTilesAdjacents(List<Tile> tilesAdjacents) {
        this.tilesAdjacents = tilesAdjacents;
    }

    @Override
    public String toString() {
        return "Tile [isInfected=" + isInfected + ", tilesAdjacents=" + tilesAdjacents + "]";
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + (isInfected ? 1231 : 1237);
        result = prime * result + ((tilesAdjacents == null) ? 0 : tilesAdjacents.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        Tile other = (Tile) obj;
        if (isInfected != other.isInfected)
            return false;
        if (tilesAdjacents == null) {
            if (other.tilesAdjacents != null)
                return false;
        } else if (!tilesAdjacents.equals(other.tilesAdjacents))
            return false;
        return true;
    }

}