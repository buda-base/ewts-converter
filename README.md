# Ewts Converter

This Java package implements the conversion between Unicode Tibetan text, and [Extended Wylie transliteration (EWTS)](http://www.thlib.org/reference/transliteration/#essay=/thl/ewts/). It also has convenience conversion methods from Diacritics Transliteration Scheme (DTS) and [ALA-LC romanization](https://www.loc.gov/catdir/cpso/romanization/tibetan.pdf) to EWTS, and from EWTS to ALA-LC romanization.

It is based on the equivalent Perl module, [Lingua::BO::Converter](http://www.digitaltibetan.org/cgi-bin/wylie.pl).

See [Change log](CHANGELOG.md) for version notes.

## Installation

Using maven:

```xml
    <dependency>
      <groupId>io.bdrc.ewtsconverter</groupId>
      <artifactId>ewts-converter</artifactId>
      <version>1.4.0</version>
    </dependency>
```

## Building

We provide one maven option: `-DperformRelease=true`, which will make the jar file gpg-signed.

## Use

```java
import io.bdrc.ewtsconverter.EwtsConverter;

EwtsConverter wl = new EwtsConverter();
System.out.println(wl.toUnicode("sems can thams cad"));
System.out.println(wl.toWylie("\u0f66\u0f44\u0f66\u0f0b\u0f62\u0f92\u0fb1\u0f66\u000a"));
```

### Options

You can pass some options to the constructor:

```java
EwtsConverter(boolean check, boolean check_strict, boolean print_warnings, boolean fix_spacing, Mode mode)
```

- `check`: generate warnings for illegal consonant sequences; default is `true`.
- `check_strict`: stricter checking, examine the whole stack; default is `true`.
- `print_warnings`: print generated warnings to `System.out`; default is `false`.
- `fix_spacing`: remove spaces after newlines, collapse multiple tseks into one, fix case, etc; default is `true`.
- `mode`: an `EwtsConverter.Mode` value, one of `EWTS` (default), `ALALC` ([alalc transliteration scheme](https://www.loc.gov/catdir/cpso/romanization/tibetan.pdf)) or `DTS` (close to alalc, not publicly documented).

### API

#### Functions of the EwtsConverter object

##### String toUnicode(String wylie_string)

Converts from Converter (EWTS) to Unicode.

##### String toUnicode(String wylie_string, ArrayList<String> warns)

Converts from Converter (EWTS) to Unicode; puts the generated warnings in the list.

##### String toWylie(String unicode_string)

Converts from Unicode to Converter. Anything that is not Tibetan Unicode is converted to EWTS comment blocks [between brackets].

##### String toWylie(String unicode_string, ArrayList<String> warns, boolean escape)

Converts from Unicode to Converter. Puts the generated warnings in the list. If escape is false, anything that is not Tibetan Unicode is just passed through as it is.

#### Static functions of the EwtsConverter class

##### String normalizeSloppyWylie(String str)

Returns a string normalizing common errors in EWTS.

##### boolean isCombining(char c)

Returns `true` if the character is a Tibetan combining character.

#### Static functions of the TransConverter class

##### String dtsToEwts(String dtsString)

Converts a string from DTS to EWTS.

##### String alalcToEwts(String alalcStr)

Converts a string from ALA-LC to EWTS.

##### String ewtsToAlalc(String ewtsStr, boolean sloppy)

Converts a string from EWTS to ALA-LC (in NFKD, lower-case). If sloppy is `true`, also normalizes common errors in EWTS.

### Performance and Concurrency

This code should perform quite decently.  When converting from Ewts to
Unicode, the entire string is split into tokens, which are themselves
strings.  If this takes too much memory, consider converting your text in
smaller chunks.  With today's computers, it should not be a problem to
convert several megabytes of tibetan text in one call.  Otherwise, it could
be worthwhile to tokenize the input on the fly, rather than all at once.

This class is entirely thread-safe.  In a multi-threaded environment,
multiple threads can share the same instance without any problems.

## License

For simplicity reasons, we distribute our modifications only under the [Apache 2.0 License](LICENSE), but the original version had this statement:

```txt
This library is Free Software.  You can redistribute it or modify it, under
the terms of, at your choice, the GNU General Public License (version 2 or
higher), the GNU Lesser General Public License (version 2 or higher), the
Mozilla Public License (any version) or the Apache License version 2 or
higher.

Please contact the author if you wish to use it under some terms not covered
here.
```
