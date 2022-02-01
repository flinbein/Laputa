package ru.flinbein.laputa.structure.generator.nature.liquid.lake

import org.bukkit.Bukkit
import org.bukkit.Material
import org.bukkit.block.data.BlockData
import ru.flinbein.laputa.structure.LaputaStructure
import ru.flinbein.laputa.structure.block.LaputaBlock
import ru.flinbein.laputa.structure.generator.LayerGenerator
import ru.flinbein.laputa.structure.generator.form.FormTags
import ru.flinbein.laputa.structure.generator.form.FormBaseBlockType
import ru.flinbein.laputa.structure.generator.nature.NatureTags
import ru.flinbein.laputa.structure.generator.platform.PlatformTags
import ru.flinbein.laputa.structure.geometry.Point
import ru.flinbein.laputa.structure.geometry.Vector3D
import java.util.*
import kotlin.collections.ArrayList
import kotlin.collections.HashSet

class LakeGenerator : LayerGenerator {

    companion object {
        private val WATER_BLOCK_DATA = Bukkit.createBlockData(Material.WATER);
    }

    private fun fillLake(lake: Lake, structure: LaputaStructure, random: Random) {
        val minHeight = lake.getMinHeight();

        lake.getLevels().forEach {
            it.points.forEach{ p ->
                val block = structure.getBlockAt(p);
                block.setTag(NatureTags.LAKE, it.y - minHeight); // ToDo make y relative to current lake
                block.abstract = false;
                val blockBelow = block.getRelative(0.0, -1.0,0.0);
                if (blockBelow.hasTag(FormTags.BASE)) {
                    blockBelow.setTag(FormTags.BASE, FormBaseBlockType.MIDDLE);
                }
            }
        }
    }

    override fun fill(structure: LaputaStructure, random: Random) {
        val topBlocks = structure.getBlocksWithTag(FormTags.BASE).filter {
            it.getTagValue(FormTags.BASE) == FormBaseBlockType.TOP
        }
        val abyssPoints = structure.getBlocksWithTag(PlatformTags.ABYSS).map { it.point }

        val lakeSearcher = LakeSearcher(abyssPoints, structure);
        val sortedBlocks = topBlocks.sortedBy { it.y };

        val lakes = ArrayList<Lake>();
        val handledBlocks = HashSet<Point>();
        for (block in sortedBlocks) {
            val point = block.point.move(Vector3D(0.0, 1.0, 0.0));
            if (handledBlocks.contains(point)) continue;
            val lake = lakeSearcher.search(point, handledBlocks) ?: continue;
            lakes.add(lake);
        }
        val maxLakeHeight = lakes.maxOf { it.getHeight() };
        for (lake in lakes) {
            val chance = lake.getHeight() / maxLakeHeight * 0.7;
            if (chance < random.nextDouble()) continue;
            fillLake(lake, structure, random)
        }
    }

    override fun handleBlock(block: LaputaBlock, random: Random, structure: LaputaStructure): BlockData? {
        if (!block.hasTag(NatureTags.LAKE)) return null;
        return WATER_BLOCK_DATA
    }
}