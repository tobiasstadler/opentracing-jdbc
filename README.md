[![Build Status](https://travis-ci.org/tobiasstadler/opentracing-jdbc.svg?branch=master)](https://travis-ci.org/tobiasstadler/opentracing-jdbc) [![Apache-2.0 license](https://img.shields.io/badge/license-Apache%202.0-blue.svg)](https://opensource.org/licenses/Apache-2.0)

# OpenTracing JDBC Instrumentation
OpenTracing instrumentation for JDBC.


## Usage

1. Add `opentracing` to jdbc url. E.g. `jdbc:opentracing:postgresql:/;onlyWithActiveSpan=true`

2. For PostgreSQL you can also use `OpenTracingPGDataSource` or `OpenTracingXAPGDataSource` and call `getConnection`

## Configuration

The following properties can be set
* `onlyWithActiveSpan`: if set to true a span will only be created if there is already another span active
* `sqlToIgnore`: a span will only be created if the sql does not match the given regex (matching is done case insensitive)
