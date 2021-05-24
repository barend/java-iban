/*
   Copyright 2021 Barend Garvelink

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

import java.io.Externalizable;
import java.io.IOException;
import java.io.InvalidObjectException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.io.ObjectStreamException;
import java.io.Serializable;
import java.util.Comparator;
import java.util.regex.Pattern;

/**
 * An immutable value type representing an International Bank Account Number. Instances of this class have correct
 * check digits and a valid length for their country code. No country-specific validation is performed, other than
 * matching the length of the IBAN to its country code. Unknown country codes are not supported.
 * @author Barend Garvelink (barend@garvelink.nl) https://github.com/barend
 */
public final class IBAN implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * A comparator that puts IBAN's into lexicographic ordering, per {@link String#compareTo(String)}.
     */
    public static final Comparator<IBAN> LEXICAL_ORDER = new Comparator<IBAN>() {
        @Override
        public int compare(IBAN iban, IBAN iban2) {
            return iban.value.compareTo(iban2.value);
        }
    };

    /**
     * The technically shortest possible IBAN. See {@link CountryCodes#SHORTEST_IBAN_LENGTH} for shortest valid length.
     */
    public static final int SHORTEST_POSSIBLE_IBAN = 5;

    /**
     * Used to remove spaces
     */
    private static final Pattern SPACE_PATTERN = Pattern.compile(" ");

    /**
     * IBAN value, normalized form (no whitespace).
     */
    private final String value;

    /**
     * Whether or not this IBAN data is from the SWIFT IBAN Registry.
     */
    private final boolean inSwiftRegistry;

    /**
     * Whether or not this IBAN is of a SEPA participating country.
     */
    private final boolean sepa;

    /**
     * Pretty-printed value, lazily initialized.
     */
    private transient String valuePretty;

    /**
     * Initializing constructor.
     * @param value the IBAN value, without any white space.
     * @throws IllegalArgumentException if the input is null, malformed or otherwise fails validation.
     */
    private IBAN(String value) {
        if (value == null) {
            throw new IllegalArgumentException("Input is null");
        }
        if (value.length() < SHORTEST_POSSIBLE_IBAN) {
            throw new IllegalArgumentException("Length is too short to be an IBAN");
        }
        if (value.charAt(2) < '0' || value.charAt(2) > '9' || value.charAt(3) < '0' || value.charAt(3) > '9') {
            throw new IllegalArgumentException("Characters at index 2 and 3 not both numeric.");
        }
        final String countryCode = value.substring(0, 2);
        final int expectedLength = CountryCodes.getLengthForCountryCode(countryCode);
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
        this.value = value;
        this.inSwiftRegistry = CountryCodes.isInSwiftRegistry(countryCode);
        this.sepa = CountryCodes.isSEPACountry(countryCode);
    }

    /**
     * Parses the given string into an IBAN object and confirms the check digits.
     * @param input the input, which can be either plain ("CC11ABCD123...") or formatted with (ASCII 0x20) space characters ("CC11 ABCD 123. ..").
     * @return the parsed and validated IBAN object, never null.
     * @throws IllegalArgumentException if the input is null, malformed or otherwise fails validation.
     * @see #valueOf(CharSequence)
     */
    public static IBAN parse(CharSequence input) {
        if (input == null || input.length() == 0) {
            throw new IllegalArgumentException("Input is null or empty string.");
        }
        if (!(isLetterOrDigit(input.charAt(0)) && isLetterOrDigit(input.charAt(input.length() - 1)))) {
            throw new IllegalArgumentException("Input begins or ends in an invalid character.");
        }
        return new IBAN(toPlain(input));
    }

    /**
     * Parses the given string into an IBAN object and confirms the check digits, but returns null for null.
     * @param input the input, which can be either plain ("CC11ABCD123...") or formatted ("CC11 ABCD 123. ..").
     * @return the parsed and validated IBAN object, or null.
     * @throws IllegalArgumentException if the input is malformed or otherwise fails validation.
     * @see #parse(CharSequence)
     */
    public static IBAN valueOf(CharSequence input) {
        if (input == null) {
            return null;
        }
        return parse(input);
    }

    /**
     * Composes an IBAN from the given country code and basic bank account number.
     * @param countryCode the country code.
     * @param bban the BBAN.
     * @return an IBAN object composed from the given parts, if valid.
     * @throws IllegalArgumentException if either input is null, if the composed IBAN fails validation.
     * @since 1.9.0
     */
    public static IBAN compose(CharSequence countryCode, CharSequence bban) {
        StringBuilder sb =
            new StringBuilder(CountryCodes.LONGEST_IBAN_LENGTH).append(countryCode).append("00").append(bban);
        int checkDigits = Modulo97.calculateCheckDigits(sb);
        sb.replace(2, 4, Integer.toString(checkDigits));
        return parse(sb);
    }

    /**
     * @deprecated invoke {@link CountryCodes#getLengthForCountryCode(CharSequence)} instead.
     * @param countryCode the country code for which to return the length.
     * @return the length of the IBAN for the given country code, or -1 if unknown.
     */
    @Deprecated
    public static int getLengthForCountryCode(String countryCode) {
        return CountryCodes.getLengthForCountryCode(countryCode);
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
     * Returns whether the IBAN's country participates in SEPA.
     * @return true if SEPA, false if non-SEPA.
     */
    public boolean isSEPA() {
        return this.sepa;
    }

    /**
     * Returns whether the source for this IBAN's format and data is the SWIFT IBAN Registry.
     * @return true if from SWIFT IBAN Registry, false if from Experimental IBANs list.
     * @since 1.6.0
     */
    public boolean isInSwiftRegistry() {
        return this.inSwiftRegistry;
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
        // This code is using a non-threadsafe (but still nullsafe) assignment. The addSpaces() operation is
        // idempotent, so no harm done if it happens to run more than once. I expect concurrent use to be rare.
        String vp = valuePretty;
        if (vp == null) {
            vp = valuePretty = addSpaces(value);
        }
        return vp;
    }

    /**
     * Returns whether the given character is in the {@code A-Za-z0-9} range.
     * This differs from {@link Character#isLetterOrDigit(char)} because it doesn't understand non-Western characters.
     */
    private static boolean isLetterOrDigit(char c) {
        return (c >= '0' && c <= '9')
            || (c >= 'A' && c <= 'Z')
            || (c >= 'a' && c <= 'z');
    }

    /**
     * Removes any spaces contained in the String thereby converting the input into a plain IBAN
     *
     * @param input
     *         possibly pretty printed IBAN
     * @return plain IBAN
     */
    public static String toPlain(CharSequence input) {
        // if SPACE_PATTERN is not found this returns input.toString();
        return SPACE_PATTERN.matcher(input).replaceAll("");
    }

    /**
     * Ensures that the input is pretty printed by first removing any spaces the String might contain and then adding spaces in the right places.
     * <p>This can be useful when prompting a user to correct wrong input</p>
     *
     * @param input
     *         plain or pretty printed IBAN
     * @return pretty printed IBAN
     */
    public static String toPretty(CharSequence input) {
        return addSpaces(toPlain(input));
    }

    /**
     * Converts a plain to a pretty printed IBAN
     *
     * @param value
     *         plain iban
     * @return pretty printed IBAN
     */
    private static String addSpaces(CharSequence value) {
        final int length = value.length();
        final int lastPossibleBlock = length - 4;
        final StringBuilder sb = new StringBuilder(length + (length - 1) / 4);
        int i;
        for (i = 0; i < lastPossibleBlock; i += 4) {
            sb.append(value, i, i + 4);
            sb.append(' ');
        }
        sb.append(value, i, length);
        return sb.toString();
    }

    /**
     * When serializing this object, substitute a {@link Memento} object.
     * @return a memento containing {@link #value}.
     * @throws ObjectStreamException never.
     * @since 1.8.0
     */
    private Object writeReplace() throws ObjectStreamException {
        return new Memento(this.toPlainString());
    }

    /**
     * Prevents deserialization of this type. Instances of IBAN are never deserialized (only {@link Memento}s are), so
     * if we ever encounter a serialized object of IBAN type, we don't want it.
     * @param stream ignored.
     * @throws IOException always.
     * @throws ClassNotFoundException never.
     * @since 1.8.0
     */
    private void readObject(java.io.ObjectInputStream stream)
            throws IOException, ClassNotFoundException {
        throw new InvalidObjectException("This type can only be deserialized from its memento type.");
    }

    /**
     * Prevents deserialization of this type. Instances of IBAN are never deserialized (only {@link Memento}s are), so
     * if we ever encounter a serialized object of IBAN type, we don't want it.
     * @throws ObjectStreamException always.
     * @since 1.8.0
     */
    private void readObjectNoData()
            throws ObjectStreamException {
        throw new InvalidObjectException("This type can only be deserialized from its memento type.");
    }

    /**
     * Serialization helper for {@link IBAN}.
     *
     * This works as a "memento pattern" implementation of the serialized form. This gets us a number of benefits:
     * <ul>
     *     <li>No need to add a public, no-arg constructor to the IBAN class, nor to make its <code>value</code>
     *     non-final. Doing either would invalidate the main design goal of the library.</li>
     *     <li>The validity constraint is checked upon deserialization.</li>
     *     <li>Smaller serialized form (still 101 bytes for an 18-byte IBAN).</li>
     *     <li>Easier to maintain binary compatibility even as the IBAN class evolves.</li>
     * </ul>
     *
     * There should be no need to ever use IBAN.Memento in your code.
     *
     * @since 1.8.0
     */
    static final class Memento implements Externalizable {
        private static final long serialVersionUID = 1L;
        private String value;

        public Memento() {
            super();
        }

        Memento(String value) {
            this();
            this.value = value;
        }

        @Override
        public void writeExternal(ObjectOutput out) throws IOException {
            out.writeLong(serialVersionUID);
            out.writeUTF(this.value);
        }

        @Override
        public void readExternal(ObjectInput in) throws IOException, ClassNotFoundException {
            long serialUID = in.readLong();
            if (serialUID == 1L) {
                this.value = in.readUTF();
            } else {
                throw new InvalidObjectException("Unsupported serial version: " + serialUID);
            }
        }

        private Object readResolve() throws ObjectStreamException {
            try {
                return IBAN.parse(this.value);
            } catch (IllegalArgumentException e) {
                InvalidObjectException ioe = new InvalidObjectException(
                        "Cannot decode serialized form: " + e.getMessage());
                ioe.initCause(e);
                throw ioe;
            }
        }
    }
}
