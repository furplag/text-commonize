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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.IntPredicate;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import org.junit.Test;

import jp.furplag.text.regex.Regexr;
import jp.furplag.text.regex.RegexrOrigin;
import jp.furplag.text.regex.RegexrRecursive;
import jp.furplag.text.regex.RegexrStandard;
import jp.furplag.util.reflect.SavageReflection;

public class RegexrTest {

  @Test
  public void test() {
    // @formatter:off
    assertThat(new Regexr(null,  null) {public int order() {return 0;}} instanceof Regexr, is(true));

    Regexr one = new Regexr("one",  "壱") {public int order() {return 1;}};
    Regexr two = new Regexr("two",  "弐") {public int order() {return 2;}};
    Regexr three = new Regexr("three",  "参") {public int order() {return 3;}};
    Regexr minus = new Regexr("minus",  "▲壱") {public int order() {return -1;}};
    // @formatter:on

    assertThat(Arrays.asList(null, two, one, three, minus).stream().toArray(), is(new RegexrOrigin[] {null, two, one, three, minus}));
    assertThat(Arrays.asList(null, two, one, three, minus).stream().sorted().toArray(), is(new RegexrOrigin[] {null, minus, one, two, three}));

    assertThat(one.equals(null), is(false));
    assertThat(one.equals((Regexr) null), is(false));
    assertThat(one.equals(""), is(false));
    assertThat(one.equals(new RegexrOrigin() {
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
      public String replaceAll(String string) {
        return null;
      }

      @Override
      public Pattern pattern() {
        return null;
      }
    }), is(false));
    assertThat(one.equals(two), is(false));
    assertThat(one.equals(new Regexr("one", "1") {
      public int order() {
        return 1;
      }
    }), is(false));
    assertThat(one == two, is(false));
    RegexrOrigin anotherOne = one;
    assertThat(one == anotherOne, is(true));
    assertThat(one == new Regexr("one", "壱") {
      public int order() {
        return 1;
      }
    }, is(false));
    assertThat(one.equals(one), is(true));
    assertThat(one.equals(new Regexr("one", "壱") {
      public int order() {
        return 1;
      }
    }), is(true));
    assertThat(one.equals(new Regexr("one", "壱") {
      public int order() {
        return -100;
      }
    }), is(true));

    assertThat(one.hashCode() == two.hashCode(), is(false));
    assertThat(one.hashCode() == new Regexr("one", "1") {
      public int order() {
        return 1;
      }
    }.hashCode(), is(false));
    assertThat(one.hashCode() == anotherOne.hashCode(), is(true));
    assertThat(one.hashCode() == new Regexr("one", "壱") {
      public int order() {
        return 1;
      }
    }.hashCode(), is(true));
    assertThat(one.hashCode() == new Regexr("one", "壱") {
      public int order() {
        return -100;
      }
    }.hashCode(), is(true));

    try {
      assertThat(one.hashCode() == new Regexr("one", "壱") {
        {
          SavageReflection.set(this, Regexr.class.getDeclaredField("pattern"), null);
          SavageReflection.set(this, Regexr.class.getDeclaredField("replacement"), null);
        }

        public int order() {
          return -100;
        }
      }.hashCode(), is(false));
    } catch (Exception e) {
      fail();
    }
  }

