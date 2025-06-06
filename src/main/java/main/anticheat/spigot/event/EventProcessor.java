package main.anticheat.spigot.event;

import io.github.retrooper.packetevents.packetwrappers.NMSPacket;
import main.anticheat.spigot.Anticheat;
import main.anticheat.spigot.config.Config;
import main.anticheat.spigot.data.PlayerData;
import main.anticheat.spigot.data.processor.PositionProcessor;
import main.anticheat.spigot.packet.Packet;
import main.anticheat.spigot.util.*;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.data.Waterlogged;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.*;
import org.bukkit.event.player.*;
import org.bukkit.util.NumberConversions;

import java.util.List;

public class EventProcessor implements Listener {
    @EventHandler(priority = EventPriority.MONITOR)
    public void onSpawn(final EntitySpawnEvent event) {
        if (event.getEntity() instanceof Firework) {
            final Firework firework = (Firework) event.getEntity();
            if (firework.getShooter() instanceof Player) {
                final Player player = (Player) firework.getShooter();
                final PlayerData data = Anticheat.INSTANCE.getPlayerDataManager().getPlayerData(player);
                if (data == null) {
                    return;
                }
                data.getPositionProcessor().setSinceFireworkTicks(0);
            }
        }
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onPlace(final BlockPlaceEvent event) {
        final Player player = event.getPlayer();
        final PlayerData data = Anticheat.INSTANCE.getPlayerDataManager().getPlayerData(player);
        if (data == null) {
            return;
        }
        if (BlockUtil.isSlime(event.getBlock())) {
            data.getActionProcessor().handleSlimePlace();
        }
        if (event.isCancelled()) {
            data.getActionProcessor().handleCancelledPlace();
        }
        if (BlockUtil.isIce(event.getBlock().getType())) {
            data.getActionProcessor().handleIcePlace();
        }
        if (BlockUtil.isClimbable(event.getBlock().getType())) {
            data.getActionProcessor().handleClimbablePlace();
        }
        if (BlockUtil.isWeb(event.getBlock().getType())) {
            data.getActionProcessor().handleWebPlace();
        }
        if (data.getPositionProcessor().isFrozen()) {
            event.setCancelled(true);
        }
        if (BlockUtil.isScaffolding(event.getBlock().getType())) {
            data.getActionProcessor().handleScaffoldingPlace(event);
        }
        data.getActionProcessor().handleBukkitPlace();
        Anticheat.INSTANCE.getReceivingPacketProcessor().handle(data, new Packet(Packet.Direction.RECEIVE, new NMSPacket(event), (byte) 120));
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onBreak(final BlockBreakEvent event) {
        final Player player = event.getPlayer();
        final PlayerData data = Anticheat.INSTANCE.getPlayerDataManager().getPlayerData(player);
        if (data == null) {
            return;
        }
        if (event.isCancelled()) {
            data.getActionProcessor().handleCancelledBreak();
        }
        if (data.getPositionProcessor().isFrozen()) {
            event.setCancelled(true);
        }
        if (event.getBlock().getType().toString().equals("NETHERRACK")) {
            data.getActionProcessor().setSinceNetherrackBreakTicks(0);
        }
        if (Config.ENABLED_CHECKS.get("FastBreakA")) {
            Anticheat.INSTANCE.getReceivingPacketProcessor().handle(data, new Packet(Packet.Direction.RECEIVE, new NMSPacket(event), (byte) 100));
        }
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onItemPickup(final PlayerPickupItemEvent event) {
        final Player player = event.getPlayer();
        final PlayerData data = Anticheat.INSTANCE.getPlayerDataManager().getPlayerData(player);
        if (data == null) {
            return;
        }
        data.getActionProcessor().handleItemPickup();
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onConsume(final PlayerItemConsumeEvent event) {
        final Player player = event.getPlayer();
        final PlayerData data = Anticheat.INSTANCE.getPlayerDataManager().getPlayerData(player);
        if (data == null) {
            return;
        }
        if (event.getItem().toString().contains("CHORUS")) {
            data.getActionProcessor().handleChorusEat();
        }
        Anticheat.INSTANCE.getReceivingPacketProcessor().handle(data, new Packet(Packet.Direction.RECEIVE, new NMSPacket(event), (byte) 97));
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onBedEnter(final PlayerBedEnterEvent event) {
        final Player player = event.getPlayer();
        final PlayerData data = Anticheat.INSTANCE.getPlayerDataManager().getPlayerData(player);
        if (data == null) {
            return;
        }
        data.getActionProcessor().handleBedEnter();
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onBedLeave(final PlayerBedLeaveEvent event) {
        final Player player = event.getPlayer();
        final PlayerData data = Anticheat.INSTANCE.getPlayerDataManager().getPlayerData(player);
        if (data == null) {
            return;
        }
        data.getActionProcessor().handleBedLeave();
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onInteract(final PlayerInteractEvent event) {
        if ((event.getAction() == Action.RIGHT_CLICK_AIR || event.getAction() == Action.RIGHT_CLICK_BLOCK) && event.getItem() != null && BlockUtil.isGlassBottle(event.getItem().getType())) {
            final Player player = event.getPlayer();
            final PlayerData data = Anticheat.INSTANCE.getPlayerDataManager().getPlayerData(player);
            data.getActionProcessor().setSinceGlassBottleFillTicks(0);
        }
        if (event.getAction() == Action.LEFT_CLICK_BLOCK || event.getAction() == Action.RIGHT_CLICK_BLOCK) {
            final Player player = event.getPlayer();
            final PlayerData data = Anticheat.INSTANCE.getPlayerDataManager().getPlayerData(player);
            if (data == null) {
                return;
            }
            if (event.getAction() == Action.RIGHT_CLICK_BLOCK && event.getItem() != null && event.getItem().getType().toString().contains("BUCKET")) {
                data.getActionProcessor().setSinceStupidBucketEmptyTicks(0);
            }
            if (event.getAction() == Action.RIGHT_CLICK_BLOCK && event.getItem() != null && event.getItem().getType().toString().contains("_BUCKET") && event.getClickedBlock() != null && !BlockUtil.isAir(event.getClickedBlock().getType())) {
                final double blockX = event.getClickedBlock().getX();
                final double blockY = event.getClickedBlock().getY();
                final double blockZ = event.getClickedBlock().getZ();
                final double playerX = player.getLocation().getX();
                final double playerY = player.getLocation().getY();
                final double playerZ = player.getLocation().getZ();
                final double distance = MathUtil.magnitude(Math.abs(blockX - playerX), Math.abs(blockY - playerY), Math.abs(blockZ - playerZ));
                if (distance < 2.25) {
                    data.getActionProcessor().setSinceBucketEmptyTicks(0);
                }
            }
            if (event.isBlockInHand() && event.isCancelled()) {
                data.getActionProcessor().setSinceCancelledPlaceTicks(0);
            }
            data.getActionProcessor().handleBukkitDig();
            Anticheat.INSTANCE.getReceivingPacketProcessor().handle(data, new Packet(Packet.Direction.RECEIVE, new NMSPacket(event), (byte) 96));
        }
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onDeath(final PlayerDeathEvent event) {
        final Player player = event.getEntity();
        final PlayerData data = Anticheat.INSTANCE.getPlayerDataManager().getPlayerData(player);
        if (data == null) {
            return;
        }
        data.getActionProcessor().handleDeath();
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onDamage(final EntityDamageEvent event) {
        if (!(event.getEntity() instanceof Player) || event.isCancelled()) {
            return;
        }
        final Player player = (Player) event.getEntity();
        final PlayerData data = Anticheat.INSTANCE.getPlayerDataManager().getPlayerData(player);
        if (data == null) {
            return;
        }
        if (data.getPositionProcessor().isFrozen()) {
            event.setCancelled(true);
        }
        data.getActionProcessor().handleDamage(event.getCause());
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onInteract(final PlayerInteractEntityEvent event) {
        final Player player = event.getPlayer();
        final PlayerData data = Anticheat.INSTANCE.getPlayerDataManager().getPlayerData(player);
        if (data == null) {
            return;
        }
        switch (event.getRightClicked().getType()) {
            case VILLAGER:
            case COW:
            case WOLF: {
                data.getClickProcessor().setLastInteractEntity(System.currentTimeMillis());
                break;
            }
        }
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onLaunch(final ProjectileLaunchEvent event) {
        if (event.getEntity() instanceof Arrow) {
            final Arrow arrow = (Arrow) event.getEntity();
            if (arrow.getShooter() != null && arrow.getShooter() instanceof Player) {
                final Player player = (Player) arrow.getShooter();
                final PlayerData data = Anticheat.INSTANCE.getPlayerDataManager().getPlayerData(player);
                if (data == null) {
                    return;
                }
                Anticheat.INSTANCE.getReceivingPacketProcessor().handle(data, new Packet(Packet.Direction.RECEIVE, new NMSPacket(event), (byte) 98));
            }
        } else if (event.getEntity() instanceof Snowball) {
            final Snowball snowball = (Snowball) event.getEntity();
            if (snowball.getShooter() != null && snowball.getShooter() instanceof Player) {
                final Player player = (Player) snowball.getShooter();
                final PlayerData data = Anticheat.INSTANCE.getPlayerDataManager().getPlayerData(player);
                if (data == null) {
                    return;
                }
                data.getActionProcessor().handleProjectileThrow();
            }
        } else if (event.getEntity() instanceof Egg) {
            final Egg egg = (Egg) event.getEntity();
            if (egg.getShooter() != null && egg.getShooter() instanceof Player) {
                final Player player = (Player) egg.getShooter();
                final PlayerData data = Anticheat.INSTANCE.getPlayerDataManager().getPlayerData(player);
                if (data == null) {
                    return;
                }
                data.getActionProcessor().handleProjectileThrow();
            }
        } else if (event.getEntity() instanceof ThrownExpBottle) {
            final ThrownExpBottle expBottle = (ThrownExpBottle) event.getEntity();
            if (expBottle.getShooter() != null && expBottle.getShooter() instanceof Player) {
                final Player player = (Player) expBottle.getShooter();
                final PlayerData data = Anticheat.INSTANCE.getPlayerDataManager().getPlayerData(player);
                if (data == null) {
                    return;
                }
                data.getActionProcessor().handleProjectileThrow();
            }
        }
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onTeleport(final PlayerTeleportEvent event) {
        final Player player = event.getPlayer();
        final PlayerData data = Anticheat.INSTANCE.getPlayerDataManager().getPlayerData(player);
        if (data == null) {
            return;
        }
        if (event.getCause() == PlayerTeleportEvent.TeleportCause.ENDER_PEARL) {
            data.getActionProcessor().handleEnderPearl();
            if (data.getPositionProcessor().isFrozen()) {
                event.setCancelled(true);
            }
        }
        if (ServerUtil.isHigherThan1_9() && event.getCause() == PlayerTeleportEvent.TeleportCause.CHORUS_FRUIT) {
            data.getActionProcessor().handleChorusTeleport();
        }
        if (event.getCause() != PlayerTeleportEvent.TeleportCause.UNKNOWN && event.getCause() != PlayerTeleportEvent.TeleportCause.ENDER_PEARL) {
            data.getActionProcessor().handleTeleport();
        }
        if (ServerUtil.isHigherThan1_13()) {
            final int x = NumberConversions.floor(event.getTo().getX());
            final int y = NumberConversions.floor(event.getTo().getY());
            final int z = NumberConversions.floor(event.getTo().getZ());
            if (event.getTo().getWorld() != null && event.getTo().getWorld().isChunkLoaded(x >> 4, z >> 4)) {
                final List<Material> nearbyBlocks = BlockUtil.getNearbyBlocks(event.getTo().getWorld(), x, y, z, 1);
                data.getPositionProcessor().setNearbyBlocks(nearbyBlocks);
            }
        }
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onWorldChange(final PlayerChangedWorldEvent event) {
        final Player player = event.getPlayer();
        final PlayerData data = Anticheat.INSTANCE.getPlayerDataManager().getPlayerData(player);
        if (data == null) {
            return;
        }
        data.getActionProcessor().handleWorldChange();
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onFish(final PlayerFishEvent event) {
        if (event.getCaught() instanceof Player) {
            final Player player = (Player) event.getCaught();
            final PlayerData data = Anticheat.INSTANCE.getPlayerDataManager().getPlayerData(player);
            if (data == null) {
                return;
            }
            data.getPositionProcessor().handleFishingRod();
        } else if (event.getCaught() instanceof Boat) {
            final Boat boat = (Boat) event.getCaught();
            Anticheat.INSTANCE.getFishingRodPulledBoats().put(boat.getEntityId(), Anticheat.INSTANCE.getTickManager().getTicks());
        }
        final Player player = event.getPlayer();
        final PlayerData data = Anticheat.INSTANCE.getPlayerDataManager().getPlayerData(player);
        if (data == null) {
            return;
        }
        data.getActionProcessor().handleFishEvent();
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onEntityDamageByEntity(final EntityDamageByEntityEvent event) {
        if (event.getEntity() instanceof Player) {
            final Player victim = (Player) event.getEntity();
            if (victim.hasMetadata("NPC") || victim.hasMetadata("npc")) {
                return;
            }
            final PlayerData victimData = Anticheat.INSTANCE.getPlayerDataManager().getPlayerData(victim);
            if (victimData == null) {
                return;
            }
            final EntityType entityType = event.getDamager().getType();
            victimData.getActionProcessor().handleBukkitAttackDamage(entityType);
        }
        if (event.getEntity() instanceof Player && event.getDamager() instanceof Arrow) {
            final Arrow arrow = (Arrow) event.getDamager();
            final Player victim2 = (Player) event.getEntity();
            if (arrow.getShooter() != null && arrow.getShooter() instanceof Player) {
                final Player shooter = (Player) arrow.getShooter();
                if (victim2.getEntityId() == shooter.getEntityId() && arrow.getKnockbackStrength() >= 2) {
                    final PlayerData data = Anticheat.INSTANCE.getPlayerDataManager().getPlayerData(victim2);
                    if (data == null) {
                        return;
                    }
                    data.getActionProcessor().setSinceBowBoostTicks(0);
                }
            }
        }
        if (!(event.getEntity() instanceof Player) || !(event.getDamager() instanceof Player) || event.isCancelled()) {
            return;
        }
        final Player damager = (Player) event.getDamager();
        final Player victim2 = (Player) event.getEntity();
        final PlayerData damagerData = Anticheat.INSTANCE.getPlayerDataManager().getPlayerData(damager);
        final PlayerData victimData2 = Anticheat.INSTANCE.getPlayerDataManager().getPlayerData(victim2);
        if (damagerData == null || victimData2 == null) {
            return;
        }
        if (victimData2.getPositionProcessor().isFrozen() || damagerData.getPositionProcessor().isFrozen()) {
            event.setCancelled(true);
        }
        if (event.getCause() == EntityDamageEvent.DamageCause.ENTITY_ATTACK) {
            damagerData.getCombatProcessor().handleBukkitAttack();
            victimData2.getCombatProcessor().handleBukkitAttackDamage();
        }
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onMove(final PlayerMoveEvent event) {
        if (event.getFrom().getX() == event.getTo().getX() && event.getFrom().getY() == event.getTo().getY() && event.getFrom().getZ() == event.getTo().getZ()) {
            return;
        }
        final Player player = event.getPlayer();
        final PlayerData data = Anticheat.INSTANCE.getPlayerDataManager().getPlayerData(player);
        if (data == null) {
            return;
        }
        if (event.isCancelled()) {
            data.getActionProcessor().setSinceCancelledMoveTicks(0);
        }
        final PositionProcessor positionProcessor = data.getPositionProcessor();
        if (positionProcessor.isFrozen()) {
            player.sendMessage(ColorUtil.translate(Config.FROZEN));
            event.setTo(new Location(player.getWorld(), data.getPositionProcessor().getSetbackX(), data.getPositionProcessor().getSetbackY(), data.getPositionProcessor().getSetbackZ(), data.getRotationProcessor().getYaw(), data.getRotationProcessor().getPitch()));
        }
        if (ServerUtil.isHigherThan1_13()) {
            if (PlayerUtil.isHigherThan1_13(player) && data.getActionProcessor().isUpdateSwim()) {
                data.getActionProcessor().setUpdateSwim(false);
                player.setSwimming(true);
            }
            final int x = NumberConversions.floor(event.getTo().getX());
            final int y = NumberConversions.floor(event.getTo().getY());
            final int z = NumberConversions.floor(event.getTo().getZ());
            final List<Material> nearbyBlocks = BlockUtil.getNearbyBlocks(event.getTo().getWorld(), x, y, z, 1);
            data.getPositionProcessor().setNearbyBlocks(nearbyBlocks);
            final List<Material> blocksUnder = BlockUtil.getNearbyBlocksBelow(event.getTo().getWorld(), x, y, z);
            data.getPositionProcessor().setBlocksUnder(blocksUnder);
            final Block block = event.getTo().getBlock();
            final Block below = event.getTo().clone().subtract(0.0, 1.0, 0.0).getBlock();
            final Block below2 = event.getTo().clone().subtract(0.0, 2.0, 0.0).getBlock();
            final Block below3 = event.getTo().clone().subtract(0.0, 3.0, 0.0).getBlock();
            final Block above = event.getTo().clone().add(0.0, 1.0, 0.0).getBlock();
            final Block above2 = event.getTo().clone().add(0.0, 2.0, 0.0).getBlock();
            if (below.getBlockData() instanceof Waterlogged) {
                final Waterlogged waterlogged = (Waterlogged) below.getBlockData();
                if (waterlogged.isWaterlogged()) {
                    data.getPositionProcessor().handleWaterlogged();
                }
            }
            if (block.getBlockData() instanceof Waterlogged) {
                final Waterlogged waterlogged = (Waterlogged) block.getBlockData();
                if (waterlogged.isWaterlogged()) {
                    data.getPositionProcessor().handleWaterlogged();
                }
            }
            if (above.getBlockData() instanceof Waterlogged) {
                final Waterlogged waterlogged = (Waterlogged) above.getBlockData();
                if (waterlogged.isWaterlogged()) {
                    data.getPositionProcessor().handleWaterlogged();
                }
            }
            if (above2.getBlockData() instanceof Waterlogged) {
                final Waterlogged waterlogged = (Waterlogged) above2.getBlockData();
                if (waterlogged.isWaterlogged()) {
                    data.getPositionProcessor().handleWaterlogged();
                }
            }
            data.getPositionProcessor().setBlockBelow(below.getType());
            data.getPositionProcessor().setBlockBelow2(below2.getType());
            data.getPositionProcessor().setBlockBelow3(below3.getType());
            if (player.getVehicle() != null) {
                data.getPositionProcessor().setVehicleBlocks(nearbyBlocks);
            }
        }
        final List<Entity> nearbyEntities = player.getNearbyEntities(1.5, 1.5, 1.5);
        positionProcessor.setNearbyEntities(nearbyEntities);
    }

    @EventHandler
    public void onKick(final PlayerKickEvent event) {
        final Player player = event.getPlayer();
        final PlayerData data = Anticheat.INSTANCE.getPlayerDataManager().getPlayerData(player);
        if (data == null) {
            return;
        }
        if (data.getPositionProcessor().isFrozen() && event.getReason().toLowerCase().contains("not enabled")) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onJoin(final PlayerJoinEvent event) {
        final Player player = event.getPlayer();
        if (Config.JOIN_MESSAGE_ENABLED) {
            final PlayerData data = Anticheat.INSTANCE.getPlayerDataManager().getPlayerData(player);
            if (data == null) {
                return;
            }
            Config.JOIN_MESSAGE.forEach(message -> player.sendMessage(ColorUtil.translate(message.replaceAll("%client-brand%", data.getClientBrand()).replaceAll("%player%", player.getName()))));
        }
    }

    @EventHandler(ignoreCancelled = true)
    public void onResurrect(final EntityResurrectEvent event) {
        if (event.getEntity() instanceof Player) {
            final Player player = (Player) event.getEntity();
            final PlayerData data = Anticheat.INSTANCE.getPlayerDataManager().getPlayerData(player);
            if (data == null) {
                return;
            }
            data.getActionProcessor().handleResurrect();
        }
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onRiptide(final PlayerRiptideEvent event) {
        final Player player = event.getPlayer();
        final PlayerData data = Anticheat.INSTANCE.getPlayerDataManager().getPlayerData(player);
        if (data == null) {
            return;
        }
        data.getPositionProcessor().setSinceRiptideTicks(0);
    }
}
