import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Collections;
import java.util.List;

public class p1 {

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
		System.out.println(allLines.stream().mapToInt(x -> (Integer.parseInt(x) / 3) - 2).sum());

	}

}
