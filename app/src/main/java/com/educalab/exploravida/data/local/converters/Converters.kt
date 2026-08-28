package com.educalab.exploravida.data.local.converters

import androidx.room.TypeConverter

/** Convierte listas de claves a texto para SQLite. Sin JSON externo. */
class Converters {

    @TypeConverter
    fun fromStringList(value: List<String>?): String =
        value.orEmpty().filter { it.isNotBlank() }.joinToString(separator = "|")

    @TypeConverter
    fun toStringList(value: String?): List<String> =
        if (value.isNullOrBlank()) emptyList() else value.split("|").filter { it.isNotBlank() }
}
