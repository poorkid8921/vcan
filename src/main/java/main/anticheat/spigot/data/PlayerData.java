package main.anticheat.spigot.data;

import io.github.retrooper.packetevents.packetwrappers.play.in.blockdig.WrappedPacketInBlockDig;
import io.github.retrooper.packetevents.packetwrappers.play.in.entityaction.WrappedPacketInEntityAction;
import io.github.retrooper.packetevents.packetwrappers.play.in.flying.WrappedPacketInFlying;
import io.github.retrooper.packetevents.packetwrappers.play.in.helditemslot.WrappedPacketInHeldItemSlot;
import io.github.retrooper.packetevents.packetwrappers.play.in.useentity.WrappedPacketInUseEntity;
import io.github.retrooper.packetevents.utils.player.ClientVersion;
import lombok.Getter;
import lombok.Setter;
import main.anticheat.api.IPlayerData;
import main.anticheat.spigot.Anticheat;
import main.anticheat.spigot.check.AbstractCheck;
import main.anticheat.spigot.check.manager.CheckManager;
import main.anticheat.spigot.data.processor.*;
import main.anticheat.spigot.exempt.ExemptProcessor;
import main.anticheat.spigot.exempt.type.ExemptType;
import main.anticheat.spigot.util.PlayerUtil;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class PlayerData implements IPlayerData {
    private final Player player;
    private final long joinTime;
    private final int joinTicks;
    private final List<AbstractCheck> checks;
    private final ExemptProcessor exemptProcessor;
    private final CombatProcessor combatProcessor;
    private final ActionProcessor actionProcessor;
    private final ClickProcessor clickProcessor;
    private final PositionProcessor positionProcessor;
    private final RotationProcessor rotationProcessor;
    private final VelocityProcessor velocityProcessor;
    private final ConnectionProcessor connectionProcessor;
    private ClientVersion clientVersion;
    private String clientBrand;
    private boolean hasSentClientBrand;
    private int totalViolations;
    private int combatViolations;
    private int movementViolations;
    private int playerViolations;
    private int autoClickerViolations;
    private int scaffoldViolations;
    private int timerViolations;
    private long lastClientBrandAlert;
    private long lastPunishment;
    private WrappedPacketInFlying flyingWrapper;
    private WrappedPacketInUseEntity useEntityWrapper;
    private WrappedPacketInBlockDig blockDigWrapper;
    private WrappedPacketInEntityAction entityActionWrapper;
    private WrappedPacketInHeldItemSlot heldItemSlotWrapper;
    private int pendingTransactions;
    private boolean setback;

    public PlayerData(final Player player) {
        this.setback = false;
        this.clientVersion = ClientVersion.UNRESOLVED;
        this.clientBrand = "Unresolved";
        this.hasSentClientBrand = false;
        this.joinTime = System.currentTimeMillis();
        this.joinTicks = Anticheat.INSTANCE.getTickManager().getTicks();
        this.pendingTransactions = 0;
        this.checks = CheckManager.loadChecks(this);
        this.exemptProcessor = new ExemptProcessor(this);
        this.combatProcessor = new CombatProcessor(this);
        this.actionProcessor = new ActionProcessor(this);
        this.clickProcessor = new ClickProcessor(this);
        this.positionProcessor = new PositionProcessor(this);
        this.rotationProcessor = new RotationProcessor(this);
        this.velocityProcessor = new VelocityProcessor(this);
        this.connectionProcessor = new ConnectionProcessor(this);
        this.player = player;
    }

    public void sendTransaction(final short id) {
        PlayerUtil.sendTransaction(this.player, id);
    }

    public void sendPing(final int id) {
        PlayerUtil.sendPing(this.player, id);
    }

    public boolean is1_17() {
        return PlayerUtil.is1_17(this);
    }

    public boolean is1_13() {
        return PlayerUtil.is1_13(this);
    }

    public boolean is1_9() {
        return PlayerUtil.is1_9(this);
    }

    public List<String> getExemptions() {
        final List<String> exempts = new ArrayList<String>();
        for (final ExemptType exemptType : ExemptType.values()) {
            if (this.exemptProcessor.isExempt(exemptType)) {
                exempts.add(exemptType.toString());
            }
        }
        return exempts;
    }

    @Override
    public String getClientBrand() {
        return this.clientBrand;
    }

    @Override
    public int getTotalViolations() {
        return this.totalViolations;
    }

    @Override
    public int getCombatViolations() {
        return this.combatViolations;
    }

    @Override
    public int getMovementViolations() {
        return this.movementViolations;
    }

    @Override
    public int getPlayerViolations() {
        return this.playerViolations;
    }

    @Override
    public int getAutoClickerViolations() {
        return this.autoClickerViolations;
    }

    @Override
    public int getScaffoldViolations() {
        return this.scaffoldViolations;
    }

    @Override
    public int getTimerViolations() {
        return this.timerViolations;
    }

    @Override
    public long getJoinTime() {
        return this.joinTime;
    }

    @Override
    public int getJoinTicks() {
        return this.joinTicks;
    }

    @Override
    public long getLastClientBrandAlert() {
        return this.lastClientBrandAlert;
    }
}