  @Test
  public void testMatches() {
    assertThat((new Regexr("one", "壱") {
      public int order() {
        return 1;
      }
    }).matches(null), is(false));
    assertThat((new Regexr("one", "壱") {
      public int order() {
        return 1;
      }
    }).matches(""), is(false));
    assertThat((new Regexr("one", "壱") {
      public int order() {
        return 1;
      }
    }).matches("1"), is(false));
    assertThat((new Regexr("one", "壱") {
      public int order() {
        return 1;
      }
    }).matches("o ne"), is(false));
    assertThat((new Regexr("one", "壱") {
      public int order() {
        return 1;
      }
    }).matches("One"), is(false));
    assertThat((new Regexr("one", "壱") {
      public int order() {
        return 1;
      }
    }).matches("\t\t\to\tne\t\t\t"), is(false));
    assertThat((new Regexr("one", "壱") {
      public int order() {
        return 1;
      }
    }).matches("\t\t\to\nne\t\t\t"), is(false));
    assertThat((new Regexr("one", "壱") {
      public int order() {
        return 1;
      }
    }).matches("one"), is(true));
    assertThat((new Regexr("one", "壱") {
      public int order() {
        return 1;
      }
    }).matches("one two three"), is(true));
    assertThat((new Regexr("one", "壱") {
      public int order() {
        return 1;
      }
    }).matches("three two one zero"), is(true));
    assertThat((new Regexr("one", "壱") {
      public int order() {
        return 1;
      }
    }).matches("three\ntwo\none\nzero"), is(true));
    assertThat((new Regexr("one", "壱") {
      public int order() {
        return 1;
      }
    }).matches("oneoneoneoneone"), is(true));
    assertThat((new Regexr("one", "壱") {
      public int order() {
        return 1;
      }
    }).matches("neoneoneoneoneo"), is(true));
    assertThat((new Regexr("one", "壱") {
      public int order() {
        return 1;
      }
    }).matches("\t\t\tone\t\t\t"), is(true));
    assertThat((new Regexr("o\\s?n\\s?e", "壱") {
      public int order() {
        return 1;
      }
    }).matches("\t\t\to\nne\t\t\t"), is(true));
    assertThat((new Regexr("o\\s*n\\s*e", "壱") {
      public int order() {
        return 1;
      }
    }).matches("\t\t\to\nn\t\t\t\n\t\t\te\t\t\t"), is(true));
  }

  @Test
  public void testFind() {
    assertThat((new Regexr("one", "壱") {
      public int order() {
        return 1;
      }
    }).find(null), is(new ArrayList<>()));
    assertThat((new Regexr("one", "壱") {
      public int order() {
        return 1;
      }
    }).find(""), is(new ArrayList<>()));
    assertThat((new Regexr("one", "壱") {
      public int order() {
        return 1;
      }
    }).find("1"), is(new ArrayList<>()));
    assertThat((new Regexr("one", "壱") {
      public int order() {
        return 1;
      }
    }).find("o ne"), is(new ArrayList<>()));
    assertThat((new Regexr("one", "壱") {
      public int order() {
        return 1;
      }
    }).find("One"), is(new ArrayList<>()));
    assertThat((new Regexr("one", "壱") {
      public int order() {
        return 1;
      }
    }).find("\t\t\to\tne\t\t\t"), is(new ArrayList<>()));
    assertThat((new Regexr("one", "壱") {
      public int order() {
        return 1;
      }
    }).find("\t\t\to\nne\t\t\t"), is(new ArrayList<>()));
    assertThat((new Regexr("one", "壱") {
      public int order() {
        return 1;
      }
    }).find("one"), is(Arrays.asList("one")));
    assertThat((new Regexr("one", "壱") {
      public int order() {
        return 1;
      }
    }).find("one two three"), is(Arrays.asList("one")));
    assertThat((new Regexr("one", "壱") {
      public int order() {
        return 1;
      }
    }).find("three two one zero"), is(Arrays.asList("one")));
    assertThat((new Regexr("one", "壱") {
      public int order() {
        return 1;
      }
    }).find("three\ntwo\none\nzero"), is(Arrays.asList("one")));
    assertThat((new Regexr("one", "壱") {
      public int order() {
        return 1;
      }
    }).find("oneoneoneoneone"), is(Arrays.asList("one", "one", "one", "one", "one")));
    assertThat((new Regexr("one", "壱") {
      public int order() {
        return 1;
      }
    }).find("neoneoneoneoneo"), is(Arrays.asList("one", "one", "one", "one")));
    assertThat((new Regexr("one", "壱") {
      public int order() {
        return 1;
      }
    }).find("\t\t\tone\t\t\t"), is(Arrays.asList("one")));
    assertThat((new Regexr("o\\s?n\\s?e", "壱") {
      public int order() {
        return 1;
      }
    }).find("\t\t\to\nne\t\t\t"), is(Arrays.asList("o\nne")));
    assertThat((new Regexr("o\\s*n\\s*e", "壱") {
      public int order() {
        return 1;
      }
    }).find("\t\t\to\nn\t\t\t\n\t\t\te\t\t\t"), is(Arrays.asList("o\nn\t\t\t\n\t\t\te")));
    assertThat((new Regexr("[one]", "壱") {
      public int order() {
        return 1;
      }
    }).find("\t\t\to\nn\t\t\t\n\t\t\te\t\t\t"), is(Arrays.asList("o", "n", "e")));
  }

