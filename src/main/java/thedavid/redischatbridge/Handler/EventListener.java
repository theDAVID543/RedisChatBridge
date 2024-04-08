package thedavid.redischatbridge.Handler;

import github.scarsz.discordsrv.DiscordSRV;
import github.scarsz.discordsrv.dependencies.jda.api.entities.TextChannel;
import io.papermc.paper.event.player.AsyncChatEvent;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.serializer.gson.GsonComponentSerializer;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import thedavid.redischatbridge.Main;
import thedavid.redischatbridge.Redis.Publisher;

public class EventListener implements org.bukkit.event.Listener{
	private final Main plugin;
	public EventListener(Main plugin){
		this.plugin = plugin;
	}
	@EventHandler(priority = EventPriority.LOW, ignoreCancelled = true)
	public void onChat(AsyncChatEvent e){
		e.setCancelled(true);
		String messagePlainString = PlainTextComponentSerializer.plainText().serialize(e.message());
		if(messagePlainString.isEmpty()){
			return;
		}
		Component prefixcomponent = LegacyComponentSerializer.legacyAmpersand().deserialize(Main.chat.getPlayerPrefix(e.getPlayer()));
		Component fullMessageComponent = Component.text()
				.append(prefixcomponent)
				.append(e.getPlayer().displayName())
				.append(Component.text(": ").color(NamedTextColor.GRAY))
				.append(e.message()).build();
		String messageGsonString = GsonComponentSerializer.gson().serialize(fullMessageComponent);
//		plugin.getLogger().info("messageGsonString: " + messageGsonString);
//		e.getPlayer().sendMessage(fullMessageComponent);
//		plugin.getServer().sendMessage(fullMessageComponent);
		Publisher.gameChatPublish(messageGsonString);
//		if(!plugin.isMainServer){
//			Publisher.toDcChatPublish(messagePlainString);
//		}
		DiscordSRV discordSRV = DiscordSRV.getPlugin();
		discordSRV.processChatMessage(e.getPlayer(), messagePlainString, "sync", false, e);
	}
}
