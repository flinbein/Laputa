package ru.flinbein.laputa.structure.generator.nature

import org.bukkit.Bukkit
import org.bukkit.Material
import org.bukkit.block.data.Bisected
import org.bukkit.block.data.BlockData
import ru.flinbein.laputa.structure.LaputaStructure
import ru.flinbein.laputa.structure.block.LaputaBlock
import ru.flinbein.laputa.structure.generator.LayerGenerator
import ru.flinbein.laputa.structure.generator.terrain.TerrainTags
import ru.flinbein.laputa.structure.geometry.Vector3D
import ru.flinbein.laputa.util.probability.ProbabilityMap
import java.util.*

class GrassGenerator : LayerGenerator {

    val coverage: Double = 0.45

    companion object {
        @JvmStatic
        private val tallGrassBottomBlockData = Bukkit.createBlockData(Material.TALL_GRASS)
        @JvmStatic
        private val tallGrassTopBlockData = Bukkit.createBlockData(Material.TALL_GRASS) {
            (it as Bisected).half = Bisected.Half.TOP
        }

        @JvmStatic
        private val flowerBlockDataArray = arrayOf(
            Material.DANDELION, Material.POPPY, Material.BLUE_ORCHID,
            Material.ALLIUM, Material.AZURE_BLUET, Material.RED_TULIP,
            Material.ORANGE_TULIP, Material.WHITE_TULIP, Material.PINK_TULIP,
            Material.OXEYE_DAISY, Material.CORNFLOWER, Material.LILY_OF_THE_VALLEY
        ).map { Bukkit.createBlockData(it) }

        @JvmStatic
        private val fernBlockData = Bukkit.createBlockData(Material.FERN)


        @JvmStatic
        private val forestVegetationProbabilityMap = ProbabilityMap.fromHashMap(hashMapOf(
            Bukkit.createBlockData(Material.GRASS) to 100.0,
            tallGrassBottomBlockData to 50.0,
            fernBlockData to 5.0,
        )).addSubChanceList(flowerBlockDataArray, 10.0)


        @JvmStatic
        private val desertVegetationProbabilityMap = ProbabilityMap.fromHashMap(hashMapOf(
            Bukkit.createBlockData(Material.DEAD_BUSH) to 1.0
        ))
    }

    private var vegetationProbabilityMap: ProbabilityMap<BlockData> = forestVegetationProbabilityMap;

    fun setType(type: GrassType) {
        vegetationProbabilityMap = when (type) {
            GrassType.DESERT -> desertVegetationProbabilityMap
            else -> forestVegetationProbabilityMap;
        }
    }

    override fun fill(structure: LaputaStructure, random: Random) {
        val blocksForGrass = structure.getBlocksWithTag(TerrainTags.TERRAIN).map {
            it.getRelative(Vector3D(0.0, 1.0, 0.0))
        }.filter {
            it.isEmpty();
        }
        val count = (coverage*blocksForGrass.size).toInt()
        val shuffledBlocks = blocksForGrass.shuffled(random)
        for (i in 0 until count) {
            val block = shuffledBlocks[i]
            val randomBlockData = vegetationProbabilityMap.getRandomItem(random);
            block.setTag(NatureTags.GRASS, randomBlockData);
            block.abstract = false;
            if (randomBlockData.material == Material.TALL_GRASS) {
                val aboveBlock = block.getRelative(0.0, 1.0, 0.0);
                aboveBlock.setTag(NatureTags.GRASS, tallGrassTopBlockData);
                aboveBlock.abstract = false;
            }
        }
    }

    override fun handleBlock(block: LaputaBlock, random: Random, structure: LaputaStructure): BlockData? {
        val blockData = block.getTagValue(NatureTags.GRASS) ?: return null;
        return blockData as BlockData;
    }

    enum class GrassType {
        FOREST, DESERT
    }
}