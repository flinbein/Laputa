package ru.flinbein.laputa.structure.block

import org.bukkit.block.data.BlockData
import ru.flinbein.laputa.structure.LaputaStructure
import ru.flinbein.laputa.structure.geometry.Point
import ru.flinbein.laputa.structure.geometry.Vector

class LaputaBlock internal constructor(private val structure: LaputaStructure, private val blockPoint: BlockPoint) {
    private val tagMap: HashMap<String, Any?> = HashMap();
    val point: Point;
    var blockData: BlockData? = null;
    val x: Double get() = point.x;
    val y: Double get() = point.y;
    val z: Double get() = point.z;

    init {
        this.point = blockPoint.toPoint()
    }

    fun setTag(tag: String, value: Any? = null) {
        tagMap[tag] = value;
    }

    fun isEmpty(): Boolean {
        return tagMap.isEmpty() && blockData == null;
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

    fun clear() {
        tagMap.clear();
        blockData = null;
    }


    fun getRelative(dx: Number, dy: Number, dz: Number): LaputaBlock {
        return getRelative(Vector(dx.toDouble(),dy.toDouble(),dz.toDouble()))
    }

    fun getRelative(vector: Vector): LaputaBlock {
        val p = point.move(vector);
        return structure.getBlockAt(p.x,p.y,p.z);
    }

    fun distance(block: LaputaBlock): Double {
        return point.getDistanceTo(block.point);
    }

    fun getNeighbors(withDiagonal: Boolean = false): List<LaputaBlock> {
        val straightNeighbors = Vector.fixedVectorsStraight_Y2D.map { getRelative(it) };
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