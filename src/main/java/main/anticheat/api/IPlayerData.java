package main.anticheat.api;

public interface IPlayerData {
    int getTotalViolations();

    int getCombatViolations();

    int getMovementViolations();

    int getPlayerViolations();

    int getAutoClickerViolations();

    int getTimerViolations();

    int getScaffoldViolations();

    long getJoinTime();

    int getJoinTicks();

    String getClientBrand();

    long getLastClientBrandAlert();
}
