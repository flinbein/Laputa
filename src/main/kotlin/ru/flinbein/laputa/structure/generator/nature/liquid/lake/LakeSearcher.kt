package ru.flinbein.laputa.structure.generator.nature.liquid.lake

import ru.flinbein.laputa.LaputaPlugin
import ru.flinbein.laputa.structure.LaputaStructure
import ru.flinbein.laputa.structure.block.LaputaBlock
import ru.flinbein.laputa.structure.geometry.Point
import ru.flinbein.laputa.structure.geometry.Vector3D
import java.util.HashSet

class LakeSearcher(
    private val abyssPoints: List<Point>,
    private val structure: LaputaStructure,
    private val allowWaterfallBlockCount: Int = 0
) {

    fun search(point: Point, modifiableIgnoredPoints: HashSet<Point>): Lake? {
        val lake = Lake();
        lake.handledPoints.addAll(modifiableIgnoredPoints);
        fillLake(point, lake);
        modifiableIgnoredPoints.addAll(lake.handledPoints);
        if (lake.hasLevels()) return lake;
        return null;
    }

    private fun isPointAbyss(point: Point): Boolean {
        return abyssPoints.any { it.x == point.x && it.z == point.z }
    }

    private fun fillLake(
        point: Point,
        lake: Lake,
        onlyBelow: Boolean = false
    ): Boolean {
        val foundPoints = arrayListOf(point);
        var currentHandlingBlockPoints = HashSet(foundPoints);

        while (currentHandlingBlockPoints.size > 0) {
            lake.handledPoints.addAll(currentHandlingBlockPoints);

            val isMetAbyss = currentHandlingBlockPoints.any {
                isPointAbyss(it);
            }
            if (isMetAbyss) {
                LaputaPlugin.getPlugin().logger.info("MET ABYSS");
                return false;
            }
            // neighbors - only empty blocks (or non-existing blocks)
            val neighbors = currentHandlingBlockPoints.flatMap { p ->
                LaputaBlock.neighborVectorsStraight_Y2D.map { p.move(it) }
            }.filter { p ->
                structure.hasNonEmptyBlockAt(p).not()
                        && foundPoints.contains(p).not()
            }
            foundPoints.addAll(currentHandlingBlockPoints);
            currentHandlingBlockPoints = HashSet(neighbors);
        }

        val currentLevel = Lake.LakeLevel(foundPoints, y = point.y);

        // Search levels below

        LaputaPlugin.getPlugin().logger.info("SEARCH BELOW");
        val pointsToSearchBelow = foundPoints.map {
            it.move(Vector3D(0.0,-1.0,0.0))
        }.filter {
            lake.handledPoints.contains(it)
            && structure.hasNonEmptyBlockAt(it).not()
        }

        val belowHandledPoints = HashSet<Point>()
        pointsToSearchBelow.forEach {
            if (belowHandledPoints.contains(it)) return@forEach;
            val isFillable = fillLake(it, lake, onlyBelow = true) ;
            if (!isFillable) return false;
        }

        lake.addLevel(currentLevel);

        if (onlyBelow) return true;

        // search above
        LaputaPlugin.getPlugin().logger.info("SEARCH ABOVE")
        val pointsToSearchAbove = foundPoints.map {
            it.move(Vector3D(0.0,1.0,0.0))
        }
        pointsToSearchAbove.forEach {
            if (lake.handledPoints.contains(it)) return@forEach
            fillLake(it, lake, onlyBelow = false)
        }
        return true;
    }
}