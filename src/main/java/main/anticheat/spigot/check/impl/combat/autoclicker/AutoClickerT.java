package main.anticheat.spigot.check.impl.combat.autoclicker;

import main.anticheat.spigot.check.AbstractCheck;
import main.anticheat.spigot.check.api.CheckInfo;
import main.anticheat.spigot.config.Config;
import main.anticheat.spigot.data.PlayerData;
import main.anticheat.spigot.exempt.type.ExemptType;
import main.anticheat.spigot.packet.Packet;
import main.anticheat.spigot.util.MathUtil;
import main.anticheat.spigot.util.type.EvictingList;

@CheckInfo(name = "Auto Clicker", type = 'T', complexType = "Kurtosis", description = "Too low kurtosis")
public class AutoClickerT extends AbstractCheck {
    private final EvictingList<Long> samples;

    public AutoClickerT(final PlayerData data) {
        super(data);
        this.samples = new EvictingList<Long>(25);
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
                final double kurtosis = MathUtil.getKurtosis(this.samples) / 1000.0;
                this.data.getClickProcessor().setKurtosis(kurtosis);
                if (kurtosis < Config.AUTOCLICKER_T_MIN_KURTOSIS) {
                    if (this.increaseBuffer() > this.MAX_BUFFER) {
                        this.fail("kurtosis=" + kurtosis);
                    }
                } else {
                    this.decayBuffer();
                }
            }
        }
    }
}
