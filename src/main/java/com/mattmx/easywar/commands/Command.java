package com.mattmx.easywar.commands;

import com.mattmx.easygui.Utils;
import com.mattmx.easywar.Main;
import com.mattmx.easywar.WarStand;
import org.bukkit.Bukkit;
import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class Command implements CommandExecutor, TabCompleter {
    private Main plugin;
    public Command(Main plugin) {
        this.plugin = plugin;
        Bukkit.getPluginCommand("ewar").setExecutor(this);
    }

    @Override
    public boolean onCommand(CommandSender sender, org.bukkit.command.Command command, @org.jetbrains.annotations.NotNull String label, @org.jetbrains.annotations.NotNull String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("Player only command");
        }
        Player p = (Player) sender;
        if (args.length > 0) {
            switch (args[0]) {
                case "gui":
                    if (!p.hasPermission("ewar.gui")) return Main.invalidPerm(p);
                    break;
                case "create":
                    if (!p.hasPermission("ewar.create")) return Main.invalidPerm(p);
                    if (!(args.length > 1)) return false;
                    ArmorStand e = p.getWorld().spawn(new Location(
                            p.getWorld(),
                            p.getLocation().getX(),
                            p.getLocation().getY() + 0.75,
                            p.getLocation().getZ()
                    ), ArmorStand.class);
                    e.setGravity(false);
                    e.setVisible(false);
                    e.setCustomNameVisible(true);
                    e.setSmall(true);
                    e.setMarker(true);
                    e.setInvulnerable(true);
                    e.setCollidable(false);
                    e.setCustomName(Utils.chat(args[1] + " &c&l0"));
                    new WarStand(e.getUniqueId().toString(), 0, false, false, args[1]);
                    WarStand.saveStands(plugin);
                    e.getWorld().getChunkAt(e.getLocation()).setForceLoaded(true);
                    break;
                case "list":
                    if (!p.hasPermission("ewar.list")) return Main.invalidPerm(p);
                    StringBuilder builder = new StringBuilder();
                    for (WarStand stand : Main.STANDS) {
                        builder.append(stand.name + "&f, ");
                    }
                    if (!builder.toString().equals("")) {
                        p.sendMessage(Utils.chat(Main.CHAT_PREFIX + "&7War stands: &c" + builder));
                    } else {
                        p.sendMessage(Utils.chat(Main.CHAT_PREFIX + "&7No war stands exist."));
                    }
                    break;
                case "del":
                    if (!p.hasPermission("ewar.del")) return Main.invalidPerm(p);
                    if (!(args.length > 1)) return false;
                    WarStand w = null;
                    for (WarStand target : Main.STANDS) {
                        if (args[1].equalsIgnoreCase(target.name)) {
                            w = target;
                            break;
                        }
                    }
                    if (w == null) return false;
                    Main.STANDS.remove(w);
                    WarStand.saveStands(plugin);
                    Location l = w.getStand().getLocation();
                    w.getStand().remove();
                    l.getWorld().getChunkAt(l).setForceLoaded(false);
                    p.sendMessage(Utils.chat(Main.CHAT_PREFIX + "&7Removed war stand " + w.name));
                    break;
                case "move":
                    if (!p.hasPermission("ewar.move")) return Main.invalidPerm(p);
                    if (!(args.length > 1)) return false;
                    w = null;
                    for (WarStand target : Main.STANDS) {
                        if (args[1].equalsIgnoreCase(target.name)) {
                            w = target;
                            break;
                        }
                    }
                    if (w == null) return false;
                    w.getStand().teleport(new Location(
                            p.getWorld(),
                            p.getLocation().getX(),
                            p.getLocation().getY() + 0.75,
                            p.getLocation().getZ()
                    ));
                    p.sendMessage(Utils.chat(Main.CHAT_PREFIX + "&7Moved war stand " + w.name));
                    break;
                case "tp":
                    if (!p.hasPermission("ewar.tp")) return Main.invalidPerm(p);
                    if (!(args.length > 1)) return false;
                    w = null;
                    for (WarStand target : Main.STANDS) {
                        if (args[1].equalsIgnoreCase(target.name)) {
                            w = target;
                            break;
                        }
                    }
                    if (w == null) return false;
                    p.teleport(w.getStand().getLocation());
                    p.sendMessage(Utils.chat(Main.CHAT_PREFIX + "&7Teleported you to " + w.name));
                    break;
                case "rename":
                    if (!p.hasPermission("ewar.rename")) return Main.invalidPerm(p);
                    if (!(args.length > 1)) return false;
                    w = null;
                    for (WarStand target : Main.STANDS) {
                        if (args[1].equalsIgnoreCase(target.name)) {
                            w = target;
                            break;
                        }
                    }
                    if (w == null) return false;
                    if (!(args.length > 2)) return false;
                    w.getStand().setCustomName(Utils.chat(args[2]));
                    w.name = args[2];
                    break;
                default:
                    for (Entity f : p.getWorld().getLivingEntities()) {
                        try {
                            p.sendMessage(Utils.chat(f.getCustomName()));
                        } catch (Exception ignore) {

                        }
                    }
                    p.sendMessage(Utils.chat(Main.CHAT_PREFIX + "&7Unknown sub command"));
                    break;
            }
        } else {
            p.sendMessage(Utils.chat(Main.CHAT_PREFIX + "&7Specify a sub command"));
        }
        return false;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, org.bukkit.command.Command command, @org.jetbrains.annotations.NotNull String alias, @org.jetbrains.annotations.NotNull String[] args) {
        if (!command.getName().equalsIgnoreCase("ewar")) return null;
        ArrayList<String> a = new ArrayList<>();
        if (args.length == 1) {
            if ("gui".startsWith(args[0])) a.add("gui");
            if ("create".startsWith(args[0])) a.add("create");
            if ("list".startsWith(args[0])) a.add("list");
            if ("del".startsWith(args[0])) a.add("del");
            if ("move".startsWith(args[0])) a.add("move");
            if ("tp".startsWith(args[0])) a.add("tp");
            if ("rename".startsWith(args[0])) a.add("rename");
        } else if (args.length == 2) {
            if (args[0].equalsIgnoreCase("del") || args[0].equalsIgnoreCase("move") || args[0].equalsIgnoreCase("tp") || args[0].equalsIgnoreCase("rename")) {
                for (WarStand w : Main.STANDS) {
                    if (w.name.startsWith(args[1])) a.add(w.name);
                }
            }
        }
        return a;
    }
}
