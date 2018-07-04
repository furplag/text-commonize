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
import java.lang.reflect.Constructor;
import java.util.Arrays;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import org.junit.Test;

import jp.furplag.sandbox.reflect.SavageReflection;
import jp.furplag.text.optimize.Optimizr;
import jp.furplag.text.regex.RegexrOrigin;

public class CjkNormalizrTest {

  @Test
  public void test() throws SecurityException, ReflectiveOperationException {
    Constructor<?> c = CjkNormalizr.class.getDeclaredConstructor();
    c.setAccessible(true);
    assertThat(c.newInstance() instanceof CjkNormalizr, is(true));
  }

  @Test
  public void testNormalize() {
    assertThat(CjkNormalizr.normalize(null), is((String) null));
    assertThat(CjkNormalizr.normalize(""), is(""));
    assertThat(CjkNormalizr.normalize("Hello World."), is("Hello World."));
    assertThat(CjkNormalizr.normalize("Ｈｅｌｌｏ　Ｗｏｒｌｄ．"), is("Hello World."));
    assertThat(CjkNormalizr.normalize("こんにちは　世界"), is("こんにちは 世界"));
    assertThat(CjkNormalizr.normalize("ｺﾝﾆﾁﾊ　世界"), is("コンニチハ 世界"));
    assertThat(CjkNormalizr.normalize("コンニチハ　世界"), is("コンニチハ 世界"));
    assertThat(CjkNormalizr.normalize("バーバパパ"), is("バーバパパ"));
    assertThat(CjkNormalizr.normalize("ﾊﾞｰﾊﾞﾊﾟﾊﾟ"), is("バーバパパ"));
    assertThat(CjkNormalizr.normalize("ハ゛ーハ゛ハ゜ハ゜"), is("バーバパパ"));
    assertThat(CjkNormalizr.normalize("ヷヸヴヹヺ"), is("ヷヸヴヹヺ"));
    assertThat(CjkNormalizr.normalize("ワ゛ヰ゛ウ゛ヱ゛ヲ゛"), is("ヷヸヴヹヺ"));
    assertThat(CjkNormalizr.normalize("ヷヸヴヹヺ"), is("ヷヸヴヹヺ"));
    assertThat(CjkNormalizr.normalize("ｱﾟｲﾟｳﾟｴﾟｵﾟﾅﾞﾆﾞﾇﾞﾈﾞﾉﾞ"), is("ア゜イ゜ウ゜エ゜オ゜ナ゛ニ゛ヌ゛ネ゛ノ゛"));
    assertThat(CjkNormalizr.normalize("あ゜い゜う゜え゜お゜な゛に゛ぬ゛ね゛の゛"), is("あ゜い゜う゜え゜お゜な゛に゛ぬ゛ね゛の゛"));
    assertThat(CjkNormalizr.normalize("あ゚い゚う゚え゚お゚な゙に゙ぬ゙ね゙の゙"), is("あ゜い゜う゜え゜お゜な゛に゛ぬ゛ね゛の゛"));
    assertThat(CjkNormalizr.normalize("パ～やん"), is("パ～やん"));
  }

