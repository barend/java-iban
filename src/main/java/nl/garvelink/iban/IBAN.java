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

import java.util.Arrays;
import java.util.Comparator;

/**
 * An immutable value object representing an International Bank Account Number. Instances of this class have correct
 * check digits and a valid length for their country code. No country-specific validation is performed, other than
 * matching the length of the IBAN to its country code. Unknown country codes are not supported.
 * @author Barend Garvelink (barend@garvelink.nl) https://github.com/barend
 */
public final class IBAN {

    /**
     * A comparator that puts IBAN's into lexical order, per {@link String#compareTo(String)}.
     */
    public static final Comparator<IBAN> LEXICAL_ORDER = new Comparator<IBAN>() {
        @Override public int compare(IBAN iban, IBAN iban2) {
            return iban.value.compareTo(iban2.value);
        }
    };

    /**
     * Known country codes, this list must be sorted to allow binary search.
     */
    private static final String[] COUNTRY_CODES = {
            "AD", "AT", "BA", "BE", "BG", "CH", "CY", "CZ", "DE", "DK", "EE", "ES", "FI", "FR", "GB", "GI", "GR",
            "HR", "HU", "IE", "IL", "IS", "IT", "LI", "LT", "LU", "LV", "MC", "ME", "MK", "MT", "MU", "NL", "NO",
            "PL", "PT", "RO", "RS", "SE", "SI", "SK", "SM", "TR"};
    /**
     * Lengths for each country's IBAN. The indices match the indices of {@link #COUNTRY_CODES}, the values are the expected length.
     */
    private static final int[] COUNTRY_IBAN_LENGTHS = {
            24 /* AD */, 20 /* AT */, 20 /* BA */, 16 /* BE */, 22 /* BG */, 21 /* CH */, 28 /* CY */, 24 /* CZ */,
            22 /* DE */, 18 /* DK */, 20 /* EE */, 24 /* ES */, 18 /* FI */, 27 /* FR */, 22 /* GB */, 23 /* GI */,
            27 /* GR */, 21 /* HR */, 28 /* HU */, 22 /* IE */, 23 /* IL */, 26 /* IS */, 27 /* IT */, 21 /* LI */,
            20 /* LT */, 20 /* LU */, 21 /* LV */, 27 /* MC */, 22 /* ME */, 19 /* MK */, 31 /* MT */, 30 /* MU */,
            18 /* NL */, 15 /* NO */, 28 /* PL */, 25 /* PT */, 24 /* RO */, 22 /* RS */, 24 /* SE */, 19 /* SI */,
            24 /* SK */, 27 /* SM */, 26 /* TR */ };

    /**
     * IBAN value, normalized form (no whitespace).
     */
    private final String value;

    /**
     * Pretty-printed value.
     */
    private final String valuePretty;

    /**
     * Initializing constructor.
     * @param value the IBAN value, without any white space.
     * @throws IllegalArgumentException if the input is null, malformed or otherwise fails validation.
     */
    private IBAN(String value) {
        if (value == null) {
            throw new IllegalArgumentException("Input is null");
        }
        if (value.length() < 15) {
            throw new IllegalArgumentException("Length is too short to be an IBAN");
        }
        if (value.charAt(0) < 'A' || value.charAt(0) > 'Z' || value.charAt(1) < 'A' || value.charAt(1) > 'Z') {
            throw new IllegalArgumentException("Characters at index 0 and 1 not both uppercase letters.");
        }
        if (value.charAt(2) < '0' || value.charAt(2) > '9' || value.charAt(3) < '0' || value.charAt(3) > '9') {
            throw new IllegalArgumentException("Characters at index 2 and 3 not both numeric.");
        }
        if (!isLetterOrDigit(value.charAt(value.length() - 1))) {
            throw new IllegalArgumentException("Last character is not a letter or digit.");
        }
        final int ccIdx = Arrays.binarySearch(COUNTRY_CODES, value.substring(0, 2));
        if (ccIdx < 0) {
            throw new UnknownCountryCodeException(value);
        }
        if (COUNTRY_IBAN_LENGTHS[ccIdx] != value.length()) {
            throw new WrongLengthException(value, COUNTRY_IBAN_LENGTHS[ccIdx]);
        }
        final int calculatedChecksum = Modulo97.checksum(value);
        if (calculatedChecksum != 1) {
            throw new WrongChecksumException(value);
        }
        this.value = value;
        this.valuePretty = prettyPrint(value);
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

    /**
     * Returns the Country Code embedded in the IBAN.
     * @return the two-letter country code.
     */
    public String getCountryCode() {
        return value.substring(0, 2);
    }

    /**
     * Returns the check digits of the IBAN.
     * @return the two check digits.
     */
    public String getCheckDigits() {
        return value.substring(2, 4);
    }

    /**
     * Returns the IBAN without formatting.
     * @return the unformatted IBAN number.
     */
    public String toPlainString() {
        return value;
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

    /**
     * Returns the IBAN in standard formatting, with a space every four characters.
     * @return the formatted IBAN number.
     * @see #toPlainString()
     */
    @Override
    public String toString() {
        return valuePretty;
    }

    /**
     * Returns whether the given character is in the {@code A-Za-z0-9} range.
     * This differs from {@link Character.isLetterOrDigit(char)} because it doesn't understand non-Western characters.
     */
    private static final boolean isLetterOrDigit(char c) {
        return (c >= '0' && c <= '9')
            || (c >= 'A' && c <= 'Z')
            || (c >= 'a' && c <= 'z');
    }

    private static final String prettyPrint(String value) {
        StringBuilder sb = new StringBuilder(value.length() + 7);
        sb.append(value, 0, 4);
        int i, max;
        for (max = value.length(), i = 4; i < max; i += 4) {
            sb.append(' ');
            sb.append(value, i, i + 4 < max ? i + 4 : max);
        }
        if (i < max) {
            sb.append(max - i);
        }
        return sb.toString();
    }
}
