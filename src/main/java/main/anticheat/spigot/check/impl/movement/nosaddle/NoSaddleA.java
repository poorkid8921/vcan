package main.anticheat.spigot.check.impl.movement.nosaddle;

import main.anticheat.spigot.Anticheat;
import main.anticheat.spigot.check.AbstractCheck;
import main.anticheat.spigot.check.api.CheckInfo;
import main.anticheat.spigot.config.Config;
import main.anticheat.spigot.data.PlayerData;
import main.anticheat.spigot.packet.Packet;
import main.anticheat.spigot.util.ServerUtil;
import org.bukkit.Bukkit;
import org.bukkit.attribute.Attribute;
import org.bukkit.entity.Horse;
import org.bukkit.inventory.ItemStack;

@CheckInfo(name = "No Saddle", type = 'A', complexType = "Spoof", description = "Controlling an entity without a saddle.")
public class NoSaddleA extends AbstractCheck {
    public NoSaddleA(final PlayerData data) {
        super(data);
    }

    @Override
    public void handle(final Packet packet) {
        if (packet.isVehicleMove()) {
            if (ServerUtil.isLowerThan1_9()) {
                return;
            }
            final boolean vehicleHorse = this.data.getPlayer().getVehicle() instanceof Horse;
            final double deltaXZ = this.data.getPositionProcessor().getVehicleDeltaXZ();
            if (vehicleHorse) {
                final Horse horse = (Horse) this.data.getPlayer().getVehicle();
                if (horse == null || horse.isLeashed()) {
                    return;
                }
                final double maxSpeed = horse.getAttribute(Attribute.GENERIC_MOVEMENT_SPEED).getBaseValue() * 2.25 + 0.1;
                final ItemStack saddle = horse.getInventory().getSaddle();
                final double difference = maxSpeed - deltaXZ;
                final boolean invalid = saddle == null && difference < 0.3 && deltaXZ > 0.1;
                final int vehicleTicks = this.data.getPositionProcessor().getVehicleTicks();
                if (invalid && vehicleTicks > 5) {
                    if (this.increaseBuffer() > this.MAX_BUFFER) {
                        this.fail("diff=" + difference + " deltaXZ=" + deltaXZ);
                        if (Config.NO_SADDLE_A_KICKOUT) {
                            Bukkit.getScheduler().runTask(Anticheat.INSTANCE.getPlugin(), () -> this.data.getPlayer().leaveVehicle());
                        }
                    }
                } else {
                    this.decayBuffer();
                }
            }
        }
    }
}
