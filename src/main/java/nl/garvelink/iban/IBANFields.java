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


import java.util.Optional;

/**
 * Extracts national encoded information from IBANs.
 *
 * <p>This class uses the JDK8 {@code Optional} API to express fields that are not present in every country's IBAN
 * format. If you're on a lower JDK level (like on Android), use {@link IBANFieldsCompat} for a version that returns
 * nulls.</p>
 * @see IBANFieldsCompat
 */
public class IBANFields {

    /**
     * Returns the bank identifier from the given IBAN, if available.
     * <p><strong>Finland (FI): </strong>
     * The spec mentions both "Not in use" and "Position 1-3 indicate the bank or banking group." I have taken "bank
     * or banking group" to be more or less synonymous with Bank Identifier and return it as such.</p>
     * <p><strong>Slovenia (SI): </strong>
     * The five digits following the checksum encode the financial institution and sub-encode the branch identifier
     * if applicable, depending on the type of financial institution. The library returns all five digits as the bank
     * identifier and never returns a branch identifier.</p>
     * <p><strong>Republic of Kosovo (XK): </strong>
     * The four digits following the checksum encode the Bank ID, and the last two of these four sub-encode the
     * branch ID. The library returns all four digits as the bank identifier. For example: if the IBAN has "1234" in
     * these positions, then the bank identifier is returned as "1234" and the branch identifier as "34".</p>
     * @param iban an iban to evaluate. Cannot be null.
     * @return the bank ID for this IBAN, or <code>EMPTY</code> if unknown.
     */
    public static Optional<String> getBankIdentifier(IBAN iban) {
        return Optional.ofNullable(CountryCodes.getBankIdentifier(iban));
    }

    /**
     * Returns the branch identifier from the given IBAN, if available.
     * @param iban an iban to evaluate. Cannot be null.
     * @return the branch ID for this IBAN, or <code>EMPTY</code> if unknown.
     */
    public static Optional<String> getBranchIdentifier(IBAN iban) {
        return Optional.ofNullable(CountryCodes.getBranchIdentifier(iban));
    }

    /** Prevent instantiation of static utility class. */
    private IBANFields() { }
}
