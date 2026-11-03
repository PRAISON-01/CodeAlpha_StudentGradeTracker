import java.math.BigDecimal;
import java.util.Optional;
import java.util.Scanner;

public class StudentGradeTrackerApplication {
    private static final ClassroomGrader grader = new ClassroomGrader();
    private static final Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        runMenu();
    }

    private static void runMenu() {
        boolean running = true;
        while (running) {
            print("""
                    \n=========================
                    STUDENT GRADE TRACKER
                    1. ADD STUDENT
                    2. RECORD SCORE
                    3. VIEW STUDENT AVERAGE
                    4. VIEW CLASS AVERAGE
                    5. VIEW TOP PERFORMER
                    6. VIEW ROSTER
                    7. REMOVE STUDENT
                    0. EXIT
                    =========================
                    """);
            String choice = input("Enter an option >> ");
            switch (choice) {
                case "1" -> addStudent();
                case "2" -> recordScore();
                case "3" -> viewStudentAverage();
                case "4" -> print("Class average: " + grader.classAverage());
                case "5" -> viewTopPerformer();
                case "6" -> viewRoster();
                case "7" -> removeStudent();
                case "0" -> {
                    print("Goodbye!");
                    running = false;
                }
                default -> print("Enter a valid option.");
            }
        }
    }

    private static void addStudent() {
        try {
            String name = input("Enter student name >> ");
            grader.addStudent(new Student(name));
            print("Student added successfully.");
        } catch (IllegalArgumentException exception) {
            print("Error: " + exception.getMessage());
        }
    }

    private static void recordScore() {
        try {
            Student student = findStudentFromInput();
            BigDecimal score = new BigDecimal(input("Enter score (0-100) >> "));
            student.addScore(score);
            print("Score recorded successfully.");
        } catch (IllegalArgumentException | NullPointerException exception) {
            print("Error: " + exception.getMessage());
        }
    }

    private static void viewStudentAverage() {
        try {
            Student student = findStudentFromInput();
            if (student.hasScores()) {
                print(student.getName() + " average: " + student.average());
            } else {
                print(student.getName() + " has no scores yet.");
            }
        } catch (IllegalArgumentException exception) {
            print("Error: " + exception.getMessage());
        }
    }

    private static void viewTopPerformer() {
        Optional<Student> topPerformer = grader.topPerformer();
        if (topPerformer.isPresent()) {
            Student student = topPerformer.get();
            print("Top performer: " + student.getName() + " (" + student.average() + ")");
        } else {
            print("There are no students with scores yet.");
        }
    }

    private static void viewRoster() {
        if (grader.getStudents().isEmpty()) {
            print("The roster is empty.");
            return;
        }
        for (Student student : grader.getStudents()) {
            String average = student.hasScores() ? student.average().toString() : "No scores";
            print(student.getName() + " — " + average);
        }
    }

    private static void removeStudent() {
        String name = input("Enter student name >> ");
        if (grader.removeStudent(name)) {
            print("Student removed successfully.");
        } else {
            print("Student not found.");
        }
    }

    private static Student findStudentFromInput() {
        String name = input("Enter student name >> ");
        return grader.findStudent(name)
                .orElseThrow(() -> new IllegalArgumentException("Student not found."));
    }

    private static String input(String prompt) {
        print(prompt);
        return scanner.nextLine().trim();
    }

    private static void print(String message) {
        System.out.println(message);
    }
}
