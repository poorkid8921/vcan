package main.anticheat.spigot.check.impl.player.scaffold;

import main.anticheat.spigot.check.AbstractCheck;
import main.anticheat.spigot.check.api.CheckInfo;
import main.anticheat.spigot.data.PlayerData;
import main.anticheat.spigot.packet.Packet;
import main.anticheat.spigot.util.PlayerUtil;
import org.bukkit.event.block.BlockPlaceEvent;

@CheckInfo(name = "Scaffold", type = 'E', complexType = "Rotations [2]", description = "Invalid rotations.")
public class ScaffoldE extends AbstractCheck {
    private long lastPlace;
    private long delay;
    private boolean bridging;
    private int placeTicks;

    public ScaffoldE(final PlayerData data) {
        super(data);
    }

    @Override
    public void handle(final Packet packet) {
        if (packet.isBlockPlaceEvent()) {
            final BlockPlaceEvent event = (BlockPlaceEvent) packet.getRawPacket().getRawNMSPacket();
            if (event.isCancelled()) {
                return;
            }
            this.bridging = PlayerUtil.isBridging(event);
            this.delay = System.currentTimeMillis() - this.lastPlace;
            this.placeTicks = 0;
            this.lastPlace = System.currentTimeMillis();
        } else if (packet.isFlying() && ++this.placeTicks < 3 && this.bridging && this.delay < 400L) {
            final float deltaPitch = this.data.getRotationProcessor().getDeltaPitch();
            final float deltaYaw = this.data.getRotationProcessor().getDeltaYaw();
            if (deltaPitch > 8.0f && deltaYaw > 100.0f) {
                if (this.increaseBuffer() > this.MAX_BUFFER) {
                    this.fail("deltaYaw=" + deltaYaw + " deltaPitch=" + deltaPitch);
                }
            } else {
                this.decayBuffer();
            }
        }
    }
}
