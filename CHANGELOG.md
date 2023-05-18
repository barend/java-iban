# Version History

## 1.14.0: unreleased

* Fixes exception when composing IBAN with check digit under 10 ([#92][i92]).
* Update to revision 94 of the SWIFT IBAN Registry
    * Mongolia (MN): added
    * Nicaragua (NI): added, note that this entry was previously in the
      experimental list, and the IBAN length has changed.
    * Somalia (SO): added
* Update to IBAN.com Experimental List
    * Nicaragua (NI): removed

[i92]: https://github.com/barend/java-iban/issues/92

## 1.13.0: 16 September 2022

* All exceptions thrown now extend `IBANException` ([#17][i17])
* No changes to country data

[i17]: https://github.com/barend/java-iban/issues/17

## 1.12.0: 27 May 2022

* Update to revision 92 of the SWIFT IBAN Registry
    * Djibouti (DJ): added
    * Russia (RU): added
    * Sudan (SU): remove incorrect branch identifier field
* Update to IBAN.com Experimental List
    * Djibouti (DJ): removed

## 1.11.0: 26 November 2021

* Update to revision 91 of the SWIFT IBAN Registry
    * Burundi (BI): added
* Update to IBAN.com Experimental List
    * Burundi (BI): removed

## 1.10.1: 20 August 2021

* Update to revision 90 of the SWIFT IBAN Registry
    * Sudan (SD): added ([#42][i42]).
* Update to IBAN.com Experimental List
    * No changes
* Drop `template-maven-plugin`. It has proven to make the CI build very flaky. Removing this also lets us remove the
  third-party artifact repository from the pom ([#33][i33]), simplifying the supply chain. It also makes the build a
  little quicker. The downside is that the build now requires Python 3 and a bourne shell. It should build on WSL2 just
  fine, but there is no pom profile to support Windows native builds.

(1.10.0 unreleased; packaging mistake)

[i33]: https://github.com/barend/java-iban/issues/33
[i42]: https://github.com/barend/java-iban/issues/42

## 1.9.0: 3 April 2021

* Compatible change: utility functions in `CountryCodes` now accept `java.lang.CharSequence` (was String).
* New API method: `IBAN.compose(CharSequence, CharSequence)`.
* New API method: `Modulo97.calculateCheckDigits(CharSequence, CharSequence)`.
* France (FR): add branch identifier ([#30][i30])
* Update to revision 89 of the SWIFT IBAN Registry
    * Andorra (AD): is now SEPA
* Update to IBAN.com Experimental List
    * No changes
* The project can now be compilead on Adopt-OpenJDK 11 HS. An outdated library used in the code generation step
  prevented this.
* The `@javax.annotation.Generated` annotation has been removed from the `CountryCodesData` class. This annotation moved
  into a library package in newer Java versions, and does not justify taking on a library dependency.

[i30]: https://github.com/barend/java-iban/issues/30

## 1.8.0: 21 November 2020

* The `IBAN` class implements `java.io.Serializable` ([#23][i23]). The serialized form should stay valid across library
  version updates. There is one obvious backwards-incompatibility: deserializing after a version downgrade, of an IBAN
  whose country only exists in the newer version, will fail.
* No updates to reference data.

[i23]:https://github.com/barend/java-iban/issues/23

## 1.7.0: 13 October 2020

* Packaging updated to support Java module system, see `docs/java-module.md`.
* Update to revision 88 of the SWIFT IBAN Registry
    * Albania (AL): bank identifier extended to 8 chars
    * Egypt (EG): now in SWIFT registry; add embedded bank and branch identifier
    * Iceland (IS): bank and branch identifier split
    * Libya (LY): added
    * Poland (PL): bank identifier changed to branch identifier
    * Seychelles (SC): bank and branch identifier split
* Update to IBAN.com Experimental List
    * No changes.
* Exception classes define `serialVersionUID`.
* Uses build-time code generation to construct the reference data. The input YAML file is included in the distribution
  JAR for completeness, but it is not used at runtime.
* Add `CountryCodes.getLastUpdateDate()` and `CountryCodes.getLastUpdateRevision()`.
* Add spotbugs:check to the build definition.
* Moved some private fields. Any code that accesses these through reflection will break.

## 1.6.1: 20 September 2019

* "Bad input" exception messages no longer echo the input ([#14][i14]).
* No changes to IBAN formats

[i14]:https://github.com/barend/java-iban/issues/14

## 1.6.0: 23 August 2019

* Update to version 83 of the IBAN registry
    * Adds Vatican City State (VA)
    * (Ignores updated example IBAN for Sao Tome e Principe, because it fails checksum validation)
* Imports IBANs from the Experimental IBANs List on iban.com, update of 12 July 2019 (Closes issue #9)
    * IBAN length for Algeria (DZ) changed to 26
    * IBAN length for Burkina Faso (BF) changed to 26
    * Adds Central African Republic (CF), Chad (TD), Comoros (KM), Djibouti (DJ), Equatorial Guinea (GQ),
      Guinea-Bissau (GW), Honduras (HN), Morocco (MA), Niger (NE), Nicaragua (NI), Togo (TG)
* New method `IBAN.isInSwiftRegistry()` indicates whether the IBAN country is listed in the SWIFT IBAN Registry
* Factory methods in `IBAN` accept `CharSequence` instead of `String`
* Upgrades to Java bytecode level 1.8. The 1.6 target was there to offer legacy Android support in 2013. This is no
  longer required in 2019.
* Drops the Sonatype OSS-parent POM (Closes issue #6)

## 1.5.1: 12 September 2017

* Change project URL to github.io, because I can't offer HTTPS with the CNAME.
* Update to version 78 of the IBAN registry
    * No changes to IBAN formats

## 1.5: 26 February 2017

* Update to version 75 of the IBAN registry
    * Decodes Bank and Branch ID for Bulgaria (BG)
    * Decodes Bank and Branch ID for Brazil (BR)
    * Adds Republic of Belarus (BY)
    * Increments IBAN length for Costa Rica (CR) from 21 to 22
    * Adds Iraq (IQ)
    * Adds El Salvador (SV)
    * Decodes Bank ID for Kosovo (XK) as 2 characters instead of 4
* Version Notes:
    * CR and IQ contributed in PR#8 by firehooper.
    * Several country names `CountryCodesParameterizedTest` were updated to reflect the IBAN registry.

## 1.4: 4 May 2016

* Update to version 66 of the IBAN registry
    * Adds Sao Tome e Principe (ST)
    * Adds bank identifier format for Ukraine (UA)
    * Adds Seychelles (SC)

## 1.3: 5 July 2015

* Update to version 58 of the IBAN registry
    * Sets SEPA flag for San Marino
    * Adds length validation for Saint Lucia
* Adds ability to extract Bank Identifier and Branch Identifier from an IBAN (issue [#5][i5]), if available. You can do
  so using the static methods in `IBANFields` (returns JDK8 `Optional`s) and `IBANFieldsCompat` (returns nulls). There
  are three countries where I had to interpret the spec in some way:
    * Finland (FI) – The spec mentions both "Not in use" and "Position 1-3 indicate the bank or banking group." I have
      taken "bank or banking group" to be more or less synonymous with Bank Identifier and return it as such.
    * Slovenia (SI) – The five digits following the checksum encode the financial institution and sub-encode the branch
      identifier if applicable, depending on the type of financial institution. The library returns all five digits as the
      bank identifier and never returns a branch identifier.
    * Republic of Kosovo (XK) – The four digits following the checksum encode the Bank ID, and the last two of these four
      sub-encode the branch ID. The library returns all four digits as the bank identifier. For example: if the IBAN has
      "1234" in these positions, then the bank identifier is returned as "1234" and the branch identifier as "34".

[i5]: https://github.com/barend/java-iban/issues/5

## 1.2: 1 September 2014

* Adds query method `IBAN.isSEPA()`.
* Merges [pull request #4][pr4] by Matthias Vill, adding some hooks to aid interactive input validation & formatting.
* Update to version 50 of the IBAN registry
    * Adds length validation rules for: Jordan, Quatar, Republic of Kosovo, Timor-Leste

[pr4]: https://github.com/barend/java-iban/pull/4

## 1.1: 25 October 2013

* Moves country information from `IBAN` into separate class `CountryCodes`
* The known country codes can be obtained from `CountryCodes.getKnownCountryCodes()`.
* The method `getLengthForCountryCode()` in `IBAN` is now deprecated, having moved into `CountryCodes`.
* Adds length validation rules for:
    * Albania, Algeria, Angola, Azerbaijan, Bahrein, Benin, Brazil, British Virgin Islands, Burkina Faso,
      Burundi, Cameroon, Cape Verde, Congo, Costa Rica, Dominican Republic, Egypt, Faroe Islands, Gabon,
      Georgia, Greenland, Guatemala, Iran, Ivory Coast, Kazakhstan, Kuwait, Lebanon, Madagascar, Mali,
      Mauritania, Moldova, Mozambique, Pakistan, State of Palestine / Palestinian Territories, Saudi Arabia,
      Senegal, Tunisia, Ukraine, United Arab Emirates
    * **Note:** this list is not limited to SEPA countries or even the IBAN registry maintained by SWIFT. The
      `CountryCodesParameterizedTest.java` file documents the origin for each of these IBAN specs.

## 1.0: 30 May 2013

* Initial release, IBAN value type.
* Supported countries:
    * Andorra, Austria, Belgium, Bosnia and Herzegovinia, Bulgaria, Croatia, Cyprus, Czech Republic,
      Denmark, Estonia, Finland, France, Germany, Gibraltar, Greece, Hungary, Iceland, Ireland,
      Israel, Italy, Latvia, Liechtenstein, Lithuania, Luxembourg, Macedonia, Malta, Mauritius, Monaco,
      Montenegro, Netherlands, Norway, Poland, Portugal, Romania, San Marino, Serbia, Slovakia, Slovenia,
      Spain, Sweden, Switzerland, Turkey, United Kingdom
