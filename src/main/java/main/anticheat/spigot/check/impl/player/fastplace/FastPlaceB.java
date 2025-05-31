package main.anticheat.spigot.check.impl.player.fastplace;

import main.anticheat.spigot.check.AbstractCheck;
import main.anticheat.spigot.check.api.CheckInfo;
import main.anticheat.spigot.config.Config;
import main.anticheat.spigot.data.PlayerData;
import main.anticheat.spigot.packet.Packet;

@CheckInfo(name = "Fast Place", type = 'B', complexType = "Delay", description = "Placing blocks too quickly.", experimental = true)
public class FastPlaceB extends AbstractCheck {
    private int blocksPlaced;

    public FastPlaceB(final PlayerData data) {
        super(data);
        this.blocksPlaced = 0;
    }

    @Override
    public void handle(final Packet packet) {
        if (packet.isBlockPlace()) {
            if (System.currentTimeMillis() - this.data.getConnectionProcessor().getLastFlying() > 55L) {
                this.blocksPlaced = 0;
            }
            ++this.blocksPlaced;
        } else if (packet.isFlying()) {
            final long flyingDelay = this.data.getConnectionProcessor().getFlyingDelay();
            if (this.blocksPlaced > Config.FASTPLACE_B_MAX_BLOCKS && flyingDelay < 105L) {
                if (this.increaseBuffer() > this.MAX_BUFFER) {
                    this.fail("blocks=" + this.blocksPlaced + " delay=" + flyingDelay);
                }
            } else {
                this.decayBuffer();
            }
            this.blocksPlaced = 0;
        }
    }
}
