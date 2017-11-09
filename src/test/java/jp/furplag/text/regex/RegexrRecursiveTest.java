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

package jp.furplag.text.regex;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;

import java.util.stream.Collectors;
import java.util.stream.IntStream;

import org.junit.Test;

import jp.furplag.text.regex.RegexrOrigin;
import jp.furplag.text.regex.RegexrRecursive;

public class RegexrRecursiveTest {

  @Test
  public void test() {
    assertThat(new RegexrRecursive(null, null), is(new RegexrRecursive(null, null){
      @Override public int order() {return 0;}
    }));

    assertThat(new RegexrRecursive(null, null).order(), is(0));
  }


  @Test
  public void testReplaceAll() {
    assertThat(new RegexrRecursive("one", "壱").replaceAll(null), is((String) null));
    assertThat(new RegexrRecursive("one", "壱").replaceAll(""), is(""));
    assertThat(new RegexrRecursive("one", "壱").replaceAll("1"), is("1"));
    assertThat(new RegexrRecursive("one", "壱").replaceAll("o ne"), is("o ne"));
    assertThat(new RegexrRecursive("one", "壱").replaceAll("One"), is("One"));
    assertThat(new RegexrRecursive("[Oo]ne", "壱").replaceAll("One"), is("壱"));
    assertThat(new RegexrRecursive("one", "壱").replaceAll("\t\t\to\tne\t\t\t"), is("\t\t\to\tne\t\t\t"));
    assertThat(new RegexrRecursive("one", "壱").replaceAll("\t\t\to\nne\t\t\t"), is("\t\t\to\nne\t\t\t"));
    assertThat(new RegexrRecursive("one", "壱").replaceAll("one"), is("壱"));
    assertThat(new RegexrRecursive("one", "壱").replaceAll("one two three"), is("壱 two three"));
    assertThat(new RegexrRecursive("one", "壱").replaceAll("three two one zero"), is("three two 壱 zero"));
    assertThat(new RegexrRecursive("one", "壱").replaceAll("three\ntwo\none\nzero"), is("three\ntwo\n壱\nzero"));
    assertThat(new RegexrRecursive("one", "壱").replaceAll("oneoneoneoneone"), is("壱壱壱壱壱"));
    assertThat(new RegexrRecursive("one", "壱").replaceAll("neoneoneoneoneo"), is("ne壱壱壱壱o"));
    assertThat(new RegexrRecursive("one", "壱").replaceAll("\t\t\tone\t\t\t"), is("\t\t\t壱\t\t\t"));
    assertThat(new RegexrRecursive("o\\s?n\\s?e", "壱").replaceAll("\t\t\to\nne\t\t\t"), is("\t\t\t壱\t\t\t"));
    assertThat(new RegexrRecursive("o\\s*n\\s*e", "壱").replaceAll("\t\t\to\nn\t\t\t\n\t\t\te\t\t\t"), is("\t\t\t壱\t\t\t"));

    String spaces = IntStream.rangeClosed(Character.MIN_CODE_POINT, Character.MAX_CODE_POINT).filter(Character::isWhitespace).mapToObj(RegexrOrigin::newString).collect(Collectors.joining("\u0020"));
    assertThat(new RegexrRecursive("[\\p{javaWhitespace}]{2,}", "\u0020").replaceAll(spaces), is(" "));
  }
}
