package center.myfit.util;

import org.springframework.test.web.servlet.request.RequestPostProcessor;

public class TestConstants {
  public static final String KEYCLOAK_ID = "123";
  public static final RequestPostProcessor POST =
      request -> {
        request.setMethod("POST");
        return request;
      };
  public static final RequestPostProcessor PUT =
      request -> {
        request.setMethod("PUT");
        return request;
      };
}
