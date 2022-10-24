package cli;

import java.io.IOException;
import java.util.*;
import password.*;

public class StoreInterface{
	public static void printPasswords(PasswordStore passStore){
		try{
			passStore.readPasswordListFromFile();
			passStore.printPasswords();
		} catch (Exception e) {
			System.out.println("An error ocurred");
			e.printStackTrace();
		}
	}

	public static void generatePassword(PasswordStore passStore, String name, String length){
		try{
			int len = Integer.parseInt(length);
			PasswordGenerator passGen = new PasswordGenerator();
			String password = passGen.generatePassword(len);
			String args[] = {"gp", name, password};
			writePassword(passStore, args); 
		} catch (NumberFormatException e) {
			System.err.println("Can't parse Integer: Input not an Integer.");
		}
	}

	public static void writePassword(PasswordStore passStore, String arguments[]){
		if(arguments.length!=3){System.out.println("Bad action syntax. Please type 'help'.");
		return;}
		try{
			passStore.readPasswordListFromFile();
			passStore.setNewPassword(arguments[1], arguments[2]);
			passStore.writePasswordList();
		} catch (Exception e) {
			System.out.println("An error ocurred");
			e.printStackTrace();
		}
	}


	public static void deletePassword(PasswordStore passStore, String arguments[]){
		if(arguments.length!=2){System.out.println("Bad action syntax. Please type 'help'.");
		return;}
		try{
			passStore.readPasswordListFromFile();
			passStore.deletePassword(arguments[1]);;
			passStore.writePasswordList();
		} catch (Exception e) {
			System.out.println("An error ocurred");
			e.printStackTrace();
		}
	}

	private static PasswordStore generatePasswordStore(String args[]){
		if(args.length<2){System.out.println("Bad arguments. Expected <store_name> <store_password>. Select '3' for more info.");
			return null;}
		try{
			PasswordStore passStore = new PasswordStore(args[0],args[0],args[1]);
			return passStore;
		} catch (IOException e) {
			System.err.println("An error ocurred while creating store files");
			return null;
		}
	} 

	private static PasswordStore readPasswordStore(String args[]){
		if(args.length<2){System.out.println("Bad arguments. Expected <datapath> <secretpath>. Select '3' for more info.");
			return null;}
		try{
			PasswordStore passStore = new PasswordStore(args[0], args[1]);
			return passStore;
		} catch (IOException e) {
			System.err.println("An error ocurred while accesing to store files");
			return null;
		}
	} 

	private static String[] readInput(int choice){
		String args[] = new String[2];
		if(choice == 1) {
			System.out.print("Type store name: ");
			args[0] = System.console().readLine();
			System.out.print("Type master password: ");
			args[1] = new String(System.console().readPassword());
		} if(choice == 2) {
			System.out.print("Type store path: ");
			args[0] = System.console().readLine();
			System.out.print("Type secret path: ");
			args[1] = new String(System.console().readLine());
		}
		return args;
	}

	public static void main (String args[]){
		Scanner in = new Scanner(System.in);
		PasswordStore passStore = null;
		System.out.println("1.- Create a new password store.\n2.- Use an existing password store.\n3.- Help.");
		int choice = in.nextInt();
		switch(choice){
			case 1: String passArgs[] = readInput(1);
					passStore = generatePasswordStore(passArgs);
					if(passStore == null) {in.close();
						return;}
					break;
			case 2: passArgs = readInput(2);
					passStore = readPasswordStore(passArgs);
					if(passStore == null) {in.close();
						return;}
					break;
			case 3: System.out.println("1.- Program arguments:\n -<store_name>: The name of the password store.\n -<store_password>: Master password, used to generate the 'something you own' secret.");
					System.out.println("2.- Program arguments:\n -<datapath>: The path where the store is.\n -<secretpath>: Path where the 'something you own' secret is.");
					in.close();
					return;
			default: System.err.println("Bad action!");
					 in.close();
					 return;
		}
		System.out.print("Type an action, or type 'help' for more info: ");
		String action = System.console().readLine();
		while(!action.equals("exit")){
			if(action.equals("help")){
				System.out.println("Possible actions:\n -<r>: Prints all the passwords from the store.\n -<w> <password_name> <password>: Writes a new password into the password store.\n -<d> <password_name>: Deletes the password that matches with the input name.\n -<gp> <password_name> <password_length>: Generates a new password, and writes it in the password store.\n -exit: Exits the current program.");
			} else {
				String arguments[] = action.split(" ");
				switch(arguments[0]){
					case "r": printPasswords(passStore);
							  break;
					case "w": writePassword(passStore, arguments);
							  break;
					case "d": deletePassword(passStore, arguments);
							  break;
					case "gp": generatePassword(passStore, arguments[1], arguments[2]);
							   break;
					default: System.err.println("Bad action. Type 'help' to see the posible actions");
							 break;
				}
			}
			System.out.print("Type an action, or type 'help' for more info: ");
			action = System.console().readLine();
		}
		in.close();
	}
}
