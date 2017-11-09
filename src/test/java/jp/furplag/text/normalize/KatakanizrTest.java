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

public class KatakanizrTest {

  @Test
  public void test() {
    assertThat(new Katakanizr().order(), is(10));
    assertThat(new Katakanizr().katakanize(null), is((String) null));
    assertThat(new Katakanizr().katakanize(""), is(""));
    assertThat(new Katakanizr().katakanize("Hello World."), is("Hello World."));
    assertThat(new Katakanizr().katakanize("こんにちは　世界"), is("コンニチハ 世界"));
    assertThat(new Katakanizr().katakanize("ｺﾝﾆﾁﾊ　世界"), is("コンニチハ 世界"));
    assertThat(new Katakanizr().katakanize("コンニチハ　世界"), is("コンニチハ 世界"));
    assertThat(new Katakanizr().katakanize("バーバパパ"), is("バーバパパ"));
    assertThat(new Katakanizr().katakanize("ﾊﾞｰﾊﾞﾊﾟﾊﾟ"), is("バーバパパ"));
    assertThat(new Katakanizr().katakanize("ハ゛ーハ゛ハ゜ハ゜"), is("バーバパパ"));
    assertThat(new Katakanizr().katakanize("ヷヸヴヹヺ"), is("ヷヸヴヹヺ"));
    assertThat(new Katakanizr().katakanize("ワ゛ヰ゛ウ゛ヱ゛ヲ゛"), is("ヷヸヴヹヺ"));
    assertThat(new Katakanizr().katakanize("ヷヸヴヹヺ"), is("ヷヸヴヹヺ"));
    assertThat(new Katakanizr().katakanize("あ゜い゜う゜え゜お゜な゛に゛ぬ゛ね゛の゛"), is("ア゜イ゜ウ゜エ゜オ゜ナ゛ニ゛ヌ゛ネ゛ノ゛"));
    assertThat(new Katakanizr().katakanize("あ゚い゚う゚え゚お゚な゙に゙ぬ゙ね゙の゙"), is("ア゜イ゜ウ゜エ゜オ゜ナ゛ニ゛ヌ゛ネ゛ノ゛"));
    assertThat(new Katakanizr().katakanize("ア゜イ゜ウ゜エ゜オ゜ナ゛ニ゛ヌ゛ネ゛ノ゛"), is("ア゜イ゜ウ゜エ゜オ゜ナ゛ニ゛ヌ゛ネ゛ノ゛"));
    assertThat(new Katakanizr().katakanize("ア゚イ゚ウ゚エ゚オ゚ナ゙ニ゙ヌ゙ネ゙ノ゙"), is("ア゜イ゜ウ゜エ゜オ゜ナ゛ニ゛ヌ゛ネ゛ノ゛"));
    assertThat(new Katakanizr().katakanize("ｱﾟｲﾟｳﾟｴﾟｵﾟﾅﾞﾆﾞﾇﾞﾈﾞﾉﾞ"), is("ア゜イ゜ウ゜エ゜オ゜ナ゛ニ゛ヌ゛ネ゛ノ゛"));
    assertThat(new Katakanizr().katakanize("パ～やん"), is("パ～ヤン"));
    assertThat(new Katakanizr().katakanize("ｱﾚﾝ･ｷﾞﾝｽﾞﾊﾞｰｸﾞ"), is("アレン・ギンズバーグ"));
  }

}
