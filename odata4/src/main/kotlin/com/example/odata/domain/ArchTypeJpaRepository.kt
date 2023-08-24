package com.example.odata.domain

import com.example.odata.odata4.ODataRuntimeException
import com.example.odata.odata4.SqlLimit
import org.apache.olingo.commons.api.data.Entity
import org.apache.olingo.commons.api.data.EntityCollection
import org.apache.olingo.server.api.uri.UriParameter
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.repository.NoRepositoryBean

@NoRepositoryBean
interface ArchJpaRepository<T> : JpaRepository<T, Int> {
    fun findAllWithLimit(offset: Int, limit: Int): List<T>
}

@Suppress("NULLABLE_TYPE_PARAMETER_AGAINST_NOT_NULL_TYPE_PARAMETER")
abstract class ArchTypeJpaRepository<T> {
    private fun findAll(sqlLimit: SqlLimit?, keyPredicates: List<UriParameter>?, filters: String?): List<T> {
        return filters?.let {
            findAllWithFilter(filters)
        } ?: run {
            if (keyPredicates?.size == 1) {
                when (keyPredicates[0].name) {
                    "id" -> getJpaRepository().findAllById(listOf(keyPredicates[0].text.toInt()))
                    else -> throw ODataRuntimeException("Not supported")
                }
            } else {
                sqlLimit?.let {
                    getJpaRepository().findAllWithLimit(it.offset, it.limit)
                } ?: getJpaRepository().findAll()
            }
        }
    }

    protected abstract fun findAllWithFilter(filter: String): List<T>

    protected abstract fun getJpaRepository(): ArchJpaRepository<T>

    protected abstract fun toEntity(archType: T): Entity

    protected abstract fun toArchType(archType: T? = null, entity: Entity): T

    private fun toEntityCollection(records: List<T>): EntityCollection {
        val collection = EntityCollection()
        if (records.isNotEmpty()) {
            val entities = collection.entities
            records.forEach {
                entities.add(toEntity(it))
            }
        }
        return collection
    }

    fun getData(
        sqlLimit: SqlLimit?,
        keyPredicates: List<UriParameter>?,
        filters: String?,
    ): EntityCollection {
        val records = findAll(sqlLimit, keyPredicates, filters)
        return toEntityCollection(records)
    }

    fun addData(entity: Entity?) = entity?.let {
        val record = getJpaRepository().save(toArchType(entity = entity))
        toEntity(record)
    }

    fun updateData(keyPredicates: List<UriParameter>, entity: Entity) {
        val records = findAll(null, keyPredicates, null)
        if (records.isNotEmpty()) {
            getJpaRepository().save(toArchType(records[0], entity))
        }
    }

    fun deleteData(keyPredicates: List<UriParameter>) {
        val records = findAll(null, keyPredicates, null)
        if (records.isNotEmpty()) {
            getJpaRepository().delete(records[0])
        }
    }
}