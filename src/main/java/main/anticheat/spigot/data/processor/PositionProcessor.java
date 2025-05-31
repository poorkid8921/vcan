package main.anticheat.spigot.data.processor;

import io.github.retrooper.packetevents.packetwrappers.play.in.blockdig.WrappedPacketInBlockDig;
import io.github.retrooper.packetevents.packetwrappers.play.in.flying.WrappedPacketInFlying;
import io.github.retrooper.packetevents.packetwrappers.play.in.pong.WrappedPacketInPong;
import io.github.retrooper.packetevents.packetwrappers.play.in.transaction.WrappedPacketInTransaction;
import io.github.retrooper.packetevents.packetwrappers.play.in.vehiclemove.WrappedPacketInVehicleMove;
import io.github.retrooper.packetevents.packetwrappers.play.out.abilities.WrappedPacketOutAbilities;
import io.github.retrooper.packetevents.packetwrappers.play.out.blockchange.WrappedPacketOutBlockChange;
import lombok.Getter;
import lombok.Setter;
import main.anticheat.api.event.VulcanGhostBlockEvent;
import main.anticheat.spigot.Anticheat;
import main.anticheat.spigot.config.Config;
import main.anticheat.spigot.data.PlayerData;
import main.anticheat.spigot.exempt.ExemptProcessor;
import main.anticheat.spigot.exempt.type.ExemptType;
import main.anticheat.spigot.util.*;
import org.bukkit.*;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.*;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.util.NumberConversions;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;

@Getter
@Setter
public class PositionProcessor {
    private final PlayerData data;
    private final Map<Short, WrappedPacketOutAbilities> queuedAbilities;
    public boolean frozen;
    public List<Entity> nearbyEntities;
    public List<Material> nearbyBlocks;
    public List<Material> vehicleBlocks;
    public List<Material> blocksUnder;
    public Material blockBelow;
    public Material blockBelow2;
    public Material blockBelow3;
    private List<Material> blocksBelow;
    private List<Material> blocksAbove;
    private List<Material> blocksAround;
    private List<Material> glitchedBlocksAbove;
    private boolean nearFence;
    private boolean nearLiquid;
    private boolean nearSlime;
    private boolean nearSoulSand;
    private boolean nearClimbable;
    private boolean nearWeb;
    private boolean nearLilyPad;
    private boolean nearDaylightSensor;
    private boolean nearBrewingStand;
    private boolean nearWall;
    private boolean nearStair;
    private boolean nearSlab;
    private boolean nearHoney;
    private boolean nearScaffolding;
    private boolean nearTrapdoor;
    private boolean nearSkull;
    private boolean nearPortalFrame;
    private boolean nearCampfire;
    private boolean nearSweetBerries;
    private boolean nearShulkerBox;
    private boolean vehicleNearBubbleColumn;
    private boolean nearSnow;
    private boolean nearAnvil;
    private boolean nearEndRod;
    private boolean nearChain;
    private boolean nearPiston;
    private boolean nearDoor;
    private boolean touchingAir;
    private boolean nearCauldron;
    private boolean nearLava;
    private boolean nearHopper;
    private boolean nearFenceGate;
    private boolean nearFlowerPot;
    private boolean onClimbable;
    private boolean inWeb;
    private boolean inBubbleColumn;
    private boolean fullySubmerged;
    private boolean inLiquid;
    private boolean collidingVertically;
    private boolean nearKelp;
    private boolean fullyStuck;
    private boolean partiallyStuck;
    private boolean onIce;
    private boolean clientOnGround;
    private boolean mathematicallyOnGround;
    private boolean nearShulker;
    private boolean nearBoat;
    private boolean nearBell;
    private boolean nearBed;
    private boolean nearCarpet;
    private boolean nearLectern;
    private boolean nearTurtleEgg;
    private boolean nearSeaPickle;
    private boolean nearIce;
    private boolean nearConduit;
    private boolean collidingHorizontally;
    private boolean nearRepeater;
    private boolean nearSolid;
    private boolean nearPane;
    private boolean vehicleNearIce;
    private boolean vehicleNearLiquid;
    private boolean vehicleNearSlime;
    private boolean vehicleInAir;
    private boolean moving;
    private boolean nearFrostedIce;
    private boolean nearBubbleColumn;
    private boolean nearFarmland;
    private boolean lastClientOnGround;
    private boolean allowFlight;
    private boolean nearSeaGrass;
    private boolean nearChest;
    private boolean vehicleNearPiston;
    private boolean nearCake;
    private boolean vehicleNearBed;
    private boolean nearAmethyst;
    private boolean nearDripstone;
    private boolean nearPowderSnow;
    private boolean setbackGround;
    private boolean collidingEntity;
    private boolean lastLastClientOnGround;
    private boolean vehicleNearEntity;
    private boolean nearPath;
    private boolean nearLantern;
    private boolean nearBorder;
    private boolean nearRail;
    private boolean nearSign;
    private boolean nearBamboo;
    private boolean nearPressurePlate;
    private boolean fuckedPosition;
    private boolean nearEnchantmentTable;
    private Location from;
    private Location to;
    private short abilitiesTransactionId;
    private short abilitiesPingId;
    private double x;
    private double y;
    private double z;
    private double lastX;
    private double lastY;
    private double lastZ;
    private double deltaX;
    private double deltaY;
    private double deltaZ;
    private double deltaXZ;
    private double deltaXYZ;
    private double lastDeltaX;
    private double lastDeltaZ;
    private double lastDeltaY;
    private double lastDeltaXZ;
    private double vehicleX;
    private double vehicleY;
    private double vehicleZ;
    private double lastVehicleX;
    private double lastVehicleY;
    private double lastVehicleZ;
    private double lastVehicleDeltaX;
    private double lastVehicleDeltaY;
    private double lastVehicleDeltaZ;
    private double vehicleDeltaX;
    private double vehicleDeltaY;
    private double vehicleDeltaZ;
    private double vehicleDeltaXZ;
    private double lastOnGroundX;
    private double lastOnGroundY;
    private double lastOnGroundZ;
    private double setbackX;
    private double setbackY;
    private double setbackZ;
    private double ghostBlockBuffer;
    private double newSetbackX;
    private double newSetbackY;
    private double newSetbackZ;
    private double ghostBlockSetbackX;
    private double ghostBlockSetbackY;
    private double ghostBlockSetbackZ;
    private double lastLegitX;
    private double lastLegitY;
    private double lastLegitZ;
    private double lastLastX;
    private double lastLastY;
    private double lastLastZ;
    private double firstJoinX;
    private double firstJoinY;
    private double firstJoinZ;
    private double unloadedChunkBuffer;
    private int sinceVehicleTicks;
    private int sinceFlyingTicks;
    private int sinceTrapdoorTicks;
    private int clientAirTicks;
    private int clientGroundTicks;
    private int sinceSwimmingTicks;
    private int sinceRiptideTicks;
    private int sinceGlidingTicks;
    private int sinceIceTicks;
    private int sinceDolphinsGraceTicks;
    private int sinceLevitationTicks;
    private int sinceBubbleColumnTicks;
    private int glidingTicks;
    private int sinceSlowFallingTicks;
    private int serverAirTicks;
    private int serverGroundTicks;
    private int sinceWebTicks;
    private int sinceLiquidTicks;
    private int climbableTicks;
    private int sinceJumpBoostTicks;
    private int sinceNearSlimeTicks;
    private int sinceHoneyTicks;
    private int sinceCollidingVerticallyTicks;
    private int sinceNearFenceTicks;
    private int sinceSoulSpeedTicks;
    private int sinceNearBedTicks;
    private int lastPulledByFishingRod;
    private int vehicleTicks;
    private int sinceFireworkTicks;
    private int sinceNearIceTicks;
    private int sinceNearPistonTicks;
    private int lastWaterLogged;
    private int sinceSpeedTicks;
    private int lastAttributeModifier;
    private int sinceAroundSlimeTicks;
    private int sinceAroundPistonTicks;
    private int sinceVehicleNearIceTicks;
    private int sinceVehicleNearLiquidTicks;
    private int sinceVehicleNearSlimeTicks;
    private int sinceVehicleNearBubbleColumnTicks;
    private int blockX;
    private int blockY;
    private int blockZ;
    private int sinceNearFrostedIceTicks;
    private int sinceNearScaffoldingTicks;
    private int ticksSinceGhostBlockSetback;
    private int lastServerAbilities;
    private int vehicleAirTicks;
    private int sinceNearShulkerBoxTicks;
    private int sinceElytraTicks;
    private int lastGhostBlockSetback;
    private int sinceNearFarmlandTicks;
    private int sinceNearStairTicks;
    private int sinceNearSlabTicks;
    private int sinceEntityCollisionTicks;
    private int sinceVehicleNearPistonTicks;
    private int sinceFishingRodTicks;
    private int sinceAttributeModifierTicks;
    private int sinceHighFlySpeedTicks;
    private int sinceAroundSlabTicks;
    private int sinceVehicleNearBedTicks;
    private int sinceWaterLogTicks;
    private int sinceGroundSpeedFailTicks;
    private int sinceNearClimbableTicks;
    private int boatsAround;
    private int sinceSetbackTicks;
    private int sinceCollidingHorizontallyTicks;
    private int sinceFlagTicks;
    private int sinceFuckingEntityTicks;
    private int sinceSpectatorTicks;
    private World world;
    private Entity bukkitVehicle;
    private Entity lastBukkitVehicle;
    private float moveForward;
    private float moveStrafing;
    private float walkSpeed;
    private float flySpeed;
    private boolean exemptFlight;
    private boolean exemptCreative;
    private boolean exemptJoined;
    private boolean exemptLiquid;
    private boolean exemptLevitation;
    private boolean exemptSlowFalling;
    private boolean exemptRiptide;
    private boolean exemptVehicle;
    private boolean exemptLenientScaffolding;
    private boolean exemptBukkitVelocity;
    private boolean exemptGliding;
    private boolean exemptElytra;
    private boolean exemptTeleport;
    private boolean exemptEnderPearl;
    private boolean exemptChunk;
    private boolean exemptComboMode;
    private boolean exemptMythicMob;
    private boolean exemptClimbable;

