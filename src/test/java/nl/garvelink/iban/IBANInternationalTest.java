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

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.Arrays;
import java.util.List;

import static org.hamcrest.Matchers.*;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.*;

/**
 * Ensures that the {@link IBAN} class accepts IBAN numbers from every participating country (...known at the time the test was last updated).
 */
@RunWith(Parameterized.class)
public class IBANInternationalTest {

    private final String plain;
    private final String pretty;
    private final boolean sepa;

    @SuppressWarnings("unused")
    public IBANInternationalTest(String testName, String sepa, String plain, String bankIdentifier, String branchIdentfier, String pretty) {
        this.plain = plain;
        this.pretty = pretty;
        this.sepa = Boolean.parseBoolean(sepa);
    }

    @Parameterized.Parameters(name = " {0} ")
    public static List<String[]> parameters() {
        return CountryCodesParameterizedTest.PARAMETERS;
    }

    @Test
    public void parseShouldAcceptPlainForm() {
        IBAN iban = IBAN.parse(plain);
        assertNotNull(iban);
        assertThat(iban.toPlainString(), is(equalTo(plain)));
        assertThat(iban.toString(), is(equalTo(pretty)));
    }

    @Test
    public void parseShouldAcceptPrettyPrintedForm() {
        IBAN iban = IBAN.parse(pretty);
        assertNotNull(iban);
        assertThat(iban.toPlainString(), is(equalTo(plain)));
        assertThat(iban.toString(), is(equalTo(pretty)));
    }

    @Test
    public void parseShouldRejectInvalidIBANLength() {
        try {
            IBAN.parse(plain + '9');
            fail("Invalid input should have been rejected for incorrect length.");
        } catch (WrongLengthException e) {
            assertThat(e.getFailedInput(), is(plain + '9'));
            assertThat(e.getExpectedLength(), is(plain.length()));
            assertThat(e.getActualLength(), is(1 + plain.length()));
        }
    }

    @Test
    public void isSEPACountry() {
        assertEquals(sepa, IBAN.parse(plain).isSEPA());
    }

    @Test
    @SuppressWarnings("deprecation")
    public void getLengthForCountryCodeShouldReturnCorrectValue() {
        assertEquals(plain.length(), IBAN.getLengthForCountryCode(plain.substring(0, 2)));
    }
}
