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

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.junit.Assert.*;

/**
 * Ensures that the {@link nl.garvelink.iban.IBAN} class accepts IBAN numbers from every participating country (...known at the time the test was last updated).
 */
@RunWith(Parameterized.class)
public class CountryCodesParameterizedTest {

    private final String plain;
    private final String pretty;

    public CountryCodesParameterizedTest(@SuppressWarnings("unused") String testName, String plain, String pretty) {
        this.plain = plain;
        this.pretty = pretty;
    }

    /**
     * List of valid international IBAN's.
     * <p>References:</p>
     * <dl>
     *     <dt>ECBS</dt>
     *     <dd>http://www.ecbs.org/iban.htm</dd>
     *     <dt>SWIFT</dt>
     *     <dd>http://www.swift.com/dsp/resources/documents/IBAN_Registry.pdf (release 45)</dd>
     *     <dt>Nordea</dt>
     *     <dd>http://www.nordea.com/Our+services/International+products+and+services/Cash+Management/IBAN+countries/908462.html (Oct 20, 2013)</dd>
     * </dl>
     * <p>The Nordea link was obtained through Wikipedia.</p>
     * <p>Political note: the SWIFT documentation uses "State of Palestine", as does this source file. The initial
     * version of this source file was based on Wikipedia and used "Palistinian Territories" [sic], which has a typo and
     * is wrong either way. The author chose to follow the name used by SWIFT, since they are the governing body of the
     * IBAN standard. No statement of political preference either way is implied, or should be inferred.</p>
     */
    static final List<String[]> PARAMETERS = Arrays.asList(
            str( "Albania",                  "AL47212110090000000235698741",    "AL47 2121 1009 0000 0002 3569 8741"),      //SWIFT, Nordea
            str( "Algeria",                  "DZ4000400174401001050486",        "DZ40 0040 0174 4010 0105 0486"),           //Nordea
            str( "Andorra",                  "AD1200012030200359100100",        "AD12 0001 2030 2003 5910 0100" ),          //SWIFT, ECBS, Nordea
            str( "Angola",                   "AO06000600000100037131174",       "AO06 0006 0000 0100 0371 3117 4"),         //Nordea
            str( "Austria",                  "AT611904300234573201",            "AT61 1904 3002 3457 3201" ),               //SWIFT, ECBS, Nordea
            str( "Azerbaijan",               "AZ21NABZ00000000137010001944",    "AZ21 NABZ 0000 0000 1370 1000 1944"),      //SWIFT, Nordea
            str( "Bahrain",                  "BH29BMAG1299123456BH00",          "BH29 BMAG 1299 1234 56BH 00"),             //SWIFT, Nordea
            str( "Belgium",                  "BE68539007547034",                "BE68 5390 0754 7034" ),                    //SWIFT, ECBS, Nordea
            str( "Benin",                    "BJ11B00610100400271101192591",    "BJ11 B006 1010 0400 2711 0119 2591"),      //Nordea
            str( "Bosnia and Herzegovinia",  "BA391290079401028494",            "BA39 1290 0794 0102 8494" ),               //SWIFT, ECBS, Nordea
            str( "Brazil",                   "BR9700360305000010009795493P1",   "BR97 0036 0305 0000 1000 9795 493P 1"),    //SWIFT, Nordea
            str( "Bulgaria",                 "BG80BNBG96611020345678",          "BG80 BNBG 9661 1020 3456 78" ),            //SWIFT, ECBS, Nordea
            str( "Burkina Faso",             "BF1030134020015400945000643",     "BF10 3013 4020 0154 0094 5000 643"),       //Nordea
            str( "Burundi",                  "BI43201011067444",                "BI43 2010 1106 7444"),                     //Nordea
            str( "Cameroon",                 "CM2110003001000500000605306",     "CM21 1000 3001 0005 0000 0605 306"),       //Nordea
            str( "Cape Verde",               "CV64000300004547069110176",       "CV64 0003 0000 4547 0691 1017 6"),         //Nordea
            str( "Congo",                    "CG5230011000202151234567890",     "CG52 3001 1000 2021 5123 4567 890"),       //Nordea
            str( "Costa Rica",               "CR0515202001026284066",           "CR05 1520 2001 0262 8406 6"),              //SWIFT, Nordea
            str( "Croatia",                  "HR1210010051863000160",           "HR12 1001 0051 8630 0016 0" ),             //SWIFT, ECBS, Nordea
            str( "Cyprus",                   "CY17002001280000001200527600",    "CY17 0020 0128 0000 0012 0052 7600" ),     //SWIFT, ECBS, Nordea
            str( "Czech Republic",           "CZ6508000000192000145399",        "CZ65 0800 0000 1920 0014 5399" ),          //SWIFT, ECBS, Nordea
            str( "Denmark",                  "DK5000400440116243",              "DK50 0040 0440 1162 43" ),                 //SWIFT, ECBS, Nordea
            str( "Dominican Republic",       "DO28BAGR00000001212453611324",    "DO28 BAGR 0000 0001 2124 5361 1324"),      //SWIFT, Nordea
            str( "Egypt",                    "EG1100006001880800100014553",     "EG11 0000 6001 8808 0010 0014 553" ),      //Nordea
            str( "Estonia",                  "EE382200221020145685",            "EE38 2200 2210 2014 5685" ),               //SWIFT, ECBS, Nordea
            str( "Faroe Islands",            "FO1464600009692713",              "FO14 6460 0009 6927 13" ),                 //Nordea
            str( "Finland",                  "FI2112345600000785",              "FI21 1234 5600 0007 85" ),                 //SWIFT, ECBS, Nordea
            str( "France",                   "FR1420041010050500013M02606",     "FR14 2004 1010 0505 0001 3M02 606" ),      //SWIFT, ECBS, Nordea
            str( "Gabon",                    "GA2140002000055602673300064",     "GA21 4000 2000 0556 0267 3300 064"),       //Nordea
            str( "Georgia",                  "GE29NB0000000101904917",          "GE29 NB00 0000 0101 9049 17"),             //SWIFT, Nordea
            str( "Germany",                  "DE89370400440532013000",          "DE89 3704 0044 0532 0130 00" ),            //SWIFT, ECBS, Nordea
            str( "Gibraltar",                "GI75NWBK000000007099453",         "GI75 NWBK 0000 0000 7099 453" ),           //SWIFT, ECBS, Nordea
            str( "Greece",                   "GR1601101250000000012300695",     "GR16 0110 1250 0000 0001 2300 695" ),      //SWIFT, ECBS, Nordea
            str( "Greenland",                "GL8964710001000206",              "GL89 6471 0001 0002 06"),                  //Nordea
            str( "Guatemala",                "GT82TRAJ01020000001210029690",    "GT82 TRAJ 0102 0000 0012 1002 9690"),      //SWIFT, Nordea
            str( "Hungary",                  "HU42117730161111101800000000",    "HU42 1177 3016 1111 1018 0000 0000" ),     //SWIFT, ECBS, Nordea
            str( "Iceland",                  "IS140159260076545510730339",      "IS14 0159 2600 7654 5510 7303 39" ),       //SWIFT, ECBS, Nordea
            str( "Iran",                     "IR580540105180021273113007",      "IR58 0540 1051 8002 1273 1130 07"),        //Nordea
            str( "Ireland",                  "IE29AIBK93115212345678",          "IE29 AIBK 9311 5212 3456 78" ),            //SWIFT, ECBS, Nordea
            str( "Israel",                   "IL620108000000099999999",         "IL62 0108 0000 0009 9999 999" ),           //SWIFT, ECBS, Nordea
            str( "Italy",                    "IT60X0542811101000000123456",     "IT60 X054 2811 1010 0000 0123 456" ),      //SWIFT, ECBS, Nordea
            str( "Ivory Coast",              "CI05A00060174100178530011852",    "CI05 A000 6017 4100 1785 3001 1852"),      //Nordea
            str( "Kazakhstan",               "KZ176010251000042993",            "KZ17 6010 2510 0004 2993"),                //SWIFT, Nordea
            str( "Kuwait",                   "KW74NBOK0000000000001000372151",  "KW74 NBOK 0000 0000 0000 1000 3721 51"),   //SWIFT, Nordea
            str( "Latvia",                   "LV80BANK0000435195001",           "LV80 BANK 0000 4351 9500 1" ),             //SWIFT, ECBS, Nordea
            str( "Lebanon",                  "LB30099900000001001925579115",    "LB30 0999 0000 0001 0019 2557 9115"),      //SWIFT, Nordea
            str( "Liechtenstein",            "LI21088100002324013AA",           "LI21 0881 0000 2324 013A A" ),             //SWIFT, ECBS, Nordea
            str( "Lithuania",                "LT121000011101001000",            "LT12 1000 0111 0100 1000" ),               //SWIFT, ECBS, Nordea
            str( "Luxembourg",               "LU280019400644750000",            "LU28 0019 4006 4475 0000" ),               //SWIFT, ECBS, Nordea
            str( "Macedonia",                "MK07250120000058984",             "MK07 2501 2000 0058 984" ),                //SWIFT, ECBS, Nordea
            str( "Madagascar",               "MG4600005030010101914016056",     "MG46 0000 5030 0101 0191 4016 056"),       //Nordea
            str( "Mali",                     "ML03D00890170001002120000447",    "ML03 D008 9017 0001 0021 2000 0447"),      //Nordea
            str( "Malta",                    "MT84MALT011000012345MTLCAST001S", "MT84 MALT 0110 0001 2345 MTLC AST0 01S" ), //SWIFT, ECBS, Nordea
            str( "Mauritania",               "MR1300012000010000002037372",     "MR13 0001 2000 0100 0000 2037 372"),       //SWIFT, Nordea
            str( "Mauritius",                "MU17BOMM0101101030300200000MUR",  "MU17 BOMM 0101 1010 3030 0200 000M UR" ),  //SWIFT, ECBS, Nordea
            str( "Moldova",                  "MD24AG000225100013104168",        "MD24 AG00 0225 1000 1310 4168"),           //SWIFT, Nordea
            str( "Monaco",                   "MC1112739000700011111000h79",     "MC11 1273 9000 7000 1111 1000 h79" ),      //SWIFT, ECBS, Nordea
            str( "Montenegro",               "ME25505000012345678951",          "ME25 5050 0001 2345 6789 51" ),            //SWIFT, ECBS, Nordea
            str( "Mozambique",               "MZ59000100000011834194157",       "MZ59 0001 0000 0011 8341 9415 7"),         //Nordea
            str( "Netherlands",              "NL91ABNA0417164300",              "NL91 ABNA 0417 1643 00" ),                 //SWIFT, ECBS, Nordea
            str( "Norway",                   "NO9386011117947",                 "NO93 8601 1117 947" ),                     //SWIFT, ECBS, Nordea
            str( "Pakistan",                 "PK24SCBL0000001171495101",        "PK24 SCBL 0000 0011 7149 5101"),           //SWIFT, Nordea
            str( "Palestine, State of",      "PS92PALS000000000400123456702",   "PS92 PALS 0000 0000 0400 1234 5670 2"),    //SWIFT, Nordea
            str( "Poland",                   "PL61109010140000071219812874",    "PL61 1090 1014 0000 0712 1981 2874" ),     //SWIFT, ECBS, Nordea
            str( "Portugal",                 "PT50000201231234567890154",       "PT50 0002 0123 1234 5678 9015 4" ),        //SWIFT, ECBS, Nordea
            str( "Quatar",                   "QA58DOHB00001234567890ABCDEFG",   "QA58 DOHB 0000 1234 5678 90AB CDEF G"),    //SWIFT
            str( "Romania",                  "RO49AAAA1B31007593840000",        "RO49 AAAA 1B31 0075 9384 0000" ),          //SWIFT, ECBS, Nordea
            str( "San Marino",               "SM86U0322509800000000270100",     "SM86 U032 2509 8000 0000 0270 100" ),      //SWIFT, ECBS, Nordea
            str( "Saudi Arabia",             "SA0380000000608010167519",        "SA03 8000 0000 6080 1016 7519"),           //SWIFT, Nordea
            str( "Senegal",                  "SN12K00100152000025690007542",    "SN12 K001 0015 2000 0256 9000 7542"),      //Nordea
            str( "Serbia",                   "RS35260005601001611379",          "RS35 2600 0560 1001 6113 79" ),            //SWIFT, ECBS, Nordea
            str( "Slovak Republic",          "SK3112000000198742637541",        "SK31 1200 0000 1987 4263 7541" ),          //SWIFT, ECBS, Nordea
            str( "Slovenia",                 "SI56191000000123438",             "SI56 1910 0000 0123 438" ),                //SWIFT, ECBS, Nordea
            str( "Spain",                    "ES9121000418450200051332",        "ES91 2100 0418 4502 0005 1332" ),          //SWIFT, ECBS, Nordea
            str( "Sweden",                   "SE4550000000058398257466",        "SE45 5000 0000 0583 9825 7466" ),          //SWIFT, ECBS, Nordea
            str( "Switzerland",              "CH9300762011623852957",           "CH93 0076 2011 6238 5295 7" ),             //SWIFT, ECBS, Nordea
            str( "Tunisia",                  "TN5914207207100707129648",        "TN59 1420 7207 1007 0712 9648"),           //SWIFT, Nordea
            str( "Turkey",                   "TR330006100519786457841326",      "TR33 0006 1005 1978 6457 8413 26" ),       //SWIFT, ECBS, Nordea
            str( "Ukraine",                  "UA573543470006762462054925026",   "UA57 3543 4700 0676 2462 0549 2502 6"),    //Nordea
            str( "United Arab Emirates",     "AE260211000000230064016",         "AE26 0211 0000 0023 0064 016"),            //SWIFT, Nordea
            str( "United Kingdom",           "GB29NWBK60161331926819",          "GB29 NWBK 6016 1331 9268 19" ),            //SWIFT, ECBS, Nordea
            str( "Virgin Islands, British",  "VG96VPVG0000012345678901",        "VG96 VPVG 0000 0123 4567 8901")            //SWIFT, Nordea
        );

