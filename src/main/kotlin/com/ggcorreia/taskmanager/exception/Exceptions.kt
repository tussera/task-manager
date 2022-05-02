package com.ggcorreia.taskmanager.exception

class IsAtFullCapacityException(msg: String) : RuntimeException(msg)
class ProcessUniquenessException(msg: String) : RuntimeException(msg)
class IsAtFullCapacityAndHigherPrioritiesException(msg: String) : RuntimeException(msg)