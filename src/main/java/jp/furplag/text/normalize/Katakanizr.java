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

/**
 * Hiragana convert to Katakana .
 *
 * @author furplag
 *
 */
public final class Katakanizr {

  /** codePoint mapping. */
  private static final int transformer;

  /** character codePoint(s) except from replacing . */
  private static final Set<Integer> exclusions;

  static {
    transformer = "ア".codePointAt(0) - "あ".codePointAt(0);
    exclusions = Arrays.asList(12352, 12439, 12440, 12441, 12442, 12443, 12444, 12447).stream().map(Integer::valueOf).collect(Collectors.toSet());
  }


  private Katakanizr() {}

  /**
   * Hiragana convert to Katakana .
   *
   * @param string the String, maybe null
   * @return Katakanized text
   */
  public static String katakanize(final String string) {
      // @formatter:off
      return RegexrOrigin.isEmpty(string) ? string :
        Optional.ofNullable(CjkNormalizr.normalize(string)).orElse("")
          .codePoints()
          .map(Katakanizr::transform)
          .mapToObj(RegexrOrigin::newString)
          .collect(Collectors.joining())
        ;
      // @formatter:on
  }

  private static int transform(final int codePoint) {
    return codePoint + (!exclusions.contains(codePoint) && UnicodeBlock.HIRAGANA.equals(UnicodeBlock.of(codePoint)) ? transformer : 0);
  }
}
