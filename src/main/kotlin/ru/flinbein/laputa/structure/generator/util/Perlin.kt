package ru.flinbein.laputa.structure.generator.util

import org.bukkit.util.noise.PerlinNoiseGenerator
import java.awt.geom.Point2D

class Perlin() {
    var amplitude: Double = 5.0;
    var frequency: Double = 1.01;
    var octaves: Int = 10;
    var zoom: Double = 0.05;

    private lateinit var perlinNoiseGenerator: PerlinNoiseGenerator;

    fun setSeed(seed: Long) {
        perlinNoiseGenerator = PerlinNoiseGenerator(seed);
    }

    fun noise(x: Double, y: Double): Double {
        return perlinNoiseGenerator.noise(x * zoom,y * zoom, octaves, frequency, amplitude,true);
    }

    fun noise(point2D: Point2D): Double {
        return noise(point2D.x, point2D.y);
    }
}