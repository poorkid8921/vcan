package main.anticheat.spigot.check.impl.player.baritone;

import io.github.retrooper.packetevents.packetwrappers.play.in.blockdig.WrappedPacketInBlockDig;
import main.anticheat.spigot.check.AbstractCheck;
import main.anticheat.spigot.check.api.CheckInfo;
import main.anticheat.spigot.data.PlayerData;
import main.anticheat.spigot.packet.Packet;

@CheckInfo(name = "Baritone", type = 'B', complexType = "Large Rotation", description = "Checks for Baritone like rotations.")
public class BaritoneB extends AbstractCheck {
    private int breakTicks;

    public BaritoneB(final PlayerData data) {
        super(data);
        this.breakTicks = 100;
    }

    @Override
    public void handle(final Packet packet) {
        if (packet.isRotation() && ++this.breakTicks < 5) {
            final float deltaYaw = this.data.getRotationProcessor().getDeltaYaw();
            final float deltaPitch = this.data.getRotationProcessor().getDeltaPitch();
            final float pitch = this.data.getRotationProcessor().getPitch();
            if (deltaYaw > 95.0f && deltaPitch < 0.15 && Math.abs(pitch) < 70.0f) {
                if (this.increaseBuffer() > this.MAX_BUFFER) {
                    this.fail("deltaPitch=" + deltaPitch + " deltaYaw=" + deltaYaw);
                }
            } else {
                this.decayBuffer();
            }
        } else if (packet.isBlockDig()) {
            final WrappedPacketInBlockDig wrapper = this.data.getBlockDigWrapper();
            if (wrapper.getDigType() == WrappedPacketInBlockDig.PlayerDigType.STOP_DESTROY_BLOCK) {
                this.breakTicks = 0;
            }
        }
    }
}
