package ru.flinbein.laputa.structure.generator.platform

import ru.flinbein.laputa.structure.LaputaStructure
import ru.flinbein.laputa.structure.generator.LayerGenerator
import java.util.*

class PlatformHoleGenerator : LayerGenerator {

    var minHoleCount: Int = 2;
    var maxHoleCount: Int = 5;
    var holeSmootherTimes: Int = 5;
    var minRadius: Double = 7.0;
    var maxRadius: Double = 10.0;

    fun getHoleShape(random: Random): Shape2D {
        val minR = minRadius/(holeSmootherTimes+1)
        val maxR = maxRadius/(holeSmootherTimes+1);

        val shapes = arrayListOf(CircleShape(random.nextDouble(minR,maxR)));
        for (i in 0 until holeSmootherTimes) {
            val circleIndex = random.nextInt(0, shapes.size);
            val curCircle = shapes[circleIndex];
            val rndPoint = curCircle.getRandomPoint(random);
            val rndRadius = random.nextDouble(minR,maxR);
            shapes.add(CircleShape(rndRadius, rndPoint))
        }
        return UnionShape2D(*shapes.toTypedArray())
    }

    override fun fill(structure: LaputaStructure, random: Random) {
        val platformBlock = structure.getBlocksWithTag(PlatformTags.PLATFORM);

        val holeCount = random.nextInt(minHoleCount, maxHoleCount+1);
        for (i in 0 until holeCount) {
            val blockIndex = random.nextInt(0, platformBlock.size);
            val block = platformBlock[blockIndex];

            val holeBlocks = block.getBlocksByShape(getHoleShape(random));
            holeBlocks.forEach {
                it.clear();
            }
        }
    }
}