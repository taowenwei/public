package com.example.odata.domain

import org.apache.olingo.commons.api.data.Entity
import org.apache.olingo.commons.api.data.Property
import org.apache.olingo.commons.api.data.ValueType
import org.springframework.data.jpa.repository.Query
import org.springframework.stereotype.Repository
import java.net.URI
import java.util.*
import javax.persistence.EntityManager


@Repository
interface TeamJpaRepository : ArchJpaRepository<Team> {
    @Query(value = "SELECT * FROM odata4_test_employee OFFSET ?1 LIMIT ?2", nativeQuery = true)
    override fun findAllWithLimit(offset: Int, limit: Int): List<Team>
}

@Suppress("UNCHECKED_CAST")
@Repository
class TeamEntityRepository(
    private val jpaRepository: TeamJpaRepository,
    private val entityManager: EntityManager,
) : ArchTypeJpaRepository<Team>() {

    override fun findAllWithFilter(filter: String): List<Team> {
        val nativeSql = "SELECT * FROM odata4_test_employee WHERE $filter"
        return entityManager.createNativeQuery(nativeSql, Team::class.java).resultList as List<Team>
    }

    override fun toEntity(archType: Team): Entity {
        val entity = Entity()
            .addProperty(Property(null, "id", ValueType.PRIMITIVE, archType.id))
            .addProperty(Property(null, "createdAt", ValueType.PRIMITIVE, Date.from(archType.createdAt)))
            .addProperty(Property(null, "firstName", ValueType.PRIMITIVE, archType.firstName))
            .addProperty(Property(null, "lastName", ValueType.PRIMITIVE, archType.lastName))
            .addProperty(Property(null, "title", ValueType.PRIMITIVE, archType.title))
            .addProperty(Property(null, "email", ValueType.PRIMITIVE, archType.email))
            .addProperty(Property(null, "employeeId", ValueType.PRIMITIVE, archType.employeeId))
        entity.id = URI("teams.${archType.id}")
        return entity
    }

    override fun toArchType(archType: Team?, entity: Entity): Team {
        val team = archType ?: Team()
        entity.properties.forEach { prop ->
            when (prop.name) {
                "id" -> team.id = prop.value as Int
                "createdAt" -> team.createdAt = (prop.value as GregorianCalendar).toInstant()
                "firstName" -> team.firstName = prop.value as String
                "lastName" -> team.lastName = prop.value as String
                "title" -> team.title = prop.value as String
                "email" -> team.email = prop.value as String
                "employeeId" -> team.employeeId = prop.value as Int
            }
        }
        return team
    }

    override fun getJpaRepository() = jpaRepository
}