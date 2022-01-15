package ru.flinbein.laputa.structure.generator.util.range

import java.util.Random

interface IRange<T : Number> {
    fun getRandomValue(random: Random) : T;
}