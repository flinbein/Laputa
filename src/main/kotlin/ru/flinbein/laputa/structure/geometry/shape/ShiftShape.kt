package ru.flinbein.laputa.structure.geometry.shape

class ShiftShape(private val shape: Shape2D, private val dx: Double, private val dz: Double): Shape2D {

    override val box2D: Box2D = Box2D(
        shape.box2D.minX-dx, shape.box2D.minZ-dz,
        shape.box2D.maxX-dx, shape.box2D.maxZ-dz,
    );

    override fun includes(x: Double, z: Double): Boolean {
        return shape.includes(x-dx, z-dz)
    }
}