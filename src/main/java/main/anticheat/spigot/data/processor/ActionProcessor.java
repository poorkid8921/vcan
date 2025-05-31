package main.anticheat.spigot.data.processor;

import io.github.retrooper.packetevents.packetwrappers.play.in.blockdig.WrappedPacketInBlockDig;
import io.github.retrooper.packetevents.packetwrappers.play.in.clientcommand.WrappedPacketInClientCommand;
import io.github.retrooper.packetevents.packetwrappers.play.in.entityaction.WrappedPacketInEntityAction;
import io.github.retrooper.packetevents.packetwrappers.play.in.flying.WrappedPacketInFlying;
import io.github.retrooper.packetevents.packetwrappers.play.in.pong.WrappedPacketInPong;
import io.github.retrooper.packetevents.packetwrappers.play.in.transaction.WrappedPacketInTransaction;
import io.github.retrooper.packetevents.packetwrappers.play.out.abilities.WrappedPacketOutAbilities;
import io.github.retrooper.packetevents.packetwrappers.play.out.entityeffect.WrappedPacketOutEntityEffect;
import io.github.retrooper.packetevents.packetwrappers.play.out.entitymetadata.WrappedPacketOutEntityMetadata;
import io.github.retrooper.packetevents.packetwrappers.play.out.entitymetadata.WrappedWatchableObject;
import io.github.retrooper.packetevents.packetwrappers.play.out.explosion.WrappedPacketOutExplosion;
import io.github.retrooper.packetevents.packetwrappers.play.out.gamestatechange.WrappedPacketOutGameStateChange;
import io.github.retrooper.packetevents.packetwrappers.play.out.position.WrappedPacketOutPosition;
import io.github.retrooper.packetevents.packetwrappers.play.out.removeentityeffect.WrappedPacketOutRemoveEntityEffect;
import io.github.retrooper.packetevents.packetwrappers.play.out.updateattributes.WrappedPacketOutUpdateAttributes;
import io.github.retrooper.packetevents.utils.attributesnapshot.AttributeSnapshotWrapper;
import io.github.retrooper.packetevents.utils.vector.Vector3d;
import io.github.retrooper.packetevents.utils.vector.Vector3f;
import lombok.Getter;
import lombok.Setter;
import main.anticheat.spigot.Anticheat;
import main.anticheat.spigot.data.PlayerData;
import main.anticheat.spigot.util.MathUtil;
import main.anticheat.spigot.util.PlayerUtil;
import main.anticheat.spigot.util.ServerUtil;
import main.anticheat.spigot.util.type.EvictingList;
import org.bukkit.GameMode;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.EntityType;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.util.Vector;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Getter
@Setter
public class ActionProcessor {
    private static final String GENERIC_MOVEMENT_SPEED;

    static {
        GENERIC_MOVEMENT_SPEED = (ServerUtil.isHigherThan1_16() ? "attribute.name.generic.movement_speed" : "generic.movementSpeed");
    }

