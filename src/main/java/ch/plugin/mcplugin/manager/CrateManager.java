package ch.plugin.mcplugin.manager;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class CrateManager {

    private final List<ItemStack> rewards;
    private final Random random;

    public CrateManager() {
        this.rewards = new ArrayList<>();
        this.random = new Random();

        // Beispielhafte Rewards hinzufügen
        rewards.add(new ItemStack(Material.DIAMOND, 5));
        rewards.add(new ItemStack(Material.GOLD_INGOT, 10));
        rewards.add(new ItemStack(Material.EMERALD, 3));
        rewards.add(new ItemStack(Material.NETHER_STAR, 1));
        rewards.add(new ItemStack(Material.IRON_INGOT, 16));
        rewards.add(new ItemStack(Material.ENDER_PEARL, 4));
        rewards.add(new ItemStack(Material.BLAZE_ROD, 2));
        rewards.add(new ItemStack(Material.TNT, 8));
    }

    public List<ItemStack> getRewards() {
        return rewards;
    }

    public ItemStack getRandomReward() {
        return rewards.get(random.nextInt(rewards.size()));
    }
}
