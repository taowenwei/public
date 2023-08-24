package com.example.odata.domain

import org.apache.olingo.commons.api.edm.EdmPrimitiveTypeKind
import org.apache.olingo.commons.api.edm.provider.CsdlEntityType
import org.apache.olingo.commons.api.edm.provider.CsdlProperty
import org.apache.olingo.commons.api.edm.provider.CsdlPropertyRef
import java.time.Instant
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.Id
import javax.persistence.Lob
import javax.persistence.Table

@Entity
@Table(name = "odata4_test_employee")
open class Team {
    @Id
    @Column(name = "id", nullable = false)
    open var id: Int? = null

    @Column(name = "create_time")
    open var createdAt: Instant? = null

    @Column(name = "first_name")
    open var firstName: String? = null

    @Column(name = "last_name")
    open var lastName: String? = null

    @Column(name = "title")
    open var title: String? = null

    @Column(name = "email")
    open var email: String? = null

    @Column(name = "employee_id")
    open var employeeId: Int? = null

    companion object {
        fun entityType(entityTypeName: String): CsdlEntityType {
            //create EntityType properties
            val id = CsdlProperty().setName("id").setType(EdmPrimitiveTypeKind.Int32.fullQualifiedName)
            val createdAt = CsdlProperty().setName("createdAt").setType(EdmPrimitiveTypeKind.Date.fullQualifiedName)
            val firstName = CsdlProperty().setName("firstName").setType(EdmPrimitiveTypeKind.String.fullQualifiedName)
            val lastName = CsdlProperty().setName("lastName").setType(EdmPrimitiveTypeKind.String.fullQualifiedName)
            val title = CsdlProperty().setName("title").setType(EdmPrimitiveTypeKind.String.fullQualifiedName)
            val email = CsdlProperty().setName("email").setType(EdmPrimitiveTypeKind.String.fullQualifiedName)
            val employeeId = CsdlProperty().setName("employeeId").setType(EdmPrimitiveTypeKind.Int32.fullQualifiedName)

            // create CsdlPropertyRef for Key element
            val propertyRefId = CsdlPropertyRef()
            propertyRefId.name = "id"

            // configure EntityType
            val entityType = CsdlEntityType()
            entityType.name = entityTypeName
            entityType.properties = listOf(id, createdAt, firstName, lastName, title, email, employeeId)
            entityType.key = listOf(propertyRefId)
            return entityType
        }
    }
}