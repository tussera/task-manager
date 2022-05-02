package com.ggcorreia.taskmanager.model

class FifoTaskManager(maxCapacity: Int) : TaskManager(maxCapacity) {
    override fun add(process: Process): Boolean {
        validateProcessUniqueness(process.pid)
        if (isAtFullCapacity()) {
            processes.getFirstIn()?.kill()
        }
        processes.add(process)
        return true
    }

    private fun List<Process>.getFirstIn(): Process? = this.minByOrNull { it.creationDate }
}