package com.example.odata.odata4

import org.apache.olingo.commons.api.format.ContentType
import org.apache.olingo.server.api.OData
import org.apache.olingo.server.api.ODataRequest
import org.apache.olingo.server.api.ODataResponse
import org.apache.olingo.server.api.ServiceMetadata
import org.apache.olingo.server.api.processor.EntityCollectionProcessor
import org.apache.olingo.server.api.uri.UriInfo
import org.apache.olingo.server.api.uri.UriResourcePrimitiveProperty
import org.apache.olingo.server.api.uri.queryoption.expression.Member
import org.springframework.stereotype.Component


@Component
class CompanyEntityCollectionProcessor(private val router: EntityRouter, private val serializer: EntitySerializer) :
    EntityCollectionProcessor {

    override fun init(odata: OData?, serviceMetadata: ServiceMetadata?) {
        serializer.odata = odata
        serializer.serviceMetadata = serviceMetadata
    }

    override fun readEntityCollection(
        request: ODataRequest?,
        response: ODataResponse?,
        uriInfo: UriInfo?,
        responseFormat: ContentType?,
    ) {
        var (edmEntitySet, keyPredicates) = ProcessorUtils.getParams(uriInfo)
        // $skip
        val offset = uriInfo?.skipOption?.value ?: 0
        // $top
        val limit = uriInfo?.topOption?.value ?: Int.MAX_VALUE
        //$ filter
        val filters = FilterExpressionSalesforce().evaluate(uriInfo?.filterOption?.expression)
        /*uriInfo?.filterOption?.expression?.let { expression ->
            val expressionVisitor = FilterExpressionVisitor()
            val expressResult = expression.accept(expressionVisitor) as Boolean
            if (expressResult) {

            } else {
                throw ODataRuntimeException("Not supported")
            }
        }*/
        val entitySet = router.getData(edmEntitySet, SqlLimit(offset, limit), keyPredicates, filters)

        // handle possible navigation
        val resourceParts = uriInfo!!.uriResourceParts
        when (resourceParts.size) {
            1 -> {}
            else -> throw ODataRuntimeException("Not supported")
        }

        // $count
        if (uriInfo.countOption?.value == true) {
            entitySet.count = entitySet.entities.size
        }
        // $orderBy
        uriInfo.orderByOption?.let {
            if (it.orders.size > 0) {
                // support only one
                val orderByItem = it.orders[0]
                val expr = orderByItem.expression as? Member
                val uriResource = expr?.resourcePath?.uriResourceParts?.get(0) as? UriResourcePrimitiveProperty
                uriResource?.let {
                    ProcessorUtils.sort(entitySet.entities, uriResource.property, orderByItem.isDescending)
                }
            }
        }
        serializer.serializeCollection(uriInfo, edmEntitySet, response, responseFormat, entitySet)
    }
}