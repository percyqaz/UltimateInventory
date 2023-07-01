package me.Percyqaz.Shulkerbox;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

public class Shulkerbox extends JavaPlugin {

    FileConfiguration config = getConfig();

    @Override
    public void onEnable() {

        boolean isPaper = false;
        try
        {
            Class.forName("com.destroystokyo.paper.utils.PaperPluginLogger");
            isPaper = true;
            this.getLogger().info("You are running PaperMC, all advanced features are enabled");
        }
        catch (ClassNotFoundException discard)
        {
            this.getLogger().info("You are not running PaperMC, some features have been disabled");
        }

        getServer().getPluginManager().registerEvents(new InventoryListener(this, config, isPaper), this);
    }
}
