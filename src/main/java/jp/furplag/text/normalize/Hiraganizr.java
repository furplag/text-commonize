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
import java.util.stream.Collectors;

import jp.furplag.text.regex.Regexr;
import jp.furplag.text.regex.RegexrStandard;

/**
 * Katakana convert to Hiragana .
 *
 * @author furplag
 *
 */
public final class Hiraganizr extends StandardNormalizr {

  private static final int transformer;
  static {
    transformer = "あ".codePointAt(0) - "ア".codePointAt(0);
  }

  /** codePoint mapping. */
  public Hiraganizr() {
    // @formatter:off
    super(
      null
    , new Regexr[]{
        new RegexrStandard("\\x{30F7}", "\u308F\u309B", 100)
      , new RegexrStandard("\\x{30F8}", "\u3090\u309B", 100)
      , new RegexrStandard("\\x{30F9}", "\u3091\u309B", 100)
      , new RegexrStandard("\\x{30FA}", "\u3092\u309B", 100)
      }
    , Arrays.asList(12448, 12535, 12536, 12537, 12538, 12539, 12540, 12543).stream().map(Integer::valueOf).collect(Collectors.toSet()));
    // @formatter:on
 }

  /**
   * {@inheritDoc}
   */
  @Override
  protected String normalization(String string) {
    return nfkc(string);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  protected int order() {
    return 10;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  protected int transform(int codePoint) {
    return codePoint + (!exclusions.contains(codePoint) && UnicodeBlock.KATAKANA.equals(UnicodeBlock.of(codePoint)) ? transformer : 0);
  }

  /**
   * Katakana convert to Hiragana .
   *
   * @param string the String, maybe null
   * @return Hiraganized text
   */
  public String hiraganize(String string) {
    return normalize(string);
  }
}
