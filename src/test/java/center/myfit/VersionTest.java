package center.myfit;

import org.apache.maven.model.Model;
import org.apache.maven.model.io.xpp3.MavenXpp3Reader;
import org.junit.jupiter.api.Test;

import java.io.FileReader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static org.apache.commons.lang3.StringUtils.isNotBlank;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class VersionTest {
  public static final String regexp = "(image: \")(.*)(\")";
  public static final Pattern pattern = Pattern.compile(regexp);

  @Test
  void deploymentVersion_shouldBeUpdated() throws Exception {
    String yaml;
    MavenXpp3Reader reader = new MavenXpp3Reader();
    Model model = reader.read(new FileReader("pom.xml"));
    String version = model.getVersion();

    if (version.endsWith("SNAPSHOT")) {
      yaml = "k3s/stage-deployment.yml";
    } else {
      yaml = ""; // TODO присвоить релизный депплоймент
    }
    String string = Files.readString(Path.of(yaml));

    assertTrue(isNotBlank(string), "Не удалось прочитать файл: " + yaml);

    Matcher matcher = pattern.matcher(string);
    String dockerImage = "";
    if (matcher.find()) {
      dockerImage = matcher.group(2);
    }

    assertTrue(isNotBlank(dockerImage), "Не найдено имя образа в файле " + yaml);

    assertTrue(dockerImage.endsWith(version), "Не совпадают версии!");
  }
}
