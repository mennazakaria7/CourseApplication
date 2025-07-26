import java.util.*;

public class Student extends User {
    Scanner input = new Scanner(System.in);


    public class Course_grade {
        private String course_title;
        private String semester;
        private Float grade;

        public Course_grade(String course_title, String semester, Float grade) {
            this.course_title = course_title;
            this.semester = semester;
            this.grade = grade;
        }

        public String getCourse_title() {
            return course_title;
        }

        public void setCourse_title(String course_title) {
            this.course_title = course_title;
        }

        public String getSemester() {
            return semester;
        }

        public void setSemester(String semester) {
            this.semester = semester;
        }

        public Float getGrade() {
            return grade;
        }

        public void setGrade(Float grade) {
            this.grade = grade;
        }
    }


    private List<Course_grade> registeredCourses;

    public List<Course_grade> getRegisteredCourses() {
        return registeredCourses;
    }

    // Constructor
    public Student(String ID, String username, String password) {
        super(ID, username, password);
        this.usertype = "student";
        this.registeredCourses = new ArrayList<>();
    }


    public void registerCourse() {
        System.out.println(" Available courses in system:");
        for (Course c : Course.getAllCourses()) {
            System.out.println("- " + c.getTitle() + " | Instructor: " + c.getInstructor_name());
        }

        String choice = "";

        while (!choice.equals("0")) {
            System.out.println("\n--- Register Course ---");
            System.out.println("1. Register for a course");
            System.out.println("0. Exit");
            System.out.print("Enter your choice: ");
            choice = input.nextLine().trim();

            if (choice.equals("1")) {
                System.out.print("Enter Course Title: ");
                String title = input.nextLine().trim();

                boolean found = false;
                for (Course c : Course.getAllCourses()) {
                    if (c.getTitle().equalsIgnoreCase(title)) {


                        System.out.print("Prerequisites for this course: ");
                        if (c.getPrerequisites().isEmpty()) {
                            System.out.println("None");
                        } else {
                            for (Course pre : c.getPrerequisites()) {
                                System.out.print(pre.getTitle() + " ");
                            }
                            System.out.println();
                        }


                        boolean prerequisitesMet = true;
                        for (Course pre : c.getPrerequisites()) {
                            boolean passed = false;
                            for (Course_grade g : registeredCourses) {
                                if (g.getCourse_title().equalsIgnoreCase(pre.getTitle())
                                        && g.getGrade() != null
                                        && g.getGrade() >= 50) {
                                    passed = true;
                                    break;
                                }
                            }
                            if (!passed) {
                                prerequisitesMet = false;
                                System.out.println(" You must complete prerequisite course: " + pre.getTitle() + " with at least 50.");
                            }
                        }

                        if (!prerequisitesMet) {
                            System.out.println("️ You cannot register for this course until prerequisites are completed.");
                            found = true;
                            break;
                        }

                        boolean alreadyRegistered = false;
                        for (Course_grade g : registeredCourses) {
                            if (g.getCourse_title().equalsIgnoreCase(title)) {
                                alreadyRegistered = true;
                                break;
                            }
                        }

                        if (alreadyRegistered) {
                            System.out.println(" You are already registered in this course.");
                        } else {
                            System.out.print("Enter Semester: ");
                            String semester = input.nextLine().trim();

                            registeredCourses.add(new Course_grade(title, semester, null));
                            System.out.println(" Registered successfully for course: " + title);
                        }

                        found = true;
                        break;
                    }
                }

                if (!found) {
                    System.out.println(" Course not found.");
                }

            } else if (!choice.equals("0")) {
                System.out.println("Invalid choice.");
            }
        }


        SystemManager.saveAllStudentsToFile();
    }


    public void ViewGrades(){



        for(Student std : SystemManager.students){

            if(std.getID().equals(this.ID)){
                System.out.println("Completed Courses:");
                for(Course_grade c : std.getRegisteredCourses()){
                    if(c.getGrade()!=null){

                        System.out.println("Course Name:"+c.getCourse_title());
                        System.out.println("Course Semester:"+c.getSemester());
                        System.out.println("Course Grade:"+c.getGrade());
                        System.out.println("=======================================");
                    }
                              else{
                                  System.out.println(c.getCourse_title()+" Course Not Completed Yet");

                    }
                }


            }




        }


    }
    public void calculateGPA() {
        float totalPoints = 0;
        int totalCredits = 0;

        for (Course_grade cg : registeredCourses) {
            if (cg.getGrade() != null) {
                float grade = cg.getGrade();
                Course course = Course.getCourseByTitle(cg.getCourse_title());

                if (course != null) {
                    int creditHours = Integer.parseInt(course.getCredit_Hours());

                    float point = 0;
                    if (grade >= 90) point = 4.0f;
                    else if (grade >= 85) point = 3.7f;
                    else if (grade >= 80) point = 3.3f;
                    else if (grade >= 75) point = 3.0f;
                    else if (grade >= 70) point = 2.7f;
                    else if (grade >= 65) point = 2.3f;
                    else if (grade >= 60) point = 2.0f;
                    else if (grade >= 50) point = 1.0f;
                    else point = 0.0f;

                    totalPoints += point * creditHours;
                    totalCredits += creditHours;
                }
            }
        }

        if (totalCredits == 0) {
            System.out.println(" No graded courses to calculate GPA.");
        } else {
            float gpa = totalPoints / totalCredits;
            System.out.printf(" Your GPA is: %.2f\n", gpa);
        }
    }
    public void generateReport() {
        System.out.println("Student Name: " + this.username);
        System.out.println("Student ID: " + this.ID);
        System.out.println("\nCourses Taken:");
        System.out.println("------------------------------------");
        System.out.printf("| %-12s | %-8s | %-5s |\n", "Course Title", "Semester", "Grade");
        System.out.println("------------------------------------");

        double totalGrades = 0;
        int validGradesCount = 0;

        for (Course_grade cg : registeredCourses) {
            if (cg.grade != null) {
                System.out.printf("| %-12s | %-8s | %-5.2f |\n", cg.course_title, cg.semester, cg.grade);
                totalGrades += cg.grade;
                validGradesCount++;
            } else {
                System.out.printf("| %-12s | %-8s | %-5s |\n", cg.course_title, cg.semester, "N/A");
            }
        }

        System.out.println("------------------------------------");

        double gpa = validGradesCount > 0 ? (totalGrades / validGradesCount) / 25.0 : 0;
        System.out.printf("\nOverall GPA: %.2f\n", gpa);

    }

    public void StudentMenu() {
        String choice = "";

        while (!choice.equals("0")) {
            System.out.println("\n--- Student Menu ---");
            System.out.println("1. Register in specific course");
            System.out.println("2. View grades in completed courses");
            System.out.println("3. Calculate Gpa ");
            System.out.println("4. Generate a report ");
            System.out.println("0. Exit");
            System.out.print("Enter your choice: ");
            choice = input.nextLine().trim();

            switch (choice) {
                case "1":
                    registerCourse();
                    break;
                case "2":
                   ViewGrades();
                    break;
                case "3":
                  calculateGPA();
                    break;

                case "4":
                    generateReport();
                    break;


                case "0":
                    System.out.println("Logging out...");
                    break;
                default:
                    System.out.println("Invalid choice.");
            }
        }
    }



}
