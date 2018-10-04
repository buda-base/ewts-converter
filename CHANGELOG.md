# Change Log
All notable changes to this project will be documented in this file.
This project adheres to [Semantic Versioning](http://semver.org/). It follows [some conventions](http://keepachangelog.com/).

## [1.4.1] - 2018-10-04
### Fixed
- fixes in ewts to ala-lc conversion

## [1.4.0] - 2018-09-25
### Added
- support for conversion from ewts to ala-lc

## [1.3.0] - 2017-12-28
### Added
- support for alalc transliteration scheme and DTS (not publicly documented)
- sloppy mode now supports various non-ascii apostrophes

#### Changed
- 0x2018 and 0x2019 are now invalid in non-sloppy mode
- sloppy mode should be a little bit faster

## [1.2.0] - 2017-09-01
### Added
- reasonable replacement for `x`, `X` and `...` used to denote unreadable part in BDRC data.

## [1.1.0] - 2017-04-14
### Added
- Maven packaging

### Fixed
- complete the list of ambiguous syllables with:
	-  `'bs` -> `'bas`
	-  `mgs` -> `mags`
- add the following decompositions when reading Unicode:
	- `\u0F75` -> `\u0F71\u0F74`
	- `\u0F73` -> `\u0F71\u0F72`

### Changed
- renaming Class to `io.bdrc.ewtsconverter.EwtsConverter`
- when `fix_spacing` is on, lower case `P`, `K`, `G`, `C`, `B`, `L`, `M`, `S` (but not `Sh`)
- when `fix_spacing` is on, replace common EWTS mistakes:
	- ` b ` -> ` ba `
	- ` m ` -> ` ma `
	- ` m'i ` -> ` ma'i `
	- ` b'i ` -> ` ba'i `
- do not add a warning when writing affixed as `'m` or `'ng` (as in `pa'm`)
- add a warning if the resulting string starts with a combining character (for XML 1.1 validation)
