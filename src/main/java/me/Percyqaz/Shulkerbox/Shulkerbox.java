package me.Percyqaz.Shulkerbox;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

public class Shulkerbox extends JavaPlugin {

    FileConfiguration config = getConfig();

    @Override
    public void onEnable() {
        this.getLogger().info("Activated");
        getServer().getPluginManager().registerEvents(new InventoryListener(this, config), this);
    }
}
