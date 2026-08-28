package com.educalab.exploravida.data.local.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

/** Sistema del ser vivo (digestivo, respiratorio, ...). Contenido semilla. */
@Entity(tableName = "living_system")
data class LivingSystemEntity(
    @PrimaryKey val id: String,
    val name: String,
    val shortDescription: String,
    val colorHex: String,
    val iconKey: String,
    val orderIndex: Int
)

/** Relacion dirigida entre dos sistemas, con su explicacion para ninos. */
@Entity(
    tableName = "system_connection",
    foreignKeys = [
        ForeignKey(
            entity = LivingSystemEntity::class,
            parentColumns = ["id"],
            childColumns = ["fromSystemId"],
            onDelete = ForeignKey.CASCADE
        ),
        ForeignKey(
            entity = LivingSystemEntity::class,
            parentColumns = ["id"],
            childColumns = ["toSystemId"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [
        Index(value = ["fromSystemId", "toSystemId"], unique = true),
        Index(value = ["toSystemId"])
    ]
)
data class SystemConnectionEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val fromSystemId: String,
    val toSystemId: String,
    val explanation: String
)

/** Una experiencia educativa completa (comer, respirar, el viaje del oxigeno...). */
@Entity(
    tableName = "learning_experience",
    indices = [Index(value = ["orderIndex"], unique = true)]
)
data class LearningExperienceEntity(
    @PrimaryKey val id: String,
    val title: String,
    val subtitle: String,
    val noraIntro: String,
    val kind: String,
    val orderIndex: Int,
    val requiredXp: Int,
    val backgroundKey: String,
    val iconKey: String
)

/** Paso animado dentro de una experiencia. */
@Entity(
    tableName = "experience_step",
    foreignKeys = [
        ForeignKey(
            entity = LearningExperienceEntity::class,
            parentColumns = ["id"],
            childColumns = ["experienceId"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [Index(value = ["experienceId", "orderIndex"], unique = true)]
)
data class ExperienceStepEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val experienceId: String,
    val orderIndex: Int,
    val title: String,
    val text: String,
    val systemId: String?,
    val animationKey: String,
    val illustrationKey: String
)

/** Zona tocable del organismo Vita. Coordenadas normalizadas 0..1. */
@Entity(
    tableName = "interactive_element",
    foreignKeys = [
        ForeignKey(
            entity = LivingSystemEntity::class,
            parentColumns = ["id"],
            childColumns = ["systemId"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [Index(value = ["systemId"])]
)
data class InteractiveElementEntity(
    @PrimaryKey val id: String,
    val name: String,
    val systemId: String,
    val description: String,
    val x: Float,
    val y: Float,
    val radius: Float,
    val illustrationKey: String
)

/** Actividad interactiva ligada a una experiencia. */
@Entity(
    tableName = "activity",
    foreignKeys = [
        ForeignKey(
            entity = LearningExperienceEntity::class,
            parentColumns = ["id"],
            childColumns = ["experienceId"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [Index(value = ["experienceId"])]
)
data class ActivityEntity(
    @PrimaryKey val id: String,
    val experienceId: String,
    val kind: String,
    val title: String,
    val prompt: String,
    val situation: String,
    val difficulty: Int,
    val xpReward: Int
)

/** Secuencia de eventos que el nino debe ordenar. */
@Entity(
    tableName = "sequence",
    foreignKeys = [
        ForeignKey(
            entity = ActivityEntity::class,
            parentColumns = ["id"],
            childColumns = ["activityId"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [Index(value = ["activityId"], unique = true)]
)
data class SequenceEntity(
    @PrimaryKey val id: String,
    val activityId: String,
    val title: String,
    val explanation: String
)

/** Tarjeta ilustrada de una secuencia. */
@Entity(
    tableName = "sequence_item",
    foreignKeys = [
        ForeignKey(
            entity = SequenceEntity::class,
            parentColumns = ["id"],
            childColumns = ["sequenceId"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [Index(value = ["sequenceId", "correctPosition"], unique = true)]
)
data class SequenceItemEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val sequenceId: String,
    val label: String,
    val correctPosition: Int,
    val systemId: String?,
    val illustrationKey: String
)

/** Reto de conexion entre dos sistemas. */
@Entity(
    tableName = "connection_challenge",
    foreignKeys = [
        ForeignKey(
            entity = ActivityEntity::class,
            parentColumns = ["id"],
            childColumns = ["activityId"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [Index(value = ["activityId", "fromSystemId", "toSystemId"], unique = true)]
)
data class ConnectionChallengeEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val activityId: String,
    val fromSystemId: String,
    val toSystemId: String,
    val explanation: String
)

/** Insignia disponible en el laboratorio. */
@Entity(tableName = "badge")
data class BadgeEntity(
    @PrimaryKey val id: String,
    val name: String,
    val description: String,
    val iconKey: String,
    val ruleKey: String,
    val threshold: Int
)
