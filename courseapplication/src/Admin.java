import java.util.ArrayList;
import java.util.Scanner;
public class Admin extends User {

    static Scanner input = new Scanner(System.in);
    public Admin( String ID, String username, String password) {
        super(ID, username, password);
        this.usertype="admin";
    }

    public Admin() {
        super();
    }
    public static void SetGrade(ArrayList<Student> students){
        System.out.println("Enter Student ID:");
        String id = input.nextLine().trim();

        for (Student std : students) {
            if (std.getID().equalsIgnoreCase(id)) {

                System.out.println("Enter course title to add grade to it:");
                String title = input.nextLine().trim();

                System.out.println("Enter course grade:");
                Float grade = input.nextFloat();

                boolean found = false;

                for (Student.Course_grade c : std.getRegisteredCourses()) {
                    if (c.getCourse_title().equalsIgnoreCase(title)) {
                        c.setGrade(grade);
                        System.out.println(" Grade set successfully for course: " + title);

                        SystemManager.saveAllStudentsToFile();
                        found = true;

                        break;
                    }
                }

                if (!found) {
                    System.out.println(" Course not found in student's registered courses.");
                }

                return;
            }

        }

        System.out.println(" Student ID not found.");
    }

    public static void adminMenu() {
        Scanner input = new Scanner(System.in);
        String choice = "";

        while (!choice.equals("0")) {
            System.out.println("\n--- Admin Menu ---");
            System.out.println("1. Add New Course");
            System.out.println("2. Set Prerequisites for a Course");
            System.out.println("3. View All Courses");
            System.out.println("4. Manage Student Grades");
            System.out.println("0. Back to Main Menu");
            System.out.print("Enter your choice: ");
            choice = input.nextLine().trim();

            switch (choice) {
                case "1":
                    Course.add_course();
                    break;
                case "2":
                    Course.SetPrerequisites();
                    break;
                case "3":
                    Course.view_all_courses_in_system();
                    break;
                case "4":
                    SetGrade(SystemManager.students);
                    break;
                case "0":
                    System.out.println("Returning to main menu...");
                    break;
                default:
                    System.out.println("Invalid choice.");
            }
        }
    }


}
