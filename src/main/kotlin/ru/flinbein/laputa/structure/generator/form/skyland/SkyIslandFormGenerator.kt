package ru.flinbein.laputa.structure.generator.form.skyland

import org.bukkit.Bukkit
import org.bukkit.Material
import org.bukkit.block.data.BlockData
import ru.flinbein.laputa.structure.LaputaStructure
import ru.flinbein.laputa.structure.block.LaputaBlock
import ru.flinbein.laputa.structure.generator.LayerGenerator
import ru.flinbein.laputa.structure.generator.form.FormTags
import ru.flinbein.laputa.structure.generator.form.FormBaseBlockType
import ru.flinbein.laputa.structure.generator.platform.PlatformTags
import ru.flinbein.laputa.structure.generator.terrain.TerrainTags
import ru.flinbein.laputa.util.Perlin
import java.util.*
import kotlin.math.pow
import kotlin.math.roundToInt

class SkyIslandFormGenerator: LayerGenerator {


    var distancePower = 1.2
    val perlin = Perlin()

    var bottomBlockData: BlockData = Bukkit.createBlockData(Material.STONE)
    var middleBlockData: BlockData = Bukkit.createBlockData(Material.DIRT)
    var topBlockData: BlockData = Bukkit.createBlockData(Material.GRASS_BLOCK)
    var minHeight: Int = 3

    override fun fill(structure: LaputaStructure, random: Random) {
        val platformBlocks = structure.getBlocksWithTag(PlatformTags.PLATFORM)


        val perlinSeed = random.nextLong()
        perlin.setSeed(perlinSeed)

        platformBlocks.forEach { bl ->
            val terrainHeight = bl.getTagValue(TerrainTags.TERRAIN_PLATFORM_HEIGHT) as Int
            val terrainBlock = bl.getRelative(0,terrainHeight,0)

            val dist = bl.getTagValue(PlatformTags.ABYSS_DISTANCE) as Double
            val poweredDist = dist.pow(distancePower)
            val noise = perlin.noise(bl.x, bl.z)
            val middleCount = random.nextInt(1, minHeight +1)

            val totalHeight = (poweredDist+ minHeight + poweredDist*noise).roundToInt()

            for (dy in 1..totalHeight) {
                val block = terrainBlock.getRelative(0, -dy, 0)
                if (dy <= middleCount) {
                    block.setTag(FormTags.BASE, FormBaseBlockType.MIDDLE)
                } else {
                    block.setTag(FormTags.BASE, FormBaseBlockType.BOTTOM)
                }
                block.setTag(FormTags.DISTANCE_FROM_TERRAIN, -dy);
                block.abstract = false;
            }
            terrainBlock.setTag(FormTags.BASE, FormBaseBlockType.TOP)
            terrainBlock.setTag(FormTags.DISTANCE_FROM_TERRAIN, 0)
            terrainBlock.abstract = false;
        }
    }

    override fun handleBlock(block: LaputaBlock, random: Random, structure: LaputaStructure): BlockData? {
        val type = block.getTagValue(FormTags.BASE) ?: return null
        return when (type) {
            FormBaseBlockType.BOTTOM -> bottomBlockData
            FormBaseBlockType.MIDDLE -> middleBlockData
            FormBaseBlockType.TOP -> topBlockData
            else -> null
        }
    }
}