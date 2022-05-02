package com.ggcorreia.taskmanager.model

import com.ggcorreia.taskmanager.exception.IsAtFullCapacityException

class DefaultTaskManager(private val maxCapacity: Int) : TaskManager(maxCapacity) {
    override fun add(process: Process): Boolean {
        validateProcessUniqueness(process.pid)
        if (isAtFullCapacity()) {
            throw IsAtFullCapacityException("Max Capacity Reached [$maxCapacity]. Cannot add more processes!")
        }
        processes.add(process)
        return true
    }
}