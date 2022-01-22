package ru.flinbein.laputa.structure.geometry

import kotlin.math.sqrt

class Vector(val x: Double = 0.0, val y: Double = 0.0, val z: Double = 0.0) {

    companion object {
        val fixedVectorsStraight_Y2D = arrayListOf(
            Vector(1.0,  0.0, 0.0),
            Vector(-1.0, 0.0, 0.0),
            Vector(0.0,  0.0, 1.0),
            Vector(0.0,  0.0, -1.0),
        )
        val fixedVectorsDiagonal_Y2D = arrayListOf(
            Vector(1.0,  0.0, 1.0),
            Vector(-1.0, 0.0, 1.0),
            Vector(1.0,  0.0, -1.0),
            Vector(-1.0, 0.0, -1.0),
        )
        val fixedVectors_Y2D = fixedVectorsStraight_Y2D + fixedVectorsDiagonal_Y2D;

        fun betweenPoints(from: Point2D, to:Point2D): Vector2D {
            return Vector2D(to.x - from.x, to.z - from.z);
        }
    }

    val length by lazy { sqrt(x*x + y*y + z*z) }

    fun normalize(): Vector {;
        return Vector(x/length, y/length, z/length);
    }

    operator fun times(n: Double) = Vector(x*n, y*n, z*n)
    operator fun div(n: Double) = Vector(x/n, z*n, z/n)
}