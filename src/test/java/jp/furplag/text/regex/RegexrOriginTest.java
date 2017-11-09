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

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.regex.Pattern;
import java.util.stream.IntStream;

import org.junit.Test;

import jp.furplag.text.regex.Regexr;
import jp.furplag.text.regex.RegexrOrigin;
import jp.furplag.text.regex.RegexrRecursive;
import jp.furplag.text.regex.RegexrStandard;

public class RegexrOriginTest {

  @Test
  public void test() {
    RegexrOrigin regexr = new RegexrOrigin() {

      @Override
      public String replaceAll(String string) {
        return null;
      }

      @Override
      public List<String> find(String string) {
        return null;
      }

      @Override
      public boolean matches(String string) {
        return false;
      }

      @Override
      public int order() {
        return 0;
      }

      @Override
      public Pattern pattern() {
        return null;
      }
    };
    assertThat(RegexrOrigin.class.isAssignableFrom(regexr.getClass()), is(true));
    assertThat(regexr.replaceAll("nothing to do."), is((String) null));
    assertThat(regexr.find("nothing to do."), is((List<String>) null));
    assertThat(regexr.matches("nothing to do."), is(false));
    assertThat(regexr.order(), is(0));
    assertThat(regexr.pattern(), is((Pattern) null));
    assertThat(regexr.regex(), is((String) null));

    regexr = new RegexrOrigin() {

      @Override
      public String replaceAll(String string) {
        return string;
      }

      @Override
      public List<String> find(String string) {
        return Arrays.asList(string);
      }

      @Override
      public boolean matches(String string) {
        return !Objects.toString(string, "").isEmpty();
      }

      @Override
      public int order() {
        return 1;
      }

      @Override
      public Pattern pattern() {
        return Pattern.compile("^[\\x{0000}]$");
      }
    };
    assertThat(RegexrOrigin.class.isAssignableFrom(regexr.getClass()), is(true));
    assertThat(regexr.replaceAll("nothing to do."), is("nothing to do."));
    assertThat(regexr.find("nothing to do."), is(Arrays.asList("nothing to do.")));
    assertThat(regexr.matches("nothing to do."), is(true));
    assertThat(regexr.order(), is(1));
    assertThat(regexr.pattern().pattern(), is(Pattern.compile("^[\\x{0000}]$").pattern()));
    assertThat(regexr.regex(), is("^[\\x{0000}]$"));
  }

  @Test
  public void testCodePoints() {

    assertThat(RegexrOrigin.codePoints((String) null).toArray(), is(new int[] {}));
    assertThat(RegexrOrigin.codePoints("").toArray(), is(new int[] {}));
    assertThat(RegexrOrigin.codePoints((String[]) null).toArray(), is(new int[] {}));
    assertThat(RegexrOrigin.codePoints(new String[] {}).toArray(), is(new int[] {}));
    assertThat(RegexrOrigin.codePoints(new String[] {null, null}).toArray(), is(new int[] {}));
    assertThat(RegexrOrigin.codePoints(new String[] {null, ""}).toArray(), is(new int[] {}));
    assertThat(RegexrOrigin.codePoints(new String[] {"", ""}).toArray(), is(new int[] {}));
    assertThat(RegexrOrigin.codePoints("南無阿弥陀仏").toArray(), is("南無阿弥陀仏".codePoints().toArray()));
    assertThat(RegexrOrigin.codePoints("諸行無常", null, "", " ", "盛者必衰").toArray(), is("諸行無常 盛者必衰".codePoints().toArray()));
  }

