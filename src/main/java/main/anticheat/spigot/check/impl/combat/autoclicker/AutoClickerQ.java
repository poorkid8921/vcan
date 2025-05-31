package main.anticheat.spigot.check.impl.combat.autoclicker;

import main.anticheat.spigot.check.AbstractCheck;
import main.anticheat.spigot.check.api.CheckInfo;
import main.anticheat.spigot.data.PlayerData;
import main.anticheat.spigot.exempt.type.ExemptType;
import main.anticheat.spigot.packet.Packet;
import main.anticheat.spigot.util.MathUtil;
import main.anticheat.spigot.util.type.EvictingList;

@CheckInfo(name = "Auto Clicker", type = 'Q', complexType = "Average Deviation", description = "Too low average deviation.")
public class AutoClickerQ extends AbstractCheck {
    private final EvictingList<Long> samples;
    private double lastDeviation;

    public AutoClickerQ(final PlayerData data) {
        super(data);
        this.samples = new EvictingList<Long>(40);
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
                final double deviation = MathUtil.getStandardDeviation(this.samples);
                final double difference = Math.abs(deviation - this.lastDeviation);
                final double average = Math.abs(deviation + this.lastDeviation) / 2.0;
                if (difference < 3.0 && average < 150.0) {
                    if (this.increaseBuffer() > this.MAX_BUFFER) {
                        this.fail("difference=" + difference + " average=" + average);
                    }
                } else {
                    this.decayBuffer();
                }
                this.lastDeviation = deviation;
            }
        }
    }
}
