package main.anticheat.spigot.network;

import io.github.retrooper.packetevents.event.PacketListenerAbstract;
import io.github.retrooper.packetevents.event.impl.PacketPlayReceiveEvent;
import io.github.retrooper.packetevents.event.impl.PacketPlaySendEvent;
import io.github.retrooper.packetevents.event.impl.PlayerEjectEvent;
import io.github.retrooper.packetevents.event.impl.PostPlayerInjectEvent;
import io.github.retrooper.packetevents.event.priority.PacketEventPriority;
import io.github.retrooper.packetevents.packettype.PacketType;
import io.github.retrooper.packetevents.packetwrappers.play.in.armanimation.WrappedPacketInArmAnimation;
import io.github.retrooper.packetevents.packetwrappers.play.in.custompayload.WrappedPacketInCustomPayload;
import io.github.retrooper.packetevents.utils.player.ClientVersion;
import main.anticheat.spigot.Anticheat;
import main.anticheat.spigot.config.Config;
import main.anticheat.spigot.data.PlayerData;
import main.anticheat.spigot.packet.Packet;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.util.Vector;

public class NetworkManager extends PacketListenerAbstract {
    public NetworkManager(final PacketEventPriority priority) {
        super(priority);
        this.filterAll();
        this.addClientSidedPlayFilter((byte) -93, (byte) -96, (byte) -94, (byte) -95, (byte) -100, (byte) -71, (byte) -88, (byte) -103, (byte) -111, (byte) -108, (byte) -68, (byte) -69, (byte) -105, (byte) -104, (byte) -86, (byte) -87, (byte) -98, (byte) -85, (byte) -78, (byte) -110, (byte) -92, (byte) -70, (byte) -75, (byte) -107, (byte) 28);
        this.addServerSidedPlayFilter((byte) -26, (byte) -13, (byte) -48, (byte) 3, (byte) -17, (byte) -3, (byte) -36, (byte) 23, (byte) -10, (byte) 27, (byte) -11, (byte) -23, (byte) -25, (byte) 29, (byte) 1, (byte) 22, (byte) -38);
    }

    @Override
    public void onPacketPlayReceive(final PacketPlayReceiveEvent event) {
        if (event.getPacketId() == PacketType.Login.Client.CUSTOM_PAYLOAD) {
            WrappedPacketInCustomPayload packet = new WrappedPacketInCustomPayload(event.getNMSPacket());
            String channel = packet.getChannelName().toLowerCase();

            if (channel.contains("autototem")) {
                Bukkit.getLogger().warning("AutoTotem Brand");
            }
        }

        final PlayerData data = Anticheat.INSTANCE.getPlayerDataManager().getPlayerData(event.getPlayer());
        if (data != null) {
            Anticheat.INSTANCE.getPacketExecutor().execute(() -> Anticheat.INSTANCE.getReceivingPacketProcessor().handle(data, new Packet(Packet.Direction.RECEIVE, event.getNMSPacket(), event.getPacketId()), event));
        }
    }

    @Override
    public void onPacketPlaySend(final PacketPlaySendEvent event) {
        final PlayerData data = Anticheat.INSTANCE.getPlayerDataManager().getPlayerData(event.getPlayer());
        if (data == null || (event.getPacketId() == -26 && (data.getCombatProcessor().getTrackedPlayer() == null || data.getCombatProcessor().getLastTrackedPlayer() == null))) {
            return;
        }
        Anticheat.INSTANCE.getPacketExecutor().execute(() -> Anticheat.INSTANCE.getSendingPacketProcessor().handle(data, new Packet(Packet.Direction.SEND, event.getNMSPacket(), event.getPacketId())));
    }

    @Override
    public void onPostPlayerInject(final PostPlayerInjectEvent event) {
        final Player player = event.getPlayer();
        final boolean npc = player.hasMetadata("NPC") || player.hasMetadata("npc");
        if (!npc) {
            Anticheat.INSTANCE.getPlayerDataManager().add(player);
        }
        final PlayerData data = Anticheat.INSTANCE.getPlayerDataManager().getPlayerData(player);
        if (data == null) {
            return;
        }
        final ClientVersion clientVersion = event.getClientVersion();
        data.setClientVersion(clientVersion);
        if (player.hasPermission("vulcan.alerts") && Config.TOGGLE_ALERTS_ON_JOIN) {
            Anticheat.INSTANCE.getAlertManager().toggleAlerts(player);
        }
        data.getPositionProcessor().onJoin();
        data.getActionProcessor().onJoin();
    }

    @Override
    public void onPlayerEject(final PlayerEjectEvent event) {
        Anticheat.INSTANCE.getPlayerDataManager().remove(event.getPlayer());
    }
}
