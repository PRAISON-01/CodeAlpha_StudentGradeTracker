import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class StudentTest {
    @Test
    void constructorRejectsMissingOrBlankNamesAndTrimsValidNames() {
        assertThrows(IllegalArgumentException.class, () -> new Student(null));
        assertThrows(IllegalArgumentException.class, () -> new Student("  "));
        assertEquals("Amina", new Student(" Amina ").getName());
    }

    @Test
    void getNameReturnsTheNormalizedName() {
        assertEquals("Amina Yusuf", new Student("Amina Yusuf").getName());
    }

    @Test
    void addScoreAcceptsZeroAndMaximumAndRejectsInvalidValues() {
        Student student = new Student("Amina");
        student.addScore(BigDecimal.ZERO);
        student.addScore(BigDecimal.valueOf(100));

        assertEquals(2, student.getScores().size());
        assertThrows(NullPointerException.class, () -> student.addScore(null));
        assertThrows(IllegalArgumentException.class, () -> student.addScore(BigDecimal.valueOf(-0.01)));
        assertThrows(IllegalArgumentException.class, () -> student.addScore(BigDecimal.valueOf(100.01)));
    }

    @Test
    void getScoresReturnsAnImmutableView() {
        Student student = new Student("Amina");
        student.addScore(BigDecimal.valueOf(75));

        assertEquals(1, student.getScores().size());
        assertThrows(UnsupportedOperationException.class,
                () -> student.getScores().add(BigDecimal.valueOf(80)));
    }

    @Test
    void hasScoresReflectsWhetherScoresHaveBeenAdded() {
        Student student = new Student("Amina");

        assertFalse(student.hasScores());
        student.addScore(BigDecimal.ZERO);
        assertTrue(student.hasScores());
    }

    @Test
    void averageReturnsZeroForNoScoresAndRoundsToTwoDecimalPlaces() {
        Student student = new Student("Amina");

        assertEquals(BigDecimal.ZERO, student.average());
        student.addScore(BigDecimal.valueOf(80));
        student.addScore(BigDecimal.valueOf(81));
        student.addScore(BigDecimal.valueOf(82));

        assertEquals(new BigDecimal("81.00"), student.average());
    }
}
