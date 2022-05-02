package com.ggcorreia.taskmanager

import com.ggcorreia.taskmanager.exception.IsAtFullCapacityAndHigherPrioritiesException
import com.ggcorreia.taskmanager.factory.TaskManagerFactory
import com.ggcorreia.taskmanager.model.Priority
import com.ggcorreia.taskmanager.model.Process
import com.ggcorreia.taskmanager.model.TaskManager
import com.ggcorreia.taskmanager.model.TaskManagerType
import org.assertj.core.api.Assertions
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

internal class PriorityTaskManagerTest {
    private lateinit var taskManager: TaskManager

    @BeforeEach
    internal fun setup() {
        taskManager = TaskManagerFactory.createTaskManager(TaskManagerType.PriorityTaskManagerType, MAX_CAPACITY)
    }

    @Test
    internal fun `should add a process when max capacity not reached`() {
        taskManager.add(Process(pid = 1, taskManager = taskManager))
        assertThat(taskManager.listProcesses().size).isEqualTo(1)
    }

    @Test
    internal fun `should remove the oldest lowest priority when adding new at full capacity`() {
        val oldestLowestPriority = Process(pid = 1, taskManager = taskManager)
        taskManager.add(oldestLowestPriority)
        val lowPriorityButNewer = Process(pid = 2, taskManager = taskManager)
        taskManager.add(lowPriorityButNewer)
        repeat(MAX_CAPACITY-2) {
            taskManager.add(Process(pid = it + 3, priority = Priority.HIGH, taskManager = taskManager))
        }
        taskManager.add(Process(pid = MAX_CAPACITY + 1, priority = Priority.HIGH, taskManager = taskManager))
        val processes = taskManager.listProcesses()
        assertThat(processes.size).isEqualTo(MAX_CAPACITY)
        assertThat(processes)
            .noneMatch { process -> process == oldestLowestPriority }
    }

    @Test
    internal fun `should not add at full capacity if the priority is LOW`() {
        repeat(MAX_CAPACITY) {
            taskManager.add(Process(pid = it + 1, taskManager = taskManager))
        }
        Assertions.assertThatExceptionOfType(IsAtFullCapacityAndHigherPrioritiesException::class.java)
            .isThrownBy {
                taskManager.add(Process(pid = MAX_CAPACITY + 1, taskManager = taskManager))
            }
        assertThat(taskManager.listProcesses().size).isEqualTo(MAX_CAPACITY)
    }

    @Test
    internal fun `should not add at full capacity if the priority is MEDIUM and no process with LOW priority`() {
        repeat(MAX_CAPACITY) {
            taskManager.add(Process(pid = it + 1, priority = Priority.MEDIUM, taskManager = taskManager))
        }
        Assertions.assertThatExceptionOfType(IsAtFullCapacityAndHigherPrioritiesException::class.java)
            .isThrownBy {
                taskManager.add(Process(pid = MAX_CAPACITY + 1, priority = Priority.MEDIUM, taskManager = taskManager))
            }
        assertThat(taskManager.listProcesses().size).isEqualTo(MAX_CAPACITY)
    }

    @Test
    internal fun `should not add at full capacity if the priority is HIGH and no process with LOW or MEDIUM priority`() {
        repeat(MAX_CAPACITY) {
            taskManager.add(Process(pid = it + 1, priority = Priority.HIGH, taskManager = taskManager))
        }
        Assertions.assertThatExceptionOfType(IsAtFullCapacityAndHigherPrioritiesException::class.java)
            .isThrownBy {
                taskManager.add(Process(pid = MAX_CAPACITY + 1, priority = Priority.HIGH, taskManager = taskManager))
            }
        assertThat(taskManager.listProcesses().size).isEqualTo(MAX_CAPACITY)
    }
}