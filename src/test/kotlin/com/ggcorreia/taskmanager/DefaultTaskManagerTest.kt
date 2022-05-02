package com.ggcorreia.taskmanager

import com.ggcorreia.taskmanager.exception.IsAtFullCapacityException
import com.ggcorreia.taskmanager.factory.TaskManagerFactory
import com.ggcorreia.taskmanager.model.Process
import com.ggcorreia.taskmanager.model.TaskManager
import com.ggcorreia.taskmanager.model.TaskManagerType
import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.assertThatExceptionOfType
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

internal class DefaultTaskManagerTest {
    private lateinit var taskManager: TaskManager

    @BeforeEach
    internal fun setup() {
        taskManager = TaskManagerFactory.createTaskManager(TaskManagerType.DefaultTaskManagerType, MAX_CAPACITY)
    }

    @Test
    internal fun `should add a process when max capacity not reached`() {
        taskManager.add(Process(pid = 1, taskManager = taskManager))
        assertThat(taskManager.listProcesses().size).isEqualTo(1)
    }

    @Test
    internal fun `should not add more process when max capacity is reached`() {
        repeat(MAX_CAPACITY) {
            taskManager.add(Process(pid = it + 1, taskManager = taskManager))
        }
        assertThatExceptionOfType(IsAtFullCapacityException::class.java)
            .isThrownBy {
                taskManager.add(Process(pid = MAX_CAPACITY + 1, taskManager = taskManager))
            }
        assertThat(taskManager.listProcesses().size).isEqualTo(MAX_CAPACITY)
    }
}