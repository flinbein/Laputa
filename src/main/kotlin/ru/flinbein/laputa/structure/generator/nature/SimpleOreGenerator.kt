package ru.flinbein.laputa.structure.generator.nature

import org.bukkit.Bukkit
import org.bukkit.Material
import ru.flinbein.laputa.structure.LaputaStructure
import ru.flinbein.laputa.structure.block.LaputaBlock
import ru.flinbein.laputa.structure.generator.LayerGenerator
import ru.flinbein.laputa.structure.generator.form.FormTags
import ru.flinbein.laputa.structure.generator.form.NatureBaseBlockType
import ru.flinbein.laputa.util.array.random
import ru.flinbein.laputa.util.probability.ProbabilityMap
import ru.flinbein.laputa.util.range.IntRange
import java.util.*
import kotlin.collections.HashMap
import kotlin.math.roundToInt

class SimpleOreGenerator() : LayerGenerator {

    companion object {
        val DEFAULT_ORE_PROBABILITY_MAP = ProbabilityMap.fromHashMap(
            hashMapOf(
                Material.COPPER_ORE to 100.0,
                Material.COAL_ORE to 90.0,
                Material.IRON_ORE to 65.0,
                Material.GOLD_ORE to 35.0,
                Material.LAPIS_ORE to 15.0,
                Material.DIAMOND_ORE to 7.0,
                Material.EMERALD_ORE to 1.0
            )
        )
        val GROW_COUNT_MAP = hashMapOf(
            Material.COPPER_ORE to IntRange(4,9),
            Material.COAL_ORE to IntRange(2,7),
            Material.IRON_ORE to IntRange(1,4),
            Material.GOLD_ORE to IntRange(1,4),
            Material.LAPIS_ORE to IntRange(4,6),
            Material.DIAMOND_ORE to IntRange(0,3),
            Material.EMERALD_ORE to IntRange(0,2)
        )
    }

    var oreProbabilityMap = DEFAULT_ORE_PROBABILITY_MAP.clone();
    var growCountMap: HashMap<Material, IntRange> = GROW_COUNT_MAP;
    var coverage: Double = 0.3;


    override fun fill(structure: LaputaStructure, random: Random) {
        val baseBlocks = structure.getBlocksWithTag(FormTags.BASE).filter {
            it.getTagValue(FormTags.BASE) == NatureBaseBlockType.BOTTOM
        }.shuffled(random);
        val countOfOrePlaces = (baseBlocks.size * coverage).roundToInt();
        for (i in 0..countOfOrePlaces) {
            growOre(baseBlocks[i], random);
        }
    }

    private fun placeOre(block: LaputaBlock, ore: Material) {
        block.setTag(NatureTags.ORE, ore);
        block.blockData = Bukkit.createBlockData(ore);
    }

    private fun growOre(block: LaputaBlock, random: Random) {
        if (block.hasTag(NatureTags.ORE)) return;
        val oreType = oreProbabilityMap.getRandomItem(random);
        placeOre(block, oreType);
        val blocks = arrayListOf(block);
        val growTimes = growCountMap[oreType]?.getRandomValue(random, 3.0) ?: 0
        repeat(growTimes) {
            val filledBlock = blocks.random(random)
            val neighbors = filledBlock.getNeighbors(withVertical = true).filter {
                it.hasTag(FormTags.BASE) && !it.hasTag(NatureTags.ORE)
            };
            if (neighbors.isEmpty()) return@repeat
            val neighbor = neighbors.random(random);
            placeOre(neighbor, oreType)
            blocks.add(neighbor);
        }
    }
}