    final EvictingList<Vector> teleports;
    private final PlayerData data;
    private final Map<Short, WrappedPacketOutEntityEffect> queuedEntityEffects;
    private final Map<Short, WrappedPacketOutRemoveEntityEffect> queuedRemoveEntityEffects;
    private final Map<Short, WrappedPacketOutGameStateChange> queuedGameStateChanges;
    private final Map<Short, WrappedPacketOutPosition> queuedTeleports;
    private boolean sprinting;
    private boolean sneaking;
    private boolean sendingAction;
    private boolean placing;
    private boolean digging;
    private boolean blocking;
    private boolean inventory;
    private boolean sendingDig;
    private boolean teleporting;
    private boolean hasSpeed;
    private boolean hasJumpBoost;
    private boolean hasLevitation;
    private boolean hasSlowFalling;
    private boolean hasDolphinsGrace;
    private boolean sitting;
    private boolean hasConduitsPower;
    private boolean crawling;
    private boolean hasSlowness;
    private boolean wearingDepthStrider;
    private boolean wearingElytra;
    private boolean bukkitGliding;
    private boolean packetGliding;
    private boolean packetSwimming;
    private boolean updateSwim;
    private double genericMovementSpeed;
    private boolean berserking;
    private int lastBukkitDiggingTick;
    private int lastDropItemTick;
    private int lastDiggingTick;
    private int sinceSprintingTicks;
    private int positionTicksExisted;
    private int lastWindowClick;
    private int amount;
    private int lastAmount;
    private int slot;
    private int lastSlot;
    private int sinceTeleportTicks;
    private int sinceExplosionTicks;
    private int lastBedEnter;
    private int lastBedLeave;
    private int lastItemPickup;
    private int lastCancelledBreak;
    private int lastChorusFruitTeleport;
    private int lastDamage;
    private int sinceDragonDamageTicks;
    private int sprintingTicks;
    private int lastRespawn;
    private int lastLowHealth;
    private int lastProjectileThrow;
    private int lastFishEvent;
    private int sinceFireDamageTicks;
    private int sinceAttackDamageTicks;
    private int sinceFallDamageTicks;
    private int sinceMagicDamageTicks;
    private int sincePoisonDamageTicks;
    private int sinceContactDamageTicks;
    private int sinceProjectileDamageTicks;
    private int lastLavaDamage;
    private int sinceLavaDamageTicks;
    private int sinceExplosionDamageTicks;
    private int sinceBucketEmptyTicks;
    private int sinceBlockPlaceTicks;
    private int sinceClimbablePlaceTicks;
    private int sinceSlimePlaceTicks;
    private int sinceCancelledPlaceTicks;
    private int sinceWorldChangeTicks;
    private int sinceRessurectTicks;
    private int sinceBukkitTeleportTicks;
    private int sinceDeathTicks;
    private int sinceNonFallDamageTicks;
    private int sinceIronGolemDamageTicks;
    private int lastScaffoldPlaceX;
    private int lastScaffoldPlaceY;
    private int lastScaffoldPlaceZ;
    private int sinceBerserkTicks;
    private int sinceCrackshotDamageTicks;
    private int sinceGlassBottleFillTicks;
    private int sinceFireballDamageTicks;
    private int speedAmplifier;
    private int jumpBoostAmplifier;
    private int levitationAmplifier;
    private int slowFallingAmplifier;
    private int dolphinsGraceAmplifier;
    private int sinceChorusFruitTeleportTicks;
    private int sinceWebPlaceTicks;
    private int sinceEnderPearlTicks;
    private int sinceBukkitVelocityTicks;
    private int sinceDamageTicks;
    private int sinceStartGlidingTicks;
    private int sinceIcePlaceTicks;
    private int sinceBlockBreakTicks;
    private int ticksExisted;
    private int sinceHoglinDamageTicks;
    private int sinceAbilitiesTicks;
    private int sincePushTicks;
    private int sinceMythicMobTicks;
    private int sinceCancelledMoveTicks;
    private int sinceRavagerDamageTicks;
    private int sinceWitherDamageTicks;
    private int sinceCrystalDamageTicks;
    private int sinceSuffociationDamageTicks;
    private int sinceStupidBucketEmptyTicks;
    private int sinceNetherrackBreakTicks;
    private int sinceBowBoostTicks;
    private double lastAbortX;
    private double lastAbortZ;
    private double distanceFromLastAbort;
    private double lastBreakX;
    private double lastBreakY;
    private double lastBreakZ;
    private double distanceFromLastBreak;
    private double health;
    private double distanceFromLastScaffoldPlace;
    private double explosionX;
    private double explosionY;
    private double explosionZ;
    private double lastTeleportX;
    private double lastTeleportY;
    private double lastTeleportZ;
    private double distanceFromLastTeleport;
    private double lastGlidingDeltaXZ;
    private long lastStartDestroy;
    private long lastStopDestroy;
    private long lastAbort;
    private GameMode gameMode;
    private short positionTransactionId;
    private short entityEffectTransactionId;
    private short removeEntityEffectTransactionId;
    private short gameStateChangeTransactionId;
    private short positionPingId;
    private short entityEffectPingId;
    private short removeEntityEffectPingId;
    private short gameStateChangePingId;
    private ItemStack helmet;
    private ItemStack chestplate;
    private ItemStack leggings;
    private ItemStack boots;
    private ItemStack itemInMainHand;
    private ItemStack itemInOffHand;

    public ActionProcessor(final PlayerData data) {
        this.genericMovementSpeed = 0.0;
        this.positionTicksExisted = 0;
        this.sinceTeleportTicks = 100;
        this.sinceExplosionTicks = 1000;
        this.sinceDragonDamageTicks = 100;
        this.sinceFireDamageTicks = 100;
        this.sinceAttackDamageTicks = 100;
        this.sinceFallDamageTicks = 100;
        this.sinceExplosionDamageTicks = 100;
        this.sinceBucketEmptyTicks = 100;
        this.sinceBlockPlaceTicks = 100;
        this.sinceClimbablePlaceTicks = 100;
        this.sinceSlimePlaceTicks = 150;
        this.sinceCancelledPlaceTicks = 100;
        this.sinceWorldChangeTicks = 100;
        this.sinceBukkitTeleportTicks = 100;
        this.sinceDeathTicks = 100;
        this.sinceNonFallDamageTicks = 100;
        this.sinceGlassBottleFillTicks = 100;
        this.sinceFireballDamageTicks = 100;
        this.speedAmplifier = 0;
        this.jumpBoostAmplifier = 0;
        this.levitationAmplifier = 0;
        this.slowFallingAmplifier = 0;
        this.dolphinsGraceAmplifier = 0;
        this.sinceChorusFruitTeleportTicks = 100;
        this.sinceWebPlaceTicks = 100;
        this.sinceEnderPearlTicks = 100;
        this.sinceBukkitVelocityTicks = 100;
        this.sinceDamageTicks = 100;
        this.sinceStartGlidingTicks = 100;
        this.sinceIcePlaceTicks = 100;
        this.sinceBlockBreakTicks = 100;
        this.ticksExisted = 0;
        this.sinceAbilitiesTicks = 0;
        this.sincePushTicks = 100;
        this.sinceMythicMobTicks = 500;
        this.sinceCancelledMoveTicks = 1000;
        this.sinceRavagerDamageTicks = 1000;
        this.sinceWitherDamageTicks = 100;
        this.sinceCrystalDamageTicks = 100;
        this.sinceSuffociationDamageTicks = 100;
        this.sinceStupidBucketEmptyTicks = 100;
        this.sinceNetherrackBreakTicks = 100;
        this.sinceBowBoostTicks = 100;
        this.gameMode = GameMode.SURVIVAL;
        this.positionTransactionId = -29768;
        this.entityEffectTransactionId = -28768;
        this.removeEntityEffectTransactionId = -27768;
        this.gameStateChangeTransactionId = -26768;
        this.positionPingId = -29768;
        this.entityEffectPingId = -28768;
        this.removeEntityEffectPingId = -27768;
        this.gameStateChangePingId = -26768;
        this.queuedEntityEffects = new HashMap<Short, WrappedPacketOutEntityEffect>();
        this.queuedRemoveEntityEffects = new HashMap<Short, WrappedPacketOutRemoveEntityEffect>();
        this.queuedGameStateChanges = new HashMap<Short, WrappedPacketOutGameStateChange>();
        this.queuedTeleports = new HashMap<Short, WrappedPacketOutPosition>();
        this.teleports = new EvictingList<Vector>(15);
        this.data = data;
    }

