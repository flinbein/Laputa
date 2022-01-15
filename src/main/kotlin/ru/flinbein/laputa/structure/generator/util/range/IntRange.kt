package ru.flinbein.laputa.structure.generator.util.range

import java.util.*

data class IntRange(var min: Int, var max: Int) : IRange<Int> {
    override fun getRandomValue(random: Random): Int {
        return random.nextInt(min, max+1);
    }
}