# text-commonize

[![Build Status](https://travis-ci.org/furplag/text-commonize.svg?branch=master)](https://travis-ci.org/furplag/text-commonize)
[![Coverage Status](https://coveralls.io/repos/github/furplag/text-commonize/badge.svg?branch=master)](https://coveralls.io/github/furplag/text-commonize?branch=master)

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
    <version>[1.0,)</version>
  </dependency>
</dependencies>
```

## License
Code is under the [Apache Licence v2](LICENCE).
