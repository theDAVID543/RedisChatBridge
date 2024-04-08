package thedavid.redischatbridge;

import github.scarsz.discordsrv.DiscordSRV;
import net.milkbowl.vault.chat.Chat;
import org.bukkit.Bukkit;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;
import thedavid.redischatbridge.Handler.CommandHandler;
import thedavid.redischatbridge.Handler.ConfigHandler;
import thedavid.redischatbridge.Handler.DiscordsrvListener;
import thedavid.redischatbridge.Handler.EventListener;
import thedavid.redischatbridge.Redis.RedisManager;
import thedavid.redischatbridge.impl.ConnectionInformation;

public final class Main extends JavaPlugin {
    public JavaPlugin instance;
    public RedisManager redisManager;
    public ConfigHandler configHandler;
    public Boolean isMainServer = false;
    public DiscordsrvListener discordsrvListener;
    public ConnectionInformation connectionInformation;

    @Override
    public void onEnable() {
        // Plugin startup logic
        
        instance = this;
        
        configHandler = new ConfigHandler(this);
        configHandler.createCustomConfig();
        isMainServer = configHandler.isMainServer();
        connectionInformation = configHandler.getConnectionInformation();
        
        redisManager = new RedisManager(this);
        redisManager.initialize();
        
        Bukkit.getPluginCommand("sync").setExecutor(new CommandHandler());
        setupChat();
        Bukkit.getPluginManager().registerEvents(new EventListener(this), this);
        
        discordsrvListener = new DiscordsrvListener(this);
        DiscordSRV.api.subscribe(discordsrvListener);
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
        redisManager.close();
        DiscordSRV.api.unsubscribe(discordsrvListener);
    }
    public static Chat chat = null;
    private void setupChat() {
        RegisteredServiceProvider<Chat> rsp = getServer().getServicesManager().getRegistration(Chat.class);
        chat = rsp.getProvider();
    }
}
