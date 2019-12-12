package com.sergio.adventofcode;

import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
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

	private static List<List<String>> determine_layers(String pixels) {
		List<List<String>> layers = new ArrayList<>();
		int wide = 25;
		int tall = 6;
		int c_layers = pixels.length() / (wide * tall);
		for (int i = 0; i < c_layers; i++) {
			List<String> layer = new ArrayList<>();
			int beginIndex = i * wide * tall;
			int endIndex = (i + 1) * wide * tall;
			String pixels_layer = pixels.substring(beginIndex, endIndex);
			for (int h = 0; h < tall; h++) {
				int bI = h * wide;
				int eI = (h + 1) * wide;
				String row = pixels_layer.substring(bI, eI);
				layer.add(row);
			}
			layers.add(layer);
		}
		return layers;
	}

	private static List<String> determine_layer_fewest_zero(List<List<String>> layers) {
		int c_zero_min = Integer.MAX_VALUE;
		int c_zero = 0;
		int index_layer_min = 0;

		for (int i = 0; i < layers.size(); i++) {
			c_zero = 0;
			for (String row : layers.get(i))
				c_zero += row.chars().filter(x -> x == '0').count();
			if (c_zero < c_zero_min) {
				c_zero_min = c_zero;
				index_layer_min = i;
			}

		}

		return layers.get(index_layer_min);

	}

	private static int determine_color(List<List<String>> layers, int layer_index, int row_index, int pixel_index) {

		if (layers.get(layer_index).get(row_index).charAt(pixel_index) == '0') {
			return 0;
		} else if (layers.get(layer_index).get(row_index).charAt(pixel_index) == '1') {
			return 1;
		}
		return determine_color(layers, layer_index + 1, row_index, pixel_index);

	}

	public static void main(String[] args) {
		String path = "pzzinput.txt";

		List<String> all_lines = read_all_lines(path);

		List<List<String>> layers = determine_layers(all_lines.get(0));
		
		List<String> layer_fewest_zero = determine_layer_fewest_zero(layers);

		List<Long> c_one_per_row = layer_fewest_zero.stream().map(x -> x.chars().filter(c -> c == '1').count())
				.collect(Collectors.toList());
		List<Long> c_two_per_row = layer_fewest_zero.stream().map(x -> x.chars().filter(c -> c == '2').count())
				.collect(Collectors.toList());

		Long c_one = c_one_per_row.stream().mapToLong(x -> x).sum();
		Long c_two = c_two_per_row.stream().mapToLong(x -> x).sum();

		Long checksum = c_one * c_two;

		System.out.println(checksum);
		
	
		
	}

}
