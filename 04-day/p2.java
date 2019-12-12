public class p2 {

	private static boolean is_valid_password(String password) {
		char[] aPassword = password.toCharArray();
		boolean never_decrease = true;
		boolean double_value = false;
		char previous_value = '#';

		for (int i = 0; i < aPassword.length - 1 && never_decrease; i++) {

			if (aPassword[i] <= aPassword[i + 1]) {

				if (aPassword[i] == aPassword[i + 1]) {
					if (i + 2 <= aPassword.length - 1) {
						if (aPassword[i + 1] != aPassword[i + 2] && previous_value != aPassword[i + 1]) {
							double_value = true;
						}
					} else if (previous_value != aPassword[i]) {
						double_value = true;
					}
				}

			} else
				never_decrease = false;

			previous_value = aPassword[i];

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
