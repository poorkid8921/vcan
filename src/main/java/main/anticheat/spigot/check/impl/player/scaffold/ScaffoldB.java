package main.anticheat.spigot.check.impl.player.scaffold;

import main.anticheat.spigot.check.AbstractCheck;
import main.anticheat.spigot.check.api.CheckInfo;
import main.anticheat.spigot.data.PlayerData;
import main.anticheat.spigot.exempt.type.ExemptType;
import main.anticheat.spigot.packet.Packet;
import main.anticheat.spigot.util.BlockUtil;
import main.anticheat.spigot.util.PlayerUtil;
import org.bukkit.block.BlockFace;
import org.bukkit.event.block.BlockPlaceEvent;

@CheckInfo(name = "Scaffold", type = 'B', complexType = "Interact", description = "Invalid interact.")
public class ScaffoldB extends AbstractCheck {
    private long lastPlace;

    public ScaffoldB(final PlayerData data) {
        super(data);
    }

    @Override
    public void handle(final Packet packet) {
        if (packet.isBlockPlaceEvent()) {
            final BlockPlaceEvent event = (BlockPlaceEvent) packet.getRawPacket().getRawNMSPacket();
            if (event.isCancelled()) {
                return;
            }
            final boolean bridging = PlayerUtil.isBridging(event);
            final long delay = System.currentTimeMillis() - this.lastPlace;
            final BlockFace face = event.getBlockAgainst().getFace(event.getBlock());
            final boolean invalidBlock = BlockUtil.isTurtleEgg(event.getBlock()) || BlockUtil.isSlab(event.getBlock());
            final boolean exempt = this.isExempt(ExemptType.LIQUID, ExemptType.CREATIVE, ExemptType.FLIGHT, ExemptType.TURTLE_EGG, ExemptType.SLAB);
            final boolean invalid = bridging && face == BlockFace.SELF && delay < 300L;
            if (invalid && !exempt && !invalidBlock) {
                if (this.increaseBuffer() > this.MAX_BUFFER) {
                    this.fail("delay=" + delay + " face=" + face.toString().toLowerCase());
                }
            } else {
                this.decayBuffer();
            }
            this.lastPlace = System.currentTimeMillis();
        }
    }
}
