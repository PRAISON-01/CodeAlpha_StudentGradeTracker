import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

public class ClassroomGrader {
    private final List<Student> students = new ArrayList<>();

    public void addStudent(Student student) {
        Objects.requireNonNull(student, "Student must not be null");
        if (findStudent(student.getName()).isPresent()) {
            throw new IllegalArgumentException("A student with this name is already enrolled");
        }
        students.add(student);
    }

    public boolean removeStudent(String studentName) {
        Objects.requireNonNull(studentName, "Student name must not be null");
        return students.removeIf(student -> student.getName().equalsIgnoreCase(studentName.trim()));
    }

    public Optional<Student> findStudent(String studentName) {
        Objects.requireNonNull(studentName, "Student name must not be null");
        String normalizedName = studentName.trim();
        return students.stream()
                .filter(student -> student.getName().equalsIgnoreCase(normalizedName))
                .findFirst();
    }

    public List<Student> getStudents() {
        return Collections.unmodifiableList(students);
    }

    public int studentCount() {
        return students.size();
    }

    public BigDecimal classAverage() {
        List<Student> gradedStudents = students.stream().filter(Student::hasScores).toList();
        if (gradedStudents.isEmpty()) {
            return BigDecimal.ZERO;
        }
        BigDecimal total = gradedStudents.stream()
                .map(Student::average)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        return total.divide(BigDecimal.valueOf(gradedStudents.size()), 2, RoundingMode.HALF_UP);
    }

    public Optional<Student> topPerformer() {
        return students.stream()
                .filter(Student::hasScores)
                .max((first, second) -> first.average().compareTo(second.average()));
    }
}
