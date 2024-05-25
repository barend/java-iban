# Security Policy

The java-iban library uses no third-party dependencies in its production code (besides the Java Runtime). The build and
automated tests do rely on some third-party dependencies.

No third-party libraries are transitively included by taking a dependency on java-iban. No third-party libraries are
embedded inside the iban-{version}.jar package through means such as shading or vendoring.

## Supported Versions

Only the most recent version is supported.

## Reporting a Vulnerability

My preferred means of reporting any issues is using the GitHub Issues for this project. Confidential disclosure can be
done through Keybase at [keybase.io/barend](https://keybase.io/barend).
