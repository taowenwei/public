package com.example.odata.odata4

import org.apache.olingo.commons.api.data.Entity
import org.apache.olingo.commons.api.edm.EdmEntitySet
import org.apache.olingo.commons.api.edm.EdmPrimitiveTypeKind
import org.apache.olingo.commons.api.edm.EdmProperty
import org.apache.olingo.server.api.uri.UriInfo
import org.apache.olingo.server.api.uri.UriParameter
import org.apache.olingo.server.api.uri.UriResourceEntitySet
import java.math.BigDecimal
import java.util.*

object ProcessorUtils {
    fun getParams(uriInfo: UriInfo?): Pair<EdmEntitySet, List<UriParameter>?> {
        val resourceParts = uriInfo!!.uriResourceParts
        val uriEntityset = resourceParts[0] as UriResourceEntitySet
        return Pair(uriEntityset.entitySet, uriEntityset.keyPredicates)
    }

    fun sort(entities: List<Entity>, property: EdmProperty, isDescending: Boolean) {
        val propertyName = property.name
        val propertyType = property.type.name
        Collections.sort(entities, Comparator { entity1, entity2 ->
            val value1 = entity1.getProperty(propertyName).value
            val value2 = entity2.getProperty(propertyName).value

            // delegate the sorting to native sorter of Integer and String
            val compareResult = when (propertyType) {
                EdmPrimitiveTypeKind.Byte.fullQualifiedName.name,
                EdmPrimitiveTypeKind.SByte.fullQualifiedName.name,
                EdmPrimitiveTypeKind.Int16.fullQualifiedName.name,
                EdmPrimitiveTypeKind.Int32.fullQualifiedName.name,
                EdmPrimitiveTypeKind.Int64.fullQualifiedName.name,
                -> {
                    val x = value1 as Int
                    val y = value2 as Int
                    x.compareTo(y)
                }
                EdmPrimitiveTypeKind.Double.fullQualifiedName.name,
                EdmPrimitiveTypeKind.Single.fullQualifiedName.name,
                -> {
                    val x = value1 as Double
                    val y = value2 as Double
                    x.compareTo(y)
                }
                EdmPrimitiveTypeKind.Decimal.fullQualifiedName.name -> {
                    val x = value1 as BigDecimal
                    val y = value2 as BigDecimal
                    x.compareTo(y)
                }
                EdmPrimitiveTypeKind.String.fullQualifiedName.name -> {
                    val x = value1 as String
                    val y = value2 as String
                    x.compareTo(y)
                }
                EdmPrimitiveTypeKind.Date.fullQualifiedName.name -> {
                    val x = value1 as Date
                    val y = value2 as Date
                    x.compareTo(y)
                }
                else -> 0
            }
            if (isDescending) -compareResult else compareResult
        })
    }
}