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

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.collection.IsEmptyCollection.empty;

/**
 * Ensures that the {@link nl.garvelink.iban.IBAN} class accepts IBAN numbers from every participating country (...known at the time the test was last updated).
 */
@RunWith(Parameterized.class)
public class CountryCodesParameterizedTest {

    private final TestData td;

    @SuppressWarnings("unused")
    public CountryCodesParameterizedTest(TestData td) {
        this.td = td;
    }

    /**
     * List of valid international IBAN's.
     * <p>References:</p>
     * <dl>
     *     <dt>SWIFT</dt>
     *     <dd>https://www.swift.com/resource/iban-registry-pdf</dd>
     *     <dt>IBAN.com Experimental List</dt>
     *     <dd>https://www.iban.com/structure</dd>
     * </dl>
     */
    static final List<TestData> PARAMETERS = Stream.of(
            // Name                          SWIFT  SEPA   Plain                              Bank        Branch      Pretty
            // Countries in the SWIFT reference, sorted by Country Code
            td( "Andorra",                    true , false, "AD1200012030200359100100",        "0001"    , "2030"    , "AD12 0001 2030 2003 5910 0100" ),          //SWIFT
            td( "United Arab Emirates (The)", true , false, "AE070331234567890123456",         "033"     , null      , "AE07 0331 2345 6789 0123 456"),            //SWIFT
            td( "Albania",                    true , false, "AL47212110090000000235698741",    "21211009", "1100"    , "AL47 2121 1009 0000 0002 3569 8741"),      //SWIFT
            td( "Austria",                    true , true , "AT611904300234573201",            "19043"   , null      , "AT61 1904 3002 3457 3201" ),               //SWIFT
            td( "Azerbaijan",                 true , false, "AZ21NABZ00000000137010001944",    "NABZ"    , null      , "AZ21 NABZ 0000 0000 1370 1000 1944"),      //SWIFT
            td( "Bosnia and Herzegovinia",    true , false, "BA391290079401028494",            "129"     , "007"     , "BA39 1290 0794 0102 8494" ),               //SWIFT
            td( "Belgium",                    true , true , "BE68539007547034",                "539"     , null      , "BE68 5390 0754 7034" ),                    //SWIFT
            td( "Bulgaria",                   true , true , "BG80BNBG96611020345678",          "BNBG"    , "9661"    , "BG80 BNBG 9661 1020 3456 78" ),            //SWIFT
            td( "Bahrain",                    true , false, "BH67BMAG00001299123456",          "BMAG"    , null      , "BH67 BMAG 0000 1299 1234 56"),             //SWIFT
            td( "Brazil",                     true , false, "BR1800360305000010009795493C1",   "00360305", "00001"   , "BR18 0036 0305 0000 1000 9795 493C 1"),    //SWIFT
            td( "Republic of Belarus",        true , false, "BY13NBRB3600900000002Z00AB00",    "NBRB"    , null      , "BY13 NBRB 3600 9000 0000 2Z00 AB00"),      //SWIFT
            td( "Switzerland",                true , true , "CH9300762011623852957",           "00762"   , null      , "CH93 0076 2011 6238 5295 7" ),             //SWIFT
            td( "Costa Rica",                 true , false, "CR05015202001026284066",          "0152"    , null      , "CR05 0152 0200 1026 2840 66"),             //SWIFT
            td( "Cyprus",                     true , true , "CY17002001280000001200527600",    "002"     , "00128"   , "CY17 0020 0128 0000 0012 0052 7600" ),     //SWIFT
            td( "Czech Republic",             true , true , "CZ6508000000192000145399",        "0800"    , null      , "CZ65 0800 0000 1920 0014 5399" ),          //SWIFT
            td( "Germany",                    true , true , "DE89370400440532013000",          "37040044", null      , "DE89 3704 0044 0532 0130 00" ),            //SWIFT
            td( "Denmark",                    true , true , "DK5000400440116243",              "0040"    , null      , "DK50 0040 0440 1162 43" ),                 //SWIFT
            td( "Dominican Republic",         true , false, "DO28BAGR00000001212453611324",    "BAGR"    , null      , "DO28 BAGR 0000 0001 2124 5361 1324"),      //SWIFT
            td( "Estonia",                    true , true , "EE382200221020145685",            "22"      , null      , "EE38 2200 2210 2014 5685" ),               //SWIFT
            td( "Egypt",                      true , false, "EG380019000500000000263180002",   "0019"    , "0005"    , "EG38 0019 0005 0000 0000 2631 8000 2" ),   //SWIFT
            td( "Spain",                      true , true , "ES9121000418450200051332",        "2100"    , "0418"    , "ES91 2100 0418 4502 0005 1332" ),          //SWIFT
            td( "Finland",                    true , true , "FI2112345600000785",              "123"     , null      , "FI21 1234 5600 0007 85" ),                 //SWIFT
            td( "Faroe Islands",              true , false, "FO6264600001631634",              "6460"    , null      , "FO62 6460 0001 6316 34" ),                 //SWIFT
            td( "France",                     true , true , "FR1420041010050500013M02606",     "20041"   , null      , "FR14 2004 1010 0505 0001 3M02 606" ),      //SWIFT
            td( "United Kingdom",             true , true , "GB29NWBK60161331926819",          "NWBK"    , "601613"  , "GB29 NWBK 6016 1331 9268 19" ),            //SWIFT
            td( "Georgia",                    true , false, "GE29NB0000000101904917",          "NB"      , null      , "GE29 NB00 0000 0101 9049 17"),             //SWIFT
            td( "Gibraltar",                  true , true , "GI75NWBK000000007099453",         "NWBK"    , null      , "GI75 NWBK 0000 0000 7099 453" ),           //SWIFT
            td( "Greenland",                  true , false, "GL8964710001000206",              "6471"    , null      , "GL89 6471 0001 0002 06"),                  //SWIFT
            td( "Greece",                     true , true , "GR1601101250000000012300695",     "011"     , "0125"    , "GR16 0110 1250 0000 0001 2300 695" ),      //SWIFT
            td( "Guatemala",                  true , false, "GT82TRAJ01020000001210029690",    "TRAJ"    , null      , "GT82 TRAJ 0102 0000 0012 1002 9690"),      //SWIFT
            td( "Croatia",                    true , true , "HR1210010051863000160",           "1001005" , null      , "HR12 1001 0051 8630 0016 0" ),             //SWIFT
            td( "Hungary",                    true , true , "HU42117730161111101800000000",    "117"     , "7301"    , "HU42 1177 3016 1111 1018 0000 0000" ),     //SWIFT
            td( "Ireland",                    true , true , "IE29AIBK93115212345678",          "AIBK"    , "931152"  , "IE29 AIBK 9311 5212 3456 78" ),            //SWIFT
            td( "Israel",                     true , false, "IL620108000000099999999",         "010"     , "800"     , "IL62 0108 0000 0009 9999 999" ),           //SWIFT
            td( "Iraq",                       true , false, "IQ98NBIQ850123456789012",         "NBIQ"    , "850"     , "IQ98 NBIQ 8501 2345 6789 012"),            //SWIFT
            td( "Iceland",                    true , true , "IS140159260076545510730339",      "01"      , "59"      , "IS14 0159 2600 7654 5510 7303 39" ),       //SWIFT
            td( "Italy",                      true , true , "IT60X0542811101000000123456",     "05428"   , "11101"   , "IT60 X054 2811 1010 0000 0123 456" ),      //SWIFT
            td( "Jordan",                     true , false, "JO94CBJO0010000000000131000302",  "CBJO"    , null      , "JO94 CBJO 0010 0000 0000 0131 0003 02"),   //SWIFT
            td( "Kuwait",                     true , false, "KW81CBKU0000000000001234560101",  "CBKU"    , null      , "KW81 CBKU 0000 0000 0000 1234 5601 01"),   //SWIFT
            td( "Kazakhstan",                 true , false, "KZ86125KZT5004100100",            "125"     , null      , "KZ86 125K ZT50 0410 0100"),                //SWIFT
            td( "Lebanon",                    true , false, "LB62099900000001001901229114",    "0999"    , null      , "LB62 0999 0000 0001 0019 0122 9114"),      //SWIFT
            td( "Saint Lucia",                true , false, "LC55HEMM000100010012001200023015","HEMM"    , null      , "LC55 HEMM 0001 0001 0012 0012 0002 3015" ),//SWIFT
            td( "Liechtenstein",              true , true , "LI21088100002324013AA",           "08810"   , null      , "LI21 0881 0000 2324 013A A" ),             //SWIFT
            td( "Lithuania",                  true , true , "LT121000011101001000",            "10000"   , null      , "LT12 1000 0111 0100 1000" ),               //SWIFT
            td( "Luxembourg",                 true , true , "LU280019400644750000",            "001"     , null      , "LU28 0019 4006 4475 0000" ),               //SWIFT
            td( "Latvia",                     true , true , "LV80BANK0000435195001",           "BANK"    , null      , "LV80 BANK 0000 4351 9500 1" ),             //SWIFT
            td( "Libya",                      true , false, "LY83002048000020100120361",       "002"     , "048"     , "LY83 0020 4800 0020 1001 2036 1"),         //SWIFT
            td( "Monaco",                     true , true , "MC5811222000010123456789030",     "11222"   , "00001"   , "MC58 1122 2000 0101 2345 6789 030" ),      //SWIFT
            td( "Moldova",                    true , false, "MD24AG000225100013104168",        "AG"      , null      , "MD24 AG00 0225 1000 1310 4168"),           //SWIFT
            td( "Montenegro",                 true , false, "ME25505000012345678951",          "505"     , null      , "ME25 5050 0001 2345 6789 51" ),            //SWIFT
            td( "Macedonia",                  true , false, "MK07250120000058984",             "250"     , null      , "MK07 2501 2000 0058 984" ),                //SWIFT
            td( "Mauritania",                 true , false, "MR1300020001010000123456753",     "00020"   , "00101"   , "MR13 0002 0001 0100 0012 3456 753"),       //SWIFT
            td( "Malta",                      true , true , "MT84MALT011000012345MTLCAST001S", "MALT"    , "01100"   , "MT84 MALT 0110 0001 2345 MTLC AST0 01S" ), //SWIFT
            td( "Mauritius",                  true , false, "MU17BOMM0101101030300200000MUR",  "BOMM01"  , "01"      , "MU17 BOMM 0101 1010 3030 0200 000M UR" ),  //SWIFT
            td( "Netherlands (The)",          true , true , "NL91ABNA0417164300",              "ABNA"    , null      , "NL91 ABNA 0417 1643 00" ),                 //SWIFT
            td( "Norway",                     true , true , "NO9386011117947",                 "8601"    , null      , "NO93 8601 1117 947" ),                     //SWIFT
            td( "Pakistan",                   true , false, "PK36SCBL0000001123456702",        "SCBL"    , null      , "PK36 SCBL 0000 0011 2345 6702"),           //SWIFT
            td( "Poland",                     true , true , "PL61109010140000071219812874",    null      , "10901014", "PL61 1090 1014 0000 0712 1981 2874" ),     //SWIFT
            td( "Palestine, State of",        true , false, "PS92PALS000000000400123456702",   "PALS"    , null      , "PS92 PALS 0000 0000 0400 1234 5670 2"),    //SWIFT
            td( "Portugal",                   true , true , "PT50000201231234567890154",       "0002"    , null      , "PT50 0002 0123 1234 5678 9015 4" ),        //SWIFT
            td( "Qatar",                      true , false, "QA58DOHB00001234567890ABCDEFG",   "DOHB"    , null      , "QA58 DOHB 0000 1234 5678 90AB CDEF G"),    //SWIFT
            td( "Romania",                    true , true , "RO49AAAA1B31007593840000",        "AAAA"    , null      , "RO49 AAAA 1B31 0075 9384 0000" ),          //SWIFT
            td( "Serbia",                     true , false, "RS35260005601001611379",          "260"     , null      , "RS35 2600 0560 1001 6113 79" ),            //SWIFT
            td( "Saudi Arabia",               true , false, "SA0380000000608010167519",        "80"      , null      , "SA03 8000 0000 6080 1016 7519"),           //SWIFT
            td( "Seychelles",                 true , false, "SC18SSCB11010000000000001497USD", "SSCB11"  , "01"      , "SC18 SSCB 1101 0000 0000 0000 1497 USD" ), //SWIFT
            td( "Sweden",                     true , true , "SE4550000000058398257466",        "500"     , null      , "SE45 5000 0000 0583 9825 7466" ),          //SWIFT
            td( "Slovenia",                   true , true , "SI56263300012039086",             "26330"   , null      , "SI56 2633 0001 2039 086" ),                //SWIFT
            td( "Slovakia",                   true , true , "SK3112000000198742637541",        "1200"    , null      , "SK31 1200 0000 1987 4263 7541" ),          //SWIFT
            td( "San Marino",                 true , true , "SM86U0322509800000000270100",     "03225"   , "09800"   , "SM86 U032 2509 8000 0000 0270 100" ),      //SWIFT
            td( "Sao Tome e Principe",        true , false, "ST23000100010051845310146",       "0001"    , "0001"    , "ST23 0001 0001 0051 8453 1014 6" ),        //SWIFT
            td( "El Salvador",                true , false, "SV62CENR00000000000000700025",    "CENR"    , null      , "SV62 CENR 0000 0000 0000 0070 0025"),      //SWIFT
            td( "Timor-Leste",                true , false, "TL380080012345678910157",         "008"     , null      , "TL38 0080 0123 4567 8910 157"),            //SWIFT
            td( "Tunisia",                    true , false, "TN5910006035183598478831",        "10"      , "006"     , "TN59 1000 6035 1835 9847 8831"),           //SWIFT
            td( "Turkey",                     true , false, "TR330006100519786457841326",      "00061"   , null      , "TR33 0006 1005 1978 6457 8413 26" ),       //SWIFT
            td( "Ukraine",                    true , false, "UA213223130000026007233566001",   "322313"  , null      , "UA21 3223 1300 0002 6007 2335 6600 1"),    //SWIFT
            td( "Vatican City State",         true , true , "VA59001123000012345678",          "001"     , null      , "VA59 0011 2300 0012 3456 78" ),            //SWIFT
            td( "Virgin Islands",             true , false, "VG96VPVG0000012345678901",        "VPVG"    , null      , "VG96 VPVG 0000 0123 4567 8901"),           //SWIFT
            td( "Kosovo",                     true , false, "XK051212012345678906",            "12"      , "12"      , "XK05 1212 0123 4567 8906"),                //SWIFT
            // Countries in the IBAN.com Experimental List, sorted by Name
            td( "Algeria",                    false, false, "DZ580002100001113000000570",      null      , null      , "DZ58 0002 1000 0111 3000 0005 70"),        //IBAN.com Experimental List
            td( "Angola",                     false, false, "AO06004400006729503010102",       null      , null      , "AO06 0044 0000 6729 5030 1010 2"),         //IBAN.com Experimental List
            td( "Benin",                      false, false, "BJ66BJ0610100100144390000769",    null      , null      , "BJ66 BJ06 1010 0100 1443 9000 0769"),      //IBAN.com Experimental List
            td( "Burkina Faso",               false, false, "BF42BF0840101300463574000390",    null      , null      , "BF42 BF08 4010 1300 4635 7400 0390"),      //IBAN.com Experimental List
            td( "Burundi",                    false, false, "BI43201011067444",                null      , null      , "BI43 2010 1106 7444"),                     //IBAN.com Experimental List
            td( "Cameroon",                   false, false, "CM2110002000300277976315008",     null      , null      , "CM21 1000 2000 3002 7797 6315 008"),       //IBAN.com Experimental List
            td( "Cape Verde",                 false, false, "CV64000500000020108215144",       null      , null      , "CV64 0005 0000 0020 1082 1514 4"),         //IBAN.com Experimental List
            td( "Central African Republic",   false, false, "CF4220001000010120069700160",     null      , null      , "CF42 2000 1000 0101 2006 9700 160"),       //IBAN.com Experimental List
            td( "Chad",                       false, false, "TD8960002000010271091600153",     null      , null      , "TD89 6000 2000 0102 7109 1600 153"),       //IBAN.com Experimental List
            td( "Comoros",                    false, false, "KM4600005000010010904400137",     null      , null      , "KM46 0000 5000 0100 1090 4400 137"),       //IBAN.com Experimental List
            td( "Congo",                      false, false, "CG3930011000101013451300019",     null      , null      , "CG39 3001 1000 1010 1345 1300 019"),       //IBAN.com Experimental List
            td( "Djibouti",                   false, false, "DJ2110002010010409943020008",     null      , null      , "DJ21 1000 2010 0104 0994 3020 008" ),      //IBAN.com Experimental List
            td( "Equatorial Guinea",          false, false, "GQ7050002001003715228190196",     null      , null      , "GQ70 5000 2001 0037 1522 8190 196" ),      //IBAN.com Experimental List
            td( "Gabon",                      false, false, "GA2140021010032001890020126",     null      , null      , "GA21 4002 1010 0320 0189 0020 126"),       //IBAN.com Experimental List
            td( "Guinea-Bissau",              false, false, "GW04GW1430010181800637601",       null      , null      , "GW04 GW14 3001 0181 8006 3760 1"),         //IBAN.com Experimental List
            td( "Honduras",                   false, false, "HN54PISA00000000000000123124",    null      , null      , "HN54 PISA 0000 0000 0000 0012 3124"),      //IBAN.com Experimental List
            td( "Iran",                       false, false, "IR710570029971601460641001",      null      , null      , "IR71 0570 0299 7160 1460 6410 01"),        //IBAN.com Experimental List
            td( "Ivory Coast",                false, false, "CI93CI0080111301134291200589",    null      , null      , "CI93 CI00 8011 1301 1342 9120 0589"),      //IBAN.com Experimental List
            td( "Madagascar",                 false, false, "MG4600005030071289421016045",     null      , null      , "MG46 0000 5030 0712 8942 1016 045"),       //IBAN.com Experimental List
            td( "Mali",                       false, false, "ML13ML0160120102600100668497",    null      , null      , "ML13 ML01 6012 0102 6001 0066 8497"),      //IBAN.com Experimental List
            td( "Morocco",                    false, false, "MA64011519000001205000534921",    null      , null      , "MA64 0115 1900 0001 2050 0053 4921"),      //IBAN.com Experimental List
            td( "Mozambique",                 false, false, "MZ59000301080016367102371",       null      , null      , "MZ59 0003 0108 0016 3671 0237 1"),         //IBAN.com Experimental List
            td( "Nicaragua",                  false, false, "NI92BAMC000000000000000003123123",null      , null      , "NI92 BAMC 0000 0000 0000 0000 0312 3123"), //IBAN.com Experimental List
            td( "Niger",                      false, false, "NE58NE0380100100130305000268",    null      , null      , "NE58 NE03 8010 0100 1303 0500 0268"),      //IBAN.com Experimental List
            td( "Senegal",                    false, false, "SN08SN0100152000048500003035",    null      , null      , "SN08 SN01 0015 2000 0485 0000 3035"),      //IBAN.com Experimental List
            td( "Togo",                       false, false, "TG53TG0090604310346500400070",    null      , null      , "TG53 TG00 9060 4310 3465 0040 0070")       //IBAN.com Experimental List
        ).sorted(Comparator.comparing(td -> td.name)).collect(Collectors.toList());

