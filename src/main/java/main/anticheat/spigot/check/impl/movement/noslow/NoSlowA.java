package main.anticheat.spigot.check.impl.movement.noslow;

import io.github.retrooper.packetevents.packetwrappers.play.in.blockdig.WrappedPacketInBlockDig;
import main.anticheat.spigot.check.AbstractCheck;
import main.anticheat.spigot.check.api.CheckInfo;
import main.anticheat.spigot.data.PlayerData;
import main.anticheat.spigot.exempt.type.ExemptType;
import main.anticheat.spigot.packet.Packet;

@CheckInfo(name = "No Slow", type = 'A', complexType = "Packet", description = "Placing while digging")
public class NoSlowA extends AbstractCheck {
    public NoSlowA(final PlayerData data) {
        super(data);
    }

    @Override
    public void handle(final Packet packet) {
        if (packet.isBlockDig()) {
            final WrappedPacketInBlockDig wrapper = this.data.getBlockDigWrapper();
            final boolean placing = this.data.getActionProcessor().isPlacing();
            final boolean exempt = this.isExempt(ExemptType.CLIENT_VERSION, ExemptType.SERVER_VERSION);
            final boolean action = wrapper.getDigType() == WrappedPacketInBlockDig.PlayerDigType.RELEASE_USE_ITEM;
            if (placing && action && !exempt) {
                if (this.increaseBuffer() > this.MAX_BUFFER) {
                    this.fail();
                }
            } else {
                this.decayBuffer();
            }
        }
    }
}
