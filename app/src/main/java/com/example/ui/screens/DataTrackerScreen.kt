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
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.FinanceEntity
import com.example.data.MemoryEntity
import com.example.data.TaskEntity
import com.example.ui.NexusViewModel
import com.example.ui.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DataTrackerScreen(viewModel: NexusViewModel) {
    var activeSubTab by remember { mutableStateOf("productivity") } // productivity, memory, finance

    val tasksList by viewModel.tasks.collectAsState()
    val memoriesList by viewModel.memories.collectAsState()
    val transactionsList by viewModel.transactions.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Spacer(modifier = Modifier.height(12.dp))

        // Sub tab Row
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .border(BorderStroke(1.dp, GridLines), RoundedCornerShape(8.dp))
                .background(CodeGray)
                .padding(4.dp),
            horizontalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            SubTabButton(
                title = "PRODUCTIVITY AI",
                isActive = activeSubTab == "productivity",
                onClick = { activeSubTab = "productivity" },
                modifier = Modifier.weight(1f).testTag("tab_productivity")
            )
            SubTabButton(
                title = "MEMORY AI",
                isActive = activeSubTab == "memory",
                onClick = { activeSubTab = "memory" },
                modifier = Modifier.weight(1f).testTag("tab_memory")
            )
            SubTabButton(
                title = "FINANCE AI",
                isActive = activeSubTab == "finance",
                onClick = { activeSubTab = "finance" },
                modifier = Modifier.weight(1f).testTag("tab_finance")
            )
        }

        // Expanded content based on sub-tabs
        AnimatedContent(
            targetState = activeSubTab,
            label = "tracker_animation",
            modifier = Modifier.weight(1f)
        ) { subTab ->
            when (subTab) {
                "productivity" -> ProductivityWorkspace(tasksList, viewModel)
                "memory" -> MemoryWorkspace(memoriesList, viewModel)
                "finance" -> FinanceWorkspace(transactionsList, viewModel)
            }
        }
    }
}

@Composable
fun SubTabButton(title: String, isActive: Boolean, onClick: () -> Unit, modifier: Modifier) {
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(6.dp))
            .background(if (isActive) SpaceNavy else Color.Transparent)
            .border(BorderStroke(1.dp, if (isActive) CyberCyan else Color.Transparent), RoundedCornerShape(6.dp))
            .clickable { onClick() }
            .padding(vertical = 10.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = title,
            style = MaterialTheme.typography.labelSmall,
            fontWeight = FontWeight.Bold,
            color = if (isActive) CyberCyan else TextSecondary,
            fontSize = 9.sp
        )
    }
}

