import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import org.javatuples.Pair;
import org.javatuples.Triplet;

public class p2 {

	private static String REGEX_PATTERN = "([RULD])(\\d+)";

	private static List<String> read_all_lines(String path) {
		List<String> lines = null;

		try {
			lines = Files.readAllLines(Paths.get(path), StandardCharsets.UTF_8);
		} catch (Exception e) {
			e.printStackTrace();
		}

		return lines;
	}

	private static Triplet<String, String, Integer> parse_wire_instruction(String wire_movement) {
		Pattern pattern = Pattern.compile(REGEX_PATTERN);
		Matcher matcher = pattern.matcher(wire_movement);
		Triplet<String, String, Integer> parse = new Triplet<>("","",0);
		if (matcher.matches()) {
			parse = parse.setAt0(matcher.group(0));
			parse = parse.setAt1(matcher.group(1));
			parse = parse.setAt2(Integer.parseInt(matcher.group(2)));
		}
		return parse;
	}

	private static List<Pair<Integer, Integer>> determine_coords_movements(int[] x_y, String orientation, int movements) {
		List<Pair<Integer, Integer>> coords = new ArrayList<>();
		switch(orientation) {
		case "U":
			for(int i=0; i < movements; i++)
				coords.add(new Pair<>(x_y[0],++x_y[1]));
			break;
		case "D":
			for(int i=0; i < movements; i++)
				coords.add(new Pair<>(x_y[0],--x_y[1]));
			break;
		case "L":
			for(int i=0; i < movements; i++)
				coords.add(new Pair<>(--x_y[0],x_y[1]));
			break;
		case "R":
			for(int i=0; i < movements; i++)
				coords.add(new Pair<>(++x_y[0],x_y[1]));
			break;
		}
		return coords;
	}
	
	private static List<List<Pair<Integer, Integer>>> determine_wires_movements(List<List<String>> wires) {
		List<List<Pair<Integer, Integer>>> wires_movements = new ArrayList<>();
		for(List<String> listTemp: wires) {
			int[] x_y = new int[2];
			List<Pair<Integer,Integer>> wire_coords = new ArrayList<>(); 
			for(String temp: listTemp) {
				Triplet<String, String, Integer> instruction = parse_wire_instruction(temp);
				wire_coords.addAll(determine_coords_movements(x_y,instruction.getValue1(),instruction.getValue2()));
			}
			wires_movements.add(wire_coords);
		}
		return wires_movements;
	}
	
	private static void place_wired_in_grid(List<List<Pair<Integer, Integer>>> wires_coords, Map<Pair<Integer, Integer>, Integer> grid) {
		for(List<Pair<Integer, Integer>> wire_coords: wires_coords) {
			Set<Pair<Integer, Integer>> own_intersections = new LinkedHashSet<>();
			for(Pair<Integer, Integer> coord: wire_coords) {
				if(!own_intersections.contains(coord)) {
					if(grid.containsKey(coord)) {
						grid.replace(coord, 2);
					}
					else
						grid.put(coord, 1);
					own_intersections.add(coord);
				}
			}
		}
		
	}
	
	private static List<Pair<Integer, Integer>> determine_intersections(Map<Pair<Integer, Integer>, Integer> grid) {
		return grid.keySet().stream()
						.filter(k -> grid.get(k)>1).collect(Collectors.toList());
	}
	
	private static int determine_min_distance(List<Pair<Integer, Integer>> intersections) {
		return intersections.stream()
					.map(x -> Math.abs(x.getValue0())+Math.abs(x.getValue1()))
					.collect(Collectors.toList()).stream().mapToInt(x -> x).min().getAsInt();
	}
	
	private static int determine_min_steps(List<List<Pair<Integer, Integer>>> wires_coords, List<Pair<Integer, Integer>> intersections) {
		List<Integer> distances = new ArrayList<>();
		
		for(Pair<Integer, Integer> intersection: intersections)
			distances.add(wires_coords.get(0).indexOf(intersection)+1 + wires_coords.get(1).indexOf(intersection)+1);
		
		return distances.stream().mapToInt(x->x).min().getAsInt();
	}
	
	public static void main(String[] args) {
        String path = "/home/crypto/Documents/programming/advent-of-code-2019/03-day/pzzinput.txt";
		List<String> all_lines = read_all_lines(path);
		List<List<String>> wires_instructions = all_lines.stream().map(x -> Arrays.stream(x.split(",")).collect(Collectors.toList()))
				.collect(Collectors.toList());
		
		List<List<Pair<Integer, Integer>>> wires_coords = determine_wires_movements(wires_instructions);
		
		
		Map<Pair<Integer, Integer>, Integer> grid = new HashMap<>();
		
		place_wired_in_grid(wires_coords, grid);
		
		List<Pair<Integer, Integer>> intersections = determine_intersections(grid);
		
		System.out.println(determine_min_steps(wires_coords, intersections));
		
	}

}
