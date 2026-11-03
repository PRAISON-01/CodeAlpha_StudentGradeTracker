import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

public class Student {
    public static final BigDecimal MIN_SCORE = BigDecimal.ZERO;
    public static final BigDecimal MAX_SCORE = BigDecimal.valueOf(100);

    private final String name;
    private final List<BigDecimal> scores = new ArrayList<>();

    public Student(String name) {
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("Student name must not be blank");
        }
        this.name = name.trim();
    }

    public String getName() {
        return name;
    }

    public void addScore(BigDecimal score) {
        Objects.requireNonNull(score, "Score must not be null");
        if (score.compareTo(MIN_SCORE) < 0 || score.compareTo(MAX_SCORE) > 0) {
            throw new IllegalArgumentException("Score must be between 0 and 100");
        }
        scores.add(score);
    }

    public List<BigDecimal> getScores() {
        return Collections.unmodifiableList(scores);
    }

    public boolean hasScores() {
        return !scores.isEmpty();
    }

    public BigDecimal average() {
        if (scores.isEmpty()) {
            return BigDecimal.ZERO;
        }
        BigDecimal total = scores.stream().reduce(BigDecimal.ZERO, BigDecimal::add);
        return total.divide(BigDecimal.valueOf(scores.size()), 2, RoundingMode.HALF_UP);
    }
}
