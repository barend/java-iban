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
 * Used in the parameterized tests to hold reference data.
 */
class TestData {
    public final String name;
    public final boolean swift;
    public final boolean sepa;
    public final String plain;
    public final String bank;
    public final String branch;
    public final String pretty;

    public TestData(String name, boolean swift, boolean sepa, String plain, String bank, String branch, String pretty) {
        this.name = name;
        this.swift = swift;
        this.sepa = sepa;
        this.plain = plain;
        this.bank = bank;
        this.branch = branch;
        this.pretty = pretty;
    }

    @Override
    public String toString() {
        return name;
    }
}
