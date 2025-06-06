package main.anticheat.spigot.check.impl.combat.killaura;

import com.google.common.collect.Lists;
import io.github.retrooper.packetevents.packetwrappers.play.in.useentity.WrappedPacketInUseEntity;
import main.anticheat.spigot.check.AbstractCheck;
import main.anticheat.spigot.check.api.CheckInfo;
import main.anticheat.spigot.data.PlayerData;
import main.anticheat.spigot.exempt.type.ExemptType;
import main.anticheat.spigot.packet.Packet;

import java.util.Collections;
import java.util.List;

@CheckInfo(name = "Kill Aura", type = 'K', complexType = "Pattern", description = "Attack pattern.")
public class KillAuraK extends AbstractCheck {
    private final List<Long> samples;
    private long lastAttack;

    public KillAuraK(final PlayerData data) {
        super(data);
        this.samples = Lists.newArrayList();
    }

    @Override
    public void handle(final Packet packet) {
        if (packet.isUseEntity()) {
            final WrappedPacketInUseEntity wrapper = this.data.getUseEntityWrapper();
            if (wrapper.getAction() != WrappedPacketInUseEntity.EntityUseAction.ATTACK || this.isExempt(ExemptType.FAST)) {
                return;
            }
            final long delay = System.currentTimeMillis() - this.lastAttack;
            this.samples.add(delay);
            if (this.samples.size() == 10) {
                Collections.sort(this.samples);
                final long range = this.samples.get(this.samples.size() - 1) - this.samples.get(0);
                if (range < 50L) {
                    if (this.increaseBuffer() > this.MAX_BUFFER) {
                        this.fail("range=" + range);
                    }
                } else {
                    this.decayBuffer();
                }
                this.samples.clear();
            }
            this.lastAttack = System.currentTimeMillis();
        }
    }
}
