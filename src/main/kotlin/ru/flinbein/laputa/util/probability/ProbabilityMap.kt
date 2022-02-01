package ru.flinbein.laputa.util.probability

import ru.flinbein.laputa.LaputaPlugin
import java.util.*
import kotlin.collections.ArrayList
import kotlin.collections.HashMap

class ProbabilityMap<T>() {

    companion object {
        fun <ItemType> fromHashMap(map: HashMap<ItemType, Double>): ProbabilityMap<ItemType> {
            val probabilityMap = ProbabilityMap<ItemType>()
            return probabilityMap.addMap(map)
        }
    }

    private val itemsArray: ArrayList<T> = ArrayList()
    private val probabilitiesRangeArray: LinkedList<Array<Double>> = LinkedList()
    private var totalChanceSum: Double = 0.0


    fun addEntry(item: T, chance: Double): ProbabilityMap<T> {
        itemsArray.add(item)
        val lastRangeMaxValue = if (probabilitiesRangeArray.isEmpty()) {
            0.0
        } else {
            probabilitiesRangeArray.last()[1]
        }
        probabilitiesRangeArray.add(
            arrayOf(lastRangeMaxValue, lastRangeMaxValue + chance)
        )
        totalChanceSum += chance

        return this
    }

    fun addMap(map: HashMap<T, Double>): ProbabilityMap<T> {
        map.forEach {
            addEntry(it.key, it.value)
        }
        return this
    }

    fun addSubChanceMap(map: HashMap<T, Double>, chance: Double): ProbabilityMap<T> {
        map.forEach {
            addEntry(it.key, it.value / chance)
        }
        return this
    }

    fun getRandomItem(random: Random): T {
        val chance = random.nextDouble(0.0, totalChanceSum)
        val index = probabilitiesRangeArray.indexOfFirst {
            chance >= it[0] && chance < it[1]
        }
        return itemsArray[index]
    }

    fun clone(): ProbabilityMap<T> {
        val newMap = ProbabilityMap<T>();
        val clonedValues = this.probabilitiesRangeArray.map { it.clone() }
        newMap.itemsArray.addAll(this.itemsArray);
        newMap.probabilitiesRangeArray.addAll(clonedValues);
        newMap.totalChanceSum = totalChanceSum;
        return newMap;
    }
}