// ==========================================
// 1. PRODUCTIVITY WORKSPACE (TASKS)
// ==========================================
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProductivityWorkspace(tasks: List<TaskEntity>, viewModel: NexusViewModel) {
    var taskTitle by remember { mutableStateOf("") }
    var taskDetails by remember { mutableStateOf("") }
    var taskDeadline by remember { mutableStateOf("") }
    var selectedPriority by remember { mutableStateOf("MEDIUM") }
    var selectedCategory by remember { mutableStateOf("TODO") }
    var isFormVisible by remember { mutableStateOf(false) }

    Column(modifier = Modifier.fillMaxSize(), verticalArrangement = Arrangement.spacedBy(12.dp)) {
        // Toggle Form
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text("ENGINEERING TASKS BOARD", style = MaterialTheme.typography.labelLarge, color = CyberCyan)
            Text(
                text = "${if(isFormVisible) "CANCEL" else "ADD NEW"}",
                style = MaterialTheme.typography.labelSmall,
                color = if(isFormVisible) CyberOrange else CyberCyan,
                modifier = Modifier
                    .clickable { isFormVisible = !isFormVisible }
                    .border(BorderStroke(1.dp, if(isFormVisible) CyberOrange else CyberCyan), RoundedCornerShape(4.dp))
                    .padding(horizontal = 8.dp, vertical = 4.dp)
            )
        }

        if (isFormVisible) {
            Card(
                colors = CardDefaults.cardColors(containerColor = SpaceNavy),
                border = BorderStroke(1.dp, GridLines),
                modifier = Modifier.fillMaxWidth().animateContentSize()
            ) {
                Column(modifier = Modifier.padding(12.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Text("CREATE NEW DIRECTIVE", style = MaterialTheme.typography.labelSmall, color = CyberCyan)
                    
                    OutlinedTextField(
                        value = taskTitle,
                        onValueChange = { taskTitle = it },
                        placeholder = { Text("Task heading (e.g. Solve GTU Thermodynamics notes)", color = TextMuted) },
                        modifier = Modifier.fillMaxWidth().testTag("add_task_title"),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = CyberCyan,
                            unfocusedBorderColor = GridLines,
                            focusedContainerColor = CodeGray,
                            unfocusedContainerColor = CodeGray,
                            focusedTextColor = TextPrimary,
                            unfocusedTextColor = TextPrimary
                        )
                    )

                    OutlinedTextField(
                        value = taskDetails,
                        onValueChange = { taskDetails = it },
                        placeholder = { Text("Task description (parameters, sizing limits)", color = TextMuted) },
                        modifier = Modifier.fillMaxWidth().testTag("add_task_details"),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = CyberCyan,
                            unfocusedBorderColor = GridLines,
                            focusedContainerColor = CodeGray,
                            unfocusedContainerColor = CodeGray,
                            focusedTextColor = TextPrimary,
                            unfocusedTextColor = TextPrimary
                        )
                    )

                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        OutlinedTextField(
                            value = taskDeadline,
                            onValueChange = { taskDeadline = it },
                            placeholder = { Text("Deadline (e.g. 26 Jun)", color = TextMuted) },
                            modifier = Modifier.weight(1f).testTag("add_task_deadline"),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = CyberCyan,
                                unfocusedBorderColor = GridLines,
                                focusedContainerColor = CodeGray,
                                unfocusedContainerColor = CodeGray,
                                focusedTextColor = TextPrimary,
                                unfocusedTextColor = TextPrimary
                            )
                        )

                        // Priority Row
                        Box(modifier = Modifier.weight(1f).align(Alignment.CenterVertically)) {
                            var exp by remember { mutableStateOf(false) }
                            Text(
                                "PRIORITY: ${selectedPriority}",
                                style = MaterialTheme.typography.labelSmall,
                                color = if(selectedPriority == "HIGH") CyberOrange else CyberCyan,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clickable { exp = true }
                                    .border(BorderStroke(1.dp, GridLines), RoundedCornerShape(4.dp))
                                    .padding(vertical = 12.dp),
                                fontWeight = FontWeight.Bold
                            )
                            DropdownMenu(expanded = exp, onDismissRequest = { exp = false }) {
                                listOf("LOW", "MEDIUM", "HIGH").forEach { p ->
                                    DropdownMenuItem(text = { Text(p) }, onClick = { selectedPriority = p; exp = false })
                                }
                            }
                        }
                    }

                    Button(
                        onClick = {
                            if (taskTitle.isNotBlank()) {
                                viewModel.addTask(taskTitle, taskDetails, taskDeadline, selectedPriority, selectedCategory)
                                taskTitle = ""
                                taskDetails = ""
                                taskDeadline = ""
                                isFormVisible = false
                            }
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = CyberCyan),
                        modifier = Modifier.fillMaxWidth().testTag("add_task_submit"),
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        Text("SAVE TASKS BOARD", color = ObsidianDark)
                    }
                }
            }
        }

        // Task List
        if (tasks.isEmpty()) {
            Box(modifier = Modifier.fillMaxSize().weight(1f), contentAlignment = Alignment.Center) {
                Text("NO DIRECTIVES ACTIVE. STANDBY FOR TASK SEQUENCE.", style = MaterialTheme.typography.bodyMedium, color = TextSecondary)
            }
        } else {
            LazyColumn(
                modifier = Modifier.fillMaxSize().weight(1f).testTag("tasks_list"),
                verticalArrangement = Arrangement.spacedBy(8.dp),
                contentPadding = PaddingValues(bottom = 96.dp)
            ) {
                items(tasks) { t ->
                    Card(
                        colors = CardDefaults.cardColors(containerColor = SpaceNavy),
                        border = BorderStroke(1.dp, GridLines),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Row(
                            modifier = Modifier.padding(12.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Row(modifier = Modifier.weight(1f), verticalAlignment = Alignment.CenterVertically) {
                                Checkbox(
                                    checked = t.isCompleted,
                                    onCheckedChange = { viewModel.toggleTaskCompletion(t) },
                                    colors = CheckboxDefaults.colors(checkedColor = CyberCyan, checkmarkColor = ObsidianDark)
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                Column {
                                    Text(
                                        text = t.title,
                                        style = MaterialTheme.typography.bodyLarge,
                                        fontWeight = FontWeight.Bold,
                                        textDecoration = if (t.isCompleted) TextDecoration.LineThrough else TextDecoration.None,
                                        color = if (t.isCompleted) TextMuted else TextPrimary
                                    )
                                    if (t.details.isNotEmpty()) {
                                        Text(
                                            text = t.details,
                                            style = MaterialTheme.typography.bodyMedium,
                                            color = TextSecondary,
                                            fontSize = 11.sp
                                        )
                                    }
                                    if (t.deadline.isNotEmpty()) {
                                        Text(
                                            text = "DEADLINE: ${t.deadline.uppercase()}",
                                            style = MaterialTheme.typography.labelSmall,
                                            fontSize = 9.sp,
                                            color = if (t.priority == "HIGH" && !t.isCompleted) CyberOrange else CyberCyan
                                        )
                                    }
                                }
                            }

                            IconButton(onClick = { viewModel.removeTask(t.id) }) {
                                Icon(Icons.Default.Delete, contentDescription = "delete", tint = CyberOrange)
                            }
                        }
                    }
                }
            }
        }
    }
}

