package com.ggcorreia.taskmanager.model

import com.ggcorreia.taskmanager.exception.IsAtFullCapacityAndHigherPrioritiesException
import com.ggcorreia.taskmanager.model.Priority.HIGH
import com.ggcorreia.taskmanager.model.Priority.LOW
import com.ggcorreia.taskmanager.model.Priority.MEDIUM

class PriorityTaskManager(maxCapacity: Int) : TaskManager(maxCapacity) {
    override fun add(process: Process): Boolean {
        validateProcessUniqueness(process.pid)
        if (isAtFullCapacity()) {
            checkAvailability(process.priority)
        }
        processes.add(process)
        return true
    }

    private fun checkAvailability(priority: Priority) =
        when (priority) {
            LOW -> null
            MEDIUM -> processes
                .filter { it.priority == LOW }
                .minByOrNull { it.creationDate }
                ?.kill()
            HIGH -> processes
                .filter { it.priority in listOf(LOW, MEDIUM) }
                .minByOrNull { it.creationDate }
                ?.kill()
        } ?: throw IsAtFullCapacityAndHigherPrioritiesException(
            "Full Capacity reached but no lower priority to be replaced!"
        )
}