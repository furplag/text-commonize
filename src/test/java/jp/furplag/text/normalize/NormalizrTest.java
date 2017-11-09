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

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;

import java.lang.Character.UnicodeBlock;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.text.Normalizer;
import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import org.junit.Test;

import jp.furplag.text.regex.Regexr;
import jp.furplag.text.regex.RegexrOrigin;
import jp.furplag.util.reflect.SavageReflection;

public class NormalizrTest {

  @Test
  public void testNormalizr() throws IllegalArgumentException, IllegalAccessException, NoSuchFieldException, SecurityException {
    class Embedded extends Normalizr {

      Embedded() {
        super(null, null, null);
      }

      Embedded(Regexr[] preNormalizr, Regexr[] postNormalizr, Set<Integer> exclusions) {
        super(preNormalizr, postNormalizr, exclusions);
      }

      @Override
      protected String normalization(String string) {
        return string;
      }

      @Override
      protected int order() {
        return 0;
      }

      @Override
      protected int transform(int codePoint) {
        return codePoint;
      }

    }
    Normalizr normalizr = new Normalizr(null, null, null) {
      @Override
      protected int transform(int codePoint) {
        return codePoint;
      }

      @Override
      protected int order() {
        return 0;
      }

      @Override
      protected String normalization(String string) {
        return string;
      }
    };
    assertThat(normalizr.hashCode(), is(new Embedded().hashCode()));
    assertThat(normalizr.toString(), is(new Embedded().toString()));
    assertThat(normalizr.toString(), is(new Embedded(null, null, null).toString()));
    assertThat(normalizr.toString(), is(new Embedded(new Regexr[] {}, new Regexr[] {}, Collections.emptySet()).toString()));
    assertThat(normalizr.toString(), is(new Embedded(new Regexr[] {null, null}, new Regexr[] {null, null}, Collections.unmodifiableSet(Arrays.asList(-1, null).stream().collect(Collectors.toSet()))).toString()));
    assertThat(normalizr.equals(normalizr), is(true));
    assertThat(new Embedded().equals(new Embedded()), is(true));
    assertThat(normalizr.equals(new Embedded()), is(true));
    assertThat(normalizr.equals(new Embedded(null, null, null)), is(true));
    assertThat(new Embedded(new Regexr[] {}, new Regexr[] {}, new LinkedHashSet<>()).equals(new Embedded(null, null, null)), is(true));
    assertThat(new Embedded(new Regexr[] {}, new Regexr[] {}, Collections.emptySet()).equals(new Embedded(null, null, null)), is(true));
    assertThat(new Embedded(new Regexr[] {null, null}, new Regexr[] {null, null}, Collections.unmodifiableSet(Arrays.asList(-1, null).stream().collect(Collectors.toSet()))).equals(new Embedded(null, null, null)), is(true));
    assertThat(new Embedded(new Regexr[] {null, null}, new Regexr[] {null, null}, Collections.unmodifiableSet(Arrays.asList(1, 2, 3).stream().collect(Collectors.toSet()))).equals(null), is(false));
    assertThat(new Embedded(new Regexr[] {null, null}, new Regexr[] {null, null}, Collections.unmodifiableSet(Arrays.asList(1, 2, 3).stream().collect(Collectors.toSet()))).equals((String) null), is(false));
    assertThat(new Embedded(new Regexr[] {null, null}, new Regexr[] {null, null}, Collections.unmodifiableSet(Arrays.asList(1, 2, 3).stream().collect(Collectors.toSet()))).equals("Normalizr"), is(false));
    assertThat(new Embedded(new Regexr[] {null, null}, new Regexr[] {null, null}, Collections.unmodifiableSet(Arrays.asList(1, 2, 3).stream().collect(Collectors.toSet()))).equals("{preNormalizr: null, postNormalizr: null, exclusions: null}"), is(false));
    assertThat(new Embedded(new Regexr[] {null, null}, new Regexr[] {null, null}, Collections.unmodifiableSet(Arrays.asList(1, 2, 3).stream().collect(Collectors.toSet()))).equals((Normalizr) null), is(false));
    assertThat(new Embedded(new Regexr[] {null, null}, new Regexr[] {null, null}, Collections.unmodifiableSet(Arrays.asList(1, 2, 3).stream().collect(Collectors.toSet()))).equals((Embedded) null), is(false));
    assertThat(new Embedded(new Regexr[] {null, null}, new Regexr[] {null, null}, Collections.unmodifiableSet(Arrays.asList(1, 2, 3).stream().collect(Collectors.toSet()))).equals(new Embedded(null, null, null)), is(false));

    SavageReflection.set(normalizr, Normalizr.class.getDeclaredField("preNormalizr"), null);
    SavageReflection.set(normalizr, Normalizr.class.getDeclaredField("postNormalizr"), null);
    SavageReflection.set(normalizr, Normalizr.class.getDeclaredField("exclusions"), null);

    assertThat(normalizr.hashCode(), is((int) Math.pow(31, 3)));
    assertThat(normalizr.toString(), is("{preNormalizr: null, postNormalizr: null, exclusions: null}"));

    Normalizr one = new Embedded() {
      @Override
      public int order() {
        return 1;
      }
    };
    Normalizr two = new Embedded() {
      @Override
      public int order() {
        return 2;
      }
    };
    Normalizr three = new Embedded() {
      @Override
      public int order() {
        return 3;
      }
    };
    assertThat(new Normalizr[] {new Embedded(), one, two, three}, is(new Normalizr[] {new Embedded(), one, two, three}));
    assertThat(Arrays.asList(new Embedded(), one, two, three).stream().sorted().toArray(Normalizr[]::new), is(new Normalizr[] {new Embedded(), one, two, three}));
    assertThat(Arrays.asList(three, two, one, new Embedded()).stream().sorted().toArray(Normalizr[]::new), is(new Normalizr[] {new Embedded(), one, two, three}));
    assertThat(Arrays.asList(one, new Embedded(), three, two).stream().sorted().toArray(Normalizr[]::new), is(new Normalizr[] {new Embedded(), one, two, three}));
    assertThat(new Embedded().compareTo(null), is(1));
    assertThat(new Embedded().compareTo(new Embedded()), is(0));
    assertThat(new Embedded().compareTo(one), is(-1));
  }

