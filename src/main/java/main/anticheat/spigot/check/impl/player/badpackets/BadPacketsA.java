package main.anticheat.spigot.check.impl.player.badpackets;

import io.github.retrooper.packetevents.packetwrappers.play.in.abilities.WrappedPacketInAbilities;
import main.anticheat.spigot.check.AbstractCheck;
import main.anticheat.spigot.check.api.CheckInfo;
import main.anticheat.spigot.data.PlayerData;
import main.anticheat.spigot.exempt.type.ExemptType;
import main.anticheat.spigot.packet.Packet;
import main.anticheat.spigot.util.ServerUtil;

import java.util.Optional;

@CheckInfo(name = "Bad Packets", type = 'A', complexType = "Abilities", description = "Spoofed abilities packets.")
public class BadPacketsA extends AbstractCheck {
    private int lastServerAbilities;

    public BadPacketsA(final PlayerData data) {
        super(data);
    }

    @Override
    public void handle(final Packet packet) {
        if (packet.isAbilities()) {
            if (this.data.getPositionProcessor().isFuckedPosition()) {
                return;
            }
            final WrappedPacketInAbilities wrapper = new WrappedPacketInAbilities(packet.getRawPacket());
            final boolean exempt = this.isExempt(ExemptType.DEATH);
            if (!this.data.getPlayer().getAllowFlight() && !exempt && this.ticks() - this.lastServerAbilities >= 200) {
                if (!this.data.getPositionProcessor().isAllowFlight()) {
                    final Optional<Boolean> flightAllowed = wrapper.isFlightAllowed();
                    if (ServerUtil.isHigherThan1_9()) {
                        if (this.increaseBuffer() > this.MAX_BUFFER) {
                            this.fail("flying=" + wrapper.isFlying());
                        } else {
                            this.decayBuffer();
                        }
                    } else if (flightAllowed.isPresent() && flightAllowed.get()) {
                        if (this.increaseBuffer() > this.MAX_BUFFER) {
                            this.fail("flightAllowed=" + wrapper.isFlightAllowed());
                        } else {
                            this.decayBuffer();
                        }
                    }
                }
            }
        } else if (packet.isAbilitiesOut()) {
            this.lastServerAbilities = this.ticks();
        }
    }
}
