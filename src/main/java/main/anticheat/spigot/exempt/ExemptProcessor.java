package main.anticheat.spigot.exempt;

import main.anticheat.spigot.data.PlayerData;
import main.anticheat.spigot.exempt.type.ExemptType;

import java.util.function.Function;

public class ExemptProcessor {
    private final PlayerData data;

    public ExemptProcessor(final PlayerData data) {
        this.data = data;
    }

    public boolean isExempt(final ExemptType exemptType) {
        return exemptType.getException().apply(this.data);
    }

    public boolean isExempt(final ExemptType... exemptTypes) {
        for (final ExemptType exemptType : exemptTypes) {
            if (this.isExempt(exemptType)) {
                return true;
            }
        }
        return false;
    }

    public boolean isExempt(final Function<PlayerData, Boolean> exception) {
        return exception.apply(this.data);
    }
}
