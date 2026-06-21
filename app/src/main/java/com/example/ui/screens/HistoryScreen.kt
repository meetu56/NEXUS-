package com.example.ui.screens

import androidx.compose.animation.*
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
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
fun HistoryScreen(viewModel: NexusViewModel) {
    val logsList by viewModel.agentLogs.collectAsState()
    var selectedLogToView by remember { mutableStateOf<AgentLogEntity?>(null) }
    var activeSubLogSection by remember { mutableStateOf("plan") } // plan, details, result

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Spacer(modifier = Modifier.height(12.dp))

        if (selectedLogToView == null) {
            // --- MAIN LIST OF AGENT DIRECTIVES HISTORY ---
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text("ARCHIVAL CONSOLE HISTORY", style = MaterialTheme.typography.labelLarge, color = CyberCyan)
                    Text("PAST WORKFLOWS AND RESULTS", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                }

                if (logsList.isNotEmpty()) {
                    IconButton(
                        onClick = { viewModel.resetSystemLogs() },
                        modifier = Modifier.testTag("reset_history_button")
                    ) {
                        Icon(Icons.Default.Delete, contentDescription = "reset logs", tint = CyberOrange)
                    }
                }
            }

            if (logsList.isEmpty()) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .weight(1f)
                        .border(BorderStroke(1.dp, GridLines), RoundedCornerShape(12.dp))
                        .background(SpaceNavy),
                    contentAlignment = Alignment.Center
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.spacedBy(8.dp)) {
                        Icon(Icons.Default.History, contentDescription = "empty", tint = TextSecondary, modifier = Modifier.size(48.dp))
                        Text(
                            "NO PREVIOUS DIRECTIVES DETECTED",
                            style = MaterialTheme.typography.labelSmall,
                            color = TextSecondary
                        )
                    }
                }
            } else {
                LazyColumn(
                    modifier = Modifier.fillMaxSize().weight(1f).testTag("history_list"),
                    verticalArrangement = Arrangement.spacedBy(10.dp),
                    contentPadding = PaddingValues(bottom = 96.dp)
                ) {
                    items(logsList) { log ->
                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable { selectedLogToView = log },
                            colors = CardDefaults.cardColors(containerColor = SpaceNavy),
                            border = BorderStroke(1.dp, GridLines)
                        ) {
                            Column(modifier = Modifier.padding(14.dp)) {
                                Text(
                                    text = "GOAL: ${log.goal.uppercase()}",
                                    style = MaterialTheme.typography.bodyLarge,
                                    fontWeight = FontWeight.Bold,
                                    color = TextPrimary,
                                    maxLines = 1
                                )
                                Spacer(modifier = Modifier.height(4.dp))
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween
                                ) {
                                    Text(
                                        text = "AGENTS: ${log.activeAgents.uppercase()}",
                                        style = MaterialTheme.typography.labelSmall,
                                        color = CyberCyan,
                                        fontSize = 9.sp
                                    )
                                    Text(
                                        text = "CLICK TO VIEW DETAIL",
                                        style = MaterialTheme.typography.labelSmall,
                                        color = CyberMagenta,
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 9.sp
                                    )
                                }
                            }
                        }
                    }
                }
            }
        } else {
            // --- DETAILED VIEW OF SPECIFIC SELECTED ARCHIVAL RUN ---
            val activeLog = selectedLogToView!!

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .weight(1f)
                    .clip(RoundedCornerShape(16.dp))
                    .border(BorderStroke(1.dp, CyberCyan), RoundedCornerShape(16.dp))
                    .background(SpaceNavy)
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                // Back bar
                Text(
                    text = "◀ RETURN TO HISTORIES DIRECTORY",
                    style = MaterialTheme.typography.labelLarge,
                    color = CyberCyan,
                    modifier = Modifier
                        .clickable { selectedLogToView = null }
                        .padding(vertical = 4.dp)
                )

                Text(
                    text = "DIRECTIVE DETAILS",
                    style = MaterialTheme.typography.labelSmall,
                    color = TextSecondary
                )

                Text(
                    text = activeLog.goal.uppercase(),
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold,
                    color = TextPrimary
                )

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .border(BorderStroke(1.dp, GridLines), RoundedCornerShape(8.dp))
                        .background(CodeGray)
                        .padding(2.dp),
                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    val subTabs = listOf("plan" to "MASTER PLAN", "details" to "AGENT WORK", "result" to "RESULT")
                    subTabs.forEach { (key, label) ->
                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .clip(RoundedCornerShape(6.dp))
                                .background(if (activeSubLogSection == key) SpaceNavy else Color.Transparent)
                                .border(BorderStroke(1.dp, if (activeSubLogSection == key) CyberCyan else Color.Transparent), RoundedCornerShape(6.dp))
                                .clickable { activeSubLogSection = key }
                                .padding(vertical = 8.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = label,
                                style = MaterialTheme.typography.labelSmall,
                                fontSize = 8.sp,
                                color = if (activeSubLogSection == key) CyberCyan else TextSecondary
                            )
                        }
                    }
                }

                // Scrollable details frame
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f)
                        .clip(RoundedCornerShape(8.dp))
                        .background(CodeGray)
                        .border(BorderStroke(1.dp, GridLines), RoundedCornerShape(8.dp))
                        .padding(12.dp)
                ) {
                    val contentToShow = when(activeSubLogSection) {
                        "plan" -> activeLog.masterPlan
                        "details" -> activeLog.agentWork
                        else -> activeLog.finalResult
                    }
                    LazyColumn(modifier = Modifier.fillMaxSize()) {
                        item {
                            Text(
                                text = contentToShow,
                                style = MaterialTheme.typography.bodyMedium,
                                color = TextPrimary,
                                fontFamily = if (activeSubLogSection == "details") FontFamily.Monospace else FontFamily.SansSerif,
                                lineHeight = 18.sp
                            )
                        }
                    }
                }
            }
        }
        
        Spacer(modifier = Modifier.height(48.dp))
    }
}
