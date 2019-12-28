package com.sergio;

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
import org.paukov.combinatorics3.Generator;

public class p2 {


	public static Integer acs(List<Integer> phase_sequence){
        List<Computer> computers = new ArrayList<>();
        for(Integer phase: phase_sequence){
            Computer computer = new Computer();
            computer.getStdin().add(phase);
            computers.add(computer);
        }

        int actual_computer = 0;
        int next_computer = 1;

        computers.get(0).getStdin().add(0);

        Integer last_output = 0;

        while(true){
            while(computers.get(actual_computer).getStdout().isEmpty()){
                computers.get(actual_computer).step();
                if(computers.get(actual_computer).isHalted())
                    return last_output;
            }
            last_output = computers.get(actual_computer).getStdout().pop();
            computers.get(next_computer).getStdin().add(last_output);
            actual_computer = next_computer;
            next_computer = (next_computer+1) % 5;
        }
        
	}

	public static List<Integer> simulate_amplifier(List<List<Integer>> phase_combinations) {

		List<Integer> all_signals_output = new ArrayList<>();
		
		for(List<Integer> phase_sequence: phase_combinations){
			all_signals_output.add(acs(phase_sequence));
		}

		return all_signals_output;

	}
	
	public static void main(String[] args) {
		List<List<Integer>> phase_combinations = Generator
				.permutation(5,6,7,8,9)
				.simple()
				.stream()
				.map(x->x)
				.collect(Collectors.toList());
		List<Integer> signals_output = simulate_amplifier(phase_combinations);
		Integer max_signal = signals_output.stream().mapToInt(x -> x).max().getAsInt();
		System.out.println(max_signal);
	}

}

class Computer {

	private static final String PATH = "pzzinput.txt";
	private List<Integer> programs;
	private LinkedList<Integer> stdin;
	private LinkedList<Integer> stdout;
	private Map<Integer, Integer> opcode_sizes;
	private int pc;
	private boolean halted;

	public Computer() {
        this.programs = load_programs();
		this.opcode_sizes = load_opcode_sizes();
		this.stdin = new LinkedList<>();
		this.stdout = new LinkedList<>();
		this.halted = false;
		this.pc = 0;
	}

	private List<Integer> load_programs() {
		List<String> all_programs = new ArrayList<>();

		try {
			all_programs = Files.readAllLines(Paths.get(Computer.PATH), StandardCharsets.UTF_8);
		} catch (Exception e) {
			e.printStackTrace();
		}

		return new ArrayList<>(Arrays.asList(all_programs.get(0).split(","))).stream().map(Integer::parseInt)
				.collect(Collectors.toList());
	}

	public void step(){

		Integer full_opcode = this.programs.get(pc);
		Integer opcode = full_opcode % 100;

		Integer mode_par1 = (full_opcode / 100) % 10;
        Integer mode_par2 = (full_opcode / 1000) % 10;
        
        if (opcode == 99){
            this.halted = true;
            return;
		}

        Integer par1 = this.programs.get(this.pc + 1);
        Integer par2 = this.opcode_sizes.get(opcode)!=2 ? this.programs.get(this.pc + 2) : 0;
        Integer par3 = this.opcode_sizes.get(opcode)!=2 ? this.programs.get(this.pc + 3) : 0;

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
			this.programs.set(par1, this.stdin.pop());
		} else if (opcode == 4) {
			Integer output = par1;
			if (mode_par1 == 0)
				output = this.programs.get(par1);
			this.stdout.push(output);
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

		if (!dirty_pc){
			this.pc += this.opcode_sizes.get(opcode);
        }

	}

	public void simulate(){
		while(!this.halted)
			step();
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

	public List<Integer> getPrograms() {
		return programs;
	}

	public void setPrograms(List<Integer> programs) {
		this.programs = programs;
	}

	public LinkedList<Integer> getStdin() {
		return stdin;
	}

	public void setStdin(LinkedList<Integer> stdin) {
		this.stdin = stdin;
	}

	public LinkedList<Integer> getStdout() {
		return stdout;
	}

	public void setStdout(LinkedList<Integer> stdout) {
		this.stdout = stdout;
	}

	public Map<Integer, Integer> getOpcode_sizes() {
		return opcode_sizes;
	}

	public void setOpcode_sizes(Map<Integer, Integer> opcode_sizes) {
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

