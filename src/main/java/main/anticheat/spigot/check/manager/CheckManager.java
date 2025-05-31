package main.anticheat.spigot.check.manager;

import main.anticheat.spigot.check.AbstractCheck;
import main.anticheat.spigot.check.api.CheckInfo;
import main.anticheat.spigot.check.impl.combat.aim.*;
import main.anticheat.spigot.check.impl.combat.autoblock.AutoBlockA;
import main.anticheat.spigot.check.impl.combat.autoblock.AutoBlockB;
import main.anticheat.spigot.check.impl.combat.autoblock.AutoBlockC;
import main.anticheat.spigot.check.impl.combat.autoblock.AutoBlockD;
import main.anticheat.spigot.check.impl.combat.autoclicker.*;
import main.anticheat.spigot.check.impl.combat.criticals.CriticalsA;
import main.anticheat.spigot.check.impl.combat.criticals.CriticalsB;
import main.anticheat.spigot.check.impl.combat.fastbow.FastBowA;
import main.anticheat.spigot.check.impl.combat.hitbox.HitboxA;
import main.anticheat.spigot.check.impl.combat.hitbox.HitboxB;
import main.anticheat.spigot.check.impl.combat.killaura.*;
import main.anticheat.spigot.check.impl.combat.reach.ReachA;
import main.anticheat.spigot.check.impl.combat.reach.ReachB;
import main.anticheat.spigot.check.impl.combat.velocity.VelocityA;
import main.anticheat.spigot.check.impl.combat.velocity.VelocityB;
import main.anticheat.spigot.check.impl.combat.velocity.VelocityC;
import main.anticheat.spigot.check.impl.combat.velocity.VelocityD;
import main.anticheat.spigot.check.impl.movement.antilevitation.AntiLevitationA;
import main.anticheat.spigot.check.impl.movement.boatfly.BoatFlyA;
import main.anticheat.spigot.check.impl.movement.boatfly.BoatFlyB;
import main.anticheat.spigot.check.impl.movement.boatfly.BoatFlyC;
import main.anticheat.spigot.check.impl.movement.elytra.*;
import main.anticheat.spigot.check.impl.movement.entityflight.EntityFlightA;
import main.anticheat.spigot.check.impl.movement.entityflight.EntityFlightB;
import main.anticheat.spigot.check.impl.movement.entityspeed.EntitySpeedA;
import main.anticheat.spigot.check.impl.movement.fastclimb.FastClimbA;
import main.anticheat.spigot.check.impl.movement.flight.*;
import main.anticheat.spigot.check.impl.movement.jesus.*;
import main.anticheat.spigot.check.impl.movement.jump.JumpA;
import main.anticheat.spigot.check.impl.movement.jump.JumpB;
import main.anticheat.spigot.check.impl.movement.motion.*;
import main.anticheat.spigot.check.impl.movement.nosaddle.NoSaddleA;
import main.anticheat.spigot.check.impl.movement.noslow.NoSlowA;
import main.anticheat.spigot.check.impl.movement.noslow.NoSlowB;
import main.anticheat.spigot.check.impl.movement.noslow.NoSlowC;
import main.anticheat.spigot.check.impl.movement.speed.SpeedA;
import main.anticheat.spigot.check.impl.movement.speed.SpeedB;
import main.anticheat.spigot.check.impl.movement.speed.SpeedC;
import main.anticheat.spigot.check.impl.movement.speed.SpeedD;
import main.anticheat.spigot.check.impl.movement.sprint.SprintA;
import main.anticheat.spigot.check.impl.movement.step.StepA;
import main.anticheat.spigot.check.impl.movement.step.StepB;
import main.anticheat.spigot.check.impl.movement.step.StepC;
import main.anticheat.spigot.check.impl.movement.strafe.StrafeA;
import main.anticheat.spigot.check.impl.movement.strafe.StrafeB;
import main.anticheat.spigot.check.impl.movement.vclip.VClipA;
import main.anticheat.spigot.check.impl.movement.wallclimb.WallClimbA;
import main.anticheat.spigot.check.impl.player.airplace.AirPlaceA;
import main.anticheat.spigot.check.impl.player.badpackets.*;
import main.anticheat.spigot.check.impl.player.baritone.BaritoneA;
import main.anticheat.spigot.check.impl.player.baritone.BaritoneB;
import main.anticheat.spigot.check.impl.player.fastbreak.FastBreakA;
import main.anticheat.spigot.check.impl.player.fastplace.FastPlaceA;
import main.anticheat.spigot.check.impl.player.fastplace.FastPlaceB;
import main.anticheat.spigot.check.impl.player.fastuse.FastUseA;
import main.anticheat.spigot.check.impl.player.ghosthand.GhostHandA;
import main.anticheat.spigot.check.impl.player.groundspoof.GroundSpoofA;
import main.anticheat.spigot.check.impl.player.groundspoof.GroundSpoofB;
import main.anticheat.spigot.check.impl.player.groundspoof.GroundSpoofC;
import main.anticheat.spigot.check.impl.player.improbable.*;
import main.anticheat.spigot.check.impl.player.invalid.*;
import main.anticheat.spigot.check.impl.player.inventory.InventoryA;
import main.anticheat.spigot.check.impl.player.inventory.InventoryB;
import main.anticheat.spigot.check.impl.player.scaffold.*;
import main.anticheat.spigot.check.impl.player.timer.TimerA;
import main.anticheat.spigot.check.impl.player.timer.TimerD;
import main.anticheat.spigot.check.impl.player.tower.TowerA;
import main.anticheat.spigot.config.Config;
import main.anticheat.spigot.data.PlayerData;

