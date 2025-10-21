# itext-doc-creator

A Clojure service (HTTP API) for converting HTML into PDF files.


## Usage

Start a REPL and run `(mount/start)` from the `core` namespace,
or just start the service with `lein run`.

Alternatively built it using `lein uberjar` and then run the `jar`-artifact:

    java -jar target/itext-doc-creator-0.1.0-SNAPSHOT-standalone.jar

This is good for packaging the application in a container.


### Configuration

The service can be configured with the following environment variables:

| Name              | Description                                        | Required |
|-------------------|----------------------------------------------------|----------|
| `PORT`            | Port for the HTTP service (defaults to `9000`).    | No       |


## License

Copyright © 2025

This program and the accompanying materials are made available under the
terms of the Eclipse Public License 2.0 which is available at
http://www.eclipse.org/legal/epl-2.0.

This Source Code may also be made available under the following Secondary
Licenses when the conditions for such availability set forth in the Eclipse
Public License, v. 2.0 are satisfied: GNU General Public License as published by
the Free Software Foundation, either version 2 of the License, or (at your
option) any later version, with the GNU Classpath Exception which is available
at https://www.gnu.org/software/classpath/license.html.
