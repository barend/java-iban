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
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.Arrays;
import java.util.List;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.*;

/**
 * Ensures that the {@link nl.garvelink.iban.IBAN} class accepts IBAN numbers from every participating country (...known at the time the test was last updated).
 */
@RunWith(Parameterized.class)
public class LenientIBANInternationalTest {

    private final String plain;
    private final String pretty;

    public LenientIBANInternationalTest(@SuppressWarnings("unused") String testName, String plain, String pretty) {
        this.plain = plain;
        this.pretty = pretty;
    }

    /**
     * List of valid international IBAN's obtained from http://www.ecbs.org/iban.htm (via Wikipedia).
     */
    @Parameterized.Parameters(name = " {0} ")
    public static List<String[]> parameters() {
        return Arrays.asList(
                str( "Andorra",         "AD1200012030200359100100",        "AD12 0001 2030 2003 5910 0100" ),
                str( "Austria",         "AT611904300234573201",            "AT61 1904 3002 3457 3201" ),
                str( "Belgium",         "BE68539007547034",                "BE68 5390 0754 7034" ),
                str( "Bosnia and Hgv.", "BA391290079401028494",            "BA39 1290 0794 0102 8494" ),
                str( "Bulgaria",        "BG80BNBG96611020345678",          "BG80 BNBG 9661 1020 3456 78" ),
                str( "Croatia",         "HR1210010051863000160",           "HR12 1001 0051 8630 0016 0" ),
                str( "Cyprus",          "CY17002001280000001200527600",    "CY17 0020 0128 0000 0012 0052 7600" ),
                str( "Czech Republic",  "CZ6508000000192000145399",        "CZ65 0800 0000 1920 0014 5399" ),
                str( "Denmark",         "DK5000400440116243",              "DK50 0040 0440 1162 43" ),
                str( "Estonia",         "EE382200221020145685",            "EE38 2200 2210 2014 5685" ),
                str( "Finland",         "FI2112345600000785",              "FI21 1234 5600 0007 85" ),
                str( "France",          "FR1420041010050500013M02606",     "FR14 2004 1010 0505 0001 3M02 606" ),
                str( "Germany",         "DE89370400440532013000",          "DE89 3704 0044 0532 0130 00" ),
                str( "Gibraltar",       "GI75NWBK000000007099453",         "GI75 NWBK 0000 0000 7099 453" ),
                str( "Greece",          "GR1601101250000000012300695",     "GR16 0110 1250 0000 0001 2300 695" ),
                str( "Hungary",         "HU42117730161111101800000000",    "HU42 1177 3016 1111 1018 0000 0000" ),
                str( "Iceland",         "IS140159260076545510730339",      "IS14 0159 2600 7654 5510 7303 39" ),
                str( "Ireland",         "IE29AIBK93115212345678",          "IE29 AIBK 9311 5212 3456 78" ),
                str( "Israel",          "IL620108000000099999999",         "IL62 0108 0000 0009 9999 999" ),
                str( "Italy",           "IT60X0542811101000000123456",     "IT60 X054 2811 1010 0000 0123 456" ),
                str( "Latvia",          "LV80BANK0000435195001",           "LV80 BANK 0000 4351 9500 1" ),
                str( "Liechtenstein",   "LI21088100002324013AA",           "LI21 0881 0000 2324 013A A" ),
                str( "Lithuania",       "LT121000011101001000",            "LT12 1000 0111 0100 1000" ),
                str( "Luxembourg",      "LU280019400644750000",            "LU28 0019 4006 4475 0000" ),
                str( "Macedonia",       "MK07250120000058984",             "MK07 2501 2000 0058 984" ),
                str( "Malta",           "MT84MALT011000012345MTLCAST001S", "MT84 MALT 0110 0001 2345 MTLC AST0 01S" ),
                str( "Mauritius",       "MU17BOMM0101101030300200000MUR",  "MU17 BOMM 0101 1010 3030 0200 000M UR" ),
                str( "Monaco",          "MC1112739000700011111000h79",     "MC11 1273 9000 7000 1111 1000 h79" ),
                str( "Montenegro",      "ME25505000012345678951",          "ME25 5050 0001 2345 6789 51" ),
                str( "Netherlands",     "NL91ABNA0417164300",              "NL91 ABNA 0417 1643 00" ),
                str( "Norway",          "NO9386011117947",                 "NO93 8601 1117 947" ),
                str( "Poland",          "PL61109010140000071219812874",    "PL61 1090 1014 0000 0712 1981 2874" ),
                str( "Portugal",        "PT50000201231234567890154",       "PT50 0002 0123 1234 5678 9015 4" ),
                str( "Romania",         "RO49AAAA1B31007593840000",        "RO49 AAAA 1B31 0075 9384 0000" ),
                str( "San Marino",      "SM86U0322509800000000270100",     "SM86 U032 2509 8000 0000 0270 100" ),
                str( "Serbia",          "RS35260005601001611379",          "RS35 2600 0560 1001 6113 79" ),
                str( "Slovak Republic", "SK3112000000198742637541",        "SK31 1200 0000 1987 4263 7541" ),
                str( "Slovenia",        "SI56191000000123438",             "SI56 1910 0000 0123 438" ),
                str( "Spain",           "ES9121000418450200051332",        "ES91 2100 0418 4502 0005 1332" ),
                str( "Sweden",          "SE4550000000058398257466",        "SE45 5000 0000 0583 9825 7466" ),
                str( "Switzerland",     "CH9300762011623852957",           "CH93 0076 2011 6238 5295 7" ),
                str( "Turkey",          "TR330006100519786457841326",      "TR33 0006 1005 1978 6457 8413 26" ),
                str( "United Kingdom",  "GB29NWBK60161331926819",          "GB29 NWBK 6016 1331 9268 19" )
        );
    }

    @Test
    public void parseShouldAcceptPlainForm() {
        LenientIBAN iban = LenientIBAN.parse(plain);
        assertNotNull(iban);
        assertThat(iban.toPlainString(), is(equalTo(plain)));
        assertThat(iban.toString(), is(equalTo(pretty)));
        assertTrue(iban.isValid());
    }

    @Test
    public void parseShouldAcceptPrettyPrintedForm() {
        LenientIBAN iban = LenientIBAN.parse(pretty);
        assertNotNull(iban);
        assertThat(iban.toPlainString(), is(equalTo(plain)));
        assertThat(iban.toString(), is(equalTo(pretty)));
        assertTrue(iban.isValid());
    }

    /**
     * Helps put the parameter data into readable formatting.
     */
    private static final String[] str(String... strs) {
        return strs;
    }
}
