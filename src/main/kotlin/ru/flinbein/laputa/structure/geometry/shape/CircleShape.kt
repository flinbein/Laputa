package ru.flinbein.laputa.structure.geometry.shape

import ru.flinbein.laputa.structure.geometry.Point2D
import java.util.*
import kotlin.math.*

class CircleShape(private val radius: Double, private val center: Point2D = Point2D(0.0,0.0)) : Shape2D {

    private val sqRadius: Double = radius*radius;
    override val box2D: Box2D = Box2D(
        center.x-radius,center.z-radius,
        center.x+radius,center.z+radius
    )

    override fun includes(x: Double, z: Double): Boolean {

        return (center.x-x).pow(2) + (center.z-z).pow(2) <= sqRadius
    }

    fun getRandomPoint(rand: Random, radiusPower: Double = 0.25): Point2D {
        val r = rand.nextDouble().pow(radiusPower) * radius;
        val a = rand.nextDouble();
        val angle = a*2*PI;
        return Point2D(
            r * sin(angle) + center.x,
            r * cos(angle) + center.z
        )
    }

    override fun move(dx: Double, dz: Double): CircleShape {
        return CircleShape(radius, Point2D(this.center.x+dx, this.center.z+dz))
    }
}