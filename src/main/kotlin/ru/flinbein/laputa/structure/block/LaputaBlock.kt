package ru.flinbein.laputa.structure.block

import ru.flinbein.laputa.structure.LaputaStructure
import ru.flinbein.laputa.structure.geometry.Point3D
import ru.flinbein.laputa.structure.geometry.Vector2D
import ru.flinbein.laputa.structure.geometry.Vector3D
import ru.flinbein.laputa.structure.geometry.shape.Shape2D

public class LaputaBlock(val structure: LaputaStructure, val point: Point3D) {
    private val tagMap: HashMap<String, Any?> = HashMap();

    val x: Double get() = point.x;
    val y: Double get() = point.y;
    val z: Double get() = point.z;

    fun addTag(tag: String, value: Any? = null) {
        tagMap[tag] = value;
    }

    fun isEmpty(): Boolean {
        return tagMap.isEmpty();
    }

    fun hasTag(tag: String): Boolean {
        return this.tagMap.contains(tag);
    }

    fun getTagValue(tag: String): Any? {
        return tagMap.get(tag);
    }

    fun removeTag(tag: String) {
        tagMap.remove(tag);
    }

    fun removeAllTags() {
        tagMap.clear();
    }


    fun getRelativeY2D(dx: Int, dz: Int): LaputaBlock {
        return getRelative(Vector3D(dx.toDouble(),0.0,dz.toDouble()));
    }
    fun getRelativeY2D(v: Vector2D): LaputaBlock {
        return getRelative(Vector3D(v.x,0.0, v.z));
    }

    fun getRelative(dx: Number, dy: Number, dz: Number): LaputaBlock {
        val p = point;
        return structure.getBlockAt(p.x + dx.toDouble(), p.y + dy.toDouble(), p.z + dz.toDouble());
    }

    fun getRelative(vector: Vector3D): LaputaBlock {
        val p = point.move(vector);
        return structure.getBlockAt(p.x,p.y,p.z);
    }

    fun distance(block: LaputaBlock): Double {
        return point.distance(block.point);
    }

    fun getNeighbors(withDiagonal: Boolean = false): List<LaputaBlock> {
        val straightNeighbors = Vector2D.fixedVectorsStraight.map { getRelativeY2D(it) };
        if (withDiagonal) {
            val diagonalNeighbors = Vector2D.fixedVectorsDiagonal.map { getRelativeY2D(it) };
            return straightNeighbors + diagonalNeighbors;
        }
        return straightNeighbors;
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