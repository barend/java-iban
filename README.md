A small Java library for dealing with International Bank Account Numbers (IBANs).

The `IBAN` class is intended for use in your domain types. `IBAN` objects enforce that their value is the correct length
for its country code and that it passes checksum validation. The `Modulo97` class exposes the checksum validation code
for other purposes, such as live input validation.

The library is compatible for use in Android apps.

### Install

Grab a package [from Github][download] or get it from Maven Central:

#### Maven

```xml
    <dependency>
        <groupId>nl.garvelink.oss</groupId>
        <artifactId>iban</artifactId>
        <version>1.2.0</version>
    </dependency>
```

#### Gradle

```javascript
    dependencies {
        compile 'nl.garvelink.oss:iban:1.2.0'
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
    iban.valueOf( null ); // null

    // The parse() method throws an exception if its argument is null.
    iban.parse( null ); // IllegalArgumentException

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
    boolean isSepa = candidate.isSEPA(); // true

    // Modulo97 API methods take CharSequence, not just String.
    StringBuilder builder = new StringBuilder( "LU000019400644750000" );
    int checkDigits = Modulo97.calculateCheckDigits( builder ); // 28

    // Get the expected IBAN length for a country code:
    int length = CountryCodes.getLengthForCountryCode( "DK" );
```

### Notes

* The length of an IBAN is checked against the required length for its country code. Other country-specific
  validation (e.g. national check digits) is absent.
* The `IBAN` class does not implement `Serializable`, because the string representation is a superior serialized form.

### Version history

#### 1.3: unreleased

* Update to version 58 of the IBAN registry
  * Sets SEPA flag for San Marino
  * Adds length validation for Saint Lucia

#### 1.2: September 1st, 2014

* Adds query method `IBAN.isSEPA()`.
* Merges [pull request #4][pr4] by Matthias Vill, adding some hooks to aid interactive input validation & formatting.
* Update to version 50 of the IBAN registry
  * Adds length validation rules for: Jordan, Quatar, Republic of Kosovo, Timor-Leste

[pr4]: https://github.com/barend/java-iban/pull/4

#### 1.1: October 25th, 2013

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

#### 1.0: May 30, 2013

* Initial release, IBAN value type.
* Supported countries:
  * Andorra, Austria, Belgium, Bosnia and Herzegovinia, Bulgaria, Croatia, Cyprus, Czech Republic,
    Denmark, Estonia, Finland, France, Germany, Gibraltar, Greece, Hungary, Iceland, Ireland,
    Israel, Italy, Latvia, Liechtenstein, Lithuania, Luxembourg, Macedonia, Malta, Mauritius, Monaco,
    Montenegro, Netherlands, Norway, Poland, Portugal, Romania, San Marino, Serbia, Slovakia, Slovenia,
    Spain, Sweden, Switzerland, Turkey, United Kingdom

### References

 * http://www.swift.com/dsp/resources/documents/IBAN_Registry.pdf
 * http://en.wikipedia.org/wiki/IBAN
 * http://www.ecbs.org/iban.htm
 * http://www.tbg5-finance.org/ibanabout.shtml
 * http://www.nordea.com/Our+services/International+products+and+services/Cash+Management/IBAN+countries/908462.html
 * http://www.europeanpaymentscouncil.eu/knowledge_bank_detail.cfm?documents_id=328

### Copyright and License

Copyright 2015 Barend Garvelink

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
