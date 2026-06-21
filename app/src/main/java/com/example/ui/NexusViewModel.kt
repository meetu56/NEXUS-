package com.example.ui

import android.app.Application
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.data.*
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class NexusViewModel(
    application: Application,
    private val repository: NexusRepository
) : AndroidViewModel(application) {

    // --- Tab and Screen Navigation state ---
    var currentTab by mutableStateOf("dashboard") // dashboard, console, trackers, history

    // --- Interactive Master AI Input states ---
    var masterGoalQuery by mutableStateOf("")
    var selectedRouteAgent by mutableStateOf("Auto-Select")
    var isGenerating by mutableStateOf(false)

    // --- Focused Agent Console state ---
    var inspectingAgentName by mutableStateOf<String?>(null) // e.g. "STUDY AGENT"
    var agentDirectQuery by mutableStateOf("")
    var agentDirectResponse by mutableStateOf("")
    var isAgentQuerying by mutableStateOf(false)

    // --- Database Observables ---
    val memories: StateFlow<List<MemoryEntity>> = repository.allMemories
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val tasks: StateFlow<List<TaskEntity>> = repository.allTasks
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val transactions: StateFlow<List<FinanceEntity>> = repository.allTransactions
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val agentLogs: StateFlow<List<AgentLogEntity>> = repository.allAgentLogs
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    // --- Active Plan display state (defaults to an engineered startup state) ---
    var activePlanLog by mutableStateOf(
        AgentLogEntity(
            goal = "Boot system & synchronize core services",
            masterPlan = "1. Establish local encrypted SQLite storage\n2. Initialize 16 specialized multi-agent subroutines\n3. Load user telemetry preferences\n4. Power up cognitive neural API bridge",
            agentWork = """
                • MASTER AI: Initializing core system... COMPLETE.
                • STUDY AGENT: GTU Mechanical Syllabus index complete. Ready to optimize revision plans.
                • MECH AGENT: Thermal stress, fluid dynamics, and Autodesk CAD concepts loaded to memory.
                • CODING AGENT: Arduino and FastAPI automation hooks ready for testing.
                • PRODUCTIVITY AI: Standard calendar logs and task pipelines active.
                • FINANCE AI: Standard research budget ledger online.
                • MEMORY AI: Telemetry databases secured offline.
            """.trimIndent(),
            finalResult = "NEXUS Personal Operating System v3.5 is fully online. Integrated multi-agent workflows are active and monitoring. Standing by for mechanical directives.",
            suggestions = "• Provide a task or design directive in the terminal query block.\n• Toggle the 'Agent Workspace' to inspect specialized subsystem capabilities.\n• Log target study goals, budgets, and tasks in the diagnostic deck.",
            activeAgents = "Master AI Cluster"
        )
    )

    // --- Database Operations ---
    fun addMemory(key: String, content: String) {
        viewModelScope.launch {
            repository.insertMemory(key, content)
        }
    }

    fun removeMemory(id: Int) {
        viewModelScope.launch {
            repository.deleteMemoryById(id)
        }
    }

    fun addTask(title: String, details: String, deadline: String, priority: String, category: String) {
        viewModelScope.launch {
            repository.insertTask(title, details, deadline, priority, category)
        }
    }

    fun toggleTaskCompletion(task: TaskEntity) {
        viewModelScope.launch {
            repository.updateTask(task.copy(isCompleted = !task.isCompleted))
        }
    }

    fun removeTask(id: Int) {
        viewModelScope.launch {
            repository.deleteTaskById(id)
        }
    }

    fun addTransaction(title: String, amount: Double, isExpense: Boolean, category: String) {
        viewModelScope.launch {
            repository.insertTransaction(title, amount, isExpense, category)
        }
    }

    fun removeTransaction(id: Int) {
        viewModelScope.launch {
            repository.deleteTransactionById(id)
        }
    }

    fun resetSystemLogs() {
        viewModelScope.launch {
            repository.clearAgentLogs()
            activePlanLog = AgentLogEntity(
                goal = "Diagnostics restart completed",
                masterPlan = "Neural buffers cleared. Memory integrity safe.",
                agentWork = "All active worker threads set to standby.",
                finalResult = "Operational environment refreshed.",
                suggestions = "Create a new Master Plan directive.",
                activeAgents = "Master AI"
            )
        }
    }

    // --- Master AI Execution Core ---
    fun submitMasterGoal() {
        if (masterGoalQuery.isBlank()) return
        val currentQuery = masterGoalQuery
        masterGoalQuery = ""
        isGenerating = true

        viewModelScope.launch {
            val result = repository.generateMultiAgentPlan(currentQuery, selectedRouteAgent)
            activePlanLog = result
            isGenerating = false
        }
    }

    // --- Direct Agent Query Execution ---
    fun submitDirectAgentQuery(agentName: String) {
        if (agentDirectQuery.isBlank()) return
        val query = agentDirectQuery
        agentDirectQuery = ""
        isAgentQuerying = true
        agentDirectResponse = "Coordinating neural resources to formulate agent response..."

        viewModelScope.launch {
            val generated = com.example.BuildConfig.GEMINI_API_KEY
            if (generated.isEmpty() || generated == "MY_GEMINI_API_KEY") {
                agentDirectResponse = "Agent [${agentName}] is operating in OFFLINE Simulation matches. Connect Gemini credentials to unlock live research, patent calculations, and syllabus answers.\n\nOffline response simulation:\n\"Analyzing mechanical goal: $query. Local structural models support high load coefficients.\""
                isAgentQuerying = false
                return@launch
            }

            val systemInstruction = "You are the specialized $agentName of NEXUS AI. Answer the following query strictly with the expertise matching your role and domain. Be detailed, technical, and precise: $query"
            val requestBody = GenerateContentRequest(
                contents = listOf(Content(parts = listOf(Part(text = query)))),
                systemInstruction = Content(parts = listOf(Part(text = systemInstruction)))
            )

            try {
                val response = RetrofitClient.service.generateContent("gemini-3.5-flash", generated, requestBody)
                val text = response.candidates?.firstOrNull()?.content?.parts?.firstOrNull()?.text
                    ?: "[Subsystem silent. Verify telemetry connections.]"
                agentDirectResponse = text

                // Automatically log to memory for retrieval!
                repository.insertMemory(agentName, "Inquiry: $query\nResponse: $text")

            } catch (e: Exception) {
                agentDirectResponse = "Neural link dropped: ${e.message}. System offline fallback engaged."
            } finally {
                isAgentQuerying = false
            }
        }
    }
}

class ViewModelFactory(
    private val application: Application,
    private val repository: NexusRepository
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(NexusViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return NexusViewModel(application, repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
