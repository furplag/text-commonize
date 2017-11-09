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

package jp.furplag.text.optimize;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.function.Function;
import java.util.function.IntPredicate;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import org.junit.Test;

import jp.furplag.text.optimize.Trimr;
import jp.furplag.text.regex.RegexrOrigin;

public class TrimrTest {

  @Test
  public void test() throws NoSuchMethodException, SecurityException, InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
    Constructor<?> c = Trimr.class.getDeclaredConstructor();
    c.setAccessible(true);
    assertThat(c.newInstance() instanceof Trimr, is(true));
  }

  @Test
  public void testTrim() {
    assertThat(Trimr.trim(null), is((String) null));
    assertThat(Trimr.trim(""), is(""));
    assertThat(Trimr.trim("theString."), is("theString."));
    assertThat(Trimr.trim("the String."), is("the String."));
    assertThat(Trimr.trim("the String ."), is("the String ."));
    assertThat(Trimr.trim("the String. "), is("the String."));
    assertThat(Trimr.trim(" the String."), is("the String."));
    assertThat(Trimr.trim(" the String. "), is("the String."));
    assertThat(Trimr.trim("   the String.   "), is("the String."));
    assertThat(Trimr.trim(" \u0010 the String. \u0002 "), is("the String."));
    assertThat(Trimr.trim("\n\n\nthe String.\n\n\n"), is("the String."));
    assertThat(Trimr.trim("\nthe String.\n"), is("the String."));
    assertThat(Trimr.trim("\t\t\tthe String.\t\t\t"), is("the String."));
    assertThat(Trimr.trim("\tthe String.\t"), is("the String."));
    assertThat(Trimr.trim(" 　 the String. 　 "), is("the String."));

    String ctrls = IntStream.rangeClosed(0, 200_000).filter(Character::isISOControl).filter(((IntPredicate) Character::isWhitespace).negate()).filter(codePoint -> codePoint > 0x001C || codePoint < 0x0020).mapToObj(RegexrOrigin::newString).collect(Collectors.joining());
    String whitespaces = IntStream.rangeClosed(0, 200_000).filter(Character::isWhitespace).mapToObj(RegexrOrigin::newString).collect(Collectors.joining());
    Function<IntStream, String> randomizr = new Function<IntStream, String>() {
      @Override
      public String apply(IntStream t) {
        return t.mapToObj(RegexrOrigin::newString).collect(Collectors.toCollection(HashSet::new)).stream().collect(Collectors.joining());
      }
    };

    final String string = "the string.";
    IntStream.range(0, 10000).forEach(i->{
      String uglified = randomizr.apply((ctrls + whitespaces).codePoints());
      uglified += string;
      uglified += randomizr.apply((ctrls + whitespaces).codePoints());

      assertThat(Trimr.trim(uglified), is(string));
    });
  }

  @Test
  public void testIsTrimmed() {
    assertThat(Trimr.isTrimmed(null), is(true));
    assertThat(Trimr.isTrimmed(""), is(true));
    assertThat(Trimr.isTrimmed("theString."), is(true));
    assertThat(Trimr.isTrimmed("the String."), is(true));
    assertThat(Trimr.isTrimmed("the String ."), is(true));
    assertThat(Trimr.isTrimmed("the String. "), is(false));
    assertThat(Trimr.isTrimmed(" the String."), is(false));
    assertThat(Trimr.isTrimmed(" the String. "), is(false));
    assertThat(Trimr.isTrimmed(Arrays.stream("theString.".split("")).collect(Collectors.joining("\n"))), is(true));

    String ctrls = IntStream.rangeClosed(0, 200_000).filter(Character::isISOControl).filter(((IntPredicate) Character::isWhitespace).negate()).filter(codePoint -> codePoint > 0x001C || codePoint < 0x0020).mapToObj(RegexrOrigin::newString).collect(Collectors.joining());
    String whitespaces = IntStream.rangeClosed(0, 200_000).filter(Character::isWhitespace).mapToObj(RegexrOrigin::newString).collect(Collectors.joining());
    Function<IntStream, String> randomizr = new Function<IntStream, String>() {
      @Override
      public String apply(IntStream t) {
        List<String> list = t.mapToObj(RegexrOrigin::newString).collect(Collectors.toCollection(ArrayList::new));
        Collections.shuffle(list);

        return list.stream().collect(Collectors.joining());
      }
    };

    final String string = "the string.";
    IntStream.range(0, 10000).forEach(i->{
      String uglified = randomizr.apply((ctrls + whitespaces).codePoints());
      uglified += string;
      uglified += randomizr.apply((ctrls + whitespaces).codePoints());
      assertThat(Trimr.isTrimmed(uglified), is(false));
      assertThat(Trimr.isTrimmed(Trimr.trim(uglified)), is(true));
    });
  }

}
