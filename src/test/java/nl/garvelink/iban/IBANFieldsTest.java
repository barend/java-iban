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

import java.util.List;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.nullValue;
import static org.junit.Assert.*;

/**
 * Ensures that the {@link IBAN} class accepts IBAN numbers from every participating country (...known at the time the test was last updated).
 */
@RunWith(Parameterized.class)
public class IBANFieldsTest {

    private final TestData td;

    @SuppressWarnings("unused")
    public IBANFieldsTest(TestData td) {
        this.td = td;
    }

    @Parameterized.Parameters(name = " {0} ")
    public static List<TestData> parameters() {
        return CountryCodesParameterizedTest.PARAMETERS;
    }

    @Test
    public void shouldExtractBankIdentifier() {
        IBAN iban = IBAN.parse(td.plain);
        assertNotNull(iban);
        if (td.bank != null) {
            assertThat(IBANFields.getBankIdentifier(iban).get(), is(equalTo(td.bank)));
            assertThat(IBANFieldsCompat.getBankIdentifier(iban), is(equalTo(td.bank)));
        } else {
            assertThat(IBANFields.getBankIdentifier(iban).isPresent(), is(false));
            assertThat(IBANFieldsCompat.getBankIdentifier(iban), is(nullValue()));
        }
    }

    @Test
    public void shouldExtractBranchIdentifier() {
        IBAN iban = IBAN.parse(td.plain);
        assertNotNull(iban);
        if (td.branch != null) {
            assertThat(IBANFields.getBranchIdentifier(iban).get(), is(equalTo(td.branch)));
            assertThat(IBANFieldsCompat.getBranchIdentifier(iban), is(equalTo(td.branch)));
        } else {
            assertThat(IBANFields.getBranchIdentifier(iban).isPresent(), is(false));
            assertThat(IBANFieldsCompat.getBranchIdentifier(iban), is(nullValue()));
        }
    }
}