  @Test
  public void testReplaceAll() {
    assertThat((new Regexr("one", "壱") {
      public int order() {
        return 1;
      }
    }).replaceAll(null), is((String) null));
    assertThat((new Regexr("one", "壱") {
      public int order() {
        return 1;
      }
    }).replaceAll(""), is(""));
    assertThat((new Regexr("one", "壱") {
      public int order() {
        return 1;
      }
    }).replaceAll("1"), is("1"));
    assertThat((new Regexr("one", "壱") {
      public int order() {
        return 1;
      }
    }).replaceAll("o ne"), is("o ne"));
    assertThat((new Regexr("one", "壱") {
      public int order() {
        return 1;
      }
    }).replaceAll("One"), is("One"));
    assertThat((new Regexr("[Oo]ne", "壱") {
      public int order() {
        return 1;
      }
    }).replaceAll("One"), is("壱"));
    assertThat((new Regexr("one", "壱") {
      public int order() {
        return 1;
      }
    }).replaceAll("\t\t\to\tne\t\t\t"), is("\t\t\to\tne\t\t\t"));
    assertThat((new Regexr("one", "壱") {
      public int order() {
        return 1;
      }
    }).replaceAll("\t\t\to\nne\t\t\t"), is("\t\t\to\nne\t\t\t"));
    assertThat((new Regexr("one", "壱") {
      public int order() {
        return 1;
      }
    }).replaceAll("one"), is("壱"));
    assertThat((new Regexr("one", "壱") {
      public int order() {
        return 1;
      }
    }).replaceAll("one two three"), is("壱 two three"));
    assertThat((new Regexr("one", "壱") {
      public int order() {
        return 1;
      }
    }).replaceAll("three two one zero"), is("three two 壱 zero"));
    assertThat((new Regexr("one", "壱") {
      public int order() {
        return 1;
      }
    }).replaceAll("three\ntwo\none\nzero"), is("three\ntwo\n壱\nzero"));
    assertThat((new Regexr("one", "壱") {
      public int order() {
        return 1;
      }
    }).replaceAll("oneoneoneoneone"), is("壱壱壱壱壱"));
    assertThat((new Regexr("one", "壱") {
      public int order() {
        return 1;
      }
    }).replaceAll("neoneoneoneoneo"), is("ne壱壱壱壱o"));
    assertThat((new Regexr("one", "壱") {
      public int order() {
        return 1;
      }
    }).replaceAll("\t\t\tone\t\t\t"), is("\t\t\t壱\t\t\t"));
    assertThat((new Regexr("o\\s?n\\s?e", "壱") {
      public int order() {
        return 1;
      }
    }).replaceAll("\t\t\to\nne\t\t\t"), is("\t\t\t壱\t\t\t"));
    assertThat((new Regexr("o\\s*n\\s*e", "壱") {
      public int order() {
        return 1;
      }
    }).replaceAll("\t\t\to\nn\t\t\t\n\t\t\te\t\t\t"), is("\t\t\t壱\t\t\t"));
  }

