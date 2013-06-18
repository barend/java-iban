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

/**
 * A {@code LenientIBAN} is an {@link IBAN} with relaxed validation requirements. It is an immutable value type
 * representing an International Bank Account Number. Instances of this class may have an incorrect checksum, an
 * incorrect length and/or an unknown country code, but they may not contain characters outside the
 * {@code [A-Za-z0-9] range}.
 * <p>
 * {@code IBAN} is assignment compatible with {@code LenientIBAN}, but not the other way around. In other words,
 * every valid {@code IBAN} is also a valid {@code LenientIBAN}.
 * </p>
 */
public class LenientIBAN {

    /**
     * IBAN value, normalized form (no whitespace).
     */
    final String value;

    /**
     * Pretty-printed value, lazily initialized.
     */
    transient String valuePretty;

    /**
     * Initializing constructor.
     * @param value the IBAN value, without any white space.
     * @throws IllegalArgumentException if the input is null or contains invalid characters.
     */
    LenientIBAN(String value) {
        if (value == null) {
            throw new IllegalArgumentException("Input is null");
        }
        if (value.length() < 5) {
            throw new IllegalArgumentException("Length is too short to be an IBAN");
        }
        if (value.charAt(0) < 'A' || value.charAt(0) > 'Z' || value.charAt(1) < 'A' || value.charAt(1) > 'Z') {
            throw new IllegalArgumentException("Characters at index 0 and 1 not both uppercase letters.");
        }
        if (value.charAt(2) < '0' || value.charAt(2) > '9' || value.charAt(3) < '0' || value.charAt(3) > '9') {
            throw new IllegalArgumentException("Characters at index 2 and 3 not both numeric.");
        }
        for (int i = 4, max = value.length(); i < max; i++) {
            if (!isLetterOrDigit(value.charAt(i))) {
                throw new IllegalArgumentException("Character at index " + i + " is not a letter or digit.");
            }
        }
        this.value = value;
    }

    /**
     * Parses the given string into a LenientIBAN object.
     * @param input the input, which can be either plain ("CC11ABCD123...") or formatted with (ASCII 0x20) space characters ("CC11 ABCD 123. ..").
     * @return the parsed and validated LenientIBAN object, never null.
     * @throws IllegalArgumentException if the input is null or malformed.
     * @see #valueOf(String)
     */
    public static LenientIBAN parse(String input) {
        if (input == null || input.length() == 0) {
            throw new IllegalArgumentException("Input is null or empty string.");
        }
        if (!isLetterOrDigit(input.charAt(0)) || !isLetterOrDigit(input.charAt(input.length() - 1))) {
            throw new IllegalArgumentException("Input begins or ends in an invalid character.");
        }
        final boolean containsInternalSpaces = input.indexOf(' ') >= 0;
        if (containsInternalSpaces) {
            return new LenientIBAN(input.replaceAll(" ", ""));
        }
        return new LenientIBAN(input);
    }

    /**
     * Parses the given string into a LenientIBAN object and confirms the check digits, but returns null for null.
     * @param input the input, which can be either plain ("CC11ABCD123...") or formatted ("CC11 ABCD 123. ..").
     * @return the parsed and validated IBAN object, or null.
     * @throws IllegalArgumentException if the input is malformed.
     * @see #parse(String)
     */
    public static LenientIBAN valueOf(String input) {
        if (input == null) {
            return null;
        }
        return parse(input);
    }

    /**
     * Returns whether this object has a known country code, the correct length for that country code, and passes
     * Modulo-97 checksum validation.
     * @return true if this passes validation.
     */
    public boolean isValid() {
        // Correct length implies known country code.
        return isCorrectLength() && isChecksumValid();
    }

    /**
     * Returns whether this object has a known country code.
     * @return true if this passes known country code validation.
     */
    public boolean isCountryCodeKnown() {
        final int expectedLength = CountryCodes.getLengthForCountryCode(value.substring(0, 2));
        return expectedLength > -1;
    }

    /**
     * Returns whether this object has the correct length for its country code (which implies that the country code is
     * known).
     * @return true if this passes country code and length validation.
     */
    public boolean isCorrectLength() {
        final int expectedLength = CountryCodes.getLengthForCountryCode(value.substring(0, 2));
        return expectedLength == value.length();
    }

    /**
     * Returns whether this object passes Modulo-97 checksum validation.
     * @return true if this passes Modulo-97 validation.
     */
    public boolean isChecksumValid() {
        final int calculatedChecksum = Modulo97.checksum(value);
        return calculatedChecksum == 1;
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

    /**
     * Returns the IBAN in standard formatting, with a space every four characters.
     * @return the formatted IBAN number.
     * @see #toPlainString()
     */
    @Override
    public String toString() {
        // This code is using a non-threadsafe (but still nullsafe) assignment. The prettyPrint() operation is
        // idempotent, so no harm done if it happens to run more than once. I expect concurrent use to be rare.
        String vp = valuePretty;
        if (vp == null) {
            vp = valuePretty = prettyPrint(value);
        }
        return vp;
    }

    /**
     * Returns whether the given character is in the {@code A-Za-z0-9} range.
     * This differs from {@link Character.isLetterOrDigit(char)} because it doesn't understand non-Western characters.
     */
    static final boolean isLetterOrDigit(char c) {
        return (c >= '0' && c <= '9')
            || (c >= 'A' && c <= 'Z')
            || (c >= 'a' && c <= 'z');
    }

    static final String prettyPrint(String value) {
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof LenientIBAN)) return false;
        return value.equals(((LenientIBAN) o).value);
    }

    @Override
    public int hashCode() {
        return value.hashCode();
    }
}
