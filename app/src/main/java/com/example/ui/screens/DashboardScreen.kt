package com.example.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.*
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.*
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.AgentLogEntity
import com.example.ui.NexusViewModel
import com.example.ui.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DashboardScreen(viewModel: NexusViewModel) {
    val memoriesList by viewModel.memories.collectAsState()
    val tasksList by viewModel.tasks.collectAsState()
    val transactionsList by viewModel.transactions.collectAsState()
    val logsList by viewModel.agentLogs.collectAsState()

    var activeOutputTab by remember { mutableStateOf("plan") } // plan, details, result, suggestions

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp),
        contentPadding = PaddingValues(top = 16.dp, bottom = 96.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // --- 1. SYSTEM TELEMETRY DOCK ---
        item {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .border(BorderStroke(1.dp, GridLines), RoundedCornerShape(12.dp))
                    .background(SpaceNavy)
                    .padding(12.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                TelemetryCell(
                    label = "SYS HEALTH",
                    value = if (viewModel.isGenerating) "PROCESSING" else "OPERATIONAL",
                    color = if (viewModel.isGenerating) CyberOrange else CyberGreen
                )
                DividerVertical()
                TelemetryCell(
                    label = "DB LEVEL",
                    value = "${memoriesList.size + tasksList.size + transactionsList.size} LOAD",
                    color = CyberCyan
                )
                DividerVertical()
                TelemetryCell(
                    label = "SYNAPSES",
                    value = "${16} AGENTS",
                    color = CyberMagenta
                )
            }
        }

        // --- 2. TERMINAL QUERY DIRECTIVE ENTRY ---
        item {
            Card(
                modifier = Modifier.fillMaxWidth().testTag("terminal_card"),
                shape = RoundedCornerShape(16.dp),
                border = BorderStroke(1.dp, if (viewModel.isGenerating) CyberOrange else CyberCyan),
                colors = CardDefaults.cardColors(containerColor = SpaceNavy)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "NEXUS COMMAND PIPELINE",
                            style = MaterialTheme.typography.labelLarge,
                            color = CyberCyan
                        )
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(
                                text = "ROUTING:",
                                style = MaterialTheme.typography.labelSmall,
                                color = TextSecondary,
                                modifier = Modifier.padding(end = 4.dp)
                            )
                            Box {
                                var expanded by remember { mutableStateOf(false) }
                                Text(
                                    text = viewModel.selectedRouteAgent.uppercase(),
                                    style = MaterialTheme.typography.labelLarge,
                                    color = CyberMagenta,
                                    modifier = Modifier
                                        .clickable { expanded = true }
                                        .border(BorderStroke(1.dp, CyberMagenta), RoundedCornerShape(4.dp))
                                        .padding(horizontal = 6.dp, vertical = 2.dp)
                                )
                                DropdownMenu(
                                    expanded = expanded,
                                    onDismissRequest = { expanded = false },
                                    modifier = Modifier.background(SpaceNavy)
                                ) {
                                    val routes = listOf("Auto-Select", "STUDY AGENT", "RESEARCH AGENT", "MECH AGENT", "CODING AGENT", "FINANCE AGENT", "HACKATHON AGENT")
                                    routes.forEach { route ->
                                        DropdownMenuItem(
                                            text = { Text(route, color = TextPrimary, fontFamily = FontFamily.Monospace) },
                                            onClick = {
                                                viewModel.selectedRouteAgent = route
                                                expanded = false
                                            }
                                        )
                                    }
                                }
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    OutlinedTextField(
                        value = viewModel.masterGoalQuery,
                        onValueChange = { viewModel.masterGoalQuery = it },
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("terminal_input"),
                        placeholder = {
                            Text(
                                "Enter complex mechanical engineering goals, research papers, design ideas, code prompts...",
                                style = MaterialTheme.typography.bodyMedium,
                                color = TextMuted
                            )
                        },
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = CyberCyan,
                            unfocusedBorderColor = GridLines,
                            focusedContainerColor = CodeGray,
                            unfocusedContainerColor = CodeGray,
                            focusedTextColor = TextPrimary,
                            unfocusedTextColor = TextPrimary
                        ),
                        singleLine = false,
                        maxLines = 4,
                        shape = RoundedCornerShape(8.dp)
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Format: 🧠Master Plan 🤖Agent Work 📊Consolidated Result 💡Suggestions",
                            style = MaterialTheme.typography.labelSmall,
                            fontSize = 8.sp,
                            color = TextSecondary
                        )

                        Button(
                            onClick = { viewModel.submitMasterGoal() },
                            enabled = !viewModel.isGenerating,
                            colors = ButtonDefaults.buttonColors(
                                containerColor = if (viewModel.isGenerating) CyberOrange else CyberCyan
                            ),
                            shape = RoundedCornerShape(8.dp),
                            modifier = Modifier.testTag("submit_button")
                        ) {
                            if (viewModel.isGenerating) {
                                CircularProgressIndicator(
                                    modifier = Modifier.size(16.dp),
                                    color = ObsidianDark,
                                    strokeWidth = 2.dp
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                Text("SOLVING...", color = ObsidianDark, style = MaterialTheme.typography.labelLarge)
                            } else {
                                Text("DELEGATE", color = ObsidianDark, style = MaterialTheme.typography.labelLarge)
                                Spacer(modifier = Modifier.width(6.dp))
                                Icon(Icons.Default.ArrowForward, contentDescription = "delegate", tint = ObsidianDark, modifier = Modifier.size(16.dp))
                            }
                        }
                    }
                }
            }
        }

        // --- 3. LIVE MULTI-AGENT SOLUTION CONSOLE ---
        item {
            val log = viewModel.activePlanLog
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .border(BorderStroke(1.dp, GridLines), RoundedCornerShape(16.dp)),
                colors = CardDefaults.cardColors(containerColor = SpaceNavy)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = "ACTIVE PROTOCOL LOG",
                        style = MaterialTheme.typography.labelLarge,
                        color = CyberMagenta
                    )
                    Text(
                        text = "GOAL: \"${log.goal.uppercase()}\"",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = TextPrimary,
                        modifier = Modifier.padding(vertical = 4.dp)
                    )
                    Text(
                        text = "ROUTED ACTIONS VIA: ${log.activeAgents.uppercase()}",
                        style = MaterialTheme.typography.labelSmall,
                        color = TextSecondary,
                        fontSize = 9.sp
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    // Output Sector Tabs
                    ScrollableTabRow(
                        selectedTabIndex = when(activeOutputTab) {
                            "plan" -> 0
                            "details" -> 1
                            "result" -> 2
                            else -> 3
                        },
                        containerColor = CodeGray,
                        edgePadding = 0.dp,
                        indicator = @Composable { tabPositions ->
                            TabRowDefaults.SecondaryIndicator(
                                Modifier.tabIndicatorOffset(tabPositions[when(activeOutputTab) {
                                    "plan" -> 0
                                    "details" -> 1
                                    "result" -> 2
                                    else -> 3
                                }]),
                                color = CyberCyan
                            )
                        }
                    ) {
                        Tab(
                            selected = activeOutputTab == "plan",
                            onClick = { activeOutputTab = "plan" },
                            text = { Text("M-PLAN", style = MaterialTheme.typography.labelLarge, color = if (activeOutputTab == "plan") CyberCyan else TextSecondary) }
                        )
                        Tab(
                            selected = activeOutputTab == "details",
                            onClick = { activeOutputTab = "details" },
                            text = { Text("AGENT WORK", style = MaterialTheme.typography.labelLarge, color = if (activeOutputTab == "details") CyberCyan else TextSecondary) }
                        )
                        Tab(
                            selected = activeOutputTab == "result",
                            onClick = { activeOutputTab = "result" },
                            text = { Text("DELIVERABLE", style = MaterialTheme.typography.labelLarge, color = if (activeOutputTab == "result") CyberCyan else TextSecondary) }
                        )
                        Tab(
                            selected = activeOutputTab == "suggestions",
                            onClick = { activeOutputTab = "suggestions" },
                            text = { Text("SUGGESTIONS", style = MaterialTheme.typography.labelLarge, color = if (activeOutputTab == "suggestions") CyberCyan else TextSecondary) }
                        )
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    // Tactical content frame
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .heightIn(min = 180.dp)
                            .clip(RoundedCornerShape(8.dp))
                            .background(CodeGray)
                            .border(BorderStroke(1.dp, GridLines), RoundedCornerShape(8.dp))
                            .padding(12.dp)
                            .animateContentSize()
                    ) {
                        val activeContent = when (activeOutputTab) {
                            "plan" -> log.masterPlan
                            "details" -> log.agentWork
                            "result" -> log.finalResult
                            else -> log.suggestions
                        }

                        Text(
                            text = activeContent,
                            style = MaterialTheme.typography.bodyMedium,
                            color = TextPrimary,
                            fontFamily = if (activeOutputTab == "details" || activeOutputTab == "plan") FontFamily.Monospace else FontFamily.SansSerif,
                            lineHeight = 20.sp
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun TelemetryCell(label: String, value: String, color: Color) {
    Column(
        modifier = Modifier.padding(horizontal = 8.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = label,
            style = MaterialTheme.typography.labelSmall,
            color = TextSecondary,
            fontSize = 9.sp
        )
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = value,
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold,
            color = color,
            fontSize = 13.sp
        )
    }
}

@Composable
fun DividerVertical() {
    Box(
        modifier = Modifier
            .width(1.dp)
            .height(24.dp)
            .background(GridLines)
    )
}
