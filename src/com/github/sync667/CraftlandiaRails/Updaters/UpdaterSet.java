package com.github.sync667.CraftlandiaRails.Updaters;

import com.github.sync667.CraftlandiaRails.CraftlandiaRails;
import com.github.sync667.CraftlandiaRails.Storage.ExpirableSet;
import com.github.sync667.CraftlandiaRailsAPI.Event.UpdaterRemoveEvent;
import org.bukkit.Bukkit;

import java.util.Iterator;

/**
 * A set for integers with a timeout of 1h
 */
final class UpdaterSet{

    // entries stay for 1h
    UpdaterSet() {
        long duration = CraftlandiaRails.myPlugin.getConfig().getInt("updater.timeout", 60) * 1200;
        updateSet = new ExpirableSet<Integer>(duration, false, "UpdaterRoutes");
    }

    private final ExpirableSet<Integer> updateSet;

    ExpirableSet<Integer> getMap() {
        return updateSet;
    }

    boolean isUpdater(Integer id) {
        return updateSet.contains(id);
    }

    void addUpdater(int id) {
        this.updateSet.add(id);
    }

    void clear() {
        Iterator<Integer> it = updateSet.getIterator();
        while (it.hasNext()) {
            Bukkit.getServer().getPluginManager().callEvent(new UpdaterRemoveEvent(it.next()));
        }
        updateSet.clear();
    }
}
