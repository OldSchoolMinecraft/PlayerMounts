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
    public void onPlayerInteractEntity(final PlayerInteractEntityEvent event)
    {
        final Player player = event.getPlayer();
        final File playerFile = Util.getPluginFile(player.getName().toLowerCase() + ".pm.conf");
        final Entity clickedEntity = event.getRightClicked();

        if (clickedEntity != null && (event.getPlayer().hasPermission("pm.mount") || event.getPlayer().isOp()))
        {
            if (clickedEntity instanceof Wolf) return;
            if (clickedEntity instanceof Vehicle) return;
            if (clickedEntity instanceof Painting) return;
            if (clickedEntity instanceof FallingSand) return;
            if (clickedEntity instanceof Player)
            {
                final Player targetPlayer = (Player) clickedEntity;
                final File targetFile = Util.getPluginFile(targetPlayer.getName().toLowerCase() + ".pm.conf");
                if (targetFile.exists())
                {
                    event.getPlayer().sendMessage(ChatColor.RED + "This player has mounts disabled!");
                    return;
                }
            }

            if (playerFile.exists()) return;

            if (event.getPlayer().getPassenger() != null && event.getPlayer().getPassenger().equals(clickedEntity))
            {
                event.getPlayer().sendMessage(ChatColor.RED + "You cannot mount your passenger!");
                return;
            }

            if (clickedEntity.getPassenger() != null)
            {
                if (clickedEntity.getPassenger().equals(event.getPlayer()))
                {
                    clickedEntity.eject();
                    return;
                }

                Entity currentPassenger = clickedEntity.getPassenger();

                if (currentPassenger instanceof Player)
                {
                    Player currentPassengerPlayer = (Player) currentPassenger;
                    if (currentPassengerPlayer.getPassenger() != null && currentPassengerPlayer.getPassenger().equals(event.getPlayer()))
                    {
                        event.getPlayer().sendMessage(ChatColor.RED + "You cannot mount your passenger due to circular relationships and recursion.");
                        return;
                    }
                }

                Entity topEntity = getTopMostPassenger(currentPassenger);

                topEntity.setPassenger(event.getPlayer());
            } else clickedEntity.setPassenger(event.getPlayer());
        }
    }

    private Entity getTopMostPassenger(Entity entity)
    {
        Entity current = entity;
        while (current.getPassenger() != null)
        {
            current = current.getPassenger();
        }
        return current;
    }

    private boolean isCircularMount(Player player, Entity topEntity)
    {
        Entity current = topEntity;
        while (current != null)
        {
            if (current.equals(player)) return true;
            current = current.getPassenger();
        }
        return false;
    }
}
