package com.ggcorreia.taskmanager

import com.ggcorreia.taskmanager.factory.TaskManagerFactory
import com.ggcorreia.taskmanager.model.Process
import com.ggcorreia.taskmanager.model.TaskManager
import com.ggcorreia.taskmanager.model.TaskManagerType
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

internal class FifoTaskManagerTest {
    private lateinit var taskManager: TaskManager

    @BeforeEach
    internal fun setup() {
        taskManager = TaskManagerFactory.createTaskManager(TaskManagerType.FifoTaskManagerType, MAX_CAPACITY)
    }

    @Test
    internal fun `should add a process when max capacity not reached`() {
        taskManager.add(Process(pid = 1, taskManager = taskManager))
        assertThat(taskManager.listProcesses().size).isEqualTo(1)
    }

    @Test
    internal fun `should remove first in process when adding new at full capacity`() {
        val firstInProcess = Process(pid = 1, taskManager = taskManager)
        taskManager.add(firstInProcess)
        repeat(MAX_CAPACITY-1) {
            taskManager.add(Process(pid = it + 2, taskManager = taskManager))
        }
        taskManager.add(Process(pid = MAX_CAPACITY + 1, taskManager = taskManager))
        val processes = taskManager.listProcesses()
        assertThat(processes.size).isEqualTo(MAX_CAPACITY)
        assertThat(processes)
            .noneMatch { process -> process == firstInProcess }
    }
}