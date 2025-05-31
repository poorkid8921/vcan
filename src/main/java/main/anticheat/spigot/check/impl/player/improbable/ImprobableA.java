package main.anticheat.spigot.check.impl.player.improbable;

import io.github.retrooper.packetevents.packetwrappers.play.in.useentity.WrappedPacketInUseEntity;
import main.anticheat.spigot.check.AbstractCheck;
import main.anticheat.spigot.check.api.CheckInfo;
import main.anticheat.spigot.config.Config;
import main.anticheat.spigot.data.PlayerData;
import main.anticheat.spigot.packet.Packet;

@CheckInfo(name = "Improbable", type = 'A', complexType = "Combat", description = "Too many combined combat violations.")
public class ImprobableA extends AbstractCheck {
    private long lastFlag;

    public ImprobableA(final PlayerData data) {
        super(data);
    }

    @Override
    public void handle(final Packet packet) {
        if (packet.isUseEntity()) {
            final WrappedPacketInUseEntity wrapper = this.data.getUseEntityWrapper();
            if (wrapper.getAction() == WrappedPacketInUseEntity.EntityUseAction.ATTACK) {
                if (System.currentTimeMillis() - this.lastFlag >= 60000L) {
                    final int combatViolations = this.data.getCombatViolations();
                    if (combatViolations > Config.IMPROBABLE_A_MAX_VIOLATIONS) {
                        this.fail("combatViolations=" + combatViolations);
                        this.lastFlag = System.currentTimeMillis();
                    }
                }
            }
        }
    }
}
