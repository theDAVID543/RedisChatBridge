package thedavid.redischatbridge.Redis;

import org.bukkit.scheduler.BukkitRunnable;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import thedavid.redischatbridge.RedisChatBridgeAPI;

public class GameChatSubThread extends BukkitRunnable{
	private final JedisPool jedisPool;
	private final GameChatSubscriber gameChatSubscriber = new GameChatSubscriber();
	
	private final String channel = RedisChatBridgeAPI.PLUGIN.redisManager.gameChatChannel;
	
	public GameChatSubThread(JedisPool jedisPool) {
		this.jedisPool = jedisPool;
	}
	
	@Override
	public void run() {
		// 注意：subscribe是一個阻塞的方法，在取消訂閱該頻道前，thread會一直阻塞在這，無法執行會後續的Code
		System.out.printf("subscribe redis, channel %s, thread will be blocked%n", channel);
		
		try(Jedis jedis = jedisPool.getResource()){
			/* 取出一个連線*/
			jedis.subscribe(gameChatSubscriber, channel);    //通過subscribe 的api去訂閱，傳入參數為訂閱者和頻道名
		}catch(Exception e){
			System.out.printf("subscribe channel error, %s%n", e);
		}
	}
	@Override
	public void cancel() {
		gameChatSubscriber.unsubscribe();
		super.cancel();
	}
}
