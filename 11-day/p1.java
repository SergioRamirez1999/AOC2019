import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class p1 {

    private static void move(Position position, Integer direction){
		Integer LEFT = 0;
        Integer UP = 1;
        Integer RIGHT = 2;
		Integer DOWN = 3;

		if(direction == UP){
			position.setY(position.getY()-1);
		}else if(direction == DOWN){
			position.setY(position.getY()+1);
		}else if(direction == LEFT){
			position.setX(position.getX()-1);
		}else if(direction == RIGHT){
			position.setX(position.getX()+1);
		}

    }

	public static void main(String[] args) {
		Computer computer = new Computer(false);
		Position position = new Position(0,0); //posicion inicial x=0 y=0
		Integer direction = 1; 	//orientacion inicial UP
		Map<Position, Integer> map = new HashMap<>();
		map.put(position, 0); //posicion inicial (0,0)
		computer.getStdin().add(new Long(map.get(position))); //panel posicion inicial pintado en negro

        while(!computer.isHalted()){
            computer.step();
			if(computer.getStdout().size() == 2){	//size 2 correspondiente a color y sentido
                Integer color_to_paint = computer.getStdout().pollFirst().intValue();
				Integer direction_to_move = computer.getStdout().pollLast().intValue();  //sentido a moverse
				computer.getStdout().clear();  //limpio la salida
				map.put(new Position(position.getX(), position.getY()), color_to_paint); //pinto la posicion
				if(direction_to_move == 0)
					direction = Math.floorMod((direction-1), 4);
				else if(direction_to_move == 1)
					direction = Math.floorMod((direction+1), 4);
				//me muevo una casilla en base a la direccion
				move(position, direction);
				if(!map.containsKey(position))
					map.put(new Position(position.getX(), position.getY()), 0);
				Long input = new Long(map.get(position));
				computer.getStdin().add(input);
            }
		}

		System.out.println(map.size());

	}

}

class Position {
    private Integer x;
    private Integer y;

    public Position(){
        this.x = 0;
        this.y = 0;
	}
	
	public Position(Integer x, Integer y){
        this.x = x;
        this.y = y;
    }

    public Integer getX() {
        return x;
    }

    public void setX(Integer x) {
        this.x = x;
    }

    public Integer getY() {
        return y;
    }

    public void setY(Integer y) {
        this.y = y;
	}

	@Override
	public String toString() {
		return "Position [x=" + x + ", y=" + y + "]";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((x == null) ? 0 : x.hashCode());
		result = prime * result + ((y == null) ? 0 : y.hashCode());
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
		Position other = (Position) obj;
		if (x == null) {
			if (other.x != null)
				return false;
		} else if (!x.equals(other.x))
			return false;
		if (y == null) {
			if (other.y != null)
				return false;
		} else if (!y.equals(other.y))
			return false;
		return true;
	}
	
}

class Computer {

	private static final String PATH = "pzzinput.txt";
	private InfiniteList programs;
	private LinkedList<Long> stdin;
	private LinkedList<Long> stdout;
    private Map<Long, Integer> opcode_sizes;
    private int rb;
	private int pc;
	private boolean halted;
	private boolean debug;

	public Computer() {
        this.programs = new InfiniteList(load_programs());
		this.opcode_sizes = load_opcode_sizes();
		this.stdin = new LinkedList<>();
		this.stdout = new LinkedList<>();
		this.halted = false;
        this.pc = 0;
		this.rb = 0;
		this.debug = false;
	}

	public Computer(boolean debug){
		this();
		this.debug = debug;
	}

	private List<Long> load_programs() {
		List<String> all_programs = new ArrayList<>();

		try {
			all_programs = Files.readAllLines(Paths.get(Computer.PATH), StandardCharsets.UTF_8);
		} catch (Exception e) {
			e.printStackTrace();
		}

		return new ArrayList<>(Arrays.asList(all_programs.get(0).split(","))).stream().map(Long::parseLong)
				.collect(Collectors.toList());
	}

