package world.ntdi.core;

import lombok.Getter;
import lombok.NonNull;
import net.kyori.adventure.platform.bukkit.BukkitAudiences;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;
import world.ntdi.api.Api;
import world.ntdi.api.command.CommandCL;
import world.ntdi.api.item.custom.CustomItemRegister;
import world.ntdi.api.nametag.PlayerNameTagService;
import world.ntdi.api.nametag.PlayerNameTagServiceImpl;
import world.ntdi.core.command.map.MapCommand;
import world.ntdi.core.hologram.HologramService;
import world.ntdi.core.hologram.HologramServiceImpl;
import world.ntdi.core.item.MiniBomb;
import world.ntdi.core.item.SquidCannon;
import world.ntdi.core.listener.ExplosionListener;
import world.ntdi.core.listener.JoinListener;
import world.ntdi.core.listener.PlayerDamageListener;
import world.ntdi.core.listener.PlayerQuitListener;
import world.ntdi.core.map.MapService;
import world.ntdi.core.map.MapServiceImpl;


public final class Core extends JavaPlugin {
    @Getter
    private MapService m_mapService;
    @Getter
    private HologramService m_hologramService;
    @Getter
    private PlayerNameTagService m_playerNameTagService;

    private BukkitAudiences m_adventure;
    private Api m_api;

    @Override
    public void onEnable() {
        // Plugin startup logic
        m_api = (Api) Bukkit.getServer().getPluginManager().getPlugin("Api");

//        try {
//            String packageName = "world.ntdi";
//            String internalsName = "spigot_" + Bukkit.getServer().getBukkitVersion().replace(".", "_").replace("-", "_");
//
//            Bukkit.getLogger().info(internalsName);
//
//            m_changePlayerNameService = (ChangePlayerNameService) Class.forName(packageName + "." + internalsName).newInstance();
//        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | ClassCastException exception) {
//            Bukkit.getLogger().log(Level.SEVERE, "Could not find a valid implementation for this server version.");
//        }

        m_adventure = BukkitAudiences.create(this);

        m_mapService = new MapServiceImpl();
        m_hologramService = new HologramServiceImpl(m_mapService, this);
        m_playerNameTagService = new PlayerNameTagServiceImpl(this);

        m_mapService.snapshotMap();

        m_hologramService.createResetHologram();
        m_hologramService.setupHologramLoop();

        CommandCL.register(new MapCommand(m_mapService, m_hologramService), "kaboom");

        registerEvent(new ExplosionListener(m_mapService));
        registerEvent(new JoinListener(m_mapService, m_playerNameTagService, this));
        registerEvent(new PlayerDamageListener());
        registerEvent(new PlayerQuitListener(m_playerNameTagService));

        createCustomItems();
    }

    private void createCustomItems() {
        CustomItemRegister.registerCustomItem(new MiniBomb(m_mapService, this));
        CustomItemRegister.registerCustomItem(new SquidCannon(this));
    }

    private void registerEvent(Listener p_listener) {
        getServer().getPluginManager().registerEvents(p_listener, this);
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
        if(m_adventure != null) {
            m_adventure.close();
            m_adventure = null;
        }

        for (Player player : Bukkit.getOnlinePlayers()) {
            m_playerNameTagService.removePlayer(player);
        }

        m_mapService.restoreMap();
    }

    public @NonNull BukkitAudiences adventure() {
        if(m_adventure == null) {
            throw new IllegalStateException("Tried to access Adventure when the plugin was disabled!");
        }
        return m_adventure;
    }

    public @NonNull Api api() {
        if (m_api == null) {
            throw new IllegalStateException("Tried to access Api when the plugin was disabled!");
        }
        return m_api;
    }
}
