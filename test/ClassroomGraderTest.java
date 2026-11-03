import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class ClassroomGraderTest {
    @Test
    void addStudentAddsStudentsAndRejectsNullOrDuplicateNames() {
        ClassroomGrader grader = new ClassroomGrader();
        Student student = new Student("Amina");

        grader.addStudent(student);

        assertSame(student, grader.findStudent("Amina").orElseThrow());
        assertThrows(NullPointerException.class, () -> grader.addStudent(null));
        assertThrows(IllegalArgumentException.class, () -> grader.addStudent(new Student("amina")));
    }

    @Test
    void removeStudentHandlesExistingMissingAndNullNames() {
        ClassroomGrader grader = new ClassroomGrader();
        grader.addStudent(new Student("Amina"));

        assertTrue(grader.removeStudent(" amina "));
        assertFalse(grader.removeStudent("Amina"));
        assertThrows(NullPointerException.class, () -> grader.removeStudent(null));
    }

    @Test
    void findStudentIsCaseInsensitiveAndHandlesMissingAndNullNames() {
        ClassroomGrader grader = new ClassroomGrader();
        Student student = new Student("Amina Yusuf");
        grader.addStudent(student);

        assertSame(student, grader.findStudent(" amina yusuf ").orElseThrow());
        assertTrue(grader.findStudent("Nobody").isEmpty());
        assertThrows(NullPointerException.class, () -> grader.findStudent(null));
    }

    @Test
    void getStudentsReturnsAnImmutableRosterView() {
        ClassroomGrader grader = new ClassroomGrader();
        Student student = new Student("Amina");
        grader.addStudent(student);

        assertEquals(1, grader.getStudents().size());
        assertSame(student, grader.getStudents().get(0));
        assertThrows(UnsupportedOperationException.class,
                () -> grader.getStudents().add(new Student("Tunde")));
    }

    @Test
    void studentCountTracksEnrollmentAndRemoval() {
        ClassroomGrader grader = new ClassroomGrader();

        assertEquals(0, grader.studentCount());
        grader.addStudent(new Student("Amina"));
        assertEquals(1, grader.studentCount());
        grader.removeStudent("Amina");
        assertEquals(0, grader.studentCount());
    }

    @Test
    void classAverageReturnsZeroForEmptyOrUngradedRostersAndAveragesGradedStudents() {
        ClassroomGrader grader = new ClassroomGrader();

        assertEquals(BigDecimal.ZERO, grader.classAverage());
        grader.addStudent(new Student("Ungraded"));
        assertEquals(BigDecimal.ZERO, grader.classAverage());

        Student first = new Student("Amina");
        first.addScore(BigDecimal.valueOf(80));
        Student second = new Student("Tunde");
        second.addScore(BigDecimal.valueOf(90));
        grader.addStudent(first);
        grader.addStudent(second);

        assertEquals(new BigDecimal("85.00"), grader.classAverage());
    }

    @Test
    void topPerformerIsEmptyWithoutScoresAndReturnsHighestScoringStudent() {
        ClassroomGrader grader = new ClassroomGrader();

        assertTrue(grader.topPerformer().isEmpty());
        grader.addStudent(new Student("Ungraded"));
        assertTrue(grader.topPerformer().isEmpty());

        Student first = new Student("Amina");
        first.addScore(BigDecimal.valueOf(90));
        Student second = new Student("Tunde");
        second.addScore(BigDecimal.valueOf(95));
        grader.addStudent(first);
        grader.addStudent(second);

        assertSame(second, grader.topPerformer().orElseThrow());
    }

    @Test
    void topPerformerIncludesZeroScoresAndKeepsFirstStudentOnTies() {
        ClassroomGrader grader = new ClassroomGrader();
        Student first = new Student("Amina");
        Student second = new Student("Tunde");
        first.addScore(BigDecimal.ZERO);
        second.addScore(BigDecimal.ZERO);
        grader.addStudent(first);
        grader.addStudent(second);

        assertSame(first, grader.topPerformer().orElseThrow());
    }
}
