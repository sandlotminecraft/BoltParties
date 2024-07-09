package com.sandlotminecraft.boltparties;

import com.alessiodp.parties.api.Parties;
import com.alessiodp.parties.api.interfaces.PartiesAPI;
import com.alessiodp.parties.api.interfaces.Party;
import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;
import org.popcraft.bolt.BoltAPI;

public final class BoltParties extends JavaPlugin implements Listener {
    private BoltAPI bolt;
    private PartiesAPI partiesApi;

    @Override
    public void onEnable() {
        this.bolt = getServer().getServicesManager().load(BoltAPI.class);
        if (bolt == null) {
            getServer().getPluginManager().disablePlugin(this);
            return;
        }
        final Plugin partiesPlugin = getServer().getPluginManager().getPlugin("Parties");
        if (partiesPlugin == null || !partiesPlugin.isEnabled()) {
            getServer().getPluginManager().disablePlugin(this);
            return;
        }
        this.partiesApi = Parties.getApi();


        bolt.registerPlayerSourceResolver((source, uuid) -> {
            if ("party" != source.getType()) {
                return false;
            }
            final Player player = Bukkit.getPlayer(uuid);
            if (player == null) {
                return false;
            }
            final Party party = partiesApi.getPartyOfPlayer(uuid);
            if (party == null) {
                return false;
            }
            final String partyName = source.getIdentifier();
            if (party.getName().toLowerCase() == partyName.toLowerCase()) {
                return true;
            }
            return false;
        });
    }

    @Override
    public void onDisable() {
        this.bolt = null;
        this.partiesApi = null;
    }
}
