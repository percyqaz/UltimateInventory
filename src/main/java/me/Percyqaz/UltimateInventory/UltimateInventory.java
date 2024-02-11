package me.Percyqaz.UltimateInventory;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.PluginManager;
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

        PluginManager pm = getServer().getPluginManager();

        config.addDefault("enableShulkerbox", true);
        config.addDefault("enableEnderChest", true);
        config.addDefault("enableCraftingTable", true);
        if (isPaper)
        {
            config.addDefault("enableSmithingTable", true);
            config.addDefault("enableStoneCutter", true);
            config.addDefault("enableGrindstone", true);
            config.addDefault("enableCartographyTable", true);
            config.addDefault("enableLoom", true);
            config.addDefault("enableAnvil", false);
        }
        config.addDefault("usePermissions", false);

        config.options().copyDefaults(true);
        saveConfig();

        pm.registerEvents(new InventoryListener(this, config, isPaper), this);
        if (pm.getPlugin("ChestSort") != null)
        {
            pm.registerEvents(new ChestSortListener(this), this);
            this.getLogger().info("ChestSort detected, enabling compatibility support");
        }
    }
}
