import java.io.*;
import java.util.*;

public class SystemManager {
    public static ArrayList<Student> students = new ArrayList<>();
    private ArrayList<Admin> admins = new ArrayList<>();
    private final String ADMIN_KEY = "2025";
    Scanner input = new Scanner(System.in);

    public SystemManager() {
        loadStudentsFromFile();
        loadAdminsFromFile();
        Course.loadCourses();
    }

    public static void saveAllStudentsToFile() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter("students.txt", false))) {
            for (Student s : students) {
                StringBuilder sb = new StringBuilder();
                sb.append(s.getID()).append(",")
                        .append(s.getUsername()).append(",")
                        .append(s.getPassword());

                for (Student.Course_grade cg : s.getRegisteredCourses()) {
                    sb.append(",")
                            .append(cg.getCourse_title()).append("-")
                            .append(cg.getSemester()).append("-")
                            .append(cg.getGrade() == null ? "null" : cg.getGrade());
                }

                writer.write(sb.toString());
                writer.newLine();
            }
            System.out.println(" All students saved to file.");
        } catch (IOException e) {
            System.out.println(" Error saving students: " + e.getMessage());
        }
    }

    private void loadStudentsFromFile() {
        try {
            File file = new File("students.txt");
            if (!file.exists()) return;

            Scanner fileScanner = new Scanner(file);
            while (fileScanner.hasNextLine()) {
                String line = fileScanner.nextLine();
                String[] parts = line.split(",");

                if (parts.length >= 3) {
                    String id = parts[0];
                    String username = parts[1];
                    String password = parts[2];

                    Student s = new Student(id, username, password);

                    for (int i = 3; i < parts.length; i++) {
                        String[] courseParts = parts[i].split("-");
                        if (courseParts.length == 3) {
                            String courseTitle = courseParts[0];
                            String semester = courseParts[1];
                            String gradeStr = courseParts[2];

                            Float grade = null;
                            if (!gradeStr.equalsIgnoreCase("null")) {
                                grade = Float.parseFloat(gradeStr);
                            }

                            Student.Course_grade cg = s.new Course_grade(courseTitle, semester, null);
                            cg.setGrade(grade);
                            s.getRegisteredCourses().add(cg);
                        }
                    }

                    students.add(s);
                }
            }

            fileScanner.close();
            System.out.println(" Students loaded from file with courses.");
        } catch (IOException e) {
            System.out.println(" Error reading students from file: " + e.getMessage());
        }
    }

    public void save_admins_tofile(Admin a) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter("Admins.txt", true))) {
            writer.write(a.getID() + "," + a.getUsername() + "," + a.getPassword());
            writer.newLine();
            System.out.println(" Admin saved to file successfully");
        } catch (IOException e) {
            System.out.println(" Error while writing to admins file: " + e.getMessage());
        }
    }

    private void loadAdminsFromFile() {
        try {
            File file = new File("admins.txt");
            if (!file.exists()) return;

            Scanner fileScanner = new Scanner(file);
            while (fileScanner.hasNextLine()) {
                String line = fileScanner.nextLine();
                String[] parts = line.split(",");

                if (parts.length == 3) {
                    String id = parts[0];
                    String username = parts[1];
                    String password = parts[2];

                    Admin a = new Admin(id, username, password);
                    admins.add(a);
                }
            }

            fileScanner.close();
            System.out.println(" Admins loaded from file.");
        } catch (IOException e) {
            System.out.println(" Error reading admins from file: " + e.getMessage());
        }
    }

    public void signUp() {
        System.out.println("Enter UserType (student or admin):");
        String usertype = input.nextLine().toLowerCase();

        String ID;
        boolean exists;

        do {
            exists = false;
            System.out.println("Enter ID:");
            ID = input.nextLine();

            for (Student s : students) {
                if (s.getID().equals(ID)) {
                    exists = true;
                    System.out.println("ID already exists. Please enter a unique ID.");
                    break;
                }
            }
            for (Admin a : admins) {
                if (a.getID().equals(ID)) {
                    exists = true;
                    System.out.println("ID already exists. Please enter a unique ID.");
                    break;
                }
            }
        } while (exists);

        System.out.println("Enter Username:");
        String username = input.nextLine();

        System.out.println("Enter Password:");
        String password = input.nextLine();

        if (usertype.equals("student")) {
            Student s = new Student(ID, username, password);
            students.add(s);
            saveAllStudentsToFile();
            System.out.println(" Student signed up successfully.");
        } else if (usertype.equals("admin")) {
            System.out.print("Enter admin key: ");
            String key = input.nextLine();
            if (key.equals(ADMIN_KEY)) {
                Admin a = new Admin(ID, username, password);
                admins.add(a);
                save_admins_tofile(a);
                System.out.println(" Admin signed up successfully.");
            } else {
                System.out.println(" Invalid admin key. You can't be admin.");
            }
        } else {
            System.out.println(" Invalid user type.");
        }
    }

    public User signIn() {
        System.out.println("Enter Username:");
        String username = input.nextLine().toLowerCase();

        System.out.println("Enter Password:");
        String password = input.nextLine();

        for (Student s : students) {
            if (s.getUsername().equals(username) && s.getPassword().equals(password)) {
                System.out.println(" Logged In Successfully as a " + s.getUsertype());
                return s;
            }
        }

        for (Admin a : admins) {
            if (a.getUsername().equals(username) && a.getPassword().equals(password)) {
                System.out.println(" Logged In Successfully as a " + a.getUsertype());
                return a;
            }
        }

        System.out.println(" Failed login");
        return null;
    }
}