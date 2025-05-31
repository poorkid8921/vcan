package main.anticheat.spigot.check.impl.player.badpackets;

import io.github.retrooper.packetevents.packetwrappers.play.in.blockdig.WrappedPacketInBlockDig;
import main.anticheat.spigot.check.AbstractCheck;
import main.anticheat.spigot.check.api.CheckInfo;
import main.anticheat.spigot.data.PlayerData;
import main.anticheat.spigot.exempt.type.ExemptType;
import main.anticheat.spigot.packet.Packet;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;

@CheckInfo(name = "Bad Packets", type = '2', complexType = "Nuker", description = "Sent too many Stop Dig packets.")
public class BadPackets2 extends AbstractCheck {
    private long lastStop;

    public BadPackets2(final PlayerData data) {
        super(data);
    }

    @Override
    public void handle(final Packet packet) {
        if (packet.isBlockDig()) {
            final WrappedPacketInBlockDig wrapper = this.data.getBlockDigWrapper();
            if (wrapper.getDigType() == WrappedPacketInBlockDig.PlayerDigType.STOP_DESTROY_BLOCK) {
                final long now = System.currentTimeMillis();
                final long delay = now - this.lastStop;
                final ItemStack tool = this.data.getPlayer().getItemInHand();
                if (tool.containsEnchantment(Enchantment.DIG_SPEED) && tool.getEnchantmentLevel(Enchantment.DIG_SPEED) > 5) {
                    return;
                }
                final boolean exempt = this.isExempt(ExemptType.FAST);
                final boolean invalid = delay < 3L;
                if (invalid && !exempt) {
                    if (this.increaseBuffer() > this.MAX_BUFFER) {
                        this.fail("delay=" + delay);
                    }
                } else {
                    this.decayBuffer();
                }
                this.lastStop = System.currentTimeMillis();
            }
        }
    }
}
