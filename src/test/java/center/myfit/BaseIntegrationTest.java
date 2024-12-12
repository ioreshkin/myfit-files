package center.myfit;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.minio.MinioClient;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.cloud.contract.wiremock.AutoConfigureWireMock;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

/** Базовый класс SpringBootTest */
@SpringBootTest(
    webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT,
    useMainMethod = SpringBootTest.UseMainMethod.ALWAYS)
@ActiveProfiles("test")
@AutoConfigureWireMock(port = 0)
public abstract class BaseIntegrationTest {
  protected ObjectMapper mapper = new ObjectMapper();
  @MockBean protected MinioClient minioClient;
  @MockBean protected RabbitTemplate rabbitTemplate;

  protected MockMvc mockMvc;

  /** Конструктор. */
  protected BaseIntegrationTest(WebApplicationContext context) {
    mockMvc = MockMvcBuilders.webAppContextSetup(context).build();
  }
}
