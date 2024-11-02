package io.github.aquesnel.examples.json_rest_client;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.soabase.recordbuilder.core.RecordBuilder;
import java.util.List;

@RecordBuilder
@RecordBuilder.Options(interpretNotNulls = true)
public record ExampleRecord(
    @JsonProperty("customerId") long customerId,
    @JsonProperty("name") String name,
    @JsonProperty("items") List<String> items)
    implements ExampleRecordBuilder.With {

  public static ExampleRecordBuilder builder() {
    return ExampleRecordBuilder.builder();
  }
}
