package ch.plugin.mcplugin.util;

import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.PotionMeta;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class WheelSpinItems {

    private final List<SpinItem> items = new ArrayList<>();
    private final Random random = new Random();

    public WheelSpinItems() {
        // Beispiel: God-Schwert mit Verzauberungen
        items.add(new SpinItem(
                new ItemBuilder(Material.DIAMOND_SWORD)
                        .displayname("§6God Sword")
                        .enchant(Enchantment.DAMAGE_ALL, 5) // Schärfe V
                        .enchant(Enchantment.DURABILITY, 5) // Haltbarkeit V
                        .amount(1) // Einzelnes Schwert
                        .build(), 5)); // 5% Wahrscheinlichkeit

        // Beispiel: Stärke II Potion
        items.add(new SpinItem(createPotionItem("§6Strength II Potion", (short) 8233, 32), 20));

        // Beispiel: Heiltränke
        items.add(new SpinItem(createPotionItem("§6Healing Potion", (short) 16389, 16), 30));

        // Beispiel: Feuerresistenztränke
        items.add(new SpinItem(createPotionItem("§6Fire Resistance Potion", (short) 8227, 16), 30));

        // Beispiel: God-Rüstungsteile
        items.add(new SpinItem(
                new ItemBuilder(Material.DIAMOND_HELMET)
                        .displayname("§6God Helmet")
                        .enchant(Enchantment.PROTECTION_ENVIRONMENTAL, 5) // Schutz V
                        .enchant(Enchantment.DURABILITY, 5) // Haltbarkeit V
                        .amount(1) // Einzelstück
                        .build(), 5)); // 5% Wahrscheinlichkeit

        items.add(new SpinItem(
                new ItemBuilder(Material.DIAMOND_CHESTPLATE)
                        .displayname("§6God Chestplate")
                        .enchant(Enchantment.PROTECTION_ENVIRONMENTAL, 5) // Schutz V
                        .enchant(Enchantment.DURABILITY, 5) // Haltbarkeit V
                        .amount(1)
                        .build(), 5));

        items.add(new SpinItem(
                new ItemBuilder(Material.DIAMOND_LEGGINGS)
                        .displayname("§6God Leggings")
                        .enchant(Enchantment.PROTECTION_ENVIRONMENTAL, 5)
                        .enchant(Enchantment.DURABILITY, 5)
                        .amount(1)
                        .build(), 5));

        items.add(new SpinItem(
                new ItemBuilder(Material.DIAMOND_BOOTS)
                        .displayname("§6God Boots")
                        .enchant(Enchantment.PROTECTION_ENVIRONMENTAL, 5)
                        .enchant(Enchantment.DURABILITY, 5)
                        .amount(1)
                        .build(), 5));
    }

    /**
     * Wählt ein zufälliges Item basierend auf den Wahrscheinlichkeiten aus.
     *
     * @return Das ausgewählte ItemStack.
     */
    public ItemStack getRandomItem() {
        int totalWeight = items.stream().mapToInt(SpinItem::getWeight).sum();
        int randomValue = random.nextInt(totalWeight);

        int currentWeight = 0;
        for (SpinItem item : items) {
            currentWeight += item.getWeight();
            if (randomValue < currentWeight) {
                return item.getItemStack().clone(); // Klonen des Items
            }
        }

        return null; // Sollte nie passieren
    }

    /**
     * Gibt die Liste aller möglichen Items zurück.
     *
     * @return Liste der Items.
     */
    public List<SpinItem> getItems() {
        return items;
    }

    /**
     * Erstellt einen Trank-ItemStack mit spezifischem Namen und Daten.
     *
     * @param displayName Der Anzeigename.
     * @param durability  Die spezifischen Trankdaten.
     * @param amount      Die Stapelgröße.
     * @return Der ItemStack für den Trank.
     */
    private ItemStack createPotionItem(String displayName, short durability, int amount) {
        ItemStack potion = new ItemStack(Material.POTION, amount);
        PotionMeta meta = (PotionMeta) potion.getItemMeta();
        if (meta != null) {
            meta.setDisplayName(displayName);
            potion.setItemMeta(meta);
        }
        potion.setDurability(durability);
        return potion;
    }

    /**
     * Hilfsklasse für ein Item mit einer Wahrscheinlichkeit.
     */
    public static class SpinItem {
        private final ItemStack itemStack;
        private final int weight;

        public SpinItem(ItemStack itemStack, int weight) {
            this.itemStack = itemStack;
            this.weight = weight;
        }

        public ItemStack getItemStack() {
            return itemStack;
        }

        public int getWeight() {
            return weight;
        }
    }
}