    public void handleUpdateAttributes(final WrappedPacketOutUpdateAttributes wrapper) {
        final List<AttributeSnapshotWrapper> attributes = wrapper.getProperties();
        for (final AttributeSnapshotWrapper attribute : attributes) {
            if (attribute.getKey().equals(ActionProcessor.GENERIC_MOVEMENT_SPEED)) {
                this.genericMovementSpeed = attribute.getValue();
            }
        }
    }

    public void handleEntityAction(final WrappedPacketInEntityAction wrapper) {
        this.sendingAction = true;
        switch (wrapper.getAction()) {
            case START_SPRINTING: {
                this.sprinting = true;
                break;
            }
            case STOP_SPRINTING: {
                this.sprinting = false;
                break;
            }
            case START_SNEAKING: {
                this.sneaking = true;
                break;
            }
            case STOP_SNEAKING: {
                this.sneaking = false;
            }
            case START_FALL_FLYING: {
                this.sinceStartGlidingTicks = 0;
                break;
            }
        }
    }

    public void handleItemPickup() {
        this.lastItemPickup = Anticheat.INSTANCE.getTickManager().getTicks();
    }

    public void handleBukkitDig() {
        this.lastBukkitDiggingTick = Anticheat.INSTANCE.getTickManager().getTicks();
    }

    public void handleBlockDig(final WrappedPacketInBlockDig wrapper) {
        this.sendingDig = true;
        switch (wrapper.getDigType()) {
            case START_DESTROY_BLOCK: {
                this.digging = true;
                this.lastStartDestroy = System.currentTimeMillis();
                break;
            }
            case STOP_DESTROY_BLOCK: {
                this.handleBlockBreak(wrapper.getBlockPosition().getX(), wrapper.getBlockPosition().getY(), wrapper.getBlockPosition().getZ());
                this.lastStopDestroy = System.currentTimeMillis();
                this.digging = false;
                break;
            }
            case ABORT_DESTROY_BLOCK: {
                this.lastAbort = System.currentTimeMillis();
                this.digging = false;
                break;
            }
            case RELEASE_USE_ITEM: {
                this.blocking = true;
                break;
            }
            case DROP_ITEM:
            case DROP_ALL_ITEMS: {
                this.lastDropItemTick = Anticheat.INSTANCE.getTickManager().getTicks();
                break;
            }
        }
        if (wrapper.getDigType() == WrappedPacketInBlockDig.PlayerDigType.ABORT_DESTROY_BLOCK) {
            this.lastAbortX = wrapper.getBlockPosition().getX();
            this.lastAbortZ = wrapper.getBlockPosition().getZ();
        }
    }

    public void handleBukkitAttackDamage(final EntityType entityType) {
        switch (entityType) {
            case ENDER_DRAGON: {
                this.sinceDragonDamageTicks = 0;
                break;
            }
            case IRON_GOLEM: {
                this.sinceIronGolemDamageTicks = 0;
                break;
            }
            case FIREBALL:
            case SMALL_FIREBALL:
            case DRAGON_FIREBALL: {
                this.sinceFireballDamageTicks = 0;
                break;
            }
            case WITHER:
            case WITHER_SKULL: {
                this.sinceWitherDamageTicks = 0;
                break;
            }
            case ENDER_CRYSTAL: {
                this.sinceCrystalDamageTicks = 0;
                break;
            }
        }
        if (ServerUtil.isHigherThan1_16() && entityType == EntityType.HOGLIN) {
            this.sinceHoglinDamageTicks = 0;
        }
        if (ServerUtil.isHigherThan1_14() && entityType == EntityType.RAVAGER) {
            this.sinceRavagerDamageTicks = 0;
        }
    }

    public void handleChorusTeleport() {
        this.lastChorusFruitTeleport = Anticheat.INSTANCE.getTickManager().getTicks();
        this.sinceChorusFruitTeleportTicks = 0;
    }

    public void handleCancelledBreak() {
        this.lastCancelledBreak = Anticheat.INSTANCE.getTickManager().getTicks();
    }

    public void handleClientCommand(final WrappedPacketInClientCommand wrapper) {
        switch (wrapper.getClientCommand()) {
            case OPEN_INVENTORY_ACHIEVEMENT: {
                this.inventory = true;
                break;
            }
            case PERFORM_RESPAWN: {
                this.lastRespawn = Anticheat.INSTANCE.getTickManager().getTicks();
                break;
            }
        }
    }

    public void handleBlockPlace() {
        this.placing = true;
        if (ServerUtil.isHigherThan1_9() && (this.data.getPlayer().getInventory().getItemInMainHand().getType().toString().contains("FIREWORK") || this.data.getPlayer().getInventory().getItemInOffHand().getType().toString().contains("FIREWORK"))) {
            this.data.getPositionProcessor().setSinceFireworkTicks(0);
        }
    }

    public void handleCloseWindow() {
        this.inventory = false;
    }

    public void handleWindowClick() {
        this.lastWindowClick = Anticheat.INSTANCE.getTickManager().getTicks();
    }

