package main.anticheat.spigot.check.impl.player.scaffold;

import io.github.retrooper.packetevents.packetwrappers.play.in.entityaction.WrappedPacketInEntityAction;
import main.anticheat.spigot.check.AbstractCheck;
import main.anticheat.spigot.check.api.CheckInfo;
import main.anticheat.spigot.config.Config;
import main.anticheat.spigot.data.PlayerData;
import main.anticheat.spigot.exempt.type.ExemptType;
import main.anticheat.spigot.packet.Packet;
import main.anticheat.spigot.util.BlockUtil;
import main.anticheat.spigot.util.PlayerUtil;
import org.bukkit.event.block.BlockPlaceEvent;

import java.util.HashMap;
import java.util.Map;

@CheckInfo(name = "Scaffold", type = 'K', complexType = "Limit", description = "Bridging too quickly.")
public class ScaffoldK extends AbstractCheck {
    private final Map<Integer, Long> placed;
    private int lastSneak;
    private int blocksPlaced;

    public ScaffoldK(final PlayerData data) {
        super(data);
        this.placed = new HashMap<Integer, Long>();
    }

    @Override
    public void handle(final Packet packet) {
        if (packet.isBlockPlaceEvent()) {
            final BlockPlaceEvent event = (BlockPlaceEvent) packet.getRawPacket().getRawNMSPacket();
            if (event.isCancelled() || BlockUtil.isSlab(event.getBlock().getType()) || this.isExempt(ExemptType.SLAB)) {
                return;
            }
            if (PlayerUtil.isBridging(event) && this.ticks() - this.lastSneak > 20) {
                ++this.blocksPlaced;
                this.placed.put(this.blocksPlaced, System.currentTimeMillis());
                if (System.currentTimeMillis() - this.placed.get(1) > 1000L) {
                    final int amount = this.placed.size();
                    if (amount >= Config.SCAFFOLD_K_MAX_BLOCKS) {
                        if (this.increaseBuffer() > this.MAX_BUFFER) {
                            this.fail("amount=" + amount);
                        }
                    } else {
                        this.decayBuffer();
                    }
                    this.placed.clear();
                    this.blocksPlaced = 0;
                }
            }
        } else if (packet.isEntityAction()) {
            final WrappedPacketInEntityAction wrapper = this.data.getEntityActionWrapper();
            if (wrapper.getAction() == WrappedPacketInEntityAction.PlayerAction.START_SNEAKING || wrapper.getAction() == WrappedPacketInEntityAction.PlayerAction.STOP_SNEAKING) {
                this.lastSneak = this.ticks();
            }
        }
    }
}
