package dikiy.weever.stone_legacy.mixin;

import com.github.standobyte.jojo.action.ActionTarget;
import com.github.standobyte.jojo.action.non_stand.VampirismDarkAura;
import com.github.standobyte.jojo.entity.mob.HungryZombieEntity;
import com.github.standobyte.jojo.power.impl.nonstand.INonStandPower;
import com.github.standobyte.jojo.util.mc.MCUtil;
import dikiy.weever.stone_legacy.mixin_helper.IWaitableEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value = VampirismDarkAura.class, remap = false)
public abstract class VampirismDarkAuraFreeZombiesMixin {
    @Inject(method = "perform(Lnet/minecraft/world/World;Lnet/minecraft/entity/LivingEntity;Lcom/github/standobyte/jojo/power/impl/nonstand/INonStandPower;Lcom/github/standobyte/jojo/action/ActionTarget;)V",
    at = @At("TAIL"), remap = false)
    public void freeZombies(World world, LivingEntity user, INonStandPower power, ActionTarget target, CallbackInfo ci) {
        if (!world.isClientSide()) {
            int range = 16 * world.getDifficulty().getId() - 8;
            for (HungryZombieEntity zombie : MCUtil.entitiesAround(HungryZombieEntity.class, user,
                    range, false, e -> e.getOwner() == user && e instanceof IWaitableEntity)) {
                ((IWaitableEntity) zombie).setOrderedToSit(false);
            }
        }
    }
}
