package thedavid.redischatbridge.Redis;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.serializer.gson.GsonComponentSerializer;
import org.bukkit.Bukkit;
import redis.clients.jedis.JedisPubSub;
import thedavid.redischatbridge.Main;
import thedavid.redischatbridge.RedisChatBridgeAPI;


public class GameChatSubscriber extends JedisPubSub{
	private final Main plugin = RedisChatBridgeAPI.PLUGIN;
	
	@Override
	public void onMessage(String channel, String message) {       //收到消息會調用
//		Bukkit.getLogger().info("receive redis published message, channel " +  channel + ", message " + message);
		Component messageComponent = GsonComponentSerializer.gson().deserialize(message);
		plugin.getServer().sendMessage(messageComponent);
//		this.unsubscribe();
	}
	
//	@Override
//	public void onSubscribe(String channel, int subscribedChannels) {    //訂閱頻道會調用
//		Bukkit.getLogger().info("subscribe redis channel success, channel " + channel + ", subscribedChannels " + subscribedChannels);
//	}
//
//	@Override
//	public void onUnsubscribe(String channel, int subscribedChannels) {   //取消訂閱會調用
//		Bukkit.getLogger().info("unsubscribe redis channel, channel " + channel + ", subscribedChannels " + subscribedChannels);
//	}
}
