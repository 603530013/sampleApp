package com.inetpsa.pims.spaceMiddleware.util

import java.time.temporal.ChronoField
import java.time.temporal.TemporalAccessor

/**
 * Read a field from a TemporalAccessor, returning 0 if the field is not supported.
 *
 * @param field
 * @return
 */
internal infix fun TemporalAccessor.read(field: ChronoField): Int =
    if (isSupported(field)) {
        get(field)
    } else {
        0
    }
