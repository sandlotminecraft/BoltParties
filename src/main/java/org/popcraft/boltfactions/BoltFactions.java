package org.popcraft.boltfactions;

import com.massivecraft.factions.FPlayer;
import com.massivecraft.factions.FPlayers;
import com.massivecraft.factions.Faction;
import com.massivecraft.factions.Factions;
import com.massivecraft.factions.FactionsPlugin;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;
import org.popcraft.bolt.BoltAPI;
import org.popcraft.bolt.source.SourceTypes;

public final class BoltFactions extends JavaPlugin implements Listener {
    private BoltAPI bolt;
    private FactionsPlugin factions;

    @Override
    public void onEnable() {
        this.bolt = getServer().getServicesManager().load(BoltAPI.class);
        if (bolt == null) {
            getServer().getPluginManager().disablePlugin(this);
            return;
        }
        this.factions = (FactionsPlugin) getServer().getPluginManager().getPlugin("Factions");
        if (factions == null) {
            getServer().getPluginManager().disablePlugin(this);
            return;
        }
        bolt.registerPlayerSourceResolver((source, uuid) -> {
            if (!SourceTypes.FACTION.equals(source.getType())) {
                return false;
            }
            final Player player = Bukkit.getPlayer(uuid);
            if (player == null) {
                return false;
            }
            final FPlayer fPlayer = FPlayers.getInstance().getByPlayer(player);
            if (fPlayer == null) {
                return false;
            }
            final String factionName = source.getIdentifier();
            final Faction faction = Factions.getInstance().getByTag(factionName);
            if (faction == null) {
                return false;
            }
            return faction.getFPlayerAdmin().getName().equals(player.getName()) || faction.getFPlayers().contains(fPlayer);
        });
    }

    @Override
    public void onDisable() {
        this.bolt = null;
        this.factions = null;
    }
}
