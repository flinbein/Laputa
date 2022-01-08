package ru.flinbein.laputa.structure.geometry.shape

class SubtractShape2D(private val shape: Shape2D, private vararg val subtractShapes: Shape2D): Shape2D  {

    override val box2D: Box2D = shape.box2D;

    override fun includes(x: Double, z: Double): Boolean {
        if (shape.includes(x,z).not()) return false;

        return subtractShapes.any { it.includes(x,z) }.not()
    }
}