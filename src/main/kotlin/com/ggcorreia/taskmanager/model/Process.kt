package com.ggcorreia.taskmanager.model

import java.time.LocalDateTime

data class Process(
    val pid: Int,
    val priority: Priority = Priority.LOW,
    val creationDate: LocalDateTime = LocalDateTime.now(),
    val taskManager: TaskManager
) {
    fun kill() {
        taskManager.removeProcessFromList(this)
    }
}

enum class Priority {
    LOW, MEDIUM, HIGH
}