import cc.kave.commons.model.events.completionevents.Context;
import org.junit.Test;
import util.IoHelper;


import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;

import static junit.framework.TestCase.assertTrue;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

public class IoHelperTest {
    private static String mockDir = "MockData/contexts";

    @Test
    public void findAllZipsTest() {
        assertEquals(IoHelper.findAllZips(mockDir).toString(), "[01org/acat/src/ACAT.sln-contexts.zip, 01org/acat/src/Setup/PresageInstaller/PresageInstaller.sln-contexts.zip, 01org/acat/src/Setup/ACATCleanup/ACATCleanup.sln-contexts.zip]");
    }

    @Test
    public void readTest() {
        List<Context> res = IoHelper.read("MockData/contexts/01org/acat/src/ACAT.sln-contexts.zip");

        assertEquals(res.size(), 690);
    }

    @Test
    public void removeFileTest() throws IOException {
        List<String> lines = Arrays.asList("testestest testtest", "test");
        Path file = Paths.get("testfile.txt");
        Files.write(file, lines, Charset.forName("UTF-8"));

        IoHelper.removeFile(String.valueOf(file));
        assertFalse(Files.exists(file));

    }

    @Test
    public void removeFileIfEmptyTest() throws IOException {
        List<String> lines = Arrays.asList();
        Path file = Paths.get("testfile.txt");
        Files.write(file, lines, Charset.forName("UTF-8"));

        IoHelper.removeFileIfEmpty(String.valueOf(file));
        assertFalse(Files.exists(file));
    }
}
