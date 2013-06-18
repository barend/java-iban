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

/**
 * Contains information about IBAN country codes.
 */
public abstract class CountryCodes {

    /**
     * Known country codes, this list must be sorted to allow binary search.
     */
    private static final String[] COUNTRY_CODES = {
            "AD", "AT", "BA", "BE", "BG", "CH", "CY", "CZ", "DE", "DK", "EE", "ES", "FI", "FR", "GB", "GI", "GR",
            "HR", "HU", "IE", "IL", "IS", "IT", "LI", "LT", "LU", "LV", "MC", "ME", "MK", "MT", "MU", "NL", "NO",
            "PL", "PT", "RO", "RS", "SE", "SI", "SK", "SM", "TR"};
    /**
     * Lengths for each country's IBAN. The indices match the indices of {@link #COUNTRY_CODES}, the values are the expected length.
     */
    private static final int[] COUNTRY_IBAN_LENGTHS = {
            24 /* AD */, 20 /* AT */, 20 /* BA */, 16 /* BE */, 22 /* BG */, 21 /* CH */, 28 /* CY */, 24 /* CZ */,
            22 /* DE */, 18 /* DK */, 20 /* EE */, 24 /* ES */, 18 /* FI */, 27 /* FR */, 22 /* GB */, 23 /* GI */,
            27 /* GR */, 21 /* HR */, 28 /* HU */, 22 /* IE */, 23 /* IL */, 26 /* IS */, 27 /* IT */, 21 /* LI */,
            20 /* LT */, 20 /* LU */, 21 /* LV */, 27 /* MC */, 22 /* ME */, 19 /* MK */, 31 /* MT */, 30 /* MU */,
            18 /* NL */, 15 /* NO */, 28 /* PL */, 25 /* PT */, 24 /* RO */, 22 /* RS */, 24 /* SE */, 19 /* SI */,
            24 /* SK */, 27 /* SM */, 26 /* TR */ };

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

    /** Prevent instantiation of static utility class. */
    private CountryCodes() { }
}
