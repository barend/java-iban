/*
   Copyright 2015 Barend Garvelink

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

import java.util.ArrayList;
import java.util.Collections;
import org.junit.Test;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.*;

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

    @Test(expected = UnknownCountryCodeException.class)
    public void parseShouldRejectUnknownCountryCode() {
        IBAN.parse("UU345678345543234");
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

        assertFalse("No object equals null", x.equals(null));
        assertTrue("An object equals itself", x.equals(x));
        assertTrue("Equality is symmetric", x.equals(y) && y.equals(x));
        assertTrue("Equality is transitive", x.equals(y) && y.equals(z) && x.equals(z));
        assertEquals("Equal objects have the same hash code", x.hashCode(), y.hashCode());
    }

    @Test
    public void testToPretty() throws Exception {
        assertEquals("", IBAN.toPretty(""));
        assertEquals("12", IBAN.toPretty("12"));
        assertEquals("12", IBAN.toPretty("1 2"));
        assertEquals("1234", IBAN.toPretty("1234"));
        assertEquals("1234", IBAN.toPretty("1 2 3 4"));
        assertEquals("1234 5", IBAN.toPretty("12345"));
        assertEquals("1234 5", IBAN.toPretty("1234 5"));
        assertEquals("1234 5678", IBAN.toPretty("12345678"));
        assertEquals("1234 5678", IBAN.toPretty("1234 5678"));
        assertEquals("1234 5678 9", IBAN.toPretty("123456789"));
        assertEquals("1234 5678 9", IBAN.toPretty("1234 5678 9"));
    }

    @Test
    public void testToPlain() throws Exception {
        assertEquals("", IBAN.toPlain(""));
        assertEquals("12", IBAN.toPlain("12"));
        assertEquals("12", IBAN.toPlain("1 2"));
        assertEquals("1234", IBAN.toPlain("1234"));
        assertEquals("1234", IBAN.toPlain("1 2 3 4"));
        assertEquals("12345", IBAN.toPlain("12345"));
        assertEquals("12345", IBAN.toPlain("1234 5"));
        assertEquals("12345678", IBAN.toPlain("12345678"));
        assertEquals("12345678", IBAN.toPlain("1234 5678"));
        assertEquals("123456789", IBAN.toPlain("123456789"));
        assertEquals("123456789", IBAN.toPlain("1234 5678 9"));
    }

    @Test
    public void lexicalSort() {
        ArrayList expected = new ArrayList();
        expected.add(IBAN.parse("DK3400000000000003"));
        expected.add(IBAN.parse("NL41BANK0000000002"));
        expected.add(IBAN.parse("NL68BANK0000000001"));
        ArrayList actual = new ArrayList();
        actual.add(IBAN.parse("NL68BANK0000000001"));
        actual.add(IBAN.parse("DK3400000000000003"));
        actual.add(IBAN.parse("NL41BANK0000000002"));
        Collections.sort(actual, IBAN.LEXICAL_ORDER);
        assertEquals(expected, actual);
    }
}
