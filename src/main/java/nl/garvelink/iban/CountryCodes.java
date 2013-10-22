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

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;

/**
 * Contains information about IBAN country codes.
 */
public abstract class CountryCodes {

    /**
     * Known country codes, this list must be sorted to allow binary search.
     */
    private static final String[] COUNTRY_CODES = {
            "AD", "AE", "AL", "AO", "AT", "AZ", "BA", "BE", "BF", "BG", "BH", "BI", "BJ", "BR", "CG", "CH", "CI",
            "CM", "CR", "CV", "CY", "CZ", "DE", "DK", "DO", "DZ", "EE", "EG", "ES", "FI", "FO", "FR", "GA", "GB",
            "GE", "GI", "GL", "GR", "GT", "HR", "HU", "IE", "IL", "IR", "IS", "IT", "KW", "KZ", "LB", "LI", "LT",
            "LU", "LV", "MC", "MD", "ME", "MG", "MK", "ML", "MR", "MT", "MU", "MZ", "NL", "NO", "PK", "PL", "PS",
            "PT", "RO", "RS", "SA", "SE", "SI", "SK", "SM", "SN", "TN", "TR", "UA", "VG" };
    /**
     * Lengths for each country's IBAN. The indices match the indices of {@link #COUNTRY_CODES}, the values are the expected length.
     */
    private static final int[] COUNTRY_IBAN_LENGTHS = {
            24 /* AD */, 23 /* AE */, 28 /* AL */, 25 /* AO */, 20 /* AT */, 28 /* AZ */, 20 /* BA */, 16 /* BE */,
            27 /* BF */, 22 /* BG */, 22 /* BH */, 16 /* BI */, 28 /* BJ */, 29 /* BR */, 27 /* CG */, 21 /* CH */,
            28 /* CI */, 27 /* CM */, 21 /* CR */, 25 /* CV */, 28 /* CY */, 24 /* CZ */, 22 /* DE */, 18 /* DK */,
            28 /* DO */, 24 /* DZ */, 20 /* EE */, 27 /* EG */, 24 /* ES */, 18 /* FI */, 18 /* FO */, 27 /* FR */,
            27 /* GA */, 22 /* GB */, 22 /* GE */, 23 /* GI */, 18 /* GL */, 27 /* GR */, 28 /* GT */, 21 /* HR */,
            28 /* HU */, 22 /* IE */, 23 /* IL */, 26 /* IR */, 26 /* IS */, 27 /* IT */, 30 /* KW */, 20 /* KZ */,
            28 /* LB */, 21 /* LI */, 20 /* LT */, 20 /* LU */, 21 /* LV */, 27 /* MC */, 24 /* MD */, 22 /* ME */,
            27 /* MG */, 19 /* MK */, 28 /* ML */, 27 /* MR */, 31 /* MT */, 30 /* MU */, 25 /* MZ */, 18 /* NL */,
            15 /* NO */, 24 /* PK */, 28 /* PL */, 29 /* PS */, 25 /* PT */, 24 /* RO */, 22 /* RS */, 24 /* SA */,
            24 /* SE */, 19 /* SI */, 24 /* SK */, 27 /* SM */, 28 /* SN */, 24 /* TN */, 26 /* TR */, 29 /* UA */,
            24 /* VG */ };

    /**
     * Returns the IBAN length for a given country code.
     * @param countryCode a non-null, uppercase, two-character country code.
     * @return the IBAN length for the given country, or -1 if the input is not a known, two-character country code.
     * @throws NullPointerException if the input is null.
     */
    public static int getLengthForCountryCode(String countryCode) {
        int index = Arrays.binarySearch(CountryCodes.COUNTRY_CODES, countryCode);
        if (index > -1) {
            return CountryCodes.COUNTRY_IBAN_LENGTHS[index];
        }
        return -1;
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
        return Arrays.binarySearch(COUNTRY_CODES, aCountryCode) >= 0;
    }

    /** Prevent instantiation of static utility class. */
    private CountryCodes() { }
}
