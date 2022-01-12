package ru.flinbein.laputa

import org.bukkit.plugin.java.JavaPlugin
class LaputaPlugin: JavaPlugin() {

    companion object {
        fun getPlugin(): LaputaPlugin {
            return getPlugin(LaputaPlugin::class.java)
        }
    }

    override fun onEnable() {
        super.onEnable()
    }
}