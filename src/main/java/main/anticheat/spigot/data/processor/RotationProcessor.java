package main.anticheat.spigot.data.processor;

import lombok.Getter;
import main.anticheat.spigot.Anticheat;
import main.anticheat.spigot.data.PlayerData;
import main.anticheat.spigot.util.MathUtil;
import main.anticheat.spigot.util.value.Values;
import org.bukkit.util.Vector;

import java.util.ArrayDeque;

@Getter
public class RotationProcessor {
    private final PlayerData data;
    private final ArrayDeque<Integer> sensitivitySamples;
    private float yaw;
    private float pitch;
    private float lastYaw;
    private float lastPitch;
    private float deltaYaw;
    private float deltaPitch;
    private float lastDeltaYaw;
    private float lastDeltaPitch;
    private float yawAccel;
    private float pitchAccel;
    private float lastYawAccel;
    private float lastPitchAccel;
    private float rawMouseDeltaX;
    private float rawMouseDeltaY;
    private float fuckedPredictedPitch;
    private float fuckedPredictedYaw;
    private float lastFuckedPredictedPitch;
    private float lastFuckedPredictedYaw;
    private boolean invalidRate;
    private boolean invalidSensitivity;
    private boolean cinematic;
    private double finalSensitivity;
    private double mcpSensitivity;
    private int sensitivity;
    private int lastRate;
    private int lastInvalidSensitivity;
    private int lastCinematic;
    private int mouseDeltaX;
    private int mouseDeltaY;

    public RotationProcessor(final PlayerData data) {
        this.sensitivitySamples = new ArrayDeque<Integer>();
        this.data = data;
    }

    public void handle(final float yaw, final float pitch) {
        this.lastYaw = this.yaw;
        this.lastPitch = this.pitch;
        this.yaw = yaw;
        this.pitch = pitch;
        this.lastDeltaYaw = this.deltaYaw;
        this.lastDeltaPitch = this.deltaPitch;
        this.deltaYaw = Math.abs(yaw - this.lastYaw);
        this.deltaPitch = Math.abs(pitch - this.lastPitch);
        this.lastPitchAccel = this.pitchAccel;
        this.lastYawAccel = this.yawAccel;
        this.yawAccel = Math.abs(this.deltaYaw - this.lastDeltaYaw);
        this.pitchAccel = Math.abs(this.deltaPitch - this.lastDeltaPitch);
        final float f = (float) this.mcpSensitivity * 0.6f + 0.2f;
        final float gcd = f * f * f * 1.2f;
        this.rawMouseDeltaX = this.deltaYaw / gcd;
        this.rawMouseDeltaY = this.deltaPitch / gcd;
        this.mouseDeltaX = (int) (this.deltaYaw / gcd);
        this.mouseDeltaY = (int) (this.deltaPitch / gcd);
        this.processCinematic();
        final float expectedYaw = this.deltaYaw * 1.073742f + (float) (this.deltaYaw + 0.15);
        final float expectedPitch = this.deltaPitch * 1.073742f - (float) (this.deltaPitch - 0.15);
        final float pitchDiff = Math.abs(this.deltaPitch - expectedPitch);
        final float yawDiff = Math.abs(this.deltaYaw - expectedYaw);
        this.lastFuckedPredictedPitch = this.fuckedPredictedPitch;
        this.lastFuckedPredictedYaw = this.fuckedPredictedYaw;
        this.fuckedPredictedPitch = Math.abs(this.deltaPitch - pitchDiff);
        this.fuckedPredictedYaw = Math.abs(this.deltaYaw - yawDiff);
        if (this.deltaPitch > 0.1 && this.deltaPitch < 25.0f) {
            this.processSensitivity();
        }
    }

    public boolean hasValidSensitivity() {
        return this.sensitivity > 0 && this.sensitivity < 200;
    }

    private void processCinematic() {
        final int now = Anticheat.INSTANCE.getTickManager().getTicks();
        final float differenceYaw = Math.abs(this.deltaYaw - this.lastDeltaYaw);
        final float differencePitch = Math.abs(this.deltaPitch - this.lastDeltaPitch);
        final float joltYaw = Math.abs(differenceYaw - this.deltaYaw);
        final float joltPitch = Math.abs(differencePitch - this.deltaPitch);
        if (joltYaw > 1.0 && joltPitch > 1.0) {
            this.lastRate = now;
        }
        if (this.deltaPitch < 20.0f && this.finalSensitivity < 0.0) {
            this.lastInvalidSensitivity = now;
        }
        if (this.invalidRate && this.invalidSensitivity) {
            this.lastCinematic = now;
        }
        this.invalidRate = (now - this.lastRate > 3);
        this.invalidSensitivity = (now - this.lastInvalidSensitivity < 3);
        this.cinematic = (now - this.lastCinematic < 8);
    }

    private void processSensitivity() {
        final float gcd = (float) MathUtil.getGcd(this.deltaPitch, this.lastDeltaPitch);
        final double sensitivityModifier = Math.cbrt(0.8333 * gcd);
        final double sensitivityStepTwo = 1.666 * sensitivityModifier - 0.3333;
        final double finalSensitivity = sensitivityStepTwo * 200.0;
        this.finalSensitivity = finalSensitivity;
        this.sensitivitySamples.add((int) finalSensitivity);
        if (this.sensitivitySamples.size() == 40) {
            this.sensitivity = MathUtil.getMode(this.sensitivitySamples);
            if (this.hasValidSensitivity()) {
                this.mcpSensitivity = Values.SENSITIVITY_MCP_VALUES.get(this.sensitivity);
            }
            this.sensitivitySamples.clear();
        }
    }

    public boolean hasTooLowSensitivity() {
        return this.sensitivity >= 0 && this.sensitivity < 50;
    }

    public float getMovementAngle() {
        final double deltaX = this.data.getPositionProcessor().getDeltaX();
        final double deltaZ = this.data.getPositionProcessor().getDeltaZ();
        final Vector movement = new Vector(deltaX, 0.0, deltaZ);
        final Vector direction = new Vector(-Math.sin(this.yaw * 3.141592653589793f / 180.0f) * 1.0 * 0.5, 0.0, Math.cos(this.yaw * 3.1415927f / 180.0f) * 1.0 * 0.5);
        return movement.angle(direction);
    }

}
