package ru.flinbein.laputa.structure.generator.platform

import ru.flinbein.laputa.structure.LaputaStructure
import ru.flinbein.laputa.structure.generator.LayerGenerator
import ru.flinbein.laputa.structure.geometry.shape.CircleShape
import ru.flinbein.laputa.structure.geometry.shape.UnionShape2D
import java.util.*
import kotlin.collections.ArrayList
import kotlin.math.floor
import kotlin.math.pow
import kotlin.math.roundToInt

class CirclePlatformGenerator : LayerGenerator {

    var minCircleCount: Int = 5;
    var maxCircleCount: Int = 10;
    var minCircleRadius: Double = 4.0;
    var maxCircleRadius: Double = 7.0;

    private fun getRandomCircle(random: Random, shapes: List<CircleShape>): CircleShape {
        val rnd = random.nextDouble().pow(0.25);
        val index = floor(rnd*shapes.size).toInt();
        return shapes[index];
//        val halfSize = shapes.size/2;
//
//        val halfIndex = (1 - random.nextDouble().pow(2)).roundToInt();
//        val indexInHalf = random.nextInt(shapes.size/2 + 1);
//        return shapes[halfIndex*halfSize + indexInHalf];
    }

    override fun fill(structure: LaputaStructure, random: Random) {
        val shapes: ArrayList<CircleShape> = ArrayList();

        val baseCircleRad = random.nextDouble(minCircleRadius,maxCircleRadius);
        val baseShape = CircleShape(baseCircleRad);
        shapes.add(baseShape);

        val count = random.nextInt(minCircleCount, maxCircleCount+1);
        for (i in 0 until count) {
            val curCircle = getRandomCircle(random, shapes)
            val rndPoint = curCircle.getRandomPoint(random);
            val rndRadius = random.nextDouble(minCircleRadius,maxCircleRadius);
            shapes.add(CircleShape(rndRadius, rndPoint))
        };
        val unionShape = UnionShape2D(*shapes.toTypedArray());


        val centerBlock = structure.getBlockAt(0,0,0);
        val platformBlocks = centerBlock.getBlocksByShape(unionShape);

        platformBlocks.forEach {
            it.addTag(PlatformTags.PLATFORM);
        }

        val abyssBlocks = platformBlocks.flatMap {
            it.getNeighbors()
        }.filter {
            it.isEmpty()
        }.distinct();

        abyssBlocks.forEach {
            it.addTag(PlatformTags.ABYSS)
        }

        platformBlocks.forEach distanceSearch@ { b ->
            var distance = Double.POSITIVE_INFINITY;
            for (emptyBlock in abyssBlocks) {
                val d = b.distance(emptyBlock);
                if (d < distance) distance = d;
                if (d == 1.0) break;
            }
            b.addTag(PlatformTags.ABYSS_DISTANCE, distance);
        }

    }
}