package main.anticheat.spigot.check.impl.combat.autoclicker;

import main.anticheat.spigot.check.AbstractCheck;
import main.anticheat.spigot.check.api.CheckInfo;
import main.anticheat.spigot.data.PlayerData;
import main.anticheat.spigot.exempt.type.ExemptType;
import main.anticheat.spigot.packet.Packet;
import main.anticheat.spigot.util.MathUtil;
import main.anticheat.spigot.util.type.EvictingList;

@CheckInfo(name = "Auto Clicker", type = 'R', complexType = "Consistency", description = "Impossible consistency.")
public class AutoClickerR extends AbstractCheck {
    private final EvictingList<Long> samples;

    public AutoClickerR(final PlayerData data) {
        super(data);
        this.samples = new EvictingList<Long>(30);
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
            if (this.samples.isFull()) {
                final int outliers = (int) this.samples.stream().filter(l -> l > 150L).count();
                final double average = MathUtil.getAverage(this.samples);
                if (outliers == 0) {
                    if (this.increaseBuffer() > this.MAX_BUFFER) {
                        this.fail("outliers=" + outliers + " average=" + average);
                    }
                } else {
                    this.decayBuffer();
                }
            }
        }
    }
}
