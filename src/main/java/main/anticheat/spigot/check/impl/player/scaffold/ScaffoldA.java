package main.anticheat.spigot.check.impl.player.scaffold;

import main.anticheat.spigot.check.AbstractCheck;
import main.anticheat.spigot.check.api.CheckInfo;
import main.anticheat.spigot.data.PlayerData;
import main.anticheat.spigot.exempt.type.ExemptType;
import main.anticheat.spigot.packet.Packet;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;

@CheckInfo(name = "Scaffold", type = 'A', complexType = "Interact", description = "Interacted with the bottom of block.")
public class ScaffoldA extends AbstractCheck {
    public ScaffoldA(final PlayerData data) {
        super(data);
    }

    @Override
    public void handle(final Packet packet) {
        if (packet.isInteractEvent()) {
            final PlayerInteractEvent event = (PlayerInteractEvent) packet.getRawPacket().getRawNMSPacket();
            if (event.getAction().equals(Action.RIGHT_CLICK_BLOCK)) {
                final Block below = this.data.getPlayer().getLocation().subtract(0.0, 1.0, 0.0).getBlock();
                final boolean exempt = this.isExempt(ExemptType.TELEPORT, ExemptType.SERVER_POSITION);
                final boolean invalid = event.getClickedBlock() != null && event.getClickedBlock().equals(below) && event.getBlockFace() == BlockFace.DOWN && event.getClickedBlock().getType().isSolid() && this.data.getPlayer().getItemInHand().getType().isSolid();
                if (invalid && !exempt) {
                    this.fail("block=" + event.getClickedBlock().getType());
                }
            }
        }
    }
}
