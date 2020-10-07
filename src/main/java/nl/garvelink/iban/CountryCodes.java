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
    private static final int BANK_IDENTIFIER_BEGIN_MASK = 0xFF;
    private static final int BANK_IDENTIFIER_END_SHIFT = 8;
    private static final int BANK_IDENTIFIER_END_MASK = 0xFF << BANK_IDENTIFIER_END_SHIFT;
    private static final int BRANCH_IDENTIFIER_BEGIN_SHIFT = 16;
    private static final int BRANCH_IDENTIFIER_BEGIN_MASK = 0xFF << BRANCH_IDENTIFIER_BEGIN_SHIFT;
    private static final int BRANCH_IDENTIFIER_END_SHIFT = 24;
    private static final int BRANCH_IDENTIFIER_END_MASK = 0xFF << BRANCH_IDENTIFIER_END_SHIFT;

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
     * Contains the start- and end-index (as per {@link String#substring(int, int)}) of the bank code and branch code
     * within a country's IBAN format. Mask:
     * <pre>
     * 0x000000FF <- begin offset bank id
     * 0x0000FF00 <- end offset bank id
     * 0x00FF0000 <- begin offset branch id
     * 0xFF000000 <- end offset branch id
     * </pre>
     */
    private static final int[] BANK_CODE_BRANCH_CODE = {
            4 |  8 << 8 |  8 << 16 | 12 << 24 /* AD */,
            4 |  7 << 8                       /* AE */,
            4 |  7 << 8 |  7 << 16 | 11 << 24 /* AL */,
            0                                 /* AO */,
            4 |  9 << 8                       /* AT */,
            4 |  8 << 8                       /* AZ */,
            4 |  7 << 8 |  7 << 16 | 10 << 24 /* BA */,
            4 |  7 << 8                       /* BE */,
            0                                 /* BF */,
            4 |  8 << 8 |  8 << 16 | 12 << 24 /* BG */,
            4 |  8 << 8                       /* BH */,
            0                                 /* BI */,
            0                                 /* BJ */,
            4 | 12 << 8 | 12 << 16 | 17 << 24 /* BR */,
            4 |  8 << 8                       /* BY */,
            0                                 /* CF */,
            0                                 /* CG */,
            4 |  9 << 8                       /* CH */,
            0                                 /* CI */,
            0                                 /* CM */,
            4 |  8 << 8                       /* CR */,
            0                                 /* CV */,
            4 |  7 << 8 |  7 << 16 | 12 << 24 /* CY */,
            4 |  8 << 8                       /* CZ */,
            4 | 12 << 8                       /* DE */,
            0                                 /* DJ */,
            4 |  8 << 8                       /* DK */,
            4 |  8 << 8                       /* DO */,
            0                                 /* DZ */,
            4 |  6 << 8                       /* EE */,
            0                                 /* EG */,
            4 |  8 << 8 |  8 << 16 | 12 << 24 /* ES */,
            4 |  7 << 8                       /* FI */, // The SWIFT spec is a bit vague here, says both "positions 1-3" and "not in use".
            4 |  8 << 8                       /* FO */,
            4 |  9 << 8                       /* FR */,
            0                                 /* GA */,
            4 |  8 << 8 |  8 << 16 | 14 << 24 /* GB */,
            4 |  6 << 8                       /* GE */,
            4 |  8 << 8                       /* GI */,
            4 |  8 << 8                       /* GL */,
            0                                 /* GQ */,
            4 |  7 << 8 |  7 << 16 | 11 << 24 /* GR */,
            4 |  8 << 8                       /* GT */,
            0                                 /* GW */,
            0                                 /* HN */,
            4 | 11 << 8                       /* HR */,
            4 |  7 << 8 |  7 << 16 | 11 << 24 /* HU */,
            4 |  8 << 8 |  8 << 16 | 14 << 24 /* IE */,
            4 |  7 << 8 |  7 << 16 | 10 << 24 /* IL */,
            4 |  8 << 8 |  8 << 16 | 11 << 24 /* IQ */,
            0                                 /* IR */,
            4 |  8 << 8                       /* IS */,
            5 | 10 << 8 | 10 << 16 | 15 << 24 /* IT */,
            4 |  8 << 8                       /* JO */,
            0                                 /* KM */,
            4 |  8 << 8                       /* KW */,
            4 |  7 << 8                       /* KZ */,
            4 |  8 << 8                       /* LB */,
            4 |  8 << 8                       /* LC */,
            4 |  9 << 8                       /* LI */,
            4 |  9 << 8                       /* LT */,
            4 |  7 << 8                       /* LU */,
            4 |  8 << 8                       /* LV */,
            0                                 /* MA */,
            4 |  9 << 8 |  9 << 16 | 14 << 24 /* MC */,
            4 |  6 << 8                       /* MD */,
            4 |  7 << 8                       /* ME */,
            0                                 /* MG */,
            4 |  7 << 8                       /* MK */,
            0                                 /* ML */,
            4 |  9 << 8 |  9 << 16 | 14 << 24 /* MR */,
            4 |  8 << 8 |  8 << 16 | 13 << 24 /* MT */,
            4 | 10 << 8 | 10 << 16 | 12 << 24 /* MU */,
            0                                 /* MZ */,
            0                                 /* NE */,
            0                                 /* NI */,
            4 |  8 << 8                       /* NL */,
            4 |  8 << 8                       /* NO */,
            4 |  8 << 8                       /* PK */,
            4 | 12 << 8                       /* PL */,
            4 |  8 << 8                       /* PS */,
            4 |  8 << 8                       /* PT */,
            4 |  8 << 8                       /* QA */,
            4 |  8 << 8                       /* RO */,
            4 |  7 << 8                       /* RS */,
            4 |  6 << 8                       /* SA */,
            4 | 12 << 8                       /* SC */,
            4 |  7 << 8                       /* SE */,
            4 |  9 << 8                       /* SI */, // Interpretation is contextual, not all bank ID's encode a branch ID, but some do. Not attempting to model that.
            4 |  8 << 8                       /* SK */,
            5 | 10 << 8 | 10 << 16 | 15 << 24 /* SM */,
            0                                 /* SN */,
            4 |  8 << 8 |  8 << 16 | 12 << 24 /* ST */,
            4 |  8 << 8                       /* SV */,
            0                                 /* TD */,
            0                                 /* TG */,
            4 |  7 << 8                       /* TL */,
            4 |  6 << 8 |  6 << 16 |  9 << 24 /* TN */,
            4 |  9 << 8                       /* TR */,
            4 | 10 << 8                       /* UA */,
            4 |  7 << 8                       /* VA */,
            4 |  8 << 8                       /* VG */,
            4 |  6 << 8 |  6 << 16 |  8 << 24 /* XK */, // The SWIFT spec mentions "1-4" as the bank ID and then "1-2" as the bank ID and "3-4" as the branch ID.
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
     * Returns the bank identifier from the given IBAN, if available.
     * @param iban an iban to evaluate. Cannot be null.
     * @return the bank ID for this IBAN, or <code>null</code> if unknown.
     */
    static String getBankIdentifier(IBAN iban) {
        int index = CountryCodes.indexOf(iban.getCountryCode());
        if (index > -1) {
            int data = BANK_CODE_BRANCH_CODE[index];
            int bankIdBegin = data & BANK_IDENTIFIER_BEGIN_MASK;
            int bankIdEnd = (data & BANK_IDENTIFIER_END_MASK) >>> BANK_IDENTIFIER_END_SHIFT;
            return bankIdBegin != 0 ? iban.toPlainString().substring(bankIdBegin, bankIdEnd) : null;
        }
        return null;
    }

    /**
     * Returns the branch identifier from the given IBAN, if available.
     * @param iban an iban to evaluate. Cannot be null.
     * @return the branch ID for this IBAN, or <code>null</code> if unknown.
     */
    static String getBranchIdentifier(IBAN iban) {
        int index = CountryCodes.indexOf(iban.getCountryCode());
        if (index > -1) {
            int data = BANK_CODE_BRANCH_CODE[index];
            int branchIdBegin = (data & BRANCH_IDENTIFIER_BEGIN_MASK) >>> BRANCH_IDENTIFIER_BEGIN_SHIFT;
            int branchIdEnd = (data & BRANCH_IDENTIFIER_END_MASK) >>> BRANCH_IDENTIFIER_END_SHIFT;
            return branchIdBegin != 0 ? iban.toPlainString().substring(branchIdBegin, branchIdEnd) : null;
        }
        return null;
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