	private Long determine_operand(int index, Long mode){
		Long operand = this.programs.getInfinite(this.pc + index);
		if(mode == 0)
			operand = this.programs.getInfinite(operand.intValue());
		else if(mode == 2){
			operand = this.programs.getInfinite(this.rb + operand.intValue());
		}
		return operand;
	}

	private void log(Long full_opcode){

		Long opcode = Math.floorMod(full_opcode, 100);
		Long mode_par1 = Math.floorMod((full_opcode / 100), 10);
		Long mode_par2 = Math.floorMod((full_opcode / 1000), 10);
		Long mode_par3 = Math.floorMod((full_opcode / 10000), 10);

		Map<String, Long> opcodes_params_log = new LinkedHashMap<>();
		opcodes_params_log.put("Full_Opcode", full_opcode);
		opcodes_params_log.put("Opcode", opcode);
		opcodes_params_log.put("Mod_Par_1", mode_par1);
		opcodes_params_log.put("Mod_Par_2", mode_par2);
		opcodes_params_log.put("Mod_Par_3", mode_par3);
		opcodes_params_log.put("Val_Par_1",  opcode != 3 ? determine_operand(1, mode_par1) : determine_operand(1, 1L));
		opcodes_params_log.put("Val_Par_2", determine_operand(2, mode_par2));
		opcodes_params_log.put("Val_Par_3", determine_operand(3, 1L));
		System.out.println(opcodes_params_log);

		Map<String, Integer> computer_system_log = new LinkedHashMap<>();
		computer_system_log.put("PC", this.pc);
		computer_system_log.put("RB", this.rb);
		System.out.println(computer_system_log);

		Map<String, List<?>> programs_out_in_log = new LinkedHashMap<>();
		programs_out_in_log.put("Stdin", this.stdin);
		programs_out_in_log.put("Stdout", this.stdout);
		programs_out_in_log.put("Programs", this.programs);
		System.out.println(programs_out_in_log);

	}

	public void step(){

		Long full_opcode = this.programs.getInfinite(pc);

		Long opcode = Math.floorMod(full_opcode, 100);

		Long mode_par1 = Math.floorMod((full_opcode / 100), 10);
		Long mode_par2 = Math.floorMod((full_opcode / 1000), 10);
		Long mode_par3 = Math.floorMod((full_opcode / 10000), 10);

		if(debug){
			log(full_opcode);
		}
        
        if (opcode.equals(99L)){
            this.halted = true;
            return;
		}

		boolean dirty_pc = false;

		if (opcode.equals(1L)) {
			Long value_par1 = determine_operand(1, mode_par1);
			Long value_par2 = determine_operand(2, mode_par2);
			Long value_par3 = determine_operand(3, 1L);
			if(mode_par3.equals(2L))
				value_par3 += this.rb;

			this.programs.setInfinite(value_par3.intValue(), value_par1 + value_par2);
		} else if (opcode.equals(2L)) {
			Long value_par1 = determine_operand(1, mode_par1);
			Long value_par2 = determine_operand(2, mode_par2);
			Long value_par3 = determine_operand(3, 1L);
			if(mode_par3.equals(2L))
				value_par3 += this.rb;
			this.programs.setInfinite(value_par3.intValue(), value_par1 * value_par2);
		} else if (opcode.equals(3L)) {
			Long value_par1 = determine_operand(1, 1L);
			if(mode_par1.equals(2L))
				value_par1 += this.rb;
			Long input = this.stdin.pollFirst();
			this.programs.setInfinite(value_par1.intValue(), input);
		} else if (opcode.equals(4L)) {
			Long value_par1 = determine_operand(1, mode_par1);
			this.stdout.add(value_par1);
		} else if (opcode.equals(5L)) {
			Long value_par1 = determine_operand(1, mode_par1);
			Long value_par2 = determine_operand(2, mode_par2);
			if (value_par1 != 0) {
				this.pc = value_par2.intValue();
				dirty_pc = true;
			}
		} else if (opcode.equals(6L)) {
			Long value_par1 = determine_operand(1, mode_par1);
			Long value_par2 = determine_operand(2, mode_par2);
			if (value_par1.equals(0L)) {
				this.pc = value_par2.intValue();
				dirty_pc = true;
			}
		} else if (opcode.equals(7L)) {
			Long value_par1 = determine_operand(1, mode_par1);
			Long value_par2 = determine_operand(2, mode_par2);
			Long value_par3 = determine_operand(3, 1L);
			if(mode_par3.equals(2L))
				value_par3 += this.rb;
			if (value_par1 < value_par2)
				this.programs.setInfinite(value_par3.intValue(), 1L);
			else
				this.programs.setInfinite(value_par3.intValue(), 0L);
		} else if (opcode.equals(8L)) {
			Long value_par1 = determine_operand(1, mode_par1);
			Long value_par2 = determine_operand(2, mode_par2);
			Long value_par3 = determine_operand(3, 1L);
			if(mode_par3.equals(2L))
				value_par3 += this.rb;
			this.programs.setInfinite(value_par3.intValue(), value_par1.equals(value_par2) ? 1L : 0L);
		} else if(opcode.equals(9L)) {
			Long value_par1 = determine_operand(1, mode_par1);
			this.rb += value_par1;
		}
		
		if (!dirty_pc){
			this.pc += this.opcode_sizes.get(opcode);
        }

	}

