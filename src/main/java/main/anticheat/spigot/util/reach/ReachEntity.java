package main.anticheat.spigot.util.reach;

import io.github.retrooper.packetevents.utils.vector.Vector3d;
import main.anticheat.spigot.util.boundingbox.BoundingBox;
import org.bukkit.Location;
import org.bukkit.entity.Entity;

import java.util.Deque;
import java.util.LinkedList;

public class ReachEntity {
    public static final BoundingBox ZERO_AABB;

    static {
        ZERO_AABB = new BoundingBox(0.0, 0.0, 0.0, 0.0, 0.0, 0.0);
    }

    public final Entity entity;
    public final Deque<BoundingBox> locations;
    public int serverPosX;
    public int serverPosY;
    public int serverPosZ;
    public double posX;
    public double posY;
    public double posZ;
    public double lastPosX;
    public double lastPosY;
    public double lastPosZ;
    public float width;
    public float height;
    public BoundingBox entityBoundingBox;
    public BoundingBox lastEntityBoundingBox;
    private int otherPlayerMPPosRotationIncrements;
    private double otherPlayerMPX;
    private double otherPlayerMPY;
    private double otherPlayerMPZ;
    private double otherPlayerMPYaw;
    private double otherPlayerMPPitch;

    public ReachEntity(final Entity entity) {
        this.locations = new LinkedList<BoundingBox>();
        this.entity = entity;
        this.entityBoundingBox = ReachEntity.ZERO_AABB.cloneBB();
        this.setSize(0.6f, 1.8f);
    }

    public ReachEntity(final Entity entity, final Vector3d vector3d) {
        this.locations = new LinkedList<BoundingBox>();
        this.entity = entity;
        this.serverPosX = (int) (vector3d.getX() * 32.0);
        this.serverPosY = (int) (vector3d.getY() * 32.0);
        this.serverPosZ = (int) (vector3d.getZ() * 32.0);
        this.posX = vector3d.getX();
        this.posY = vector3d.getY();
        this.posZ = vector3d.getZ();
        this.entityBoundingBox = ReachEntity.ZERO_AABB.cloneBB();
    }

    public ReachEntity(final Entity entity, final int posX, final int posY, final int posZ, final double multiplier) {
        this.locations = new LinkedList<BoundingBox>();
        this.entity = entity;
        this.serverPosX = posX;
        this.serverPosY = posY;
        this.serverPosZ = posZ;
        this.posX = this.serverPosX / multiplier;
        this.posY = this.serverPosY / multiplier;
        this.posZ = this.serverPosZ / multiplier;
        this.entityBoundingBox = ReachEntity.ZERO_AABB.cloneBB();
    }

    public void processMovement() {
        if (this.otherPlayerMPPosRotationIncrements > 0) {
            final double d0 = this.posX + (this.otherPlayerMPX - this.posX) / this.otherPlayerMPPosRotationIncrements;
            final double d2 = this.posY + (this.otherPlayerMPY - this.posY) / this.otherPlayerMPPosRotationIncrements;
            final double d3 = this.posZ + (this.otherPlayerMPZ - this.posZ) / this.otherPlayerMPPosRotationIncrements;
            --this.otherPlayerMPPosRotationIncrements;
            this.setPosition(d0, d2, d3);
        }
    }

    public void setPositionAndRotation2(final double x, final double y, final double z, final int posRotationIncrements, final boolean p_180426_10_) {
        this.otherPlayerMPX = x;
        this.otherPlayerMPY = y;
        this.otherPlayerMPZ = z;
        this.otherPlayerMPPosRotationIncrements = posRotationIncrements;
    }

    public void setSize(final float width, final float height) {
        if (width != this.width || height != this.height) {
            final float f = this.width;
            this.width = width;
            this.height = height;
            this.setEntityBoundingBox(new BoundingBox(this.getEntityBoundingBox().getMinX(), this.getEntityBoundingBox().getMinY(), this.getEntityBoundingBox().getMinZ(), this.getEntityBoundingBox().getMinX() + this.width, this.getEntityBoundingBox().getMinY() + this.height, this.getEntityBoundingBox().getMinZ() + this.width));
        }
    }