    public void handleArmAnimation() {
        if (this.digging) {
            this.lastDiggingTick = Anticheat.INSTANCE.getTickManager().getTicks();
        } else if (this.lastAbortX != 0.0 && this.lastAbortZ != 0.0) {
            final double locationX = this.data.getPositionProcessor().getX();
            final double locationZ = this.data.getPositionProcessor().getZ();
            this.distanceFromLastAbort = MathUtil.hypot(Math.abs(locationX - this.lastAbortX), Math.abs(locationZ - this.lastAbortZ));
            if (this.distanceFromLastAbort > 12.0) {
                final double lastAbortX = 0.0;
                this.distanceFromLastAbort = lastAbortX;
                this.lastAbortZ = lastAbortX;
                this.lastAbortX = lastAbortX;
            }
        }
    }

    public void handleBukkitPlace() {
        this.sinceBlockPlaceTicks = 0;
        this.handleCancelledBlockPlace();
    }

    private void handleCancelledBlockPlace() {
        this.amount = this.data.getPlayer().getItemInHand().getAmount();
        this.slot = this.data.getPlayer().getInventory().getHeldItemSlot();
        if (this.amount == this.lastAmount && this.slot == this.lastSlot) {
            this.sinceCancelledPlaceTicks = 0;
        }
        this.lastSlot = this.slot;
        this.lastAmount = this.amount;
    }

    public void handleBlockBreak(final double x, final double y, final double z) {
        this.sinceBlockBreakTicks = 0;
        this.lastBreakX = x;
        this.lastBreakY = y;
        this.lastBreakZ = z;
    }

    public void handleTeleport() {
        this.sinceBukkitTeleportTicks = 0;
    }

    public void handleServerPosition(final WrappedPacketOutPosition wrapper) {
        ++this.positionTransactionId;
        ++this.positionPingId;
        if (this.positionTransactionId > -28769) {
            this.positionTransactionId = -29768;
        }
        if (this.positionPingId > -28769) {
            this.positionPingId = -29768;
        }
        this.sinceTeleportTicks = 0;
        final Vector teleport = new Vector(wrapper.getPosition().getX(), wrapper.getPosition().getY(), wrapper.getPosition().getZ());
        this.teleports.add(teleport);
        if (ServerUtil.isHigherThan1_17()) {
            this.data.sendPing(this.positionPingId);
        } else {
            this.data.sendTransaction(this.positionTransactionId);
        }
        this.queuedTeleports.put(this.positionTransactionId, wrapper);
    }

    public void handleEntityEffect(final WrappedPacketOutEntityEffect wrapper) {
        ++this.entityEffectTransactionId;
        ++this.entityEffectPingId;
        if (this.entityEffectTransactionId > -27769) {
            this.entityEffectTransactionId = -28768;
        }
        if (this.entityEffectPingId > -27769) {
            this.entityEffectPingId = -28768;
        }
        if (ServerUtil.isHigherThan1_17()) {
            this.data.sendPing(this.entityEffectPingId);
        } else {
            this.data.sendTransaction(this.entityEffectTransactionId);
        }
        this.queuedEntityEffects.put(this.entityEffectTransactionId, wrapper);
    }

    public void handleRemoveEntityEffect(final WrappedPacketOutRemoveEntityEffect wrapper) {
        ++this.removeEntityEffectTransactionId;
        ++this.removeEntityEffectPingId;
        if (this.removeEntityEffectTransactionId > -26769) {
            this.removeEntityEffectTransactionId = -27768;
        }
        if (this.removeEntityEffectPingId > -26769) {
            this.removeEntityEffectPingId = -27768;
        }
        if (ServerUtil.isHigherThan1_17()) {
            this.data.sendPing(this.removeEntityEffectPingId);
        } else {
            this.data.sendTransaction(this.removeEntityEffectTransactionId);
        }
        this.queuedRemoveEntityEffects.put(this.removeEntityEffectTransactionId, wrapper);
    }

    public void handleGameStateChange(final WrappedPacketOutGameStateChange wrapper) {
        ++this.gameStateChangeTransactionId;
        ++this.gameStateChangePingId;
        if (this.gameStateChangeTransactionId > -25769) {
            this.gameStateChangeTransactionId = -26768;
        }
        if (this.gameStateChangePingId > -25769) {
            this.gameStateChangePingId = -26768;
        }
        if (ServerUtil.isHigherThan1_17()) {
            this.data.sendPing(this.gameStateChangePingId);
        } else {
            this.data.sendTransaction(this.gameStateChangeTransactionId);
        }
        this.queuedGameStateChanges.put(this.gameStateChangeTransactionId, wrapper);
    }

    private void confirmPosition(final WrappedPacketInTransaction wrapper) {
        if (this.queuedTeleports.containsKey(wrapper.getActionNumber())) {
            final WrappedPacketOutPosition position = this.queuedTeleports.get(wrapper.getActionNumber());
            final Vector3d vector = position.getPosition();
            this.lastTeleportX = vector.getX();
            this.lastTeleportY = vector.getY();
            this.lastTeleportZ = vector.getZ();
            this.sinceTeleportTicks = 0;
            this.queuedTeleports.remove(wrapper.getActionNumber());
        }
    }

    public void confirmPositionPing(final WrappedPacketInPong wrapper) {
        final short id = (short) wrapper.getId();
        if (this.queuedTeleports.containsKey(id)) {
            final WrappedPacketOutPosition position = this.queuedTeleports.get(id);
            final Vector3d vector = position.getPosition();
            this.lastTeleportX = vector.getX();
            this.lastTeleportY = vector.getY();
            this.lastTeleportZ = vector.getZ();
            this.sinceTeleportTicks = 0;
            this.queuedTeleports.remove(id);
        }
    }