    public PositionProcessor(final PlayerData data) {
        this.abilitiesTransactionId = -30768;
        this.abilitiesPingId = -30768;
        this.queuedAbilities = new HashMap<Short, WrappedPacketOutAbilities>();
        this.sinceVehicleTicks = 100;
        this.sinceFlyingTicks = 100;
        this.sinceTrapdoorTicks = 100;
        this.sinceSwimmingTicks = 100;
        this.sinceRiptideTicks = 200;
        this.sinceGlidingTicks = 100;
        this.sinceDolphinsGraceTicks = 100;
        this.sinceLevitationTicks = 100;
        this.sinceBubbleColumnTicks = 100;
        this.sinceSlowFallingTicks = 100;
        this.sinceWebTicks = 100;
        this.sinceLiquidTicks = 100;
        this.sinceJumpBoostTicks = 100;
        this.sinceNearSlimeTicks = 100;
        this.sinceHoneyTicks = 100;
        this.sinceCollidingVerticallyTicks = 100;
        this.sinceNearFenceTicks = 100;
        this.sinceSoulSpeedTicks = 100;
        this.sinceNearBedTicks = 100;
        this.sinceFireworkTicks = 200;
        this.sinceNearIceTicks = 100;
        this.sinceNearPistonTicks = 100;
        this.sinceSpeedTicks = 100;
        this.sinceAroundSlimeTicks = 500;
        this.sinceAroundPistonTicks = 500;
        this.sinceVehicleNearIceTicks = 500;
        this.sinceVehicleNearLiquidTicks = 150;
        this.sinceVehicleNearSlimeTicks = 150;
        this.sinceVehicleNearBubbleColumnTicks = 150;
        this.sinceNearScaffoldingTicks = 100;
        this.ticksSinceGhostBlockSetback = 500;
        this.sinceNearShulkerBoxTicks = 100;
        this.sinceElytraTicks = 100;
        this.sinceNearFarmlandTicks = 100;
        this.sinceEntityCollisionTicks = 100;
        this.sinceVehicleNearPistonTicks = 100;
        this.sinceFishingRodTicks = 1000;
        this.sinceAttributeModifierTicks = 100;
        this.sinceHighFlySpeedTicks = 200;
        this.sinceAroundSlabTicks = 100;
        this.sinceVehicleNearBedTicks = 1000;
        this.sinceWaterLogTicks = 1000;
        this.sinceGroundSpeedFailTicks = 1000;
        this.sinceNearClimbableTicks = 1000;
        this.boatsAround = 0;
        this.sinceSetbackTicks = 1000;
        this.sinceCollidingHorizontallyTicks = 100;
        this.sinceFlagTicks = 1000;
        this.sinceFuckingEntityTicks = 100;
        this.sinceSpectatorTicks = 100;
        this.moveForward = 0.0f;
        this.moveStrafing = 0.0f;
        this.walkSpeed = 0.2f;
        this.flySpeed = 0.2f;
        this.data = data;
    }

    public void onJoin() {
        this.walkSpeed = this.data.getPlayer().getWalkSpeed();
        final Location location = this.data.getPlayer().getLocation();
        this.firstJoinX = location.getX();
        this.firstJoinY = location.getY();
        this.firstJoinZ = location.getZ();
        this.setbackX = location.getX();
        this.setbackY = location.getY();
        this.setbackZ = location.getZ();
    }

