package main.anticheat.spigot.check.data;

import lombok.Getter;
import lombok.Setter;
import main.anticheat.api.check.ICheckData;

import java.util.List;

@Getter
@Setter
public class CheckData implements ICheckData {
    private boolean enabled;
    private boolean punishable;
    private boolean broadcastPunishment;
    private boolean hotbarShuffle;
    private boolean randomRotation;
    private int maxViolations;
    private int alertInterval;
    private int minimumVlToNotify;
    private int maximumPing;
    private int randomRotationMinimumVl;
    private int hotbarShuffleMinimumVl;
    private int randomRotationInterval;
    private int hotbarShuffleInterval;
    private double minimumTps;
    private double maxBuffer;
    private double bufferDecay;
    private double bufferMultiple;
    private List<String> punishmentCommands;
    private String checkName;

    public CheckData(final String checkName) {
        this.checkName = checkName;
    }

    @Override
    public String toString() {
        return "{checkName=" + this.checkName + " enabled=" + this.enabled + " punishable=" + this.punishable + " broadcastPunishment=" + this.broadcastPunishment + "}";
    }

    @Override
    public boolean isEnabled() {
        return this.enabled;
    }

    @Override
    public boolean isPunishable() {
        return this.punishable;
    }

    @Override
    public boolean isBroadcastPunishment() {
        return this.broadcastPunishment;
    }

    @Override
    public boolean isHotbarShuffle() {
        return this.hotbarShuffle;
    }

    @Override
    public boolean isRandomRotation() {
        return this.randomRotation;
    }

    @Override
    public int getMaxViolations() {
        return this.maxViolations;
    }

    @Override
    public int getAlertInterval() {
        return this.alertInterval;
    }

    @Override
    public int getMinimumVlToNotify() {
        return this.minimumVlToNotify;
    }

    @Override
    public int getMaxPing() {
        return this.maximumPing;
    }

    @Override
    public int getMinimumVlToRandomlyRotate() {
        return this.randomRotationMinimumVl;
    }

    @Override
    public int getMinimumVlToShuffleHotbar() {
        return this.hotbarShuffleMinimumVl;
    }

    @Override
    public double getMinimumTps() {
        return this.minimumTps;
    }

    @Override
    public double getMaxBuffer() {
        return this.maxBuffer;
    }

    @Override
    public double getBufferDecay() {
        return this.bufferDecay;
    }

    @Override
    public double getBufferMultiple() {
        return this.bufferMultiple;
    }

    @Override
    public List<String> getPunishmentCommands() {
        return this.punishmentCommands;
    }

    @Override
    public int getRandomRotationInterval() {
        return this.randomRotationInterval;
    }
}
