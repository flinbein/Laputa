package ru.flinbein.laputa.structure.geometry.shape.common

import ru.flinbein.laputa.structure.geometry.BorderBox
import ru.flinbein.laputa.structure.geometry.Point
import ru.flinbein.laputa.structure.geometry.Vector3D

class ShiftShape(private val shape: Shape, private val vector: Vector3D): Shape {

    override val borderBox = BorderBox(
        shape.borderBox.minPoint.move(vector),
        shape.borderBox.maxPoint.move(vector)
    )

    override fun includes(point: Point): Boolean {
        return shape.includes(point.move(vector));
    }
}