    public void handleFlying(final WrappedPacketInFlying wrapper) {
        this.lastLastClientOnGround = this.lastClientOnGround;
        this.lastClientOnGround = this.clientOnGround;
        this.clientOnGround = wrapper.isOnGround();
        this.handleFlyingTicks();
        if (wrapper.isPosition()) {
            this.fuckedPosition = (this.blockX == 0 && this.blockY == 0 && this.blockZ == 0);
            this.world = this.data.getPlayer().getWorld();
            this.lastLastX = this.lastX;
            this.lastLastY = this.lastY;
            this.lastLastZ = this.lastZ;
            this.lastX = this.x;
            this.lastY = this.y;
            this.lastZ = this.z;
            this.x = wrapper.getX();
            this.y = wrapper.getY();
            this.z = wrapper.getZ();
            this.from = ((this.to == null) ? new Location(this.world, this.x, this.y, this.z) : this.to);
            this.to = new Location(this.world, this.x, this.y, this.z);
            this.lastDeltaX = this.deltaX;
            this.lastDeltaY = this.deltaY;
            this.lastDeltaZ = this.deltaZ;
            this.lastDeltaXZ = this.deltaXZ;
            this.deltaX = this.x - this.lastX;
            this.deltaY = this.y - this.lastY;
            this.deltaZ = this.z - this.lastZ;
            this.deltaXZ = MathUtil.hypot(this.deltaX, this.deltaZ);
            this.deltaXYZ = MathUtil.magnitude(this.deltaX, this.deltaY, this.deltaZ);
            this.moving = (Math.abs(this.deltaX) > 0.0 || Math.abs(this.deltaZ) > 0.0 || Math.abs(this.deltaY) > 0.0);
            this.mathematicallyOnGround = (this.y % 0.015625 == 0.0);
            if (this.data.getPlayer().getVehicle() == null) {
                this.vehicleTicks = 0;
            }

            this.cacheBlocks();

            this.handleGhostBlock();
            this.handleUnloadedChunk();
            if (ServerUtil.isHigherThan1_13() && !this.frozen) {
                if (this.blockBelow != null && this.blockBelow != Material.AIR && this.blockBelow.isBlock() && this.mathematicallyOnGround && !BlockUtil.isLiquid(this.blockBelow) && this.sinceSetbackTicks > 5 && this.deltaXZ < 2.0 && !this.data.getActionProcessor().isTeleporting() && this.ticksSinceGhostBlockSetback > 10) {
                    this.setbackX = this.x;
                    this.setbackY = this.y;
                    this.setbackZ = this.z;
                }
            } else if (this.blockBelow != null && this.blockBelow.isSolid() && this.mathematicallyOnGround && !BlockUtil.isLiquid(this.blockBelow) && !this.frozen && this.deltaXZ < 2.0 && this.sinceSetbackTicks > 5 && !this.data.getActionProcessor().isTeleporting() && this.ticksSinceGhostBlockSetback > 10) {
                this.setbackX = this.x;
                this.setbackY = this.y;
                this.setbackZ = this.z;
            }
            this.handlePositionTicks();
            this.parseForwardAndStrafe();
            if (this.data.getActionProcessor().getGenericMovementSpeed() > 0.11) {
                this.sinceAttributeModifierTicks = 0;
            }
            this.cacheExemptions();
            if (Config.VELOCITY_WORLD_BORDER && ServerUtil.isHigherThan1_8()) {
                final WorldBorder worldBorder = this.world.getWorldBorder();
                final double centerX = worldBorder.getCenter().getX();
                final double centerZ = worldBorder.getCenter().getZ();
                final double size = worldBorder.getSize() / 2.0;
                double dx1 = this.x - centerX - size;
                double dx2 = this.x - centerX + size;
                double dx3 = this.z - centerZ - size;
                double dx4 = this.z - centerZ + size;
                if (dx1 < 0.0) {
                    dx1 *= -1.0;
                }
                if (dx2 < 0.0) {
                    dx2 *= -1.0;
                }
                if (dx3 < 0.0) {
                    dx3 *= -1.0;
                }
                if (dx4 < 0.0) {
                    dx4 *= -1.0;
                }
                final double[] distances = {dx1, dx2, dx3, dx4};
                Arrays.sort(distances);
                this.nearBorder = (distances[0] < 1.0);
            }
        }
    }

    private void cacheExemptions() {
        final ExemptProcessor exemptProcessor = this.data.getExemptProcessor();
        this.exemptFlight = exemptProcessor.isExempt(ExemptType.FLIGHT);
        this.exemptCreative = exemptProcessor.isExempt(ExemptType.CREATIVE);
        this.exemptJoined = exemptProcessor.isExempt(ExemptType.JOINED);
        this.exemptLiquid = exemptProcessor.isExempt(ExemptType.LIQUID);
        this.exemptLevitation = exemptProcessor.isExempt(ExemptType.LEVITATION);
        this.exemptSlowFalling = exemptProcessor.isExempt(ExemptType.SLOW_FALLING);
        this.exemptRiptide = exemptProcessor.isExempt(ExemptType.RIPTIDE);
        this.exemptVehicle = exemptProcessor.isExempt(ExemptType.VEHICLE);
        this.exemptLenientScaffolding = exemptProcessor.isExempt(ExemptType.LENIENT_SCAFFOLDING);
        this.exemptBukkitVelocity = exemptProcessor.isExempt(ExemptType.BUKKIT_VELOCITY);
        this.exemptGliding = exemptProcessor.isExempt(ExemptType.GLIDING);
        this.exemptElytra = exemptProcessor.isExempt(ExemptType.ELYTRA);
        this.exemptTeleport = exemptProcessor.isExempt(ExemptType.TELEPORT);
        this.exemptEnderPearl = exemptProcessor.isExempt(ExemptType.ENDER_PEARL);
        this.exemptChunk = exemptProcessor.isExempt(ExemptType.CHUNK);
        this.exemptComboMode = exemptProcessor.isExempt(ExemptType.COMBO_MODE);
        this.exemptMythicMob = exemptProcessor.isExempt(ExemptType.MYTHIC_MOB);
        this.exemptClimbable = exemptProcessor.isExempt(ExemptType.CLIMBABLE);
    }

    private void cacheBlocks() {
        if (this.nearbyBlocks == null || this.blockBelow == null || this.blockBelow2 == null || this.blockBelow3 == null) {
            return;
        }
        this.blockX = NumberConversions.floor(this.x);
        this.blockY = NumberConversions.floor(this.y);
        this.blockZ = NumberConversions.floor(this.z);
        this.blocksBelow = PlayerUtil.getBlocksBelow(this.data);
        this.blocksAbove = PlayerUtil.getBlocksAbove(this.data);
        this.blocksAround = PlayerUtil.getBlocksAround(this.data);
        this.glitchedBlocksAbove = PlayerUtil.getBlocksAboveGlitched(this.data);
        this.onIce = PlayerUtil.isOnIce(this.data);
        this.fullyStuck = PlayerUtil.isFullyStuck(this.data);
        this.partiallyStuck = PlayerUtil.isFullyStuck(this.data);
        this.collidingVertically = !StreamUtil.allMatch(this.blocksAbove, BlockUtil::isAir);
        this.collidingHorizontally = !StreamUtil.allMatch(this.blocksAround, BlockUtil::isAir);
        this.nearSolid = StreamUtil.anyMatch(this.nearbyBlocks, BlockUtil::isSolid);
        this.touchingAir = (StreamUtil.allMatch(this.blocksAround, BlockUtil::isAir) && StreamUtil.allMatch(this.blocksBelow, BlockUtil::isAir));
        this.handleCollisions();
    }

