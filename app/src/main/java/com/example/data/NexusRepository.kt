package com.example.data

import com.example.BuildConfig
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext

class NexusRepository(
    private val memoryDao: MemoryDao,
    private val taskDao: TaskDao,
    private val financeDao: FinanceDao,
    private val agentLogDao: AgentLogDao,
    private val geminiService: GeminiApiService = RetrofitClient.service
) {
    // Local DB Observables
    val allMemories: Flow<List<MemoryEntity>> = memoryDao.getAllMemories()
    val allTasks: Flow<List<TaskEntity>> = taskDao.getAllTasks()
    val allTransactions: Flow<List<FinanceEntity>> = financeDao.getAllTransactions()
    val allAgentLogs: Flow<List<AgentLogEntity>> = agentLogDao.getAllLogs()

    // Local DB Mutators
    suspend fun insertMemory(key: String, content: String) = withContext(Dispatchers.IO) {
        memoryDao.insertMemory(MemoryEntity(key = key, content = content))
    }

    suspend fun deleteMemoryById(id: Int) = withContext(Dispatchers.IO) {
        memoryDao.deleteMemoryById(id)
    }

    suspend fun insertTask(title: String, details: String, deadline: String, priority: String, category: String) = withContext(Dispatchers.IO) {
        taskDao.insertTask(
            TaskEntity(
                title = title,
                details = details,
                deadline = deadline,
                priority = priority,
                category = category
            )
        )
    }

    suspend fun updateTask(task: TaskEntity) = withContext(Dispatchers.IO) {
        taskDao.updateTask(task)
    }

    suspend fun deleteTaskById(id: Int) = withContext(Dispatchers.IO) {
        taskDao.deleteTaskById(id)
    }

    suspend fun insertTransaction(title: String, amount: Double, isExpense: Boolean, category: String) = withContext(Dispatchers.IO) {
        financeDao.insertTransaction(
            FinanceEntity(
                title = title,
                amount = amount,
                isExpense = isExpense,
                category = category
            )
        )
    }

    suspend fun deleteTransactionById(id: Int) = withContext(Dispatchers.IO) {
        financeDao.deleteTransactionById(id)
    }

    suspend fun insertAgentLog(log: AgentLogEntity) = withContext(Dispatchers.IO) {
        agentLogDao.insertLog(log)
    }

    suspend fun clearAgentLogs() = withContext(Dispatchers.IO) {
        agentLogDao.clearLogs()
    }

    // --- Gemini Multi-Agent System Core Trigger ---
    suspend fun generateMultiAgentPlan(
        goal: String,
        targetAgent: String = "Auto-Select"
    ): AgentLogEntity = withContext(Dispatchers.IO) {
        val apiKey = BuildConfig.GEMINI_API_KEY
        if (apiKey.isEmpty() || apiKey == "MY_GEMINI_API_KEY") {
            return@withContext AgentLogEntity(
                goal = goal,
                masterPlan = "NEXUS AI System Keys Initializing... See Suggestions.",
                agentWork = "Agent simulation bypassed: Key not loaded.",
                finalResult = "To activate full autonomous capabilities, please insert your personal Gemini API key in the AI Studio Secrets panel.",
                suggestions = "No credentials. Retrying with locally compiled simulation outputs.",
                activeAgents = "Master AI"
            )
        }

        // System Instruction setting up Master AI orchestration
        val systemInstructionText = """
            You are NEXUS AI, an advanced autonomous multi-agent engineering operating system.
            You manage 16 specialized agents: 
            1. STUDY AGENT: Syllabus, paper analysis, important questions.
            2. RESEARCH AGENT: Literature check, IEEE formatting, citation research.
            3. MECHANICAL ENGINEERING AGENT: Machine Design, CFD, thermodynamics, fluid, calculations.
            4. CODING AGENT: Python, Arduino, CPP, JavaScript, fullstack.
            5. DOCUMENT AI: Summary, structuring, knowledge base.
            6. PRODUCTIVITY AI: Timetable, reminders, trackers.
            7. COMMUNICATION AI: Pitch sheets, professional emails, resume.
            8. HACKATHON AI: Innovation, SIH ideas, timelines, budget roadmap.
            9. DESIGN AI: Logo prompts, wireframes, posters, styling.
            10. INTERNET AI: Engineering trends, alert summaries.
            11. FINANCE AI: Expense sheets, competition budgeting.
            12. VOICE AI: Speech control, audio logs.
            13. AUTOMATION AI: n8n, workflows, scripts.
            14. MEMORY AI: User preferences, long term records.
            15. DESKTOP AI: System workflow advice, downloads.
            16. MOBILE AI: Trackers, notifications, coordinates.

            Understand the user's objective. Determine which agents are critical. Run a simulated delegation sequence and produce the result exactly in the requested output format.

            You MUST strictly format your output under four explicit sections:
            1. '🧠 Master Plan' - describe Goal, Selected Agents, and Task sequence of actions.
            2. '🤖 Agent Work' - provide detailed, bulleted tech analysis or derivations/scripts from each participating agent.
            3. '📊 Final Result' - consolidate the solution or actual deliverable in a beautiful summary.
            4. '💡 Suggestions' - suggest better approaches, automation hooks, or risks.

            Never break character. You are high-end, precise, professional, encouraging, and focused on mechanical engineering, entrepreneurship, and technical growth.
        """.trimIndent()

        val prompt = if (targetAgent == "Auto-Select") {
            "Implement and solve the following target engineering goal: $goal"
        } else {
            "Explicitly route this goal to $targetAgent first, and combine support from other agents: $goal"
        }

        val request = GenerateContentRequest(
            contents = listOf(Content(parts = listOf(Part(text = prompt)))),
            systemInstruction = Content(parts = listOf(Part(text = systemInstructionText)))
        )

        try {
            // Using gemini-3.5-flash for responsive multi-agent simulation
            val response = geminiService.generateContent("gemini-3.5-flash", apiKey, request)
            val fullText = response.candidates?.firstOrNull()?.content?.parts?.firstOrNull()?.text
                ?: "System anomaly. Could not capture neural signals from Master AI."

            // Parse response into structural sections
            val sections = parseGeminiSections(fullText)
            val log = AgentLogEntity(
                goal = goal,
                masterPlan = sections["masterPlan"] ?: "Goal accepted. Master AI coordinating telemetry.",
                agentWork = sections["agentWork"] ?: "Working...",
                finalResult = sections["finalResult"] ?: "Processing core results.",
                suggestions = sections["suggestions"] ?: "Proposing optimization paths.",
                activeAgents = targetAgent
            )
            // Log local history automatically
            agentLogDao.insertLog(log)
            log

        } catch (e: Exception) {
            val errorLog = AgentLogEntity(
                goal = goal,
                masterPlan = "Master Connection Error: ${e.message}",
                agentWork = "Communication channel down.",
                finalResult = "Failed to establish secure proxy with Gemini cloud endpoints.",
                suggestions = "Ensure internet access is enabled and verify GEMINI_API_KEY value.",
                activeAgents = "Master AI"
            )
            errorLog
        }
    }

    private fun parseGeminiSections(rawText: String): Map<String, String> {
        val resultMap = mutableMapOf<String, String>()
        
        // Find positions of headers
        val masterPlanHeader = listOf("**🧠 Master Plan**", "🧠 Master Plan", "Master Plan")
        val agentWorkHeader = listOf("**🤖 Agent Work**", "🤖 Agent Work", "Agent Work")
        val finalResultHeader = listOf("**📊 Final Result**", "📊 Final Result", "Final Result")
        val suggestionsHeader = listOf("**💡 Suggestions**", "💡 Suggestions", "Suggestions")

        fun findHeaderIndex(headers: List<String>): Int {
            for (header in headers) {
                val idx = rawText.indexOf(header)
                if (idx != -1) return idx
            }
            return -1
        }

        fun getHeaderMatched(headers: List<String>): String? {
            for (header in headers) {
                if (rawText.contains(header)) return header
            }
            return null
        }

        val idx1 = findHeaderIndex(masterPlanHeader)
        val idx2 = findHeaderIndex(agentWorkHeader)
        val idx3 = findHeaderIndex(finalResultHeader)
        val idx4 = findHeaderIndex(suggestionsHeader)

        // Extract substrings based on indices found
        try {
            if (idx1 != -1 && idx2 != -1) {
                val matchedHeader = getHeaderMatched(masterPlanHeader) ?: "Master Plan"
                resultMap["masterPlan"] = rawText.substring(idx1 + matchedHeader.length, idx2).trim()
            } else if (idx1 != -1) {
                val matchedHeader = getHeaderMatched(masterPlanHeader) ?: "Master Plan"
                resultMap["masterPlan"] = rawText.substring(idx1 + matchedHeader.length).trim()
            } else {
                resultMap["masterPlan"] = "Delegated to the autonomous cluster."
            }

            if (idx2 != -1) {
                val matchedHeader = getHeaderMatched(agentWorkHeader) ?: "Agent Work"
                val endIdx = if (idx3 != -1) idx3 else if (idx4 != -1) idx4 else rawText.length
                resultMap["agentWork"] = rawText.substring(idx2 + matchedHeader.length, endIdx).trim()
            } else {
                resultMap["agentWork"] = "Specialized agents are preparing outputs."
            }

            if (idx3 != -1) {
                val matchedHeader = getHeaderMatched(finalResultHeader) ?: "Final Result"
                val endIdx = if (idx4 != -1) idx4 else rawText.length
                resultMap["finalResult"] = rawText.substring(idx3 + matchedHeader.length, endIdx).trim()
            } else {
                resultMap["finalResult"] = rawText
            }

            if (idx4 != -1) {
                val matchedHeader = getHeaderMatched(suggestionsHeader) ?: "Suggestions"
                resultMap["suggestions"] = rawText.substring(idx4 + matchedHeader.length).trim()
            } else {
                resultMap["suggestions"] = "- Verify calculations locally.\n- Set up continuous automation dashboards."
            }
        } catch (_: Exception) {
            resultMap["masterPlan"] = rawText
            resultMap["agentWork"] = "Check full text response raw parsing details."
            resultMap["finalResult"] = rawText
            resultMap["suggestions"] = "Optimize query density."
        }

        return resultMap
    }
}
