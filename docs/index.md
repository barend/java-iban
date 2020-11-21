A small Java library for dealing with International Bank Account Numbers (IBANs).

The `IBAN` class is intended for use in your domain types. `IBAN` objects enforce that their value is the correct length
for its country code and that it passes checksum validation. The `Modulo97` class exposes the checksum validation code
for other purposes, such as live input validation.

The library is compatible for use in Android apps. It is in maintenance mode; I'll occasionally update it to the latest
version of the IBAN registry, but I don't plan on developing any new features.

* [Installation](#Installation)
* [Usage Examples](#Use)
* [Version History](#Version-History)
* [Design Choices](#Design-Choices)
* [References](#References)
* [Alternatives](#Alternatives)
* [Copyright and License](#Copyright-and-License)

### Installation

Grab a package [from Github][download] or get it from Maven Central:

#### Maven

```xml
    <dependency>
        <groupId>nl.garvelink.oss</groupId>
        <artifactId>iban</artifactId>
        <version>1.8.0</version>
    </dependency>
```

#### Gradle

```groovy
    dependencies {
        compile 'nl.garvelink.oss:iban:1.8.0'
    }
```

[download]: https://github.com/barend/java-iban/releases

### Use

Obtain an `IBAN` instance using one of the static factory methods: `valueOf( )` and `parse( )`. Methods throw
`java.lang.IllegalArgumentException` on invalid input.

```java
    // Obtain an instance of IBAN.
    IBAN iban = IBAN.valueOf( "NL91ABNA0417164300" );

    // toString() emits standard formatting, toPlainString() is compact.
    String s = iban.toString(); // "NL91 ABNA 0417 1643 00"
    String p = iban.toPlainString(); // "NL91ABNA0417164300"

    // Input may be formatted.
    iban = IBAN.valueOf( "BE68 5390 0754 7034" );

    // The valueOf() method returns null if its argument is null.
    IBAN.valueOf( null ); // null

    // The parse() method throws an exception if its argument is null.
    IBAN.parse( null ); // IllegalArgumentException

    // IBAN does not implement Comparable<T>, but a simple Comparator is provided.
    List<IBAN> ibans = getListOfIBANs();
    Collections.sort( ibans, IBAN.LEXICAL_ORDER );

    // The equals() and hashCode() methods are implemented.
    Map<IBAN, String> ibansAsKeys = Maps.newHashMap();
    ibansAsKeys.put( iban, "this is fine" );

    // You can use the Modulo97 class directly to compute or verify the check digits on an input.
    String candidate = "GB29 NWBK 6016 1331 9268 19";
    boolean valid = Modulo97.verifyCheckDigits( candidate ); // true

    // You can query whether an IBAN is of a SEPA-participating country
    boolean isSepa = IBAN.parse(candidate).isSEPA(); // true

    // You can query whether an IBAN is in the SWIFT Registry
    boolean isRegistered = IBAN.parse(candidate).isInSwiftRegistry(); // true

    // Modulo97 API methods take CharSequence, not just String.
    StringBuilder builder = new StringBuilder( "LU000019400644750000" );
    int checkDigits = Modulo97.calculateCheckDigits( builder ); // 28

    // Get the expected IBAN length for a country code:
    int length = CountryCodes.getLengthForCountryCode( "DK" );

    // Get the Bank Identifier and Branch Identifier (JDK 8):
    Optional<String> bankId = IBANFields.getBankIdentifier( iban );
    Optional<String> branchId = IBANFields.getBranchIdentifier( iban );

    // Get the Bank Identifier and Branch Identifier (pre-JDK 8):
    String bankId = IBANFieldsCompat.getBankIdentifier( iban );
    String branchId = IBANFieldsCompat.getBranchIdentifier( iban );
```

### Version History

#### 1.8.0: 21 November 2020
* The `IBAN` class implements `java.io.Serializable` ([#23][i23]). The serialized form should stay valid across library
  version updates. There is one obvious backwards-incompatibility: deserializing after a version downgrade, of an IBAN
  whose country only exists in the newer version, will fail.
* No updates to reference data.

[i23]:https://github.com/barend/java-iban/issues/23

#### 1.7.0: 13 October 2020
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

#### 1.6.1: 20 September 2019
* "Bad input" exception messages no longer echo the input ([#14][i14]).
* No changes to IBAN formats

[i14]:https://github.com/barend/java-iban/issues/14

#### 1.6.0: 23 August 2019
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

#### 1.5.1: 12 September 2017

* Change project URL to github.io, because I can't offer HTTPS with the CNAME.
* Update to version 78 of the IBAN registry
  * No changes to IBAN formats

#### 1.5: 26 February 2017

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

#### 1.4: 4 May 2016

* Update to version 66 of the IBAN registry
  * Adds Sao Tome e Principe (ST)
  * Adds bank identifier format for Ukraine (UA)
  * Adds Seychelles (SC)

#### 1.3: 5 July 2015

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

#### 1.2: 1 September 2014

* Adds query method `IBAN.isSEPA()`.
* Merges [pull request #4][pr4] by Matthias Vill, adding some hooks to aid interactive input validation & formatting.
* Update to version 50 of the IBAN registry
  * Adds length validation rules for: Jordan, Quatar, Republic of Kosovo, Timor-Leste

[pr4]: https://github.com/barend/java-iban/pull/4

#### 1.1: 25 October 2013

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

#### 1.0: 30 May 2013

* Initial release, IBAN value type.
* Supported countries:
  * Andorra, Austria, Belgium, Bosnia and Herzegovinia, Bulgaria, Croatia, Cyprus, Czech Republic,
    Denmark, Estonia, Finland, France, Germany, Gibraltar, Greece, Hungary, Iceland, Ireland,
    Israel, Italy, Latvia, Liechtenstein, Lithuania, Luxembourg, Macedonia, Malta, Mauritius, Monaco,
    Montenegro, Netherlands, Norway, Poland, Portugal, Romania, San Marino, Serbia, Slovakia, Slovenia,
    Spain, Sweden, Switzerland, Turkey, United Kingdom

### Design Choices

I like the Joda-Time library and I try to follow the same design principles. I'm explicitly targetting Android, which
rules out some modern Java language constructs. I'm trying to keep the library as simple as I can.

* The `IBAN` objects are immutable and the IBAN therein is non-empty and valid. There is no support for partial or
  invalid IBANs. Note that "valid" isn't as strict as it could be:
  * It checks that the length is correct (varies per country) and that the check digits are correct.
  * The national format mask (such as `QA2!n4!a21!c`) is not enforced. This seems to me like more work than necessary.
    The modulo-97 checksum catches most input errors anyway, and I don't want to force a memory-hungry regex check onto
    Android users. Speaking of Android, this mask could be used for keyboard switching on an `IBANEditText`, but that's
    for a different open-source project.
  * Any national check digits are not enforced. Doing this right is more work than I want to put into this. I lack the
    country-specific knowledge of all the gotchas and intricacies. If other countries' check digits are anything like
    those in the Netherlands, they're going to differ by Bank Identifier.
* There is no way to configure extra restrictions such as "only SEPA countries" on the `IBAN.valueOf()` method. This, to
  me, would look too much like Joda-Time's pluggable `Chronology` system, which leads to <acronym title="Principle of
  Least Surprise">PoLS</acronym> violations (background: [Why JSR-310 isn't Joda-Time][wjij]).
* There is no class to represent a partially entered IBAN or a potentially-invalid IBAN. I'm sure there are use cases
  where you want to shift this sort of data around. As far as this library is concerned, if it's not an IBAN it's just a
  string, and there already exist data types for dealing with those.
* Any feature that's not present in _all_ IBAN's is kept outside the `IBAN` class. Currently, that's the support for
  extracting Bank and Branch identifiers, which lives in the `IBANFields` and `IBANFieldsCompat` classes.
* The library originally supported an SDK 14 (Ice Cream Sandwich) era Android app. This is why it relies on bit-packing
  to reduce bytecode size and why there's a pre-JDK8 API.
* IBAN instances implement `java.io.Serializable`. When deserializing, they do the same validity checks as during
  construction. This means that any object that goes in valid, should come out valid, but it doesn't protect against
  willful tampering. **Caution:** an IBAN encoded by Java serialization is about five times the size (in bytes) of its
  cleartext form in UTF-8. The canonical string format is the preferred way to transmit an IBAN object.

[wjij]: https://blog.joda.org/2009/11/why-jsr-310-isn-joda-time_4941.html

### References

 * **SWIFT IBAN Registry**
   https://www.swift.com/sites/default/files/resources/iban_registry.pdf
 * **SEPA Participants**
   https://www.europeanpaymentscouncil.eu/document-library/other/epc-list-sepa-scheme-countries
 * **Experimental IBANs**
   https://www.iban.com/structure
 * **General Information**
   http://en.wikipedia.org/wiki/IBAN

### Alternatives

If you're looking for a more comprehensive IBAN library, you may prefer [iban4j][iban4j].

[iban4j]:https://github.com/arturmkrtchyan/iban4j

### Copyright and License

Copyright 2020 Barend Garvelink

```none
   Licensed under the Apache License, Version 2.0 (the "License");
   you may not use this file except in compliance with the License.
   You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

   Unless required by applicable law or agreed to in writing, software
   distributed under the License is distributed on an "AS IS" BASIS,
   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   See the License for the specific language governing permissions and
   limitations under the License.
```
