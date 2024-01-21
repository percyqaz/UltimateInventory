package me.Percyqaz.UltimateInventory;

import java.util.*;

import org.bukkit.*;
import org.bukkit.block.ShulkerBox;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.inventory.*;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.*;
import org.bukkit.inventory.meta.BlockStateMeta;
import org.bukkit.inventory.meta.ItemMeta;

import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

public class InventoryListener implements Listener
{
    UltimateInventory plugin;
    FileConfiguration config;
    boolean isPaper;
    Map<UUID, ItemStack> openShulkerBoxes = new HashMap<>();

    boolean enableShulkerbox;
    boolean enableEnderChest;
    boolean enableCraftingTable;
    boolean enableSmithingTable;
    boolean enableStoneCutter;
    boolean enableGrindstone;
    boolean enableCartographyTable;
    boolean enableLoom;
    boolean enableAnvil;
    boolean enableEnchantingTable;

    boolean usePermissions;

    public InventoryListener(UltimateInventory plugin, FileConfiguration config, boolean isPaper) {
        this.config = config;
        this.plugin = plugin;
        this.isPaper = isPaper;

        enableShulkerbox = config.getBoolean("enableShulkerbox", true);
        enableEnderChest = config.getBoolean("enableEnderChest", true);
        enableCraftingTable = config.getBoolean("enableCraftingTable", true);
        if (isPaper)
        {
            enableSmithingTable = config.getBoolean("enableSmithingTable", true);
            enableStoneCutter = config.getBoolean("enableStoneCutter", true);
            enableGrindstone = config.getBoolean("enableGrindstone", true);
            enableCartographyTable = config.getBoolean("enableCartographyTable", true);
            enableLoom = config.getBoolean("enableLoom", true);
            enableAnvil = config.getBoolean("enableAnvil", false);
            enableEnchantingTable = config.getBoolean("enableEnchantingTable", false);
        }
        usePermissions = config.getBoolean("usePermissions", false);
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
            case WHITE_SHULKER_BOX:
                return true;
            default:
                return false;
        }
    }

    private boolean IsAnvil(Material material)
    {
        switch (material)
        {
            case ANVIL:
            case CHIPPED_ANVIL:
            case DAMAGED_ANVIL:
                return true;
            default:
                return false;
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

    private void ShowCraftingTable(HumanEntity player)
    {
        if (player.getOpenInventory().getType() == InventoryType.WORKBENCH)
        {
            return;
        }

        player.openWorkbench(null, true);
    }

    private void ShowStoneCutter(HumanEntity player)
    {
        if (player.getOpenInventory().getType() == InventoryType.STONECUTTER)
        {
            return;
        }

        player.openStonecutter(null, true);
    }

    private void ShowCartographyTable(HumanEntity player)
    {
        if (player.getOpenInventory().getType() == InventoryType.CARTOGRAPHY)
        {
            return;
        }

        player.openCartographyTable(null, true);
    }

    private void ShowLoom(HumanEntity player)
    {
        if (player.getOpenInventory().getType() == InventoryType.LOOM)
        {
            return;
        }

        player.openLoom(null, true);
    }

    private void ShowSmithingTable(HumanEntity player)
    {
        if (player.getOpenInventory().getType() == InventoryType.SMITHING)
        {
            return;
        }

        player.openSmithingTable(null, true);
    }

    private void ShowGrindstone(HumanEntity player)
    {
        if (player.getOpenInventory().getType() == InventoryType.GRINDSTONE)
        {
            return;
        }

        player.openGrindstone(null, true);
    }

    private void ShowAnvil(HumanEntity player)
    {
        if (player.getOpenInventory().getType() == InventoryType.ANVIL)
        {
            return;
        }

        player.openAnvil(null, true);
    }

    private void ShowEnchantingTable(HumanEntity player)
    {
        if (player.getOpenInventory().getType() == InventoryType.ENCHANTING)
        {
            return;
        }

        player.openEnchanting(null, true);
    }

    private void OpenShulkerbox(HumanEntity player, ItemStack shulkerItem)
    {
        // Don't open the box if already open (avoids a duplication bug)
        if (openShulkerBoxes.containsKey(player.getUniqueId()) && openShulkerBoxes.get(player.getUniqueId()).equals(shulkerItem))
        {
            return;
        }

        // Added NBT for "locking" to prevent stacking shulker boxes
        ItemMeta meta = shulkerItem.getItemMeta();
        PersistentDataContainer data = meta.getPersistentDataContainer();
        NamespacedKey nbtKey = new NamespacedKey(plugin, "__shulkerbox_plugin");
        if(!data.has(nbtKey, PersistentDataType.STRING))
        {
            data.set(nbtKey, PersistentDataType.STRING, String.valueOf(System.currentTimeMillis()));
            shulkerItem.setItemMeta(meta);
        }

        Inventory shulker_inventory = ((ShulkerBox)((BlockStateMeta)meta).getBlockState()).getSnapshotInventory();
        Inventory inventory;
        if (!meta.hasDisplayName())
        {
            inventory = Bukkit.createInventory(null, InventoryType.SHULKER_BOX);
        }
        else
        {
            inventory = Bukkit.createInventory(null, InventoryType.SHULKER_BOX, meta.getDisplayName());
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

        // Delete NBT for "locking" to prevent stacking shulker boxes
        PersistentDataContainer data = meta.getPersistentDataContainer();
        NamespacedKey nbtKey = new NamespacedKey(plugin, "__shulkerbox_plugin");
        if(data.has(nbtKey, PersistentDataType.STRING))
        {
            data.remove(nbtKey);
        }

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

        if (!(clickedInventory == InventoryType.PLAYER || clickedInventory == InventoryType.ENDER_CHEST || clickedInventory == InventoryType.SHULKER_BOX || clickedInventory == InventoryType.CREATIVE))
        {
            return;
        }

        ItemStack item = e.getCurrentItem();
        if (item == null)
        {
            return;
        }

        if (item.getAmount() != 1)
        {
            return;
        }

        Material itemType = item.getType();
        HumanEntity player = e.getWhoClicked();

        if (clickedInventory != InventoryType.SHULKER_BOX
                && IsShulkerBox(itemType)
                && enableShulkerbox
                && (!usePermissions || player.hasPermission("ultimateinventory.shulkerbox")))
        {
            Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(
                    plugin,
                    () -> OpenShulkerbox(player, item)
            );
            e.setCancelled(true);
        }

        if (itemType == Material.ENDER_CHEST
                && enableEnderChest
                && (!usePermissions || player.hasPermission("ultimateinventory.enderchest")))
        {
            Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(
                    plugin,
                    () -> ShowEnderchest(player)
            );
            e.setCancelled(true);
        }

        if (itemType == Material.CRAFTING_TABLE
                && enableCraftingTable
                && (!usePermissions || player.hasPermission("ultimateinventory.craftingtable")))
        {
            Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(
                    plugin,
                    () -> ShowCraftingTable(player)
            );
            e.setCancelled(true);
        }

        if (isPaper)
        {
            if (itemType == Material.STONECUTTER
                    && enableStoneCutter
                    && (!usePermissions || player.hasPermission("ultimateinventory.stonecutter")))
            {
                Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(
                        plugin,
                        () -> ShowStoneCutter(player)
                );
                e.setCancelled(true);
            }

            if (itemType == Material.CARTOGRAPHY_TABLE
                    && enableCartographyTable
                    && (!usePermissions || player.hasPermission("ultimateinventory.cartographytable")))
            {
                Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(
                        plugin,
                        () -> ShowCartographyTable(player)
                );
                e.setCancelled(true);
            }

            if (itemType == Material.LOOM
                    && enableLoom
                    && (!usePermissions || player.hasPermission("ultimateinventory.loom")))
            {
                Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(
                        plugin,
                        () -> ShowLoom(player)
                );
                e.setCancelled(true);
            }

            if (itemType == Material.SMITHING_TABLE
                    && enableSmithingTable
                    && (!usePermissions || player.hasPermission("ultimateinventory.smithingtable")))
            {
                Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(
                        plugin,
                        () -> ShowSmithingTable(player)
                );
                e.setCancelled(true);
            }

            if (itemType == Material.GRINDSTONE
                    && enableGrindstone
                    && (!usePermissions || player.hasPermission("ultimateinventory.grindstone")))
            {
                Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(
                        plugin,
                        () -> ShowGrindstone(player)
                );
                e.setCancelled(true);
            }

            if (IsAnvil(itemType)
                    && enableAnvil
                    && (!usePermissions || player.hasPermission("ultimateinventory.anvil")))
            {
                Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(
                        plugin,
                        () -> ShowAnvil(player)
                );
                e.setCancelled(true);
            }

            if (itemType == Material.ENCHANTING_TABLE
                    && enableEnchantingTable
                    && (!usePermissions || player.hasPermission("ultimateinventory.enchantingtable")))
            {
                Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(
                        plugin,
                        () -> ShowEnchantingTable(player)
                );
                e.setCancelled(true);
            }
        }
    }

    @EventHandler(priority = EventPriority.NORMAL)
    public void RightClick(PlayerInteractEvent e)
    {
        if (e.getHand() != EquipmentSlot.HAND || e.getAction() != Action.RIGHT_CLICK_AIR)
        {
            return;
        }

        Player player = e.getPlayer();
        ItemStack item = player.getInventory().getItemInMainHand();
        Material itemType = item.getType();

        if (IsShulkerBox(itemType)
                && item.getAmount() == 1
                && enableShulkerbox
                && (!usePermissions || player.hasPermission("ultimateinventory.shulkerbox")))
        {
            Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(
                    plugin,
                    () -> OpenShulkerbox(player, item)
            );
            e.setCancelled(true);
        }

        if (itemType == Material.ENDER_CHEST
                && enableEnderChest
                && (!usePermissions || player.hasPermission("ultimateinventory.enderchest")))
        {
            Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(
                    plugin,
                    () -> ShowEnderchest(player)
            );
            e.setCancelled(true);
        }

        if (itemType == Material.CRAFTING_TABLE
                && enableCraftingTable
                && (!usePermissions || player.hasPermission("ultimateinventory.craftingtable")))
        {
            Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(
                    plugin,
                    () -> ShowCraftingTable(player)
            );
            e.setCancelled(true);
        }

        if (isPaper)
        {
            if (itemType == Material.STONECUTTER
                    && enableStoneCutter
                    && (!usePermissions || player.hasPermission("ultimateinventory.stonecutter")))
            {
                Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(
                        plugin,
                        () -> ShowStoneCutter(player)
                );
                e.setCancelled(true);
            }

            if (itemType == Material.CARTOGRAPHY_TABLE
                    && enableCartographyTable
                    && (!usePermissions || player.hasPermission("ultimateinventory.cartographytable")))
            {
                Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(
                        plugin,
                        () -> ShowCartographyTable(player)
                );
                e.setCancelled(true);
            }

            if (itemType == Material.LOOM
                    && enableLoom
                    && (!usePermissions || player.hasPermission("ultimateinventory.loom")))
            {
                Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(
                        plugin,
                        () -> ShowLoom(player)
                );
                e.setCancelled(true);
            }

            if (itemType == Material.SMITHING_TABLE
                    && enableSmithingTable
                    && (!usePermissions || player.hasPermission("ultimateinventory.smithingtable")))
            {
                Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(
                        plugin,
                        () -> ShowSmithingTable(player)
                );
                e.setCancelled(true);
            }

            if (itemType == Material.GRINDSTONE
                    && enableGrindstone
                    && (!usePermissions || player.hasPermission("ultimateinventory.grindstone")))
            {
                Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(
                        plugin,
                        () -> ShowGrindstone(player)
                );
                e.setCancelled(true);
            }

            if (IsAnvil(itemType)
                    && enableAnvil
                    && (!usePermissions || player.hasPermission("ultimateinventory.anvil")))
            {
                Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(
                        plugin,
                        () -> ShowAnvil(player)
                );
                e.setCancelled(true);
            }

            if (itemType == Material.ENCHANTING_TABLE
                    && enableEnchantingTable
                    && (!usePermissions || player.hasPermission("ultimateinventory.enchantingtable")))
            {
                Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(
                        plugin,
                        () -> ShowEnchantingTable(player)
                );
                e.setCancelled(true);
            }
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

    // Needs to close shulker box before items drop on death to avoid a duplication bug
    @EventHandler(priority = EventPriority.HIGHEST)
    public void Death(PlayerDeathEvent e)
    {
        Player player = e.getEntity();
        if (openShulkerBoxes.containsKey(player.getUniqueId()))
        {
            CloseShulkerbox(player);
        }
    }
}