  @Test
  public void testIsEmpty() {
    assertThat(RegexrOrigin.isEmpty((String) null), is(true));
    assertThat(RegexrOrigin.isEmpty(""), is(true));
    assertThat(RegexrOrigin.isEmpty("\u0000"), is(false));
    assertThat(RegexrOrigin.isEmpty("\t\r\n\u0020"), is(false));
    assertThat(RegexrOrigin.isEmpty("南無阿弥陀仏"), is(false));

    assertThat(RegexrOrigin.isEmpty((String[]) null), is(true));
    assertThat(RegexrOrigin.isEmpty(new String[] {}), is(true));
    assertThat(RegexrOrigin.isEmpty(new String[] {null}), is(true));
    assertThat(RegexrOrigin.isEmpty(null, null), is(true));
    assertThat(RegexrOrigin.isEmpty(null, ""), is(true));
    assertThat(RegexrOrigin.isEmpty("", null), is(true));
    assertThat(RegexrOrigin.isEmpty("", ""), is(true));
    assertThat(RegexrOrigin.isEmpty("宇", null), is(false));
    assertThat(RegexrOrigin.isEmpty(null, "宙"), is(false));
    assertThat(RegexrOrigin.isEmpty("南無阿弥陀仏".split("")), is(false));
  }

  @Test
  public void testNewString() {
    assertThat(RegexrOrigin.newString(-1), is(""));
    assertThat(RegexrOrigin.newString(-1, 21163), is("劫"));
    assertThat(RegexrOrigin.newString(0), is("\u0000"));
    assertThat(RegexrOrigin.newString("南無阿弥陀仏".codePoints().toArray()), is("南無阿弥陀仏"));

    assertThat(RegexrOrigin.newString(IntStream.rangeClosed(-128, 128).toArray()), is(RegexrOrigin.newString(IntStream.rangeClosed(0, 128).toArray())));
  }

  @Test
  public void testJoin() {
    assertThat(RegexrOrigin.join((String) null), is(""));
    assertThat(RegexrOrigin.join(""), is(""));
    assertThat(RegexrOrigin.join((String[]) null), is(""));
    assertThat(RegexrOrigin.join(new String[] {}), is(""));
    assertThat(RegexrOrigin.join(new String[] {null}), is(""));
    assertThat(RegexrOrigin.join(null, null), is(""));
    assertThat(RegexrOrigin.join(null, ""), is(""));
    assertThat(RegexrOrigin.join("", null), is(""));
    assertThat(RegexrOrigin.join("", ""), is(""));

    assertThat(RegexrOrigin.join("三千", "世界"), is("三千世界"));
    assertThat(RegexrOrigin.join("", "色即是空"), is("色即是空"));
    assertThat(RegexrOrigin.join("諸行無常", null, "", " ", "盛者必衰"), is("諸行無常 盛者必衰"));
  }

  @Test
  public void testAnyMatch() {
    assertThat(RegexrOrigin.anyMatch(null), is(false));
    assertThat(RegexrOrigin.anyMatch(""), is(false));
    assertThat(RegexrOrigin.anyMatch("色不異空"), is(false));
    assertThat(RegexrOrigin.anyMatch("色不異空", ((Regexr) null)), is(false));
    assertThat(RegexrOrigin.anyMatch("色不異空", ((Regexr[]) null)), is(false));
    assertThat(RegexrOrigin.anyMatch("色不異空", null, null, null), is(false));
    assertThat(RegexrOrigin.anyMatch("色不異空", new Regexr[]{}), is(false));
    assertThat(RegexrOrigin.anyMatch("色不異空", new Regexr[]{null}), is(false));
    assertThat(RegexrOrigin.anyMatch("色不異空", new RegexrStandard("[空不異色]", RegexrOrigin.newString(128591))), is(true));
    assertThat(RegexrOrigin.anyMatch("色不異空", new RegexrStandard("色", RegexrOrigin.newString(128591))), is(true));
    assertThat(RegexrOrigin.anyMatch("色不異空", new RegexrStandard("[空不異色]", RegexrOrigin.newString(128591)), new RegexrStandard("[一切苦厄]", RegexrOrigin.newString(128591))), is(true));
    assertThat(RegexrOrigin.anyMatch("色不異空", new RegexrStandard("[諸行無常]", RegexrOrigin.newString(128591)), new RegexrStandard("[一切苦厄]", RegexrOrigin.newString(128591))), is(false));
    assertThat(RegexrOrigin.anyMatch("色不異空", new RegexrStandard("[空不異色]", RegexrOrigin.newString(128591)), new RegexrRecursive("[一切苦厄]", RegexrOrigin.newString(128591))), is(true));
    assertThat(RegexrOrigin.anyMatch("色不異空", new RegexrStandard("[諸行無常]", RegexrOrigin.newString(128591)), new RegexrRecursive("[一切苦厄]", RegexrOrigin.newString(128591))), is(false));
  }

