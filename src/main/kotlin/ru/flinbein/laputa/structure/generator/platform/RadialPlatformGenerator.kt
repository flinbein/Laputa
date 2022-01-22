package ru.flinbein.laputa.structure.generator.platform

import ru.flinbein.laputa.structure.LaputaStructure
import ru.flinbein.laputa.structure.generator.LayerGenerator
import ru.flinbein.laputa.structure.generator.util.Perlin
import ru.flinbein.laputa.structure.generator.util.range.DoubleRange
import java.util.*
import kotlin.math.atan2
import kotlin.math.cos
import kotlin.math.pow
import kotlin.math.sin

class RadialPlatformGenerator : LayerGenerator {

    val sizeRange = DoubleRange(40.0,50.0);
    var noisePower: Double = 0.3;

    val perlin = Perlin();

    private fun getNoiseAtCirclePoint(x: Double, z: Double): Double {
        val noise = perlin.noise(x,z);
        val normalized = (noise+1)/2;
        return normalized.pow(noisePower);
    }

    override fun fill(structure: LaputaStructure, random: Random) {
        val radius = sizeRange.getRandomValue(random);
        perlin.setSeed(random.nextLong());

        val shape = object: Shape2D {
            override val box2D: Box2D = Box2D(
                -radius,-radius,
                +radius,+radius
            )

            override fun includes(x: Double, z: Double): Boolean {
                val polarRadians = atan2(z,x);
                val polarLength = Vector2D(x,z).getLength();

                val circlePosX = cos(polarRadians)*radius;
                val circlePosZ = sin(polarRadians)*radius;
                val noiseAtPoint = getNoiseAtCirclePoint(circlePosX, circlePosZ)
                return polarLength <= noiseAtPoint*radius;
            }
        }
        val blocks = structure.getBlockAt(0,0,0).getBlocksByShape(shape);
        blocks.forEach {
            it.setTag(PlatformTags.PLATFORM)
        }
    }
}