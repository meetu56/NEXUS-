package com.example.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "memories")
data class MemoryEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val key: String, // Tag or title (e.g. "PREFERENCES")
    val content: String, // Stringified data or note
    val timestamp: Long = System.currentTimeMillis()
)

@Entity(tableName = "tasks")
data class TaskEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val title: String,
    val details: String = "",
    val deadline: String = "", // formatted date
    val priority: String = "MEDIUM", // LOW, MEDIUM, HIGH
    val isCompleted: Boolean = false,
    val category: String = "TODO" // TODO, CALENDAR, HABIT, SYLLABUS
)

@Entity(tableName = "finance")
data class FinanceEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val title: String,
    val amount: Double,
    val isExpense: Boolean = true,
    val category: String = "RESEARCH", // SCHOLARSHIP, COMPETITION, HARDWARE, LEISURE, OTHER
    val timestamp: Long = System.currentTimeMillis()
)

@Entity(tableName = "agent_logs")
data class AgentLogEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val goal: String,
    val masterPlan: String,
    val agentWork: String,
    val finalResult: String,
    val suggestions: String,
    val activeAgents: String, // Comma-separated agent names
    val timestamp: Long = System.currentTimeMillis()
)
