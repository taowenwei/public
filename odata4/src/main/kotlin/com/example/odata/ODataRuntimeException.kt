package com.example.odata.odata4

import org.apache.olingo.commons.api.http.HttpStatusCode
import org.apache.olingo.server.api.ODataApplicationException
import org.slf4j.LoggerFactory
import java.util.*

class ODataRuntimeException(
    message: String = "",
    status: Int = HttpStatusCode.NOT_IMPLEMENTED.statusCode,
) : ODataApplicationException(message, status, Locale.ROOT) {
    init {
        LoggerFactory.getLogger(ODataRuntimeException::class.java).info(message)
        printStackTrace()
    }
}