package ru.flinbein.laputa.structure.geometry

import java.lang.RuntimeException
import kotlin.math.max
import kotlin.math.min

class BorderBox(point1: Point, point2: Point) {

    val minX = min(point1.x, point2.x)
    val minY = min(point1.y, point2.y)
    val minZ = min(point1.z, point2.z)
    val maxX = max(point1.x, point2.x)
    val maxY = max(point1.y, point2.y)
    val maxZ = max(point1.z, point2.z)
    val minPoint = Point(minX, minY, minZ);
    val maxPoint = Point(maxX, maxY, maxZ);

    init {
        if (point1.isInSameDimensions(point2).not()) {
            throw RuntimeException("Trying to create border box with points in different dimensions");
        }
    }


}