// ==========================================
// 2. MEMORY WORKSPACE (CITATIONS / PREFERENCES)
// ==========================================
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MemoryWorkspace(memories: List<MemoryEntity>, viewModel: NexusViewModel) {
    var memKey by remember { mutableStateOf("") }
    var memValue by remember { mutableStateOf("") }
    var isFormVisible by remember { mutableStateOf(false) }

    Column(modifier = Modifier.fillMaxSize(), verticalArrangement = Arrangement.spacedBy(12.dp)) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text("MEMORY AI REGISTRY", style = MaterialTheme.typography.labelLarge, color = CyberCyan)
            Text(
                text = "${if(isFormVisible) "CANCEL" else "WRITE"}",
                style = MaterialTheme.typography.labelSmall,
                color = if(isFormVisible) CyberOrange else CyberCyan,
                modifier = Modifier
                    .clickable { isFormVisible = !isFormVisible }
                    .border(BorderStroke(1.dp, if(isFormVisible) CyberOrange else CyberCyan), RoundedCornerShape(4.dp))
                    .padding(horizontal = 8.dp, vertical = 4.dp)
            )
        }

        if (isFormVisible) {
            Card(
                colors = CardDefaults.cardColors(containerColor = SpaceNavy),
                border = BorderStroke(1.dp, GridLines),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(12.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Text("COMMIT DATA FOR MULTI-AGENTS", style = MaterialTheme.typography.labelSmall, color = CyberCyan)
                    
                    OutlinedTextField(
                        value = memKey,
                        onValueChange = { memKey = it },
                        placeholder = { Text("Subject key (e.g. GTU Syllabus, CAD Dimension)", color = TextMuted) },
                        modifier = Modifier.fillMaxWidth().testTag("add_memory_key"),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = CyberCyan,
                            unfocusedBorderColor = GridLines,
                            focusedContainerColor = CodeGray,
                            unfocusedContainerColor = CodeGray,
                            focusedTextColor = TextPrimary,
                            unfocusedTextColor = TextPrimary
                        )
                    )

                    OutlinedTextField(
                        value = memValue,
                        onValueChange = { memValue = it },
                        placeholder = { Text("Details (values, preferences, formulas...)", color = TextMuted) },
                        modifier = Modifier.fillMaxWidth().testTag("add_memory_value"),
                        maxLines = 4,
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = CyberCyan,
                            unfocusedBorderColor = GridLines,
                            focusedContainerColor = CodeGray,
                            unfocusedContainerColor = CodeGray,
                            focusedTextColor = TextPrimary,
                            unfocusedTextColor = TextPrimary
                        )
                    )

                    Button(
                        onClick = {
                            if (memKey.isNotBlank() && memValue.isNotBlank()) {
                                viewModel.addMemory(memKey, memValue)
                                memKey = ""
                                memValue = ""
                                isFormVisible = false
                            }
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = CyberCyan),
                        modifier = Modifier.fillMaxWidth().testTag("add_memory_submit"),
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        Text("COMMIT TO REGISTRY", color = ObsidianDark)
                    }
                }
            }
        }

        if (memories.isEmpty()) {
            Box(modifier = Modifier.fillMaxSize().weight(1f), contentAlignment = Alignment.Center) {
                Text("MEMORY STACK CLEAR. READY FOR LONG-TERM PREFERENCES.", style = MaterialTheme.typography.bodyMedium, color = TextSecondary)
            }
        } else {
            LazyColumn(
                modifier = Modifier.fillMaxSize().weight(1f).testTag("memories_list"),
                verticalArrangement = Arrangement.spacedBy(8.dp),
                contentPadding = PaddingValues(bottom = 96.dp)
            ) {
                items(memories) { memo ->
                    Card(
                        colors = CardDefaults.cardColors(containerColor = SpaceNavy),
                        border = BorderStroke(1.dp, GridLines),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column(modifier = Modifier.padding(12.dp)) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = memo.key.uppercase(),
                                    style = MaterialTheme.typography.labelLarge,
                                    color = CyberCyan,
                                    fontWeight = FontWeight.Bold
                                )
                                IconButton(onClick = { viewModel.removeMemory(memo.id) }) {
                                    Icon(Icons.Default.Delete, contentDescription = "delete", tint = CyberOrange, modifier = Modifier.size(18.dp))
                                }
                            }
                            Spacer(modifier = Modifier.height(6.dp))
                            Text(
                                text = memo.content,
                                style = MaterialTheme.typography.bodyMedium,
                                color = TextPrimary,
                                fontFamily = FontFamily.Monospace,
                                fontSize = 11.sp,
                                lineHeight = 16.sp
                            )
                        }
                    }
                }
            }
        }
    }
}

