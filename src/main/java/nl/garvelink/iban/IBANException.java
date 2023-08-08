/*
   Copyright 2022 Barend Garvelink

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
 * Base class for all exceptions thrown by this library.
 */
public abstract class IBANException extends IllegalArgumentException {
    private static final long serialVersionUID = 1L;
    private final CharSequence failedInput;

    public IBANException(String message, CharSequence failedInput) {
        super(message);
        this.failedInput = failedInput;
    }

    /**
     * Returns the input that triggered the exception, which can be null.
     * @return the unparsed input.
     */
    public final CharSequence getFailedInput() {
        return failedInput;
    }
}
