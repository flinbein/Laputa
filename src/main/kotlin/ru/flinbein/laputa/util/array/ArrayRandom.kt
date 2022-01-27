package ru.flinbein.laputa.util.array

import java.util.*

fun <T> List<T>.random(random: Random): T {
    val index = random.nextInt(0, this.size);
    return this[index]
}