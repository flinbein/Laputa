package ru.flinbein.laputa.structure.generator.nature.liquid.lake

import ru.flinbein.laputa.structure.geometry.Point

class Lake {

    private val levels: ArrayList<LakeLevel> = ArrayList();
    val handledPoints = HashSet<Point>();

    fun addLevel(level: LakeLevel) {
        levels.add(level);
    }

    fun hasLevels() = levels.isEmpty().not();

    fun getMinHeight(): Double {
        return levels.minOf { it.y };
    }

    fun getLevels(): List<LakeLevel> {
        return levels;
    }

    fun getHeight(): Double {
        if (levels.isEmpty()) return 0.0;
        val min = levels.minOf { it.y };
        val max = levels.maxOf { it.y };
        return max-min + 1.0
    }

    data class LakeLevel(val points: List<Point>, val y: Double) {
        fun getSize() = points.size;
    };
}