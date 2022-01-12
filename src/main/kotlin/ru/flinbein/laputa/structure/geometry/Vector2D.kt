package ru.flinbein.laputa.structure.geometry

@JvmRecord
data class Vector2D(val x: Double, val z: Double) {
    companion object {
        val fixedVectorsStraight = arrayListOf(
            Vector2D(1.0,0.0),
            Vector2D(-1.0,0.0),
            Vector2D(0.0,1.0),
            Vector2D(0.0,-1.0),
        )
        val fixedVectorsDiagonal = arrayListOf(
            Vector2D(1.0,1.0),
            Vector2D(-1.0,1.0),
            Vector2D(1.0,-1.0),
            Vector2D(-1.0,-1.0),
        )
        val fixedVectors = fixedVectorsStraight + fixedVectorsDiagonal;
    }
};