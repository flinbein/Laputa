package ru.flinbein.laputa.structure

import org.bukkit.block.Block
import org.bukkit.block.data.BlockData
import ru.flinbein.laputa.structure.block.CoordinateMap3D
import ru.flinbein.laputa.structure.block.LaputaBlock
import ru.flinbein.laputa.structure.block.NamespacedTag
import ru.flinbein.laputa.structure.generator.LayerGenerator
import ru.flinbein.laputa.structure.geometry.Point3D
import java.util.*

class LaputaStructure(private var seed: Long) {
    private val blockMap = CoordinateMap3D();
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
        return blockMap.get(p) {
            LaputaBlock(this, p)
        };
    }

    fun getBlockAt(x: Int, y: Int, z: Int): LaputaBlock {
        val xD = x.toDouble();
        val yD = y.toDouble();
        val zD = z.toDouble();
        val p = Point3D(xD, yD, zD);
        return getBlockAt(p);
    }

    fun getBlockAt(x: Double, y: Double, z: Double): LaputaBlock {
        val xI = x.toInt();
        val yI = y.toInt();
        val zI = z.toInt();
        return getBlockAt(xI,yI,zI);
    }

    fun getBlocksWithTag(tag: NamespacedTag): List<LaputaBlock> {
        return blockMap.getValues().filter {
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
        generated = true;
    }

    fun apply(center: Block) {
        if (generated.not()) {
            // ToDo error?
            return;
        }
        val blocks = blockMap.getValues();
        val reversed = generators.asReversed();
        blocks.forEach {
            var blockData: BlockData? = null;
            for (layerGenerator in reversed) {
                val bData = layerGenerator.getBlockData(this, it.point);
                if (bData != null) {
                    blockData = bData;
                    break;
                }
            }
            if (blockData == null) return;
            val p = it.point;
            val bukkitBlock = center.getRelative(
                p.x.toInt(), p.y.toInt(), p.z.toInt()
            );
            bukkitBlock.blockData = blockData;
        }
    }

}