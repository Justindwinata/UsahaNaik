package com.justindwinata.usahanaik.data.repository

import com.justindwinata.usahanaik.data.local.WeeklyRetrospectiveDao
import com.justindwinata.usahanaik.data.mapper.WeeklyRetrospectiveMapper.toDomain
import com.justindwinata.usahanaik.data.mapper.WeeklyRetrospectiveMapper.toEntity
import com.justindwinata.usahanaik.domain.model.WeeklyRetrospective
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class LocalWeeklyRetrospectiveRepository(
    private val dao: WeeklyRetrospectiveDao,
    private val nowProvider: () -> Long = { System.currentTimeMillis() }
) : WeeklyRetrospectiveRepository {
    override suspend fun saveRetrospective(retrospective: WeeklyRetrospective): WeeklyRetrospective {
        dao.deleteRetrospectiveForWeek(retrospective.weekLabel)
        val now = nowProvider()
        val entity = retrospective.toEntity(now)
        val id = dao.insertRetrospective(entity)
        return entity.copy(id = id).toDomain()
    }

    override suspend fun getRetrospectiveForWeek(weekLabel: String): WeeklyRetrospective? {
        return dao.getRetrospectiveForWeek(weekLabel)?.toDomain()
    }

    override suspend fun getLatestRetrospective(): WeeklyRetrospective? {
        return dao.getLatestRetrospective()?.toDomain()
    }

    override suspend fun listRetrospectives(): List<WeeklyRetrospective> {
        return dao.listRetrospectives().map { it.toDomain() }
    }

    override fun observeRetrospectives(): Flow<List<WeeklyRetrospective>> {
        return dao.observeRetrospectives().map { retrospectives -> retrospectives.map { it.toDomain() } }
    }

    override suspend fun clearRetrospectives() {
        dao.clearRetrospectives()
    }
}
