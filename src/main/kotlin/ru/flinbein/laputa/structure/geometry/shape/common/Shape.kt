package ru.flinbein.laputa.structure.geometry.shape.common

import ru.flinbein.laputa.structure.geometry.BorderBox
import ru.flinbein.laputa.structure.geometry.Point
import ru.flinbein.laputa.structure.geometry.Vector3D

interface Shape {
    val borderBox: BorderBox;

    fun includes(point: Point): Boolean;


    fun move(vector: Vector3D): Shape {
        return ShiftShape(this, vector);
    }

    fun intersect(vararg shapes: Shape): Shape {
        return IntersectShape(this, *shapes)
    }

    fun subtract(vararg shapes: Shape): Shape {
        return SubtractShape(this, *shapes)
    }

    fun union(vararg shapes: Shape): Shape {
        return UnionShape(this, *shapes);
    }
}