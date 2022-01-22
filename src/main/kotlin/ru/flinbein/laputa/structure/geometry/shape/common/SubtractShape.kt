package ru.flinbein.laputa.structure.geometry.shape.common

import ru.flinbein.laputa.structure.geometry.BorderBox
import ru.flinbein.laputa.structure.geometry.Point

class SubtractShape(private val shape: Shape, private vararg val subtractShapes: Shape): Shape {

    override val borderBox: BorderBox = shape.borderBox;

    override fun includes(point: Point): Boolean {
        if (shape.includes(point).not()) return false;

        return subtractShapes.any { it.includes(point) }.not()
    }
}