    public void setPosition(final double x, final double y, final double z) {
        this.lastPosX = this.posX;
        this.lastPosY = this.posY;
        this.lastPosZ = this.posZ;
        this.lastEntityBoundingBox = this.entityBoundingBox.cloneBB();
        this.posX = x;
        this.posY = y;
        this.posZ = z;
        final float f = this.width / 2.0f;
        final float f2 = this.height;
        this.setEntityBoundingBox(new BoundingBox(x - f, y, z - f, x + f, y + f2, z + f));
    }

    public void resetToBoundingAxis() {
        this.posX = this.entityBoundingBox.middleX();
        this.posY = this.entityBoundingBox.getMinY();
        this.posZ = this.entityBoundingBox.middleZ();
    }

    public Location getPosition() {
        return new Location(this.entity.getWorld(), this.posX, this.entityBoundingBox.getMinY(), this.posZ);
    }

    public void setPosition(final Location location) {
        this.setPosition(location.getX(), location.getY(), location.getZ());
    }

    public Location getLastPosition() {
        return new Location(this.entity.getWorld(), this.lastPosX, this.lastPosY, this.lastPosZ);
    }

    public BoundingBox getEntityBoundingBox() {
        return this.entityBoundingBox;
    }

    public void setEntityBoundingBox(final BoundingBox entityBoundingBox) {
        this.entityBoundingBox = entityBoundingBox;
    }

    public BoundingBox getLastEntityBoundingBox() {
        return this.lastEntityBoundingBox;
    }

    public void setLastEntityBoundingBox(final BoundingBox lastEntityBoundingBox) {
        this.lastEntityBoundingBox = lastEntityBoundingBox;
    }

    public Entity getEntity() {
        return this.entity;
    }

    public Deque<BoundingBox> getLocations() {
        return this.locations;
    }

    public int getServerPosX() {
        return this.serverPosX;
    }

    public void setServerPosX(final int serverPosX) {
        this.serverPosX = serverPosX;
    }

    public int getServerPosY() {
        return this.serverPosY;
    }

    public void setServerPosY(final int serverPosY) {
        this.serverPosY = serverPosY;
    }

    public int getServerPosZ() {
        return this.serverPosZ;
    }

    public void setServerPosZ(final int serverPosZ) {
        this.serverPosZ = serverPosZ;
    }

    public int getOtherPlayerMPPosRotationIncrements() {
        return this.otherPlayerMPPosRotationIncrements;
    }

    public void setOtherPlayerMPPosRotationIncrements(final int otherPlayerMPPosRotationIncrements) {
        this.otherPlayerMPPosRotationIncrements = otherPlayerMPPosRotationIncrements;
    }

    public double getOtherPlayerMPX() {
        return this.otherPlayerMPX;
    }

    public void setOtherPlayerMPX(final double otherPlayerMPX) {
        this.otherPlayerMPX = otherPlayerMPX;
    }

    public double getOtherPlayerMPY() {
        return this.otherPlayerMPY;
    }

    public void setOtherPlayerMPY(final double otherPlayerMPY) {
        this.otherPlayerMPY = otherPlayerMPY;
    }

    public double getOtherPlayerMPZ() {
        return this.otherPlayerMPZ;
    }

    public void setOtherPlayerMPZ(final double otherPlayerMPZ) {
        this.otherPlayerMPZ = otherPlayerMPZ;
    }

    public double getOtherPlayerMPYaw() {
        return this.otherPlayerMPYaw;
    }

    public void setOtherPlayerMPYaw(final double otherPlayerMPYaw) {
        this.otherPlayerMPYaw = otherPlayerMPYaw;
    }

    public double getOtherPlayerMPPitch() {
        return this.otherPlayerMPPitch;
    }

    public void setOtherPlayerMPPitch(final double otherPlayerMPPitch) {
        this.otherPlayerMPPitch = otherPlayerMPPitch;
    }

