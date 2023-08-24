package com.example.odata.odata4

import org.apache.olingo.commons.api.data.Entity
import org.apache.olingo.commons.api.data.EntityCollection
import org.apache.olingo.commons.api.edm.EdmEntityType
import org.apache.olingo.commons.api.format.ContentType
import org.apache.olingo.commons.api.http.HttpStatusCode
import org.apache.olingo.server.api.OData
import org.apache.olingo.server.api.ODataRequest
import org.apache.olingo.server.api.ODataResponse
import org.apache.olingo.server.api.ServiceMetadata
import org.apache.olingo.server.api.processor.EntityProcessor
import org.apache.olingo.server.api.uri.UriInfo
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Component


@Component
class CompanyEntityProcessor(private val router: EntityRouter, private val serializer: EntitySerializer) :
    EntityProcessor {
    val logger: Logger = LoggerFactory.getLogger(CompanyEntityProcessor::class.java)

    override fun init(odata: OData?, serviceMetadata: ServiceMetadata?) {
        serializer.odata = odata
        serializer.serviceMetadata = serviceMetadata
    }

    override fun readEntity(
        request: ODataRequest?,
        response: ODataResponse,
        uriInfo: UriInfo,
        responseFormat: ContentType,
    ) {
        val (edmEntitySet, keyPredicates) = ProcessorUtils.getParams(uriInfo)
        val entitySet = router.getData(edmEntitySet, keyPredicates = keyPredicates)

        // handle possible navigation
        val resourceParts = uriInfo.uriResourceParts
        when (resourceParts.size) {
            1 -> {}
            else -> throw ODataRuntimeException("Not supported")
        }
        serializer.serializeCollection(null, edmEntitySet, response, responseFormat, entitySet, true)
    }

    override fun createEntity(
        request: ODataRequest?,
        response: ODataResponse?,
        uriInfo: UriInfo?,
        requestFormat: ContentType?,
        responseFormat: ContentType?,
    ) {
        // 1. Retrieve the entity type from the URI
        val (edmEntitySet, _) = ProcessorUtils.getParams(uriInfo)
        val requestEntity = getRequestEntity(request, requestFormat, edmEntitySet.entityType)
        logger.info("Create a new entity with ${requestEntity.toString()}")
        val entity = router.addData(edmEntitySet, requestEntity)
        val entitySet = EntityCollection()
        entitySet.entities.add(entity)
        serializer.serializeCollection(null, edmEntitySet, response, responseFormat, entitySet)
        entity?.let {
            response!!.statusCode = HttpStatusCode.CREATED.statusCode
        }
    }

    override fun updateEntity(
        request: ODataRequest?,
        response: ODataResponse?,
        uriInfo: UriInfo?,
        requestFormat: ContentType?,
        responseFormat: ContentType?,
    ) {
        // 1. Retrieve the entity type from the URI
        val (edmEntitySet, keyPredicates) = ProcessorUtils.getParams(uriInfo)
        val requestEntity = getRequestEntity(request, requestFormat, edmEntitySet.entityType)
        requestEntity?.let {
            logger.info("Update an entity with $requestEntity")
            router.updateData(edmEntitySet, keyPredicates!!, it)
        }
        response!!.statusCode = HttpStatusCode.NO_CONTENT.statusCode
    }

    override fun deleteEntity(request: ODataRequest?, response: ODataResponse?, uriInfo: UriInfo?) {
        val (edmEntitySet, keyPredicates) = ProcessorUtils.getParams(uriInfo)
        router.deleteData(edmEntitySet, keyPredicates!!)
        response!!.statusCode = HttpStatusCode.NO_CONTENT.statusCode
    }

    private fun getRequestEntity(
        request: ODataRequest?,
        requestFormat: ContentType?,
        edmEntityType: EdmEntityType,
    ): Entity? {
        val requestInputStream = request!!.body
        val deserializer = serializer.odata?.createDeserializer(requestFormat)
        val requestEntity = deserializer?.entity(requestInputStream, edmEntityType)
        return requestEntity?.entity
    }
}