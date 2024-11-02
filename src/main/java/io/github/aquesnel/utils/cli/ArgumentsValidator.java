package io.github.aquesnel.utils.cli;

import java.util.List;

public interface ArgumentsValidator {

  List<? extends Exception> validateArguments();
}
