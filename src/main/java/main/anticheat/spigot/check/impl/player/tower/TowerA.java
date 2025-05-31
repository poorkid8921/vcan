package main.anticheat.spigot.check.impl.player.tower;

import main.anticheat.spigot.check.AbstractCheck;
import main.anticheat.spigot.check.api.CheckInfo;
import main.anticheat.spigot.data.PlayerData;
import main.anticheat.spigot.exempt.type.ExemptType;
import main.anticheat.spigot.packet.Packet;
import main.anticheat.spigot.util.BlockUtil;
import org.bukkit.block.BlockFace;
import org.bukkit.event.block.BlockPlaceEvent;

@CheckInfo(name = "Tower", type = 'A', complexType = "Limit", description = "Towering too quickly.")
public class TowerA extends AbstractCheck {
    private long lastPlace;

    public TowerA(final PlayerData data) {
        super(data);
    }

    @Override
    public void handle(final Packet packet) {
        if (packet.isBlockPlaceEvent()) {
            final BlockPlaceEvent event = (BlockPlaceEvent) packet.getRawPacket().getRawNMSPacket();
            if (event.isCancelled() || !event.getBlock().getType().isSolid()) {
                return;
            }
            final long delay = System.currentTimeMillis() - this.lastPlace;
            final double deltaXZ = this.data.getPositionProcessor().getDeltaXZ();
            final double deltaY = this.data.getPositionProcessor().getDeltaY();
            final boolean validX = this.data.getPlayer().getLocation().getBlockX() == event.getBlock().getLocation().getBlockX();
            final boolean validZ = this.data.getPlayer().getLocation().getBlockZ() == event.getBlock().getLocation().getBlockZ();
            final BlockFace face = event.getBlockAgainst().getFace(event.getBlock());
            final boolean invalidBlock = BlockUtil.isScaffolding(event.getBlock()) || BlockUtil.isString(event.getBlock()) || BlockUtil.isTurtleEgg(event.getBlock()) || BlockUtil.isClimbable(event.getBlock());
            final boolean exempt = this.isExempt(ExemptType.CREATIVE, ExemptType.FLIGHT, ExemptType.JUMP_BOOST, ExemptType.CANCELLED_PLACE, ExemptType.LIQUID, ExemptType.TURTLE_EGG, ExemptType.CLIMBABLE, ExemptType.BUKKIT_VELOCITY, ExemptType.EXPLOSION);
            final boolean invalid = face == BlockFace.UP && validX && validZ && delay < 250L && deltaY > 0.0 && deltaXZ < 0.1;
            if (invalid && !exempt && !invalidBlock) {
                if (this.increaseBuffer() > this.MAX_BUFFER) {
                    this.fail("delay=" + delay + " deltaY=" + deltaY);
                }
            } else {
                this.decayBuffer();
            }
            this.lastPlace = System.currentTimeMillis();
        }
    }
}
