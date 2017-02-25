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

/**
 * Extracts national encoded information from IBANs.
 *
 * <p>This class returns nulls to express fields that are not present in every country's IBAN format. If you're on JDK8,
 * you may prefer {@link IBANFields}, which uses the {@code Optional} API.</p>
 */
public class IBANFieldsCompat {

    private static final int BANK_IDENTIFIER_BEGIN_MASK = 0xFF;
    private static final int BANK_IDENTIFIER_END_SHIFT = 8;
    private static final int BANK_IDENTIFIER_END_MASK = 0xFF << BANK_IDENTIFIER_END_SHIFT;
    private static final int BRANCH_IDENTIFIER_BEGIN_SHIFT = 16;
    private static final int BRANCH_IDENTIFIER_BEGIN_MASK = 0xFF << BRANCH_IDENTIFIER_BEGIN_SHIFT;
    private static final int BRANCH_IDENTIFIER_END_SHIFT = 24;
    private static final int BRANCH_IDENTIFIER_END_MASK = 0xFF << BRANCH_IDENTIFIER_END_SHIFT;

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
     * @return the bank ID for this IBAN, or null if unknown.
     */
    public static String getBankIdentifier(IBAN iban) {
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
     * @return the branch ID for this IBAN, or null if unknown.
     */
    public static String getBranchIdentifier(IBAN iban) {
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
            0                                 /* CG */,
            4 |  9 << 8                       /* CH */,
            0                                 /* CI */,
            0                                 /* CM */,
            4 |  8 << 8                       /* CR */,
            0                                 /* CV */,
            4 |  7 << 8 |  7 << 16 | 12 << 24 /* CY */,
            4 |  8 << 8                       /* CZ */,
            4 | 12 << 8                       /* DE */,
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
            4 |  7 << 8 |  7 << 16 | 11 << 24 /* GR */,
            4 |  8 << 8                       /* GT */,
            4 | 11 << 8                       /* HR */,
            4 |  7 << 8 |  7 << 16 | 11 << 24 /* HU */,
            4 |  8 << 8 |  8 << 16 | 14 << 24 /* IE */,
            4 |  7 << 8 |  7 << 16 | 10 << 24 /* IL */,
            4 |  8 << 8 |  8 << 16 | 11 << 24 /* IQ */,
            0                                 /* IR */,
            4 |  8 << 8                       /* IS */,
            5 | 10 << 8 | 10 << 16 | 15 << 24 /* IT */,
            4 |  8 << 8                       /* JO */,
            4 |  8 << 8                       /* KW */,
            4 |  7 << 8                       /* KZ */,
            4 |  8 << 8                       /* LB */,
            4 |  8 << 8                       /* LC */,
            4 |  9 << 8                       /* LI */,
            4 |  9 << 8                       /* LT */,
            4 |  7 << 8                       /* LU */,
            4 |  8 << 8                       /* LV */,
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
            4 |  7 << 8                       /* TL */,
            4 |  6 << 8 |  6 << 16 |  9 << 24 /* TN */,
            4 |  9 << 8                       /* TR */,
            4 | 10 << 8                       /* UA */,
            4 |  8 << 8                       /* VG */,
            4 |  6 << 8 |  6 << 16 |  8 << 24 /* XK */, // The SWIFT spec mentions "1-4" as the bank ID and then "1-2" as the bank ID and "3-4" as the branch ID.
    };
}
