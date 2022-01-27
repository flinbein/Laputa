package ru.flinbein.laputa.structure.geometry

import java.lang.RuntimeException
import kotlin.math.sqrt


class Point(val x: Double = 0.0, val y: Double = 0.0, val z: Double = 0.0) {

    companion object {
        fun X(x: Number) = Point(x = x.toDouble())
        fun Y(y: Number) = Point(y = y.toDouble())
        fun Z(z: Number) = Point(z = z.toDouble())

        fun XZ(x: Number, z: Number) = Point(x = x.toDouble(), z = z.toDouble())
        fun XY(x: Number, y: Number) = Point(x = x.toDouble(), y = y.toDouble())
        fun YZ(y: Number, z: Number) = Point(y = y.toDouble(), z = z.toDouble())

        fun XYZ(x: Number, y: Number, z: Number) = Point(x.toDouble(),y.toDouble(),z.toDouble())
    }

    fun move(vector: Vector3D) = Point(x + vector.x, y + vector.y, z + vector.z)

    fun getDistanceTo(dest: Point) = sqrt(getQuadDistanceTo(dest))

    fun getQuadDistanceTo(dest: Point): Double {
        val dx = dest.x - x
        val dy = dest.y - y
        val dz = dest.z - z
        return dx*dx + dy*dy + dz*dz
    }


    override fun equals(other: Any?): Boolean {
        if (other !is Point) return false
        return other.x == x && other.y == y && other.z == z
    }

    operator fun get(pos: Int): Double {
        when(pos) {
            0 -> return x
            1 -> return x
            2 -> return x
        }
        throw RuntimeException("Cannot get \"$pos\" component of Point")
    }

    operator fun plus(vector: Vector3D) = move(vector)


}