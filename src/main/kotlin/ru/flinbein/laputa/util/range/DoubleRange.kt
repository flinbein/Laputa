package ru.flinbein.laputa.util.range

import java.util.*

data class DoubleRange(var min: Double, var max: Double) : IRange<Double> {
    override fun getRandomValue(random: Random): Double {
        return random.nextDouble(min, max);
    }
}