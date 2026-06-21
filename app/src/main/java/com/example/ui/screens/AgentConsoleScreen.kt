package com.example.ui.screens

import androidx.compose.animation.*
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.NexusViewModel
import com.example.ui.theme.*

data class AgentMeta(
    val name: String,
    val description: String,
    val icon: ImageVector,
    val color: Color,
    val expertise: List<String>,
    val placeholderQuery: String
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AgentConsoleScreen(viewModel: NexusViewModel) {
    val agents = remember {
        listOf(
            AgentMeta(
                "STUDY AGENT",
                "Syllabus checks, notes & revision worksheets.",
                Icons.Default.School,
                CyberCyan,
                listOf("Gujarat Technological Univ (GTU) syllabus indexer", "Formula sheet compactor", "Important exam question forecasting", "Notes Summarization"),
                "Forecast 5 important questions for Fluid Mechanics Exam based on typical GTU paper structures."
            ),
            AgentMeta(
                "RESEARCH AGENT",
                "Literature searches, citing & IEEE structures.",
                Icons.Default.MenuBook,
                CyberBlue,
                listOf("Literature check & synthesis reports", "APA/IEEE text formats", "Research gap discovery", "Publication drafts"),
                "Synthesize a list of recent literature papers related to smart agriculture solar water pumps and format citations in IEEE standard."
            ),
            AgentMeta(
                "MECHANICAL AGENT",
                "Formulas, fluid mechanics, CAD & CFD advice.",
                Icons.Default.Build,
                CyberCyan,
                listOf("Stress limit & machine sizing calculations", "Thermodynamics heat cycles", "Aerodynamics & CFD parameters", "HVAC pipeline modeling"),
                "Calculate head and torque requirements for a water pump lifting water at 25 liters/minute to a height of 15 meters."
            ),
            AgentMeta(
                "CODING AGENT",
                "Arduino, Python and fullstack operations.",
                Icons.Default.Code,
                CyberGreen,
                listOf("Arduino nano & ESP32 direct scripts", "FastAPI background routers", "Python automation scripts", "Data analysis algorithms"),
                "Write an Arduino script to control a soil moisture sensor connected to an ESP32 micro-servo valve."
            ),
            AgentMeta(
                "DOCUMENT AI",
                "File text summaries and tag index folders.",
                Icons.Default.Article,
                TextSecondary,
                listOf("Context extraction from PDFs/Word files", "Knowledge base builder", "Duplicate tag discovery", "Automated folder layout"),
                "Summarize a conceptual structure for organizing a mechanical engineer's design handbook."
            ),
            AgentMeta(
                "PRODUCTIVITY AI",
                "Time tracks, timetables, reminder loops.",
                Icons.Default.CheckCircle,
                CyberGreen,
                listOf("Exams & thesis timetabling", "Priority habit building", "Calendar slot syncing", "Task progression logs"),
                "Generate an action-packed 3-hour weekly review timetable for an active engineering researcher."
            ),
            AgentMeta(
                "COMMUNICATION AI",
                "Email drafting, LinkedIn articles, resubmits.",
                Icons.Default.Email,
                CyberBlue,
                listOf("SOP coverletters", "Professional email pitches", "Academic proposals", "Engineering summaries for LinkedIn"),
                "Draft a high-impact email to a hardware vendor pitching a collaborative pilot project for farmers in Gujarat."
            ),
            AgentMeta(
                "HACKATHON AI",
                "SIH setups, competition decks & budgeting.",
                Icons.Default.Lightbulb,
                CyberMagenta,
                listOf("Smart India Hackathon ideas", "ISHRAE HVAC competitions", "Core budgets & cost timelines", "Innovation roadmapping"),
                "Draft an innovation roadmap and estimated budget layout for the NASA Space Apps green energy portal challenge."
            ),
            AgentMeta(
                "DESIGN AI",
                "Wireframes, graphic layouts, poster drafts.",
                Icons.Default.Palette,
                CyberMagenta,
                listOf("Logo design midjourney prompts", "Poster specifications", "Website wireframing blueprints", "Mechanical product concepts"),
                "Generate a premium UI/UX theme and graphic styling concept for an engineering operating system dashboard."
            ),
            AgentMeta(
                "INTERNET AI",
                "Realtime alerts, tech calls & grants search.",
                Icons.Default.Public,
                CyberCyan,
                listOf("Live engineering research alerts", "Funding announcements & grants", "Scholarship alerts", "Conference calls indexer"),
                "Summarize the latest developments in 2026 mechanical smart automation trends with general references."
            ),
            AgentMeta(
                "FINANCE AI",
                "Cost Ledgers & savings dashboards.",
                Icons.Default.AccountBalanceWallet,
                CyberOrange,
                listOf("Hardware components pricing checklist", "Scholarship payout planning", "Competitive event expense sheets", "Budget tracker integration"),
                "Draft a parts budget spreadsheet table for building a prototype IoT solar pump."
            ),
            AgentMeta(
                "VOICE AI",
                "Voice commands, dictation transcription.",
                Icons.Default.Mic,
                CyberCyan,
                listOf("Speech commands interpreter", "Sound memos transcribing", "Hands-free operations checklists", "Auditory system logs"),
                "Simulate a hands-free voice directive list for starting engine diagnostics telemetry check."
            ),
            AgentMeta(
                "AUTOMATION AI",
                "n8n workflows, webhook integrations.",
                Icons.Default.Autorenew,
                CyberOrange,
                listOf("n8n action logs", "Google Sheet triggers", "GitHub deploy hooks", "Periodic scraping triggers"),
                "Outline an automation script outline connecting low-soil humidity triggers to standard telegram alerts."
            ),
            AgentMeta(
                "MEMORY AI",
                "Preferences database search.",
                Icons.Default.Storage,
                CyberBlue,
                listOf("Searchable credentials files", "Academics/research folders indices", "Privacy key management", "User habit preferences folder"),
                "Show details of how memory AI archives long-term preferences offline to save tokens on prompt setups."
            ),
            AgentMeta(
                "DESKTOP AI",
                "VS Code shortcuts, CAD software help.",
                Icons.Default.Computer,
                TextSecondary,
                listOf("Autodesk CAD/SolidWorks design macros", "VS Code command presets", "Commandline build setups", "Screenshot capture flow"),
                "Show the top 5 Solidworks keyboard shortcuts for rapid drawing sketch constraints."
            ),
            AgentMeta(
                "MOBILE AI",
                "Local notifications trigger, camera scanning.",
                Icons.Default.PhoneAndroid,
                CyberMagenta,
                listOf("Push alert managers", "Camera image text scanner", "Mock GPS coordinates check", "Cross-device telemetry sync"),
                "Explain the system configuration used for scanning printed documents using camera feed and local OCR."
            )
        )
    }

    if (viewModel.inspectingAgentName == null) {
        // --- BASE SECTOR: GRID SHOWING ALL 16 AGENTS ---
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Spacer(modifier = Modifier.height(12.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = "INTEGRATED COGNITIVE ARRAY",
                        style = MaterialTheme.typography.labelLarge,
                        color = CyberCyan
                    )
                    Text(
                        text = "16 ACTIVE MULTI-AGENTS",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = TextPrimary
                    )
                }
            }

            LazyVerticalGrid(
                columns = GridCells.Fixed(2),
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(bottom = 96.dp),
                horizontalArrangement = Arrangement.spacedBy(10.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                items(agents) { agent ->
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(130.dp)
                            .testTag("agent_card_${agent.name.lowercase().replace(" ", "_")}")
                            .clickable { viewModel.inspectingAgentName = agent.name },
                        shape = RoundedCornerShape(12.dp),
                        border = BorderStroke(1.dp, GridLines),
                        colors = CardDefaults.cardColors(containerColor = SpaceNavy)
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(12.dp),
                            verticalArrangement = Arrangement.SpaceBetween
                        ) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.Top
                            ) {
                                Icon(
                                    imageVector = agent.icon,
                                    contentDescription = agent.name,
                                    tint = agent.color,
                                    modifier = Modifier.size(28.dp)
                                )
                                Box(
                                    modifier = Modifier
                                        .size(6.dp)
                                        .clip(RoundedCornerShape(3.dp))
                                        .background(CyberGreen)
                                )
                            }

                            Column {
                                Text(
                                    text = agent.name,
                                    style = MaterialTheme.typography.labelLarge,
                                    fontWeight = FontWeight.Bold,
                                    color = TextPrimary,
                                    fontSize = 11.sp
                                )
                                Text(
                                    text = agent.description,
                                    style = MaterialTheme.typography.bodySmall,
                                    color = TextSecondary,
                                    fontSize = 9.sp,
                                    lineHeight = 12.sp,
                                    maxLines = 2
                                )
                            }
                        }
                    }
                }
            }
        }
    } else {
        // --- INSPECTION DIALOG SCREEN ---
        val selectedAgent = agents.first { it.name == viewModel.inspectingAgentName }

        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
                .clip(RoundedCornerShape(16.dp))
                .border(BorderStroke(1.dp, selectedAgent.color), RoundedCornerShape(16.dp))
                .background(SpaceNavy)
        ) {
            val scrollState = rememberScrollState()

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(scrollState)
                    .padding(20.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                // Return Bar
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.clickable {
                            viewModel.inspectingAgentName = null
                            viewModel.agentDirectResponse = ""
                        }
                    ) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "back", tint = selectedAgent.color)
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = "BACK TO ARRAY",
                            style = MaterialTheme.typography.labelLarge,
                            color = selectedAgent.color
                        )
                    }

                    Box(
                        modifier = Modifier
                            .border(BorderStroke(1.dp, selectedAgent.color), RoundedCornerShape(4.dp))
                            .padding(horizontal = 8.dp, vertical = 2.dp)
                    ) {
                        Text(
                            text = "ONLINE",
                            style = MaterialTheme.typography.labelSmall,
                            color = CyberGreen,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }

                // Core Agent Info Card
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = selectedAgent.icon,
                        contentDescription = selectedAgent.name,
                        tint = selectedAgent.color,
                        modifier = Modifier.size(48.dp)
                    )
                    Spacer(modifier = Modifier.width(16.dp))
                    Column {
                        Text(
                            text = selectedAgent.name,
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.Bold,
                            color = TextPrimary
                        )
                        Text(
                            text = selectedAgent.description,
                            style = MaterialTheme.typography.bodyMedium,
                            color = TextSecondary
                        )
                    }
                }

                // Expertise Check List
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .border(BorderStroke(1.dp, GridLines), RoundedCornerShape(8.dp))
                        .background(CodeGray)
                        .padding(12.dp)
                ) {
                    Text(
                        text = "DOMAIN EXPERTISE SPEC",
                        style = MaterialTheme.typography.labelSmall,
                        color = selectedAgent.color
                    )
                    Spacer(modifier = Modifier.height(6.dp))
                    selectedAgent.expertise.forEach { spec ->
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.padding(vertical = 2.dp)
                        ) {
                            Text(
                                text = "‣",
                                color = selectedAgent.color,
                                modifier = Modifier.padding(end = 6.dp),
                                fontWeight = FontWeight.Bold
                            )
                            Text(
                                text = spec,
                                style = MaterialTheme.typography.bodyMedium,
                                color = TextPrimary
                            )
                        }
                    }
                }

                // Prompt Workspace Area
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Text(
                        text = "DIRECT COMMUNICATIONS LINK",
                        style = MaterialTheme.typography.labelSmall,
                        color = selectedAgent.color
                    )

                    OutlinedTextField(
                        value = viewModel.agentDirectQuery,
                        onValueChange = { viewModel.agentDirectQuery = it },
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("agent_direct_input"),
                        placeholder = {
                            Text(
                                "Ask ${selectedAgent.name} anything...",
                                style = MaterialTheme.typography.bodyMedium,
                                color = TextMuted
                            )
                        },
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = selectedAgent.color,
                            unfocusedBorderColor = GridLines,
                            focusedContainerColor = CodeGray,
                            unfocusedContainerColor = CodeGray,
                            focusedTextColor = TextPrimary,
                            unfocusedTextColor = TextPrimary
                        ),
                        shape = RoundedCornerShape(8.dp)
                    )

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            text = "Preserve to memories automatically",
                            style = MaterialTheme.typography.labelSmall,
                            color = TextMuted,
                            modifier = Modifier.align(Alignment.CenterVertically)
                        )

                        Button(
                            onClick = { viewModel.submitDirectAgentQuery(selectedAgent.name) },
                            enabled = !viewModel.isAgentQuerying,
                            colors = ButtonDefaults.buttonColors(containerColor = selectedAgent.color),
                            shape = RoundedCornerShape(8.dp),
                            modifier = Modifier.testTag("agent_direct_submit")
                        ) {
                            if (viewModel.isAgentQuerying) {
                                CircularProgressIndicator(
                                    modifier = Modifier.size(16.dp),
                                    color = ObsidianDark,
                                    strokeWidth = 2.dp
                                )
                                Spacer(modifier = Modifier.width(6.dp))
                                Text("LINKING...", color = ObsidianDark, style = MaterialTheme.typography.labelSmall)
                            } else {
                                Text("TRANSMIT", color = ObsidianDark, style = MaterialTheme.typography.labelSmall)
                            }
                        }
                    }
                }

                // Response Area
                if (viewModel.agentDirectResponse.isNotEmpty()) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .border(BorderStroke(1.dp, GridLines), RoundedCornerShape(8.dp))
                            .background(CodeGray)
                            .padding(12.dp)
                    ) {
                        Text(
                            text = "${selectedAgent.name} FEEDBACK LOG",
                            style = MaterialTheme.typography.labelSmall,
                            color = selectedAgent.color
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = viewModel.agentDirectResponse,
                            style = MaterialTheme.typography.bodyMedium,
                            color = TextPrimary,
                            lineHeight = 18.sp
                        )
                    }
                }
                
                Spacer(modifier = Modifier.height(80.dp))
            }
        }
    }
}
