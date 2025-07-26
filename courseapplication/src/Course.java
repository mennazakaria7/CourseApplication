import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;

public class Course {
    private String title;
    private String syllabus;
    private String credit_Hours;
    private String instructor_name;
    private ArrayList<Course> prerequisites = new ArrayList<>();

    private static Map<String, Course> courses = new HashMap<>();
    private static Scanner input = new Scanner(System.in);


    public Course() {}

    public Course(String title, String syllabus, String credit_Hours, String instructor_name) {
        this.title = title;
        this.syllabus = syllabus;
        this.credit_Hours = credit_Hours;
        this.instructor_name = instructor_name;
    }
    public static ArrayList<Course> getAllCourses() {
        return new ArrayList<>(courses.values());
    }

    public static Course getCourseByTitle(String title) {
        return courses.get(title);
    }



    public String getTitle() {
        return title;
    }

    public String getSyllabus() {
        return syllabus;
    }

    public String getCredit_Hours() {
        return credit_Hours;
    }

    public String getInstructor_name() {
        return instructor_name;
    }

    public ArrayList<Course> getPrerequisites() {
        return prerequisites;
    }

    public void addPrerequisite(Course c) {
        if (!prerequisites.contains(c)) {
            prerequisites.add(c);
        }
    }


    public static void saveAllCoursesToFile() {
        try {
            FileWriter writer = new FileWriter("coursesInfo.txt", false);

            for (Course c : courses.values()) {
                StringBuilder preqList = new StringBuilder();
                if (c.prerequisites.isEmpty()) {
                    preqList.append("-");
                } else {
                    for (int i = 0; i < c.prerequisites.size(); i++) {
                        preqList.append(c.prerequisites.get(i).getTitle());
                        if (i < c.prerequisites.size() - 1) {
                            preqList.append("|");
                        }
                    }
                }

                writer.write(c.getTitle() + "," + c.getSyllabus() + "," + c.getCredit_Hours() + "," +
                        c.getInstructor_name() + "," + preqList + "\n");
            }

            writer.close();
            System.out.println("All courses (with prerequisites) saved successfully.");

        } catch (IOException e) {
            System.out.println("Error while saving all courses: " + e.getMessage());
        }
    }


    public static void loadCourses() {
        try {
            File file = new File("coursesInfo.txt");
            if (!file.exists()) {
                System.out.println("No course file found. Skipping load.");
                return;
            }

            Scanner fileScanner = new Scanner(file);
            ArrayList<String[]> rawLines = new ArrayList<>();

            while (fileScanner.hasNextLine()) {
                String line = fileScanner.nextLine();
                String[] parts = line.split(",", 5);

                if (parts.length == 5) {
                    String title = parts[0].trim();
                    String syllabus = parts[1].trim();
                    String creditHours = parts[2].trim();
                    String instructorName = parts[3].trim();
                    String prereqRaw = parts[4].trim();

                    Course c = new Course(title, syllabus, creditHours, instructorName);
                    courses.put(title, c);
                    rawLines.add(parts);
                }
            }
            fileScanner.close();

            for (String[] parts : rawLines) {
                String courseTitle = parts[0].trim();
                String prereqRaw = parts[4].trim();
                Course current = courses.get(courseTitle);
                if (current == null) continue;

                if (!prereqRaw.equals("-")) {
                    String[] preqTitles = prereqRaw.split("\\|");
                    for (String preqTitle : preqTitles) {
                        Course preqCourse = courses.get(preqTitle.trim());
                        if (preqCourse != null) {
                            current.addPrerequisite(preqCourse);
                        }
                    }
                }
            }

            System.out.println("Courses and prerequisites loaded successfully.");
        } catch (IOException e) {
            System.out.println("Error while loading courses: " + e.getMessage());
        }
    }


    public static void add_course() {
        System.out.println("Enter Course title:");
        String title = input.nextLine().trim();
        if (courses.containsKey(title)) {
            System.out.println("Sorry, Course with this title already exists.");
            return;
        }

        System.out.println("Enter Course syllabus:");
        String syllabus = input.nextLine();

        System.out.println("Enter Course credit hours:");
        String credit_Hours = input.nextLine();

        System.out.println("Enter Course instructor name:");
        String instructor_name = input.nextLine();

        Course c = new Course(title, syllabus, credit_Hours, instructor_name);
        courses.put(title, c);

        saveAllCoursesToFile();
        System.out.println("New Course added successfully.");
    }


    public static void view_all_courses_in_system() {
        for (Course c : courses.values()) {
            System.out.println("--------------------------------");
            System.out.println("Title: " + c.title);
            System.out.println("Syllabus: " + c.syllabus);
            System.out.println("Credit Hours: " + c.credit_Hours);
            System.out.println("Instructor: " + c.instructor_name);
            System.out.print("Prerequisites: ");
            if (c.prerequisites.isEmpty()) {
                System.out.println("No Prerequisites");
            } else {
                for (Course p : c.prerequisites) {
                    System.out.print(p.getTitle() + "&&");
                }
                System.out.println();
            }
        }
    }


    public static void SetPrerequisites() {
        System.out.println("Enter Course Title to add prerequisites to it :");
        String courseTitle = input.nextLine().trim();

        Course targetCourse = courses.get(courseTitle);
        if (targetCourse == null) {
            System.out.println("Course not found.");
            return;
        }

        System.out.println("Enter the title of the prerequisite course:");
        String preqTitle = input.nextLine().trim();
        Course preqCourse = courses.get(preqTitle);

        if (preqCourse == null) {
            System.out.println("Prerequisite course not found.");
            return;
        }

        targetCourse.addPrerequisite(preqCourse);
        System.out.println("Prerequisite added successfully.");


        saveAllCoursesToFile();
    }
}
