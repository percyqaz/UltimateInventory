# Ultimate Inventory
 Minecraft/Spigot plugin to streamline inventory management

## Open ender chests, shulker boxes and crafting tables by right-clicking **in the inventory**
![demo1](https://user-images.githubusercontent.com/21290233/232254337-f1f93c3f-a896-418f-9473-ad58645b00f3.gif)

## Open ender chests, shulker boxes and crafting tables by right-clicking **in your hand**
![demo2](https://user-images.githubusercontent.com/21290233/232254528-4d8f7aa0-33ee-439a-a8a0-e75f5b599a84.gif)


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
