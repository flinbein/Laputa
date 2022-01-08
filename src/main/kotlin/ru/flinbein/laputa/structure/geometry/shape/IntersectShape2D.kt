package ru.flinbein.laputa.structure.geometry.shape

class IntersectShape2D(private vararg val shapes: Shape2D) : Shape2D {
    override val box2D: Box2D

    init {
        var minX: Double = Double.NEGATIVE_INFINITY
        var minZ: Double = Double.NEGATIVE_INFINITY
        var maxX: Double = Double.POSITIVE_INFINITY
        var maxZ: Double = Double.POSITIVE_INFINITY
        shapes.forEach {
            val box = it.box2D;
            if (box.minX > minX) minX = box.minX;
            if (box.minZ > minZ) minZ = box.minZ;
            if (box.maxX < maxX) maxX = box.maxX;
            if (box.maxZ < maxZ) maxZ = box.maxZ;
        }
        box2D = if (shapes.isEmpty()) Box2D(.0,.0,.0,.0);
        else Box2D(minX,minZ,maxX,maxZ)
    }

    override fun includes(x: Double, z: Double): Boolean {
        return shapes.all { it.includes(x,z) }
    }


}