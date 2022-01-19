package ru.flinbein.laputa.structure.generator.platform

import ru.flinbein.laputa.structure.LaputaStructure
import ru.flinbein.laputa.structure.block.LaputaBlock
import ru.flinbein.laputa.structure.generator.LayerGenerator
import ru.flinbein.laputa.structure.generator.util.range.DoubleRange
import ru.flinbein.laputa.structure.generator.util.range.IntRange
import ru.flinbein.laputa.structure.geometry.Point3D
import ru.flinbein.laputa.structure.geometry.shape.CircleShape
import ru.flinbein.laputa.structure.geometry.shape.UnionShape2D
import java.util.*
import kotlin.collections.ArrayList
import kotlin.math.floor
import kotlin.math.pow

class MultiPlatformGenerator : LayerGenerator {

    val circleCountRange = IntRange(5,10);
    val circleRadiusRange = DoubleRange(4.0,7.0);
    var levelCount = IntRange(1,1)

    private fun getRandomCircle(random: Random, shapes: List<CircleShape>): CircleShape {
        val rnd = random.nextDouble().pow(0.25);
        val index = floor(rnd*shapes.size).toInt();
        return shapes[index];
    }

    private fun getPlatformBlocks(point: Point3D, circleCount: Int, random: Random, structure: LaputaStructure): List<LaputaBlock> {
        val shapes: ArrayList<CircleShape> = arrayListOf(
            CircleShape(0.0)
        );
        for (i in 0 until circleCount) {
            val curCircle = getRandomCircle(random, shapes)
            val rndPoint = curCircle.getRandomPoint(random);
            val rndRadius = circleRadiusRange.getRandomValue(random);
            shapes.add(CircleShape(rndRadius, rndPoint))
        };
        val unionShape = UnionShape2D(*shapes.toTypedArray());
        val centerBlock = structure.getBlockAt(point);
        return centerBlock.getBlocksByShape(unionShape);
    }

    override fun fill(structure: LaputaStructure, random: Random) {
        val shapes: ArrayList<CircleShape> = ArrayList();

        val baseCircleRad = circleRadiusRange.getRandomValue(random);
        val baseShape = CircleShape(baseCircleRad);
        shapes.add(baseShape);

        val count = circleCountRange.getRandomValue(random);
        for (i in 0 until count) {
            val curCircle = getRandomCircle(random, shapes)
            val rndPoint = curCircle.getRandomPoint(random);
            val rndRadius = circleRadiusRange.getRandomValue(random);
            shapes.add(CircleShape(rndRadius, rndPoint))
        };
        val unionShape = UnionShape2D(*shapes.toTypedArray());


        val centerBlock = structure.getBlockAt(0,0,0);
        val platformBlocks = centerBlock.getBlocksByShape(unionShape);

        platformBlocks.forEach {
            it.setTag(PlatformTags.PLATFORM, 0.0);
        }

    }
}