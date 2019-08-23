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

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;

/**
 * Contains information about IBAN country codes.
 */
public abstract class CountryCodes {

    private static final int SEPA = 1 << 8;
    private static final int SWIFT = 1 << 9;
    private static final int REMOVE_METADATA_MASK = 0xFF;

    /**
     * Known country codes, this list must be sorted to allow binary search.
     */
    private static final String[] COUNTRY_CODES = {
            "AD",
            "AE",
            "AL",
            "AO",
            "AT",
            "AZ",
            "BA",
            "BE",
            "BF",
            "BG",
            "BH",
            "BI",
            "BJ",
            "BR",
            "BY",
            "CF",
            "CG",
            "CH",
            "CI",
            "CM",
            "CR",
            "CV",
            "CY",
            "CZ",
            "DE",
            "DJ",
            "DK",
            "DO",
            "DZ",
            "EE",
            "EG",
            "ES",
            "FI",
            "FO",
            "FR",
            "GA",
            "GB",
            "GE",
            "GI",
            "GL",
            "GQ",
            "GR",
            "GT",
            "GW",
            "HN",
            "HR",
            "HU",
            "IE",
            "IL",
            "IQ",
            "IR",
            "IS",
            "IT",
            "JO",
            "KM",
            "KW",
            "KZ",
            "LB",
            "LC",
            "LI",
            "LT",
            "LU",
            "LV",
            "MA",
            "MC",
            "MD",
            "ME",
            "MG",
            "MK",
            "ML",
            "MR",
            "MT",
            "MU",
            "MZ",
            "NE",
            "NI",
            "NL",
            "NO",
            "PK",
            "PL",
            "PS",
            "PT",
            "QA",
            "RO",
            "RS",
            "SA",
            "SC",
            "SE",
            "SI",
            "SK",
            "SM",
            "SN",
            "ST",
            "SV",
            "TD",
            "TG",
            "TL",
            "TN",
            "TR",
            "UA",
            "VA",
            "VG",
            "XK",
        };
    /**
     * Lengths for each country's IBAN. The indices match the indices of {@link #COUNTRY_CODES}, the values are the expected length.
     */
    private static final int[] COUNTRY_IBAN_LENGTHS = {
            24        | SWIFT /* AD */,
            23        | SWIFT /* AE */,
            28        | SWIFT /* AL */,
            25                /* AO */,
            20 | SEPA | SWIFT /* AT */,
            28        | SWIFT /* AZ */,
            20        | SWIFT /* BA */,
            16 | SEPA | SWIFT /* BE */,
            28                /* BF */,
            22 | SEPA | SWIFT /* BG */,
            22        | SWIFT /* BH */,
            16                /* BI */,
            28                /* BJ */,
            29        | SWIFT /* BR */,
            28        | SWIFT /* BY */,
            27                /* CF */,
            27                /* CG */,
            21 | SEPA | SWIFT /* CH */,
            28                /* CI */,
            27                /* CM */,
            22        | SWIFT /* CR */,
            25                /* CV */,
            28 | SEPA | SWIFT /* CY */,
            24 | SEPA | SWIFT /* CZ */,
            22 | SEPA | SWIFT /* DE */,
            27                /* DJ */,
            18 | SEPA | SWIFT /* DK */,
            28        | SWIFT /* DO */,
            26                /* DZ */,
            20 | SEPA | SWIFT /* EE */,
            27                /* EG */,
            24 | SEPA | SWIFT /* ES */,
            18 | SEPA | SWIFT /* FI */,
            18        | SWIFT /* FO */,
            27 | SEPA | SWIFT /* FR */,
            27                /* GA */,
            22 | SEPA | SWIFT /* GB */,
            22        | SWIFT /* GE */,
            23 | SEPA | SWIFT /* GI */,
            18        | SWIFT /* GL */,
            27                /* GQ */,
            27 | SEPA | SWIFT /* GR */,
            28        | SWIFT /* GT */,
            25                /* GW */,
            28                /* HN */,
            21 | SEPA | SWIFT /* HR */,
            28 | SEPA | SWIFT /* HU */,
            22 | SEPA | SWIFT /* IE */,
            23        | SWIFT /* IL */,
            23        | SWIFT /* IQ */,
            26                /* IR */,
            26 | SEPA | SWIFT /* IS */,
            27 | SEPA | SWIFT /* IT */,
            30        | SWIFT /* JO */,
            27                /* KM */,
            30        | SWIFT /* KW */,
            20        | SWIFT /* KZ */,
            28        | SWIFT /* LB */,
            32        | SWIFT /* LC */,
            21 | SEPA | SWIFT /* LI */,
            20 | SEPA | SWIFT /* LT */,
            20 | SEPA | SWIFT /* LU */,
            21 | SEPA | SWIFT /* LV */,
            28                /* MA */,
            27 | SEPA | SWIFT /* MC */,
            24        | SWIFT /* MD */,
            22        | SWIFT /* ME */,
            27                /* MG */,
            19        | SWIFT /* MK */,
            28                /* ML */,
            27        | SWIFT /* MR */,
            31 | SEPA | SWIFT /* MT */,
            30        | SWIFT /* MU */,
            25                /* MZ */,
            28                /* NE */,
            32                /* NI */,
            18 | SEPA | SWIFT /* NL */,
            15 | SEPA | SWIFT /* NO */,
            24        | SWIFT /* PK */,
            28 | SEPA | SWIFT /* PL */,
            29        | SWIFT /* PS */,
            25 | SEPA | SWIFT /* PT */,
            29        | SWIFT /* QA */,
            24 | SEPA | SWIFT /* RO */,
            22        | SWIFT /* RS */,
            24        | SWIFT /* SA */,
            31        | SWIFT /* SC */,
            24 | SEPA | SWIFT /* SE */,
            19 | SEPA | SWIFT /* SI */,
            24 | SEPA | SWIFT /* SK */,
            27 | SEPA | SWIFT /* SM */,
            28                /* SN */,
            25        | SWIFT /* ST */,
            28        | SWIFT /* SV */,
            27                /* TD */,
            28                /* TG */,
            23        | SWIFT /* TL */,
            24        | SWIFT /* TN */,
            26        | SWIFT /* TR */,
            29        | SWIFT /* UA */,
            22 | SEPA | SWIFT /* VA */,
            24        | SWIFT /* VG */,
            20        | SWIFT /* XK */,
        };

