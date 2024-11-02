package io.github.aquesnel.utils.cli;

import com.beust.jcommander.JCommander;
import com.beust.jcommander.ParameterDescription;
import com.beust.jcommander.ParameterException;
import com.beust.jcommander.internal.DefaultConsole;
import java.io.IOException;
import java.io.PrintStream;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.IdentityHashMap;
import java.util.List;
import java.util.Map;

public final class ArgumentParser {

  /**
   * Parse, validate, and process cli arguments. Validation errors and help/usage messages will be
   * printed to STDERR.
   *
   * <p>This method will cause the JVM to exit with return code 1 if help/usage is requested or any
   * validation fails.
   *
   * @param <T> The parameter configuration class (ie. with the @Parameter annotations).
   * @param argsClazz - The parameter configuration class (ie. with the @Parameter annotations).
   * @param programName - The program name.
   * @param args - The command line arguments to parse.
   * @return the parsed arguments in a new instance of the argsClazz.
   */
  public static <T> T processCliArguments(Class<T> argsClazz, String[] args) {

    T parsedArgs;
    try {
      parsedArgs = argsClazz.getConstructor().newInstance();
    } catch (InstantiationException
        | IllegalAccessException
        | IllegalArgumentException
        | InvocationTargetException
        | NoSuchMethodException
        | SecurityException e) {
      throw new RuntimeException(e);
    }

    ProgramName programNameAnnotation = argsClazz.getAnnotation(ProgramName.class);
    if (programNameAnnotation == null) {
      throw new IllegalArgumentException(
          "BUG: The class "
              + argsClazz.getName()
              + " is missing the "
              + ProgramName.class.getName()
              + " annotation.");
    }
    String programName = programNameAnnotation.value();

    try (Slf4jOutputStream outputStream = new Slf4jOutputStream()) {
      processCliArguments(
          JCommander.newBuilder()
              .addObject(parsedArgs)
              // to print JCommander debug into to the console/logger, use the JVM arg:
              // -Djcommander.debug=true
              .console(new DefaultConsole(new PrintStream(outputStream)))
              .programName(programName)
              .args(args));

    } catch (IOException e) {
      throw new RuntimeException(e);
    }

    return parsedArgs;
  }

  private static void processCliArguments(JCommander.Builder builder) {

    JCommander argsParser;
    try {
      // The builder is assumed to already have the arguments, and therefore the builder will call
      // JCommander.parse()
      argsParser = builder.build();
    } catch (final ParameterException e) {
      System.err.println("ERROR: " + e.getMessage());
      System.err.println("Use the --help argument for more information.");
      System.err.flush();
      Runtime.getRuntime().exit(1);
      return; // make the compiler happy since the previous line does not return
    }

    final List<Exception> errors = new ArrayList<>();
    Map<Object, Boolean> objectsValidated = new IdentityHashMap<>();
    for (ParameterDescription description : argsParser.getDescriptions().values()) {

      if (description.isHelp()
          && Boolean.TRUE.equals(description.getParameterized().get(description.getObject()))) {
        final StringBuilder usageMessage = new StringBuilder();
        argsParser.usage(usageMessage);
        System.err.println(usageMessage.toString());
        System.err.flush();
        Runtime.getRuntime().exit(0);
      }

      Boolean wasValidated = objectsValidated.put(description.getObject(), true);
      if (!Boolean.TRUE.equals(wasValidated)) {
        if (description.getObject() instanceof ArgumentsValidator validator) {
          errors.addAll(validator.validateArguments());
        }
      }
    }
    if (!errors.isEmpty()) {
      for (Exception error : errors) {
        System.err.println("ERROR: " + error.getMessage());
      }
      System.err.println("Use the --help argument for more information.");
      System.err.flush();
      Runtime.getRuntime().exit(1);
    }
  }
}
