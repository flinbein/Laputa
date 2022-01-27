package ru.flinbein.laputa.util.range

import java.util.Random

interface IRange<T : Number> {
    fun getRandomValue(random: Random) : T;
}