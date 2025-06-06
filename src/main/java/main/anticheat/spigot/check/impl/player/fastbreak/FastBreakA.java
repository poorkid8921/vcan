package main.anticheat.spigot.check.impl.player.fastbreak;

import main.anticheat.spigot.check.AbstractCheck;
import main.anticheat.spigot.check.api.CheckInfo;
import main.anticheat.spigot.config.Config;
import main.anticheat.spigot.data.PlayerData;
import main.anticheat.spigot.exempt.type.ExemptType;
import main.anticheat.spigot.packet.Packet;
import main.anticheat.spigot.util.BlockUtil;
import main.anticheat.spigot.util.BreakUtils;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.player.PlayerInteractEvent;

@CheckInfo(name = "Fast Break", type = 'A', complexType = "Delay", description = "Breaking blocks too quickly.")
public class FastBreakA extends AbstractCheck {
    private long time;

    public FastBreakA(final PlayerData data) {
        super(data);
    }

    @Override
    public void handle(final Packet packet) {
        if (packet.isInteractEvent()) {
            final PlayerInteractEvent event = (PlayerInteractEvent) packet.getRawPacket().getRawNMSPacket();
            if (event.getAction() != Action.LEFT_CLICK_BLOCK || event.isCancelled() || event.getPlayer().getGameMode() != GameMode.SURVIVAL) {
                return;
            }
            final Block block = event.getClickedBlock();
            if (block == null || block.getType() == Material.AIR || block.getType().toString().contains("DOOR") || BlockUtil.isTrapdoor(block.getType()) || block.getType().toString().contains("CONCRETE") || block.getType().toString().contains("TRAP") || block.getType().toString().contains("INFESTED") || BlockUtil.isDoor(block.getType()) || block.getType().toString().equals("FIRE") || block.getType().toString().contains("MUSHROOM") || block.getType().toString().contains("NOTEBLOCK")) {
                return;
            }
            this.time = System.currentTimeMillis() + BreakUtils.getDigTime(block, event.getPlayer());
        } else if (packet.isBlockBreakEvent()) {
            final BlockBreakEvent event2 = (BlockBreakEvent) packet.getRawPacket().getRawNMSPacket();
            final Block block = event2.getBlock();
            if (event2.getPlayer().getGameMode() != GameMode.SURVIVAL || this.data.getActionProcessor().isBerserking() || BlockUtil.isAmethyst(event2.getBlock().getType()) || this.isExempt(ExemptType.VEHICLE) || event2.getBlock().getType().toString().contains("INFESTED") || this.data.getPlayer().getItemInHand().getEnchantmentLevel(Enchantment.DIG_SPEED) > 5 || this.data.getActionProcessor().isHasConduitsPower() || event2.isCancelled() || BlockUtil.isShulkerBox(event2.getBlock().getType()) || BlockUtil.isDeepSlate(event2.getBlock().getType()) || BlockUtil.isAir(event2.getBlock().getType()) || BlockUtil.isPiston(event2.getBlock().getType()) || block.getType().toString().contains("FIRE") || block.getType().toString().contains("DOOR") || BlockUtil.isTrapdoor(block.getType()) || block.getType().toString().contains("CONCRETE") || block.getType().toString().contains("MUSHROOM") || block.getType().toString().contains("NOTEBLOCK") || block.getType().toString().contains("TRAP") || block.getType().toString().contains("INFESTED") || BlockUtil.isDoor(block.getType()) || block.getType().toString().equals("FIRE")) {
                return;
            }
            final double difference = (double) (System.currentTimeMillis() - this.time);
            if (difference < Config.FASTBREAK_A_MIN_DIFFERENCE) {
                if (this.increaseBuffer() > this.MAX_BUFFER) {
                    this.fail("block=" + event2.getBlock().getType() + " diff=" + difference + " tool=" + this.data.getPlayer().getItemInHand().getType());
                }
            } else {
                this.decayBuffer();
            }
        }
    }
}
