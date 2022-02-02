package ru.flinbein.laputa.structure.generator.nature.tree

import org.bukkit.*
import org.bukkit.block.BlockState
import org.bukkit.block.data.BlockData
import ru.flinbein.laputa.structure.LaputaStructure
import ru.flinbein.laputa.structure.block.LaputaBlock
import ru.flinbein.laputa.structure.generator.LayerGenerator
import ru.flinbein.laputa.structure.generator.form.FormBaseBlockType
import ru.flinbein.laputa.structure.generator.form.FormTags
import ru.flinbein.laputa.structure.generator.nature.NatureTags
import ru.flinbein.laputa.structure.geometry.Point
import ru.flinbein.laputa.structure.geometry.Vector3D
import java.util.*
import java.util.function.Predicate
import kotlin.collections.HashMap
import kotlin.math.roundToInt

class TreeGenerator : LayerGenerator {

    var coverage = 0.2;

    private fun generateTree(
        structure: LaputaStructure,
        point: Point,
        random: Random,
        treeType: TreeType
    ) {
        val world = Bukkit.getWorlds()[0];
        val loc = Location(world,0.0,0.0, 0.0);
        val blocksMap = HashMap<Point, BlockData>()

        val predicate = Predicate<BlockState> { bs ->
            val p = Point(
                bs.x - loc.x + point.x,
                bs.y - loc.y + point.y,
                bs.z - loc.z + point.z
            )
            blocksMap[p] = bs.blockData;
            return@Predicate false;
        }
        world.generateTree(loc, random, treeType, predicate);

        val hasAnyIntersection = blocksMap.any {
            it.value.material != Material.DIRT
                && structure.hasNonAbstractBlock(it.key)
        }
        if (hasAnyIntersection) return;

        blocksMap.forEach {
            val block = structure.getBlockAt(it.key);
            block.setTag(NatureTags.TREE, it.value);
            block.abstract = false;
        }
    }

    override fun fill(structure: LaputaStructure, random: Random) {
        val blocks = structure.getBlocksWithTag(FormTags.BASE).filter {
            it.getTagValue(FormTags.BASE) == FormBaseBlockType.TOP
            && structure.hasNonAbstractBlock(it.point.move(Vector3D(0.0,1.0,1.1))).not()
        }.shuffled(random)
        val count = (blocks.size * coverage).roundToInt();

        for ( i in 0 until count) {
            val point = blocks[i].point.move(Vector3D(0.0, 1.0, 0.0));
            generateTree(structure, point, random, TreeType.TREE);
        }
    }

    override fun handleBlock(block: LaputaBlock, random: Random, structure: LaputaStructure): BlockData? {
        val blockData = block.getTagValue(NatureTags.TREE) ?: return null;
        return blockData as BlockData;
    }
}