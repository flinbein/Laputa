package ru.flinbein.laputa.structure.generator.nature.liquid.lake

import org.bukkit.Bukkit
import org.bukkit.Material
import ru.flinbein.laputa.structure.LaputaStructure
import ru.flinbein.laputa.structure.generator.LayerGenerator
import ru.flinbein.laputa.structure.generator.form.FormTags
import ru.flinbein.laputa.structure.generator.form.NatureBaseBlockType
import ru.flinbein.laputa.structure.generator.nature.NatureTags
import ru.flinbein.laputa.structure.generator.platform.PlatformTags
import ru.flinbein.laputa.structure.geometry.Point
import ru.flinbein.laputa.structure.geometry.Vector3D
import java.util.*
import kotlin.collections.ArrayList
import kotlin.collections.HashSet

class LakeGenerator : LayerGenerator {

    companion object {
        val WATER_BLOCK_DATA = Bukkit.createBlockData(Material.WATER);
    }

    private fun fillLake(lake: Lake, structure: LaputaStructure, random: Random) {
        val maxHeight = lake.getHeight();
        val height = random.nextDouble(maxHeight);

        lake.getLevelBelowHeight(height).forEach {
            it.points.forEach{ p ->
                val block = structure.getBlockAt(p);
                block.setTag(NatureTags.LAKE);
                block.blockData = WATER_BLOCK_DATA;
            }
        }
    }

    override fun fill(structure: LaputaStructure, random: Random) {
        val topBlocks = structure.getBlocksWithTag(FormTags.BASE).filter {
            it.getTagValue(FormTags.BASE) == NatureBaseBlockType.TOP
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
        lakes.forEach{ fillLake(it, structure, random) }
    }


}