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
package nl.garvelink.iban;

import java.util.Comparator;

/**
 * An immutable value type representing an International Bank Account Number. Instances of this class have correct
 * check digits and a valid length for their country code. No country-specific validation is performed, other than
 * matching the length of the IBAN to its country code. Unknown country codes are not supported.
 * <p>
 * {@code IBAN} is assignment compatible with {@link LenientIBAN}, but not the other way around. In other words,
 * every valid {@code IBAN} is also a valid {@code LenientIBAN}.
 * </p>
 * @author Barend Garvelink (barend@garvelink.nl) https://github.com/barend
 */
public final class IBAN extends LenientIBAN {

    /**
     * A comparator that puts IBAN's into lexicographic ordering, per {@link String#compareTo(String)}.
     */
    public static final Comparator<IBAN> LEXICAL_ORDER = new Comparator<IBAN>() {
        @Override public int compare(IBAN iban, IBAN iban2) {
            return iban.value.compareTo(iban2.value);
        }
    };

    /**
     * Initializing constructor.
     * @param value the IBAN value, without any white space.
     * @throws IllegalArgumentException if the input is null, malformed or otherwise fails validation.
     */
    private IBAN(String value) {
        super(value);
        if (value.length() < 15) {
            throw new IllegalArgumentException("Length is too short to be an IBAN");
        }
        final int expectedLength = CountryCodes.getLengthForCountryCode(value.substring(0, 2));
        if (expectedLength < 0) {
            throw new UnknownCountryCodeException(value);
        }
        if (expectedLength != value.length()) {
            throw new WrongLengthException(value, expectedLength);
        }
        final int calculatedChecksum = Modulo97.checksum(value);
        if (calculatedChecksum != 1) {
            throw new WrongChecksumException(value);
        }
    }

    /**
     * Parses the given string into an IBAN object and confirms the check digits.
     * @param input the input, which can be either plain ("CC11ABCD123...") or formatted with (ASCII 0x20) space characters ("CC11 ABCD 123. ..").
     * @return the parsed and validated IBAN object, never null.
     * @throws IllegalArgumentException if the input is null, malformed or otherwise fails validation.
     * @see #valueOf(String)
     */
    public static IBAN parse(String input) {
        if (input == null || input.length() == 0) {
            throw new IllegalArgumentException("Input is null or empty string.");
        }
        if (!isLetterOrDigit(input.charAt(0)) || !isLetterOrDigit(input.charAt(input.length() - 1))) {
            throw new IllegalArgumentException("Input begins or ends in an invalid character.");
        }
        final boolean containsInternalSpaces = input.indexOf(' ') >= 0;
        if (containsInternalSpaces) {
            return new IBAN(input.replaceAll(" ", ""));
        }
        return new IBAN(input);
    }

    /**
     * Parses the given string into an IBAN object and confirms the check digits, but returns null for null.
     * @param input the input, which can be either plain ("CC11ABCD123...") or formatted ("CC11 ABCD 123. ..").
     * @return the parsed and validated IBAN object, or null.
     * @throws IllegalArgumentException if the input is malformed or otherwise fails validation.
     * @see #parse(String)
     */
    public static IBAN valueOf(String input) {
        if (input == null) {
            return null;
        }
        return parse(input);
    }

    @Override
    public boolean isValid() {
        return true;
    }

    @Override
    public boolean isCountryCodeKnown() {
        return true;
    }

    @Override
    public boolean isCorrectLength() {
        return true;
    }

    @Override
    public boolean isChecksumValid() {
        return true;
    }

    /**
     * @deprecated invoke {@link CountryCodes#getLengthForCountryCode(String)} instead.
     */
    @Deprecated
    public static int getLengthForCountryCode(String countryCode) {
        return CountryCodes.getLengthForCountryCode(countryCode);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof IBAN)) return false;
        return value.equals(((IBAN) o).value);
    }

    @Override
    public int hashCode() {
        return value.hashCode();
    }
}
