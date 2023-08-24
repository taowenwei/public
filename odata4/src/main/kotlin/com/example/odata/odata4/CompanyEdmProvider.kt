package com.example.odata.odata4

import com.example.odata.domain.Job
import com.example.odata.domain.Team
import org.apache.olingo.commons.api.edm.FullQualifiedName
import org.apache.olingo.commons.api.edm.provider.CsdlAbstractEdmProvider
import org.apache.olingo.commons.api.edm.provider.CsdlEntityContainer
import org.apache.olingo.commons.api.edm.provider.CsdlEntityContainerInfo
import org.apache.olingo.commons.api.edm.provider.CsdlEntitySet
import org.apache.olingo.commons.api.edm.provider.CsdlSchema
import org.springframework.stereotype.Component


const val NAMESPACE = "public"

// EDM Container
const val CONTAINER_NAME = "container"
val CONTAINER = FullQualifiedName(NAMESPACE, CONTAINER_NAME)

// Entity Types Names
const val ET_TEAM_NAME = "team"
val ET_TEAM_FQN = FullQualifiedName(NAMESPACE, ET_TEAM_NAME)
const val ES_TEAMS_NAME = "teams"

const val ET_JOB_NAME = "job"
val ET_JOB_FQN = FullQualifiedName(NAMESPACE, ET_JOB_NAME)
const val ES_JOBS_NAME = "jobs"

@Component
class CommerceEdmProvider : CsdlAbstractEdmProvider() {
    override fun getEntityType(entityTypeName: FullQualifiedName) = when (entityTypeName) {
        ET_TEAM_FQN -> Team.entityType(ET_TEAM_NAME)
        ET_JOB_FQN -> Job.entityType(ET_JOB_NAME)
        else -> null
    }

    override fun getEntitySet(entityContainer: FullQualifiedName, entitySetName: String) = when (entityContainer) {
        CONTAINER -> when (entitySetName) {
            ES_TEAMS_NAME -> {
                val entitySet = CsdlEntitySet()
                entitySet.name = ES_TEAMS_NAME
                entitySet.setType(ET_TEAM_FQN)
                entitySet
            }
            ES_JOBS_NAME -> {
                val entitySet = CsdlEntitySet()
                entitySet.name = ES_JOBS_NAME
                entitySet.setType(ET_JOB_FQN)
                entitySet
            }
            else -> null
        }
        else -> null
    }

    override fun getEntityContainer(): CsdlEntityContainer? {
        val entityContainer = CsdlEntityContainer()
        entityContainer.name = CONTAINER_NAME
        entityContainer.entitySets =
            listOf(getEntitySet(CONTAINER, ES_TEAMS_NAME), getEntitySet(CONTAINER, ES_JOBS_NAME))
        return entityContainer
    }

    override fun getSchemas(): List<CsdlSchema>? {
        val schema = CsdlSchema()
        schema.namespace = NAMESPACE
        schema.entityTypes = listOf(getEntityType(ET_TEAM_FQN), getEntityType(ET_JOB_FQN))
        schema.entityContainer = entityContainer
        return listOf(schema)
    }

    override fun getEntityContainerInfo(entityContainerName: FullQualifiedName?): CsdlEntityContainerInfo? {
        val entityContainerInfo = CsdlEntityContainerInfo()
        entityContainerInfo.containerName = CONTAINER
        return entityContainerInfo
    }
}