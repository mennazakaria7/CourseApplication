import java.util.*;

public class Main {
    public static void main(String[] args) {
        //TIP Press <shortcut actionId="ShowIntentionActions"/> with your caret at the highlighted text
        // to see how IntelliJ IDEA suggests fixing it.
        Scanner input = new Scanner(System.in);
        SystemManager manager = new SystemManager();

       String choice = "";



        while (!choice.equals("0")) {
            System.out.println("\n--- Main Menu ---");
            System.out.println("1. Sign Up");
            System.out.println("2. Sign In");
            System.out.println("0. Exit");
            System.out.print("Enter your choice: ");
            choice = input.nextLine().trim();

            switch (choice) {
                case "1":
                    manager.signUp();
                    break;
                case "2": {
                    User user = manager.signIn();
                    if (user != null) {
                        if (user instanceof Admin) {
                            Admin.adminMenu();
                        } else if (user instanceof Student) {
                            ((Student) user).StudentMenu();
                        }
                    }
                    break;
                }
                case "0":
                    System.out.println("Exiting program...");
                    break;
                default:
                    System.out.println("Invalid choice. Try again.");
            }
        }
    }


}