package ru.flinbein.laputa.structure.generator.nature

import org.bukkit.Bukkit
import org.bukkit.Material
import org.bukkit.block.data.Bisected
import ru.flinbein.laputa.structure.LaputaStructure
import ru.flinbein.laputa.structure.block.LaputaBlock
import ru.flinbein.laputa.structure.generator.LayerGenerator
import ru.flinbein.laputa.structure.generator.terrain.TerrainTags
import ru.flinbein.laputa.structure.geometry.Vector3D
import java.util.*

class GrassGenerator : LayerGenerator {

    val type: GrassType = GrassType.FOREST
    val coverage: Double = 0.45

    companion object {
        private val flowerBlockDataArray = arrayOf(
            Material.DANDELION, Material.POPPY, Material.BLUE_ORCHID,
            Material.ALLIUM, Material.AZURE_BLUET, Material.RED_TULIP,
            Material.ORANGE_TULIP, Material.WHITE_TULIP, Material.PINK_TULIP,
            Material.OXEYE_DAISY, Material.CORNFLOWER, Material.LILY_OF_THE_VALLEY
        ).map { Bukkit.createBlockData(it) }

        private val grassBlockData = Bukkit.createBlockData(Material.GRASS)
        private val tallGrassTopBlockData = Bukkit.createBlockData(Material.TALL_GRASS)
        private val tallGrassBottomBlockData = Bukkit.createBlockData(Material.TALL_GRASS)
        private val fernBlockData = Bukkit.createBlockData(Material.FERN)
        private val deadBushBlockData = Bukkit.createBlockData(Material.DEAD_BUSH)
    }

    init {
        (tallGrassTopBlockData as Bisected).half = Bisected.Half.TOP
    }

    private fun generateGrass(grassBlock: LaputaBlock, random: Random) {
        grassBlock.setTag(NatureTags.GRASS)
        if (type == GrassType.DESERT) {
            grassBlock.blockData = deadBushBlockData
        } else if (type == GrassType.FOREST) {
            val type = random.nextDouble()
            if (type <= 0.85) {
                grassBlock.blockData = grassBlockData
            } else if (type <= 0.95) {
                grassBlock.blockData = tallGrassBottomBlockData
                val upperBlock = grassBlock.getRelative(0, 1, 0)
                upperBlock.setTag(NatureTags.GRASS)
                upperBlock.blockData = tallGrassTopBlockData
            } else {
                grassBlock.blockData = fernBlockData
            }
        }
    }

    private fun generateFlower(grassBlock: LaputaBlock, random: Random) {
        grassBlock.setTag(NatureTags.GRASS)
        val flowerIndex = random.nextInt(0, flowerBlockDataArray.size)
        grassBlock.blockData = flowerBlockDataArray[flowerIndex]
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
            if (type === GrassType.FOREST) {
                val rnd = random.nextDouble()
                if (rnd < 0.95) {
                    generateGrass(block,random)
                } else {
                    generateFlower(block, random)
                }
            } else {
                generateGrass(block,random)
            }
        }
    }

    enum class GrassType {
        FOREST, DESERT
    }
}