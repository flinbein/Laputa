package ru.flinbein.laputa.structure.block

import ru.flinbein.laputa.structure.geometry.Point

@JvmRecord
data class BlockPoint2D(val x: Int, val z: Int) {
    companion object {
        fun fromPoint(point: Point) = BlockPoint2D(
            point.x.toInt(),
            point.z.toInt(),
        )
    }
}