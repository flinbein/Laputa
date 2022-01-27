package ru.flinbein.laputa.structure.generator.platform

import ru.flinbein.laputa.LaputaPlugin
import ru.flinbein.laputa.structure.LaputaStructure
import ru.flinbein.laputa.structure.block.LaputaBlock
import ru.flinbein.laputa.structure.generator.LayerGenerator
import ru.flinbein.laputa.util.range.DoubleRange
import ru.flinbein.laputa.util.range.IntRange
import ru.flinbein.laputa.structure.geometry.Point
import ru.flinbein.laputa.structure.geometry.Vector3D
import ru.flinbein.laputa.structure.geometry.shape.common.Shape
import ru.flinbein.laputa.structure.geometry.shape.common.UnionShape
import ru.flinbein.laputa.structure.geometry.shape.flat.CircleShape
import java.util.*
import kotlin.collections.ArrayList
import kotlin.math.sqrt

class TreePlatformGenerator : LayerGenerator {

    val sizeRange = DoubleRange(10.0,15.0)
    val innerCircleRadiusRange = DoubleRange(4.0,8.0)
    val pointCountRange = IntRange(10,20)
    var accuracy = 0.5
    var smoothCount = 4

    override fun fill(structure: LaputaStructure, random: Random) {
        val circle = CircleShape(sizeRange.getRandomValue(random))
        val curPointCount = pointCountRange.getRandomValue(random)

        val points = ArrayList<Point>()

        repeat(curPointCount) {
            val randomPoint = circle.getRandomPoint(random, 0.75)
            points.add(randomPoint)
        }

        val graph = buildGraph(points)
        val centerBlock = structure.getBlockAt(0,0,0)

        for (row in 0 until points.size) {
            for (col in row+1 until points.size) {
                val dist = graph[row][col]
                if (dist != null) {
                    val pointA = points[row]
                    val pointB = points[col]
                    fillLine(pointA,pointB,dist,centerBlock,random)
                }
            }
        }

    }

    private fun fillLine(pointA: Point, pointB: Point, dist: Double, centerBlock: LaputaBlock, random: Random) {
        val vector = Vector3D.betweenPoints(pointA, pointB) / (dist/accuracy)
        LaputaPlugin.getPlugin().logger.info("LINE FROM $pointA to $pointB V=$vector")
        val times = (dist / accuracy).toInt()
        var curPoint = pointA
        val shapes = ArrayList<Shape>()
        repeat(times + 1) {
            val radius = innerCircleRadiusRange.getRandomValue(random)
            val circle = CircleShape(radius, curPoint)
            val innerShapes = arrayListOf(circle)
            // smoother
            repeat(smoothCount) {
                val smoothRndPoint = circle.getRandomPoint(random, 0.25)
                val smoothRadius = innerCircleRadiusRange.getRandomValue(random) / 2
                innerShapes.add(CircleShape(smoothRadius, smoothRndPoint))
            }
            // Join smooth-circles and base circle
            shapes.add(UnionShape(*innerShapes.toTypedArray()))
            curPoint = curPoint.move(vector)
        }
        val unionShape = UnionShape(*shapes.toTypedArray())
        val blocks = centerBlock.getBlocksByShape(unionShape)
        blocks.forEach { it.setTag(PlatformTags.PLATFORM) }
    }

    private fun buildGraph(points: ArrayList<Point>) : Array<Array<Double?>> {
        val graph = Array<Array<Double?>>(points.size) {
            arrayOfNulls(points.size)
        }
        val centerPoint = Point.XZ(0.0,0.0)
        val startPoint = points.minByOrNull { centerPoint.getQuadDistanceTo(it) }!!

        val connectedPoints = arrayListOf(startPoint)
        val notConnectedPoints = arrayListOf(*points.toTypedArray())
        notConnectedPoints.remove(startPoint)

        // Build a graph
        while (connectedPoints.size < points.size) {
            var minDist: Double = Double.POSITIVE_INFINITY
            var selectedConnectedPoint: Point? = null
            var closestNotConnectedPoint: Point? = null
            // Search closest point to any of connected points
            for (ncPoint in notConnectedPoints) {
                for (cPoint in connectedPoints) {
                    val dist = cPoint.getQuadDistanceTo(ncPoint)
                    if (dist < minDist) {
                        minDist = dist
                        selectedConnectedPoint = cPoint
                        closestNotConnectedPoint = ncPoint
                    }
                }
            }
            val indexA = points.indexOf(closestNotConnectedPoint)
            val indexB = points.indexOf(selectedConnectedPoint)
            val sqrtDist = sqrt(minDist)
            graph[indexA][indexB] = sqrt(sqrtDist)
            graph[indexB][indexA] = sqrt(sqrtDist)
            connectedPoints.add(closestNotConnectedPoint!!)
            notConnectedPoints.remove(closestNotConnectedPoint)
        }
        return graph
    }

}