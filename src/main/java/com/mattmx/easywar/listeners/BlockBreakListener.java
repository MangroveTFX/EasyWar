package com.mattmx.easywar.listeners;

import com.mattmx.easygui.Utils;
import com.mattmx.easywar.Main;
import com.mattmx.easywar.WarStand;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;

public class BlockBreakListener implements Listener {
    public Main plugin;
    public BlockBreakListener(Main plugin) {
        this.plugin = plugin;
        Bukkit.getPluginManager().registerEvents(this, plugin);
    }
    @EventHandler
    public void onBreak(BlockBreakEvent e) {
        for (WarStand stand : Main.STANDS) {
            ArmorStand a = stand.getStand();
            if (a.getLocation().getBlockX() == e.getBlock().getX() &&
            a.getLocation().getBlockY() == e.getBlock().getY() &&
            a.getLocation().getBlockZ() == e.getBlock().getZ()) {
                if (e.getBlock().getBlockData().getMaterial() == Material.OBSIDIAN) {
                    stand.capturing = false;
                    for (Player player : Bukkit.getOnlinePlayers()) {
                        if (stand.captured) return;
                        player.playSound(a.getLocation(), Sound.BLOCK_NOTE_BLOCK_BIT, 1.0f, 1.0f);
                        player.sendMessage(Utils.chat(plugin.getConfig().getString("un-capturing-message").replace("%name%", stand.name).replace("%prefix%", Main.CHAT_PREFIX)));
                    }
                }
            }
        }
    }
}
