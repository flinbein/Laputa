package ru.flinbein.laputa.structure

import org.bukkit.block.Block
import org.bukkit.block.data.BlockData
import org.bukkit.entity.Player
import ru.flinbein.laputa.structure.block.LaputaBlock
import ru.flinbein.laputa.structure.generator.LayerGenerator
import ru.flinbein.laputa.structure.geometry.Point2D
import ru.flinbein.laputa.structure.geometry.Point3D
import java.lang.RuntimeException
import java.util.*
import kotlin.collections.HashMap

class LaputaStructure(private var seed: Long) {
    private val blockMap = HashMap<Point3D,LaputaBlock>();
    private val generatedBlockDataMap = HashMap<Point3D,BlockData>();
    private val generators: LinkedList<LayerGenerator> = LinkedList();
    private var random: Random = Random(seed);
    var generated: Boolean = false
        private set;

    fun getSeed(): Long = seed;
    fun setSeed(newSeed: Long) {
        seed = newSeed;
    };

    fun getBlockAt(point3D: Point3D): LaputaBlock {
        val p = point3D.fixedValues();
        return blockMap.getOrPut(p) {
            LaputaBlock(this, p)
        };
    }

    fun getBlockAt(x: Number, y: Number, z: Number): LaputaBlock {
        val xD = x.toDouble();
        val yD = y.toDouble();
        val zD = z.toDouble();
        val p = Point3D(xD, yD, zD).fixedValues();
        return getBlockAt(p);
    }

    fun hasBlockAt(x: Number, y: Number, z: Number): Boolean {
        return blockMap.containsKey(Point3D(x.toDouble(),y.toDouble(),z.toDouble()));
    }
    fun hasBlockAt(point3D: Point3D): Boolean {
        return blockMap.containsKey(point3D);
    }

    // Maybe should create a block at x,0,z and check up&down for blocks;
    fun getHighestBlockAt(x: Double, z: Double): LaputaBlock? {
        return blockMap.values.filter {
            it.point.x == x && it.point.z == z
        }.sortedBy {
            it.point.y
        }.getOrNull(0);
    }
    fun getHighestBlockAt(point: Point2D): LaputaBlock? {
        return getHighestBlockAt(point.x, point.z);
    }

    // Maybe should create a block at x,z,0 and check up&down for blocks;
    fun getHighestBlockWithTag(x: Double, z: Double, tag: String): LaputaBlock? {
        return blockMap.values.filter {
            it.point.x == x && it.point.z == z && it.hasTag(tag)
        }.sortedBy {
            it.point.y
        }.getOrNull(0);
    }
    fun getHighestBlockWithTag(point: Point2D, tag: String): LaputaBlock? {
        return getHighestBlockWithTag(point.x, point.z, tag);
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
            generator.fill(this, random);
        }

        // Fill generatedBlockDataMap
        fillGeneratedBlockDataMap();


        generated = true;
    }

    private fun fillGeneratedBlockDataMap() {
        generatedBlockDataMap.clear();
        val blocks = blockMap.values;
        val reversed = generators.asReversed();
        blocks.forEach {
            var blockData: BlockData? = null;
            for (layerGenerator in reversed) {
                val bData = layerGenerator.getBlockData(it, random, this);
                if (bData != null) {
                    blockData = bData;
                    break;
                }
            }
            if (blockData == null) return@forEach;
            generatedBlockDataMap[it.point] = blockData;
        }
    }

    fun preview(center: Block, player: Player) {
        if (generated.not()) {
            throw RuntimeException("Structure must be generated before previewing or applying")
        }
        generatedBlockDataMap.forEach {
            val block = center.getRelative(it.key.x.toInt(), it.key.y.toInt(), it.key.z.toInt());
            player.sendBlockChange(block.location, it.value);
        }
    }

    fun apply(center: Block) {
        if (generated.not()) {
            throw RuntimeException("Structure must be generated before previewing or applying")
        }
        generatedBlockDataMap.forEach {
            val block = center.getRelative(it.key.x.toInt(), it.key.y.toInt(), it.key.z.toInt());
            block.blockData = it.value;
        }
    }

}