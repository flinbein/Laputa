package ru.flinbein.laputa.structure.generator

import org.bukkit.block.data.BlockData
import ru.flinbein.laputa.structure.LaputaStructure
import ru.flinbein.laputa.structure.block.LaputaBlock
import ru.flinbein.laputa.structure.geometry.Point3D
import java.util.*

interface LayerGenerator {

    public fun fill(structure: LaputaStructure, random: Random);
    public fun getBlockData(block: LaputaBlock, random: Random, structure: LaputaStructure): BlockData? {
        return null;
    };
}