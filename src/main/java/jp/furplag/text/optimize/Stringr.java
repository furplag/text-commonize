package jp.furplag.text.optimize;

import java.util.Arrays;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public interface Stringr {

  /**
   * shorthand for {@code ((String) RegexrOrigin.join(strings)).codePoints()} .
   *
   * @param strings the string(s), maybe null
   * @return an IntStream of Unicode code points from this strings
   * @see {@link #join(String...)}
   * @see {@link String#codePoints()}
   */
  static IntStream codePoints(final String... strings) {
    return join(strings).codePoints();
  }

  /**
   * shorthand for {@code (Objects.toString(RegexrOrigin.join(strings), "")).isEmpty()} .
   *
   * @param strings the string(s), maybe null
   * @return {@code true} if {@link String#length() length} is {@code 0} or {@code null}, otherwise {@code false}
   */
  static boolean isEmpty(final String... strings) {
    return join(strings).isEmpty();
  }

  /**
   * joins the elements of the provided array into a single String containing the provided list of elements.
   * <p>
   * Null objects within the array are represented by empty strings.
   * </p>
   *
   * @param strings the string(s), maybe null
   * @return the joined string, return empty string if null array input.
   */
  static String join(final String... strings) {
    // @formatter:off
    return Arrays.stream(
      Optional.ofNullable(strings).orElse(new String[] {}))
        .filter(Objects::nonNull)
        .filter(((Predicate<String>) String::isEmpty).negate())
      .collect(Collectors.joining());
    // @formatter:on
  }

  /**
   * shorthand for {@code new String(((int[]) codePoints), 0, codePoints.length)} .
   *
   * @param codePoints Array that is the source of Unicode code points
   * @return the string represented by Unicode code points.
   * @see {@link String#String(int[], int, int)}
   */
  static String newString(final int... codePoints) {
    final int[] temporal = verifyCodePoints(codePoints);

    return temporal.length < 1 ? "" : new String(temporal, 0, temporal.length);
  }

  /**
   * exclude from the provided array if the value of the elements contains that is a invalid Unicode code point .
   *
   * @param codePoints Array that is the source of Unicode code points
   * @return an array of Unicode code points not contains invalid value
   * @see {@link Character#isValidCodePoint(int)}
   */
  static int[] verifyCodePoints(final int... codePoints) {
    // @formatter:off
    return Arrays.stream(
      Optional.ofNullable(codePoints).orElse(new int[] {}))
        .filter(Character::isValidCodePoint)
      .toArray();
    // @formatter:on
  }

}
