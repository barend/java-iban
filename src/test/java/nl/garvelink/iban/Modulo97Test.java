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
import static org.junit.Assert.*;
import static org.hamcrest.Matchers.*;

/**
 * Test suite for {@link Modulo97}.
 */
public class Modulo97Test {

    @Test(expected = IllegalArgumentException.class)
    public void itShouldRejectNull() {
        Modulo97.checksum(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void itShouldRejectLength0() {
        Modulo97.checksum("");
    }

    @Test(expected = IllegalArgumentException.class)
    public void itShouldRejectLength1() {
        Modulo97.checksum("M");
    }

    @Test(expected = IllegalArgumentException.class)
    public void itShouldRejectLength2() {
        Modulo97.checksum("MO");
    }

    @Test(expected = IllegalArgumentException.class)
    public void itShouldRejectLength3() {
        Modulo97.checksum("MO9");
    }

    @Test(expected = IllegalArgumentException.class)
    public void itShouldRejectLength4() {
        Modulo97.checksum("MO97");
    }

    @Test(expected = IllegalArgumentException.class)
    public void itShouldRejectLength1PaddedTo5() {
        Modulo97.checksum("M    ");
    }

    @Test(expected = IllegalArgumentException.class)
    public void itShouldRejectLength2PaddedTo5() {
        Modulo97.checksum("   MO");
    }

    @Test(expected = IllegalArgumentException.class)
    public void itShouldRejectLength3PaddedTo5() {
        Modulo97.checksum("M O 9");
    }

    @Test(expected = IllegalArgumentException.class)
    public void itShouldRejectLength4PaddedTo5() {
        Modulo97.checksum(" MO97");
    }

    @Test(expected = IllegalArgumentException.class)
    public void itShouldRejectInvalidNonWhitespace() {
        Modulo97.checksum("TS00☠");
    }

    @Test(expected = IllegalArgumentException.class)
    public void itShouldRejectInvalidWhitespace() {
        Modulo97.checksum("MO97\tA");
    }

    @Test
    public void itShouldCalculateAnExpectedChecksum() {
        assertThat(Modulo97.checksum("MO00T"), is(83));
    }

    @Test
    public void itShouldCalculateAnExpectedCheckDigits() {
        assertThat(Modulo97.calculateCheckDigits("MO00T"), is(15));
    }

    @Test
    public void itShouldReturn1ForAKnownCorrectChecksum() {
        assertThat(Modulo97.checksum("MO15T"), is(1));
    }

    @Test
    public void itShouldVerifyAKnownCorrectChecksum() {
        assertThat(Modulo97.verifyCheckDigits("MO15T"), is(true));
        for (int i = 0; i < 15; i++) {
            assertThat(Modulo97.verifyCheckDigits(String.format("MO%02dT", i)), is(false));
        }
        for (int i = 16; i < 100; i++) {
            assertThat(Modulo97.verifyCheckDigits(String.format("MO%02dT", i)), is(false));
        }
    }

    @Test(expected = IllegalArgumentException.class)
    public void itShouldRefuseToCalculateCheckDigitsIfIndex2IsNot0() {
        Modulo97.calculateCheckDigits("MO10A");
    }

    @Test(expected = IllegalArgumentException.class)
    public void itShouldRefuseToCalculateCheckDigitsIfIndex3IsNot0() {
        Modulo97.calculateCheckDigits("MO02A");
    }
}
