package ru.flinbein.laputa.structure.geometry

@JvmRecord
data class Point2D(
    val x: Double,
    val z: Double,
) {
    fun move(vector2D: Vector2D): Point2D {
        return Point2D(x + vector2D.x, z + vector2D.z)
    }
}