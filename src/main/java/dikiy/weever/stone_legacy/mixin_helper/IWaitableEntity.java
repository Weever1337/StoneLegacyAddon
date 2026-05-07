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
    boolean isInWaterOrBubble();
    boolean isOnGround();
    LivingEntity getOwner();
    double distanceToSqr(Entity living);
    PathNavigator getNavigation();
}