  @Test
  @SuppressWarnings("unchecked")
  public void testDenormalize() throws SecurityException, ReflectiveOperationException {
    assertThat(CjkNormalizr.denormalize(null), is((String) null));
    assertThat(CjkNormalizr.denormalize(""), is(""));
    assertThat(CjkNormalizr.denormalize("   \r\n   \r\n   \r\n"), is(""));
    assertThat(CjkNormalizr.denormalize("諸行無常"), is("諸行無常"));
    assertThat(CjkNormalizr.denormalize("南\t無\t阿\t弥\t陀\t仏"), is("南 無 阿 弥 陀 仏"));
    assertThat(CjkNormalizr.denormalize("Wanderlei Silva"), is("Ｗａｎｄｅｒｌｅｉ Ｓｉｌｖａ"));
    assertThat(CjkNormalizr.denormalize("Ｗａｎｄｅｒｌｅｉ Ｓｉｌｖａ"), is("Ｗａｎｄｅｒｌｅｉ Ｓｉｌｖａ"));

    Map<Integer, Integer> exclusives = (Map<Integer, Integer>) SavageReflection.get(CjkNormalizr.class, "exclusives");
    String latins = RegexrOrigin.newString(IntStream.rangeClosed(0, 0x00FF).toArray());
    String expect = Optimizr.optimize(RegexrOrigin.newString(latins.codePoints().map(codePoint->exclusives.getOrDefault(codePoint, codePoint + (UnicodeBlock.BASIC_LATIN.equals(UnicodeBlock.of(codePoint)) && !Character.isWhitespace(codePoint) && !Character.isISOControl(codePoint) ? 65248 : 0))).toArray()));
    assertThat(CjkNormalizr.denormalize(latins), is(expect));
  }

  @Test
  public void testIsNormalized() {
    assertThat(CjkNormalizr.isNormalized(null), is(true));
    assertThat(CjkNormalizr.isNormalized(""), is(true));
    assertThat(CjkNormalizr.isNormalized("theString."), is(true));
    assertThat(CjkNormalizr.isNormalized("the String."), is(true));
    assertThat(CjkNormalizr.isNormalized("the String ."), is(true));
    assertThat(CjkNormalizr.isNormalized("the String. "), is(false));
    assertThat(CjkNormalizr.isNormalized(" the String."), is(false));
    assertThat(CjkNormalizr.isNormalized(" the String. "), is(false));
    assertThat(CjkNormalizr.isNormalized(Arrays.stream("theString.".split("")).collect(Collectors.joining("\n"))), is(true));
    assertThat(CjkNormalizr.isNormalized(Arrays.stream("theString.".split("")).collect(Collectors.joining("\n\n"))), is(false));
    assertThat(CjkNormalizr.isNormalized("Hello World."), is(true));
    assertThat(CjkNormalizr.isNormalized("Ｈｅｌｌｏ　Ｗｏｒｌｄ．"), is(false));
    assertThat(CjkNormalizr.isNormalized("こんにちは　世界"), is(false));
    assertThat(CjkNormalizr.isNormalized("こんにちは 世界"), is(true));
    assertThat(CjkNormalizr.isNormalized("ｺﾝﾆﾁﾊ　世界"), is(false));
    assertThat(CjkNormalizr.isNormalized("コンニチハ　世界"), is(false));
    assertThat(CjkNormalizr.isNormalized("コンニチハ 世界"), is(true));
    assertThat(CjkNormalizr.isNormalized("バーバパパ"), is(true));
    assertThat(CjkNormalizr.isNormalized("ﾊﾞｰﾊﾞﾊﾟﾊﾟ"), is(false));
    assertThat(CjkNormalizr.isNormalized("ハ゛ーハ゛ハ゜ハ゜"), is(false));
    assertThat(CjkNormalizr.isNormalized("ヷヸヴヹヺ"), is(true));
    assertThat(CjkNormalizr.isNormalized("ワ゛ヰ゛ウ゛ヱ゛ヲ゛"), is(false));
    assertThat(CjkNormalizr.isNormalized("ヷヸヴヹヺ"), is(false));
    assertThat(CjkNormalizr.isNormalized("ｱﾟｲﾟｳﾟｴﾟｵﾟﾅﾞﾆﾞﾇﾞﾈﾞﾉﾞ"), is(false));
    assertThat(CjkNormalizr.isNormalized("あ゜い゜う゜え゜お゜な゛に゛ぬ゛ね゛の゛"), is(true));
    assertThat(CjkNormalizr.isNormalized("あ゚い゚う゚え゚お゚な゙に゙ぬ゙ね゙の゙"), is(false));
    assertThat(CjkNormalizr.isNormalized("パ～やん"), is(true));
    assertThat(CjkNormalizr.isNormalized("パ~やん"), is(true));
  }
}