// ==========================================
// 3. FINANCE WORKSPACE (LEDGER BUDGET)
// ==========================================
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FinanceWorkspace(transactions: List<FinanceEntity>, viewModel: NexusViewModel) {
    var title by remember { mutableStateOf("") }
    var amountText by remember { mutableStateOf("") }
    var isExpense by remember { mutableStateOf(true) }
    var category by remember { mutableStateOf("RESEARCH") }
    var isFormVisible by remember { mutableStateOf(false) }

    // Net budget computations
    val totalIn = transactions.filter { !it.isExpense }.sumOf { it.amount }
    val totalOut = transactions.filter { it.isExpense }.sumOf { it.amount }
    val currentBalance = totalIn - totalOut

    Column(modifier = Modifier.fillMaxSize(), verticalArrangement = Arrangement.spacedBy(12.dp)) {
        // Budget Metrics Header
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .border(BorderStroke(1.dp, GridLines), RoundedCornerShape(12.dp))
                .background(SpaceNavy)
                .padding(12.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column {
                Text("SECURE FUND BALANCE", style = MaterialTheme.typography.labelSmall, color = TextSecondary)
                Text(
                    text = "${if(currentBalance >= 0) "+" else "-"}$${String.format("%.2f", kotlin.math.abs(currentBalance))}",
                    style = MaterialTheme.typography.titleLarge,
                    color = if (currentBalance >= 0) CyberGreen else CyberOrange,
                    fontWeight = FontWeight.Bold
                )
            }
            Column(horizontalAlignment = Alignment.End) {
                Text("TOTAL OUT: $${String.format("%.2f", totalOut)}", style = MaterialTheme.typography.labelSmall, color = CyberOrange)
                Text("TOTAL IN: $${String.format("%.2f", totalIn)}", style = MaterialTheme.typography.labelSmall, color = CyberGreen)
            }
        }

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text("FINANCE LEDGER LOGS", style = MaterialTheme.typography.labelLarge, color = CyberCyan)
            Text(
                text = "${if(isFormVisible) "CANCEL" else "LOG DEAL"}",
                style = MaterialTheme.typography.labelSmall,
                color = if(isFormVisible) CyberOrange else CyberCyan,
                modifier = Modifier
                    .clickable { isFormVisible = !isFormVisible }
                    .border(BorderStroke(1.dp, if(isFormVisible) CyberOrange else CyberCyan), RoundedCornerShape(4.dp))
                    .padding(horizontal = 8.dp, vertical = 4.dp)
            )
        }

        if (isFormVisible) {
            Card(
                colors = CardDefaults.cardColors(containerColor = SpaceNavy),
                border = BorderStroke(1.dp, GridLines),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(12.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Text("ADD COMPONENT BUDGET OR GRANT", style = MaterialTheme.typography.labelSmall, color = CyberCyan)

                    OutlinedTextField(
                        value = title,
                        onValueChange = { title = it },
                        placeholder = { Text("Transaction details (e.g. Solar panels, ISHRAE grant)", color = TextMuted) },
                        modifier = Modifier.fillMaxWidth().testTag("add_finance_title"),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = CyberCyan,
                            unfocusedBorderColor = GridLines,
                            focusedContainerColor = CodeGray,
                            unfocusedContainerColor = CodeGray,
                            focusedTextColor = TextPrimary,
                            unfocusedTextColor = TextPrimary
                        )
                    )

                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp), verticalAlignment = Alignment.CenterVertically) {
                        OutlinedTextField(
                            value = amountText,
                            onValueChange = { amountText = it },
                            placeholder = { Text("Amount ($)", color = TextMuted) },
                            modifier = Modifier.weight(1f).testTag("add_finance_amount"),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = CyberCyan,
                                unfocusedBorderColor = GridLines,
                                focusedContainerColor = CodeGray,
                                unfocusedContainerColor = CodeGray,
                                focusedTextColor = TextPrimary,
                                unfocusedTextColor = TextPrimary
                            )
                        )

                        // Expense/Income toggle
                        Button(
                            onClick = { isExpense = !isExpense },
                            colors = ButtonDefaults.buttonColors(containerColor = if (isExpense) CyberOrange else CyberGreen),
                            shape = RoundedCornerShape(8.dp),
                            modifier = Modifier.weight(1f).testTag("add_finance_type")
                        ) {
                            Text(
                                text = if (isExpense) "EXPENSE ‣" else "INCOME ‣",
                                color = ObsidianDark,
                                style = MaterialTheme.typography.labelSmall
                            )
                        }
                    }

                    // Category Selector Row
                    Box(modifier = Modifier.fillMaxWidth()) {
                        var exp by remember { mutableStateOf(false) }
                        Text(
                            "FIELD CATEGORY: $category",
                            style = MaterialTheme.typography.labelSmall,
                            color = CyberMagenta,
                            modifier = Modifier
                                .clickable { exp = true }
                                .border(BorderStroke(1.dp, GridLines), RoundedCornerShape(4.dp))
                                .padding(12.dp)
                                .fillMaxWidth(),
                            fontWeight = FontWeight.Bold
                        )
                        DropdownMenu(expanded = exp, onDismissRequest = { exp = false }) {
                            listOf("RESEARCH", "HARDWARE", "SCHOLARSHIP", "COMPETITION", "OTHER").forEach { c ->
                                DropdownMenuItem(text = { Text(c) }, onClick = { category = c; exp = false })
                            }
                        }
                    }

                    Button(
                        onClick = {
                            val amt = amountText.toDoubleOrNull() ?: 0.0
                            if (title.isNotBlank() && amt > 0) {
                                viewModel.addTransaction(title, amt, isExpense, category)
                                title = ""
                                amountText = ""
                                isFormVisible = false
                            }
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = CyberCyan),
                        modifier = Modifier.fillMaxWidth().testTag("add_finance_submit"),
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        Text("LOG TRANSACTION", color = ObsidianDark)
                    }
                }
            }
        }

        if (transactions.isEmpty()) {
            Box(modifier = Modifier.fillMaxSize().weight(1f), contentAlignment = Alignment.Center) {
                Text("LEDGER EMPTY. START LOGGING SECURE FLOWS.", style = MaterialTheme.typography.bodyMedium, color = TextSecondary)
            }
        } else {
            LazyColumn(
                modifier = Modifier.fillMaxSize().weight(1f).testTag("finance_list"),
                verticalArrangement = Arrangement.spacedBy(8.dp),
                contentPadding = PaddingValues(bottom = 96.dp)
            ) {
                items(transactions) { f ->
                    Card(
                        colors = CardDefaults.cardColors(containerColor = SpaceNavy),
                        border = BorderStroke(1.dp, GridLines),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Row(
                            modifier = Modifier.padding(12.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column {
                                Text(
                                    text = f.title,
                                    style = MaterialTheme.typography.bodyMedium,
                                    fontWeight = FontWeight.Bold,
                                    color = TextPrimary
                                )
                                Text(
                                    text = "${f.category.uppercase()} • ${if (f.isExpense) "DEBIT" else "CREDIT"}",
                                    style = MaterialTheme.typography.labelSmall,
                                    fontSize = 9.sp,
                                    color = TextSecondary
                                )
                            }
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Text(
                                    text = "${if (f.isExpense) "-" else "+"}$${String.format("%.2f", f.amount)}",
                                    style = MaterialTheme.typography.titleMedium,
                                    color = if (f.isExpense) CyberOrange else CyberGreen,
                                    fontWeight = FontWeight.Bold,
                                    modifier = Modifier.padding(end = 12.dp)
                                )
                                IconButton(onClick = { viewModel.removeTransaction(f.id) }) {
                                    Icon(Icons.Default.Delete, contentDescription = "delete", tint = CyberOrange, modifier = Modifier.size(18.dp))
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
