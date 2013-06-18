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

import org.junit.Test;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.nullValue;
import static org.junit.Assert.*;

/**
 * Miscellaneous tests for the {@link nl.garvelink.iban.IBAN} class.
 */
public class LenientIBANTest {
    private static final String VALID_IBAN = "NL91ABNA0417164300";
    private static final String INVALID_IBAN = "NL12ABNA0417164300";

    @Test
    public void getCountryCodeShouldReturnTheCountryCode() {
        assertThat(LenientIBAN.parse(VALID_IBAN).getCountryCode(), is("NL"));
    }

    @Test
    public void getCheckDigitsShouldReturnTheCheckDigits() {
        assertThat(LenientIBAN.parse(VALID_IBAN).getCheckDigits(), is("91"));
    }

    @Test
    public void valueOfNullIsNull() {
        assertThat(LenientIBAN.valueOf(null), is(nullValue()));
    }

    @Test(expected = IllegalArgumentException.class)
    public void parseShouldRejectNull() {
        LenientIBAN.parse(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void parseShouldRejectInvalidInput() {
        LenientIBAN.parse("Shenanigans!");
    }

    @Test(expected = IllegalArgumentException.class)
    public void parseShouldRejectLeadingWhitespace() {
        LenientIBAN.parse(" " + VALID_IBAN);
    }

    @Test(expected = IllegalArgumentException.class)
    public void parseShouldRejectTrailingWhitespace() {
        LenientIBAN.parse(VALID_IBAN + ' ');
    }

    @Test
    public void parseShouldAllowUnknownCountryCode() {
        LenientIBAN iban = LenientIBAN.parse("RF203"); // This is MOD97 valid (and actually a structured payment reference, but I digress)
        assertThat(iban.getCountryCode(), is("RF"));
        assertFalse(iban.isCountryCodeKnown());
        assertFalse(iban.isCorrectLength());
        assertFalse(iban.isValid());
        assertTrue(iban.isChecksumValid());
    }

    @Test
    public void parseShouldAllowChecksumFailure() {
        LenientIBAN iban = LenientIBAN.parse(INVALID_IBAN);
        assertThat(iban.getCountryCode(), is("NL"));
        assertTrue(iban.isCountryCodeKnown());
        assertTrue(iban.isCorrectLength());
        assertFalse(iban.isValid());
        assertFalse(iban.isChecksumValid());
    }

    @Test
    public void testEqualsContract() {
        LenientIBAN x = LenientIBAN.parse(VALID_IBAN);
        LenientIBAN y = LenientIBAN.parse(VALID_IBAN);
        LenientIBAN z = LenientIBAN.parse(VALID_IBAN);

        assertFalse("No object equals null", x.equals(null));
        assertTrue("An object equals itself", x.equals(x));
        assertTrue("Equality is symmetric", x.equals(y) && y.equals(x));
        assertTrue("Equality is transitive", x.equals(y) && y.equals(z) && x.equals(z));
        assertEquals("Equal objects have the same hash code", x.hashCode(), y.hashCode());
    }
}
