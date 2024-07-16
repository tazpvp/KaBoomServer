package world.ntdi.api;

import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;
import world.ntdi.api.hologram.Hologram;
import world.ntdi.api.item.custom.CustomItemListener;
import world.ntdi.api.sql.database.PostgresqlDatabase;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;


public final class Api extends JavaPlugin {
    @Getter
    private static PostgresqlDatabase m_postgresqlDatabase;


    @Override
    public void onEnable() {
        // Plugin startup logic
        getConfig().options().copyDefaults(true);
        saveDefaultConfig();

        initializeDatabase();

        getServer().getPluginManager().registerEvents(new CustomItemListener(), this);
    }

    private void initializeDatabase() {
        final String sqlHost = getConfig().getString("sql-host");
        final String sqlPort = getConfig().getString("sql-port");
        final String sqlUser = getConfig().getString("sql-user");
        final String sqlPassword = getConfig().getString("sql-password");

        final String connectionString = "jdbc:postgresql://" + sqlHost + ":" + sqlPort + "/postgres";

        try {
            m_postgresqlDatabase = new PostgresqlDatabase(connectionString, sqlUser, sqlPassword);
        } catch (SQLException p_e) {
            throw new RuntimeException(p_e);
        }
    }

    @Override
    public void onDisable() {
        m_postgresqlDatabase.close();

        if (!toBeDeleted.isEmpty()) {
            toBeDeleted.forEach(Hologram::deleteHologram);
        }
    }

    public static Api getInstance(){
        return (Api) Bukkit.getPluginManager().getPlugin("Api");
    }

    public static List<Hologram> toBeDeleted = new ArrayList<>();

}