    public double getPosX() {
        return this.posX;
    }

    public void setPosX(final double posX) {
        this.posX = posX;
    }

    public double getPosY() {
        return this.posY;
    }

    public void setPosY(final double posY) {
        this.posY = posY;
    }

    public double getPosZ() {
        return this.posZ;
    }

    public void setPosZ(final double posZ) {
        this.posZ = posZ;
    }

    public double getLastPosX() {
        return this.lastPosX;
    }

    public void setLastPosX(final double lastPosX) {
        this.lastPosX = lastPosX;
    }

    public double getLastPosY() {
        return this.lastPosY;
    }

    public void setLastPosY(final double lastPosY) {
        this.lastPosY = lastPosY;
    }

    public double getLastPosZ() {
        return this.lastPosZ;
    }

    public void setLastPosZ(final double lastPosZ) {
        this.lastPosZ = lastPosZ;
    }

    public float getWidth() {
        return this.width;
    }

    public void setWidth(final float width) {
        this.width = width;
    }

    public float getHeight() {
        return this.height;
    }

    public void setHeight(final float height) {
        this.height = height;
    }

    @Override
    public boolean equals(final Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof ReachEntity)) {
            return false;
        }
        final ReachEntity other = (ReachEntity) o;
        if (!other.canEqual(this)) {
            return false;
        }
        if (this.getServerPosX() != other.getServerPosX()) {
            return false;
        }
        if (this.getServerPosY() != other.getServerPosY()) {
            return false;
        }
        if (this.getServerPosZ() != other.getServerPosZ()) {
            return false;
        }
        if (this.getOtherPlayerMPPosRotationIncrements() != other.getOtherPlayerMPPosRotationIncrements()) {
            return false;
        }
        if (Double.compare(this.getOtherPlayerMPX(), other.getOtherPlayerMPX()) != 0) {
            return false;
        }
        if (Double.compare(this.getOtherPlayerMPY(), other.getOtherPlayerMPY()) != 0) {
            return false;
        }
        if (Double.compare(this.getOtherPlayerMPZ(), other.getOtherPlayerMPZ()) != 0) {
            return false;
        }
        if (Double.compare(this.getOtherPlayerMPYaw(), other.getOtherPlayerMPYaw()) != 0) {
            return false;
        }
        if (Double.compare(this.getOtherPlayerMPPitch(), other.getOtherPlayerMPPitch()) != 0) {
            return false;
        }
        if (Double.compare(this.getPosX(), other.getPosX()) != 0) {
            return false;
        }
        if (Double.compare(this.getPosY(), other.getPosY()) != 0) {
            return false;
        }
        if (Double.compare(this.getPosZ(), other.getPosZ()) != 0) {
            return false;
        }
        if (Double.compare(this.getLastPosX(), other.getLastPosX()) != 0) {
            return false;
        }
        if (Double.compare(this.getLastPosY(), other.getLastPosY()) != 0) {
            return false;
        }
        if (Double.compare(this.getLastPosZ(), other.getLastPosZ()) != 0) {
            return false;
        }
        if (Float.compare(this.getWidth(), other.getWidth()) != 0) {
            return false;
        }
        if (Float.compare(this.getHeight(), other.getHeight()) != 0) {
            return false;
        }
        final Object this$entity = this.getEntity();
        final Object other$entity = other.getEntity();
        Label_0325:
        {
            if (this$entity == null) {
                if (other$entity == null) {
                    break Label_0325;
                }
            } else if (this$entity.equals(other$entity)) {
                break Label_0325;
            }
            return false;
        }
        final Object this$locations = this.getLocations();
        final Object other$locations = other.getLocations();
        Label_0362:
        {
            if (this$locations == null) {
                if (other$locations == null) {
                    break Label_0362;
                }
            } else if (this$locations.equals(other$locations)) {
                break Label_0362;
            }
            return false;
        }
        final Object this$entityBoundingBox = this.getEntityBoundingBox();
        final Object other$entityBoundingBox = other.getEntityBoundingBox();
        Label_0399:
        {
            if (this$entityBoundingBox == null) {
                if (other$entityBoundingBox == null) {
                    break Label_0399;
                }
            } else if (this$entityBoundingBox.equals(other$entityBoundingBox)) {
                break Label_0399;
            }
            return false;
        }
        final Object this$lastEntityBoundingBox = this.getLastEntityBoundingBox();
        final Object other$lastEntityBoundingBox = other.getLastEntityBoundingBox();
        if (this$lastEntityBoundingBox == null) {
            return other$lastEntityBoundingBox == null;
        } else return this$lastEntityBoundingBox.equals(other$lastEntityBoundingBox);
    }

    protected boolean canEqual(final Object other) {
        return other instanceof ReachEntity;
    }

    @Override
    public int hashCode() {
        int result = 1;
        result = result * 59 + this.getServerPosX();
        result = result * 59 + this.getServerPosY();
        result = result * 59 + this.getServerPosZ();
        result = result * 59 + this.getOtherPlayerMPPosRotationIncrements();
        result = result * 59 + Double.hashCode(this.getOtherPlayerMPX());
        result = result * 59 + Double.hashCode(this.getOtherPlayerMPY());
        result = result * 59 + Double.hashCode(this.getOtherPlayerMPZ());
        result = result * 59 + Double.hashCode(this.getOtherPlayerMPYaw());
        result = result * 59 + Double.hashCode(this.getOtherPlayerMPPitch());
        result = result * 59 + Double.hashCode(this.getPosX());
        result = result * 59 + Double.hashCode(this.getPosY());
        result = result * 59 + Double.hashCode(this.getPosZ());
        result = result * 59 + Double.hashCode(this.getLastPosX());
        result = result * 59 + Double.hashCode(this.getLastPosY());
        result = result * 59 + Double.hashCode(this.getLastPosZ());
        result = result * 59 + Float.floatToIntBits(this.getWidth());
        result = result * 59 + Float.floatToIntBits(this.getHeight());
        final Object $entity = this.getEntity();
        result = result * 59 + (($entity == null) ? 43 : $entity.hashCode());
        final Object $locations = this.getLocations();
        result = result * 59 + (($locations == null) ? 43 : $locations.hashCode());
        final Object $entityBoundingBox = this.getEntityBoundingBox();
        result = result * 59 + (($entityBoundingBox == null) ? 43 : $entityBoundingBox.hashCode());
        final Object $lastEntityBoundingBox = this.getLastEntityBoundingBox();
        result = result * 59 + (($lastEntityBoundingBox == null) ? 43 : $lastEntityBoundingBox.hashCode());
        return result;
    }

    @Override
    public String toString() {
        return "ReachEntity(entity=" + this.getEntity() + ", locations=" + this.getLocations() + ", serverPosX=" + this.getServerPosX() + ", serverPosY=" + this.getServerPosY() + ", serverPosZ=" + this.getServerPosZ() + ", otherPlayerMPPosRotationIncrements=" + this.getOtherPlayerMPPosRotationIncrements() + ", otherPlayerMPX=" + this.getOtherPlayerMPX() + ", otherPlayerMPY=" + this.getOtherPlayerMPY() + ", otherPlayerMPZ=" + this.getOtherPlayerMPZ() + ", otherPlayerMPYaw=" + this.getOtherPlayerMPYaw() + ", otherPlayerMPPitch=" + this.getOtherPlayerMPPitch() + ", posX=" + this.getPosX() + ", posY=" + this.getPosY() + ", posZ=" + this.getPosZ() + ", lastPosX=" + this.getLastPosX() + ", lastPosY=" + this.getLastPosY() + ", lastPosZ=" + this.getLastPosZ() + ", width=" + this.getWidth() + ", height=" + this.getHeight() + ", entityBoundingBox=" + this.getEntityBoundingBox() + ", lastEntityBoundingBox=" + this.getLastEntityBoundingBox() + ")";
    }
}
