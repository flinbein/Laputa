package ru.flinbein.laputa.structure.block

import ru.flinbein.laputa.structure.geometry.Point3D

class CoordinateMap3D {
    private val map: HashMap<Point3D, LaputaBlock> = HashMap();

    fun get(x: Double, y: Double, z: Double, defaultValue: () -> LaputaBlock): LaputaBlock {
        return map.getOrPut(Point3D(x,y,z), defaultValue);
    }
    fun get(point: Point3D, defaultValue: () -> LaputaBlock): LaputaBlock {
        return map.getOrPut(point, defaultValue);
    }

    fun clear() {
        map.clear();
    }

    fun getValues(): List<LaputaBlock> {
        return map.values.toList();
    }
}