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

import java.io.Serializable;
import java.text.Normalizer;
import java.util.Arrays;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.StringJoiner;
import java.util.stream.Collectors;

import jp.furplag.text.regex.Regexr;
import jp.furplag.text.regex.RegexrOrigin;

/**
 * text normalization .
 *
 * @author furplag
 *
 */
public abstract class Normalizr implements Serializable, Comparable<Normalizr> {

  protected final Regexr[] preNormalizr;

  protected final Regexr[] postNormalizr;

  protected final Set<Integer> exclusions;

  protected Normalizr(Regexr[] preNormalizr, Regexr[] postNormalizr, Set<Integer> exclusions) {
    this.preNormalizr = Arrays.stream(Optional.ofNullable(preNormalizr).orElse(new Regexr[]{})).filter(Objects::nonNull).toArray(Regexr[]::new);
    this.postNormalizr = Arrays.stream(Optional.ofNullable(postNormalizr).orElse(new Regexr[]{})).filter(Objects::nonNull).toArray(Regexr[]::new);
    this.exclusions = Optional.ofNullable(exclusions).orElse(new HashSet<>()).stream().filter(Objects::nonNull).filter(Character::isValidCodePoint).sorted().collect(Collectors.toCollection(LinkedHashSet::new));
  }

  /**
   * {@link Normalizer#normalize(CharSequence, java.text.Normalizer.Form) Normalizer#normalize}
   * against null .
   *
   * @param string the String, maybe null
   * @return normalized text using Normalization Form C
   * @see {@link Normalizer.Form#NFC}
   */
  protected static String nfc(final String string) {
    return normalizeDefault(string, Normalizer.Form.NFC);
  }

  /**
   * {@link Normalizer#normalize(CharSequence, java.text.Normalizer.Form) Normalizer#normalize}
   * against null .
   *
   * @param string the String, maybe null
   * @return normalized text using Normalization Form D
   * @see {@link Normalizer.Form#NFD}
   */
  protected static String nfd(final String string) {
    return normalizeDefault(string, Normalizer.Form.NFD);
  }

  /**
   * {@link Normalizer#normalize(CharSequence, java.text.Normalizer.Form) Normalizer#normalize}
   * against null .
   *
   * @param string the String, maybe null
   * @return normalized text using Normalization Form KC
   * @see {@link Normalizer.Form#NFKC}
   */
  protected static String nfkc(final String string) {
    // @formatter:off
    return normalizeDefault(string, Normalizer.Form.NFKC);
    // @formatter:on
  }

  /**
   * {@link Normalizer#normalize(CharSequence, java.text.Normalizer.Form) Normalizer#normalize}
   * against null .
   *
   * @param string the String, maybe null
   * @return normalized text using Normalization Form KD
   * @see {@link Normalizer.Form#NFKD}
   */
  protected static String nfkd(final String string) {
    return normalizeDefault(string, Normalizer.Form.NFKD);
  }

  /**
   * {@link Normalizer#normalize(CharSequence, java.text.Normalizer.Form) Normalizer#normalize}
   * against null .
   *
   * @param string the String, maybe null
   * @param form The normalization form . {@link java.text.Normalizer.Form#NFC NFC},
   *        {@link java.text.Normalizer.Form#NFD NFD}, {@link java.text.Normalizer.Form#NFKC NFKC},
   *        {@link java.text.Normalizer.Form#NFKD NFKD}
   * @return normalized text using Normalization Form .
   */
  private static String normalizeDefault(final String string, final Normalizer.Form form) {
    return RegexrOrigin.isEmpty(string) || form == null ? string : Normalizer.normalize(string, form);
  }

  @Override
  public int compareTo(Normalizr o) {
    return o == null ? 1 : Integer.valueOf(this.order()).compareTo(o.order());
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public boolean equals(Object obj) {
    return this == obj || (obj instanceof Normalizr && this.toString().equals(((Normalizr) obj).toString()));
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + ((preNormalizr == null) ? 0 : Arrays.hashCode(preNormalizr));
    result = prime * result + ((postNormalizr == null) ? 0 : Arrays.hashCode(postNormalizr));
    result = prime * result + ((exclusions == null) ? 0 : Arrays.hashCode(exclusions.toArray(new Integer[]{})));

    return result;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public String toString() {
    // @formatter:off
    return new StringJoiner(", ", "{", "}")
      .add(String.format("preNormalizr: %s", Arrays.toString(preNormalizr)))
      .add(String.format("postNormalizr: %s", Arrays.toString(postNormalizr)))
      .add(String.format("exclusions: %s", exclusions == null ? null : Arrays.toString(exclusions.toArray(new Integer[]{}))))
      .toString();
    // @formatter:on
  }

  /**
   * {@link Normalizer#normalize(CharSequence, java.text.Normalizer.Form) Normalizer#normalize}
   * against null .
   *
   * @param string the String, maybe null
   * @return normalized text using Normalization Form .
   */
  protected abstract String normalization(String string);

  /**
   * text normalization for using under standard input text .
   *
   * @param string the String, maybe null
   * @return normalized text
   */
  protected String normalize(String string) {
    // @formatter:off
    return RegexrOrigin.isEmpty(string) ? string :
      postNormalization(
        Optional.ofNullable(normalization(preNormalization(string))).orElse("")
          .codePoints()
          .map(this::transform)
          .mapToObj(RegexrOrigin::newString)
          .collect(Collectors.joining())
      );
    // @formatter:on
  }

  /** return {@code this.order}. */
  protected abstract int order();

  /**
   * execute {@link RegexrOrigin#replaceAll(String, Regexr...)} after normalization .
   *
   * @param string the String, maybe null
   * @return the string constructed by replacing each matching subsequence by the replacement string
   */
  protected String postNormalization(String string) {
    return RegexrOrigin.replaceAll(string, postNormalizr);
  }

  /**
   * execute {@link RegexrOrigin#replaceAll(String, Regexr...)} before normalization .
   *
   * @param string the String, maybe null
   * @return the string constructed by replacing each matching subsequence by the replacement string
   */
  protected String preNormalization(String string) {
    return RegexrOrigin.replaceAll(string, preNormalizr);
  }

  /**
   * replace ( or stay ) {@code codePoint} for text normalization .
   *
   * @param codePoint Unicode code point
   * @return replaced ( or stay ) Unicode code point
   */
  protected abstract int transform(int codePoint);
}