    @Parameterized.Parameters(name = " {0} ")
    public static List<TestData> parameters() {
        return PARAMETERS;
    }

    @Test
    public void getLengthForCountryCodeShouldReturnCorrectValue() {
        assertThat(td.plain.length(), is(equalTo(CountryCodes.getLengthForCountryCode(td.plain.substring(0, 2)))));
    }

    @Test
    public void isKnownCountryCodeShouldReturnTrue() {
        assertThat(CountryCodes.isKnownCountryCode(td.plain.substring(0, 2)), is(true));
    }

    /**
     * Used by the {@code xxxTestShouldBeExhaustive()} methods to ensure that there are no untested country codes
     * defined in the {@code CountryCodes} class. The inverse, a country code that exists in the test but not in
     * the application code does not have to be separately verified.
     * @see #prepareTestShouldBeExhaustive()
     * @see #updateTestShouldBeExhaustive()
     * @see #finishTestShouldBeExhaustive()
     */
    private static final Set<String> allCountryCodes = Collections.synchronizedSet(new HashSet<>(PARAMETERS.size()));

    @BeforeClass
    public static void prepareTestShouldBeExhaustive() {
        allCountryCodes.addAll(CountryCodes.getKnownCountryCodes());
    }

    @Test
    public void updateTestShouldBeExhaustive() {
        allCountryCodes.remove(td.plain.substring(0, 2));
    }

    @AfterClass
    public static void finishTestShouldBeExhaustive() {
        assertThat("There are entries in CountryCodes.java that are not covered in test: " + allCountryCodes,
                allCountryCodes, is(empty()));
    }

    private static TestData td(String name, boolean swift, boolean sepa, String plain, String bank, String branch, String pretty) {
        return new TestData(name, swift, sepa, plain, bank, branch, pretty);
    }
}
