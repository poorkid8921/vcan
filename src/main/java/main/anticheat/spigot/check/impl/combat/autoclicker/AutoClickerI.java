package main.anticheat.spigot.check.impl.combat.autoclicker;

import com.google.common.collect.Lists;
import main.anticheat.spigot.check.AbstractCheck;
import main.anticheat.spigot.check.api.CheckInfo;
import main.anticheat.spigot.data.PlayerData;
import main.anticheat.spigot.exempt.type.ExemptType;
import main.anticheat.spigot.packet.Packet;
import main.anticheat.spigot.util.MathUtil;

import java.util.Deque;

@CheckInfo(name = "Auto Clicker", type = 'I', complexType = "Kurtosis", description = "Too low kurtosis.")
public class AutoClickerI extends AbstractCheck {
    private final Deque<Long> samples;

    public AutoClickerI(final PlayerData data) {
        super(data);
        this.samples = Lists.newLinkedList();
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
            if (this.samples.size() == 50) {
                final double kurtosis = MathUtil.getKurtosis(this.samples);
                final double scaled = kurtosis / 1000.0;
                if (scaled < 41.78) {
                    if (this.increaseBuffer() > this.MAX_BUFFER) {
                        this.fail("kurtosis=" + scaled);
                    }
                } else {
                    this.decayBuffer();
                }
                this.samples.clear();
            }
        }
    }
}
