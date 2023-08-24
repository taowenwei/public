package com.example.odata.odata4

import org.apache.olingo.commons.api.data.ContextURL
import org.apache.olingo.commons.api.data.EntityCollection
import org.apache.olingo.commons.api.data.Property
import org.apache.olingo.commons.api.edm.EdmEntitySet
import org.apache.olingo.commons.api.edm.EdmPrimitiveType
import org.apache.olingo.commons.api.format.ContentType
import org.apache.olingo.commons.api.http.HttpHeader
import org.apache.olingo.commons.api.http.HttpStatusCode
import org.apache.olingo.server.api.OData
import org.apache.olingo.server.api.ODataResponse
import org.apache.olingo.server.api.ServiceMetadata
import org.apache.olingo.server.api.serializer.EntityCollectionSerializerOptions
import org.apache.olingo.server.api.serializer.EntitySerializerOptions
import org.apache.olingo.server.api.serializer.PrimitiveSerializerOptions
import org.apache.olingo.server.api.uri.UriInfo
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Component
import java.io.InputStream


@Component
class EntitySerializer(var odata: OData? = null, var serviceMetadata: ServiceMetadata? = null) {
    val logger: Logger = LoggerFactory.getLogger(EntitySerializer::class.java)

    fun serializeCollection(
        uriInfo: UriInfo?,
        edmEntitySet: EdmEntitySet,
        response: ODataResponse?,
        responseFormat: ContentType?,
        entitySet: EntityCollection,
        entityOnly: Boolean = false,
    ) {
        val serializer = odata!!.createSerializer(responseFormat)

        if (entitySet.entities.size > 0) {
            val entity = entitySet.entities[0].toString()
            logger.info("Total ${entitySet.entities.size} entities; peek 1st entity: $entity")
        }

        if (entityOnly) {
            val contextUrl = ContextURL.with().entitySet(edmEntitySet).build()
            val options = EntitySerializerOptions.with().contextURL(contextUrl).build()
            val serializerResult =
                serializer.entity(serviceMetadata, edmEntitySet.entityType, entitySet.entities[0], options)
            sendResponse(response, responseFormat, serializerResult.content)
        } else {
            when (entitySet.entities.size) {
                0 -> response!!.statusCode = HttpStatusCode.NO_CONTENT.statusCode
                else -> {
                    // $select
                    val selectList = uriInfo?.selectOption?.let {
                        val edmEntityType = edmEntitySet.entityType
                        odata!!.createUriHelper().buildContextURLSelectList(edmEntityType, null, it)
                    }
                    val contextUrl = ContextURL.with().entitySet(edmEntitySet).selectList(selectList).build()
                    val options =
                        EntityCollectionSerializerOptions.with().count(uriInfo?.countOption).contextURL(contextUrl)
                            .select(uriInfo?.selectOption).build()
                    val serializerResult =
                        serializer.entityCollection(serviceMetadata, edmEntitySet.entityType, entitySet, options)
                    sendResponse(response, responseFormat, serializerResult.content)
                }
            }
        }
    }

    fun serializeProperty(
        edmEntitySet: EdmEntitySet,
        response: ODataResponse?,
        responseFormat: ContentType?,
        property: Property?,
        edmType: EdmPrimitiveType,
    ) {
        val serializer = odata!!.createSerializer(responseFormat)
        property?.let {
            val contextUrl = ContextURL.with().entitySet(edmEntitySet).navOrPropertyPath(property.name).build()
            val options = PrimitiveSerializerOptions.with().contextURL(contextUrl).build()
            val serializerResult = serializer.primitive(serviceMetadata, edmType, property, options)
            sendResponse(response, responseFormat, serializerResult.content)
        } ?: run {
            response!!.statusCode = HttpStatusCode.NO_CONTENT.statusCode
        }
    }

    private fun sendResponse(response: ODataResponse?, responseFormat: ContentType?, content: InputStream) {
        response!!.content = content
        response.statusCode = HttpStatusCode.OK.statusCode
        response.setHeader(HttpHeader.CONTENT_TYPE, responseFormat!!.toContentTypeString())
    }
}
