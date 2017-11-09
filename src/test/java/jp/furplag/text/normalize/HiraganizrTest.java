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

public class HiraganizrTest {

  @Test
  public void test() {
    assertThat(new Hiraganizr().order(), is(10));
    assertThat(new Hiraganizr().hiraganize(null), is((String) null));
    assertThat(new Hiraganizr().hiraganize(""), is(""));
    assertThat(new Hiraganizr().hiraganize("Hello World."), is("Hello World."));
    assertThat(new Hiraganizr().hiraganize("こんにちは　世界"), is("こんにちは 世界"));
    assertThat(new Hiraganizr().hiraganize("ｺﾝﾆﾁﾊ　世界"), is("こんにちは 世界"));
    assertThat(new Hiraganizr().hiraganize("コンニチハ　世界"), is("こんにちは 世界"));
    assertThat(new Hiraganizr().hiraganize("バーバパパ"), is("ばーばぱぱ"));
    assertThat(new Hiraganizr().hiraganize("ﾊﾞｰﾊﾞﾊﾟﾊﾟ"), is("ばーばぱぱ"));
    assertThat(new Hiraganizr().hiraganize("ハ゛ーハ゛ハ゜ハ゜"), is("ばーばぱぱ"));
    assertThat(new Hiraganizr().hiraganize("ヷヸヴヹヺ"), is("わ゛ゐ゛ゔゑ゛を゛"));
    assertThat(new Hiraganizr().hiraganize("ワ゛ヰ゛ウ゛ヱ゛ヲ゛"), is("わ゛ゐ゛ゔゑ゛を゛"));
    assertThat(new Hiraganizr().hiraganize("ヷヸヴヹヺ"), is("わ゛ゐ゛ゔゑ゛を゛"));
    assertThat(new Hiraganizr().hiraganize("あ゜い゜う゜え゜お゜な゛に゛ぬ゛ね゛の゛"), is("あ゜い゜う゜え゜お゜な゛に゛ぬ゛ね゛の゛"));
    assertThat(new Hiraganizr().hiraganize("あ゚い゚う゚え゚お゚な゙に゙ぬ゙ね゙の゙"), is("あ゜い゜う゜え゜お゜な゛に゛ぬ゛ね゛の゛"));
    assertThat(new Hiraganizr().hiraganize("ア゜イ゜ウ゜エ゜オ゜ナ゛ニ゛ヌ゛ネ゛ノ゛"), is("あ゜い゜う゜え゜お゜な゛に゛ぬ゛ね゛の゛"));
    assertThat(new Hiraganizr().hiraganize("ア゚イ゚ウ゚エ゚オ゚ナ゙ニ゙ヌ゙ネ゙ノ゙"), is("あ゜い゜う゜え゜お゜な゛に゛ぬ゛ね゛の゛"));
    assertThat(new Hiraganizr().hiraganize("ｱﾟｲﾟｳﾟｴﾟｵﾟﾅﾞﾆﾞﾇﾞﾈﾞﾉﾞ"), is("あ゜い゜う゜え゜お゜な゛に゛ぬ゛ね゛の゛"));
    assertThat(new Hiraganizr().hiraganize("パ～やん"), is("ぱ～やん"));
    assertThat(new Hiraganizr().hiraganize("ｱﾚﾝ･ｷﾞﾝｽﾞﾊﾞｰｸﾞ"), is("あれん・ぎんずばーぐ"));
  }

}
