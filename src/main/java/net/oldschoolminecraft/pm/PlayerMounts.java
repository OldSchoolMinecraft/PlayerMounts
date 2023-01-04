package net.oldschoolminecraft.pm;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;

public class PlayerMounts extends JavaPlugin
{
    public void onEnable() {
        this.getServer().getPluginManager().registerEvents(new PlayerHandler(), this);

        System.out.println("PlayerMounts enabled");
    }

    public boolean onCommand(final CommandSender sender, final Command command, final String label, final String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(ChatColor.RED + "Players only!");
            return true;
        }
        final Player player = (Player)sender;
        if (label.equalsIgnoreCase("nomounts")) {
            if (!player.hasPermission("pm.mount") && !player.isOp()) {
                player.sendMessage(ChatColor.RED + "You do not have permission to use this command!");
                return true;
            }
            try {
                final File file = Util.getPluginFile(player.getName().toLowerCase() + ".pm.conf");
                file.getParentFile().mkdirs();
                file.createNewFile();
                player.sendMessage(ChatColor.RED + "You have disabled your mounts!");
                return true;
            }
            catch (Exception ex) {
                sender.sendMessage(ChatColor.RED + "Oh noes: " + ex.getMessage());
            }
        }
        if (label.equalsIgnoreCase("yesmounts") && (player.hasPermission("pm.mount") || player.isOp())) {
            final File file = Util.getPluginFile(player.getName().toLowerCase() + ".pm.conf");
            if (file.exists()) {
                file.delete();
            }
            player.sendMessage(ChatColor.GREEN + "You have enabled your mounts!");
            return true;
        }
        return false;
    }

    public void onDisable() {
        System.out.println("PlayerMounts disabled");
    }
}
