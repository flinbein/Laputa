package ru.flinbein.laputa.structure.generator.platform

import ru.flinbein.laputa.structure.LaputaStructure
import ru.flinbein.laputa.structure.generator.LayerGenerator
import java.util.*

class PlatformAbyssTagGenerator : LayerGenerator {

    override fun fill(structure: LaputaStructure, random: Random) {
        val platformBlocks = structure.getBlocksWithTag(PlatformTags.PLATFORM)

        val abyssBlocks = platformBlocks.flatMap {
            it.getNeighbors()
        }.filter {
            it.isEmpty()
        }.distinct()

        abyssBlocks.forEach {
            it.setTag(PlatformTags.ABYSS)
        }

        platformBlocks.forEach distanceSearch@ { b ->
            var distance = Double.POSITIVE_INFINITY
            for (emptyBlock in abyssBlocks) {
                val d = b.distance(emptyBlock)
                if (d < distance) distance = d
                if (d == 1.0) break
            }
            b.setTag(PlatformTags.ABYSS_DISTANCE, distance)
        }
    }
}