	public void simulate(){
		while(!this.halted)
			step();
	}

	private Map<Long, Integer> load_opcode_sizes() {
		Map<Long, Integer> opcode_sizes = new HashMap<>();
		opcode_sizes.put(1L, 4);
		opcode_sizes.put(2L, 4);
		opcode_sizes.put(3L, 2);
		opcode_sizes.put(4L, 2);
		opcode_sizes.put(5L, 3);
		opcode_sizes.put(6L, 3);
		opcode_sizes.put(7L, 4);
		opcode_sizes.put(8L, 4);
		opcode_sizes.put(9L, 2);
		opcode_sizes.put(99L, 0);
		return opcode_sizes;
	}

	public InfiniteList getPrograms() {
		return programs;
	}

	public void setPrograms(InfiniteList programs) {
		this.programs = programs;
	}

	public LinkedList<Long> getStdin() {
		return stdin;
	}

	public void setStdin(LinkedList<Long> stdin) {
		this.stdin = stdin;
	}

	public LinkedList<Long> getStdout() {
		return stdout;
	}

	public void setStdout(LinkedList<Long> stdout) {
		this.stdout = stdout;
	}

	public Map<Long, Integer> getOpcode_sizes() {
		return opcode_sizes;
	}

	public void setOpcode_sizes(Map<Long, Integer> opcode_sizes) {
		this.opcode_sizes = opcode_sizes;
	}

	public int getPc() {
		return pc;
	}

	public void setPc(int pc) {
		this.pc = pc;
	}

	public boolean isHalted() {
		return halted;
	}

	public void setHalted(boolean halted) {
		this.halted = halted;
	}

}

class InfiniteList extends ArrayList<Long> {

	private static final long serialVersionUID = 1L;

	public InfiniteList(){}
	
	public InfiniteList(List<Long> c) {
		c.forEach(x -> this.add(x));
    }

    public void addInfinite(int index, Long element){
        if(index >= this.size()){
            for(int i = this.size(); i < index; i++)
                this.add(0L);
        }
        this.add(element);
    }

    public Long getInfinite(int index){
        if(index >= this.size())
            this.addInfinite(index, 0L);
        return this.get(index);
	}

	public Long setInfinite(int index, Long element){
        if(index >= this.size())
		    addInfinite(index, 0L);
		Long previous_value = this.get(index);
		this.remove(index);
		this.add(index, element);
		return previous_value;
	}
}
