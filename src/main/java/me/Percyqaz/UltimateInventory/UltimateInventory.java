package me.Percyqaz.UltimateInventory;

import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

public class UltimateInventory extends JavaPlugin {

    FileConfiguration config = getConfig();

    @Override
    public void onEnable() {

        boolean isPaper = false;
        try
        {
            Class.forName("com.destroystokyo.paper.utils.PaperPluginLogger");
            isPaper = true;
            this.getLogger().info("You are running PaperMC, some extra features are enabled");
        }
        catch (ClassNotFoundException e)
        {
            //https://www.spigotmc.org/threads/quick-question-about-posting-resources.394544/#post-3543896
            this.getLogger().info("You are not running PaperMC");
        }

        getServer().getPluginManager().registerEvents(new InventoryListener(this, config, isPaper), this);
        if (getServer().getPluginManager().getPlugin("ChestSort") != null)
        {
            getServer().getPluginManager().registerEvents(new ChestSortListener(this), this);
            this.getLogger().info("ChestSort detected, enabling compatibility support");
        }
    }
}
