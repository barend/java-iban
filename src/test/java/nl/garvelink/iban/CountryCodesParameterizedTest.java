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

    @SuppressWarnings("unused")
    public CountryCodesParameterizedTest(String testName, String sepa, String plain, String bankIdentifier, String branchIdentfier, String pretty) {
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
     *     <dd>http://www.swift.com/dsp/resources/documents/IBAN_Registry.pdf (release 58)</dd>
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
            str( "Albania",                  "false", "AL47212110090000000235698741",    "212"     , "1100"    , "AL47 2121 1009 0000 0002 3569 8741"),      //SWIFT, Nordea
            str( "Algeria",                  "false", "DZ4000400174401001050486",        null      , null      , "DZ40 0040 0174 4010 0105 0486"),           //Nordea
            str( "Andorra",                  "false", "AD1200012030200359100100",        "0001"    , "2030"    , "AD12 0001 2030 2003 5910 0100" ),          //SWIFT, ECBS, Nordea
            str( "Angola",                   "false", "AO06000600000100037131174",       null      , null      , "AO06 0006 0000 0100 0371 3117 4"),         //Nordea
            str( "Austria",                  "true" , "AT611904300234573201",            "19043"   , null      , "AT61 1904 3002 3457 3201" ),               //SWIFT, ECBS, Nordea
            str( "Azerbaijan",               "false", "AZ21NABZ00000000137010001944",    "NABZ"    , null      , "AZ21 NABZ 0000 0000 1370 1000 1944"),      //SWIFT, Nordea
            str( "Bahrain",                  "false", "BH29BMAG1299123456BH00",          "BMAG"    , null      , "BH29 BMAG 1299 1234 56BH 00"),             //SWIFT, Nordea
            str( "Belgium",                  "true" , "BE68539007547034",                "539"     , null      , "BE68 5390 0754 7034" ),                    //SWIFT, ECBS, Nordea
            str( "Benin",                    "false", "BJ11B00610100400271101192591",    null      , null      , "BJ11 B006 1010 0400 2711 0119 2591"),      //Nordea
            str( "Bosnia and Herzegovinia",  "false", "BA391290079401028494",            "129"     , "007"     , "BA39 1290 0794 0102 8494" ),               //SWIFT, ECBS, Nordea
            str( "Brazil",                   "false", "BR9700360305000010009795493P1",   null      , null      , "BR97 0036 0305 0000 1000 9795 493P 1"),    //SWIFT, Nordea
            str( "Bulgaria",                 "true" , "BG80BNBG96611020345678",          null      , null      , "BG80 BNBG 9661 1020 3456 78" ),            //SWIFT, ECBS, Nordea
            str( "Burkina Faso",             "false", "BF1030134020015400945000643",     null      , null      , "BF10 3013 4020 0154 0094 5000 643"),       //Nordea
            str( "Burundi",                  "false", "BI43201011067444",                null      , null      , "BI43 2010 1106 7444"),                     //Nordea
            str( "Cameroon",                 "false", "CM2110003001000500000605306",     null      , null      , "CM21 1000 3001 0005 0000 0605 306"),       //Nordea
            str( "Cape Verde",               "false", "CV64000300004547069110176",       null      , null      , "CV64 0003 0000 4547 0691 1017 6"),         //Nordea
            str( "Congo",                    "false", "CG5230011000202151234567890",     null      , null      , "CG52 3001 1000 2021 5123 4567 890"),       //Nordea
            str( "Costa Rica",               "false", "CR0515202001026284066",           "152"     , null      , "CR05 1520 2001 0262 8406 6"),              //SWIFT, Nordea
            str( "Croatia",                  "true" , "HR1210010051863000160",           "1001005" , null      , "HR12 1001 0051 8630 0016 0" ),             //SWIFT, ECBS, Nordea
            str( "Cyprus",                   "true" , "CY17002001280000001200527600",    "002"     , "00128"   , "CY17 0020 0128 0000 0012 0052 7600" ),     //SWIFT, ECBS, Nordea
            str( "Czech Republic",           "true" , "CZ6508000000192000145399",        "0800"    , null      , "CZ65 0800 0000 1920 0014 5399" ),          //SWIFT, ECBS, Nordea
            str( "Denmark",                  "true" , "DK5000400440116243",              "0040"    , null      , "DK50 0040 0440 1162 43" ),                 //SWIFT, ECBS, Nordea
            str( "Dominican Republic",       "false", "DO28BAGR00000001212453611324",    "BAGR"    , null      , "DO28 BAGR 0000 0001 2124 5361 1324"),      //SWIFT, Nordea
            str( "Egypt",                    "false", "EG1100006001880800100014553",     null      , null      , "EG11 0000 6001 8808 0010 0014 553" ),      //Nordea
            str( "Estonia",                  "true" , "EE382200221020145685",            "22"      , null      , "EE38 2200 2210 2014 5685" ),               //SWIFT, ECBS, Nordea
            str( "Faroe Islands",            "false", "FO1464600009692713",              "6460"    , null      , "FO14 6460 0009 6927 13" ),                 //Nordea
            str( "Finland",                  "true" , "FI2112345600000785",              "123"     , null      , "FI21 1234 5600 0007 85" ),                 //SWIFT, ECBS, Nordea
            str( "France",                   "true" , "FR1420041010050500013M02606",     "20041"   , null      , "FR14 2004 1010 0505 0001 3M02 606" ),      //SWIFT, ECBS, Nordea
            str( "Gabon",                    "false", "GA2140002000055602673300064",     null      , null      , "GA21 4000 2000 0556 0267 3300 064"),       //Nordea
            str( "Georgia",                  "false", "GE29NB0000000101904917",          "NB"      , null      , "GE29 NB00 0000 0101 9049 17"),             //SWIFT, Nordea
            str( "Germany",                  "true" , "DE89370400440532013000",          "37040044", null      , "DE89 3704 0044 0532 0130 00" ),            //SWIFT, ECBS, Nordea
            str( "Gibraltar",                "true" , "GI75NWBK000000007099453",         "NWBK"    , null      , "GI75 NWBK 0000 0000 7099 453" ),           //SWIFT, ECBS, Nordea
            str( "Greece",                   "true" , "GR1601101250000000012300695",     "011"     , "0125"    , "GR16 0110 1250 0000 0001 2300 695" ),      //SWIFT, ECBS, Nordea
            str( "Greenland",                "false", "GL8964710001000206",              "6471"    , null      , "GL89 6471 0001 0002 06"),                  //Nordea
            str( "Guatemala",                "false", "GT82TRAJ01020000001210029690",    "TRAJ"    , null      , "GT82 TRAJ 0102 0000 0012 1002 9690"),      //SWIFT, Nordea
            str( "Hungary",                  "true" , "HU42117730161111101800000000",    "117"     , "7301"    , "HU42 1177 3016 1111 1018 0000 0000" ),     //SWIFT, ECBS, Nordea
            str( "Iceland",                  "true" , "IS140159260076545510730339",      "0159"    , null      , "IS14 0159 2600 7654 5510 7303 39" ),       //SWIFT, ECBS, Nordea
            str( "Iran",                     "false", "IR580540105180021273113007",      null      , null      , "IR58 0540 1051 8002 1273 1130 07"),        //Nordea
            str( "Ireland",                  "true" , "IE29AIBK93115212345678",          "AIBK"    , "931152"  , "IE29 AIBK 9311 5212 3456 78" ),            //SWIFT, ECBS, Nordea
            str( "Israel",                   "false", "IL620108000000099999999",         "010"     , "800"     , "IL62 0108 0000 0009 9999 999" ),           //SWIFT, ECBS, Nordea
            str( "Italy",                    "true" , "IT60X0542811101000000123456",     "05428"   , "11101"   , "IT60 X054 2811 1010 0000 0123 456" ),      //SWIFT, ECBS, Nordea
            str( "Ivory Coast",              "false", "CI05A00060174100178530011852",    null      , null      , "CI05 A000 6017 4100 1785 3001 1852"),      //Nordea
            str( "Jordan",                   "false", "JO94CBJO0010000000000131000302",  "CBJO"    , null      , "JO94 CBJO 0010 0000 0000 0131 0003 02"),   //SWIFT
            str( "Kazakhstan",               "false", "KZ86125KZT5004100100",            "125"     , null      , "KZ86 125K ZT50 0410 0100"),                //SWIFT, Nordea
            str( "Kuwait",                   "false", "KW81CBKU0000000000001234560101",  "CBKU"    , null      , "KW81 CBKU 0000 0000 0000 1234 5601 01"),   //SWIFT, Nordea
            str( "Latvia",                   "true" , "LV80BANK0000435195001",           "BANK"    , null      , "LV80 BANK 0000 4351 9500 1" ),             //SWIFT, ECBS, Nordea
            str( "Lebanon",                  "false", "LB30099900000001001925579115",    "0999"    , null      , "LB30 0999 0000 0001 0019 2557 9115"),      //SWIFT, Nordea
            str( "Liechtenstein",            "true" , "LI21088100002324013AA",           "08810"   , null      , "LI21 0881 0000 2324 013A A" ),             //SWIFT, ECBS, Nordea
            str( "Lithuania",                "true" , "LT121000011101001000",            "10000"   , null      , "LT12 1000 0111 0100 1000" ),               //SWIFT, ECBS, Nordea
            str( "Luxembourg",               "true" , "LU280019400644750000",            "001"     , null      , "LU28 0019 4006 4475 0000" ),               //SWIFT, ECBS, Nordea
            str( "Macedonia",                "false", "MK07250120000058984",             "250"     , null      , "MK07 2501 2000 0058 984" ),                //SWIFT, ECBS, Nordea
            str( "Madagascar",               "false", "MG4600005030010101914016056",     null      , null      , "MG46 0000 5030 0101 0191 4016 056"),       //Nordea
            str( "Mali",                     "false", "ML03D00890170001002120000447",    null      , null      , "ML03 D008 9017 0001 0021 2000 0447"),      //Nordea
            str( "Malta",                    "true" , "MT84MALT011000012345MTLCAST001S", "MALT"    , "01100"   , "MT84 MALT 0110 0001 2345 MTLC AST0 01S" ), //SWIFT, ECBS, Nordea
            str( "Mauritania",               "false", "MR1300020001010000123456753",     "00020"   , "00101"   , "MR13 0002 0001 0100 0012 3456 753"),       //SWIFT, Nordea
            str( "Mauritius",                "false", "MU17BOMM0101101030300200000MUR",  "BOMM01"  , "01"      , "MU17 BOMM 0101 1010 3030 0200 000M UR" ),  //SWIFT, ECBS, Nordea
            str( "Moldova",                  "false", "MD24AG000225100013104168",        "AG"      , null      , "MD24 AG00 0225 1000 1310 4168"),           //SWIFT, Nordea
            str( "Monaco",                   "true" , "MC5811222000010123456789030",     "11222"   , "00001"   , "MC58 1122 2000 0101 2345 6789 030" ),      //SWIFT, ECBS, Nordea
            str( "Montenegro",               "false", "ME25505000012345678951",          "505"     , null      , "ME25 5050 0001 2345 6789 51" ),            //SWIFT, ECBS, Nordea
            str( "Mozambique",               "false", "MZ59000100000011834194157",       null      , null      , "MZ59 0001 0000 0011 8341 9415 7"),         //Nordea
            str( "Netherlands",              "true" , "NL91ABNA0417164300",              "ABNA"    , null      , "NL91 ABNA 0417 1643 00" ),                 //SWIFT, ECBS, Nordea
            str( "Norway",                   "true" , "NO9386011117947",                 "8601"    , null      , "NO93 8601 1117 947" ),                     //SWIFT, ECBS, Nordea
            str( "Pakistan",                 "false", "PK24SCBL0000001171495101",        "SCBL"    , null      , "PK24 SCBL 0000 0011 7149 5101"),           //SWIFT, Nordea
            str( "Palestine, State of",      "false", "PS92PALS000000000400123456702",   "PALS"    , null      , "PS92 PALS 0000 0000 0400 1234 5670 2"),    //SWIFT, Nordea
            str( "Poland",                   "true" , "PL61109010140000071219812874",    "10901014", null      , "PL61 1090 1014 0000 0712 1981 2874" ),     //SWIFT, ECBS, Nordea
            str( "Portugal",                 "true" , "PT50000201231234567890154",       "0002"    , null      , "PT50 0002 0123 1234 5678 9015 4" ),        //SWIFT, ECBS, Nordea
            str( "Quatar",                   "false", "QA58DOHB00001234567890ABCDEFG",   "DOHB"    , null      , "QA58 DOHB 0000 1234 5678 90AB CDEF G"),    //SWIFT
            str( "Republic of Kosovo",       "false", "XK051212012345678906",            "1212"    , "12"      , "XK05 1212 0123 4567 8906"),                //SWIFT
            str( "Romania",                  "true" , "RO49AAAA1B31007593840000",        "AAAA"    , null      , "RO49 AAAA 1B31 0075 9384 0000" ),          //SWIFT, ECBS, Nordea
// NOTE: The IBAN Registry, v58 contains the "LC62" sample IBAN. This fails checksum verification. Corrected to "LC55" below.
//            str( "Saint Lucia",              "false", "LC62HEMM000100010012001200023015",null      , null      , "LC62 HEMM 0001 0001 0012 0012 0002 3015" ),//SWIFT
            str( "Saint Lucia",              "false", "LC55HEMM000100010012001200023015","HEMM"    , null      , "LC55 HEMM 0001 0001 0012 0012 0002 3015" ),//SWIFT
            str( "San Marino",               "true" , "SM86U0322509800000000270100",     "03225"   , "09800"   , "SM86 U032 2509 8000 0000 0270 100" ),      //SWIFT, ECBS, Nordea
            str( "Sao Tome e Principe",      "false", "ST68000100010051845310112",       "0001"    , "0001"    , "ST68 0001 0001 0051 8453 1011 2" ),        //SWIFT
            str( "Saudi Arabia",             "false", "SA0380000000608010167519",        "80"      , null      , "SA03 8000 0000 6080 1016 7519"),           //SWIFT, Nordea
            str( "Senegal",                  "false", "SN12K00100152000025690007542",    null      , null      , "SN12 K001 0015 2000 0256 9000 7542"),      //Nordea
            str( "Serbia",                   "false", "RS35260005601001611379",          "260"     , null      , "RS35 2600 0560 1001 6113 79" ),            //SWIFT, ECBS, Nordea
// NOTE: The IBAN Registry, v65, contains the "SC25" sample IBAN, which fails checksum verification.
//            str( "Seychelles",               "false", "SC25SSCB11010000000000001497USD", "SSCB1101", null      , "SC25 SSCB 1101 0000 0000 0000 1497 USD" ), //SWIFT
            str( "Seychelles",               "false", "SC18SSCB11010000000000001497USD", "SSCB1101", null      , "SC18 SSCB 1101 0000 0000 0000 1497 USD" ), //SWIFT
            str( "Slovak Republic",          "true" , "SK3112000000198742637541",        "1200"    , null      , "SK31 1200 0000 1987 4263 7541" ),          //SWIFT, ECBS, Nordea
            str( "Slovenia",                 "true" , "SI56263300012039086",             "26330"   , null      , "SI56 2633 0001 2039 086" ),                //SWIFT, ECBS, Nordea
            str( "Spain",                    "true" , "ES9121000418450200051332",        "2100"    , "0418"    , "ES91 2100 0418 4502 0005 1332" ),          //SWIFT, ECBS, Nordea
            str( "Sweden",                   "true" , "SE4550000000058398257466",        "500"     , null      , "SE45 5000 0000 0583 9825 7466" ),          //SWIFT, ECBS, Nordea
            str( "Switzerland",              "true" , "CH9300762011623852957",           "00762"   , null      , "CH93 0076 2011 6238 5295 7" ),             //SWIFT, ECBS, Nordea
            str( "Timor-Leste",              "false", "TL380080012345678910157",         "008"     , null      , "TL38 0080 0123 4567 8910 157"),            //SWIFT
            str( "Tunisia",                  "false", "TN5910006035183598478831",        "10"      , "006"     , "TN59 1000 6035 1835 9847 8831"),           //SWIFT, Nordea
            str( "Turkey",                   "false", "TR330006100519786457841326",      "00061"   , null      , "TR33 0006 1005 1978 6457 8413 26" ),       //SWIFT, ECBS, Nordea
            str( "Ukraine",                  "false", "UA213996220000026007233566001",   "399622"  , null      , "UA21 3996 2200 0002 6007 2335 6600 1"),    //SWIFT
            str( "United Arab Emirates",     "false", "AE070331234567890123456",         "033"     , null      , "AE07 0331 2345 6789 0123 456"),            //SWIFT, Nordea
            str( "United Kingdom",           "true" , "GB29NWBK60161331926819",          "NWBK"    , "601613"  , "GB29 NWBK 6016 1331 9268 19" ),            //SWIFT, ECBS, Nordea
            str( "Virgin Islands, British",  "false", "VG96VPVG0000012345678901",        "VPVG"    , null      , "VG96 VPVG 0000 0123 4567 8901")            //SWIFT, Nordea
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