    private void confirmEntityEffect(final WrappedPacketInTransaction wrapper) {
        if (this.queuedEntityEffects.containsKey(wrapper.getActionNumber())) {
            final WrappedPacketOutEntityEffect confirmation = this.queuedEntityEffects.get(wrapper.getActionNumber());
            if (confirmation == null) {
                return;
            }
            final int amplifier = confirmation.getAmplifier() + 1;
            switch (confirmation.getEffectId()) {
                case 1: {
                    this.speedAmplifier = amplifier;
                    this.hasSpeed = true;
                    break;
                }
                case 2: {
                    this.hasSlowness = true;
                    break;
                }
                case 8: {
                    this.jumpBoostAmplifier = amplifier;
                    this.hasJumpBoost = true;
                    break;
                }
                case 25: {
                    this.levitationAmplifier = amplifier;
                    this.hasLevitation = true;
                    break;
                }
                case 28: {
                    this.slowFallingAmplifier = amplifier;
                    this.hasSlowFalling = true;
                    break;
                }
                case 29: {
                    this.hasConduitsPower = true;
                    break;
                }
                case 30: {
                    this.dolphinsGraceAmplifier = amplifier;
                    this.hasDolphinsGrace = true;
                    break;
                }
            }
            this.queuedEntityEffects.remove(wrapper.getActionNumber());
        }
    }

    private void confirmEntityEffectPing(final WrappedPacketInPong wrapper) {
        final short id = (short) wrapper.getId();
        if (this.queuedEntityEffects.containsKey(id)) {
            final WrappedPacketOutEntityEffect confirmation = this.queuedEntityEffects.get(id);
            final int amplifier = confirmation.getAmplifier() + 1;
            switch (confirmation.getEffectId()) {
                case 1: {
                    this.speedAmplifier = amplifier;
                    this.hasSpeed = true;
                    break;
                }
                case 8: {
                    this.jumpBoostAmplifier = amplifier;
                    this.hasJumpBoost = true;
                    break;
                }
                case 25: {
                    this.levitationAmplifier = amplifier;
                    this.hasLevitation = true;
                    break;
                }
                case 28: {
                    this.slowFallingAmplifier = amplifier;
                    this.hasSlowFalling = true;
                    break;
                }
                case 29: {
                    this.hasConduitsPower = true;
                    break;
                }
                case 30: {
                    this.dolphinsGraceAmplifier = amplifier;
                    this.hasDolphinsGrace = true;
                    break;
                }
            }
            this.queuedEntityEffects.remove(id);
        }
    }

    private void confirmRemoveEntityEffectPing(final WrappedPacketInPong wrapper) {
        final short id = (short) wrapper.getId();
        if (this.queuedRemoveEntityEffects.containsKey(id)) {
            final WrappedPacketOutRemoveEntityEffect confirmation = this.queuedRemoveEntityEffects.get(id);
            if (confirmation == null) {
                return;
            }
            switch (confirmation.getEffectId()) {
                case 1: {
                    this.speedAmplifier = 0;
                    this.hasSpeed = false;
                    this.data.getPositionProcessor().setSinceSpeedTicks(0);
                    break;
                }
                case 2: {
                    this.hasSlowness = false;
                    break;
                }
                case 8: {
                    this.jumpBoostAmplifier = 0;
                    this.hasJumpBoost = false;
                    break;
                }
                case 25: {
                    this.levitationAmplifier = 0;
                    this.hasLevitation = false;
                    break;
                }
                case 28: {
                    this.slowFallingAmplifier = 0;
                    this.hasSlowFalling = false;
                    break;
                }
                case 29: {
                    this.hasConduitsPower = false;
                    break;
                }
                case 30: {
                    this.dolphinsGraceAmplifier = 0;
                    this.hasDolphinsGrace = false;
                    break;
                }
            }
            this.queuedRemoveEntityEffects.remove(id);
        }
    }

    private void confirmRemoveEntityEffect(final WrappedPacketInTransaction wrapper) {
        if (this.queuedRemoveEntityEffects.containsKey(wrapper.getActionNumber())) {
            final WrappedPacketOutRemoveEntityEffect confirmation = this.queuedRemoveEntityEffects.get(wrapper.getActionNumber());
            if (confirmation == null) {
                return;
            }
            switch (confirmation.getEffectId()) {
                case 1: {
                    this.speedAmplifier = 0;
                    this.hasSpeed = false;
                    this.data.getPositionProcessor().setSinceSpeedTicks(0);
                    break;
                }
                case 8: {
                    this.jumpBoostAmplifier = 0;
                    this.hasJumpBoost = false;
                    break;
                }
                case 25: {
                    this.levitationAmplifier = 0;
                    this.hasLevitation = false;
                    break;
                }
                case 28: {
                    this.slowFallingAmplifier = 0;
                    this.hasSlowFalling = false;
                    break;
                }
                case 29: {
                    this.hasConduitsPower = false;
                    break;
                }
                case 30: {
                    this.dolphinsGraceAmplifier = 0;
                    this.hasDolphinsGrace = false;
                    break;
                }
            }
            this.queuedRemoveEntityEffects.remove(wrapper.getActionNumber());
        }
    }

    private void confirmGameStateChangePing(final WrappedPacketInPong wrapper) {
        final short id = (short) wrapper.getId();
        if (this.queuedGameStateChanges.containsKey(id)) {
            final WrappedPacketOutGameStateChange confirmation = this.queuedGameStateChanges.get(id);
            if (confirmation == null) {
                return;
            }
            if (confirmation.getReason() == 3) {
                switch ((int) confirmation.getValue()) {
                    case 0: {
                        this.gameMode = GameMode.SURVIVAL;
                        break;
                    }
                    case 1: {
                        this.gameMode = GameMode.CREATIVE;
                        break;
                    }
                    case 2: {
                        this.gameMode = GameMode.ADVENTURE;
                        break;
                    }
                    case 3: {
                        if (ServerUtil.isHigherThan1_7()) {
                            this.gameMode = GameMode.SPECTATOR;
                            break;
                        }
                        break;
                    }
                }
            }
            this.queuedGameStateChanges.remove(id);
        }
    }

