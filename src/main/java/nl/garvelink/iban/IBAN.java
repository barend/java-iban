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

import java.math.BigInteger;
import java.util.Arrays;
import java.util.Comparator;

/**
 * An immutable value object representing an International Bank Account Number. Instances of this class have a valid
 * base97 doCalculateChecksum and a valid length for their country code. Any country-specific validation is not currently performed.
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
     * The BigInteger '97', used in the MOD97 division.
     */
    private static final BigInteger NINETY_SEVEN = new BigInteger("97");

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
        if (value.charAt(0) < 'A' || value.charAt(0) > 'Z') {
            throw new IllegalArgumentException("First character is not an uppercase letter.");
        }
        if (value.charAt(2) < '0' || value.charAt(2) > '9' || value.charAt(3) < '0' || value.charAt(3) > '9') {
            throw new IllegalArgumentException("Digits 3 and 4 not both numeric.");
        }
        if (!Character.isLetterOrDigit(value.charAt(value.length() - 1))) {
            throw new IllegalArgumentException("Last character is not a letter or digit.");
        }
        final int ccIdx = Arrays.binarySearch(COUNTRY_CODES, value.substring(0, 2));
        if (ccIdx < 0) {
            throw new UnknownCountryCodeException(value);
        }
        if (COUNTRY_IBAN_LENGTHS[ccIdx] != value.length()) {
            throw new WrongLengthException(value, COUNTRY_IBAN_LENGTHS[ccIdx]);
        }
        final int calculatedChecksum = doCalculateChecksum(value);
        if (calculatedChecksum != 1) {
            throw new WrongChecksumException(value);
        }
        this.value = value;
        this.valuePretty = prettyPrint(value);
    }

    /**
     * Parses the given string into an IBAN object and confirms the check digits.
     * @param input the input, which can be either plain ("CC11ABCD123...") or formatted ("CC11 ABCD 123. ..").
     * @return the parsed and validated IBAN object, never null.
     * @throws IllegalArgumentException if the input is null, malformed or otherwise fails validation.
     * @see #valueOf(String)
     */
    public static IBAN parse(String input) {
        return new IBAN(removeInternalWhitespace(input));
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
     * Composes an IBAN for the given country code, BIC and BBAN, calculating the check digits.
     * @param country the country code.
     * @param bic the bank identification code.
     * @param bban the basic bank account number.
     * @return an IBAN object composed of the given inputs, with a valid doCalculateChecksum.
     */
    public static IBAN compose(String country, String bic, String bban) {
        throw new UnsupportedOperationException();
    }

    /**
     * Calculates the check digits for a given IBAN.
     * @param input the input, which can be either plain ("CC11ABCD123...") or formatted ("CC11 ABCD 123. .."). The existing check digits are ignored and can be any two character values.
     * @return the check digits calculated for the given IBAN.
     */
    public static int calculateChecksum(String input) {
        return doCalculateChecksum(removeInternalWhitespace(input));
    }

    /**
     * Returns the Country Code embedded in the IBAN.
     * @return the two-letter country code.
     */
    public String getCountryCode() {
        return value.substring(0, 2);
    }

    /**
     * Returns the Bank Identifier Code embedded in the IBAN.
     * @return the four-character BIC.
     */
    public String getBIC() {
        return value.substring(4, 8);
    }

    /**
     * Returns the Basic Bank Account Number embedded in the IBAN.
     * @return the account number (alphanumeric, length varies per country code).
     */
    public String getBBAN() {
        return value.substring(8);
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
     * Calculates the check digits for a given IBAN.
     * @param normalizedInput the input, which must nog contain any white space ("CC11ABCD123..."). The existing check digits are ignored and can be any two (non whitespace) character values.
     * @return the check digits calculated for the given IBAN.
     */
    private static int doCalculateChecksum(CharSequence normalizedInput) {
        final char[] buffer = new char[normalizedInput.length() * 2];
        int offset = transform(normalizedInput, 4, normalizedInput.length(), buffer, 0);
        offset = transform(normalizedInput, 0, 4, buffer, offset);
        final BigInteger sum = new BigInteger(new String(buffer, 0, offset));
        final BigInteger remainder = sum.remainder(NINETY_SEVEN);
        return remainder.intValue();
    }

    private static final String removeInternalWhitespace(String input) {
        final boolean containsSpaces = input != null && input.charAt(0) >= 'A' && input.charAt(0) <= 'Z' && input.indexOf(' ') >= 0;
        if (containsSpaces) {
            return input.replaceAll(" ", "");
        }
        return input;
    }

    /**
     * Copies {@code src[srcPos...srcLen)} into {@code dest[destPos)} while applying character to numeric transformation.
     * @param src the data to begin copying, must contain only characters {@code [A-Za-z0-9]}.
     * @param srcPos the index in {@code src} to begin transforming.
     * @param srcLen the number of characters after {@code srcPos} to transform.
     * @param dest the buffer to write transform into.
     * @param destPos the index in {@code dest} to begin writing.
     * @return the value of {@destPos} incremented by the number of characters that were added, i.e. the next unused index in {@code dest}.
     * @throws IllegalArgumentException if {@code src} contains an unsupported character.
     * @throws ArrayIndexOutOfBoundsException if {@code dest} does not have enough capacity to store the transformed result (keep in mind that a single character from {@code src} can expand to two characters in {@code dest}).
     */
    private static int transform(final CharSequence src, final int srcPos, final int srcLen, final char[] dest, final int destPos) {
        int offset = destPos;
        for (int i = srcPos; i < srcLen; i++) {
            char c = src.charAt(i);
            if (c >= '0' && c <= '9') {
                dest[offset++] = c;
            } else if (c >= 'A' && c <= 'Z') {
                int tmp = 10 + (c - 'A');
                dest[offset++] = (char)('0' + tmp / 10);
                dest[offset++] = (char)('0' + tmp % 10);
            } else if (c >= 'a' && c <= 'z') {
                int tmp = 10 + (c - 'a');
                dest[offset++] = (char)('0' + tmp / 10);
                dest[offset++] = (char)('0' + tmp % 10);
            } else {
                throw new IllegalArgumentException("Invalid character '" + c + "'.");
            }
        }
        return offset;
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
