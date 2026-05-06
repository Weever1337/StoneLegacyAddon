package dikiy.weever.stone_legacy.mixin_helper;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.goal.Goal;

import java.util.EnumSet;

public class WaitGoal extends Goal {
    private final IWaitableEntity mob;

    public WaitGoal(IWaitableEntity mob) {
        this.mob = mob;
        this.setFlags(EnumSet.of(Goal.Flag.JUMP, Goal.Flag.MOVE));
    }

    public boolean canContinueToUse() {
        return this.mob.isOrderedToSit();
    }

    public boolean canUse() {
        if (this.mob.isInWaterOrBubble()) {
            return false;
        } else if (!this.mob.isOnGround()) {
            return false;
        } else {
            LivingEntity livingentity = this.mob.getOwner();
            if (livingentity == null) {
                return true;
            } else {
                return this.mob.distanceToSqr(livingentity) < 144.0D && livingentity.getLastHurtByMob() != null ? false : this.mob.isOrderedToSit();
            }
        }
    }

    public void start() {
        this.mob.getNavigation().stop();
        this.mob.setInSittingPose(true);
    }

    public void stop() {
        this.mob.setInSittingPose(false);
    }
}
