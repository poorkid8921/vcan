package main.anticheat.spigot.check.impl.player.timer;

import main.anticheat.spigot.check.AbstractCheck;
import main.anticheat.spigot.check.api.CheckInfo;
import main.anticheat.spigot.config.Config;
import main.anticheat.spigot.data.PlayerData;
import main.anticheat.spigot.exempt.type.ExemptType;
import main.anticheat.spigot.packet.Packet;
import main.anticheat.spigot.util.MathUtil;
import main.anticheat.spigot.util.type.EvictingList;

@CheckInfo(name = "Timer", type = 'A', complexType = "Average", description = "Increased game speed.")
public class TimerA extends AbstractCheck {
    private final EvictingList<Long> samples;
    private long lastFlying;

    public TimerA(final PlayerData data) {
        super(data);
        this.samples = new EvictingList<Long>(50);
    }

    @Override
    public void handle(final Packet packet) {
        if (packet.isFlying() && !this.fuckedPosition()) {
            final long delay = System.currentTimeMillis() - this.lastFlying;
            final boolean exempt = this.isExempt(ExemptType.JOINED, ExemptType.TPS, ExemptType.VEHICLE);
            if (this.data.getActionProcessor().isSitting()) {
                return;
            }
            if (delay > 4L && !exempt) {
                this.samples.add(delay);
            }
            if (this.samples.isFull()) {
                final double average = MathUtil.getAverage(this.samples);
                final double speed = 50.0 / average;
                final double scaled = speed * 100.0;
                if (speed >= Config.TIMER_A_MAX_SPEED) {
                    if (this.increaseBuffer() > this.MAX_BUFFER) {
                        this.fail("speed=" + scaled + "% delay=" + delay);
                    }
                } else {
                    this.decayBuffer();
                }
            }
            this.lastFlying = System.currentTimeMillis();
        } else if (packet.isTeleport()) {
            this.samples.add(150L);
        }
    }
}