  @Test
  public void testCtrlRemovr() {
    assertThat(Regexr.CtrlRemovr, is(new RegexrStandard("[\\p{Cc}&&[^\\s\\x{001C}-\\x{001F}]]", "")));
    assertThat(Regexr.CtrlRemovr.order(), is(0));

    String ctrls = IntStream.rangeClosed(0, 200_000).filter(Character::isISOControl).filter(((IntPredicate) Character::isWhitespace).negate()).filter(codePoint -> codePoint > 0x001C || codePoint < 0x0020).mapToObj(RegexrOrigin::newString).collect(Collectors.joining());
    String ctrlsR = IntStream.rangeClosed(0, 200_000).mapToObj(RegexrOrigin::newString).filter(s->Regexr.CtrlRemovr.pattern.matcher(s).matches()).collect(Collectors.joining());
    assertThat(ctrlsR.codePoints().toArray(), is(ctrls.codePoints().toArray()));
    assertThat(ctrlsR, is(ctrls));
    assertThat(Regexr.CtrlRemovr.replaceAll(ctrls) , is(""));
    assertThat(Regexr.CtrlRemovr.replaceAll(ctrlsR) , is(""));

    final String string = "PrettyfyMe.";
    String uglified = "";
    for (String ctrl : ctrls.split("")) {
      // @formatter:off
      uglified = Arrays.stream(string.split("")).collect(Collectors.joining(ctrl + ctrl + ctrl + ctrl + ctrl));
      // @formatter:on
      assertThat(Character.isISOControl(ctrl.codePointAt(0)), is(true));
      assertThat(Regexr.CtrlRemovr.replaceAll(uglified), is(string));
    }
    uglified = Arrays.stream(string.split("")).collect(Collectors.joining(ctrls));
    assertThat(Regexr.CtrlRemovr.replaceAll(uglified), is(string));
    uglified = Arrays.stream(string.split("\u0020")).collect(Collectors.joining(ctrlsR));
    assertThat(Regexr.CtrlRemovr.replaceAll(uglified), is(string));
  }

  @Test
  public void testLinefeedLintr() {
    assertThat(Regexr.LinefeedLintr, is(new RegexrRecursive("\\s+\\n|\\n\\s+", "\n")));
    assertThat(Regexr.LinefeedLintr.order(), is(1000));

    final String string = "P\nr\ne\nt\nt\ny\nf\ny\nM\ne\n.";
    String uglified = "";
    String space = " \n  \n\n";
    while (space.length() < 100000) {
      uglified = Arrays.stream(string.split("\n")).collect(Collectors.joining(space += space));
      assertThat(Regexr.LinefeedLintr.replaceAll(uglified), is(string));
    }
  }

  @Test
  public void testSpaceLintr() {
    assertThat(Regexr.SpaceLintr, is(new RegexrRecursive("[\\p{javaWhitespace}&&[^\\x{000A}]]{2,}", "\u0020")));
    assertThat(Regexr.SpaceLintr.order(), is(100));

    final String string = "S h r i n k m e .";
    String uglified = "";
    String space = " ";
    while (space.length() < 10000) {
      uglified = Arrays.stream(string.split("\u0020")).collect(Collectors.joining(space));
      assertThat(Regexr.SpaceLintr.replaceAll(uglified), is(string));
      space += space;
    }
  }

