package ru.flinbein.laputa.structure

import org.bukkit.block.Block
import org.bukkit.entity.Player
import ru.flinbein.laputa.LaputaPlugin
import ru.flinbein.laputa.structure.block.BlockPoint
import ru.flinbein.laputa.structure.block.BlockPoint2D
import ru.flinbein.laputa.structure.block.LaputaBlock
import ru.flinbein.laputa.structure.generator.LayerGenerator
import ru.flinbein.laputa.structure.geometry.Point
import java.lang.RuntimeException
import java.util.*
import kotlin.collections.HashMap

class LaputaStructure(private var seed: Long) {
    private val blockMap = HashMap<BlockPoint, LaputaBlock>();
    private val heightsMap = HashMap<BlockPoint2D, Array<Int>>(); // [minY, maxY] for each Point2D(X, Z)
//    private val generatedBlockDataMap = HashMap<Point3D,BlockData>();
    private val generators: LinkedList<LayerGenerator> = LinkedList();
    private var random: Random = Random(seed);
    var generated: Boolean = false
        private set;

    fun getSeed(): Long = seed;
    fun setSeed(newSeed: Long) {
        seed = newSeed;
    };

    fun getBlockAt(point: Point): LaputaBlock {
        val bp = BlockPoint.fromPoint(point);
        val block = blockMap.getOrPut(bp) {
            LaputaBlock(this, bp)
        };
        val bp2D = BlockPoint2D.fromPoint(point);
        val heights = heightsMap.getOrPut(bp2D) {
            arrayOf(bp.y, bp.y);
        }
        if (bp.y < heights[0]) heights[0] = bp.y;
        if (bp.y > heights[1]) heights[1] = bp.y;
        return block;
    }

    fun getBlockAt(x: Number, y: Number, z: Number): LaputaBlock {
        val xD = x.toDouble();
        val yD = y.toDouble();
        val zD = z.toDouble();
        return getBlockAt(Point(xD, yD, zD));
    }


    fun hasBlockAt(point: Point): Boolean {
        return blockMap.containsKey(BlockPoint.fromPoint(point));
    }
    fun hasBlockAt(x: Number, y: Number, z: Number): Boolean {
        return hasBlockAt(Point(x.toDouble(),y.toDouble(),z.toDouble()));
    }


    fun getHighestBlockAt(point: Point): LaputaBlock? {
        val bp = BlockPoint2D.fromPoint(point);
        val heights = heightsMap[bp] ?: return null;
        return blockMap[BlockPoint(bp.x, heights[1], bp.z)]
    }
    fun getHighestBlockAt(x: Double, z: Double): LaputaBlock? {
        return getHighestBlockAt(Point(x,z));
    }

    fun getHighestBlockWithTag(point: Point, tag: String): LaputaBlock? {
        val heights = heightsMap[BlockPoint2D.fromPoint(point)] ?: return null;
        val maxY = heights[1]
        val minY = heights[0]
        for (y in maxY downTo minY) {
            val pointOfBlock = Point(point.x, y.toDouble(), point.z);
            if (hasBlockAt(pointOfBlock).not()) continue;
            val block = getBlockAt(pointOfBlock);
            if (block.hasTag(tag)) return block;
        }
        return null;
    }
    fun getHighestBlockWithTag(x: Double, z: Double, tag: String): LaputaBlock? {
        return getHighestBlockWithTag(Point2D(x, z), tag);
    }



    fun getBlocksWithTag(tag: String): List<LaputaBlock> {
        return blockMap.values.filter {
            it.hasTag(tag);
        }
    }

    fun addGenerator(vararg generator: LayerGenerator) {
        generators.addAll(generator)
    }

    fun generate() {
        blockMap.clear();
        random = Random(seed);
        for (generator in generators) {
            val ts = Date().time;
            generator.fill(this, random);
            val dif = Date().time - ts;
            LaputaPlugin.getPlugin().logger.info("GEN ${generator.javaClass.name} DONE IN $dif")
        }
        generated = true;
    }


    fun preview(center: Block, player: Player) {
        if (generated.not()) {
            throw RuntimeException("Structure must be generated before previewing or applying")
        }
        blockMap.forEach {
            val blockData = it.value.blockData ?: return@forEach;
            val block = center.getRelative(it.key.x.toInt(), it.key.y.toInt(), it.key.z.toInt());
            player.sendBlockChange(block.location, blockData);
        }
    }

    fun apply(center: Block) {
        if (generated.not()) {
            throw RuntimeException("Structure must be generated before previewing or applying")
        }
        blockMap.forEach {
            val blockData = it.value.blockData ?: return@forEach;
            val block = center.getRelative(it.key.x.toInt(), it.key.y.toInt(), it.key.z.toInt());
            block.blockData = blockData;
        }
    }

}