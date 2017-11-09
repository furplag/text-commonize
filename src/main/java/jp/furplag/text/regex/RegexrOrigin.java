/**
 * Copyright (C) 2017+ furplag (https://github.com/furplag)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package jp.furplag.text.regex;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Predicate;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

/**
 * DRY, DRY, DRY...
 *
 * @author furplag
 *
 */
public interface RegexrOrigin {

  /**
   * returns result of evaluate that {@link Matcher#find(String)} .
   *
   * @param string the string
   * @param regexrs {@link Regexr Regexr(s)}
   * @return {@code true} if, and only if, a subsequence of the input sequence matches this pattern
   */
  static boolean anyMatch(final String string, final Regexr... regexrs) {
    return regexrs(regexrs).filter(regexr->regexr.matches(string)).count() > 0;
  }

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
   * returns matched elements any of {@code regexrs} of the given string.
   *
   * @param string the string, maybe null
   * @param regexrs {@link Regexr Regexr(s)}
   * @return matched elements any of {@code regexrs} of the given string
   */
  static List<String> findAny(final String string, final Regexr... regexrs) {
    return regexrs(regexrs).flatMap(regexr->regexr.find(string).stream()).collect(Collectors.toCollection(ArrayList::new));
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
   * shorthand for {@code Arrays.stream(regexrs).filter(Objects::nonNull).sorted()} .
   *
   * @param regexrs {@link Regexr Regexr(s)}
   * @return treatmented {@code regexrs}
   */
  static Stream<Regexr> regexrs(final Regexr... regexrs) {
    // @formatter:off
    return Arrays.stream(Optional.ofNullable(regexrs).orElse(new Regexr[]{}))
      .filter(Objects::nonNull)
      .sorted();
    // @formatter:on
  }

  /**
   * shorthand for {@code regexrs.foreach(r->r.replaceAll(string))}.
   *
   * @param string the string, maybe null
   * @param regexrs {@link Regexr Regexr(s)}
   * @return the string constructed by replacing each matching subsequence by the replacement string
   */
  static String replaceAll(final String string, final Regexr... regexrs) {
    if (RegexrOrigin.isEmpty(string)) return string;
    final String[] result = {string};
    regexrs(regexrs).forEach(regexr->result[0] = regexr.replaceAll(result[0]));

    return result[0];
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

  /**
   * returns matched elements of the given string .
   *
   * @param string the string
   * @return matched elements of the given string
   */
  List<String> find(final String string);


  /**
   * returns result of evaluate that {@link Matcher#find(String)} .
   *
   * @param string the string
   * @return {@code true} if, and only if, a subsequence of the input sequence matches this pattern
   *
   */
  boolean matches(final String string);

  /** return {@code this.order}. */
  int order();

  /** return {@code this.pattern}. */
  Pattern pattern();

  /**
   * shorthand for {@link Pattern#pattern()} .
   *
   * @return The source of this pattern
   */
  default String regex() {
    return pattern() == null ? null : pattern().pattern();
  }

  /**
   * replaces every subsequence of the input sequence that matches the pattern with the given replacement string.
   *
   * @param string the string
   * @return the string constructed by replacing each matching subsequence by the replacement string
   */
  String replaceAll(final String string);
}
