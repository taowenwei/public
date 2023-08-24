package com.example.odata.odata4

import org.apache.olingo.commons.api.edm.EdmPrimitiveType
import org.apache.olingo.commons.api.format.ContentType
import org.apache.olingo.server.api.OData
import org.apache.olingo.server.api.ODataRequest
import org.apache.olingo.server.api.ODataResponse
import org.apache.olingo.server.api.ServiceMetadata
import org.apache.olingo.server.api.processor.PrimitiveProcessor
import org.apache.olingo.server.api.uri.UriInfo
import org.apache.olingo.server.api.uri.UriResourceProperty
import org.springframework.stereotype.Component


@Component
class CompanyEntityPrimitiveProcessor(private val router: EntityRouter, private val serializer: EntitySerializer) :
    PrimitiveProcessor {

    override fun init(odata: OData?, serviceMetadata: ServiceMetadata?) {
        serializer.odata = odata
        serializer.serviceMetadata = serviceMetadata
    }

    override fun readPrimitive(
        request: ODataRequest?,
        response: ODataResponse?,
        uriInfo: UriInfo?,
        responseFormat: ContentType?,
    ) {
        // 1. Retrieve info from URI
        val resourceParts = uriInfo!!.uriResourceParts
        val (edmEntitySet, keyPredicates) = ProcessorUtils.getParams(uriInfo)
        val uriProperty = resourceParts[resourceParts.size - 1] as UriResourceProperty
        val edmProperty = uriProperty.property
        val edmPropertyName = edmProperty.name
        val edmPropertyType = edmProperty.type as EdmPrimitiveType

        // 2. retrieve data from backend
        // 2.1. retrieve the entity data, for which the property has to be read
        val entitySet = router.getData(edmEntitySet, keyPredicates = keyPredicates)
        val property = entitySet.entities?.get(0)?.getProperty(edmPropertyName)
        serializer.serializeProperty(edmEntitySet, response, responseFormat, property, edmPropertyType)
    }

    override fun updatePrimitive(
        request: ODataRequest?,
        response: ODataResponse?,
        uriInfo: UriInfo?,
        requestFormat: ContentType?,
        responseFormat: ContentType?,
    ) {
        TODO("Not yet implemented")
    }

    override fun deletePrimitive(request: ODataRequest?, response: ODataResponse?, uriInfo: UriInfo?) {
        TODO("Not yet implemented")
    }
}