  @Test
  public void testSpaceNormalizr() {
    assertThat(Regexr.SpaceNormalizr, is(new RegexrStandard("[\\p{javaWhitespace}&&[^\\x{000A}\\x{0020}]]", "\u0020")));
    assertThat(Regexr.SpaceNormalizr.order(), is(10));

    String spaces = IntStream.rangeClosed(0, 200_000).filter(Character::isWhitespace).filter(codePoint -> codePoint != 0x000A && codePoint != 0x0020).mapToObj(RegexrOrigin::newString).collect(Collectors.joining());
    String spacesR = IntStream.rangeClosed(0, 200_000).mapToObj(RegexrOrigin::newString).filter(s->Regexr.SpaceNormalizr.pattern().matcher(s).matches()).collect(Collectors.joining());
    assertThat(spacesR.codePoints().toArray(), is(spaces.codePoints().toArray()));
    assertThat(spacesR, is(spaces));
    assertThat(Regexr.SpaceNormalizr.replaceAll(spaces) , is(spaces.replaceAll("\\p{javaWhitespace}", " ")));
    assertThat(Regexr.SpaceNormalizr.replaceAll(spacesR) , is(spacesR.replaceAll("\\p{javaWhitespace}", " ")));

    final String string = "N o r m a l i z e m e .";
    String uglified = "";
    for (String space : spaces.split("")) {
      // @formatter:off
      uglified = Arrays.stream(string.split("\u0020")).collect(Collectors.joining(space + space + space + space + space));
      // @formatter:on
      assertThat(Character.isWhitespace(space.codePointAt(0)), is(true));
      assertThat(Regexr.SpaceNormalizr.replaceAll(uglified), is(Arrays.stream(string.split("\u0020")).collect(Collectors.joining("\u0020\u0020\u0020\u0020\u0020"))));
    }
    uglified = Arrays.stream(string.split("\u0020")).collect(Collectors.joining(spaces));
    assertThat(Regexr.SpaceNormalizr.replaceAll(uglified), is(Arrays.stream(string.split("\u0020")).collect(Collectors.joining(spaces.replaceAll("\\p{javaWhitespace}", " ")))));
    uglified = Arrays.stream(string.split("\u0020")).collect(Collectors.joining(spacesR));
    assertThat(Regexr.SpaceNormalizr.replaceAll(uglified), is(Arrays.stream(string.split("\u0020")).collect(Collectors.joining(spacesR.replaceAll("\\p{javaWhitespace}", " ")))));
  }

  @Test
  public void testTrimr() {
    assertThat(Regexr.Trimr, is(new RegexrStandard("^[\\p{javaWhitespace}]+|[\\p{javaWhitespace}]+$", "")));
    assertThat(Regexr.Trimr.order(), is(10000));

    String whitespaces = IntStream.rangeClosed(0, 200_000).filter(Character::isWhitespace).mapToObj(RegexrOrigin::newString).collect(Collectors.joining());
    String whitespacesR = IntStream.rangeClosed(0, 200_000).mapToObj(RegexrOrigin::newString).filter(s -> Regexr.Trimr.pattern.matcher(s).matches()).collect(Collectors.joining());
    assertThat(whitespacesR, is(whitespaces));
    assertThat(whitespacesR.codePoints().toArray(), is(whitespaces.codePoints().toArray()));
    assertThat(Regexr.Trimr.replaceAll(whitespaces), is(""));
    assertThat(Regexr.Trimr.replaceAll(whitespacesR), is(""));

    final String string = "Trim me.";
    String uglified = "";
    for (String space : whitespacesR.split("")) {
    // @formatter:off
    uglified = new StringBuilder(string)
      .insert(0, space + space + space + space + space)
      .append(space + space + space + space + space)
      .toString();
    // @formatter:on
      assertThat(Character.isWhitespace(space.codePointAt(0)), is(true));
      assertThat(Regexr.Trimr.replaceAll(uglified), is(string));
    }
    do {
      uglified = new StringBuilder(uglified).insert(0, whitespacesR).append(whitespacesR).toString();
    } while (uglified.length() < 10000);
    assertThat(Regexr.Trimr.replaceAll(uglified), is(string));
  }

  @Test
  public void testConstants() {
    assertThat(new Regexr[] {Regexr.CtrlRemovr, Regexr.LinefeedLintr, Regexr.SpaceLintr, Regexr.SpaceNormalizr, Regexr.Trimr}, is(new Regexr[] {Regexr.CtrlRemovr, Regexr.LinefeedLintr, Regexr.SpaceLintr, Regexr.SpaceNormalizr, Regexr.Trimr}));
    assertThat(RegexrOrigin.regexrs(new Regexr[] {Regexr.CtrlRemovr, Regexr.LinefeedLintr, Regexr.SpaceLintr, Regexr.SpaceNormalizr, Regexr.Trimr}).toArray(Regexr[]::new), is(new Regexr[] {Regexr.CtrlRemovr, Regexr.SpaceNormalizr, Regexr.SpaceLintr, Regexr.LinefeedLintr, Regexr.Trimr}));
  }
}
