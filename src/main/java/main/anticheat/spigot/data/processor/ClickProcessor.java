package main.anticheat.spigot.data.processor;

import lombok.Getter;
import lombok.Setter;
import main.anticheat.spigot.data.PlayerData;
import main.anticheat.spigot.exempt.type.ExemptType;
import main.anticheat.spigot.util.MathUtil;
import main.anticheat.spigot.util.type.EvictingList;

@Getter
@Setter
public class ClickProcessor {
    private final PlayerData data;
    private final EvictingList<Long> samples;
    private long lastSwing;
    private long delay;
    private long lastInteractEntity;
    private double cps;
    private double kurtosis;

    public ClickProcessor(final PlayerData data) {
        this.lastSwing = -1L;
        this.lastInteractEntity = System.currentTimeMillis();
        this.samples = new EvictingList<Long>(20);
        this.data = data;
    }

    public void handleArmAnimation() {
        if (!this.data.getExemptProcessor().isExempt(ExemptType.AUTOCLICKER_NON_DIG)) {
            final long now = System.currentTimeMillis();
            if (this.lastSwing > 0L) {
                this.delay = now - this.lastSwing;
                this.samples.add(this.delay);
                if (this.samples.isFull()) {
                    this.cps = MathUtil.getCps(this.samples);
                }
            }
            this.lastSwing = now;
        }
    }
}
