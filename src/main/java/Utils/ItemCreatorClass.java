package Utils;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Arrays;

@SuppressWarnings("deprecation")
public class ItemCreatorClass {
    public static ItemStack createItem(int id, int amount, int data, String name, boolean unbreakable, String[] lore) {
        ItemStack item = new ItemStack(Material.getMaterial(id), amount, (byte) data);
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(name);
        if (unbreakable == true)
            meta.spigot().setUnbreakable(true);
        if (lore != null)
            meta.setLore(Arrays.asList(lore));
        item.setItemMeta(meta);

        return item;
    }

    public static ItemStack createItemwithoutID(Material mat, int amount, int data, String name, boolean unbreakable, String[] lore) {
        ItemStack item = new ItemStack(mat, amount, (byte) data);
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(name);
        if (unbreakable == true)
            meta.spigot().setUnbreakable(true);
        if (lore != null)
            meta.setLore(Arrays.asList(lore));
        item.setItemMeta(meta);

        return item;
    }
}