    /**
     * The shortest valid IBAN according to {@link #COUNTRY_IBAN_LENGTHS}
     */
    public static final int SHORTEST_IBAN_LENGTH;

    /**
     * The longest valid IBAN according to {@link #COUNTRY_IBAN_LENGTHS}
     */
    public static final int LONGEST_IBAN_LENGTH;

    static {
        int min = Integer.MAX_VALUE;
        int max = 0;
        for (int countryIbanLength : COUNTRY_IBAN_LENGTHS) {
            final int length = REMOVE_METADATA_MASK & countryIbanLength;
            if (length > max) {
                max = length;
            }
            if (length < min) {
                min = length;
            }
        }
        SHORTEST_IBAN_LENGTH = min;
        LONGEST_IBAN_LENGTH = max;
    }

    /**
     * Returns the index of the given country code in {@link #COUNTRY_CODES} by binary search.
     * @param countryCode a country code.
     * @return the array index, or -1.
     */
    static int indexOf(String countryCode) {
        return Arrays.binarySearch(CountryCodes.COUNTRY_CODES, countryCode);
    }

    /**
     * Returns the IBAN length for a given country code.
     * @param countryCode a non-null, uppercase, two-character country code.
     * @return the IBAN length for the given country, or -1 if the input is not a known, two-character country code.
     * @throws NullPointerException if the input is null.
     */
    public static int getLengthForCountryCode(String countryCode) {
        int index = indexOf(countryCode);
        if (index > -1) {
            return CountryCodes.COUNTRY_IBAN_LENGTHS[index] & REMOVE_METADATA_MASK;
        }
        return -1;
    }

    /**
     * Returns whether the given country code is in SEPA.
     * @param countryCode a non-null, uppercase, two-character country code.
     * @return true if SEPA, false if not.
     * @throws NullPointerException if the input is null.
     */
    public static boolean isSEPACountry(String countryCode) {
        int index = indexOf(countryCode);
        if (index > -1) {
            return (CountryCodes.COUNTRY_IBAN_LENGTHS[index] & SEPA) == SEPA;
        }
        return false;
    }

    /**
     * Returns whether the source for this IBAN's format and data is the SWIFT IBAN Registry.
     * @param countryCode a non-null, uppercase, two-character country code.
     * @return true if our data is from the SWIFT IBAN Registry, false if not.
     * @throws NullPointerException if the input is null.
     */
    public static boolean isInSwiftRegistry(String countryCode) {
        int index = indexOf(countryCode);
        if (index > -1) {
            return (CountryCodes.COUNTRY_IBAN_LENGTHS[index] & SWIFT) == SWIFT;
        }
        return false;
    }

    /**
     * Returns the known country codes.
     * @return the collection of known country codes, upper case, in alphabetical order.
     */
    public static Collection<String> getKnownCountryCodes() {
        return Collections.unmodifiableList(Arrays.asList(COUNTRY_CODES));
    }

    /**
     * Returns whether the given string is a known country code.
     * @param aCountryCode the string to evaluate.
     * @return {@code true} if {@code aCountryCode} is a two-letter, uppercase String present in {@link #getKnownCountryCodes()}.
     */
    public static boolean isKnownCountryCode(String aCountryCode) {
        if (aCountryCode == null || aCountryCode.length() != 2) {
            return false;
        }
        return indexOf(aCountryCode) >= 0;
    }

    /** Prevent instantiation of static utility class. */
    private CountryCodes() { }
}
