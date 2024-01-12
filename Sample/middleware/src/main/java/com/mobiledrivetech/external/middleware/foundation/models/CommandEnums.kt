package com.mobiledrivetech.external.middleware.foundation.models

enum class CommandStatus {
    WAITING,
    CANCELED,
    SUCCEEDED,
    FAILED
}

sealed class CommandType(val type: String) {
    object Get : CommandType("GET")
    object Set : CommandType("SET")
    object Subscribe : CommandType("SUBSCRIBE")
    object Unsubscribe : CommandType("UNSUBSCRIBE")
    object Unknown : CommandType("unknown")

    override fun toString(): String {
        return this.javaClass.simpleName + "($type)"
    }

    companion object {
        private val map by lazy {
            CommandType::class.nestedClasses // return a list of object nested class
                .asSequence() // loop as sequence for optimisation
                .map { it.objectInstance } // return only class with object instance
                .filterIsInstance<CommandType>() // get object instance
        }

        fun valueOf(value: String) =
            map.firstOrNull { value.equals(it.type, true) } ?: Unknown
    }
}

open class CommandName(name: String) {

    var name: String? = name
        private set

    override fun toString(): String {
        return name ?: ""
    }

    override fun equals(other: Any?): Boolean {
        return name == (other as CommandName).name
    }

    override fun hashCode(): Int {
        return name?.hashCode() ?: 0
    }
}
