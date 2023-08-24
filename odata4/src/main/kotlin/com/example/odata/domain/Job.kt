package com.example.odata.domain

import org.apache.olingo.commons.api.edm.EdmPrimitiveTypeKind
import org.apache.olingo.commons.api.edm.provider.CsdlEntityType
import org.apache.olingo.commons.api.edm.provider.CsdlProperty
import org.apache.olingo.commons.api.edm.provider.CsdlPropertyRef
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.Id
import javax.persistence.Table

@Entity
@Table(name = "odata4_test_job")
open class Job {
    @Id
    @Column(name = "id", nullable = false)
    open var id: Int? = null

    @Column(name = "eid", nullable = false)
    open var eid: Int? = null

    @Column(name = "order_id", nullable = false)
    open var orderId: Int? = null

    @Column(name = "address", nullable = false)
    open var address: String? = null

    companion object {
        fun entityType(entityTypeName: String): CsdlEntityType {
            //create EntityType properties
            val id = CsdlProperty().setName("id").setType(EdmPrimitiveTypeKind.Int32.fullQualifiedName)
            val eid = CsdlProperty().setName("eid").setType(EdmPrimitiveTypeKind.Int32.fullQualifiedName)
            val orderId = CsdlProperty().setName("orderId").setType(EdmPrimitiveTypeKind.Int32.fullQualifiedName)
            val address = CsdlProperty().setName("address").setType(EdmPrimitiveTypeKind.String.fullQualifiedName)

            // create CsdlPropertyRef for Key element
            val propertyRefId = CsdlPropertyRef()
            propertyRefId.name = "id"

            // configure EntityType
            val entityType = CsdlEntityType()
            entityType.name = entityTypeName
            entityType.properties = listOf(id, eid, orderId, address)
            entityType.key = listOf(propertyRefId)
            return entityType
        }
    }
}