package ru.flinbein.laputa.structure.geometry.shape.s2d

import ru.flinbein.laputa.structure.geometry.BorderBox
import ru.flinbein.laputa.structure.geometry.Point
import ru.flinbein.laputa.structure.geometry.Vector
import ru.flinbein.laputa.structure.geometry.shape.common.Shape
import java.util.*
import kotlin.math.*

class CircleShapeY2D(private val radius: Double, private val center: Point = Point.Y_2D(0.0,0.0)) : Shape {

    private val sqRadius: Double = radius*radius;
    override val borderBox = BorderBox(
        center.move(Vector(-radius,.0,-radius)),
        center.move(Vector(radius,.0,radius))
    )

    override fun includes(point: Point): Boolean {
        return (center.x-point.x).pow(2) + (center.z-point.z).pow(2) <= sqRadius
    }

    fun getRandomPoint(rand: Random, radiusPower: Double = 0.25): Point {
        val r = rand.nextDouble().pow(radiusPower) * radius;
        val a = rand.nextDouble();
        val angle = a*2*PI;
        return Point.Y_2D(
            r * sin(angle) + center.x,
            r * cos(angle) + center.z
        )
    }

    // Get point on a circle. Phase from 0 to 1
    fun getPointOnLine(phase: Double): Point {
        val radians = phase * 2 * PI;
        val x = Math.cos(radians) * radius;
        val z = Math.sin(radians) * radius;
        return Point.Y_2D(x,z);
    }

    override fun move(vector: Vector): CircleShapeY2D {
        return CircleShapeY2D(radius, center.move(vector))
    }
}