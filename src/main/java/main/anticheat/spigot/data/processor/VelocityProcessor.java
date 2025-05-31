package main.anticheat.spigot.data.processor;

import io.github.retrooper.packetevents.packetwrappers.play.in.pong.WrappedPacketInPong;
import io.github.retrooper.packetevents.packetwrappers.play.in.transaction.WrappedPacketInTransaction;
import lombok.Getter;
import lombok.Setter;
import main.anticheat.spigot.Anticheat;
import main.anticheat.spigot.data.PlayerData;
import main.anticheat.spigot.util.MathUtil;
import main.anticheat.spigot.util.PlayerUtil;
import main.anticheat.spigot.util.ServerUtil;
import org.bukkit.util.Vector;

import java.util.HashMap;
import java.util.Map;

@Getter
@Setter
public class VelocityProcessor {
    private final PlayerData data;
    private final Map<Short, VelocitySnapshot> velocities;
    private double velocityX;
    private double velocityY;
    private double velocityZ;
    private double velocityXZ;
    private long lastRepliedVelocityConfirmation;
    private long lastSentVelocityTransaction;
    private short transactionId;
    private short pingId;
    private VelocitySnapshot snapshot;
    private int tick;
    private int transactionFlyingTicks;
    private int sinceHighVelocityTicks;

    public VelocityProcessor(final PlayerData data) {
        this.lastRepliedVelocityConfirmation = System.currentTimeMillis();
        this.lastSentVelocityTransaction = System.currentTimeMillis();
        this.transactionId = -31768;
        this.pingId = -31768;
        this.velocities = new HashMap<>();
        this.transactionFlyingTicks = 100;
        this.sinceHighVelocityTicks = 100;
        this.data = data;
    }

    public void handle(final double velocityX, final double velocityY, final double velocityZ) {
        this.velocityX = velocityX;
        this.velocityY = velocityY;
        this.velocityZ = velocityZ;
        this.velocityXZ = MathUtil.hypot(velocityX, velocityZ);
        if (this.velocityXZ > 1.0) {
            this.sinceHighVelocityTicks = 0;
        }
        this.tick = Anticheat.INSTANCE.getTickManager().getTicks();
        ++this.transactionId;
        ++this.pingId;
        if (this.transactionId > -30769) {
            this.transactionId = -31768;
        }
        if (this.pingId > -30769) {
            this.pingId = -31768;
        }
        if (ServerUtil.isHigherThan1_17()) {
            this.data.sendPing(this.pingId);
        } else {
            PlayerUtil.sendTransaction(this.data.getPlayer(), this.transactionId);
        }
        this.lastSentVelocityTransaction = System.currentTimeMillis();
        this.velocities.put(this.transactionId, new VelocitySnapshot(new Vector(velocityX, velocityY, velocityZ), this.transactionId));
        final int lastDamage = this.data.getActionProcessor().getLastDamage();
        if (this.tick - lastDamage >= 3) {
            this.data.getActionProcessor().handleBukkitVelocity();
        }
    }

    public void handleFlying() {
        ++this.transactionFlyingTicks;
        ++this.sinceHighVelocityTicks;
    }

    public void handleTransaction(final WrappedPacketInTransaction wrapper) {
        if (this.velocities.containsKey(wrapper.getActionNumber())) {
            this.transactionFlyingTicks = 0;
            this.snapshot = this.velocities.get(wrapper.getActionNumber());
            this.velocities.remove(wrapper.getActionNumber());
            this.lastRepliedVelocityConfirmation = System.currentTimeMillis();
        }
    }

    public void handlePong(final WrappedPacketInPong wrapper) {
        final short id = (short) wrapper.getId();
        if (this.velocities.containsKey(id)) {
            this.transactionFlyingTicks = 0;
            this.snapshot = this.velocities.get(id);
            this.velocities.remove(id);
            this.lastRepliedVelocityConfirmation = System.currentTimeMillis();
        }
    }

    public static class VelocitySnapshot {
        private final Vector velocity;
        private final short transactionId;

        public VelocitySnapshot(final Vector velocity, final short transactionId) {
            this.velocity = velocity;
            this.transactionId = transactionId;
        }

        public Vector getVelocity() {
            return this.velocity;
        }

        public short getTransactionId() {
            return this.transactionId;
        }
    }
}
