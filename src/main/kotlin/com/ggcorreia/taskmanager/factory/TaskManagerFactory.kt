package com.ggcorreia.taskmanager.factory

import com.ggcorreia.taskmanager.model.DefaultTaskManager
import com.ggcorreia.taskmanager.model.FifoTaskManager
import com.ggcorreia.taskmanager.model.PriorityTaskManager
import com.ggcorreia.taskmanager.model.TaskManagerType
import com.ggcorreia.taskmanager.model.TaskManagerType.DefaultTaskManagerType
import com.ggcorreia.taskmanager.model.TaskManagerType.FifoTaskManagerType
import com.ggcorreia.taskmanager.model.TaskManagerType.PriorityTaskManagerType

abstract class TaskManagerFactory {
    companion object {
        fun createTaskManager(type: TaskManagerType, maxCapacity: Int) =
            when (type) {
                DefaultTaskManagerType -> DefaultTaskManager(maxCapacity)
                FifoTaskManagerType -> FifoTaskManager(maxCapacity)
                PriorityTaskManagerType -> PriorityTaskManager(maxCapacity)
            }
    }
}