  @Test
  public void testfindAny() {
    assertThat(RegexrOrigin.findAny(null), is(Collections.EMPTY_LIST));
    assertThat(RegexrOrigin.findAny(""), is(Collections.EMPTY_LIST));
    assertThat(RegexrOrigin.findAny("色不異空"), is(Collections.EMPTY_LIST));
    assertThat(RegexrOrigin.findAny("色不異空", ((Regexr) null)), is(Collections.EMPTY_LIST));
    assertThat(RegexrOrigin.findAny("色不異空", ((Regexr[]) null)), is(Collections.EMPTY_LIST));
    assertThat(RegexrOrigin.findAny("色不異空", null, null, null), is(Collections.EMPTY_LIST));
    assertThat(RegexrOrigin.findAny("色不異空", new Regexr[]{}), is(Collections.EMPTY_LIST));
    assertThat(RegexrOrigin.findAny("色不異空", new Regexr[]{null}), is(Collections.EMPTY_LIST));
    assertThat(RegexrOrigin.findAny("色不異空", new RegexrStandard("[空不異色]", RegexrOrigin.newString(128591))), is(Arrays.asList("色不異空".split(""))));
    assertThat(RegexrOrigin.findAny("色不異空", new RegexrStandard("色", RegexrOrigin.newString(128591))), is(Arrays.asList("色")));
    assertThat(RegexrOrigin.findAny("色不異空", new RegexrStandard("[空不異色]", RegexrOrigin.newString(128591)), new RegexrRecursive("[一切苦厄]", RegexrOrigin.newString(128591))), is(Arrays.asList("色不異空".split(""))));
    assertThat(RegexrOrigin.findAny("色不異空", new RegexrStandard("[諸行無常]", RegexrOrigin.newString(128591)), new RegexrRecursive("[一切苦厄]", RegexrOrigin.newString(128591))), is(Collections.EMPTY_LIST));
  }

  @Test
  public void testReplaceAll() {
    assertThat(RegexrOrigin.replaceAll(null, ((Regexr) null)), is(((String) null)));
    assertThat(RegexrOrigin.replaceAll("", ((Regexr) null)), is(""));
    assertThat(RegexrOrigin.replaceAll("", ((Regexr[]) null)), is(""));
    assertThat(RegexrOrigin.replaceAll("色即是空", null, null, null), is("色即是空"));
    assertThat(RegexrOrigin.replaceAll("色即是空", new Regexr[]{}), is("色即是空"));
    assertThat(RegexrOrigin.replaceAll("色即是空", new Regexr[]{null}), is("色即是空"));
    assertThat(RegexrOrigin.replaceAll("色即是空", Regexr.Trimr, Regexr.CtrlRemovr), is("色即是空"));
    assertThat(RegexrOrigin.replaceAll("色即是空", new RegexrStandard("色即是空", "空即是色")), is("空即是色"));
    assertThat(RegexrOrigin.replaceAll("色即是空", new RegexrStandard("[色即是空]", RegexrOrigin.newString(128591)), new RegexrRecursive("[\\x{1F64F}]{2,}", RegexrOrigin.newString(128591))), is("🙏"));
    assertThat(RegexrOrigin.replaceAll("色即是空", new RegexrStandard("[色即是空]", RegexrOrigin.newString(128520)), new RegexrRecursive("[\\x{1F64F}]{2,}", RegexrOrigin.newString(128591))), is("😈😈😈😈"));
    assertThat(RegexrOrigin.replaceAll("色即是空", new RegexrStandard("[色即是空]", RegexrOrigin.newString(128520)), new RegexrStandard("[\\x{1F608}]", RegexrOrigin.newString(128591)), new RegexrRecursive("[\\x{1F64F}]{2,}", RegexrOrigin.newString(128591))), is("🙏"));
    assertThat(RegexrOrigin.replaceAll("色即是空", new RegexrStandard("[色即是空]+", RegexrOrigin.newString(128591))), is("🙏"));
  }
}
