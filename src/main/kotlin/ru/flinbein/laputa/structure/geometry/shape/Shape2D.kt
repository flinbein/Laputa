package ru.flinbein.laputa.structure.geometry.shape

interface Shape2D {
    val box2D: Box2D;

    fun includes(x: Double, z: Double): Boolean;

    fun intersect(vararg shape: Shape2D): Shape2D {
        return IntersectShape2D(this, *shape)
    }

    fun move(dx: Double, dz: Double): Shape2D {
        return ShiftShape(this,dx,dz);
    }
}