    private void handleCollisions() {
        if (this.nearbyBlocks != null && this.nearbyEntities != null) {
            final boolean b = false;
            this.nearPowderSnow = b;
            this.nearDripstone = b;
            this.nearCake = b;
            this.nearChest = b;
            this.nearSeaGrass = b;
            this.nearLava = b;
            this.nearFarmland = b;
            this.nearBubbleColumn = b;
            this.nearFrostedIce = b;
            this.nearSign = b;
            this.nearPressurePlate = b;
            this.nearEnchantmentTable = b;
            this.nearAmethyst = b;
            this.nearHoney = b;
            this.nearScaffolding = b;
            this.nearChain = b;
            this.nearEndRod = b;
            this.nearTurtleEgg = b;
            this.nearKelp = b;
            this.nearShulkerBox = b;
            this.nearSeaPickle = b;
            this.nearBell = b;
            this.nearLectern = b;
            this.nearBamboo = b;
            this.nearLantern = b;
            this.nearConduit = b;
            this.nearSweetBerries = b;
            this.nearCampfire = b;
            this.nearBed = b;
            this.nearSkull = b;
            this.nearPane = b;
            this.nearPiston = b;
            this.nearLilyPad = b;
            this.nearDoor = b;
            this.nearHopper = b;
            this.nearSlab = b;
            this.nearRail = b;
            this.nearRepeater = b;
            this.nearAnvil = b;
            this.nearSnow = b;
            this.nearFenceGate = b;
            this.nearIce = b;
            this.nearStair = b;
            this.nearCauldron = b;
            this.nearWall = b;
            this.nearBrewingStand = b;
            this.nearDaylightSensor = b;
            this.nearPath = b;
            this.nearPortalFrame = b;
            this.nearTrapdoor = b;
            this.nearFlowerPot = b;
            this.nearWeb = b;
            this.nearClimbable = b;
            this.nearSoulSand = b;
            this.nearCarpet = b;
            this.nearLiquid = b;
            this.nearFence = b;
            this.nearSlime = b;
            for (final Material material : this.nearbyBlocks) {
                this.nearSlime |= (material == Material.SLIME_BLOCK);
                this.nearLiquid |= (material == Material.WATER || material == Material.LAVA);
                this.nearWeb |= (material == Material.COBWEB);
                this.nearFlowerPot |= (material == Material.FLOWER_POT);
                this.nearPortalFrame |= (material == Material.END_PORTAL_FRAME);
                this.nearDaylightSensor |= (material == Material.DAYLIGHT_DETECTOR);
                this.nearBrewingStand |= (material == Material.BREWING_STAND);
                this.nearIce |= (material == Material.ICE || material == Material.PACKED_ICE || material == Material.FROSTED_ICE);
                this.nearSnow |= (material == Material.SNOW);
                this.nearAnvil |= (material == Material.ANVIL || material == Material.CHIPPED_ANVIL || material == Material.DAMAGED_ANVIL);
                this.nearRepeater |= (material == Material.REPEATER);
                this.nearHopper |= (material == Material.HOPPER);
                this.nearLilyPad |= (material == Material.LILY_PAD);
                this.nearPiston |= (material == Material.PISTON || material == Material.PISTON_HEAD || material == Material.MOVING_PISTON || material == Material.STICKY_PISTON);
                this.nearConduit |= (material == Material.CONDUIT);
                this.nearSeaPickle |= (material == Material.SEA_PICKLE);
                this.nearKelp |= (material == Material.KELP || material == Material.KELP_PLANT);
                this.nearTurtleEgg |= (material == Material.TURTLE_EGG);
                this.nearEndRod |= (material == Material.END_ROD);
                this.nearScaffolding |= BlockUtil.isScaffolding(material);
                this.nearHoney |= BlockUtil.isHoney(material);
                this.nearCauldron |= (material == Material.CAULDRON);
                this.nearCake |= (material == Material.CAKE);
                this.nearEnchantmentTable |= BlockUtil.isEnchantmentTable(material);
                this.nearAmethyst |= BlockUtil.isAmethyst(material);
                this.nearRail |= BlockUtil.isRail(material);
                this.nearLantern |= BlockUtil.isLantern(material);
                this.nearPowderSnow |= BlockUtil.isPowderSnow(material);
                this.nearDripstone |= BlockUtil.isDripstone(material);
                this.nearChest |= BlockUtil.isChest(material);
                this.nearSeaGrass |= BlockUtil.isSeaGrass(material);
                this.nearBell |= BlockUtil.isBell(material);
                this.nearChain |= BlockUtil.isChain(material);
                this.nearLectern |= BlockUtil.isLectern(material);
                this.nearSweetBerries |= BlockUtil.isSweetBerries(material);
                this.nearBubbleColumn |= BlockUtil.isBubbleColumn(material);
                this.nearFrostedIce |= BlockUtil.isFrostedIce(material);
                this.nearSoulSand |= BlockUtil.isSoulSand(material);
                this.nearCarpet |= BlockUtil.isCarpet(material);
                this.nearTrapdoor |= BlockUtil.isTrapdoor(material);
                this.nearWall |= BlockUtil.isWall(material);
                this.nearFarmland |= BlockUtil.isFarmland(material);
                this.nearStair |= BlockUtil.isStair(material);
                this.nearPressurePlate |= BlockUtil.isPressurePlate(material);
                this.nearFence |= BlockUtil.isFence(material);
                this.nearClimbable |= BlockUtil.isClimbable(material);
                this.nearFenceGate |= BlockUtil.isFenceGate(material);
                this.nearSlab |= BlockUtil.isSlab(material);
                this.nearDoor |= BlockUtil.isDoor(material);
                this.nearPane |= BlockUtil.isPane(material);
                this.nearSkull |= BlockUtil.isSkull(material);
                this.nearBed |= BlockUtil.isBed(material);
                this.nearCampfire |= BlockUtil.isCampfire(material);
                this.nearShulkerBox |= BlockUtil.isShulkerBox(material);
                this.nearPath |= BlockUtil.isPath(material);
                this.nearSign |= BlockUtil.isSign(material);
                this.nearBamboo |= BlockUtil.isBamboo(material);
            }
            final boolean nearBoat = false;
            this.collidingEntity = nearBoat;
            this.nearShulker = nearBoat;
            this.nearBoat = nearBoat;
            for (final Entity entity : this.nearbyEntities) {
                this.nearBoat |= EntityUtil.isBoat(entity);
                this.nearShulker |= EntityUtil.isShulker(entity);
                if (entity instanceof LivingEntity) {
                    this.collidingEntity = true;
                }
                if (entity instanceof Firework) {
                    this.sinceFireworkTicks = 0;
                }
                if (entity instanceof EnderDragon || entity instanceof EnderDragonPart) {
                    this.data.getActionProcessor().setSinceDragonDamageTicks(0);
                }
            }
            if (this.nearbyEntities.size() > Config.ENTITY_CRAM_FIX_AMOUNT) {
                this.sinceFuckingEntityTicks = 0;
            }
            this.inBubbleColumn = PlayerUtil.isInBubbleColumn(this.data);
            this.onClimbable = PlayerUtil.isOnClimbable(this.data);
            this.inWeb = PlayerUtil.isInWeb(this.data);
            this.inLiquid = PlayerUtil.isInLiquid(this.data);
            this.fullySubmerged = PlayerUtil.isFullySubmerged(this.data);
        }
        if (this.nearBoat || this.nearShulker || this.nearTrapdoor || this.nearCarpet || this.nearCampfire || this.nearBrewingStand || this.nearRepeater || this.nearDaylightSensor || this.nearSkull || this.nearLilyPad || this.nearShulkerBox || this.nearFlowerPot) {
            this.serverAirTicks = 0;
        }
    }

