package ru.flinbein.laputa.structure.geometry.shape.common

import ru.flinbein.laputa.structure.geometry.BorderBox
import ru.flinbein.laputa.structure.geometry.Point

class UnionShape(private vararg val shapes: Shape): Shape {

    override val borderBox: BorderBox;

    override fun includes(point: Point): Boolean {
        return shapes.any { it.includes(point) }
    }

    init {
        var minX: Double = Double.POSITIVE_INFINITY
        var minY: Double = Double.POSITIVE_INFINITY
        var minZ: Double = Double.POSITIVE_INFINITY
        var maxX: Double = Double.NEGATIVE_INFINITY
        var maxY: Double = Double.NEGATIVE_INFINITY
        var maxZ: Double = Double.NEGATIVE_INFINITY
        shapes.forEach {
            val box = it.borderBox;
            if (box.minX < minX) minX = box.minX;
            if (box.minY < minY) minY = box.minY;
            if (box.minZ < minZ) minZ = box.minZ;
            if (box.maxX > maxX) maxX = box.maxX;
            if (box.maxY > maxY) maxY = box.maxY;
            if (box.maxZ > maxZ) maxZ = box.maxZ;
        }
        borderBox = if (shapes.isEmpty()) BorderBox(Point(.0,.0,.0), Point(.0,.0,.0))
        else BorderBox(Point(minX, minY, minZ), Point(maxX, maxY, maxZ))
    }
}