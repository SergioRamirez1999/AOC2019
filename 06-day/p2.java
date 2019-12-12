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
	
	private static List<String> road_to_san(Map<String, String> map_orbit) {
		
		List<String> road = new ArrayList<>();
		
		String laterKey = map_orbit.get("SAN");
		road.add(0, laterKey);
		
		while(!laterKey.equals("COM")) {
			laterKey = map_orbit.get(laterKey);
			road.add(0, laterKey);
		}
		
		return road;

	}
	
	private static List<String> road_to_you(Map<String, String> map_orbit) {
		
		List<String> road = new ArrayList<>();
		
		String laterKey = map_orbit.get("YOU");
		road.add(0, laterKey);
		
		while(!laterKey.equals("COM")) {
			laterKey = map_orbit.get(laterKey);
			road.add(0, laterKey);
		}
		
		return road;
		
	}
	
	private static Integer distance_between_you_and_san(Map<String, String> map_orbit) {
		List<String> road_to_you = road_to_you(map_orbit);
		List<String> road_to_san = road_to_san(map_orbit);
		
		List<String> l1 = new ArrayList<>(road_to_you);
		l1.removeAll(road_to_san);
		
		List<String> l2 = new ArrayList<>(road_to_san);
		l2.removeAll(road_to_you);
		l1.addAll(l2);
		
		return l1.size();
	}

	private static List<Integer> calculate_orbits(Map<String, String> map_orbit) {
		return map_orbit.keySet().stream().map(x -> determine_orbits(map_orbit, x)).collect(Collectors.toList());
	}

	public static void main(String[] args) {
		String path = "pzzinput.txt";
		
		List<String> all_lines = read_all_lines(path);

		Map<String, String> map_orbit = determine_mapping_orbit(all_lines);
		
		List<Integer> orbit_calculated = calculate_orbits(map_orbit);
		
		Integer distance = distance_between_you_and_san(map_orbit);
		
		System.out.println("Distance between Santa and You: " + distance);
		
	}

}
