package ru.flinbein.laputa.structure.generator.skyland

import org.bukkit.Bukkit
import org.bukkit.Material
import org.bukkit.block.data.BlockData
import ru.flinbein.laputa.structure.LaputaStructure
import ru.flinbein.laputa.structure.block.NamespacedTag
import ru.flinbein.laputa.structure.generator.LayerGenerator
import ru.flinbein.laputa.structure.geometry.Point3D
import ru.flinbein.laputa.structure.geometry.shape.CircleShape
import ru.flinbein.laputa.structure.geometry.shape.UnionShape2D
import java.util.*
import kotlin.collections.ArrayList

class PlatformLayerGenerator : LayerGenerator {

    companion object {
        val PLATFORM_TAG = NamespacedTag("platform", "base");
        private val BLOCK_DATA = Bukkit.createBlockData(Material.GRASS);
    }

    private val minCircleCount: Int = 5;
    private val maxCircleCount: Int = 10;
    private val minCircleRadius: Double = 4.0;
    private val maxCircleRadius: Double = 7.0;

    override fun fill(structure: LaputaStructure, random: Random) {
        val shapes: ArrayList<CircleShape> = ArrayList();

        val baseCircleRad = random.nextDouble(minCircleRadius,maxCircleRadius);
        val baseShape = CircleShape(baseCircleRad);
        shapes.add(baseShape);

        val count = random.nextInt(minCircleCount, maxCircleCount+1);
        for (i in 0..count) {
            val circleIndex = random.nextInt(shapes.size);
            val curCircle = shapes[circleIndex];
            val rndPoint = curCircle.getRandomPoint(random);
            val rndRadius = random.nextDouble(minCircleRadius,maxCircleRadius);
            shapes.add(CircleShape(rndRadius, rndPoint))
        };
        val unionShape = UnionShape2D(*shapes.toTypedArray());


        val centerBlock = structure.getBlockAt(0,0,0);
        val blocks = centerBlock.getBlocksByShape(unionShape);
        blocks.forEach {
            it.addTags(PLATFORM_TAG)
        }

    }

    override fun getBlockData(structure: LaputaStructure, point: Point3D): BlockData? {
        if (structure.getBlockAt(point).hasTag(PLATFORM_TAG)) {
            return BLOCK_DATA;
        }
        return null;
    }
}