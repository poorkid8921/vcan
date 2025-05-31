package main.anticheat.spigot.check.impl.player.scaffold;

import main.anticheat.spigot.check.AbstractCheck;
import main.anticheat.spigot.check.api.CheckInfo;
import main.anticheat.spigot.data.PlayerData;
import main.anticheat.spigot.exempt.type.ExemptType;
import main.anticheat.spigot.packet.Packet;
import main.anticheat.spigot.util.MathUtil;
import main.anticheat.spigot.util.PlayerUtil;
import org.bukkit.event.block.BlockPlaceEvent;

@CheckInfo(name = "Scaffold", type = 'D', complexType = "Rotations", description = "Invalid rotations.")
public class ScaffoldD extends AbstractCheck {
    private long lastPlace;
    private long delay;
    private boolean bridging;
    private int placeTicks;

    public ScaffoldD(final PlayerData data) {
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
        } else if (packet.isRotation() && ++this.placeTicks < 6 && this.bridging && this.delay < 400L) {
            final float lastDeltaPitch = this.data.getRotationProcessor().getLastDeltaPitch();
            final float deltaPitch = this.data.getRotationProcessor().getDeltaPitch();
            final long expandedPitch = (long) (MathUtil.EXPANDER * deltaPitch);
            final long expandedLastPitch = (long) (MathUtil.EXPANDER * lastDeltaPitch);
            final long gcd = MathUtil.getGcd(expandedPitch, expandedLastPitch);
            final boolean exempt = this.isExempt(ExemptType.CINEMATIC);
            if (gcd < 131072L && gcd > 0L && deltaPitch > 0.15 && !exempt) {
                if (this.increaseBuffer() > this.MAX_BUFFER) {
                    this.fail("rotation=" + gcd / 100L);
                }
            } else {
                this.decayBuffer();
            }
        }
    }
}
