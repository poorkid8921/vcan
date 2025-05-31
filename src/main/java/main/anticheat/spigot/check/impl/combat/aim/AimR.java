package main.anticheat.spigot.check.impl.combat.aim;

import main.anticheat.spigot.check.AbstractCheck;
import main.anticheat.spigot.check.api.CheckInfo;
import main.anticheat.spigot.data.PlayerData;
import main.anticheat.spigot.packet.Packet;

@CheckInfo(name = "Aim", type = 'R', complexType = "Divisor X", description = "Subtle aim assist modifications.")
public class AimR extends AbstractCheck {
    public AimR(final PlayerData data) {
        super(data);
    }

    @Override
    public void handle(final Packet packet) {
        if (!packet.isRotation() || this.hitTicks() < 3) {
        }
    }
}
