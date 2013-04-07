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

/**
 * Indicates that a candate IBAN failed validation because its stated check digits didn't match the calculated expectation.
 */
public class WrongChecksumException extends IllegalArgumentException {
    private final String failedInput;
    private final int declaredChecksum;
    private final int calculatedChecksum;

    WrongChecksumException(String failedInput, int declaredChecksum, int calculatedChecksum) {
        super("Input \"" + failedInput + "\" failed checksum validation: declared " + declaredChecksum + ", but calculated " + calculatedChecksum + '.');
        this.failedInput = failedInput;
        this.declaredChecksum = declaredChecksum;
        this.calculatedChecksum = calculatedChecksum;
    }

    public String getFailedInput() {
        return failedInput;
    }

    public int getCalculatedChecksum() {
        return calculatedChecksum;
    }

    public int getDeclaredChecksum() {
        return declaredChecksum;
    }
}
