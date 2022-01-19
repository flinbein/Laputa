package ru.flinbein.laputa.structure.geometry

import kotlin.math.sqrt

@JvmRecord
data class Vector2D(val x: Double, val z: Double) {
    companion object {
        val fixedVectorsStraight = arrayListOf(
            Vector2D(1.0,0.0),
            Vector2D(-1.0,0.0),
            Vector2D(0.0,1.0),
            Vector2D(0.0,-1.0),
        )
        val fixedVectorsDiagonal = arrayListOf(
            Vector2D(1.0,1.0),
            Vector2D(-1.0,1.0),
            Vector2D(1.0,-1.0),
            Vector2D(-1.0,-1.0),
        )
        val fixedVectors = fixedVectorsStraight + fixedVectorsDiagonal;

        fun betweenPoints(from: Point2D, to:Point2D): Vector2D {
            return Vector2D(to.x - from.x, to.z - from.z);
        }
    }

    fun getLength(): Double {
        return sqrt(x*x+z*z);
    }

    fun normalize(): Vector2D {
        val length = getLength();
        return Vector2D(x/length, z/length);
    }

    operator fun times(n: Double) = Vector2D(x*n, z*n)
    operator fun div(n: Double) = Vector2D(x/n, z/n)
};