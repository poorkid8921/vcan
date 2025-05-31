package main.anticheat.spigot.check.impl.combat.hitbox;

import io.github.retrooper.packetevents.packetwrappers.play.in.useentity.WrappedPacketInUseEntity;
import main.anticheat.spigot.Anticheat;
import main.anticheat.spigot.check.AbstractCheck;
import main.anticheat.spigot.check.api.CheckInfo;
import main.anticheat.spigot.config.Config;
import main.anticheat.spigot.data.PlayerData;
import main.anticheat.spigot.exempt.type.ExemptType;
import main.anticheat.spigot.packet.Packet;
import main.anticheat.spigot.util.MathUtil;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

@CheckInfo(name = "Hitbox", type = 'A', complexType = "History", description = "Attacked while not looking at target.")
public class HitboxA extends AbstractCheck {
    private boolean attacking;

    public HitboxA(final PlayerData data) {
        super(data);
    }

    @Override
    public void handle(final Packet packet) {
        if (packet.isUseEntity()) {
            final WrappedPacketInUseEntity wrapper = this.data.getUseEntityWrapper();
            if (wrapper.getAction() != WrappedPacketInUseEntity.EntityUseAction.ATTACK || !(wrapper.getEntity() instanceof Player)) {
                return;
            }
            this.attacking = true;
        } else if (packet.isFlying()) {
            if (this.attacking) {
                final int ticks = Anticheat.INSTANCE.getTickManager().getTicks();
                final int pingTicks = MathUtil.getPingInTicks(this.data);
                final double x = this.data.getPositionProcessor().getX();
                final double z = this.data.getPositionProcessor().getZ();
                final Vector origin = new Vector(x, 0.0, z);
                final double angle = this.data.getCombatProcessor().getTargetLocations().stream().filter(pair -> Math.abs(ticks - pair.getY() - pingTicks) < 3).mapToDouble(pair -> {
                    final Vector targetLocation = pair.getX().toVector().setY(0.0);
                    final Vector direction = targetLocation.clone().subtract(origin);
                    final Vector target = this.getDirection(this.data.getRotationProcessor().getYaw(), this.data.getRotationProcessor().getPitch()).setY(0.0);
                    return (double) direction.angle(target);
                }).min().orElse(-1.0);
                final double distance = this.data.getCombatProcessor().getDistance();
                if (angle == -1.0) {
                    this.decreaseBufferBy(0.025);
                } else {
                    final PlayerData targetData = Anticheat.INSTANCE.getPlayerDataManager().getPlayerData(this.data.getCombatProcessor().getTrackedPlayer());
                    if (targetData == null) {
                        return;
                    }
                    final boolean targetExempt = targetData.getExemptProcessor().isExempt(ExemptType.VEHICLE, ExemptType.BOAT);
                    double maxAngle = Config.HITBOX_A_MAX_ANGLE;
                    if (System.currentTimeMillis() - this.data.getConnectionProcessor().getLastFast() < 1500L) {
                        maxAngle += 0.125;
                    }
                    final boolean exempt = this.isExempt(ExemptType.CREATIVE, ExemptType.GLIDING, ExemptType.VEHICLE);
                    final float deltaYaw = this.data.getRotationProcessor().getDeltaYaw();
                    if (angle > maxAngle && distance > 1.5 && distance < 10.0 && !exempt && !targetExempt) {
                        if (this.increaseBuffer() > this.MAX_BUFFER) {
                            this.fail("angle=" + angle + " distance=" + distance + " dYaw=" + deltaYaw);
                        }
                    } else {
                        this.decayBuffer();
                    }
                }
            }
            this.attacking = false;
        }
    }

    private Vector getDirection(final float yaw, final float pitch) {
        final Vector vector = new Vector();
        vector.setY(-Math.sin(Math.toRadians(pitch)));
        final double xz = Math.cos(Math.toRadians(pitch));
        vector.setX(-xz * Math.sin(Math.toRadians(yaw)));
        vector.setZ(xz * Math.cos(Math.toRadians(yaw)));
        return vector;
    }
}
