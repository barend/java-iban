/*
   Copyright 2019 Barend Garvelink

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

import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InvalidObjectException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Arrays;
import java.util.Base64;
import java.util.List;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.fail;

/**
 * Miscellaneous tests for the {@link IBAN} class.
 */
public class IBANTest {
    private static final String VALID_IBAN = "NL91ABNA0417164300";
    private static final String INVALID_IBAN = "NL12ABNA0417164300";

    @Test
    public void getCountryCodeShouldReturnTheCountryCode() {
        assertThat(IBAN.parse(VALID_IBAN).getCountryCode(), is("NL"));
    }

    @Test
    public void getCheckDigitsShouldReturnTheCheckDigits() {
        assertThat(IBAN.parse(VALID_IBAN).getCheckDigits(), is("91"));
    }

    @Test
    public void valueOfNullIsNull() {
        assertThat(IBAN.valueOf(null), is(nullValue()));
    }

    @Test
    public void valueOfShouldAcceptToString() {
        // The valueOf(String) should accept IBAN's own toString() output as input.
        IBAN original = IBAN.parse(VALID_IBAN);
        IBAN copy = IBAN.valueOf(original.toString());
        assertThat(copy, is(equalTo(original)));
    }

    @Test(expected = IllegalArgumentException.class)
    public void parseShouldRejectNull() {
        IBAN.parse(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void parseShouldRejectInvalidInput() {
        IBAN.parse("Shenanigans!");
    }

    @Test(expected = IllegalArgumentException.class)
    public void parseShouldRejectLeadingWhitespace() {
        IBAN.parse(" " + VALID_IBAN);
    }

    @Test(expected = IllegalArgumentException.class)
    public void parseShouldRejectTrailingWhitespace() {
        IBAN.parse(VALID_IBAN + ' ');
    }

    @Test
    public void parseShouldRejectUnknownCountryCode() {
        try {
            IBAN.parse("UU345678345543234");
            fail("Invalid input should have been rejected for unknown country code.");
        } catch (UnknownCountryCodeException e) {
            assertThat(e.getFailedInput(), is("UU345678345543234"));
        }
    }

    @Test
    public void parseShouldRejectChecksumFailure() {
        try {
            IBAN.parse(INVALID_IBAN);
            fail("Invalid input should have been rejected for checksum mismatch.");
        } catch (WrongChecksumException e) {
            assertThat(e.getFailedInput(), is(INVALID_IBAN));
        }
    }

    @Test
    public void testEqualsContract() {
        IBAN x = IBAN.parse(VALID_IBAN);
        IBAN y = IBAN.parse(VALID_IBAN);
        IBAN z = IBAN.parse(VALID_IBAN);

        assertThat("No object equals null", x.equals(null), is(false));
        assertThat("An object equals itself", x.equals(x), is(true));
        assertThat("Equality is symmetric", x.equals(y) && y.equals(x), is(true));
        assertThat("Equality is transitive", x.equals(y) && y.equals(z) && x.equals(z), is(true));
        assertThat("Equal objects have the same hash code", x.hashCode(), is(equalTo(y.hashCode())));
    }

    @Test
    public void testToPretty() throws Exception {
        assertThat(IBAN.toPretty(""), is(equalTo("")));
        assertThat(IBAN.toPretty("12"), is(equalTo("12")));
        assertThat(IBAN.toPretty("1 2"), is(equalTo("12")));
        assertThat(IBAN.toPretty("1234"), is(equalTo("1234")));
        assertThat(IBAN.toPretty("1 2 3 4"), is(equalTo("1234")));
        assertThat(IBAN.toPretty("12345"), is(equalTo("1234 5")));
        assertThat(IBAN.toPretty("1234 5"), is(equalTo("1234 5")));
        assertThat(IBAN.toPretty("12345678"), is(equalTo("1234 5678")));
        assertThat(IBAN.toPretty("1234 5678"), is(equalTo("1234 5678")));
        assertThat(IBAN.toPretty("123456789"), is(equalTo("1234 5678 9")));
        assertThat(IBAN.toPretty("1234 5678 9"), is(equalTo("1234 5678 9")));
    }

    @Test
    public void testToPlain() throws Exception {
        assertThat(IBAN.toPlain(""), is(equalTo("")));
        assertThat(IBAN.toPlain("12"), is(equalTo("12")));
        assertThat(IBAN.toPlain("1 2"), is(equalTo("12")));
        assertThat(IBAN.toPlain("1234"), is(equalTo("1234")));
        assertThat(IBAN.toPlain("1 2 3 4"), is(equalTo("1234")));
        assertThat(IBAN.toPlain("12345"), is(equalTo("12345")));
        assertThat(IBAN.toPlain("1234 5"), is(equalTo("12345")));
        assertThat(IBAN.toPlain("12345678"), is(equalTo("12345678")));
        assertThat(IBAN.toPlain("1234 5678"), is(equalTo("12345678")));
        assertThat(IBAN.toPlain("123456789"), is(equalTo("123456789")));
        assertThat(IBAN.toPlain("1234 5678 9"), is(equalTo("123456789")));
    }

    @Test
    public void testSerializationRoundTrip() throws IOException, ClassNotFoundException {
        IBAN original = IBAN.parse(VALID_IBAN);
        ByteArrayOutputStream baos = new ByteArrayOutputStream(2048);
        new ObjectOutputStream(baos).writeObject(original);

        byte[] bytes = baos.toByteArray();
//        System.out.println(Base64.getEncoder().encodeToString(bytes)); // for the tests below
        ObjectInputStream ois = new ObjectInputStream(new ByteArrayInputStream(bytes));
        IBAN copy = (IBAN) ois.readObject();
        assertThat(copy, is(equalTo(original)));
    }

    @Test
    public void testSerializedFormCompatibility() throws IOException, ClassNotFoundException {
        // This was manually sampled from the preceding test. This test is to ensure that the serialised form remains
        // stable as the library evolves.
        String serializedForm = "rO0ABXNyAB5ubC5nYXJ2ZWxpbmsuaWJhbi5JQkFOJE1lbWVudG8AAAAAAAAAAQwAAHhwdxwAAAAAAAAAAQASTkw5MUFCTkEwNDE3MTY0MzAweA==";
        ObjectInputStream ois = new ObjectInputStream(new ByteArrayInputStream(Base64.getDecoder().decode(serializedForm)));
        IBAN copy = (IBAN) ois.readObject();
        assertThat(copy.toPlainString(), is(equalTo(VALID_IBAN)));
    }

    @Test
    public void testDeserializationPerformsValidation() throws IOException, ClassNotFoundException {
        // This is the same base64 blob as above, altered to put in a garbage IBAN value.
        String serializedForm = "rO0ABXNyAB5ubC5nYXJ2ZWxpbmsuaWJhbi5JQkFOJE1lbWVudG8AAAAAAAAAAQwAAHhwdxwAAAAAAAAAAQASTkw5MUFCTkEwNEE3MTY0MzAweA==";
        ObjectInputStream ois = new ObjectInputStream(new ByteArrayInputStream(Base64.getDecoder().decode(serializedForm)));
        try {
            IBAN copy = (IBAN) ois.readObject();
            fail("IBAN should not have deserialized successfully: " + copy);
        } catch (InvalidObjectException e) {
            // Ensure that the right exception type is inside the IOE, to make sure we're testing a bad IBAN and not
            // just a bad serialized blob.
            assertThat(e.getCause(), is(instanceOf(WrongChecksumException.class)));
        }
    }

    @Test
    public void lexicalSort() {
        List<IBAN> expected = Arrays.asList(IBAN.parse("DK3400000000000003"), IBAN.parse("NL41BANK0000000002"), IBAN.parse("NL68BANK0000000001"));
        List<IBAN> actual = Arrays.asList(IBAN.parse("NL68BANK0000000001"), IBAN.parse("DK3400000000000003"), IBAN.parse("NL41BANK0000000002"));
        actual.sort(IBAN.LEXICAL_ORDER);
        assertThat(actual, is(equalTo(expected)));
    }
}
