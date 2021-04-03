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

import java.time.LocalDate;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;

import static nl.garvelink.iban.CountryCodesData.*;

/**
 * Contains information about IBAN country codes.
 */
public abstract class CountryCodes {

    /**
     * The shortest known valid IBAN.
     */
    public static final int SHORTEST_IBAN_LENGTH;

    /**
     * The longest known valid IBAN.
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
     * Returns the index of the given country code by binary search.
     * @param countryCode a country code.
     * @return the array index, or -1.
     */
    static int indexOf(String countryCode) {
        return Arrays.binarySearch(COUNTRY_CODES, countryCode);
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
    public static int getLengthForCountryCode(CharSequence countryCode) {
        int index = indexOf(countryCode.toString());
        if (index > -1) {
            return COUNTRY_IBAN_LENGTHS[index] & REMOVE_METADATA_MASK;
        }
        return -1;
    }

    /**
     * Returns whether the given country code is in SEPA.
     * @param countryCode a non-null, uppercase, two-character country code.
     * @return true if SEPA, false if not.
     * @throws NullPointerException if the input is null.
     */
    public static boolean isSEPACountry(CharSequence countryCode) {
        int index = indexOf(countryCode.toString());
        if (index > -1) {
            return (COUNTRY_IBAN_LENGTHS[index] & SEPA) == SEPA;
        }
        return false;
    }

    /**
     * Returns whether the source for this IBAN's format and data is the SWIFT IBAN Registry.
     * @param countryCode a non-null, uppercase, two-character country code.
     * @return true if our data is from the SWIFT IBAN Registry, false if not.
     * @throws NullPointerException if the input is null.
     */
    public static boolean isInSwiftRegistry(CharSequence countryCode) {
        int index = indexOf(countryCode.toString());
        if (index > -1) {
            return (COUNTRY_IBAN_LENGTHS[index] & SWIFT) == SWIFT;
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
    public static boolean isKnownCountryCode(CharSequence aCountryCode) {
        if (aCountryCode == null || aCountryCode.length() != 2) {
            return false;
        }
        return indexOf(aCountryCode.toString()) >= 0;
    }

    /**
     * Returns the date that the IBAN reference data was last updated.
     * @return last update date of the reference data in this library.
     */
    public static LocalDate getLastUpdateDate() {
        return LocalDate.parse(LAST_UPDATE_DATE);
    }

    /**
     * Returns the version information of the SWIFT IBAN Registry used on {@link #getLastUpdateDate()}.
     * @return revision information of the SWIFT IBAN Registry.
     */
    public static String getLastUpdateRevision() {
        return LAST_UPDATE_REV;
    }

    /** Prevent instantiation of static utility class. */
    private CountryCodes() { }
}
