package main.anticheat.spigot.check.impl.player.badpackets;

import main.anticheat.spigot.check.AbstractCheck;
import main.anticheat.spigot.check.api.CheckInfo;
import main.anticheat.spigot.data.PlayerData;
import main.anticheat.spigot.packet.Packet;

@CheckInfo(name = "Bad Packets", type = 'N', complexType = "Post BlockDig", description = "Post BlockDig packets.")
public class BadPacketsN extends AbstractCheck {
    private long lastFlying;
    private boolean sent;

    public BadPacketsN(final PlayerData data) {
        super(data);
        this.lastFlying = 0L;
        this.sent = false;
    }

    @Override
    public void handle(final Packet packet) {
        if (packet.isFlying()) {
            final long delay = System.currentTimeMillis() - this.lastFlying;
            if (this.sent) {
                if (delay > 40L && delay < 100L) {
                    if (this.increaseBuffer() > this.MAX_BUFFER) {
                        this.fail();
                    }
                } else {
                    this.decayBuffer();
                }
                this.sent = false;
            }
            this.lastFlying = System.currentTimeMillis();
        } else if (packet.isBlockDig()) {
            final long delay = System.currentTimeMillis() - this.lastFlying;
            if (delay < 10L) {
                this.sent = true;
            }
        }
    }
}
