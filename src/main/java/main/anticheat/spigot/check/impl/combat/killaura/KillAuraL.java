package main.anticheat.spigot.check.impl.combat.killaura;

import io.github.retrooper.packetevents.utils.player.ClientVersion;
import main.anticheat.spigot.check.AbstractCheck;
import main.anticheat.spigot.check.api.CheckInfo;
import main.anticheat.spigot.data.PlayerData;
import main.anticheat.spigot.exempt.type.ExemptType;
import main.anticheat.spigot.packet.Packet;
import main.anticheat.spigot.util.PlayerUtil;
import org.bukkit.entity.Player;

@CheckInfo(name = "Kill Aura", type = 'L', complexType = "Strafe", experimental = true, description = "Impossible strafe.")
public class KillAuraL extends AbstractCheck {
    public KillAuraL(final PlayerData data) {
        super(data);
    }

    @Override
    public void handle(final Packet packet) {
        if (packet.isPosition() && this.hitTicks() < 3) {
            final double deltaXZ = this.data.getPositionProcessor().getDeltaXZ();
            final double acceleration = this.data.getPositionProcessor().getAcceleration();
            final float baseSpeed = PlayerUtil.getBaseSpeed(this.data, 0.275f);
            final long swingDelay = this.data.getClickProcessor().getDelay();
            final boolean sprinting = this.data.getActionProcessor().isSprinting();
            final boolean validTarget = this.data.getCombatProcessor().getTarget() != null && this.data.getCombatProcessor().getTarget() instanceof Player;
            final boolean validVersion = PlayerUtil.getClientVersion(this.data.getPlayer()) != null && PlayerUtil.getClientVersion(this.data.getPlayer()).isLowerThan(ClientVersion.v_1_9);
            final boolean combo = this.data.getPlayer().getMaximumNoDamageTicks() < 18 || (validTarget && ((Player) this.data.getCombatProcessor().getTarget()).getMaximumNoDamageTicks() < 18);
            final boolean exempt = this.isExempt(ExemptType.SOUL_SAND);
            final boolean invalid = acceleration < 0.0025 && !sprinting && deltaXZ > baseSpeed && swingDelay < 500L && validTarget && validVersion;
            if (invalid && !exempt && !combo) {
                if (this.increaseBuffer() > this.MAX_BUFFER) {
                    this.fail("d=" + deltaXZ + " a=" + acceleration + " s=" + swingDelay);
                }
            } else {
                this.decayBuffer();
            }
        }
    }
}