    public void handleVehicleMove(final WrappedPacketInVehicleMove wrapper) {
        ++this.vehicleTicks;
        this.lastBukkitVehicle = this.bukkitVehicle;
        this.bukkitVehicle = this.data.getPlayer().getVehicle();
        if (this.lastBukkitVehicle != this.bukkitVehicle) {
            this.vehicleTicks = 0;
        }
        this.vehicleX = wrapper.getX();
        this.vehicleY = wrapper.getY();
        this.vehicleZ = wrapper.getZ();
        this.lastVehicleDeltaX = this.vehicleDeltaX;
        this.lastVehicleDeltaY = this.vehicleDeltaY;
        this.lastVehicleDeltaZ = this.vehicleDeltaZ;
        this.vehicleDeltaX = this.vehicleX - this.lastVehicleX;
        this.vehicleDeltaY = this.vehicleY - this.lastVehicleY;
        this.vehicleDeltaZ = this.vehicleZ - this.lastVehicleZ;
        this.vehicleDeltaXZ = MathUtil.hypot(this.vehicleDeltaX, this.vehicleDeltaZ);
        if (ServerUtil.isLowerThan1_13() && ServerUtil.isHigherThan1_9()) {
            this.vehicleBlocks = BlockUtil.getNearbyBlocksAsync(this.world, NumberConversions.floor(this.vehicleX), NumberConversions.floor(this.vehicleY), NumberConversions.floor(this.vehicleZ), 1);
        }
        if (this.vehicleBlocks != null) {
            final boolean b = false;
            this.vehicleNearBed = b;
            this.vehicleNearBubbleColumn = b;
            this.vehicleNearSlime = b;
            this.vehicleNearPiston = b;
            this.vehicleNearLiquid = b;
            this.vehicleNearIce = b;
            for (final Material material : this.vehicleBlocks) {
                this.vehicleNearIce |= BlockUtil.isIce(material);
                this.vehicleNearLiquid |= BlockUtil.isLiquid(material);
                this.vehicleNearPiston |= BlockUtil.isPiston(material);
                this.vehicleNearSlime |= BlockUtil.isSlime(material);
                this.vehicleNearBubbleColumn |= BlockUtil.isBubbleColumn(material);
                this.vehicleNearBed |= BlockUtil.isBed(material);
            }
            this.vehicleInAir = StreamUtil.allMatch(this.vehicleBlocks, BlockUtil::isAir);
        }
        this.boatsAround = 0;
        this.vehicleNearEntity = false;
        if (this.nearbyEntities != null) {
            for (final Entity entity : this.nearbyEntities) {
                if (entity instanceof Boat) {
                    ++this.boatsAround;
                }
                if (entity instanceof LivingEntity) {
                    this.vehicleNearEntity = true;
                }
            }
        }
        if (this.vehicleNearIce) {
            this.sinceVehicleNearIceTicks = 0;
        } else {
            ++this.sinceVehicleNearIceTicks;
        }
        if (this.vehicleNearPiston) {
            this.sinceVehicleNearPistonTicks = 0;
        } else {
            ++this.sinceVehicleNearPistonTicks;
        }
        if (this.vehicleNearBed) {
            this.sinceVehicleNearBedTicks = 0;
        } else {
            ++this.sinceVehicleNearBedTicks;
        }
        if (this.vehicleNearLiquid) {
            this.sinceVehicleNearLiquidTicks = 0;
        } else {
            ++this.sinceVehicleNearLiquidTicks;
        }
        if (this.vehicleNearSlime) {
            this.sinceVehicleNearSlimeTicks = 0;
        } else {
            ++this.sinceVehicleNearSlimeTicks;
        }
        if (this.vehicleNearBubbleColumn) {
            this.sinceVehicleNearBubbleColumnTicks = 0;
        } else {
            ++this.sinceVehicleNearBubbleColumnTicks;
        }
        if (this.vehicleInAir) {
            ++this.vehicleAirTicks;
        } else {
            this.vehicleAirTicks = 0;
        }
        this.lastVehicleX = this.vehicleX;
        this.lastVehicleY = this.vehicleY;
        this.lastVehicleZ = this.vehicleZ;
    }

    public void handleFlyingTicks() {
        ++this.sinceGroundSpeedFailTicks;
        ++this.ticksSinceGhostBlockSetback;
        ++this.sinceFishingRodTicks;
        ++this.sinceAttributeModifierTicks;
        ++this.sinceSetbackTicks;
        if (this.clientOnGround) {
            ++this.clientGroundTicks;
        } else {
            this.clientGroundTicks = 0;
        }
        if (!this.clientOnGround) {
            ++this.clientAirTicks;
        } else {
            this.clientAirTicks = 0;
        }
    }

