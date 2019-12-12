public class p1 {

	private static boolean is_valid_password(String password) {
		char[] aPassword = password.toCharArray();
		boolean never_decrease = true;
		boolean double_value = false;

		for (int i = 0; i < aPassword.length - 1 && never_decrease; i++) {

			if (aPassword[i] <= aPassword[i + 1]) {

				if (aPassword[i] == aPassword[i + 1]) {
					if (i + 2 <= aPassword.length - 1)
                        double_value = true;
					else
						double_value = true;
					
				}

			} else
				never_decrease = false;

		}

		return never_decrease && double_value;
	}

	public static void main(String[] args) {

		Integer min_value = 145852;
		Integer max_value = 616942;

		int counter = 0;
		for (int i = min_value; i < max_value; i++) {
			if (is_valid_password(String.valueOf(i)))
				counter++;
		}

		System.out.println(counter);
	}

}