    private void confirmGameStateChange(final WrappedPacketInTransaction wrapper) {
        if (this.queuedGameStateChanges.containsKey(wrapper.getActionNumber())) {
            final WrappedPacketOutGameStateChange confirmation = this.queuedGameStateChanges.get(wrapper.getActionNumber());
            if (confirmation == null) {
                return;
            }
            if (confirmation.getReason() == 3) {
                switch ((int) confirmation.getValue()) {
                    case 0: {
                        this.gameMode = GameMode.SURVIVAL;
                        break;
                    }
                    case 1: {
                        this.gameMode = GameMode.CREATIVE;
                        break;
                    }
                    case 2: {
                        this.gameMode = GameMode.ADVENTURE;
                        break;
                    }
                    case 3: {
                        if (ServerUtil.isHigherThan1_7()) {
                            this.gameMode = GameMode.SPECTATOR;
                            break;
                        }
                        break;
                    }
                }
            }
            this.queuedGameStateChanges.remove(wrapper.getActionNumber());
        }
    }

    public void handlePong(final WrappedPacketInPong wrapper) {
        this.confirmPositionPing(wrapper);
        this.confirmEntityEffectPing(wrapper);
        this.confirmGameStateChangePing(wrapper);
        this.confirmRemoveEntityEffectPing(wrapper);
    }

    public void handleTransaction(final WrappedPacketInTransaction wrapper) {
        this.confirmPosition(wrapper);
        this.confirmEntityEffect(wrapper);
        this.confirmGameStateChange(wrapper);
        this.confirmRemoveEntityEffect(wrapper);
    }

    public void handleWorldChange() {
        this.sinceWorldChangeTicks = 0;
        this.data.getPositionProcessor().setServerAirTicks(0);
    }

    public void handleDeath() {
        this.sinceDeathTicks = 0;
    }

    public void handleDamage(final EntityDamageEvent.DamageCause cause) {
        this.lastDamage = Anticheat.INSTANCE.getTickManager().getTicks();
        this.sinceDamageTicks = 0;
        if (cause != EntityDamageEvent.DamageCause.FALL && cause != EntityDamageEvent.DamageCause.SUFFOCATION) {
            this.sinceNonFallDamageTicks = 0;
        }
        switch (cause) {
            case SUFFOCATION: {
                this.sinceSuffociationDamageTicks = 0;
                break;
            }
            case ENTITY_EXPLOSION:
            case BLOCK_EXPLOSION: {
                this.sinceExplosionDamageTicks = 0;
                break;
            }
            case ENTITY_ATTACK: {
                this.sinceAttackDamageTicks = 0;
                break;
            }
            case FIRE:
            case FIRE_TICK: {
                this.sinceFireDamageTicks = 0;
                break;
            }
            case FALL: {
                this.sinceFallDamageTicks = 0;
                break;
            }
            case MAGIC: {
                this.sinceMagicDamageTicks = 0;
                break;
            }
            case POISON: {
                this.sincePoisonDamageTicks = 0;
                break;
            }
            case CONTACT: {
                this.sinceContactDamageTicks = 0;
                break;
            }
            case PROJECTILE: {
                this.sinceProjectileDamageTicks = 0;
                break;
            }
            case LAVA: {
                this.sinceLavaDamageTicks = 0;
                break;
            }
        }
    }

    public void handleEnderPearl() {
        this.sinceEnderPearlTicks = 0;
    }

    public void handleBedEnter() {
        this.lastBedEnter = Anticheat.INSTANCE.getTickManager().getTicks();
    }

    public void handleBedLeave() {
        this.lastBedLeave = Anticheat.INSTANCE.getTickManager().getTicks();
    }

