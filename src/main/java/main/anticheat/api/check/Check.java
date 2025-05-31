package main.anticheat.api.check;

public interface Check {
    String getCategory();

    String getName();

    char getType();

    char getDisplayType();

    int getMaxVl();

    int getVl();

    void setVl(final int p0);

    double getMaxBuffer();

    boolean isExperimental();

    double getBufferDecay();

    double getBufferMultiple();

    int getMinimumVlToNotify();

    int getAlertInterval();

    double getBuffer();

    void setBuffer(final double p0);

    boolean isPunishable();

    String getDescription();

    String getComplexType();

    String getDisplayName();
}
