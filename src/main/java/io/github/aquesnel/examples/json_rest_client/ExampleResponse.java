package io.github.aquesnel.examples.json_rest_client;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.soabase.recordbuilder.core.RecordBuilder;
import java.util.List;

@RecordBuilder
@RecordBuilder.Options(interpretNotNulls = true)
public record ExampleResponse(@JsonProperty("records") List<ExampleRecord> callRecords)
    implements ExampleResponseBuilder.With {

  public static ExampleResponseBuilder builder() {
    return ExampleResponseBuilder.builder();
  }
}
