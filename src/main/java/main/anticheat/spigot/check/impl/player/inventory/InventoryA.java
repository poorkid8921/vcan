package main.anticheat.spigot.check.impl.player.inventory;

import io.github.retrooper.packetevents.packetwrappers.play.in.windowclick.WrappedPacketInWindowClick;
import main.anticheat.spigot.check.AbstractCheck;
import main.anticheat.spigot.check.api.CheckInfo;
import main.anticheat.spigot.data.PlayerData;
import main.anticheat.spigot.exempt.type.ExemptType;
import main.anticheat.spigot.packet.Packet;

@CheckInfo(name = "Inventory", type = 'A', complexType = "Sprint", description = "Sprinting while clicking in inventory.")
public class InventoryA extends AbstractCheck {
    public InventoryA(final PlayerData data) {
        super(data);
    }

    @Override
    public void handle(final Packet packet) {
        if (packet.isWindowClick()) {
            final WrappedPacketInWindowClick wrapper = new WrappedPacketInWindowClick(packet.getRawPacket());
            final int sprintingTicks = this.data.getActionProcessor().getSprintingTicks();
            final double deltaXZ = this.data.getPositionProcessor().getDeltaXZ();
            final boolean exempt = this.isExempt(ExemptType.VEHICLE, ExemptType.TELEPORT, ExemptType.WORLD_CHANGE, ExemptType.SERVER_POSITION, ExemptType.ELYTRA, ExemptType.GLIDING, ExemptType.FIREWORK);
            final boolean invalid = sprintingTicks > 5 && deltaXZ > 0.25;
            if (invalid && !exempt && wrapper.getWindowId() == 0) {
                if (this.increaseBuffer() > this.MAX_BUFFER) {
                    this.fail("ticks=" + sprintingTicks);
                }
            } else {
                this.decayBuffer();
            }
        }
    }
}
