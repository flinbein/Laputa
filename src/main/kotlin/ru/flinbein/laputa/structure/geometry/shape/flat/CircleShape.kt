package ru.flinbein.laputa.structure.geometry.shape.flat

import ru.flinbein.laputa.structure.geometry.BorderBox
import ru.flinbein.laputa.structure.geometry.Point
import ru.flinbein.laputa.structure.geometry.Vector3D
import ru.flinbein.laputa.structure.geometry.dimension.DimensionCharacteristic2D
import ru.flinbein.laputa.structure.geometry.shape.common.Shape
import java.util.*
import kotlin.math.*

class CircleShape(
    private val radius: Double,
    private val center: Point = Point.XZ(0.0,0.0),
    private val dimension: DimensionCharacteristic2D = DimensionCharacteristic2D.XZ
) : Shape {

    private val sqRadius: Double = radius*radius;
    override val borderBox: BorderBox;

    init {
        val arr1 = arrayOf(radius, radius, radius);
        val arr2 = arrayOf(-radius, -radius, -radius);
        val zeroIndex = when(dimension) {
            DimensionCharacteristic2D.XY -> 2
            DimensionCharacteristic2D.XZ -> 1
            DimensionCharacteristic2D.YZ -> 0
        }
        arr2[zeroIndex] = 0.0;
        arr1[zeroIndex] = 0.0;
        val pointA = center.move(Vector3D(arr1[0], arr1[1], arr1[2]))
        val pointB = center.move(Vector3D(arr2[0], arr2[1], arr2[2]))
        borderBox = BorderBox(pointA, pointB)

    }

    override fun includes(point: Point): Boolean {
        when (dimension) {
            DimensionCharacteristic2D.XY -> {
                if (point.z != center.z) return false
            }
            DimensionCharacteristic2D.XZ -> {
                if (point.y != center.y) return false
            }
            DimensionCharacteristic2D.YZ -> {
                if (point.z != center.z) return false
            }
        }
        return point.getQuadDistanceTo(center) <= sqRadius
    }

    private fun getVectorToCirclePoint(angle: Double, r: Double): Vector3D {
        when (dimension) {
            DimensionCharacteristic2D.XY -> {
                return Vector3D(
                    r * sin(angle) + center.x,
                    0.0,
                    r * cos(angle) + center.y
                )
            }
            DimensionCharacteristic2D.XZ -> {
                return Vector3D(
                    r * sin(angle) + center.x,
                    r * cos(angle) + center.z,
                    0.0
                )
            }
            DimensionCharacteristic2D.YZ -> {
                return Vector3D(
                    0.0,
                    r * sin(angle) + center.y,
                    r * cos(angle) + center.z
                )
            }
        }
    }

    fun getRandomPoint(rand: Random, radiusPower: Double = 0.25): Point {
        val r = rand.nextDouble().pow(radiusPower) * radius;
        val a = rand.nextDouble();
        val angle = a*2*PI;
        val vector = getVectorToCirclePoint(angle,r);
        return center.move(vector);
    }

    // Get point on a circle. Phase from 0 to 1
    fun getPointOnLine(phase: Double): Point {
        val angle = phase * 2 * PI;
        val vector = getVectorToCirclePoint(angle,radius);
        return center.move(vector)
    }

    override fun move(vector: Vector3D): CircleShape {
        return CircleShape(radius, center.move(vector))
    }
}