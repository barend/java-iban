/*
   Copyright 2013 Barend Garvelink

   Licensed under the Apache License, Version 2.0 (the "License");
   you may not use this file except in compliance with the License.
   You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

   Unless required by applicable law or agreed to in writing, software
   distributed under the License is distributed on an "AS IS" BASIS,
   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   See the License for the specific language governing permissions and
   limitations under the License.
 */

/**
 * This package defines utilities for dealing with IBAN numbers. Its main entity is {@link nl.garvelink.iban.IBAN}, an
 * immutable value type representing an IBAN number. The Modulo-97 checksum is implemented in
 * {@link nl.garvelink.iban.Modulo97}, which can be used directly if needed. The code in this package
 * throws {@code IllegalArgumentException} on invalid input. Methods don't take {@code null} arguments unless otherwise
 * noted.
 * <p>Usage sample</p>
 * <pre>
 * // Obtain an instance of IBAN.
 * IBAN iban = IBAN.valueOf( "NL91ABNA0417164300" );
 *
 * // toString() emits standard formatting, toPlainString() is compact.
 * String s = iban.toString(); // "NL91 ABNA 0417 1643 00"
 * String p = iban.toPlainString(); // "NL91ABNA0417164300"
 *
 * // Input may be formatted.
 * iban = IBAN.valueOf( "BE68 5390 0754 7034" );
 *
 * // The valueOf() method returns null if its argument is null.
 * iban.valueOf( null ); // null
 *
 * // The parse() method throws an exception if its argument is null.
 * iban.parse( null ); // IllegalArgumentException
 *
 * // IBAN does not implement Comparable<T>, but a simple Comparator is provided.
 * List<IBAN> ibans = getListOfIBANs();
 * Collections.sort( ibans, IBAN.LEXICAL_ORDER );
 *
 * // The equals() and hashCode() methods are implemented.
 * Map<IBAN, String> ibansAsKeys = Maps.newHashMap();
 * ibansAsKeys.put( iban, "this is fine" );
 *
 * // You can use the Modulo97 class directly to compute or verify the check digits on an input.
 * String candidate = "GB29 NWBK 6016 1331 9268 19";
 * boolean valid = Modulo97.verifyCheckDigits( candidate ); // true
 *
 * // Modulo97 API methods take CharSequence, not just String.
 * StringBuilder builder = new StringBuilder( "LU000019400644750000" );
 * int checkDigits = Modulo97.calculateCheckDigits( builder ); // 28
 *
 * // Get the expected IBAN length for a country code:
 *  int length = IBAN.getLengthForCountryCode( "DK" );
 * </pre>
 *
 * <p>Copyright 2013 Barend Garvelink. This code can be used under the terms of the Apache License, Version 2.0.
 * Attribution is appreciated, but not required.</p>
 */
package nl.garvelink.iban;
