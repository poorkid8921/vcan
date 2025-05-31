package main.anticheat.spigot.check.impl.player.fastuse;

import main.anticheat.spigot.check.AbstractCheck;
import main.anticheat.spigot.check.api.CheckInfo;
import main.anticheat.spigot.data.PlayerData;
import main.anticheat.spigot.exempt.type.ExemptType;
import main.anticheat.spigot.packet.Packet;
import main.anticheat.spigot.util.BlockUtil;
import org.bukkit.event.player.PlayerItemConsumeEvent;

@CheckInfo(name = "Fast Use", type = 'A', complexType = "Delay", description = "Using items too quickly.")
public class FastUseA extends AbstractCheck {
    private long startEat;

    public FastUseA(final PlayerData data) {
        super(data);
    }

    @Override
    public void handle(final Packet packet) {
        if (packet.isBlockPlace()) {
            this.startEat = System.currentTimeMillis();
        } else if (packet.isItemConsumeEvent()) {
            final PlayerItemConsumeEvent event = (PlayerItemConsumeEvent) packet.getRawPacket().getRawNMSPacket();
            if (BlockUtil.isKelp(event.getItem().getType())) {
                return;
            }
            final long delay = System.currentTimeMillis() - this.startEat;
            final long flyingDelay = this.data.getConnectionProcessor().getFlyingDelay();
            final boolean invalid = delay < 1000L && flyingDelay < 10L;
            final boolean exempt = this.isExempt(ExemptType.DROPPED_ITEM, ExemptType.PICKED_UP_ITEM);
            if (invalid && !exempt) {
                this.fail("delay=" + delay + " item=" + this.data.getPlayer().getItemInHand().getType() + " delay=" + flyingDelay);
            }
        }
    }
}
