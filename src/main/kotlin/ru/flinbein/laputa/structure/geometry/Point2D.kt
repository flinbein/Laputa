package ru.flinbein.laputa.structure.geometry

import kotlin.math.sqrt

@JvmRecord
data class Point2D(
    val x: Double,
    val z: Double,
) {
    fun move(vector2D: Vector2D): Point2D {
        return Point2D(x + vector2D.x, z + vector2D.z)
    }

    fun getDistanceTo(dist: Point2D): Double {
        return sqrt(getQuadDistanceTo(dist))
    }

    fun getQuadDistanceTo(dist: Point2D): Double {
        val dx = dist.x - x;
        val dz = dist.z - z;
        return dx*dx + dz*dz
    }
}