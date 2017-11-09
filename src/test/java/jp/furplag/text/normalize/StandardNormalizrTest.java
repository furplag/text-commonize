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
import java.text.Normalizer;
import java.util.Arrays;
import java.util.Set;
import java.util.function.IntPredicate;
import java.util.stream.IntStream;

import org.junit.Test;

import jp.furplag.text.optimize.Optimizr;
import jp.furplag.text.regex.Regexr;
import jp.furplag.text.regex.RegexrOrigin;

public class StandardNormalizrTest {

  @Test
  public void test() {
    class None extends StandardNormalizr {
      None () {
        super(null, null, null);
      }
      @Override
      protected int transform(int codePoint) {
        return codePoint;
      }
    };
    class Embedded extends StandardNormalizr {
      Embedded(Regexr[] preNormalizr, Regexr[] postNormalizr, Set<Integer> exclusions) {
        super(preNormalizr, postNormalizr, exclusions);
      }

      @Override
      protected int transform(int codePoint) {
        return codePoint;
      }
    };
    class LowPriority extends StandardNormalizr {
      LowPriority() {
        super(IntStream.rangeClosed(0, 128).toArray());
      }
      @Override
      protected int transform(int codePoint) {
        return codePoint;
      }
      @Override public int order() {return Integer.MAX_VALUE;}
    };

    assertThat(new None(), is(new Embedded(null, null, null)));
    assertThat(new None(), is(new Embedded(new Regexr[]{}, new Regexr[]{}, null)));
    assertThat(Arrays.asList(new LowPriority(), new None()).stream().sorted().toArray(Normalizr[]::new), is(new Normalizr[]{new None(), new LowPriority()}));
  }

  @Test
  public void testNormalize() {
    class Embedded extends StandardNormalizr {
      Embedded () {
        super(null, null, null);
      }
      protected Embedded(Regexr[] preNormalizr, Regexr[] postNormalizr, Set<Integer> exclusions) {
        super(preNormalizr, postNormalizr, exclusions);
      }

      @Override
      protected int transform(int codePoint) {
        return codePoint;
      }
    };

    assertThat(new Embedded().normalize(null), is((String) null));
    assertThat(new Embedded().normalize(""), is(""));
    IntStream.range(0, 20_0000)
    .filter(i -> UnicodeBlock.of(i) != null)
    .filter(((IntPredicate)Character::isISOControl).negate())
    .mapToObj(RegexrOrigin::newString)
    .forEach(s -> {
      // @formatter:off
      String expected =
        Normalizer.normalize(s.replace("～", "@～@"), Normalizer.Form.NFKC)
        .replaceAll("[\\x{2010}-\\x{2012}]", "\u002D")
        .replaceAll("@~@", "～")
        .replaceAll("\\x{0020}?[\\x{3099}\\x{309B}\\x{FF9E}]", "\u309B")
        .replaceAll("\\x{0020}?[\\x{309A}\\x{309C}\\x{FF9F}]", "\u309C")
      ;
      assertThat(new Embedded().normalize(s).trim(), is(Optimizr.optimize(expected)));
      // @formatter:on
    });

    assertThat(new Embedded().normalize("Hello World."), is("Hello World."));
    assertThat(new Embedded().normalize("こんにちは　世界"), is("こんにちは 世界"));
    assertThat(new Embedded().normalize("ｺﾝﾆﾁﾊ　世界"), is("コンニチハ 世界"));
    assertThat(new Embedded().normalize("コンニチハ　世界"), is("コンニチハ 世界"));
    assertThat(new Embedded().normalize("バーバパパ"), is("バーバパパ"));
    assertThat(new Embedded().normalize("ﾊﾞｰﾊﾞﾊﾟﾊﾟ"), is("バーバパパ"));
    assertThat(new Embedded().normalize("ハ゛ーハ゛ハ゜ハ゜"), is("バーバパパ"));
    assertThat(new Embedded().normalize("ヷヸヴヹヺ"), is("ヷヸヴヹヺ"));
    assertThat(new Embedded().normalize("ワ゛ヰ゛ウ゛ヱ゛ヲ゛"), is("ヷヸヴヹヺ"));
    assertThat(new Embedded().normalize("ヷヸヴヹヺ"), is("ヷヸヴヹヺ"));
    assertThat(new Embedded().normalize("あ゜い゜う゜え゜お゜な゛に゛ぬ゛ね゛の゛"), is("あ゜い゜う゜え゜お゜な゛に゛ぬ゛ね゛の゛"));
    assertThat(new Embedded().normalize("あ゚い゚う゚え゚お゚な゙に゙ぬ゙ね゙の゙"), is("あ゜い゜う゜え゜お゜な゛に゛ぬ゛ね゛の゛"));
    assertThat(new Embedded().normalize("パ～やん"), is("パ～やん"));
  }
}
