package com.sergio.adventofcode;

import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class p1 {

	private static List<String> read_all_lines(String path) {
		List<String> all_lines = new ArrayList<>();
		try {
			all_lines = Files.readAllLines(Paths.get(path), StandardCharsets.UTF_8);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return all_lines;
	}

	private static Map<String, String> determine_mapping_orbit(List<String> orbits) {
		return orbits.stream()
				.collect(Collectors.toMap(orbit -> orbit.split("\\)")[1], orbit -> orbit.split("\\)")[0]));
	}

	private static int determine_orbits(Map<String, String> map_orbit, String orbit) {
		if (!map_orbit.containsKey(orbit))
			return 0;
		else
			return 1 + determine_orbits(map_orbit, map_orbit.get(orbit));
	}
	

	private static List<Integer> calculate_orbits(Map<String, String> map_orbit) {
		return map_orbit.keySet().stream().map(x -> determine_orbits(map_orbit, x)).collect(Collectors.toList());
	}

	public static void main(String[] args) {
		String path = "pzzinput.txt";
		
		List<String> all_lines = read_all_lines(path);

		Map<String, String> map_orbit = determine_mapping_orbit(all_lines);
		
		List<Integer> orbit_calculated = calculate_orbits(map_orbit);
		
		Integer checksum = orbit_calculated.stream().mapToInt(x -> x).sum();
		
		System.out.println("Checksum: " + checksum);
		
	}

}
