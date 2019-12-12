package com.sergio.adventofcode;

import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import org.javatuples.Pair;

public class Dia10 {

	private static List<List<Character>> read_all_lines(String path) {
		List<String> all_lines = new ArrayList<>();
		try {
			all_lines = Files.readAllLines(Paths.get(path), StandardCharsets.UTF_8);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return all_lines.stream()
				.map(x -> x.chars().mapToObj(c -> (char) c).collect(Collectors.toList()))
				.collect(Collectors.toList());
	}

	private static Map<Pair<Integer, Integer>, List<Pair<Integer, Integer>>> grid_coordinate_asteroid(
			List<List<Character>> asteroids_coords) {
		Map<Pair<Integer, Integer>, List<Pair<Integer, Integer>>> grid = new HashMap<>();

		for (int y = 0; y < asteroids_coords.size(); y++) {
			List<Character> line = asteroids_coords.get(y);
			for (int x = 0; x < line.size(); x++) {
				if (line.get(x) == '#')
					grid.put(new Pair<>(x, y), new ArrayList<>());
			}
		}
		return grid;
	}

	private static List<Pair<Integer, Integer>> direct_sight_vision(
			Map<Pair<Integer, Integer>, List<Pair<Integer, Integer>>> grid, Pair<Integer, Integer> ast_pivot) {
		List<Pair<Integer, Integer>> sight_vision = new ArrayList<>();
		Set<Double> theta_angles = new LinkedHashSet<>();
		double x2 = ast_pivot.getValue0();
		double y2 = ast_pivot.getValue1();
		for (Pair<Integer, Integer> ast : grid.keySet()) {
			if (!ast.equals(ast_pivot)) {
				double x1 = ast.getValue0();
				double y1 = ast.getValue1();
				double theta = Math.atan2((y1 - y2), (x1 - x2));
				if (!theta_angles.contains(theta)) {
					sight_vision.add(ast);
					theta_angles.add(theta);
				}
			}
		}
		return sight_vision;
	}

	private static Map<Pair<Integer, Integer>, List<Pair<Integer, Integer>>> grid_direct_sight_vision(
			Map<Pair<Integer, Integer>, List<Pair<Integer, Integer>>> grid) {
		Map<Pair<Integer, Integer>, List<Pair<Integer, Integer>>> grid_sight_vision = new HashMap<>();

		for (Pair<Integer, Integer> pivot : grid.keySet())
			grid_sight_vision.put(pivot, direct_sight_vision(grid, pivot));

		return grid_sight_vision;
	}

	public static void main(String[] args) {
		String path = "pzzinput.txt";

		List<List<Character>> all_lines = read_all_lines(path);

		Map<Pair<Integer, Integer>, List<Pair<Integer, Integer>>> grid = grid_coordinate_asteroid(all_lines);

		Map<Pair<Integer, Integer>, List<Pair<Integer, Integer>>> grid_sight_vision = grid_direct_sight_vision(grid);

		List<Pair<Pair<Integer, Integer>, Long>> asteroid_sight_vision = grid_sight_vision.entrySet().stream()
				.map(k_v -> new Pair<Pair<Integer, Integer>, Long>(k_v.getKey(), k_v.getValue().stream().count()))
				.collect(Collectors.toList());

		Long max_direct_sight_vision = asteroid_sight_vision.stream()
				.map(p -> p.getValue1())
				.collect(Collectors.toList()).stream().mapToLong(x -> x).max().getAsLong();

		System.out.println(max_direct_sight_vision);

	}

}
