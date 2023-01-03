package me.Percyqaz.Shulkerbox;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.SoundCategory;
import org.bukkit.block.ShulkerBox;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.inventory.*;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BlockStateMeta;
import org.bukkit.inventory.meta.ItemMeta;

public class InventoryListener implements Listener
{
    Shulkerbox plugin;
    FileConfiguration config;
    Map<UUID, ItemStack> openShulkerBoxes = new HashMap<>();

    public InventoryListener(Shulkerbox plugin, FileConfiguration config)
    {
        this.config = config;
        this.plugin = plugin;
    }

    private boolean IsShulkerBox(Material material)
    {
        switch (material)
        {
            case SHULKER_BOX:
            case RED_SHULKER_BOX:
            case MAGENTA_SHULKER_BOX:
            case PINK_SHULKER_BOX:
            case PURPLE_SHULKER_BOX:
            case YELLOW_SHULKER_BOX:
            case ORANGE_SHULKER_BOX:
            case LIME_SHULKER_BOX:
            case GREEN_SHULKER_BOX:
            case CYAN_SHULKER_BOX:
            case BLUE_SHULKER_BOX:
            case LIGHT_BLUE_SHULKER_BOX:
            case LIGHT_GRAY_SHULKER_BOX:
            case GRAY_SHULKER_BOX:
            case BROWN_SHULKER_BOX:
            case BLACK_SHULKER_BOX:
            case WHITE_SHULKER_BOX: return true;
            default: return false;
        }
    }

    private void ShowEnderchest(HumanEntity player)
    {
        if (player.getOpenInventory().getType() == InventoryType.ENDER_CHEST)
        {
            player.closeInventory();
            Bukkit.getServer().getPlayer(player.getUniqueId()).playSound(player, Sound.BLOCK_ENDER_CHEST_CLOSE, SoundCategory.BLOCKS, 1.0f, 1.2f);
        }
        else
        {
            player.openInventory(player.getEnderChest());
            Bukkit.getServer().getPlayer(player.getUniqueId()).playSound(player, Sound.BLOCK_ENDER_CHEST_OPEN, SoundCategory.BLOCKS, 1.0f, 1.2f);
        }
    }

    private void OpenShulkerbox(HumanEntity player, ItemStack shulkerItem)
    {
        // Don't open the box if already open (avoids a duplication bug)
        if (openShulkerBoxes.containsKey(player.getUniqueId()) && openShulkerBoxes.get(player.getUniqueId()).equals(shulkerItem))
        {
            return;
        }

        Inventory shulker_inventory = ((ShulkerBox)((BlockStateMeta)shulkerItem.getItemMeta()).getBlockState()).getSnapshotInventory();
        String displayName = shulkerItem.getItemMeta().getDisplayName();
        Inventory inventory;
        if (displayName.isEmpty())
        {
            inventory = Bukkit.createInventory(null, InventoryType.SHULKER_BOX);
        }
        else
        {
            inventory = Bukkit.createInventory(null, InventoryType.SHULKER_BOX, displayName);
        }
        inventory.setContents(shulker_inventory.getContents());

        player.openInventory(inventory);
        Bukkit.getServer().getPlayer(player.getUniqueId()).playSound(player, Sound.BLOCK_SHULKER_BOX_OPEN, SoundCategory.BLOCKS, 1.0f, 1.2f);

        openShulkerBoxes.put(player.getUniqueId(), shulkerItem);
    }

    private void CloseShulkerbox(HumanEntity player)
    {
        ItemStack shulkerItem = openShulkerBoxes.get(player.getUniqueId());
        BlockStateMeta meta = (BlockStateMeta)shulkerItem.getItemMeta();
        ShulkerBox shulkerbox = (ShulkerBox)meta.getBlockState();
        shulkerbox.getInventory().setContents(player.getOpenInventory().getTopInventory().getContents());

        meta.setBlockState(shulkerbox);
        shulkerItem.setItemMeta(meta);

        Bukkit.getServer().getPlayer(player.getUniqueId()).playSound(player, Sound.BLOCK_SHULKER_BOX_CLOSE, SoundCategory.BLOCKS, 1.0f, 1.2f);

        openShulkerBoxes.remove(player.getUniqueId());
    }

    @EventHandler(priority = EventPriority.LOW)
    public void InventoryClick(InventoryClickEvent e)
    {
        if (e.getAction() == InventoryAction.NOTHING)
        {
            return;
        }

        if (!e.isRightClick() || e.isShiftClick())
        {
            // Prevent moving or modifying shulker box stacks while a shulker box is open
            if (openShulkerBoxes.containsKey(e.getWhoClicked().getUniqueId()) &&
                    (
                            e.getAction() == InventoryAction.HOTBAR_SWAP
                                    || e.getAction() == InventoryAction.HOTBAR_MOVE_AND_READD
                                    || (e.getCurrentItem() != null && IsShulkerBox(e.getCurrentItem().getType()))
                    )
            )
            {
                e.setCancelled(true);
            }

            return;
        }

        InventoryType clickedInventory = e.getClickedInventory().getType();

        if (!(clickedInventory == InventoryType.PLAYER || clickedInventory == InventoryType.ENDER_CHEST || clickedInventory == InventoryType.CREATIVE))
        {
            return;
        }

        ItemStack item = e.getCurrentItem();
        Material itemType = item.getType();

        if (itemType == Material.ENDER_CHEST)
        {
            Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(
                    plugin,
                    () -> ShowEnderchest(e.getWhoClicked())
            );
            e.setCancelled(true);
        }

        if (IsShulkerBox(itemType))
        {
            Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(
                    plugin,
                    () -> OpenShulkerbox(e.getWhoClicked(), item)
            );
            e.setCancelled(true);
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void InventoryClose(InventoryCloseEvent e)
    {
        if (openShulkerBoxes.containsKey(e.getPlayer().getUniqueId()))
        {
            CloseShulkerbox(e.getPlayer());
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void Death(PlayerDeathEvent e)
    {
        Player player = e.getEntity();
        if (openShulkerBoxes.containsKey(player.getUniqueId()))
        {
            // Needs to happen before items drop on death to avoid a duplication bug
            CloseShulkerbox(player);
        }
    }
}
