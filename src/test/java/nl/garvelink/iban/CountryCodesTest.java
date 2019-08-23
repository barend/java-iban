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

import java.util.ArrayList;
import java.util.List;
import org.junit.Test;
import static org.junit.Assert.*;
import static org.hamcrest.Matchers.*;

import java.util.Iterator;

/**
 * Some tests for {@link CountryCodes}.
 */
public class CountryCodesTest {

    @Test(expected = UnsupportedOperationException.class)
    public void getCountryCodesShouldNotBeEditable() {
        // Not meant to be an exhaustive test, just a reminder to keep API consistent if implementation changes.
        CountryCodes.getKnownCountryCodes().add("ZZ");
    }

    @Test
    public void getCountryCodesShouldBeInAscendingOrder() {
        List<String> raw = new ArrayList<>(CountryCodes.getKnownCountryCodes());
        List<String> sorted = new ArrayList<>(CountryCodes.getKnownCountryCodes());
        sorted.sort(String::compareTo);
        assertEquals(sorted, raw);
    }

    @Test
    public void isKnownCountryCodeShouldReturnFalseForNull() {
        assertFalse(CountryCodes.isKnownCountryCode(null));
    }

    @Test
    public void isKnownCountryCodeShouldReturnFalseForLowercase() {
        assertFalse(CountryCodes.isKnownCountryCode("nl"));
    }

    @Test
    public void getLengthForUnknownCountryCodeReturnsMinusOne() {
        assertThat(CountryCodes.getLengthForCountryCode("XX"), is(-1));
    }
}