    public void handlePositionTicks() {
        ++this.sinceFlagTicks;
        ++this.sinceFuckingEntityTicks;
        if (this.touchingAir) {
            ++this.serverAirTicks;
        } else {
            this.serverAirTicks = 0;
        }
        if (this.mathematicallyOnGround) {
            ++this.serverGroundTicks;
        } else {
            this.serverGroundTicks = 0;
        }
        if (this.flySpeed > 0.11f) {
            this.sinceHighFlySpeedTicks = 0;
        } else {
            ++this.sinceHighFlySpeedTicks;
        }
        if (this.nearSlime || (this.blockBelow != null && BlockUtil.isSlime(this.blockBelow)) || (this.blockBelow2 != null && BlockUtil.isSlime(this.blockBelow2)) || (this.blockBelow3 != null && BlockUtil.isSlime(this.blockBelow3)) || (this.blockBelow != null && BlockUtil.isSlime(this.blockBelow)) || (this.blockBelow2 != null && BlockUtil.isSlime(this.blockBelow2)) || (this.blockBelow3 != null && BlockUtil.isSlime(this.blockBelow3))) {
            this.sinceNearSlimeTicks = 0;
        } else {
            ++this.sinceNearSlimeTicks;
        }
        ++this.sinceWaterLogTicks;
        if (this.nearIce) {
            this.sinceNearIceTicks = 0;
        } else {
            ++this.sinceNearIceTicks;
        }
        if (this.data.getActionProcessor().isHasSpeed()) {
            this.sinceSpeedTicks = 0;
        } else {
            ++this.sinceSpeedTicks;
        }
        if (ServerUtil.isHigherThan1_8() && this.data.getPlayer().getGameMode() == GameMode.SPECTATOR) {
            this.sinceSpectatorTicks = 0;
        } else {
            ++this.sinceSpectatorTicks;
        }
        if (this.nearBed) {
            this.sinceNearBedTicks = 0;
        } else {
            ++this.sinceNearBedTicks;
        }
        if (this.nearFarmland) {
            this.sinceNearFarmlandTicks = 0;
        } else {
            ++this.sinceNearFarmlandTicks;
        }
        if (this.nearSlab) {
            this.sinceNearSlabTicks = 0;
        } else {
            ++this.sinceNearSlabTicks;
        }
        if (this.nearPiston) {
            this.sinceNearPistonTicks = 0;
        } else {
            ++this.sinceNearPistonTicks;
        }
        if (this.collidingEntity) {
            this.sinceEntityCollisionTicks = 0;
        } else {
            ++this.sinceEntityCollisionTicks;
        }
        if (this.data.getActionProcessor().isHasJumpBoost()) {
            this.sinceJumpBoostTicks = 0;
        } else {
            ++this.sinceJumpBoostTicks;
        }
        if (this.collidingVertically) {
            this.sinceCollidingVerticallyTicks = 0;
        } else {
            ++this.sinceCollidingVerticallyTicks;
        }
        if (this.collidingHorizontally) {
            this.sinceCollidingHorizontallyTicks = 0;
        } else {
            ++this.sinceCollidingHorizontallyTicks;
        }
        if (this.nearFrostedIce) {
            this.sinceNearFrostedIceTicks = 0;
        } else {
            ++this.sinceNearFrostedIceTicks;
        }
        if (this.nearFence) {
            this.sinceNearFenceTicks = 0;
        } else {
            ++this.sinceNearFenceTicks;
        }
        if (this.onIce) {
            this.sinceIceTicks = 0;
        } else {
            ++this.sinceIceTicks;
        }
        if (this.nearTrapdoor) {
            this.sinceTrapdoorTicks = 0;
        } else {
            ++this.sinceTrapdoorTicks;
        }
        if (this.nearWeb) {
            this.sinceWebTicks = 0;
        } else {
            ++this.sinceWebTicks;
        }
        if (this.nearClimbable) {
            this.sinceNearClimbableTicks = 0;
        } else {
            ++this.sinceNearClimbableTicks;
        }
        if (this.nearShulker || this.nearShulkerBox) {
            this.sinceNearShulkerBoxTicks = 0;
        } else {
            ++this.sinceNearShulkerBoxTicks;
        }
        if (this.nearScaffolding) {
            this.sinceNearScaffoldingTicks = 0;
        } else {
            ++this.sinceNearScaffoldingTicks;
        }
        if (this.inLiquid || this.nearLiquid) {
            this.sinceLiquidTicks = 0;
        } else {
            ++this.sinceLiquidTicks;
        }
        if (this.onClimbable) {
            ++this.climbableTicks;
        } else {
            this.climbableTicks = 0;
        }
        if (this.nearStair) {
            this.sinceNearStairTicks = 0;
        } else {
            ++this.sinceNearStairTicks;
        }
        if (this.blocksAround != null && StreamUtil.anyMatch(this.blocksAround, BlockUtil::isSlime)) {
            this.sinceAroundSlimeTicks = 0;
        } else {
            ++this.sinceAroundSlimeTicks;
        }
        if (this.blocksAround != null && StreamUtil.anyMatch(this.blocksAround, BlockUtil::isPiston)) {
            this.sinceAroundPistonTicks = 0;
        } else {
            ++this.sinceAroundPistonTicks;
        }
        if (this.blocksAround != null && StreamUtil.anyMatch(this.blocksAround, BlockUtil::isSlab)) {
            this.sinceAroundSlabTicks = 0;
        } else {
            ++this.sinceAroundSlabTicks;
        }
        if (this.data.getPlayer().isInsideVehicle() || this.data.getPlayer().getVehicle() != null) {
            this.sinceVehicleTicks = 0;
        } else {
            ++this.sinceVehicleTicks;
        }
        ++this.sinceFireworkTicks;
        if (this.data.getPlayer().getAllowFlight() || this.allowFlight) {
            this.sinceFlyingTicks = 0;
        } else {
            ++this.sinceFlyingTicks;
        }
        if (ServerUtil.isHigherThan1_9()) {
            if (this.data.getActionProcessor().isHasSlowFalling()) {
                this.sinceSlowFallingTicks = 0;
            } else {
                ++this.sinceSlowFallingTicks;
            }
            if (PlayerUtil.isSwimming(this.data.getPlayer()) || this.data.getActionProcessor().isPacketSwimming()) {
                this.sinceSwimmingTicks = 0;
            } else {
                ++this.sinceSwimmingTicks;
            }
            if (this.data.getActionProcessor().isBukkitGliding() || this.data.getActionProcessor().isPacketGliding()) {
                this.sinceGlidingTicks = 0;
            } else {
                ++this.sinceGlidingTicks;
            }
            if (PlayerUtil.isGliding(this.data.getPlayer())) {
                ++this.glidingTicks;
            } else {
                this.glidingTicks = 0;
            }
            if (ServerUtil.isHigherThan1_16() && this.nearSoulSand && this.data.getActionProcessor().getBoots() != null && this.data.getActionProcessor().getBoots().getEnchantmentLevel(Enchantment.SOUL_SPEED) > 0) {
                this.sinceSoulSpeedTicks = 0;
            } else {
                ++this.sinceSoulSpeedTicks;
            }
            if (this.data.getActionProcessor().isWearingElytra()) {
                this.sinceElytraTicks = 0;
            } else {
                ++this.sinceElytraTicks;
            }
            if (PlayerUtil.isRiptiding(this.data.getPlayer())) {
                this.sinceRiptideTicks = 0;
            } else {
                ++this.sinceRiptideTicks;
            }
            if (this.nearHoney) {
                this.sinceHoneyTicks = 0;
            } else {
                ++this.sinceHoneyTicks;
            }
            if (this.data.getActionProcessor().isHasLevitation()) {
                this.sinceLevitationTicks = 0;
            } else {
                ++this.sinceLevitationTicks;
            }
            if (PlayerUtil.hasDolphinsGrace(this.data.getPlayer())) {
                this.sinceDolphinsGraceTicks = 0;
            } else {
                ++this.sinceDolphinsGraceTicks;
            }
            if (this.inBubbleColumn || this.nearBubbleColumn) {
                this.sinceBubbleColumnTicks = 0;
            } else {
                ++this.sinceBubbleColumnTicks;
            }
        }
    }

    public void handleBlockDig(final WrappedPacketInBlockDig wrapper) {
        if (ServerUtil.isHigherThan1_9() && wrapper.getDigType() == WrappedPacketInBlockDig.PlayerDigType.RELEASE_USE_ITEM) {
            final boolean liquid = this.sinceLiquidTicks < 30 || this.sinceWaterLogTicks < 30;
            final boolean playerWeather = this.data.getPlayer().getPlayerWeather() == WeatherType.DOWNFALL;
            final boolean rain = this.data.getPlayer().getWorld().hasStorm();
            if ((liquid || rain || playerWeather) && PlayerUtil.isHoldingTridentWithRiptide(this.data.getPlayer())) {
                this.sinceRiptideTicks = 0;
            }
        }
    }

    public boolean isNearGround() {
        if (ServerUtil.isHigherThan1_13()) {
            if (this.blockBelow != null && this.blockBelow2 != null) {
                return !BlockUtil.isAir(this.blockBelow) || !BlockUtil.isAir(this.blockBelow2);
            }
        } else if (this.blockBelow != null && this.blockBelow2 != null) {
            return !BlockUtil.isAir(this.blockBelow) || !BlockUtil.isAir(this.blockBelow2);
        }
        return false;
    }

    public void handleArmAnimation() {
        if (ServerUtil.isHigherThan1_9()) {
            if (this.data.getPlayer().getInventory().getItemInMainHand().getType().toString().contains("FIREWORK")) {
                this.sinceFireworkTicks = 0;
            }
            if (this.data.getPlayer().getInventory().getItemInOffHand().getType().toString().contains("FIREWORK")) {
                this.sinceFireworkTicks = 0;
            }
        }
    }

    public boolean isOnLiquid() {
        return this.blocksBelow != null && StreamUtil.allMatch(this.blocksBelow, BlockUtil::isLiquid);
    }

    public boolean isAirBelow() {
        if (ServerUtil.isHigherThan1_13()) {
            return this.blocksBelow == null || this.blocksUnder == null || !StreamUtil.allMatch(this.blocksBelow, BlockUtil::isAir) || !StreamUtil.allMatch(this.blocksUnder, BlockUtil::isAir);
        }
        return this.blocksBelow == null || !StreamUtil.allMatch(this.blocksBelow, BlockUtil::isAir);
    }

    public double getAcceleration() {
        return Math.abs(this.deltaXZ - this.lastDeltaXZ);
    }

