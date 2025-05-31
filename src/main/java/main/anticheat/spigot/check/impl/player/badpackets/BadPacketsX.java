package main.anticheat.spigot.check.impl.player.badpackets;

import main.anticheat.spigot.check.AbstractCheck;
import main.anticheat.spigot.check.api.CheckInfo;
import main.anticheat.spigot.data.PlayerData;
import main.anticheat.spigot.exempt.type.ExemptType;
import main.anticheat.spigot.packet.Packet;

@CheckInfo(name = "Bad Packets", type = 'X', complexType = "Post ArmAnimation", description = "Post ArmAnimation packets.")
public class BadPacketsX extends AbstractCheck {
    private long lastFlying;
    private boolean sent;

    public BadPacketsX(final PlayerData data) {
        super(data);
        this.lastFlying = 0L;
        this.sent = false;
    }

    @Override
    public void handle(final Packet packet) {
        if (packet.isFlying()) {
            final long delay = System.currentTimeMillis() - this.lastFlying;
            if (this.isExempt(ExemptType.CLIENT_VERSION, ExemptType.SERVER_VERSION)) {
                return;
            }
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
        } else if (packet.isArmAnimation()) {
            final long delay = System.currentTimeMillis() - this.lastFlying;
            if (delay < 10L) {
                this.sent = true;
            }
        }
    }
}
