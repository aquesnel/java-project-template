package io.github.aquesnel.utils.http;

import io.soabase.recordbuilder.core.RecordBuilder;
import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpClient.Redirect;
import java.net.http.HttpClient.Version;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.net.http.HttpResponse.BodyHandlers;
import java.time.Duration;
import java.util.List;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/** A simple HttpClient wrapper that logs the request and response. */
public final class SimpleHttpClient {

  private static final Logger LOGGER = LoggerFactory.getLogger(SimpleHttpClient.class);

  public static HttpResponse<String> makeRequest(HttpRequest request)
      throws IOException, InterruptedException {

    HttpClient client =
        HttpClient.newBuilder()
            .version(Version.HTTP_1_1)
            .followRedirects(Redirect.NORMAL)
            .connectTimeout(Duration.ofSeconds(10))
            .build();

    LOGGER
        .atInfo()
        .setMessage("Sending HTTP request")
        .addKeyValue("request", HttpRequestLogSchema.from(request))
        .log();

    HttpResponse<String> response = client.send(request, BodyHandlers.ofString());
    LOGGER
        .atInfo()
        .setMessage("Received HTTP response")
        .addKeyValue("response", HttpResponseLogSchema.from(response))
        .log();

    return response;
  }

  // TODO: I'm not able to easily extract the body from the request, so just exclude it for now.
  @RecordBuilder
  @RecordBuilder.Options(interpretNotNulls = true)
  public static record HttpRequestLogSchema(
      String method, URI uri, Map<String, List<String>> headers)
      implements SimpleHttpClientHttpRequestLogSchemaBuilder.With {

    public static SimpleHttpClientHttpRequestLogSchemaBuilder builder() {
      return SimpleHttpClientHttpRequestLogSchemaBuilder.builder();
    }

    public static HttpRequestLogSchema from(HttpRequest request) {
      return builder()
          .method(request.method())
          .uri(request.uri())
          .headers(request.headers().map())
          .build();
    }
  }

  @RecordBuilder
  @RecordBuilder.Options(interpretNotNulls = true)
  public static record HttpResponseLogSchema(
      int statusCode, URI uri, Map<String, List<String>> headers, String body)
      implements SimpleHttpClientHttpResponseLogSchemaBuilder.With {

    public static SimpleHttpClientHttpResponseLogSchemaBuilder builder() {
      return SimpleHttpClientHttpResponseLogSchemaBuilder.builder();
    }

    public static HttpResponseLogSchema from(HttpResponse<?> response) {
      return builder()
          .statusCode(response.statusCode())
          .uri(response.uri())
          .headers(response.headers().map())
          .body(response.body().toString())
          .build();
    }
  }
}