    public void handleFlying(final WrappedPacketInFlying wrapper) {
        this.blocking = false;
        this.sendingDig = false;
        this.sendingAction = false;
        this.placing = false;
        ++this.ticksExisted;
        ++this.sinceFireDamageTicks;
        ++this.sinceMagicDamageTicks;
        ++this.sinceFallDamageTicks;
        ++this.sinceExplosionTicks;
        ++this.sinceAttackDamageTicks;
        ++this.sinceNonFallDamageTicks;
        ++this.sinceContactDamageTicks;
        ++this.sinceWitherDamageTicks;
        ++this.sincePoisonDamageTicks;
        ++this.sinceSuffociationDamageTicks;
        ++this.sincePushTicks;
        ++this.sinceProjectileDamageTicks;
        ++this.sinceLavaDamageTicks;
        ++this.sinceDragonDamageTicks;
        ++this.sinceHoglinDamageTicks;
        ++this.sinceStupidBucketEmptyTicks;
        ++this.sinceExplosionDamageTicks;
        ++this.sinceMythicMobTicks;
        ++this.sinceBucketEmptyTicks;
        ++this.sinceBlockPlaceTicks;
        ++this.sinceIronGolemDamageTicks;
        ++this.sinceGlassBottleFillTicks;
        ++this.sinceClimbablePlaceTicks;
        ++this.sinceSlimePlaceTicks;
        ++this.sinceCrystalDamageTicks;
        ++this.sinceWorldChangeTicks;
        ++this.sinceRessurectTicks;
        ++this.sinceBukkitTeleportTicks;
        ++this.sinceDeathTicks;
        ++this.sinceBerserkTicks;
        ++this.sinceCrackshotDamageTicks;
        ++this.sinceFireballDamageTicks;
        ++this.sinceWebPlaceTicks;
        ++this.sinceBukkitVelocityTicks;
        ++this.sinceChorusFruitTeleportTicks;
        ++this.sinceBowBoostTicks;
        ++this.sinceEnderPearlTicks;
        ++this.sinceCancelledPlaceTicks;
        ++this.sinceAbilitiesTicks;
        ++this.sinceDamageTicks;
        ++this.sinceStartGlidingTicks;
        ++this.sinceIcePlaceTicks;
        ++this.sinceBlockBreakTicks;
        ++this.sinceRavagerDamageTicks;
        ++this.sinceNetherrackBreakTicks;
        ++this.sinceTeleportTicks;
        this.health = this.data.getPlayer().getHealth();
        if (wrapper.isMoving()) {
            ++this.positionTicksExisted;
            this.helmet = this.data.getPlayer().getInventory().getHelmet();
            this.chestplate = this.data.getPlayer().getInventory().getChestplate();
            this.leggings = this.data.getPlayer().getInventory().getLeggings();
            this.boots = this.data.getPlayer().getInventory().getBoots();
            this.itemInMainHand = this.data.getPlayer().getInventory().getItemInHand();
            if (ServerUtil.isHigherThan1_9()) {
                this.itemInOffHand = this.data.getPlayer().getInventory().getItemInOffHand();
            }
            if (ServerUtil.isHigherThan1_13()) {
                if (this.helmet != null && this.helmet.hasItemMeta() && this.helmet.getItemMeta() != null && this.helmet.getItemMeta().hasAttributeModifiers()) {
                    this.data.getPositionProcessor().setSinceAttributeModifierTicks(0);
                }
                if (this.chestplate != null && this.chestplate.hasItemMeta() && this.chestplate.getItemMeta() != null && this.chestplate.getItemMeta().hasAttributeModifiers()) {
                    this.data.getPositionProcessor().setSinceAttributeModifierTicks(0);
                }
                if (this.leggings != null && this.leggings.hasItemMeta() && this.leggings.getItemMeta() != null && this.leggings.getItemMeta().hasAttributeModifiers()) {
                    this.data.getPositionProcessor().setSinceAttributeModifierTicks(0);
                }
                if (this.boots != null && this.boots.hasItemMeta() && this.boots.getItemMeta() != null && this.boots.getItemMeta().hasAttributeModifiers()) {
                    this.data.getPositionProcessor().setSinceAttributeModifierTicks(0);
                }
                if (this.itemInOffHand != null && this.itemInOffHand.hasItemMeta() && this.itemInOffHand.getItemMeta() != null && this.itemInOffHand.getItemMeta().hasAttributeModifiers()) {
                    this.data.getPositionProcessor().setSinceAttributeModifierTicks(0);
                }
                if (this.itemInMainHand != null && this.itemInMainHand.hasItemMeta() && this.itemInMainHand.getItemMeta() != null && this.itemInMainHand.getItemMeta().hasAttributeModifiers()) {
                    this.data.getPositionProcessor().setSinceAttributeModifierTicks(0);
                }
            }
            this.wearingDepthStrider = (ServerUtil.isHigherThan1_7() && this.boots != null && this.boots.containsEnchantment(Enchantment.DEPTH_STRIDER));
            this.wearingElytra = (ServerUtil.isHigherThan1_9() && this.chestplate != null && this.chestplate.getType().toString().equals("ELYTRA"));
            this.bukkitGliding = PlayerUtil.isGliding(this.data.getPlayer());
            if (this.bukkitGliding) {
                this.lastGlidingDeltaXZ = this.data.getPositionProcessor().getDeltaXZ();
            }
            this.teleporting = false;
            final double x = wrapper.getX();
            final double y = wrapper.getY();
            final double z = wrapper.getZ();
            if (this.sprinting) {
                ++this.sprintingTicks;
            } else {
                this.sprintingTicks = 0;
            }
            if (!this.sprinting) {
                ++this.sinceSprintingTicks;
            } else {
                this.sinceSprintingTicks = 0;
            }
            if (this.lastBreakX != 0.0 && this.lastBreakZ != 0.0) {
                this.distanceFromLastBreak = MathUtil.magnitude(Math.abs(x - this.lastBreakX), Math.abs(y - this.lastBreakY), Math.abs(z - this.lastBreakZ));
                if (this.distanceFromLastBreak > 8.0) {
                    final double lastBreakX = 0.0;
                    this.distanceFromLastBreak = lastBreakX;
                    this.lastBreakZ = lastBreakX;
                    this.lastBreakX = lastBreakX;
                }
            }
            if (this.lastScaffoldPlaceX != 0 && this.lastScaffoldPlaceY != 0 && this.lastScaffoldPlaceZ != 0) {
                this.distanceFromLastScaffoldPlace = MathUtil.magnitude(Math.abs(x - this.lastScaffoldPlaceX), Math.abs(y - this.lastScaffoldPlaceY), Math.abs(z - this.lastScaffoldPlaceZ));
                if (this.distanceFromLastScaffoldPlace > 12.0) {
                    final int lastScaffoldPlaceX = 0;
                    this.lastScaffoldPlaceZ = lastScaffoldPlaceX;
                    this.lastScaffoldPlaceY = lastScaffoldPlaceX;
                    this.lastScaffoldPlaceX = lastScaffoldPlaceX;
                }
            }
            for (final Vector teleport : this.teleports) {
                final double deltaX = Math.abs(teleport.getX() - x);
                final double deltaY = Math.abs(teleport.getY() - y);
                final double deltaZ = Math.abs(teleport.getZ() - z);
                if (deltaX <= 0.03 && deltaY <= 0.03 && deltaZ <= 0.03) {
                    this.teleporting = true;
                }
            }
        }
    }