    public void handleFishingRod() {
        this.lastPulledByFishingRod = Anticheat.INSTANCE.getTickManager().getTicks();
        this.sinceFishingRodTicks = 0;
    }

    public void parseForwardAndStrafe() {
        final double angle = MathUtil.angleOf(this.lastZ, this.lastX, this.z, this.x);
        final float rawYaw = this.data.getRotationProcessor().getYaw() % 360.0f;
        final double yaw = (rawYaw >= 0.0f) ? rawYaw : ((double) (rawYaw + 360.0f));
        final double angleDeltaRaw = (double) (Math.round(MathUtil.getDistanceBetweenAngles360Raw(yaw, angle) / 5.0) * 5L);
        final double angleDelta = (double) (Math.round(MathUtil.getDistanceBetweenAngles360(yaw, angle) / 5.0) * 5L);
        float moveStrafing = 0.0f;
        float moveForward = 0.0f;
        if (angleDelta >= 0.0 && angleDelta < 90.0) {
            ++moveForward;
        } else if (angleDelta > 90.0 && angleDelta <= 180.0) {
            --moveForward;
        }
        if (angleDeltaRaw > 0.0 && angleDeltaRaw < 180.0) {
            --moveStrafing;
        } else if (angleDeltaRaw > 180.0 && angleDeltaRaw < 360.0) {
            ++moveStrafing;
        }
        this.moveForward = moveForward;
        this.moveStrafing = moveStrafing;
    }

    public void handleWaterlogged() {
        this.sinceWaterLogTicks = 0;
        this.lastWaterLogged = Anticheat.INSTANCE.getTickManager().getTicks();
    }

    private void handleGhostBlock() {
        if (!Config.GHOST_BLOCK_FIX || ServerUtil.getTPS() < Config.GHOST_BLOCK_MIN_TPS) {
            return;
        }
        final boolean exempt = this.data.getExemptProcessor().isExempt(ExemptType.LAGGED_NEAR_GROUND, ExemptType.BLOCK_PLACE_FAST, ExemptType.LAGGED_NEAR_GROUND_, ExemptType.FLIGHT, ExemptType.TELEPORT, ExemptType.SWIMMING, ExemptType.DEATH);
        final boolean ghostBlock = this.serverAirTicks > Config.GHOST_BLOCK_MIN_TICKS && this.clientGroundTicks >= 1 && this.serverGroundTicks >= 1;
        if (ghostBlock && !exempt) {
            final double ghostBlockBuffer = this.ghostBlockBuffer + 1.0;
            this.ghostBlockBuffer = ghostBlockBuffer;
            if (ghostBlockBuffer > Config.GHOST_BLOCK_MAX_BUFFER) {
                if (Config.ENABLE_API) {
                    final VulcanGhostBlockEvent event = new VulcanGhostBlockEvent(this.data.getPlayer());
                    Bukkit.getPluginManager().callEvent(event);
                    if (event.isCancelled()) {
                        return;
                    }
                }
                if (Config.GHOST_BLOCK_FIX_UPDATE_BLOCKS) {
                    Bukkit.getScheduler().runTask(Anticheat.INSTANCE.getPlugin(), () -> {
                        final WrappedPacketOutBlockChange wrapper = new WrappedPacketOutBlockChange(new Location(this.world, this.blockX, this.blockY - 1, this.blockZ), this.world.getBlockAt(this.blockX, this.blockY - 1, this.blockZ).getType());
                        final WrappedPacketOutBlockChange wrapper2 = new WrappedPacketOutBlockChange(new Location(this.world, this.blockX + 1, this.blockY - 1, this.blockZ), this.world.getBlockAt(this.blockX + 1, this.blockY - 1, this.blockZ).getType());
                        final WrappedPacketOutBlockChange wrapper3 = new WrappedPacketOutBlockChange(new Location(this.world, this.blockX - 1, this.blockY - 1, this.blockZ), this.world.getBlockAt(this.blockX - 1, this.blockY - 1, this.blockZ).getType());
                        final WrappedPacketOutBlockChange wrapper4 = new WrappedPacketOutBlockChange(new Location(this.world, this.blockX, this.blockY - 1, this.blockZ + 1), this.world.getBlockAt(this.blockX, this.blockY - 1, this.blockZ + 1).getType());
                        final WrappedPacketOutBlockChange wrapper5 = new WrappedPacketOutBlockChange(new Location(this.world, this.blockX, this.blockY - 1, this.blockZ - 1), this.world.getBlockAt(this.blockX, this.blockY - 1, this.blockZ - 1).getType());
                        final WrappedPacketOutBlockChange wrapper6 = new WrappedPacketOutBlockChange(new Location(this.world, this.blockX - 1, this.blockY - 1, this.blockZ - 1), this.world.getBlockAt(this.blockX - 1, this.blockY - 1, this.blockZ - 1).getType());
                        final WrappedPacketOutBlockChange wrapper7 = new WrappedPacketOutBlockChange(new Location(this.world, this.blockX - 1, this.blockY - 1, this.blockZ + 1), this.world.getBlockAt(this.blockX - 1, this.blockY - 1, this.blockZ + 1).getType());
                        final WrappedPacketOutBlockChange wrapper8 = new WrappedPacketOutBlockChange(new Location(this.world, this.blockX + 1, this.blockY - 1, this.blockZ + 1), this.world.getBlockAt(this.blockX - 1, this.blockY - 1, this.blockZ + 1).getType());
                        final WrappedPacketOutBlockChange wrapper9 = new WrappedPacketOutBlockChange(new Location(this.world, this.blockX + 1, this.blockY - 1, this.blockZ - 1), this.world.getBlockAt(this.blockX + 1, this.blockY - 1, this.blockZ - 1).getType());
                        PlayerUtil.sendPacket(this.data.getPlayer(), wrapper);
                        PlayerUtil.sendPacket(this.data.getPlayer(), wrapper2);
                        PlayerUtil.sendPacket(this.data.getPlayer(), wrapper3);
                        PlayerUtil.sendPacket(this.data.getPlayer(), wrapper4);
                        PlayerUtil.sendPacket(this.data.getPlayer(), wrapper5);
                        PlayerUtil.sendPacket(this.data.getPlayer(), wrapper6);
                        PlayerUtil.sendPacket(this.data.getPlayer(), wrapper7);
                        PlayerUtil.sendPacket(this.data.getPlayer(), wrapper8);
                        PlayerUtil.sendPacket(this.data.getPlayer(), wrapper9);
                    });
                }
                if (Config.GHOST_BLOCK_SETBACK) {
                    final float yaw = this.data.getRotationProcessor().getYaw();
                    final float pitch = this.data.getRotationProcessor().getPitch();
                    final Location setbackLocation = new Location(this.world, this.setbackX, this.setbackY, this.setbackZ, yaw, pitch);
                    Bukkit.getScheduler().runTask(Anticheat.INSTANCE.getPlugin(), () -> this.data.getPlayer().teleport(setbackLocation, PlayerTeleportEvent.TeleportCause.UNKNOWN));
                    this.ticksSinceGhostBlockSetback = 0;
                    this.lastGhostBlockSetback = Anticheat.INSTANCE.getTickManager().getTicks();
                }
                if (Config.GHOST_BLOCK_MESSAGE_ENABLED) {
                    this.data.getPlayer().sendMessage(ColorUtil.translate(Config.GHOST_BLOCK_MESSAGE));
                }
                if (Config.GHOST_BLOCK_STAFF_MESSAGE_ENABLED) {
                    Anticheat.INSTANCE.getAlertManager().sendMessage(Config.GHOST_BLOCK_STAFF_MESSAGE.replaceAll("%x%", Integer.toString(this.blockX)).replaceAll("%y%", Integer.toString(this.blockY)).replaceAll("%z%", Integer.toString(this.blockZ)).replaceAll("%ticks%", Integer.toString(this.serverAirTicks)).replaceAll("%world%", this.data.getPlayer().getWorld().getName()).replaceAll("%player%", this.data.getPlayer().getName()));
                }
                if (Config.GHOST_BLOCK_PRINT_TO_CONSOLE) {
                    Anticheat.INSTANCE.getPlugin().getLogger().log(Level.INFO, ColorUtil.translate(Config.GHOST_BLOCK_CONSOLE_MESSAGE.replaceAll("%ticks%", Integer.toString(this.serverAirTicks)).replaceAll("%player%", this.data.getPlayer().getName()).replaceAll("%world%", this.data.getPlayer().getWorld().getName()).replaceAll("%z%", Integer.toString(this.blockZ)).replaceAll("%y%", Integer.toString(this.blockY)).replaceAll("%x%", Integer.toString(this.blockX))));
                }
                this.serverAirTicks = 0;
                this.ghostBlockBuffer = 0.0;
            }
        } else if (this.ghostBlockBuffer > 0.0) {
            this.ghostBlockBuffer -= Config.GHOST_BLOCK_BUFFER_DECAY;
        }
    }

