package com.mattmx.easywar;

import com.mattmx.easygui.Utils;
import com.mattmx.easywar.commands.Command;
import com.mattmx.easywar.listeners.BlockBreakListener;
import com.mattmx.easywar.listeners.BlockPlaceListener;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.*;
import java.util.ArrayList;

public final class Main extends JavaPlugin {
    public static String CHAT_PREFIX = "&c&l&oEasy War &f&l> &7";
    public static List<WarStand> STANDS = new ArrayList<WarStand>();
    @Override
    public void onEnable() {
        getLogger().info("Starting.");
        saveDefaultConfig();
        CHAT_PREFIX = getConfig().getString("prefix");
        new BukkitRunnable() {
            @Override
            public void run() {
                WarStand.init(Main.this);
                WarStand.registerStands(Main.this);
                new BlockBreakListener(Main.this);
                new BlockPlaceListener(Main.this);
                new Command(Main.this);

                getLogger().

                        info("Started.");
                Bukkit.getScheduler().

                        scheduleAsyncRepeatingTask(Main.this, new Runnable() {
                            @Override
                            public void run() {
                                for (WarStand w : STANDS) {
                                    w.doSecond(Main.this);
                                }
                            }
                        }, 0L, 20L);
            }
        }.runTaskLater(this, 1L);
    }

    @Override
    public void onDisable() {
        WarStand.saveStands(this);
    }

    public static boolean invalidPerm(Player p) {
        p.sendMessage(Utils.chat(CHAT_PREFIX + "&7You do not have permissions to execute this command."));
        return false;
    }
}
