package thedavid.redischatbridge.Handler;

import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import thedavid.redischatbridge.Main;
import thedavid.redischatbridge.impl.ConnectionInformation;

import java.io.File;
import java.io.IOException;

public class ConfigHandler{
	private Main plugin;
	public ConfigHandler(Main plugin){
		this.plugin = plugin;
	}
	private File configFile;
	private FileConfiguration config;
	public void createCustomConfig() {
		configFile = new File(plugin.instance.getDataFolder(), "config.yml");
		if (!configFile.exists()) {
			configFile.getParentFile().mkdirs();
			plugin.instance.saveResource("config.yml", false);
		}
		
		config = new YamlConfiguration();
		try {
			config.load(configFile);
		} catch (IOException | InvalidConfigurationException e) {
			e.printStackTrace();
		}
	}
	public Boolean isMainServer(){
		return config.getBoolean("MainServer");
	}
	public ConnectionInformation getConnectionInformation(){
		return new ConnectionInformation(config.getString("Redis.Host"),
				config.getInt("Redis.Port"),
				config.getString("Redis.Username"),
				config.getString("Redis.Password"));
	}
}
