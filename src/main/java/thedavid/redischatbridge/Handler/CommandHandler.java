package thedavid.redischatbridge.Handler;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;
import redis.clients.jedis.JedisPool;
import thedavid.redischatbridge.Redis.Publisher;
import thedavid.redischatbridge.RedisChatBridgeAPI;

public class CommandHandler implements CommandExecutor{
	public CommandHandler(){
		jedisPool = RedisChatBridgeAPI.PLUGIN.redisManager.jedisPool;
	}
	private final JedisPool jedisPool;
	@Override
	public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String s, @NotNull String[] args){
		if(args.length == 0){
			sender.sendMessage("Usage: /sync <message>");
			return false;
		}
		String sendCommand = String.join(" ", args);
		Publisher.syncCommandPublish(sendCommand);
		return true;
	}
}
