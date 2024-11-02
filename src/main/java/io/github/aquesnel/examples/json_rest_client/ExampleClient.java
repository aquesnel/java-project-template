package io.github.aquesnel.examples.json_rest_client;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.aquesnel.utils.http.SimpleHttpClient;
import java.io.IOException;
import java.net.URI;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.util.Objects;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/** A client for a REST API. */
public final class ExampleClient {

  private static final Logger LOGGER = LoggerFactory.getLogger(ExampleClient.class);
  private static final ObjectMapper MAPPER =
      new ObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, true);

  private final URI mBaseUri;

  private ExampleClient(URI baseUri) {

    mBaseUri = Objects.requireNonNull(baseUri);
  }

  public ExampleResponse fetchData() throws IOException, InterruptedException {

    HttpRequest request =
        HttpRequest.newBuilder() //
            .uri(mBaseUri)
            .timeout(Duration.ofSeconds(20))
            .GET()
            .build();

    HttpResponse<String> response = SimpleHttpClient.makeRequest(request);
    LOGGER
        .atDebug()
        .setMessage("Received call log response")
        .addKeyValue("responseBody", response.body())
        .log();

    return MAPPER.readValue(response.body(), ExampleResponse.class);
  }
}
