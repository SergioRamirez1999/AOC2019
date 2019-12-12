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

class Computer {

	private List<Integer> programs;
	private int pc;
	private LinkedList<Integer> stdin;
	private Map<Integer, Integer> opcode_sizes;

	public Computer(String path) {
		this.programs = load_programs(path);
		this.pc = 0;
		this.stdin = new LinkedList<>();
		this.stdin.push(5);
		this.opcode_sizes = load_opcode_sizes();
	}

	private List<Integer> load_programs(String path) {
		List<String> all_programs = new ArrayList<>();

		try {
			all_programs = Files.readAllLines(Paths.get(path), StandardCharsets.UTF_8);
		} catch (Exception e) {
			e.printStackTrace();
		}

		return new ArrayList<>(Arrays.asList(all_programs.get(0).split(","))).stream().map(Integer::parseInt)
				.collect(Collectors.toList());
	}

	private Map<Integer, Integer> load_opcode_sizes() {
		Map<Integer, Integer> opcode_sizes = new HashMap<Integer, Integer>();
		opcode_sizes.put(1, 4);
		opcode_sizes.put(2, 4);
		opcode_sizes.put(3, 2);
		opcode_sizes.put(4, 2);
		opcode_sizes.put(5, 3);
		opcode_sizes.put(6, 3);
		opcode_sizes.put(7, 4);
		opcode_sizes.put(8, 4);
		opcode_sizes.put(99, 0);
		return opcode_sizes;
	}

	public void simulate() {
		Integer full_opcode = this.programs.get(pc);
		// Last value
		Integer opcode = full_opcode % 100;

		// Load parameter mode
		Integer mode_par1 = (full_opcode / 100) % 10;
		Integer mode_par2 = (full_opcode / 1000) % 10;

		if (opcode == 99)
			return;

		// Load parameters
		Integer par1 = this.programs.get(this.pc + 1);
		Integer par2 = this.programs.get(this.pc + 2);
		Integer par3 = this.programs.get(this.pc + 3);

		Integer value_par1 = par1;
		Integer value_par2 = par2;
		Integer result = 0;

		boolean dirty_pc = false;

		if (opcode == 1) {
			if (mode_par1 == 0)
				value_par1 = this.programs.get(par1);
			if (mode_par2 == 0)
				value_par2 = this.programs.get(par2);
			result = value_par1 + value_par2;
			this.programs.set(par3, result);
		} else if (opcode == 2) {
			if (mode_par1 == 0)
				value_par1 = this.programs.get(par1);
			if (mode_par2 == 0)
				value_par2 = this.programs.get(par2);
			result = value_par1 * value_par2;
			this.programs.set(par3, result);
		} else if (opcode == 3) {
			this.programs.set(par3, this.stdin.pop());
		} else if (opcode == 4) {
			Integer output = par1;
			if (mode_par1 == 0)
				output = this.programs.get(par1);
			System.out.println(output);
		} else if (opcode == 5) {
			if (mode_par1 == 0)
				value_par1 = this.programs.get(par1);
			if (mode_par2 == 0)
				value_par2 = this.programs.get(par2);
			if (value_par1 != 0) {
				this.pc = value_par2;
				dirty_pc = true;
			}
		} else if (opcode == 6) {
			if (mode_par1 == 0)
				value_par1 = this.programs.get(par1);
			if (mode_par2 == 0)
				value_par2 = this.programs.get(par2);
			if (value_par1 == 0) {
				this.pc = value_par2;
				dirty_pc = true;
			}
		} else if (opcode == 7) {
			if (mode_par1 == 0)
				value_par1 = this.programs.get(par1);
			if (mode_par2 == 0)
				value_par2 = this.programs.get(par2);
			if (value_par1 < value_par2)
				this.programs.set(par3, 1);
			else
				this.programs.set(par3, 0);
		} else if (opcode == 8) {
			if (mode_par1 == 0)
				value_par1 = this.programs.get(par1);
			if (mode_par2 == 0)
				value_par2 = this.programs.get(par2);
			if (value_par1 == value_par2)
				this.programs.set(par3, 1);
			else
				this.programs.set(par3, 0);
		}

		if (!dirty_pc)
			this.pc += this.opcode_sizes.get(opcode);

		simulate();

	}

}

public class p2 {
	
	public static void main(String[] args) {
		
		
		String path = "pzzinput.txt";
		Computer computer = new Computer(path);
		computer.simulate();
		
	}

}
