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

package jp.furplag.text.normalize;

import java.lang.Character.UnicodeBlock;
import java.util.Arrays;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import jp.furplag.text.regex.RegexrOrigin;
import jp.furplag.text.regex.RegexrStandard;

/**
 * Katakana convert to Hiragana .
 *
 * @author furplag
 *
 */
public final class Hiraganizr {

  /** codePoint mapping. */
  private static final int transformer;

  /** character codePoint(s) except from replacing . */
  private static final Set<Integer> exclusions;

  static {
    transformer = "あ".codePointAt(0) - "ア".codePointAt(0);
    exclusions = Arrays.asList(12448, 12535, 12536, 12537, 12538, 12539, 12540, 12543).stream().map(Integer::valueOf).collect(Collectors.toSet());
  }

  /** codePoint mapping. */
  private Hiraganizr() {}

  private static int transform(final int codePoint) {
    return codePoint + (!exclusions.contains(codePoint) && UnicodeBlock.KATAKANA.equals(UnicodeBlock.of(codePoint)) ? transformer : 0);
  }

  /**
   * Katakana convert to Hiragana .
   *
   * @param string the String, maybe null
   * @return Hiraganized text
   */
  public static String hiraganize(final String string) {
    // @formatter:off
    return RegexrOrigin.isEmpty(string) ? string :
      RegexrOrigin.replaceAll(
        Optional.ofNullable(CjkNormalizr.normalize(string)).orElse("")
          .codePoints()
          .map(Hiraganizr::transform)
          .mapToObj(RegexrOrigin::newString)
          .collect(Collectors.joining())
      , new RegexrStandard("\\x{30F7}", "\u308F\u309B")
      , new RegexrStandard("\\x{30F8}", "\u3090\u309B")
      , new RegexrStandard("\\x{30F9}", "\u3091\u309B")
      , new RegexrStandard("\\x{30FA}", "\u3092\u309B")
      );
    // @formatter:on
  }
}