    public void handleSlimePlace() {
        this.sinceSlimePlaceTicks = 0;
    }

    public void handleCancelledPlace() {
        this.sinceCancelledPlaceTicks = 0;
    }

    public void handleIcePlace() {
        this.sinceIcePlaceTicks = 0;
    }

    public void handleBukkitVelocity() {
        this.sinceBukkitVelocityTicks = 0;
    }

    public void handleClimbablePlace() {
        this.sinceClimbablePlaceTicks = 0;
    }

    public void handleWebPlace() {
        this.sinceWebPlaceTicks = 0;
    }

    public void handleProjectileThrow() {
        this.lastProjectileThrow = Anticheat.INSTANCE.getTickManager().getTicks();
    }

    public void handleFishEvent() {
        this.lastFishEvent = Anticheat.INSTANCE.getTickManager().getTicks();
    }

    public void handleAbilities(final WrappedPacketOutAbilities wrapper) {
        this.sinceAbilitiesTicks = 0;
    }

    public void handleResurrect() {
        this.sinceRessurectTicks = 0;
    }

    public void handleScaffoldingPlace(final BlockPlaceEvent event) {
        this.lastScaffoldPlaceX = event.getBlockPlaced().getX();
        this.lastScaffoldPlaceY = event.getBlockPlaced().getY();
        this.lastScaffoldPlaceZ = event.getBlockPlaced().getZ();
    }

    public void handleCrackshotDamage() {
        this.sinceCrackshotDamageTicks = 0;
    }

    public void handleChorusEat() {
        this.sinceChorusFruitTeleportTicks = 0;
    }

    public void handleMetaData(final WrappedPacketOutEntityMetadata wrapper) {
        final WrappedWatchableObject watchable = this.getIndex(wrapper.getWatchableObjects(), 0);
        if (watchable != null) {
            final Object zeroBitField = watchable.getRawValue();
            if (zeroBitField instanceof Byte) {
                final byte field = (byte) zeroBitField;
                this.packetGliding = (PlayerUtil.isHigherThan1_9(this.data.getPlayer()) && (field & 0x80) == 0x80);
                this.packetSwimming = ((field & 0x10) == 0x10);
            }
        }
    }

    private WrappedWatchableObject getIndex(final List<WrappedWatchableObject> objects, final int index) {
        for (final WrappedWatchableObject object : objects) {
            if (object.getIndex() == index) {
                return object;
            }
        }
        return null;
    }

    public void handleExplosion(final WrappedPacketOutExplosion wrapper) {
        final Vector3f velocity = wrapper.getPlayerVelocity();
        this.explosionX = velocity.getX();
        this.explosionY = velocity.getY();
        this.explosionZ = velocity.getZ();
        this.sinceExplosionTicks = 0;
    }

    public void onJoin() {
        this.hasSpeed = this.data.getPlayer().hasPotionEffect(PotionEffectType.SPEED);
        if (this.hasSpeed) {
            if (ServerUtil.isHigherThan1_13()) {
                this.speedAmplifier = this.data.getPlayer().getPotionEffect(PotionEffectType.SPEED).getAmplifier() + 1;
            } else {
                this.speedAmplifier = PlayerUtil.getPotionLevel(this.data.getPlayer(), PotionEffectType.SPEED);
            }
        }
        this.hasSlowness = this.data.getPlayer().hasPotionEffect(PotionEffectType.SLOW);
        this.hasJumpBoost = this.data.getPlayer().hasPotionEffect(PotionEffectType.JUMP);
        if (this.hasJumpBoost) {
            if (ServerUtil.isHigherThan1_13()) {
                this.jumpBoostAmplifier = this.data.getPlayer().getPotionEffect(PotionEffectType.JUMP).getAmplifier() + 1;
            } else {
                this.jumpBoostAmplifier = PlayerUtil.getPotionLevel(this.data.getPlayer(), PotionEffectType.JUMP);
            }
        }
        if (ServerUtil.isHigherThan1_11()) {
            this.hasLevitation = this.data.getPlayer().hasPotionEffect(PotionEffectType.LEVITATION);
            if (this.hasLevitation) {
                if (ServerUtil.isHigherThan1_13()) {
                    this.levitationAmplifier = this.data.getPlayer().getPotionEffect(PotionEffectType.LEVITATION).getAmplifier() + 1;
                } else {
                    this.levitationAmplifier = PlayerUtil.getPotionLevel(this.data.getPlayer(), PotionEffectType.LEVITATION);
                }
            }
        }
        if (ServerUtil.isHigherThan1_13()) {
            this.hasDolphinsGrace = this.data.getPlayer().hasPotionEffect(PotionEffectType.DOLPHINS_GRACE);
            if (this.hasDolphinsGrace) {
                this.dolphinsGraceAmplifier = this.data.getPlayer().getPotionEffect(PotionEffectType.DOLPHINS_GRACE).getAmplifier() + 1;
            }
            this.hasConduitsPower = this.data.getPlayer().hasPotionEffect(PotionEffectType.CONDUIT_POWER);
            this.hasSlowFalling = this.data.getPlayer().hasPotionEffect(PotionEffectType.SLOW_FALLING);
            if (this.hasSlowFalling) {
                this.slowFallingAmplifier = this.data.getPlayer().getPotionEffect(PotionEffectType.SLOW_FALLING).getAmplifier() + 1;
            }
        }
    }
}
