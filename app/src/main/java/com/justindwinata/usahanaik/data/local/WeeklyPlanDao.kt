package com.justindwinata.usahanaik.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import kotlinx.coroutines.flow.Flow

@Dao
interface WeeklyPlanDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsertPlan(plan: WeeklyGrowthPlanEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsertTasks(tasks: List<WeeklyTaskEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsertMilestones(milestones: List<WeeklyMilestoneEntity>)

    @Query("SELECT * FROM weekly_growth_plans WHERE id = :id LIMIT 1")
    suspend fun getPlan(id: Long = WeeklyGrowthPlanEntity.ACTIVE_PLAN_ID): WeeklyGrowthPlanEntity?

    @Query("SELECT * FROM weekly_growth_plans WHERE id = :id LIMIT 1")
    fun observePlan(id: Long = WeeklyGrowthPlanEntity.ACTIVE_PLAN_ID): Flow<WeeklyGrowthPlanEntity?>

    @Query("SELECT * FROM weekly_tasks WHERE planId = :planId ORDER BY sortOrder ASC")
    suspend fun getTasks(planId: Long = WeeklyGrowthPlanEntity.ACTIVE_PLAN_ID): List<WeeklyTaskEntity>

    @Query("SELECT * FROM weekly_milestones WHERE planId = :planId ORDER BY sortOrder ASC")
    suspend fun getMilestones(planId: Long = WeeklyGrowthPlanEntity.ACTIVE_PLAN_ID): List<WeeklyMilestoneEntity>

    @Query("UPDATE weekly_tasks SET status = :status WHERE id = :taskId")
    suspend fun updateTaskStatus(taskId: String, status: String)

    @Query("UPDATE weekly_milestones SET status = :status, progressPercentage = :progressPercentage WHERE id = :milestoneId")
    suspend fun updateMilestoneStatus(milestoneId: String, status: String, progressPercentage: Int)

    @Query("SELECT EXISTS(SELECT 1 FROM weekly_growth_plans WHERE id = :id)")
    suspend fun hasPlan(id: Long = WeeklyGrowthPlanEntity.ACTIVE_PLAN_ID): Boolean

    @Query("DELETE FROM weekly_tasks WHERE planId = :planId")
    suspend fun deleteTasks(planId: Long = WeeklyGrowthPlanEntity.ACTIVE_PLAN_ID)

    @Query("DELETE FROM weekly_milestones WHERE planId = :planId")
    suspend fun deleteMilestones(planId: Long = WeeklyGrowthPlanEntity.ACTIVE_PLAN_ID)

    @Query("DELETE FROM weekly_growth_plans WHERE id = :id")
    suspend fun deletePlan(id: Long = WeeklyGrowthPlanEntity.ACTIVE_PLAN_ID)

    @Transaction
    suspend fun replaceActivePlan(
        plan: WeeklyGrowthPlanEntity,
        tasks: List<WeeklyTaskEntity>,
        milestones: List<WeeklyMilestoneEntity>
    ) {
        deleteTasks(plan.id)
        deleteMilestones(plan.id)
        deletePlan(plan.id)
        upsertPlan(plan)
        upsertTasks(tasks)
        upsertMilestones(milestones)
    }

    @Transaction
    suspend fun deleteActivePlan() {
        deleteTasks()
        deleteMilestones()
        deletePlan()
    }
}
