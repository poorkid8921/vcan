package main.anticheat.spigot.check.impl.player.scaffold;

import main.anticheat.spigot.check.AbstractCheck;
import main.anticheat.spigot.check.api.CheckInfo;
import main.anticheat.spigot.data.PlayerData;
import main.anticheat.spigot.packet.Packet;
import main.anticheat.spigot.util.PlayerUtil;
import org.bukkit.event.block.BlockPlaceEvent;

@CheckInfo(name = "Scaffold", type = 'I', complexType = "Acceleration", description = "Invalid acceleration.")
public class ScaffoldI extends AbstractCheck {
    public ScaffoldI(final PlayerData data) {
        super(data);
    }

    @Override
    public void handle(final Packet packet) {
        if (packet.isBlockPlaceEvent()) {
            final BlockPlaceEvent event = (BlockPlaceEvent) packet.getRawPacket().getRawNMSPacket();
            if (event.isCancelled()) {
                return;
            }
            if (PlayerUtil.isBridging(event)) {
                final double acceleration = this.data.getPositionProcessor().getAcceleration();
                final float deltaPitch = this.data.getRotationProcessor().getDeltaPitch();
                final float deltaYaw = this.data.getRotationProcessor().getDeltaYaw();
                final boolean invalid = acceleration < 0.01 && deltaYaw > 150.0f && deltaPitch > 10.0f;
                if (invalid) {
                    this.fail("acceleration=" + acceleration + " deltaYaw=" + deltaYaw + " deltaPitch=" + deltaPitch);
                }
            }
        }
    }
}
