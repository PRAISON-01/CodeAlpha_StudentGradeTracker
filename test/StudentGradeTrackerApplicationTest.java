import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;

import static org.junit.jupiter.api.Assertions.assertTrue;

class StudentGradeTrackerApplicationTest {
    @Test
    void mainDisplaysMenuAndExitsWhenExitIsSelected() throws Exception {
        InputStream originalInput = System.in;
        PrintStream originalOutput = System.out;
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        System.setIn(new ByteArrayInputStream("0\n".getBytes(StandardCharsets.UTF_8)));
        System.setOut(new PrintStream(output, true, StandardCharsets.UTF_8));

        try {
            StudentGradeTrackerApplication.main(new String[0]);
        } finally {
            System.setIn(originalInput);
            System.setOut(originalOutput);
        }

        String displayedText = output.toString(StandardCharsets.UTF_8);
        assertTrue(displayedText.contains("STUDENT GRADE TRACKER"));
        assertTrue(displayedText.contains("Goodbye!"));
    }
}
