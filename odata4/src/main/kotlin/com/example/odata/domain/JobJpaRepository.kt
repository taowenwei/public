package com.example.odata.domain

import org.apache.olingo.commons.api.data.Entity
import org.apache.olingo.commons.api.data.Property
import org.apache.olingo.commons.api.data.ValueType
import org.springframework.data.jpa.repository.Query
import org.springframework.stereotype.Repository
import java.net.URI
import javax.persistence.EntityManager


@Repository
interface JobJpaRepository : ArchJpaRepository<Job> {
    @Query(value = "SELECT * FROM odata4_test_job OFFSET ?1 LIMIT ?2", nativeQuery = true)
    override fun findAllWithLimit(offset: Int, limit: Int): List<Job>
}

@Suppress("UNCHECKED_CAST")
@Repository
class JobEntityRepository(
    private val jpaRepository: JobJpaRepository,
    private val entityManager: EntityManager,
) : ArchTypeJpaRepository<Job>() {

    override fun findAllWithFilter(filter: String): List<Job> {
        val nativeSql = "SELECT * FROM odata4_test_job WHERE $filter"
        return entityManager.createNativeQuery(nativeSql, Job::class.java).resultList as List<Job>

    }

    override fun toEntity(archType: Job): Entity {
        val entity = Entity()
            .addProperty(Property(null, "id", ValueType.PRIMITIVE, archType.id))
            .addProperty(Property(null, "eid", ValueType.PRIMITIVE, archType.eid))
            .addProperty(Property(null, "orderId", ValueType.PRIMITIVE, archType.orderId))
            .addProperty(Property(null, "address", ValueType.PRIMITIVE, archType.address))
        entity.id = URI("jobs.${archType.id}")
        return entity
    }

    override fun toArchType(archType: Job?, entity: Entity): Job {
        val job = archType ?: Job()
        entity.properties.forEach { prop ->
            when (prop.name) {
                "id" -> job.id = prop.value as Int
                "eid" -> job.eid = prop.value as Int
                "orderId" -> job.orderId = prop.value as Int
                "address" -> job.address = prop.value as String
            }
        }
        return job
    }

    override fun getJpaRepository() = jpaRepository
}
