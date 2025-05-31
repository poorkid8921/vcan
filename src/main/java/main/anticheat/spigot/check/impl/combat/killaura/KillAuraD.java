package main.anticheat.spigot.check.impl.combat.killaura;

import io.github.retrooper.packetevents.packetwrappers.play.in.useentity.WrappedPacketInUseEntity;
import main.anticheat.spigot.check.AbstractCheck;
import main.anticheat.spigot.check.api.CheckInfo;
import main.anticheat.spigot.data.PlayerData;
import main.anticheat.spigot.exempt.type.ExemptType;
import main.anticheat.spigot.packet.Packet;

@CheckInfo(name = "Kill Aura", type = 'D', complexType = "Multi Aura", description = "Attacked two entities at once.")
public class KillAuraD extends AbstractCheck {
    private int lastEntityId;
    private int ticks;

    public KillAuraD(final PlayerData data) {
        super(data);
    }

    @Override
    public void handle(final Packet packet) {
        if (packet.isUseEntity()) {
            final WrappedPacketInUseEntity wrapper = this.data.getUseEntityWrapper();
            if (wrapper.getAction() != WrappedPacketInUseEntity.EntityUseAction.ATTACK) {
                return;
            }
            final boolean exempt = this.isExempt(ExemptType.CLIENT_VERSION, ExemptType.SERVER_VERSION);
            final int id = wrapper.getEntityId();
            if (id != this.lastEntityId && !exempt && ++this.ticks > 1) {
                this.fail("id=" + id + " lastId=" + this.lastEntityId);
            }
            this.lastEntityId = id;
        } else if (packet.isFlying()) {
            this.ticks = 0;
        }
    }
}
