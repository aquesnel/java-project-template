package io.github.aquesnel.examples;

import com.beust.jcommander.Parameter;
import io.github.aquesnel.utils.cli.ArgumentParser;
import io.github.aquesnel.utils.cli.ArgumentsValidator;
import io.github.aquesnel.utils.cli.ProgramName;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class Main {

  private static final Logger LOGGER = LoggerFactory.getLogger(Main.class);

  @ProgramName("example-main")
  public static final class CommandLineArguments implements ArgumentsValidator {

    @Parameter(
        names = {"-g", "--greet"},
        description = "The name to greet.",
        help = true)
    public String greet = "World!";

    @Parameter(
        names = {"-h", "--help"},
        description = "Display the usage documentation.",
        help = true)
    public boolean help;

    public static CommandLineArguments parse(String[] args) {
      return ArgumentParser.processCliArguments(CommandLineArguments.class, args);
    }

    @Override
    public List<Exception> validateArguments() {

      List<Exception> validationErrors = new ArrayList<>();

      if (greet == null) {
        validationErrors.add(
            new IllegalArgumentException(
                "The --greet argument must be non-null. Received: "
                    + Objects.toString(greet)
                    + "\n"));
      }

      return validationErrors;
    }
  }

  private final String mGreet;

  public Main(String greet) {
    mGreet = Objects.requireNonNull(greet);
  }

  public static void main(String[] args) throws Exception {

    CommandLineArguments parsedArgs = CommandLineArguments.parse(args);
    final Main main = create(parsedArgs);

    final int exitCode = main.run();

    System.out.flush();
    System.err.flush();
    Runtime.getRuntime().exit(exitCode);
  }

  private static Main create(CommandLineArguments parsedArgs) {
    return new Main(parsedArgs.greet);
  }

  public int run() {

    LOGGER.atInfo().setMessage("Hello " + mGreet).addKeyValue("greet", mGreet).log();
    return 0;
  }
}