    public void handleTransaction(final WrappedPacketInTransaction wrapper) {
        if (this.queuedAbilities.containsKey(wrapper.getActionNumber())) {
            this.sinceFlyingTicks = 0;
            final WrappedPacketOutAbilities confirmation = this.queuedAbilities.get(wrapper.getActionNumber());
            if (confirmation == null) {
                return;
            }
            this.lastServerAbilities = Anticheat.INSTANCE.getTickManager().getTicks();
            this.allowFlight = confirmation.isFlightAllowed();
            this.walkSpeed = confirmation.getWalkSpeed() * 2.0f;
            this.flySpeed = confirmation.getFlySpeed() * 2.0f;
            this.queuedAbilities.remove(wrapper.getActionNumber());
        }
    }

    public void handlePong(final WrappedPacketInPong wrapper) {
        final short id = (short) wrapper.getId();
        if (this.queuedAbilities.containsKey(id)) {
            this.sinceFlyingTicks = 0;
            final WrappedPacketOutAbilities confirmation = this.queuedAbilities.get(id);
            if (confirmation == null) {
                return;
            }
            this.lastServerAbilities = Anticheat.INSTANCE.getTickManager().getTicks();
            this.allowFlight = confirmation.isFlightAllowed();
            this.walkSpeed = confirmation.getWalkSpeed() * 2.0f;
            this.flySpeed = confirmation.getFlySpeed() * 2.0f;
            this.queuedAbilities.remove(id);
        }
    }

    public void handleAbilities(final WrappedPacketOutAbilities wrapper) {
        ++this.abilitiesTransactionId;
        ++this.abilitiesPingId;
        if (this.abilitiesTransactionId > -29769) {
            this.abilitiesTransactionId = -30768;
        }
        if (this.abilitiesPingId > -29769) {
            this.abilitiesPingId = -30768;
        }
        if (ServerUtil.isHigherThan1_17()) {
            this.data.sendPing(this.abilitiesPingId);
        } else {
            this.data.sendTransaction(this.abilitiesTransactionId);
        }
        this.queuedAbilities.put(this.abilitiesTransactionId, wrapper);
    }

    public boolean isOnSoulSand() {
        if (ServerUtil.isHigherThan1_13()) {
            return this.nearbyBlocks != null && !this.nearbyBlocks.isEmpty() && BlockUtil.isSoulSandOnly(this.nearbyBlocks.get(16));
        }
        return this.nearbyBlocks != null && !this.nearbyBlocks.isEmpty() && BlockUtil.isSoulSandOnly(this.nearbyBlocks.get(16));
    }

    public boolean isOnHoney() {
        if (ServerUtil.isHigherThan1_13()) {
            return this.nearbyBlocks != null && !this.nearbyBlocks.isEmpty() && BlockUtil.isHoney(this.nearbyBlocks.get(16));
        }
        return this.nearbyBlocks != null && !this.nearbyBlocks.isEmpty() && BlockUtil.isHoney(this.nearbyBlocks.get(16));
    }

    public void setback() {
        if (this.y < this.setbackY && Config.SETBACK_LOWER_POSITION) {
            return;
        }
        this.sinceSetbackTicks = 0;
        Bukkit.getScheduler().runTask(Anticheat.INSTANCE.getPlugin(), () -> this.data.getPlayer().teleport(new Location(this.world, this.setbackX, this.setbackY, this.setbackZ, this.data.getRotationProcessor().getYaw(), this.data.getRotationProcessor().getPitch()), PlayerTeleportEvent.TeleportCause.UNKNOWN));
    }

    public void handleUnloadedChunk() {
        if (!Config.UNLOADED_CHUNK_SETBACK) {
            return;
        }
        final boolean exempt = this.data.getExemptProcessor().isExempt(ExemptType.JOINED, ExemptType.TELEPORT, ExemptType.WORLD_CHANGE, ExemptType.CHUNK, ExemptType.WORLD_CHANGE, ExemptType.FLIGHT, ExemptType.ELYTRA, ExemptType.GLIDING, ExemptType.SLOW_FALLING) || this.data.getActionProcessor().getPositionTicksExisted() < 20 || this.data.getActionProcessor().isTeleporting();
        final double diff = Math.abs(Math.abs(this.deltaY) - 0.09800000190734881);
        if (this.deltaY < -0.09000000357627869 && !this.clientOnGround && !exempt && diff < 0.001) {
            final double unloadedChunkBuffer = this.unloadedChunkBuffer + 1.0;
            this.unloadedChunkBuffer = unloadedChunkBuffer;
            if (unloadedChunkBuffer > Config.MAX_UNLOADED_CHUNK_TICKS) {
                final float yaw = this.data.getRotationProcessor().getYaw();
                final float pitch = this.data.getRotationProcessor().getPitch();
                final Location setbackLocation = new Location(this.world, this.setbackX, this.setbackY, this.setbackZ, yaw, pitch);
                Bukkit.getScheduler().runTask(Anticheat.INSTANCE.getPlugin(), () -> this.data.getPlayer().teleport(setbackLocation, PlayerTeleportEvent.TeleportCause.UNKNOWN));
                if (!Config.UNLOADED_CHUNK_SETBACK_MESSAGE.equals("")) {
                    Anticheat.INSTANCE.getAlertManager().sendMessage(Config.UNLOADED_CHUNK_SETBACK_MESSAGE.replaceAll("%x%", Integer.toString(this.blockX)).replaceAll("%y%", Integer.toString(this.blockY)).replaceAll("%z%", Integer.toString(this.blockZ)).replaceAll("%ticks%", Integer.toString(this.serverAirTicks)).replaceAll("%world%", this.data.getPlayer().getWorld().getName()).replaceAll("%player%", this.data.getPlayer().getName()));
                }
                this.unloadedChunkBuffer = 0.0;
            } else if (this.unloadedChunkBuffer > 0.0) {
                this.unloadedChunkBuffer -= 0.025;
            }
        }
    }
}
