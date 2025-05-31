package main.anticheat.spigot.check.impl.combat.autoclicker;

import com.google.common.collect.Lists;
import main.anticheat.spigot.check.AbstractCheck;
import main.anticheat.spigot.check.api.CheckInfo;
import main.anticheat.spigot.data.PlayerData;
import main.anticheat.spigot.exempt.type.ExemptType;
import main.anticheat.spigot.packet.Packet;

import java.util.Collections;
import java.util.List;

@CheckInfo(name = "Auto Clicker", type = 'J', complexType = "Range", description = "Impossible consistency.")
public class AutoClickerJ extends AbstractCheck {
    private final List<Long> samples;

    public AutoClickerJ(final PlayerData data) {
        super(data);
        this.samples = Lists.newArrayList();
    }

    @Override
    public void handle(final Packet packet) {
        if (packet.isArmAnimation() && !this.isExempt(ExemptType.AUTOCLICKER)) {
            final long delay = this.data.getClickProcessor().getDelay();
            if (delay > 5000L) {
                this.samples.clear();
                return;
            }
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
        }
    }
}
