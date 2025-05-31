package main.anticheat.spigot.data.processor;

import io.github.retrooper.packetevents.packetwrappers.play.in.useentity.WrappedPacketInUseEntity;
import io.github.retrooper.packetevents.packetwrappers.play.out.entity.WrappedPacketOutEntity;
import lombok.Getter;
import lombok.Setter;
import main.anticheat.spigot.Anticheat;
import main.anticheat.spigot.data.PlayerData;
import main.anticheat.spigot.util.MathUtil;
import main.anticheat.spigot.util.type.EvictingList;
import main.anticheat.spigot.util.type.Pair;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Entity;
import org.bukkit.entity.ItemFrame;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

@Getter
@Setter
public class CombatProcessor {
    private final PlayerData data;
    private final EvictingList<Pair<Location, Integer>> targetLocations;
    private int hitTicks;
    private int lastTargetAttackTick;
    private int lastPlayerAttackDamage;
    private int lastItemFrame;
    private Entity target;
    private Player trackedPlayer;
    private Player lastTrackedPlayer;
    private double distance;
    private double complexDistance;

    public CombatProcessor(final PlayerData data) {
        this.hitTicks = 100;
        this.targetLocations = new EvictingList<Pair<Location, Integer>>(20);
        this.data = data;
    }

    public void handleUseEntity(final WrappedPacketInUseEntity wrapper) {
        if (wrapper.getEntity() instanceof ItemFrame) {
            this.lastItemFrame = Anticheat.INSTANCE.getTickManager().getTicks();
        }
        if (wrapper.getAction() != WrappedPacketInUseEntity.EntityUseAction.ATTACK) {
            return;
        }
        this.target = wrapper.getEntity();
        if (this.target != null) {
            this.distance = this.data.getPlayer().getLocation().toVector().setY(0).distance(this.target.getLocation().toVector().setY(0)) - 0.42;
        }
        if (wrapper.getEntity() instanceof Player) {
            this.lastTrackedPlayer = (Player) ((this.trackedPlayer == null) ? wrapper.getEntity() : this.trackedPlayer);
            this.trackedPlayer = (Player) wrapper.getEntity();
            if (this.trackedPlayer != this.lastTrackedPlayer) {
                this.targetLocations.clear();
            }
        }
        this.hitTicks = 0;
    }

    public void handleRelEntityMove(final WrappedPacketOutEntity wrapper) {
        if (this.trackedPlayer != null && this.trackedPlayer.getEntityId() == wrapper.getEntityId()) {
            final PlayerData targetData = Anticheat.INSTANCE.getPlayerDataManager().getPlayerData(this.trackedPlayer);
            if (targetData == null) {
                return;
            }
            final int ticks = Anticheat.INSTANCE.getTickManager().getTicks();
            final World world = targetData.getPlayer().getWorld();
            final double x = targetData.getPositionProcessor().getX() - wrapper.getDeltaX();
            final double y = targetData.getPositionProcessor().getY() - wrapper.getDeltaY();
            final double z = targetData.getPositionProcessor().getZ() - wrapper.getDeltaZ();
            final Location targetLocation = new Location(world, x, y, z);
            this.targetLocations.add(new Pair<Location, Integer>(targetLocation, ticks));
        }
    }

    public void handleServerPosition() {
        this.targetLocations.clear();
    }

    public void handleBukkitAttack() {
        this.lastTargetAttackTick = Anticheat.INSTANCE.getTickManager().getTicks();
    }

    public void handleBukkitAttackDamage() {
        this.lastPlayerAttackDamage = Anticheat.INSTANCE.getTickManager().getTicks();
    }

    public int getTicksSinceAttack() {
        return Anticheat.INSTANCE.getTickManager().getTicks() - this.lastTargetAttackTick;
    }

    public int getTicksSinceAttackDamage() {
        return Anticheat.INSTANCE.getTickManager().getTicks() - this.lastPlayerAttackDamage;
    }

    public void handleFlying() {
        ++this.hitTicks;
        if (!this.targetLocations.isEmpty() && this.trackedPlayer != null) {
            final int ticks = Anticheat.INSTANCE.getTickManager().getTicks();
            final int pingTicks = MathUtil.getPingInTicks(this.data);
            final double x = this.data.getPositionProcessor().getX();
            final double z = this.data.getPositionProcessor().getZ();
            final Vector origin = new Vector(x, 0.0, z);
            this.complexDistance = this.data.getCombatProcessor().getTargetLocations().stream().filter(pair -> Math.abs(ticks - pair.getY() - pingTicks) < 3).mapToDouble(pair -> {
                final Vector victimVector = pair.getX().toVector().setY(0);
                return origin.distance(victimVector) - 0.52;
            }).min().orElse(-1.0);
        }
    }
}
