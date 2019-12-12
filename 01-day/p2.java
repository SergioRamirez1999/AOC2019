import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Collections;
import java.util.List;

public class p2 {

	private static List<String> readAllLines(String path) {
		List<String> allLines = Collections.emptyList();
		try {
			allLines = Files.readAllLines(Paths.get(path), StandardCharsets.UTF_8);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return allLines;
	}

	public static void main(String[] args) {
        String path = "/home/crypto/Documents/programming/advent-of-code-2019/01-day/pzzinput.txt";
		List<String> allLines = readAllLines(path);

		int result = 0;
		for (String temp : allLines) {
			int value = Integer.parseInt(temp);
			while ((value / 3) - 2 > 0) {
				value = (value / 3) - 2;
				result += value;
			}
		}
		System.out.println(result);
	}

}
