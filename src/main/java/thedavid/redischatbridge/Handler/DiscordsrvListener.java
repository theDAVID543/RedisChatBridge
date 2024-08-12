package thedavid.redischatbridge.Handler;

import github.scarsz.discordsrv.DiscordSRV;
import github.scarsz.discordsrv.api.ListenerPriority;
import github.scarsz.discordsrv.api.Subscribe;
import github.scarsz.discordsrv.api.events.DiscordGuildMessagePostProcessEvent;
import github.scarsz.discordsrv.api.events.DiscordGuildMessageReceivedEvent;
import github.scarsz.discordsrv.dependencies.jda.api.events.channel.text.update.TextChannelUpdateTopicEvent;
import github.scarsz.discordsrv.util.DiscordUtil;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import thedavid.redischatbridge.Main;

import java.util.Map;
import java.util.UUID;

public class DiscordsrvListener{
	public Main plugin;
	public DiscordsrvListener(Main plugin){
		this.plugin = plugin;
	}
	@Subscribe(priority = ListenerPriority.MONITOR)
	public void discordMessageReceived(DiscordGuildMessageReceivedEvent e){
		if(e.getAuthor().isBot() || e.getChannel() != DiscordSRV.getPlugin().getDestinationTextChannelForGameChannelName("sync")){
			return;
		}
		Map<String, UUID> link = DiscordSRV.getPlugin().getAccountLinkManager().getLinkedAccounts();
		String message = e.getMessage().getContentRaw();
		OfflinePlayer author = Bukkit.getOfflinePlayer(link.get(e.getAuthor().getId()));
		Player onlinePlayer = Bukkit.getPlayer(author.getUniqueId());
		Component fullMessageComponent = Component.text()
				.append(Component.text("[DC]").color(NamedTextColor.BLUE)).build();
		if(onlinePlayer != null){
			Component prefixcomponent = LegacyComponentSerializer.legacy('&').deserialize(Main.chat.getPlayerPrefix(onlinePlayer));
			fullMessageComponent = fullMessageComponent
					.append(prefixcomponent)
					.append(onlinePlayer.displayName())
					.append(Component.text(": ").color(NamedTextColor.GRAY))
					.append(Component.text(message));
		}else{
			Component prefixcomponent = LegacyComponentSerializer.legacyAmpersand().deserialize(Main.chat.getPlayerPrefix("world",author));
			fullMessageComponent = fullMessageComponent
					.append(prefixcomponent)
					.append(Component.text(author.getName()))
					.append(Component.text(": ").color(NamedTextColor.GRAY))
					.append(Component.text(message));
		}
		Bukkit.getServer().sendMessage(fullMessageComponent);
	}
	@Subscribe
	public void discordMessageProcessed(DiscordGuildMessagePostProcessEvent event) {
//		if(event.getAuthor().isBot()){
			event.setCancelled(true);
//			Bukkit.getLogger().info("cancelled bot message");
//		}
	}
	@Subscribe
	public void discordChannelTopicUpdate(TextChannelUpdateTopicEvent e){
		Bukkit.getLogger().info("channel topic update: " + e.getNewTopic());
		e.getChannel().getTopic();
		DiscordUtil.setTextChannelTopic(DiscordSRV.getPlugin().getDestinationTextChannelForGameChannelName("sync"), e.getNewTopic());
	}
}
