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

import java.util.Arrays;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import jp.furplag.text.optimize.Optimizr;
import jp.furplag.text.regex.Regexr;
import jp.furplag.text.regex.RegexrOrigin;
import jp.furplag.text.regex.RegexrStandard;

/**
 * normalize the string for using under standard input text .
 *
 * <ul>
 * <li>similar hyphens normalize to hyphen.</li>
 * <li>hyphen normalize.</li>
 * <li>Combining-Voiced-Soundmark replace to Full-Width-Voiced-Soundmark.</li>
 * </ul>
 *
 * @author furplag
 *
 */
public abstract class StandardNormalizr extends Normalizr {

  protected static final Regexr[] standardPreNormalizr;
  protected static final Regexr[] standardPostNormalizr;
  static {
    standardPreNormalizr = new Regexr[]{
      new RegexrStandard("\\x{0020}?[\\x{3099}\\x{309B}\\x{FF9E}]", "\uFF9E")
    , new RegexrStandard("\\x{0020}?[\\x{309A}\\x{309C}\\x{FF9F}]", "\uFF9F")
    , new RegexrStandard("[\\x{2010}-\\x{2012}]", "\u002D")
    , new RegexrStandard("\uFF5E", "\uFF00\u007E\uFF00")
    };
    standardPostNormalizr = new Regexr[]{
      Regexr.CjkNormalizr
    , new RegexrStandard("\uFF00\u007E\uFF00", "\uFF5E")
    };
  }

  protected StandardNormalizr(int... exclusions) {
    super(standardPreNormalizr, standardPostNormalizr, Arrays.stream(RegexrOrigin.verifyCodePoints(exclusions)).mapToObj(Integer::valueOf).collect(Collectors.toCollection(HashSet::new)));
  }

  protected StandardNormalizr(Regexr[] preNormalizr, Regexr[] postNormalizr, Set<Integer> exclusions) {
    super(merge(standardPreNormalizr, preNormalizr), merge(standardPostNormalizr, postNormalizr), exclusions);
  }

  /**
   * {@inheritDoc}
   *
   * <p>using {@link #nfkc(String) NFKC} and {@link Optimizr#optimize(String) Optimizr.optimize}.</p>
   * @see {@link Optimizr#optimize(String)}
   */
  @Override
  protected String normalization(String string) {
    return nfkc(Optimizr.optimize(string));
  }

  @Override
  protected int order() {
    return 0;
  }

  /**
   * merging array .
   *
   * @param statics an Array of {@link Regexr}
   * @param regexrs an Array of {@link Regexr}
   * @return merged array
   */
  private static Regexr[] merge(Regexr[] statics, Regexr... regexrs) {
    List<Regexr> merged = Arrays.stream(Optional.ofNullable(statics).orElse(new Regexr[]{})).collect(Collectors.toList());
    Arrays.stream(Optional.ofNullable(regexrs).orElse(new Regexr[]{})).filter(Objects::nonNull).forEach(merged::add);

    return merged.stream().sorted().collect(Collectors.toCollection(LinkedHashSet::new)).toArray(new Regexr[]{});
  }
}
