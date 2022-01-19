package ru.flinbein.laputa.structure.geometry

import kotlin.math.sqrt

@JvmRecord
data class Point3D(
    val x: Double,
    val y: Double,
    val z: Double,
) {
    fun fixedValues(): Point3D {
        return Point3D(x.toInt().toDouble(), y.toInt().toDouble(), z.toInt().toDouble());
    }

    fun move(vector: Vector3D): Point3D {
        return Point3D(x + vector.x, y+vector.y, z + vector.z)
    }

    fun getDistanceTo(dist: Point3D): Double {
        return sqrt(getQuadDistanceTo(dist))
    }

    fun getQuadDistanceTo(dist: Point3D): Double {
        val dx = dist.x - x;
        val dy = dist.y - y;
        val dz = dist.z - z;
        return dx*dx + dy*dy + dz*dz
    }
}