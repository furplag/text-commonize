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

import jp.furplag.text.optimize.Optimizr;
import jp.furplag.text.regex.Regexr;
import jp.furplag.text.regex.RegexrOrigin;

/**
 * normalize the string for using under standard input text .
 *
 * <ul>
 * <li>optimize text using {@link Optimizr#optimize(String)}.</li>
 * <li>similar hyphens normalize to hyphen.</li>
 * <li>hyphen normalize.</li>
 * <li>CJK half width character replace to full width mostly ( Hangul, Katakana, Hiragana ) .</li>
 * <li>CJK full width character replace to Latin or single byte character, if those are convertible.</li>
 * <li>Combining-Voiced-Soundmark replace to Full-Width-Voiced-Soundmark.</li>
 * </ul>
 *
 * @author furplag
 *
 */
public final class CjkNormalizr {

  /**
   * Optimizr instances should NOT be constructed in standard programming.
   */
  private CjkNormalizr() {}

  /**
   * returns normalized string for using under standard input text .
   *
   * <ul>
   * <li>optimize text using {@link Optimizr#optimize(String)}.</li>
   * <li>similar hyphens normalize to hyphen.</li>
   * <li>hyphen normalize.</li>
   * <li>CJK half width character replace to full width mostly ( Hangul, Katakana, Hiragana ) .</li>
   * <li>CJK full width character replace to Latin or single byte character, if those are convertible.</li>
   * <li>Combining-Voiced-Soundmark replace to Full-Width-Voiced-Soundmark.</li>
   * </ul>
   *
   * @param string the string, maybe null
   * @return optimized text
   */
  public static String normalize(final String string) {
    return RegexrOrigin.isEmpty(string) ? string :
      Optimizr.optimize(RegexrOrigin.replaceAll(string, Regexr.CjkNormalizr));
  }

  /**
   * detects whether the specified string is normalized.
   *
   * @param string the string, maybe null
   * @return true if the specified string is normalized
   */
  public static boolean isNormalized(final String string) {
    return RegexrOrigin.isEmpty(string) || (Optimizr.isOptimized(string) && string.equals(normalize(string)));
  }
}
