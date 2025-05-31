package main.anticheat.spigot.check.impl.combat.reach;

import io.github.retrooper.packetevents.packetwrappers.play.in.useentity.WrappedPacketInUseEntity;
import main.anticheat.spigot.Anticheat;
import main.anticheat.spigot.check.AbstractCheck;
import main.anticheat.spigot.check.api.CheckInfo;
import main.anticheat.spigot.config.Config;
import main.anticheat.spigot.data.PlayerData;
import main.anticheat.spigot.exempt.type.ExemptType;
import main.anticheat.spigot.packet.Packet;
import main.anticheat.spigot.util.MathUtil;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

@CheckInfo(name = "Reach", type = 'A', complexType = "History", description = "Hit from too far away.")
public class ReachA extends AbstractCheck {
    private boolean attacking;

    public ReachA(final PlayerData data) {
        super(data);
        this.attacking = true;
    }

    @Override
    public void handle(final Packet packet) {
        if (packet.isUseEntity()) {
            final WrappedPacketInUseEntity wrapper = this.data.getUseEntityWrapper();
            if (wrapper.getAction() != WrappedPacketInUseEntity.EntityUseAction.ATTACK || !(wrapper.getEntity() instanceof Player)) {
                return;
            }
            this.attacking = true;
        } else if (packet.isFlying() && this.attacking) {
            final int ticks = Anticheat.INSTANCE.getTickManager().getTicks();
            final int pingTicks = MathUtil.getPingInTicks(this.data);
            final double x = this.data.getPositionProcessor().getX();
            final double z = this.data.getPositionProcessor().getZ();
            final Vector origin = new Vector(x, 0.0, z);
            final double distance = this.data.getCombatProcessor().getTargetLocations().stream().filter(pair -> Math.abs(ticks - pair.getY() - pingTicks) < 3).mapToDouble(pair -> {
                final Vector victimVector = pair.getX().toVector().setY(0);
                return origin.distance(victimVector) - 0.52;
            }).min().orElse(-1.0);
            if (distance == -1.0) {
                this.decreaseBufferBy(0.025);
            } else {
                final PlayerData targetData = Anticheat.INSTANCE.getPlayerDataManager().getPlayerData(this.data.getCombatProcessor().getTrackedPlayer());
                if (targetData == null) {
                    return;
                }
                final boolean knockback = this.data.getPlayer().getItemInHand().getEnchantmentLevel(Enchantment.KNOCKBACK) > 0;
                final boolean exempt = this.isExempt(ExemptType.CREATIVE, ExemptType.VEHICLE, ExemptType.BOAT);
                final boolean targetExempt = targetData.getExemptProcessor().isExempt(ExemptType.VEHICLE, ExemptType.BOAT);
                double max = Config.REACH_A_MAX_REACH;
                if (System.currentTimeMillis() - this.data.getConnectionProcessor().getLastFast() < 1500L) {
                    max += 1.15;
                }
                if (distance > max && distance < 8.0 && !knockback && !exempt && !targetExempt) {
                    if (this.increaseBuffer() > this.MAX_BUFFER) {
                        this.fail("distance=" + distance);
                    }
                } else {
                    this.decayBuffer();
                }
            }
            this.attacking = false;
        }
    }
}
