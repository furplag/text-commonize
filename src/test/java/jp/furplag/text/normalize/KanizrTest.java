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

import org.junit.Test;

public class KanizrTest {

  @Test
  public void testHiraganize() {
    assertThat(Kanizr.hiraganize(null), is((String) null));
    assertThat(Kanizr.hiraganize(""), is(""));
    assertThat(Kanizr.hiraganize("Hello World."), is("Hello World."));
    assertThat(Kanizr.hiraganize("こんにちは　世界"), is("こんにちは 世界"));
    assertThat(Kanizr.hiraganize("ｺﾝﾆﾁﾊ　世界"), is("こんにちは 世界"));
    assertThat(Kanizr.hiraganize("コンニチハ　世界"), is("こんにちは 世界"));
    assertThat(Kanizr.hiraganize("バーバパパ"), is("ばーばぱぱ"));
    assertThat(Kanizr.hiraganize("ﾊﾞｰﾊﾞﾊﾟﾊﾟ"), is("ばーばぱぱ"));
    assertThat(Kanizr.hiraganize("ハ゛ーハ゛ハ゜ハ゜"), is("ばーばぱぱ"));
    assertThat(Kanizr.hiraganize("ヷヸヴヹヺ"), is("わ゛ゐ゛ゔゑ゛を゛"));
    assertThat(Kanizr.hiraganize("ワ゛ヰ゛ウ゛ヱ゛ヲ゛"), is("わ゛ゐ゛ゔゑ゛を゛"));
    assertThat(Kanizr.hiraganize("ヷヸヴヹヺ"), is("わ゛ゐ゛ゔゑ゛を゛"));
    assertThat(Kanizr.hiraganize("あ゜い゜う゜え゜お゜な゛に゛ぬ゛ね゛の゛"), is("あ゜い゜う゜え゜お゜な゛に゛ぬ゛ね゛の゛"));
    assertThat(Kanizr.hiraganize("あ゚い゚う゚え゚お゚な゙に゙ぬ゙ね゙の゙"), is("あ゜い゜う゜え゜お゜な゛に゛ぬ゛ね゛の゛"));
    assertThat(Kanizr.hiraganize("ア゜イ゜ウ゜エ゜オ゜ナ゛ニ゛ヌ゛ネ゛ノ゛"), is("あ゜い゜う゜え゜お゜な゛に゛ぬ゛ね゛の゛"));
    assertThat(Kanizr.hiraganize("ア゚イ゚ウ゚エ゚オ゚ナ゙ニ゙ヌ゙ネ゙ノ゙"), is("あ゜い゜う゜え゜お゜な゛に゛ぬ゛ね゛の゛"));
    assertThat(Kanizr.hiraganize("ｱﾟｲﾟｳﾟｴﾟｵﾟﾅﾞﾆﾞﾇﾞﾈﾞﾉﾞ"), is("あ゜い゜う゜え゜お゜な゛に゛ぬ゛ね゛の゛"));
    assertThat(Kanizr.hiraganize("パ～やん"), is("ぱ～やん"));
    assertThat(Kanizr.hiraganize("ｱﾚﾝ･ｷﾞﾝｽﾞﾊﾞｰｸﾞ"), is("あれん・ぎんずばーぐ"));
  }

  @Test
  public void testKatakanize() {
    assertThat(Kanizr.katakanize(null), is((String) null));
    assertThat(Kanizr.katakanize(""), is(""));
    assertThat(Kanizr.katakanize("Hello World."), is("Hello World."));
    assertThat(Kanizr.katakanize("こんにちは　世界"), is("コンニチハ 世界"));
    assertThat(Kanizr.katakanize("ｺﾝﾆﾁﾊ　世界"), is("コンニチハ 世界"));
    assertThat(Kanizr.katakanize("コンニチハ　世界"), is("コンニチハ 世界"));
    assertThat(Kanizr.katakanize("バーバパパ"), is("バーバパパ"));
    assertThat(Kanizr.katakanize("ﾊﾞｰﾊﾞﾊﾟﾊﾟ"), is("バーバパパ"));
    assertThat(Kanizr.katakanize("ハ゛ーハ゛ハ゜ハ゜"), is("バーバパパ"));
    assertThat(Kanizr.katakanize("ヷヸヴヹヺ"), is("ヷヸヴヹヺ"));
    assertThat(Kanizr.katakanize("ワ゛ヰ゛ウ゛ヱ゛ヲ゛"), is("ヷヸヴヹヺ"));
    assertThat(Kanizr.katakanize("ヷヸヴヹヺ"), is("ヷヸヴヹヺ"));
    assertThat(Kanizr.katakanize("あ゜い゜う゜え゜お゜な゛に゛ぬ゛ね゛の゛"), is("ア゜イ゜ウ゜エ゜オ゜ナ゛ニ゛ヌ゛ネ゛ノ゛"));
    assertThat(Kanizr.katakanize("あ゚い゚う゚え゚お゚な゙に゙ぬ゙ね゙の゙"), is("ア゜イ゜ウ゜エ゜オ゜ナ゛ニ゛ヌ゛ネ゛ノ゛"));
    assertThat(Kanizr.katakanize("ア゜イ゜ウ゜エ゜オ゜ナ゛ニ゛ヌ゛ネ゛ノ゛"), is("ア゜イ゜ウ゜エ゜オ゜ナ゛ニ゛ヌ゛ネ゛ノ゛"));
    assertThat(Kanizr.katakanize("ア゚イ゚ウ゚エ゚オ゚ナ゙ニ゙ヌ゙ネ゙ノ゙"), is("ア゜イ゜ウ゜エ゜オ゜ナ゛ニ゛ヌ゛ネ゛ノ゛"));
    assertThat(Kanizr.katakanize("ｱﾟｲﾟｳﾟｴﾟｵﾟﾅﾞﾆﾞﾇﾞﾈﾞﾉﾞ"), is("ア゜イ゜ウ゜エ゜オ゜ナ゛ニ゛ヌ゛ネ゛ノ゛"));
    assertThat(Kanizr.katakanize("パ～やん"), is("パ～ヤン"));
    assertThat(Kanizr.katakanize("ｱﾚﾝ･ｷﾞﾝｽﾞﾊﾞｰｸﾞ"), is("アレン・ギンズバーグ"));
  }

}
