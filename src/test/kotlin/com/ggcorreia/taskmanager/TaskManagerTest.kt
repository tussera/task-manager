package com.ggcorreia.taskmanager

import com.ggcorreia.taskmanager.exception.ProcessUniquenessException
import com.ggcorreia.taskmanager.factory.TaskManagerFactory
import com.ggcorreia.taskmanager.model.Priority
import com.ggcorreia.taskmanager.model.Process
import com.ggcorreia.taskmanager.model.SortBy
import com.ggcorreia.taskmanager.model.TaskManager
import com.ggcorreia.taskmanager.model.TaskManagerType
import org.assertj.core.api.Assertions
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

const val MAX_CAPACITY = 10

internal class TaskManagerTest {
    private lateinit var taskManager: TaskManager
    private var processList = mutableListOf<Process>()

    @BeforeEach
    internal fun setup() {
        taskManager = TaskManagerFactory.createTaskManager(TaskManagerType.DefaultTaskManagerType, MAX_CAPACITY)
        processList.clear()
        processList.add(Process(pid = 20, taskManager = taskManager))
        processList.add(Process(pid = 10, priority = Priority.MEDIUM, taskManager = taskManager))
        processList.add(Process(pid = 30, priority = Priority.HIGH, taskManager = taskManager))
        processList.add(Process(pid = 60, priority = Priority.MEDIUM, taskManager = taskManager))
        processList.add(Process(pid = 50, priority = Priority.HIGH, taskManager = taskManager))
    }

    @Test
    internal fun `should throw exception if the capacity is not greater than 0`() {
        Assertions.assertThatThrownBy {
            taskManager = TaskManagerFactory.createTaskManager(TaskManagerType.DefaultTaskManagerType, 0)
        }.hasMessage("Max Capacity should be greater than 0!")
    }

    @Test
    internal fun `should throw exception when adding process with existent PID`() {
        taskManager.add(processList[0])
        Assertions.assertThatExceptionOfType(ProcessUniquenessException::class.java)
            .isThrownBy {
                taskManager.add(processList[0])
            }
    }

    @Test
    internal fun `should list all processes sorting by Creation Date ASC`() {
        taskManager.add(processList[0])
        taskManager.add(processList[1])
        taskManager.add(processList[2])
        taskManager.add(processList[3])
        taskManager.add(processList[4])

        val expectedResult = listOf(processList[0], processList[1], processList[2], processList[3], processList[4])
        val result = taskManager.listProcesses(SortBy.CreationDateASC)

        assertThat(result).isEqualTo(expectedResult)
    }

    @Test
    internal fun `should list all processes sorting by Creation Date DESC`() {
        taskManager.add(processList[0])
        taskManager.add(processList[1])
        taskManager.add(processList[2])
        taskManager.add(processList[3])
        taskManager.add(processList[4])

        val expectedResult = listOf(processList[4], processList[3], processList[2], processList[1], processList[0])
        val result = taskManager.listProcesses(SortBy.CreationDateDESC)

        assertThat(result).isEqualTo(expectedResult)
    }

    @Test
    internal fun `should list all processes sorting by Priority ASC`() {
        taskManager.add(processList[0])
        taskManager.add(processList[1])
        taskManager.add(processList[2])
        taskManager.add(processList[3])
        taskManager.add(processList[4])

        val expectedResult = listOf(processList[0], processList[1], processList[3], processList[2], processList[4])
        val result = taskManager.listProcesses(SortBy.PriorityASC)

        assertThat(result)
            .usingRecursiveComparison()
            .ignoringFields(Process::creationDate.name, Process::pid.name)
            .isEqualTo(expectedResult)
    }

    @Test
    internal fun `should list all processes sorting by Priority DESC`() {
        taskManager.add(processList[0])
        taskManager.add(processList[1])
        taskManager.add(processList[2])
        taskManager.add(processList[3])
        taskManager.add(processList[4])

        val expectedResult = listOf(processList[4], processList[2], processList[3], processList[1], processList[0])
        val result = taskManager.listProcesses(SortBy.PriorityDESC)

        assertThat(result)
            .usingRecursiveComparison()
            .ignoringFields(Process::creationDate.name, Process::pid.name)
            .isEqualTo(expectedResult)
    }

    @Test
    internal fun `should list all processes sorting by Id ASC`() {
        taskManager.add(processList[0])
        taskManager.add(processList[1])
        taskManager.add(processList[2])
        taskManager.add(processList[3])
        taskManager.add(processList[4])

        val expectedResult = listOf(processList[1], processList[0], processList[2], processList[4], processList[3])
        val result = taskManager.listProcesses(SortBy.IdASC)

        assertThat(result).isEqualTo(expectedResult)
    }

    @Test
    internal fun `should list all processes sorting by Id DESC`() {
        taskManager.add(processList[0])
        taskManager.add(processList[1])
        taskManager.add(processList[2])
        taskManager.add(processList[3])
        taskManager.add(processList[4])

        val expectedResult = listOf(processList[3], processList[4], processList[2], processList[0], processList[1])
        val result = taskManager.listProcesses(SortBy.IdDESC)

        assertThat(result).isEqualTo(expectedResult)
    }

    @Test
    internal fun `should kill a process by Id`() {
        taskManager.add(processList[0])
        taskManager.add(processList[1])
        taskManager.add(processList[2])
        taskManager.add(processList[3])
        taskManager.add(processList[4])

        val expectedResult = listOf(processList[0], processList[1], processList[3], processList[4])
        taskManager.kill(processList[2].pid)
        val result = taskManager.listProcesses(SortBy.CreationDateASC)

        assertThat(result).isEqualTo(expectedResult)
    }

    @Test
    internal fun `should kill all process from a given group`() {
        taskManager.add(processList[0])
        taskManager.add(processList[1])
        taskManager.add(processList[2])
        taskManager.add(processList[3])
        taskManager.add(processList[4])

        val expectedResult = listOf(processList[0], processList[1], processList[3])
        taskManager.killGroup(Priority.HIGH)
        val result = taskManager.listProcesses(SortBy.CreationDateASC)

        assertThat(result).isEqualTo(expectedResult)
    }

    @Test
    internal fun `should kill all process`() {
        taskManager.add(processList[0])
        taskManager.add(processList[1])
        taskManager.add(processList[2])
        taskManager.add(processList[3])
        taskManager.add(processList[4])

        val expectedResult = listOf<Process>()
        taskManager.killAll()
        val result = taskManager.listProcesses()

        assertThat(result).isEqualTo(expectedResult)
    }

}