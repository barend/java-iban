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

[![Build](https://github.com/barend/java-iban/actions/workflows/main.yml/badge.svg)](https://github.com/barend/java-iban/actions/workflows/main.yml) [![CodeQL](https://github.com/barend/java-iban/actions/workflows/codeql-analysis.yml/badge.svg)](https://github.com/barend/java-iban/actions/workflows/codeql-analysis.yml)

### Installation

Grab a package [from Github][download] or get it from Maven Central:

#### Maven

```xml
    <dependency>
        <groupId>nl.garvelink.oss</groupId>
        <artifactId>iban</artifactId>
        <version>1.12.0</version>
    </dependency>
```

#### Gradle

```groovy
    dependencies {
        compile 'nl.garvelink.oss:iban:1.12.0'
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

    // Compose the IBAN for a country and BBAN
    IBAN.compose( "BI", "201011067444" ); // BI43201011067444

    // You can query whether an IBAN is of a SEPA-participating country
    boolean isSepa = IBAN.parse(candidate).isSEPA(); // true

    // You can query whether an IBAN is in the SWIFT Registry
    boolean isRegistered = IBAN.parse(candidate).isInSwiftRegistry(); // true

    // Modulo97 API methods take CharSequence, not just String.
    StringBuilder builder = new StringBuilder( "LU000019400644750000" );
    int checkDigits = Modulo97.calculateCheckDigits( builder ); // 28

    // Modulo97 API can calculate check digits, also for non-iban inputs.
    // It does assume/require that the check digits are on indices 2 and 3.
    Modulo97.calculateCheckDigits( "GB", "NWBK60161331926819" ); // 29
    Modulo97.calculateCheckDigits( "XX", "X" ); // 50

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

## 1.12.0: 27 May 2022

* Update to revision 92 of the SWIFT IBAN Registry
    * Djibouti (DJ): added
    * Russia (RU): added
    * Sudan (SU): remove incorrect branch identifier field
* Update to IBAN.com Experimental List
    * Djibouti (DJ): removed

#### 1.11.0: 26 November 2021

* Update to revision 91 of the SWIFT IBAN Registry
    * Burundi (BI): added
* Update to IBAN.com Experimental List
    * Burundi (BI): removed

#### Earlier versions

See [CHANGELOG](./CHANGELOG.md) for prior releases.

### Design Choices

I like the Joda-Time library and I try to follow the same design principles. I'm explicitly targetting Android, which
at the time this library started was still on Java 1.6. I'm trying to keep the library as simple as I can.

* Easy to integrate: don't bring transitive dependencies.
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

Copyright 2013–2022 Barend Garvelink

```none
   Licensed under the Apache License, Version 2.0 (the "License");
   you may not use this file except in compliance with the License.
   You may obtain a copy of the License at

       https://www.apache.org/licenses/LICENSE-2.0

   Unless required by applicable law or agreed to in writing, software
   distributed under the License is distributed on an "AS IS" BASIS,
   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   See the License for the specific language governing permissions and
   limitations under the License.
```
