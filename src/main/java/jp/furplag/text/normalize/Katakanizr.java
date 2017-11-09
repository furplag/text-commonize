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

/**
 * Hiragana convert to Katakana .
 *
 * @author furplag
 *
 */
public final class Katakanizr extends StandardNormalizr {

  /** codePoint mapping. */
  private static final int transformer;
  static {
    transformer = "ア".codePointAt(0) - "あ".codePointAt(0);
  }

  public Katakanizr() {
    // @formatter:off
    super(12352, 12439, 12440, 12441, 12442, 12443, 12444, 12447);
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
    return codePoint + (!exclusions.contains(codePoint) && UnicodeBlock.HIRAGANA.equals(UnicodeBlock.of(codePoint)) ? transformer : 0);
  }

  /**
   * Hiragana convert to Katakana .
   *
   * @param string the String, maybe null
   * @return Katakanized text
   */
  public String katakanize(String string) {
    return normalize(string);
  }
}
