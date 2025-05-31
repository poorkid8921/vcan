package main.anticheat.spigot.check.impl.combat.criticals;

import main.anticheat.spigot.check.AbstractCheck;
import main.anticheat.spigot.check.api.CheckInfo;
import main.anticheat.spigot.data.PlayerData;
import main.anticheat.spigot.exempt.type.ExemptType;
import main.anticheat.spigot.packet.Packet;

@CheckInfo(name = "Criticals", type = 'B', complexType = "Modulo", description = "Tried to hit Critical hit on ground.")
public class CriticalsB extends AbstractCheck {
    public CriticalsB(final PlayerData data) {
        super(data);
    }

    @Override
    public void handle(final Packet packet) {
        if (!packet.isUseEntity() || this.hitTicks() > 2)
            return;

        final double modulo = this.data.getPositionProcessor().getY() % 0.015625;
        final boolean invalid = modulo < 1.0E-5 && modulo > 0.0;
        final boolean exempt = this.isExempt(ExemptType.LIQUID, ExemptType.TRAPDOOR, ExemptType.COLLIDING_VERTICALLY, ExemptType.BOAT, ExemptType.TELEPORT, ExemptType.WEB, ExemptType.ENDER_PEARL, ExemptType.CHORUS_FRUIT, ExemptType.SERVER_POSITION, ExemptType.VEHICLE, ExemptType.FLIGHT, ExemptType.SLAB);
        if (invalid && !exempt) {
            if (this.increaseBuffer() > this.MAX_BUFFER) {
                this.setback();

                this.fail("modulo=" + modulo);
            }
        } else {
            this.decayBuffer();
        }
    }
}
