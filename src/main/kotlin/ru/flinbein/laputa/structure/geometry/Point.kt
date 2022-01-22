package ru.flinbein.laputa.structure.geometry

import java.lang.RuntimeException
import kotlin.math.sqrt

class Point(val x: Double = Double.NaN, val y: Double = Double.NaN, val z: Double = Double.NaN) {

    companion object {
        fun X1D(x: Number): Point = Point(x = x.toDouble());
        fun Y1D(y: Number): Point = Point(y = y.toDouble());
        fun Z1D(z: Number): Point = Point(z = z.toDouble());

        fun X_2D(y: Number, z: Number): Point = Point(y = y.toDouble(), z = z.toDouble());
        fun Y_2D(x: Number, z: Number): Point = Point(x = x.toDouble(), z = z.toDouble());
        fun Z_2D(x: Number, y: Number): Point = Point(x = x.toDouble(), y = y.toDouble());

    }

    init {
        var count = 0
        if (x.isNaN().not()) count++
        if (y.isNaN().not()) count++
        if (z.isNaN().not()) count++
        dimensionCount = count
    }

    fun getDimensionsCount(): Int {
        var count = 0;
        if (x.isNaN().not()) count++;
        if (y.isNaN().not()) count++;
        if (z.isNaN().not()) count++;
        return count;
    }

    fun isInvalid(): Boolean = getDimensionsCount() == 0
    fun is1D(): Boolean = getDimensionsCount() == 1
    fun is2D(): Boolean = getDimensionsCount() == 2
    fun is3D(): Boolean = getDimensionsCount() == 3

    fun isInSameDimensions(point: Point): Boolean {
        return x.isNaN() == point.x.isNaN() && y.isNaN() == point.y.isNaN() && z.isNaN() == point.z.isNaN()
    }

    fun move(vector: Vector) = Point(x + vector.x, y + vector.y, z + vector.z);

    fun getDistanceTo(dest: Point) = sqrt(getQuadDistanceTo(dest))

    fun getQuadDistanceTo(dest: Point): Double {
        if (isInSameDimensions(dest).not()) {
            throw RuntimeException("Trying to get distance between points with different dimensions")
        }
        val dx = dest.x - x;
        val dy = dest.y - y;
        val dz = dest.z - z;
        return arrayOf(dx,dy,dz).filterNot { it.isNaN() }.reduce { a,b ->
            a+b
        }
    }

}