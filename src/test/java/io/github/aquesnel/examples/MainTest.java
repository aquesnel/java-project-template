package io.github.aquesnel.examples;

import java.io.IOException;
import java.io.InputStream;
import org.hamcrest.MatcherAssert;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

public final class MainTest {

  @BeforeEach
  public void setup() {}

  @AfterEach
  public void tearDown() {}

  @Test
  public void test_run_whenAlways_returnsZero() {

    // setup
    Main main = new Main("World!");
    InputStream mockInputStream = Mockito.mock(InputStream.class);

    // test
    int result = main.run();

    // verify
    MatcherAssert.assertThat(result, Matchers.equalTo(0));

    Mockito.verifyNoInteractions(mockInputStream);
  }

  @Test
  public void test_mocking_whenAlways_returnsSuccess() throws IOException {

    // setup
    InputStream mockInputStream = Mockito.mock(InputStream.class);

    // test
    mockInputStream.available();

    // verify

    Mockito.verify(mockInputStream).available();
  }
}
