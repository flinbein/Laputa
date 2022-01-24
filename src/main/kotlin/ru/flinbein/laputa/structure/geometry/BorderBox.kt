package ru.flinbein.laputa.structure.geometry

import kotlin.math.max
import kotlin.math.min

data class BorderBox(val point1: Point, val point2: Point) {

    val minX = min(point1.x, point2.x)
    val minY = min(point1.y, point2.y)
    val minZ = min(point1.z, point2.z)
    val maxX = max(point1.x, point2.x)
    val maxY = max(point1.y, point2.y)
    val maxZ = max(point1.z, point2.z)
    val minPoint = Point(minX, minY, minZ);
    val maxPoint = Point(maxX, maxY, maxZ);


}