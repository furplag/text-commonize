# text-commonize

[![Build Status](https://travis-ci.org/furplag/text-commonize.svg?branch=master)](https://travis-ci.org/furplag/text-commonize)
[![Coverage Status](https://coveralls.io/repos/github/furplag/text-commonize/badge.svg?branch=master)](https://coveralls.io/github/furplag/text-commonize?branch=master)
[![Codacy Badge](https://api.codacy.com/project/badge/Grade/49feeef5102c4e45a8931b09feed4abd)](https://www.codacy.com/app/furplag/text-commonize?utm_source=github.com&amp;utm_medium=referral&amp;utm_content=furplag/text-commonize&amp;utm_campaign=Badge_Grade)
[![codebeat badge](https://codebeat.co/badges/15c40392-e0a7-4d67-955b-9565c78e36c6)](https://codebeat.co/projects/github-com-furplag-text-commonize-master)
[![Maintainability](https://api.codeclimate.com/v1/badges/39a4c2be5b1aeac0e685/maintainability)](https://codeclimate.com/github/furplag/text-commonize/maintainability)

optimized Unicode normalization for using under standard input text.

## Getting Start
Add the following snippet to any project's pom that depends on your project
```xml
<repositories>
  ...
  <repository>
    <id>text-commonize</id>
    <url>https://raw.github.com/furplag/text-commonize/mvn-repo/</url>
    <snapshots>
      <enabled>true</enabled>
      <updatePolicy>always</updatePolicy>
    </snapshots>
  </repository>
</repositories>
...
<dependencies>
  ...
  <dependency>
    <groupId>jp.furplag.sandbox</groupId>
    <artifactId>text-commonize</artifactId>
    <version>[1.1,)</version>
  </dependency>
</dependencies>
```

## Usage
```java

  // CjkNormalizr#normalize(String)
  String anExampleSomeoneInputHisName = "醜悪な　入力値（ｾｸｼｬﾙｳﾞｧｲｵﾚｯﾄ＃１）";
  System.out.println(CjkNormalizr.normalize(anExampleSomeoneInputHisName)); // "醜悪な 入力値(セクシャルヴァイオレット#1)"
  String anotherOneInputHisName = "Mr.\t Copy&Ｐａｓｔｅ\r\n";
  System.out.println(CjkNormalizr.normalize(anotherOneInputHisName)); // "Mr. Copy&Paste"
```

## License
Code is under the [Apache Licence v2](LICENCE).
