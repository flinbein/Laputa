package ru.flinbein.laputa.structure.block

class CoordinateMap3D<KEY, VAL> {
    private val map: HashMap<KEY, VAL> = HashMap();

    fun get(point: KEY, defaultValue: () -> VAL): VAL {
        return map.getOrPut(point, defaultValue);
    }

    fun has(key: KEY): Boolean {
        return map.containsKey(key);
    }

    fun clear() {
        map.clear();
    }

    fun getValues(): List<VAL> {
        return map.values.toList();
    }
}