package ru.flinbein.laputa.structure.generator

import ru.flinbein.laputa.structure.LaputaStructure
import java.util.*

interface LayerGenerator {

    public fun fill(structure: LaputaStructure, random: Random);
}