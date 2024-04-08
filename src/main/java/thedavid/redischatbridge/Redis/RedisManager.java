package thedavid.redischatbridge.Redis;

import org.bukkit.Bukkit;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;
import thedavid.redischatbridge.Main;

public class RedisManager{
	public Main plugin;
	public RedisManager(Main plugin){
		this.plugin = plugin;
	}
	public JedisPool jedisPool;
	public String gameChatChannel = "ChatBridge";
	public String loginBridgeChannel = "LoginBridge";
	public String disconnectBridgeChannel = "DisconnectBridge";
	public String syncCommandChannel = "SyncCommand";
	public GameChatSubThread gameChatSubThread;
	public LoginSubThread loginSubThread;
	public DisconnectSubThread disconnectSubThread;
	public void initialize(){
		if(plugin.connectionInformation.getUsername() != null && plugin.connectionInformation.getPassword() != null){
			jedisPool = new JedisPool(new JedisPoolConfig(),
					plugin.connectionInformation.getIp(),
					plugin.connectionInformation.getPort(),
					plugin.connectionInformation.getUsername(),
					plugin.connectionInformation.getPassword());
		}else{
			jedisPool = new JedisPool(new JedisPoolConfig(),
					plugin.connectionInformation.getIp(),
					plugin.connectionInformation.getPort());
		}
		Bukkit.getLogger().info(String.format("redis pool is starting, redis ip %s, redis port %d",
				plugin.connectionInformation.getIp(),
				plugin.connectionInformation.getPort()));
		gameChatSubThread = new GameChatSubThread(jedisPool);
		gameChatSubThread.runTaskAsynchronously(plugin);
		if(plugin.isMainServer){
			loginSubThread = new LoginSubThread(jedisPool);
			loginSubThread.runTaskAsynchronously(plugin);
			disconnectSubThread = new DisconnectSubThread(jedisPool);
			disconnectSubThread.runTaskAsynchronously(plugin);
		}
	}
	public void close(){
		jedisPool.close();
		gameChatSubThread.cancel();
		if(plugin.isMainServer){
			loginSubThread.cancel();
			disconnectSubThread.cancel();
		}
	}
}
