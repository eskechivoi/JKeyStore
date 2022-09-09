import java.util.*;

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

	public static void main (String args[]){
		if(args.length<2){System.out.println("Bad arguments. Expected <store_name> <store_password>. Type 'help' for more info");
		return;}
		PasswordStore passStore = new PasswordStore(args[0],args[0],args[1]);
		Scanner in = new Scanner(System.in);
		System.out.print("Type an action, or type 'help' for more info: ");
		String action = in.nextLine();
		while(!action.equals("exit")){
			if(action.equals("help")){
				System.out.println("Program arguments:\n -<store_name>: The name of the password store.\n -<store_password>: The master password of the store, used for encryption.\n");
				System.out.println("Possible actions:\n -<r>: Prints all the passwords from the store.\n -<w> <password_name> <password>: Writes a new password into the password store.\n -<d> <password_name>: Deletes the password that matches with the input name.\n -exit: Exits the current program.");
			} else {
				String arguments[] = action.split(" ");
				switch(arguments[0]){
					case "r": printPasswords(passStore);
							  break;
					case "w": writePassword(passStore, arguments);
							  break;
					case "d": deletePassword(passStore, arguments);
							  break;
					default: System.out.println("Bad action. Type 'help' to see the posible actions");
							 break;
				}
			}
			action = in.nextLine();
		}
		in.close();
	}
}
