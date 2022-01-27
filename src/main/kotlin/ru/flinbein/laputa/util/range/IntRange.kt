package ru.flinbein.laputa.util.range

import java.util.*
import kotlin.math.pow
import kotlin.math.roundToInt

data class IntRange(var min: Int, var max: Int) : IRange<Int> {
    override fun getRandomValue(random: Random): Int {
        return getRandomValue(random, 1.0);
    }

    fun getRandomValue(random: Random, randomPower: Double): Int {
        val rnd = random.nextDouble().pow(randomPower);
        return (min + rnd*max).roundToInt();
    }
}