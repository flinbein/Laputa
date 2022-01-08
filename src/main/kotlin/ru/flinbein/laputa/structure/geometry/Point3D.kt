package ru.flinbein.laputa.structure.geometry

@JvmRecord
data class Point3D(
    val x: Double,
    val y: Double,
    val z: Double,
) {
    fun fixedValues(): Point3D {
        return Point3D(x.toInt().toDouble(), y.toInt().toDouble(), z.toInt().toDouble());
    }
}