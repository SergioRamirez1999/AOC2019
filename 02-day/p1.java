import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

class Computer {
	private List<Integer> programs;
	private int pc;

	public Computer(String path){
		this.programs = readAllLines(path);
		this.programs.set(1, 12);
		this.programs.set(2, 2);
		this.pc = 0;
	}

	private List<Integer> readAllLines(String path) {
		List<String> allLines = Collections.emptyList();

		try {
			allLines = Files.readAllLines(Paths.get(path), StandardCharsets.UTF_8);
		} catch (Exception e) {
			e.printStackTrace();
		}

		return Arrays.stream(allLines.get(0).split(",")).map(Integer::parseInt).collect(Collectors.toList());
	}

	public void simulate(){

		Integer opcode = this.programs.get(this.pc);
		Integer param1 = this.programs.get(this.pc+1);
		Integer param2 = this.programs.get(this.pc+2);
		Integer param3 = this.programs.get(this.pc+3);

		if(opcode == 99){
			System.out.println(this.programs.get(0));
			return;
		}

		if(opcode == 1){
			this.programs.set(param3, this.programs.get(param1) + this.programs.get(param2));
		}else if(opcode == 2){
			this.programs.set(param3, this.programs.get(param1) * this.programs.get(param2));
		}

		this.pc += 4;

		simulate();
			

	}

}

public class p1 {
	
	public static void main(String[] args) {
		String path = "/home/crypto/Documents/programming/advent-of-code-2019/02-day/pzzinput.txt";
		Computer computer = new Computer(path);
		computer.simulate();
	}

}
