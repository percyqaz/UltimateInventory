package me.Percyqaz.UltimateInventory;

import de.jeff_media.chestsort.api.ChestSortEvent;
import org.bukkit.NamespacedKey;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

public class ChestSortListener implements Listener
{
    UltimateInventory plugin;

    public ChestSortListener(UltimateInventory plugin)
    {
        this.plugin = plugin;
    }

    @EventHandler(priority = EventPriority.NORMAL)
    public void ChestSort(ChestSortEvent chestSortEvent)
    {
        NamespacedKey nbtKey = new NamespacedKey(plugin, "__shulkerbox_plugin");
        for (var itemStack : chestSortEvent.getInventory().getContents())
        {
            if (itemStack == null) continue;
            ItemMeta meta = itemStack.getItemMeta();
            PersistentDataContainer data = meta.getPersistentDataContainer();
            if(data.has(nbtKey, PersistentDataType.STRING))
            {
                plugin.getLogger().info("Prevented movement of a shulker box");
                chestSortEvent.setUnmovable(itemStack);
            }
        }
    }

}
