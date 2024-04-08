package thedavid.redischatbridge.Redis;

import org.bukkit.Bukkit;
import org.bukkit.scheduler.BukkitRunnable;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import thedavid.redischatbridge.RedisChatBridgeAPI;

public class LoginSubThread extends BukkitRunnable{
	private final JedisPool jedisPool;
	private final LoginSubscriber loginSubscriber = new LoginSubscriber();
	
	private final String channel = RedisChatBridgeAPI.PLUGIN.redisManager.loginBridgeChannel;
	
	public LoginSubThread(JedisPool jedisPool) {
		this.jedisPool = jedisPool;
	}
	
	@Override
	public void run() {
		// 注意：subscribe是一個阻塞的方法，在取消訂閱該頻道前，thread會一直阻塞在這，無法執行會後續的Code
		Bukkit.getLogger().info("subscribe redis, channel: " + channel);
		
		try(Jedis jedis = jedisPool.getResource()){
			/* 取出一个連線*/
			jedis.subscribe(loginSubscriber, channel);    //通過subscribe 的api去訂閱，傳入參數為訂閱者和頻道名
		}catch(Exception e){
			Bukkit.getLogger().info("subscribe channel error: "+ e);
		}
	}
	@Override
	public void cancel() {
		loginSubscriber.unsubscribe();
		super.cancel();
	}
}
