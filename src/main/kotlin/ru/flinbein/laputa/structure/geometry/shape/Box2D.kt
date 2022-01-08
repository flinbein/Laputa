package ru.flinbein.laputa.structure.geometry.shape

@JvmRecord
data class Box2D(
    val minX: Double,
    val minZ: Double,
    val maxX: Double,
    val maxZ: Double,
) {}