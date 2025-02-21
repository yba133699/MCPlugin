package ch.plugin.mcplugin.listener;

import ch.plugin.mcplugin.MCPlugin;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.server.ServerListPingEvent;

public class MotdListener implements Listener {

    @EventHandler
    public void motd(ServerListPingEvent e){
        e.setMotd("             " + MCPlugin.instance.getPrefix() + "§bSkyPvP§b " +
                "   ");
    }

}
