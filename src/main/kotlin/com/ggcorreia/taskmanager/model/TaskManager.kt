package com.ggcorreia.taskmanager.model

import com.ggcorreia.taskmanager.exception.ProcessUniquenessException

abstract class TaskManager(private val maxCapacity: Int) {
    protected val processes = mutableListOf<Process>()

    init {
        require(maxCapacity > 0) { "Max Capacity should be greater than 0!" }
    }

    abstract fun add(process: Process): Boolean

    fun isAtFullCapacity() = processes.count() >= maxCapacity

    fun validateProcessUniqueness(pid: Int) =
        getProcessById(pid)?.let {
            throw ProcessUniquenessException("Process with pid[$pid] already exists!")
        }

    fun listProcesses(): List<Process> {
        return processes
    }

    fun listProcesses(sortBy: SortBy): List<Process> {
        val result = processes
        when(sortBy) {
            SortBy.CreationDateASC -> result.sortBy { it.creationDate }
            SortBy.CreationDateDESC -> result.sortByDescending { it.creationDate }
            SortBy.PriorityASC -> result.sortBy { it.priority.ordinal }
            SortBy.PriorityDESC -> result.sortByDescending { it.priority.ordinal }
            SortBy.IdASC -> result.sortBy { it.pid }
            SortBy.IdDESC -> result.sortByDescending { it.pid }
        }
        return result;
    }

    fun removeProcessFromList(process: Process) = processes.remove(process)

    fun kill(pid: Int) = getProcessById(pid)
        ?.kill()
        ?: throw IllegalStateException()

    fun killGroup(priority: Priority) = processes
        .filter { it.priority == priority }
        .forEach(Process::kill)

    fun killAll() {
        // Copy the original list to avoid ConcurrentModificationException when the process tries to kill itself
        val proc = processes.toMutableList()
        proc.forEach(Process::kill)
    }

    private fun getProcessById(pid: Int) = processes.firstOrNull { it.pid == pid }
}

sealed class TaskManagerType {
    object DefaultTaskManagerType : TaskManagerType()
    object FifoTaskManagerType : TaskManagerType()
    object PriorityTaskManagerType : TaskManagerType()
}

sealed class SortBy {
    object CreationDateASC : SortBy()
    object CreationDateDESC : SortBy()
    object PriorityASC : SortBy()
    object PriorityDESC : SortBy()
    object IdASC : SortBy()
    object IdDESC : SortBy()
}