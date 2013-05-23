An immutable value type for IBAN numbers.

## Usage

```java
    // Obtain an instance of IBAN.
    IBAN iban = IBAN.valueOf( "NL91ABNA0417164300" );

    // Input may be formatted.
    iban = IBAN.valueOf( "NL91 ABNA 0417 1643 00" );

    // The valueOf() method returns null if its argument is null.
    iban.valueOf( null );

    // The parse() method throws an exception if its argument is null.
    iban.parse( null ); // Kaboom.

    // IBAN does not implement Comparable<T>, but a simple Comparator is provided.
    List<IBAN> ibans = getListOfIBANs();
    Collections.sort( ibans, IBAN.LEXICAL_ORDER );

    // You can use the Modulo97 class directly to compute or verify the check digits.
    String candidate = "NL91ABNA0417164300";
    Modulo97.verifyCheckDigits( candidate );

    // API methods take CharSequence, not just String.
    StringBuilder builder = new StringBuilder( "NL00ABNA0417164300" );
    int checkDigits = Modulo97.calculateCheckDigits( builder );
```

## References

 * http://en.wikipedia.org/wiki/IBAN
 * http://www.ecbs.org/iban.htm

## Copyright and License

Copyright 2013 Barend Garvelink

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
