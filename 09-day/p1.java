import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class p1 {

	public static void main(String[] args) {
		Computer computer = new Computer();
		computer.getStdin().push(2L);
		computer.simulate();
		System.out.println(computer.getStdout());
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

	public Computer() {
        this.programs = new InfiniteList(load_programs());
		this.opcode_sizes = load_opcode_sizes();
		this.stdin = new LinkedList<>();
		this.stdout = new LinkedList<>();
		this.halted = false;
        this.pc = 0;
        this.rb = 0;
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

	private Long determine_opcode(int index, Long mode){
		Long operand = this.programs.getInfinite(this.pc + index);
		if(mode == 0)
			operand = this.programs.getInfinite(operand.intValue());
		else if(mode == 2){
			operand = this.programs.getInfinite(this.rb + operand.intValue());
		}
		return operand;
	}

	public void step(){

		Long full_opcode = this.programs.getInfinite(pc);
		Long opcode = full_opcode % 100;

		Long mode_par1 = (full_opcode / 100) % 10;
		Long mode_par2 = (full_opcode / 1000) % 10;
		Long mode_par3 = (full_opcode / 10000) % 10;
        
        if (opcode == 99){
            this.halted = true;
            return;
		}

		boolean dirty_pc = false;

		if (opcode == 1) {
			Long value_par1 = determine_opcode(1, mode_par1);
			Long value_par2 = determine_opcode(2, mode_par2);
			Long value_par3 = determine_opcode(3, 1L);
			if(mode_par3 == 2)
				value_par3 += this.rb;
			this.programs.setInfinite(value_par3.intValue(), value_par1 + value_par2);
		} else if (opcode == 2) {
			Long value_par1 = determine_opcode(1, mode_par1);
			Long value_par2 = determine_opcode(2, mode_par2);
			Long value_par3 = determine_opcode(3, 1L);
			if(mode_par3 == 2)
				value_par3 += this.rb;
			this.programs.setInfinite(value_par3.intValue(), value_par1 * value_par2);
		} else if (opcode == 3) {
			Long value_par1 = determine_opcode(1, 1L);
			if(mode_par1 == 2)
				value_par1 += this.rb;
			System.out.println(value_par1);
			this.programs.setInfinite(value_par1.intValue(), this.stdin.pop());
		} else if (opcode == 4) {
			Long value_par1 = determine_opcode(1, mode_par1);
			this.stdout.add(value_par1);
		} else if (opcode == 5) {
			Long value_par1 = determine_opcode(1, mode_par1);
			Long value_par2 = determine_opcode(2, mode_par2);
			if (value_par1 != 0) {
				this.pc = value_par2.intValue();
				dirty_pc = true;
			}
		} else if (opcode == 6) {
			Long value_par1 = determine_opcode(1, mode_par1);
			Long value_par2 = determine_opcode(2, mode_par2);
			if (value_par1 == 0) {
				this.pc = value_par2.intValue();
				dirty_pc = true;
			}
		} else if (opcode == 7) {
			Long value_par1 = determine_opcode(1, mode_par1);
			Long value_par2 = determine_opcode(2, mode_par2);
			Long value_par3 = determine_opcode(3, 1L);
			if(mode_par3 == 2L)
				value_par3 += this.rb;
			if (value_par1 < value_par2)
				this.programs.setInfinite(value_par3.intValue(), 1L);
			else
				this.programs.setInfinite(value_par3.intValue(), 0L);
		} else if (opcode == 8) {
			Long value_par1 = determine_opcode(1, mode_par1);
			Long value_par2 = determine_opcode(2, mode_par2);
			Long value_par3 = determine_opcode(3, 1L);
			if(mode_par3 == 2L)
				value_par3 += this.rb;
			if (value_par1 == value_par2)
				this.programs.setInfinite(value_par3.intValue(), 1L);
			else
				this.programs.setInfinite(value_par3.intValue(), 0L);
		} else if(opcode == 9) {
			Long value_par1 = determine_opcode(1, mode_par1);
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