  @Test
  public void testNormalizeDefault() throws NoSuchMethodException, SecurityException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
    Method normalizeDefault = Normalizr.class.getDeclaredMethod("normalizeDefault", String.class, Normalizer.Form.class);
    normalizeDefault.setAccessible(true);
    assertThat(normalizeDefault.invoke(null, (String) null, (Normalizer.Form) null), is((String) null));
    assertThat(normalizeDefault.invoke(null, "failure", (Normalizer.Form) null), is("failure"));
  }

  @Test
  public void testNfc() {
    assertNull(Normalizr.nfc(null));
    assertThat(Normalizr.nfc(""), is(""));
    IntStream.range(0, 20_0000).filter(i -> UnicodeBlock.of(i) != null).mapToObj(RegexrOrigin::newString).forEach(s -> assertThat(Normalizr.nfc(s), is(Normalizer.normalize(s, Normalizer.Form.NFC))));

    assertThat(Normalizr.nfc(null), is((String) null));
    assertThat(Normalizr.nfc(""), is(""));
    assertThat(Normalizr.nfd("バーバパパ"), is("バーバパパ"));
    assertThat(Normalizr.nfc("ハ゛ーハ゛ハ゜ハ゜"), is("ハ゛ーハ゛ハ゜ハ゜"));
    assertThat(Normalizr.nfc("ワ゛ヰ゛ウ゛ヱ゛ヲ゛"), is("ワ゛ヰ゛ウ゛ヱ゛ヲ゛"));
    assertThat(Normalizr.nfc("ヷヸヴヹヺ"), is("ヷヸヴヹヺ"));
    assertThat(Normalizr.nfc("あ゜い゜う゜え゜お゜な゛に゛ぬ゛ね゛の゛"), is("あ゜い゜う゜え゜お゜な゛に゛ぬ゛ね゛の゛"));
    assertThat(Normalizr.nfc("あ゚い゚う゚え゚お゚な゙に゙ぬ゙ね゙の゙"), is("あ゚い゚う゚え゚お゚な゙に゙ぬ゙ね゙の゙"));
  }

  @Test
  public void testNfd() {
    assertNull(Normalizr.nfd(null));
    assertThat(Normalizr.nfd(""), is(""));
    // @formatter:off
    IntStream.range(0, 20_0000)
    .filter(i -> UnicodeBlock.of(i) != null)
    .mapToObj(RegexrOrigin::newString)
    .forEach(s -> assertThat(Normalizr.nfd(s), is(Normalizer.normalize(s, Normalizer.Form.NFD))));

    assertThat(Normalizr.nfd(null), is((String) null));
    assertThat(Normalizr.nfd(""), is(""));
    assertThat(Normalizr.nfd("バーバパパ"), is("バーバパパ"));
    assertThat(Normalizr.nfd("ハ゛ーハ゛ハ゜ハ゜"), is("ハ゛ーハ゛ハ゜ハ゜"));
    assertThat(Normalizr.nfd("ワ゛ヰ゛ウ゛ヱ゛ヲ゛"), is("ワ゛ヰ゛ウ゛ヱ゛ヲ゛"));
    assertThat(Normalizr.nfd("ヷヸヴヹヺ"), is("ヷヸヴヹヺ"));
    assertThat(Normalizr.nfd("あ゜い゜う゜え゜お゜な゛に゛ぬ゛ね゛の゛"), is("あ゜い゜う゜え゜お゜な゛に゛ぬ゛ね゛の゛"));
    assertThat(Normalizr.nfd("あ゚い゚う゚え゚お゚な゙に゙ぬ゙ね゙の゙"), is("あ゚い゚う゚え゚お゚な゙に゙ぬ゙ね゙の゙"));
  }

  @Test
  public void testNfkc() {
    assertNull(Normalizr.nfkc(null));
    assertThat(Normalizr.nfkc(""), is(""));
    IntStream.range(0, 20_0000)
    .filter(i -> UnicodeBlock.of(i) != null)
    .mapToObj(RegexrOrigin::newString)
    .forEach(s -> {
      // @formatter:off
      String expected =
        Normalizer.normalize(s, Normalizer.Form.NFKC)
      ;
      assertThat(Normalizr.nfkc(s), is(expected));
      // @formatter:on
    });

    assertThat(Normalizr.nfkc(null), is((String) null));
    assertThat(Normalizr.nfkc(""), is(""));
    assertThat(Normalizr.nfkc("バーバパパ"), is("バーバパパ"));
    assertThat(Normalizr.nfkc("ハ゛ーハ゛ハ゜ハ゜"), is("ハ ゙ーハ ゙ハ ゚ハ ゚"));
    assertThat(Normalizr.nfkc("ヷヸヴヹヺ"), is("ヷヸヴヹヺ"));
    assertThat(Normalizr.nfkc("ワ゛ヰ゛ウ゛ヱ゛ヲ゛"), is("ワ ゙ヰ ゙ウ ゙ヱ ゙ヲ ゙"));
    assertThat(Normalizr.nfkc("ヷヸヴヹヺ"), is("ヷヸヴヹヺ"));
    assertThat(Normalizr.nfkc("あ゜い゜う゜え゜お゜な゛に゛ぬ゛ね゛の゛"), is("あ ゚い ゚う ゚え ゚お ゚な ゙に ゙ぬ ゙ね ゙の ゙"));
    assertThat(Normalizr.nfkc("あ゚い゚う゚え゚お゚な゙に゙ぬ゙ね゙の゙"), is("あ゚い゚う゚え゚お゚な゙に゙ぬ゙ね゙の゙"));
  }

  @Test
  public void testNkfd() {
    assertNull(Normalizr.nfkd(null));
    assertThat(Normalizr.nfkd(""), is(""));
    IntStream.range(0, 20_0000).filter(i -> UnicodeBlock.of(i) != null).mapToObj(RegexrOrigin::newString).forEach(s -> {
      // @formatter:off
      String expected =
        Normalizer.normalize(s, Normalizer.Form.NFKD)
      ;
      assertThat(Normalizr.nfkd(s), is(expected));
      // @formatter:on
    });

    assertThat(Normalizr.nfkd(null), is((String) null));
    assertThat(Normalizr.nfkd(""), is(""));
    assertThat(Normalizr.nfkd("バーバパパ"), is("バーバパパ"));
    assertThat(Normalizr.nfkd("ハ゛ーハ゛ハ゜ハ゜"), is("ハ ゙ーハ ゙ハ ゚ハ ゚"));
    assertThat(Normalizr.nfkd("ヷヸヴヹヺ"), is("ヷヸヴヹヺ"));
    assertThat(Normalizr.nfkd("ワ゛ヰ゛ウ゛ヱ゛ヲ゛"), is("ワ ゙ヰ ゙ウ ゙ヱ ゙ヲ ゙"));
    assertThat(Normalizr.nfkd("ヷヸヴヹヺ"), is("ヷヸヴヹヺ"));
    assertThat(Normalizr.nfkd("あ゜い゜う゜え゜お゜な゛に゛ぬ゛ね゛の゛"), is("あ ゚い ゚う ゚え ゚お ゚な ゙に ゙ぬ ゙ね ゙の ゙"));
    assertThat(Normalizr.nfkd("あ゚い゚う゚え゚お゚な゙に゙ぬ゙ね゙の゙"), is("あ゚い゚う゚え゚お゚な゙に゙ぬ゙ね゙の゙"));
  }

  @Test
  public void testNormalization() {
    class Embedded extends Normalizr {

      Embedded() {
        super(null, null, null);
      }

      Embedded(Regexr[] preNormalizr, Regexr[] postNormalizr, Set<Integer> exclusions) {
        super(preNormalizr, postNormalizr, exclusions);
      }

      @Override
      protected int transform(int codePoint) {
        return codePoint;
      }

      @Override
      protected int order() {
        return 0;
      }

      @Override
      protected String normalization(String string) {
        return string;
      }
    };

    assertThat(new Embedded().normalization(null), is((String) null));
    assertThat(new Embedded().normalization(""), is(""));
    assertThat(new Embedded().normalization(" "), is(" "));
    assertThat(new Embedded().normalization("バーバパパ"), is("バーバパパ"));
    assertThat(new Embedded() {
      @Override
      protected String normalization(String string) {
      return nfc(string);
    }}.normalization("バーバパパ"), is("バーバパパ"));
    assertThat(new Embedded() {
      @Override
      protected String normalization(String string) {
      return nfd(string);
    }}.normalization("バーバパパ"), is("バーバパパ"));
    assertThat(new Embedded() {
      @Override
      protected String normalization(String string) {
      return nfkc(string);
    }}.normalization("ハ゛ーハ゛ハ゜ハ゜"), is("ハ ゙ーハ ゙ハ ゚ハ ゚"));
  }

  @Test
  public void testNormalize() {
    class Embedded extends Normalizr {

      Embedded() {
        super(null, null, null);
      }

      Embedded(Regexr[] preNormalizr, Regexr[] postNormalizr, Set<Integer> exclusions) {
        super(preNormalizr, postNormalizr, exclusions);
      }

      @Override
      protected int transform(int codePoint) {
        return codePoint;
      }

      @Override
      protected int order() {
        return 0;
      }

      @Override
      protected String normalization(String string) {
        return string;
      }
    };
    assertThat(new Embedded() {
      @Override
      protected String normalization(String string) {
      return nfkc(string);
    }}.normalize("ハ゛ーハ゛ハ゜ハ゜"), is("ハ ゙ーハ ゙ハ ゚ハ ゚"));

    assertThat(new Embedded(new Regexr[]{Regexr.Trimr}, null, null) {
      @Override
      protected String normalization(String string) {
      return nfkc(string);
    }}.normalize(null), is((String) null));
    assertThat(new Embedded(new Regexr[]{Regexr.Trimr}, null, null) {
      @Override
      protected String normalization(String string) {
      return nfkc(string);
    }}.normalize(""), is(""));
    assertThat(new Embedded(new Regexr[]{Regexr.Trimr}, null, null) {
      @Override
      protected String normalization(String string) {
      return nfkc(string);
    }}.normalize("ハ゛ーハ゛ハ゜ハ゜"), is("ハ ゙ーハ ゙ハ ゚ハ ゚"));
    assertThat(new Embedded(new Regexr[]{Regexr.Trimr}, null, null) {
      @Override
      protected String normalization(String string) {
      return nfkc(string);
    }}.normalize("  ハ゛ーハ゛ハ゜ハ゜  "), is("ハ ゙ーハ ゙ハ ゚ハ ゚"));
  }
}
