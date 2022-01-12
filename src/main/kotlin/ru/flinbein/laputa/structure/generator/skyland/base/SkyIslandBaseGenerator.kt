package ru.flinbein.laputa.structure.generator.skyland.base

import org.bukkit.Bukkit
import org.bukkit.Material
import org.bukkit.block.data.BlockData
import ru.flinbein.laputa.LaputaPlugin
import ru.flinbein.laputa.structure.LaputaStructure
import ru.flinbein.laputa.structure.block.LaputaBlock
import ru.flinbein.laputa.structure.generator.LayerGenerator
import ru.flinbein.laputa.structure.generator.platform.PlatformTags
import ru.flinbein.laputa.structure.generator.skyland.SkyIslandTags
import ru.flinbein.laputa.structure.generator.terrain.TerrainTags
import ru.flinbein.laputa.structure.generator.util.Perlin
import java.util.*
import kotlin.math.pow
import kotlin.math.roundToInt

class SkyIslandBaseGenerator: LayerGenerator {


    var distancePower = 1.2;
    val perlin = Perlin();


    companion object {
        private val MIN_HEIGHT = 3;

        private val BLOCK_DATA_STONE = Bukkit.createBlockData(Material.STONE);
        private val BLOCK_DATA_DIRT = Bukkit.createBlockData(Material.DIRT);
        private val BLOCK_DATA_GRASS = Bukkit.createBlockData(Material.GRASS_BLOCK);
    }

    override fun fill(structure: LaputaStructure, random: Random) {
        val platformBlocks = structure.getBlocksWithTag(PlatformTags.PLATFORM);


        val perlinSeed = random.nextLong();
        perlin.setSeed(perlinSeed);

        platformBlocks.forEach { bl ->
            val terrainBlock = structure.getHighestBlockWithTag(bl.x, bl.z, TerrainTags.TERRAIN) ?: return@forEach;

            val dist = bl.getTagValue(PlatformTags.ABYSS_DISTANCE) as Double;
            val poweredDist = dist.pow(distancePower);
            val noise = perlin.noise(bl.x, bl.z);
            val middleCount = random.nextInt(1, MIN_HEIGHT +1);

            val totalHeight = (poweredDist+ MIN_HEIGHT + poweredDist*noise).roundToInt();

            for (dy in 1..totalHeight) {
                val block = terrainBlock.getRelative(0, -dy, 0);
                val type = if (dy <= middleCount)
                    SkyIslandBaseType.MIDDLE
                else
                    SkyIslandBaseType.BOTTOM
                ;
                block.addTag(SkyIslandTags.BASE, type);
            }
            terrainBlock.addTag(SkyIslandTags.BASE, SkyIslandBaseType.TOP)
        }
    }

    override fun getBlockData(block: LaputaBlock, random: Random, structure: LaputaStructure): BlockData? {
        val type = (block.getTagValue(SkyIslandTags.BASE) as SkyIslandBaseType?) ?: return null;

        if (type == SkyIslandBaseType.BOTTOM) return BLOCK_DATA_STONE;
        if (type == SkyIslandBaseType.MIDDLE) return BLOCK_DATA_DIRT;
        if (type == SkyIslandBaseType.TOP) return BLOCK_DATA_GRASS;
        return null;
    }
}