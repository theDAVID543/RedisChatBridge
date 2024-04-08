package thedavid.redischatbridge.Redis;

import redis.clients.jedis.JedisPool;
import thedavid.redischatbridge.RedisChatBridgeAPI;
import redis.clients.jedis.Jedis;

public class Publisher{
	private static final JedisPool jedisPool = RedisChatBridgeAPI.PLUGIN.redisManager.jedisPool;
	private static final String gameChatChannel = RedisChatBridgeAPI.PLUGIN.redisManager.gameChatChannel;
	private static final String syncCommandChannel = RedisChatBridgeAPI.PLUGIN.redisManager.syncCommandChannel;
	public static void gameChatPublish(String message){
		try (Jedis jedis = jedisPool.getResource()) {
			jedis.publish(gameChatChannel, message);
		}
	}
	public static void syncCommandPublish(String message){
		try (Jedis jedis = jedisPool.getResource()) {
			jedis.publish(syncCommandChannel, message);
		}
	}
}
