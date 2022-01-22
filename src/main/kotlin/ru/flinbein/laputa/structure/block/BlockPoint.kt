package ru.flinbein.laputa.structure.block

import ru.flinbein.laputa.structure.geometry.Point

@JvmRecord
data class BlockPoint(val x: Int, val y: Int, val z: Int) {
    companion object {
        fun fromPoint(point: Point) = BlockPoint(
            point.x.toInt(),
            point.y.toInt(),
            point.z.toInt(),
        )
        fun fromPoint_Y2D(point: Point) = BlockPoint(
            point.x.toInt(),
            0,
            point.z.toInt(),
        )
    }

    fun toPoint(): Point = Point(x.toDouble(), y.toDouble(), z.toDouble());
}