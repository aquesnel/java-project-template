package io.github.aquesnel.utils.cli;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Objects;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.event.Level;

public final class Slf4jOutputStream extends OutputStream {

  private final Logger mLogger;
  private final Level mLevel;
  private final ByteArrayOutputStream mBuffer = new ByteArrayOutputStream(1000);

  public Slf4jOutputStream() {
    this(LoggerFactory.getLogger(Slf4jOutputStream.class), Level.DEBUG);
  }

  public Slf4jOutputStream(Logger logger, Level level) {
    mLogger = Objects.requireNonNull(logger);
    mLevel = Objects.requireNonNull(level);
  }

  @Override
  public void write(int b) {
    if (b == '\n') {
      String line = mBuffer.toString();
      mBuffer.reset();
      mLogger.atLevel(mLevel).log(line);
    } else if (b == '\r') {
      // skip
    } else {
      mBuffer.write(b);
    }
  }

  @Override
  public void flush() throws IOException {
    mBuffer.flush();
    String line = mBuffer.toString();
    mBuffer.reset();
    mLogger.atLevel(mLevel).log(line);
  }

  @Override
  public void close() throws IOException {
    flush();
    mBuffer.close();
  }
}
