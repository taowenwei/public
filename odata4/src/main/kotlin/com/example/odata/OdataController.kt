package com.example.odata

import org.apache.olingo.commons.api.edm.provider.CsdlEdmProvider
import org.apache.olingo.server.api.OData
import org.apache.olingo.server.api.processor.EntityCollectionProcessor
import org.apache.olingo.server.api.processor.EntityProcessor
import org.apache.olingo.server.api.processor.PrimitiveProcessor
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import java.net.URLDecoder
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletRequestWrapper
import javax.servlet.http.HttpServletResponse

const val URI = "/OData/V1.0"

@RestController
@RequestMapping(URI)
class OdataController {
    val logger: Logger = LoggerFactory.getLogger(OdataController::class.java)

    @Autowired
    lateinit var edmProvider: CsdlEdmProvider

    @Autowired
    lateinit var collectionProcessor: EntityCollectionProcessor

    @Autowired
    lateinit var entityProcessor: EntityProcessor

    @Autowired
    lateinit var entityPrimityProcessor: PrimitiveProcessor

    @RequestMapping(value = ["**"])
    fun process(request: HttpServletRequest?, response: HttpServletResponse?) {
        val odata = OData.newInstance()
        val edm = odata.createServiceMetadata(edmProvider, listOf())
        val handler = odata.createHandler(edm)
        handler.register(collectionProcessor)
        handler.register(entityProcessor)
        handler.register(entityPrimityProcessor)
        handler.process(object : HttpServletRequestWrapper(request) {
            // Spring MVC matches the whole path as the servlet path
            // Olingo wants just the prefix, ie upto /OData/V1.0, so that it
            // can parse the rest of it as an OData path. So we need to override
            // getServletPath()
            override fun getServletPath(): String {
                return URI
            }
        }, response)

        val url = request?.requestURL.toString() + request?.queryString?.let { "?$it" }
        logger.info("Req: ${request?.method}: ${URLDecoder.decode(url, "UTF-8")} @${response?.status}")
    }
}