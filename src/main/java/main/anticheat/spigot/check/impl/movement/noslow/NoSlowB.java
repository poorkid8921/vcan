package main.anticheat.spigot.check.impl.movement.noslow;

import main.anticheat.spigot.check.AbstractCheck;
import main.anticheat.spigot.check.api.CheckInfo;
import main.anticheat.spigot.data.PlayerData;
import main.anticheat.spigot.exempt.type.ExemptType;
import main.anticheat.spigot.packet.Packet;
import main.anticheat.spigot.util.PlayerUtil;

@CheckInfo(name = "No Slow", type = 'B', complexType = "Soul Sand", description = "Moving too quickly on Soul Sand.")
public class NoSlowB extends AbstractCheck {
    public NoSlowB(final PlayerData data) {
        super(data);
    }

    @Override
    public void handle(final Packet packet) {
        if (packet.isPosition()) {
            final double deltaXZ = this.data.getPositionProcessor().getDeltaXZ();
            final double max = PlayerUtil.getBaseSpeed(this.data, 0.2f) + this.data.getActionProcessor().getSpeedAmplifier() * 0.0625f;
            final boolean onSoulSand = this.data.getPositionProcessor().isOnSoulSand();
            final boolean onGround = this.data.getPositionProcessor().isMathematicallyOnGround();
            final boolean exempt = this.isExempt(ExemptType.FLIGHT, ExemptType.CREATIVE, ExemptType.VELOCITY, ExemptType.TELEPORT, ExemptType.ENDER_PEARL, ExemptType.CHORUS_FRUIT, ExemptType.SOUL_SPEED, ExemptType.ELYTRA, ExemptType.RIPTIDE, ExemptType.HIGH_SPEED, ExemptType.DEPTH_STRIDER, ExemptType.ATTRIBUTE_MODIFIER);
            final boolean invalid = onSoulSand && deltaXZ > max && onGround;
            final boolean walkSpeed = this.data.getPositionProcessor().getWalkSpeed() > 0.11f || this.data.getActionProcessor().getGenericMovementSpeed() > 0.10999999940395355;
            if (invalid && !exempt && !walkSpeed) {
                if (this.increaseBuffer() > this.MAX_BUFFER) {
                    this.fail("deltaXZ=" + deltaXZ + " max=" + max);
                }
            } else {
                this.decayBuffer();
            }
        }
    }
}
