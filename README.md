# Shulkerbox
 Minecraft/Spigot plugin to streamline inventory management

Features:
- Right-click on an ender chest in your inventory to open your ender chest
- Right-clicking again closes your ender chest
- Right-click on a shulker box in your inventory OR in your ender chest to open it

Anti duplication measures against:
- Dropping the shulker box
- Moving it around
- Dying for any reason
- Disconnecting for any reason

After these measures I have not been able to find any duplication exploits yet after trying every trick I could think of

Here are ways you could create a duplication bug:
- Using a plugin or mod that moves items around in a player's inventory while they have a shulker box open
- Using a plugin or mod that lets you see into a player's inventory or enderchest such as /invsee from Essentials

Please consider your plugins carefully, I take no responsibility in these cases as my only goal was to have no duplication bugs when this plugin is on its own

If you find any duplication bugs please immediately report them [here](https://github.com/percyqaz/Shulkerbox/issues)

Future features and known issues:
- Issue: Doesn't work in the creative inventory. If this matters to you that's very unfortunate as this is written for a private survival server
- Issue: You can put your enderchest into your enderchest and lose it if you only had one. Try not to do that
- Feature: Config to toggle on/off shulker, crafting table, enderchest behaviour
