package org.purpur.armor;

import org.bukkit.plugin.java.JavaPlugin;

public final class PurpurLeatherArmor extends JavaPlugin {

    @Override
    public void onEnable() {
        ArmorListener listener = new ArmorListener();
        getCommand("pparmor").setExecutor(listener);
        getCommand("pparmor").setTabCompleter(listener);

        getLogger().info("PurpurLeatherArmor has been enabled.");
    }

    @Override
    public void onDisable() {

    }
}
