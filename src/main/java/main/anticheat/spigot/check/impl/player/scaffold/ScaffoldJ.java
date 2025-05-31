package main.anticheat.spigot.check.impl.player.scaffold;

import main.anticheat.spigot.check.AbstractCheck;
import main.anticheat.spigot.check.api.CheckInfo;
import main.anticheat.spigot.data.PlayerData;
import main.anticheat.spigot.packet.Packet;
import main.anticheat.spigot.util.MathUtil;
import main.anticheat.spigot.util.PlayerUtil;
import org.bukkit.event.block.BlockPlaceEvent;

@CheckInfo(name = "Scaffold", type = 'J', complexType = "Acceleration [2]", description = "Invalid pitch change.")
public class ScaffoldJ extends AbstractCheck {
    public ScaffoldJ(final PlayerData data) {
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
                final boolean invalid = MathUtil.isExponentiallySmall(acceleration) && deltaPitch > 18.0f;
                if (invalid) {
                    this.fail("acceleration=" + acceleration + " deltaPitch=" + deltaPitch);
                }
            }
        }
    }
}
