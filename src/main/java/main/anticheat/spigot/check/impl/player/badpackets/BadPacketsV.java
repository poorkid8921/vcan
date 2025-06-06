package main.anticheat.spigot.check.impl.player.badpackets;

import main.anticheat.spigot.Anticheat;
import main.anticheat.spigot.check.AbstractCheck;
import main.anticheat.spigot.check.api.CheckInfo;
import main.anticheat.spigot.config.Config;
import main.anticheat.spigot.data.PlayerData;
import main.anticheat.spigot.exempt.type.ExemptType;
import main.anticheat.spigot.packet.Packet;
import main.anticheat.spigot.util.ServerUtil;
import org.bukkit.Bukkit;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Player;

@CheckInfo(name = "Bad Packets", type = 'V', complexType = "Null Vehicle", description = "Spoofed SteerVehicle packets.")
public class BadPacketsV extends AbstractCheck {
    public BadPacketsV(final PlayerData data) {
        super(data);
    }

    @Override
    public void handle(final Packet packet) {
        if (packet.isSteerVehicle()) {
            if (this.data.getConnectionProcessor().getTransactionPing() > 1500L) {
                return;
            }
            final boolean empty = this.data.getPositionProcessor().getNearbyEntities() != null && this.data.getPositionProcessor().getNearbyEntities().isEmpty();
            final boolean invalid = this.data.getPlayer().getVehicle() == null;
            final boolean exempt = this.isExempt(ExemptType.STAIRS, ExemptType.SLAB);
            final boolean armorStand = ServerUtil.isHigherThan1_8() && this.data.getPlayer().getVehicle() != null && this.data.getPlayer().getVehicle() instanceof ArmorStand;
            final boolean sitting = this.data.getActionProcessor().isSitting();
            final boolean player = this.data.getPlayer().getVehicle() != null && this.data.getPlayer().getVehicle() instanceof Player;
            if (invalid && empty && !armorStand && !exempt && !player && !sitting) {
                if (this.increaseBuffer() > this.MAX_BUFFER) {
                    this.fail("vehicle=" + this.data.getPlayer().getVehicle() + " entities=" + this.data.getPositionProcessor().getNearbyEntities());
                    if (Config.BAD_PACKETS_V_KICKOUT) {
                        Bukkit.getScheduler().runTask(Anticheat.INSTANCE.getPlugin(), () -> this.data.getPlayer().leaveVehicle());
                    }
                }
            } else {
                this.decayBuffer();
            }
        } else if (packet.isFlying()) {
            this.decreaseBufferBy(0.045);
        }
    }
}
