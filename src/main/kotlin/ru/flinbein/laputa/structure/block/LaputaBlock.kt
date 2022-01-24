package ru.flinbein.laputa.structure.block

import org.bukkit.block.data.BlockData
import ru.flinbein.laputa.structure.LaputaStructure
import ru.flinbein.laputa.structure.geometry.Point
import ru.flinbein.laputa.structure.geometry.Vector3D
import ru.flinbein.laputa.structure.geometry.shape.common.Shape

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
        return getRelative(Vector3D(dx.toDouble(),dy.toDouble(),dz.toDouble()))
    }

    fun getRelative(vector: Vector3D): LaputaBlock {
        val p = point.move(vector);
        return structure.getBlockAt(p.x,p.y,p.z);
    }

    fun distance(block: LaputaBlock): Double {
        return point.getDistanceTo(block.point);
    }

    fun getNeighbors(withDiagonal: Boolean = false): List<LaputaBlock> {
        val straightNeighbors = Vector3D.fixedVectorsStraight_Y2D.map { getRelative(it) };
        if (withDiagonal) {
            val diagonalNeighbors = Vector3D.fixedVectorsDiagonal_Y2D.map { getRelative(it) };
            return straightNeighbors + diagonalNeighbors;
        }
        return straightNeighbors;
    }

    fun getBlocksByShape(shape: Shape): List<LaputaBlock> {
        val box = shape.borderBox;
        return sequence {
            for (x in box.minX.toInt()..box.maxX.toInt())
                for (y in box.minY.toInt()..box.maxY.toInt())
                    for (z in box.minZ.toInt()..box.maxZ.toInt()) {
                        val point = Point.XYZ(x,y,z)
                        if (shape.includes(point).not()) continue;
                        yield(getRelative(x,y,z))
                    }
        }.toList();

    }
}