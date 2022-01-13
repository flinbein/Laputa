package ru.flinbein.laputa.structure.generator.terrain

import ru.flinbein.laputa.structure.LaputaStructure
import ru.flinbein.laputa.structure.generator.LayerGenerator
import ru.flinbein.laputa.structure.generator.platform.PlatformTags
import ru.flinbein.laputa.structure.generator.util.Perlin
import java.util.*
import kotlin.math.pow
import kotlin.math.roundToInt

class PerlinTerrainGenerator: LayerGenerator {

    var hillAmplitude = 5;
    var distancePower = 0.8;
    val perlin = Perlin();

    override fun fill(structure: LaputaStructure, random: Random) {
        val perlinSeed = random.nextLong();
        perlin.setSeed(perlinSeed);

        val platformBlocks = structure.getBlocksWithTag(PlatformTags.PLATFORM);
        val maxDistance = platformBlocks.map {
            it.getTagValue(PlatformTags.ABYSS_DISTANCE) as Double?
        }.filterNotNull().maxOrNull() ?: 1.0;

        val halfMaxDistance = maxDistance / 2;

        platformBlocks.forEach {
            val noise = perlin.noise(it.x, it.z);
            val dist = it.getTagValue(PlatformTags.ABYSS_DISTANCE) as Double;
            val poweredDist = dist.pow(distancePower);

            val dy = (poweredDist + noise*hillAmplitude - halfMaxDistance);
            val yDif = dy.roundToInt();
            it.setTag(TerrainTags.TERRAIN_HEIGHT, yDif);
            it.getRelative(0,yDif,0).setTag(TerrainTags.TERRAIN,dy);
        }
    }

}