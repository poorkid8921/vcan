package main.anticheat.spigot.check.impl.movement.flight;

import main.anticheat.spigot.check.AbstractCheck;
import main.anticheat.spigot.check.api.CheckInfo;
import main.anticheat.spigot.data.PlayerData;
import main.anticheat.spigot.data.processor.PositionProcessor;
import main.anticheat.spigot.exempt.type.ExemptType;
import main.anticheat.spigot.packet.Packet;

@CheckInfo(name = "Flight", type = 'F', complexType = "Prediction [S]", description = "Invalid XY-Axis movement.")
public class FlightF extends AbstractCheck {
    public FlightF(final PlayerData data) {
        super(data);
    }

    @Override
    public void handle(final Packet packet) {
        if (!packet.isFlying() || fuckedPosition() || teleporting()) return;

        PositionProcessor posProcessor = data.getPositionProcessor();
        if (data.getPlayer().getAllowFlight() || posProcessor.isAllowFlight() ||
                (posProcessor.isClientOnGround() && posProcessor.isTouchingAir() && posProcessor.isMathematicallyOnGround()))
            return;

        double deltaY = posProcessor.getDeltaY();
        boolean ladder = Math.abs(deltaY - 0.11760000228882461) <= 0.001;
        if ((ladder && isExempt(ExemptType.FAST)) || deltaY + 0.09800000190734881 <= 0.001) return;

        if (posProcessor.isAirBelow()) return;

        int airTicks = posProcessor.getServerAirTicks();
        double lastDeltaY = posProcessor.getLastDeltaY();
        double prediction = (lastDeltaY - 0.08) * 0.9800000190734863;
        double difference = Math.abs(deltaY - prediction);
        boolean explosion = data.getActionProcessor().getSinceExplosionDamageTicks() < 40;

        if (airTicks < 30 && isExempt(ExemptType.PISTON)) return;

        long delay = data.getConnectionProcessor().getFlyingDelay();
        int maxTicks = 6 + (data.getActionProcessor().isHasJumpBoost() ? data.getActionProcessor().getJumpBoostAmplifier() : 0);

        boolean invalid = airTicks > maxTicks && difference > 1.0E-8 && airTicks > 6;
        boolean exempt = isExempt(
                ExemptType.SHULKER_BOX, ExemptType.WEB, ExemptType.BOAT, ExemptType.ILLEGAL_BLOCK, ExemptType.SLEEPING,
                ExemptType.SHULKER, ExemptType.TRAPDOOR, ExemptType.FISHING_ROD, ExemptType.SERVER_POSITION_FAST,
                ExemptType.LILY_PAD, ExemptType.SPECTATOR, ExemptType.HONEY, ExemptType.CHORUS_FRUIT, ExemptType.BED,
                ExemptType.CHAIN, ExemptType.WORLD_CHANGE, ExemptType.CANCELLED_PLACE, ExemptType.LAGGED_NEAR_GROUND_,
                ExemptType.EMPTIED_BUCKET, ExemptType.DEAD, ExemptType.CANCELLED_BREAK, ExemptType.FENCE,
                ExemptType.LAGGED_NEAR_GROUND, ExemptType.BLOCK_PLACE_FAST, ExemptType.GLASS_PANE, ExemptType.NOT_MOVING
        ) || posProcessor.isExemptFlight() || posProcessor.isExemptCreative() || posProcessor.isExemptJoined()
                || posProcessor.isExemptLiquid() || posProcessor.isExemptLevitation() || posProcessor.isExemptSlowFalling()
                || posProcessor.isExemptRiptide() || posProcessor.isExemptVehicle() || posProcessor.isExemptLenientScaffolding()
                || posProcessor.isExemptBukkitVelocity() || posProcessor.isExemptTeleport() || posProcessor.isExemptEnderPearl()
                || posProcessor.isExemptGliding() || posProcessor.isExemptElytra() || posProcessor.isExemptChunk()
                || posProcessor.isExemptComboMode() || posProcessor.isExemptMythicMob() || posProcessor.isExemptClimbable();

        int sinceTeleportTicks = data.getActionProcessor().getSinceTeleportTicks();

        if (invalid && !exempt && !explosion) {
            if (increaseBuffer() > MAX_BUFFER) {
                fail("deltaY=" + deltaY + " difference=" + difference + " ticks=" + airTicks + " delay=" + delay + " tp=" + sinceTeleportTicks);
            }
        } else {
            decayBuffer();
        }
    }
}