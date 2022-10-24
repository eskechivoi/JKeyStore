package password;

import java.util.*;

public class PasswordGenerator {
	/**
	 * Generates a new password String with a given length.
	 * @param length the password length
	 * @return passwordStr the password string*/
	public String generatePassword(int length){
		Random elector = new Random();
		StringBuilder passwordStr = new StringBuilder();
		for(int i=0; i<length; i++){
			int election = elector.nextInt(3); //Choose between 0 (numbers), 1 (mayus) or 2 (minus).
			switch(election){
				case 0: passwordStr.append((char)elector.ints(1,48,57).findFirst().getAsInt());
					break;
				case 1: passwordStr.append((char)elector.ints(1,65,90).findFirst().getAsInt());
					break;
				case 2: passwordStr.append((char)elector.ints(1,97,122).findFirst().getAsInt());
					break;
			}
		}
		return passwordStr.toString();
	}
}
