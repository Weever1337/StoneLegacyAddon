package dikiy.weever.stone_legacy.mixin_helper;

import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.pathfinding.PathNavigator;
import org.spongepowered.asm.mixin.Unique;

public interface IWaitableEntity {
    @Unique
    public boolean isInSittingPose();
    @Unique
    public void setInSittingPose(boolean isInPose);
    @Unique
    public boolean isOrderedToSit();
    @Unique
    public void setOrderedToSit(boolean isOrdered);
    @Unique
    void setOwner(LivingEntity owner);

    // wouldn't use it
    @Unique
    boolean isInWaterOrBubble();
    @Unique
    boolean isOnGround();
    @Unique
    LivingEntity getOwner();
    @Unique
    double distanceToSqr(Entity living);
    @Unique
    PathNavigator getNavigation();
}
