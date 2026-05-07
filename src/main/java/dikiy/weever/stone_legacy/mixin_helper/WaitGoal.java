package dikiy.weever.stone_legacy.mixin_helper;

import com.github.standobyte.jojo.entity.mob.HungryZombieEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.goal.Goal;

import java.util.EnumSet;

public class WaitGoal extends Goal {
    private final HungryZombieEntity mob;

    public WaitGoal(HungryZombieEntity mob) {
        this.mob = mob;
        this.setFlags(EnumSet.of(Goal.Flag.JUMP, Goal.Flag.MOVE));
    }

    public boolean canContinueToUse() {
        return ((IWaitableEntity)this.mob).isOrderedToSit();
    }

    public boolean canUse() {
        if (this.mob.isInWaterOrBubble()) {
            return false;
        } else
        if (!this.mob.isOnGround()) {
            return false;
        } else {
            LivingEntity livingentity = this.mob.getOwner();
            if (livingentity == null) {
                return true;
            } else {
                return this.mob.distanceToSqr(livingentity) < 144.0D && livingentity.getLastHurtByMob() != null ? false : ((IWaitableEntity)this.mob).isOrderedToSit();
            }
        }
    }

    public void start() {
        this.mob.getNavigation().stop();
        ((IWaitableEntity)this.mob).setInSittingPose(true);
    }

    public void stop() {
        ((IWaitableEntity)this.mob).setInSittingPose(false);
    }
}
