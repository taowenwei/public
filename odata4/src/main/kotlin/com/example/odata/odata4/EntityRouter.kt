package com.example.odata.odata4

import com.example.odata.domain.JobEntityRepository
import com.example.odata.domain.TeamEntityRepository
import org.apache.olingo.commons.api.data.Entity
import org.apache.olingo.commons.api.data.EntityCollection
import org.apache.olingo.commons.api.edm.EdmEntitySet
import org.apache.olingo.server.api.uri.UriParameter
import org.springframework.stereotype.Component

data class SqlLimit(val offset: Int, val limit: Int)

@Component
class EntityRouter(
    private val teamRepository: TeamEntityRepository,
    private val jobRepository: JobEntityRepository,
    private val edmProvider: CommerceEdmProvider,
) {
    fun getData(
        edmEntitySet: EdmEntitySet,
        sqlLimit: SqlLimit? = null,
        keyPredicates: List<UriParameter>? = null,
        filters: String? = null,
    ) = when (edmEntitySet.name) {
        ES_TEAMS_NAME -> teamRepository.getData(sqlLimit, keyPredicates, filters)
        ES_JOBS_NAME -> jobRepository.getData(sqlLimit, keyPredicates, filters)
        else -> EntityCollection()
    }

    fun addData(
        edmEntitySet: EdmEntitySet,
        entity: Entity?,
    ) = when (edmEntitySet.name) {
        ES_TEAMS_NAME -> teamRepository.addData(entity)
        ES_JOBS_NAME -> jobRepository.addData(entity)
        else -> null
    }

    fun updateData(
        edmEntitySet: EdmEntitySet,
        keyPredicates: List<UriParameter>,
        new: Entity,
    ) {
        when (edmEntitySet.name) {
            ES_TEAMS_NAME -> teamRepository.updateData(keyPredicates, new)
            ES_JOBS_NAME -> jobRepository.updateData(keyPredicates, new)
        }
    }

    fun deleteData(
        edmEntitySet: EdmEntitySet,
        keyPredicates: List<UriParameter>,
    ) {
        when (edmEntitySet.name) {
            ES_TEAMS_NAME -> teamRepository.deleteData(keyPredicates)
            ES_JOBS_NAME -> jobRepository.deleteData(keyPredicates)
        }
    }

//    fun getRelatedData(
//        edmEntitySet: EdmEntitySet,
//        entity: Entity,
//        uriResource: UriResource,
//    ): Pair<EdmEntitySet, EntityCollection> {
//        val navigation = uriResource as UriResourceNavigation
//        val property = navigation.property
//        val edmBindingTarget =
//            if (!property.containsTarget()) edmEntitySet.getRelatedBindingTarget(property.name) else edmEntitySet
//        val respEdmEntitySet = edmBindingTarget as EdmEntitySet
//
//        return when (edmEntitySet.name) {
//            ES_TEAMS_NAME -> {
//                val entityCollection = teamRepository.getRelatedData(entity,
//                    navigation,
//                    jobRepository)
//                Pair(respEdmEntitySet, entityCollection)
//            }
//            ES_JOBS_NAME -> {
//                val entityCollection = jobRepository.getRelatedData(entity,
//                    navigation,
//                    teamRepository)
//                Pair(respEdmEntitySet, entityCollection)
//            }
//            else -> Pair(respEdmEntitySet, EntityCollection())
//        }
//    }
}