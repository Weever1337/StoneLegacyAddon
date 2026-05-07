package dikiy.weever.stone_legacy.action.non_stand.vampirism;

import com.github.standobyte.jojo.action.ActionTarget;
import com.github.standobyte.jojo.action.non_stand.VampirismAction;
import com.github.standobyte.jojo.entity.mob.HungryZombieEntity;
import com.github.standobyte.jojo.power.impl.nonstand.INonStandPower;
import com.github.standobyte.jojo.util.mc.MCUtil;
import dikiy.weever.stone_legacy.mixin_helper.IWaitableEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.world.World;

public class VampirismWaitOrder extends VampirismAction {

 public VampirismWaitOrder(Builder builder) { super(builder); }

 @Override
 protected void perform(World world, LivingEntity user, INonStandPower power, ActionTarget target) {
  if (!world.isClientSide()) {
   int range = 16 * world.getDifficulty().getId() - 8;
   for (HungryZombieEntity zombie : MCUtil.entitiesAround(HungryZombieEntity.class, user,
           range, true, e -> ((IWaitableEntity)e).getOwner() == user && e instanceof IWaitableEntity)) {
    ((IWaitableEntity)zombie).setOrderedToSit(true);
   }
  }
 }
}
