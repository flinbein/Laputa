package ru.flinbein.laputa.structure.block

import ru.flinbein.laputa.structure.LaputaStructure
import ru.flinbein.laputa.structure.geometry.Point3D
import ru.flinbein.laputa.structure.geometry.shape.Shape2D

public class LaputaBlock(val structure: LaputaStructure, val point: Point3D) {
    private val tags: HashSet<NamespacedTag> = HashSet();

    fun addTags(vararg tags: NamespacedTag) {
        this.tags.addAll(tags);
    }

    fun hasTag(tag: NamespacedTag): Boolean {
        return this.tags.contains(tag);
    }

    fun hasAnyTagFromNamespace(namespace: String): Boolean {
        return tags.any { it.namespace == namespace }
    }

    fun getRelativeY2D(dx: Int, dz: Int): LaputaBlock {
        return structure.getBlockAt(point.x+dx, point.y, point.z+dz);
    }

    fun getRelative(dx: Int, dy: Int, dz: Int): LaputaBlock {
        return structure.getBlockAt(point.x+dx, point.y+dy, point.z+dz);
    }

    fun getBlocksByShape(shape2D: Shape2D): List<LaputaBlock> {
        val box2D = shape2D.box2D;
        return sequence {
            for (x in box2D.minX.toInt()..box2D.maxX.toInt() step 1) {
                for (z in box2D.minZ.toInt()..box2D.maxZ.toInt() step 1) {
                    if (shape2D.includes(x.toDouble(),z.toDouble()).not()) continue;

                    yield(getRelativeY2D(x,z));
                }
            }
        }.toList();

    }
}