import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.List;

public class CheckManager {
    public static final List<Constructor<?>> CONSTRUCTORS;
    public static final Class<? extends AbstractCheck>[] CHECKS;

    static {
        CONSTRUCTORS = new ArrayList<>();
        CHECKS = new Class[]{AimA.class, AimB.class, AimC.class, AimD.class, AimE.class, AimF.class, AimG.class, AimH.class, AimI.class, AimK.class, AimL.class, AimN.class, AimO.class, AimP.class, AimQ.class, AimR.class, AimS.class, AimU.class, AimW.class, AimX.class, AimY.class, AimZ.class, AutoBlockA.class, AutoBlockB.class, AutoBlockC.class, AutoBlockD.class, AutoClickerA.class, AutoClickerB.class, AutoClickerC.class, AutoClickerD.class, AutoClickerE.class, AutoClickerF.class, AutoClickerG.class, AutoClickerH.class, AutoClickerI.class, AutoClickerJ.class, AutoClickerK.class, AutoClickerL.class, AutoClickerM.class, AutoClickerN.class, AutoClickerO.class, AutoClickerP.class, AutoClickerQ.class, AutoClickerR.class, AutoClickerS.class, AutoClickerT.class, FastBowA.class, HitboxA.class, HitboxB.class, KillAuraA.class, KillAuraB.class, KillAuraC.class, KillAuraD.class, KillAuraF.class, KillAuraH.class, KillAuraJ.class, KillAuraK.class, KillAuraL.class, ReachA.class, ReachB.class, VelocityA.class, VelocityB.class, VelocityC.class, VelocityD.class, BoatFlyA.class, BoatFlyB.class, BoatFlyC.class, AntiLevitationA.class, NoSaddleA.class, EntitySpeedA.class, EntityFlightA.class, EntityFlightB.class, GhostHandA.class, ElytraA.class, ElytraB.class, ElytraC.class, ElytraF.class, ElytraG.class, ElytraI.class, ElytraK.class, ElytraL.class, ElytraM.class, CriticalsA.class, CriticalsB.class, FastClimbA.class, FlightA.class, FlightB.class, FlightC.class, FlightD.class, FlightE.class, FlightF.class, JesusA.class, JesusB.class, JesusC.class, JesusD.class, JesusE.class, JumpA.class, JumpB.class, MotionA.class, MotionB.class, MotionC.class, MotionD.class, MotionE.class, MotionG.class, MotionH.class, NoSlowA.class, NoSlowB.class, NoSlowC.class, SpeedA.class, SpeedB.class, SpeedC.class, SpeedD.class, StepA.class, StepB.class, StepC.class, SprintA.class, StrafeA.class, StrafeB.class, WallClimbA.class, BaritoneA.class, BaritoneB.class, BadPackets1.class, BadPackets2.class, BadPackets4.class, BadPackets5.class, BadPacketsA.class, BadPacketsB.class, BadPacketsC.class, BadPacketsD.class, BadPacketsE.class, BadPacketsF.class, BadPacketsG.class, BadPacketsH.class, BadPacketsI.class, BadPacketsJ.class, BadPacketsK.class, BadPacketsM.class, BadPacketsN.class, BadPacketsO.class, BadPacketsP.class, BadPacketsQ.class, BadPacketsR.class, BadPacketsT.class, BadPacketsV.class, BadPacketsW.class, BadPacketsX.class, BadPacketsY.class, BadPacketsZ.class, FastPlaceA.class, FastPlaceB.class, FastBreakA.class, FastUseA.class, GroundSpoofA.class, GroundSpoofB.class, GroundSpoofC.class, ImprobableA.class, ImprobableB.class, ImprobableC.class, ImprobableD.class, ImprobableE.class, ImprobableF.class, InvalidA.class, InvalidC.class, InvalidD.class, InvalidE.class, InvalidF.class, InvalidG.class, InvalidI.class, InvalidJ.class, VClipA.class, AirPlaceA.class, InventoryA.class, InventoryB.class, ScaffoldA.class, ScaffoldB.class, ScaffoldC.class, ScaffoldD.class, ScaffoldE.class, ScaffoldF.class, ScaffoldG.class, ScaffoldH.class, ScaffoldI.class, ScaffoldJ.class, ScaffoldK.class, ScaffoldM.class, ScaffoldN.class, TimerA.class, TimerD.class, TowerA.class};
    }

    public static List<AbstractCheck> loadChecks(final PlayerData data) {
        final List<AbstractCheck> checkList = new ArrayList<AbstractCheck>();
        for (final Constructor<?> constructor : CheckManager.CONSTRUCTORS) {
            try {
                checkList.add((AbstractCheck) constructor.newInstance(data));
            } catch (final Exception ignored) {
            }
        }
        return checkList;
    }

    public static void setup() {
        for (final Class<? extends AbstractCheck> clazz : CheckManager.CHECKS) {
            if (clazz.isAnnotationPresent(CheckInfo.class)) {
                final CheckInfo checkInfo = clazz.getAnnotation(CheckInfo.class);
                final String name = (checkInfo.name() + checkInfo.type()).replace(" ", "");
                if (Config.ENABLED_CHECKS.get(name)) {
                    try {
                        CheckManager.CONSTRUCTORS.add(clazz.getConstructor(PlayerData.class));
                    } catch (final Exception ignored) {}
                }
            }
        }
    }
}