    @Parameterized.Parameters(name = " {0} ")
    public static List<String[]> parameters() {
        return PARAMETERS;
    }

    @Test
    public void getLengthForCountryCodeShouldReturnCorrectValue() {
        assertEquals(plain.length(), CountryCodes.getLengthForCountryCode(plain.substring(0, 2)));
    }

    @Test
    public void isKnownCountryCodeShouldReturnTrue() {
        assertTrue(CountryCodes.isKnownCountryCode(plain.substring(0, 2)));
    }

    /**
     * Used by the {@code xxxTestShouldBeExhaustive()} methods to ensure that there are no untested country codes
     * defined in the {@code CountryCodes} class. The inverse, a country code that exists in the test but not in
     * the application code does not have to be separately verified.
     * @see #prepareTestShouldBeExhaustive()
     * @see #updateTestShouldBeExhaustive()
     * @see #finishTestShouldBeExhaustive()
     */
    private static final Set<String> allCountryCodes = Collections.synchronizedSet(new HashSet<String>(PARAMETERS.size()));

    @BeforeClass
    public static void prepareTestShouldBeExhaustive() {
        allCountryCodes.addAll(CountryCodes.getKnownCountryCodes());
    }

    @Test
    public void updateTestShouldBeExhaustive() {
        allCountryCodes.remove(plain.substring(0, 2));
    }

    @AfterClass
    public static void finishTestShouldBeExhaustive() {
        assertTrue("There are entries in CountryCodes.java that are not covered in test: " + allCountryCodes,
                allCountryCodes.isEmpty());
    }

    /**
     * Helps put the parameter data into readable formatting.
     */
    private static final String[] str(String... strs) {
        return strs;
    }
}
