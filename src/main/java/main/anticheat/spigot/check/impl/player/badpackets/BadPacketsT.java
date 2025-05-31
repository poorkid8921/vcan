package main.anticheat.spigot.check.impl.player.badpackets;

import io.github.retrooper.packetevents.packetwrappers.play.in.keepalive.WrappedPacketInKeepAlive;
import main.anticheat.spigot.check.AbstractCheck;
import main.anticheat.spigot.check.api.CheckInfo;
import main.anticheat.spigot.data.PlayerData;
import main.anticheat.spigot.packet.Packet;

@CheckInfo(name = "Bad Packets", type = 'T', complexType = "Keep Alive", description = "Invalid KeepAlive packets.")
public class BadPacketsT extends AbstractCheck {
    private long lastId;

    public BadPacketsT(final PlayerData data) {
        super(data);
        this.lastId = -1L;
    }

    @Override
    public void handle(final Packet packet) {
        if (packet.isKeepAlive()) {
            final WrappedPacketInKeepAlive wrapper = new WrappedPacketInKeepAlive(packet.getRawPacket());
            final long id = wrapper.getId();
            if (id == this.lastId || id == 0L) {
                this.fail("id=" + id + " lastId=" + this.lastId);
            }
            this.lastId = id;
        }
    }
}
