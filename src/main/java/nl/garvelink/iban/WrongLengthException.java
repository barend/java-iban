/*
   Copyright 2021 Barend Garvelink

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
 * Indicates that a candate IBAN failed validation because it's the wrong length for its country code.
 */
public class WrongLengthException extends IBANException {
    private static final long serialVersionUID = 2L;
    /** Actual length of input. */
    private final int actualLength;
    /** Expected length of input. */
    private final int expectedLength;

    WrongLengthException(String failedInput, int expectedLength) {
        super("Input failed length validation: found " + failedInput.length() + ", but expect "
                + expectedLength + " for country code.", failedInput);
        this.actualLength = failedInput.length();
        this.expectedLength = expectedLength;
    }

    /**
     * The expected length for the input, given its country code.
     * @return expected length of country IBAN.
     */
    public int getExpectedLength() {
        return expectedLength;
    }

    /**
     * The actual length of the input IBAN.
     * @return length of {@link #getFailedInput()}.
     */
    public int getActualLength() {
        return actualLength;
    }
}
