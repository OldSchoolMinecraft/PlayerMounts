package net.oldschoolminecraft.pm;

import org.bukkit.ChatColor;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerListener;

import java.io.File;

public class PlayerHandler extends PlayerListener
{
    @EventHandler
    public void onPlayerInteractEntity(final PlayerInteractEntityEvent event) {
        final File playerFile = Util.getPluginFile(event.getPlayer().getName().toLowerCase() + ".pm.conf");
        final Entity ent = event.getRightClicked();
        if (ent != null && (event.getPlayer().hasPermission("pm.mount") || event.getPlayer().isOp())) {
            if (ent instanceof Wolf)
                return;
            if (ent instanceof Vehicle)
                return;
            if (ent instanceof Painting)
                return;
            if (ent instanceof FallingSand)
                return;
            if (ent instanceof Player) {
                final Player targetPlayer = (Player)ent;
                final File targetFile = Util.getPluginFile(targetPlayer.getName().toLowerCase() + ".pm.conf");
                if (targetFile.exists()) {
                    event.getPlayer().sendMessage(ChatColor.RED + "This player has mounts disabled!");
                    return;
                }
            }
            if (playerFile.exists()) {
                return;
            }
            if (ent.getPassenger() != null) {
                Entity passent = ent.getPassenger();
                while (passent != null){
                    passent = passent.getPassenger();
                }
                passent.setPassenger(event.getPlayer());
            }
            else {
                ent.setPassenger(event.getPlayer());
            }
        }
    }
}
