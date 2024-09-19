package dikiy.weever.stone_legacy.mixin;

import com.github.standobyte.jojo.action.ActionTarget;
import com.github.standobyte.jojo.action.non_stand.VampirismAction;
import com.github.standobyte.jojo.action.non_stand.VampirismZombieSummon;
import com.github.standobyte.jojo.init.power.non_stand.ModPowers;
import com.github.standobyte.jojo.power.impl.nonstand.INonStandPower;
import com.github.standobyte.jojo.util.mc.MCUtil;
import net.minecraft.entity.LivingEntity;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

@Mixin(VampirismZombieSummon.class)
public class VampireActionMixin extends VampirismAction {
    public VampireActionMixin(Builder builder) {
        super(builder);
    }

    @Inject(method = "perform(Lnet/minecraft/world/World;Lnet/minecraft/entity/LivingEntity;Lcom/github/standobyte/jojo/power/impl/nonstand/INonStandPower;Lcom/github/standobyte/jojo/action/ActionTarget;)V", at = @At("HEAD"), cancellable = true)
    protected void perform(World world, LivingEntity user, INonStandPower power, ActionTarget target, CallbackInfo ci) {
        List<LivingEntity> entitiesAround = MCUtil.entitiesAround(LivingEntity.class, user, 16, false, null);
        AtomicInteger spawned = new AtomicInteger();
        int canSpawned = world.getDifficulty().getId();
        entitiesAround.forEach(entity -> {
            if (spawned.get() <= canSpawned) {
                if (entity.deathTime >= 1) {
                    INonStandPower.getNonStandPowerOptional(entity).ifPresent(nonPower -> {
                        if (nonPower.getType() == ModPowers.HAMON.get()) {
                            if (nonPower.getEnergy() < 5) {
                                MCUtil.onEntityResurrect(entity);
                                nonPower.clear();
                                nonPower.givePower(ModPowers.ZOMBIE.get());
                                spawned.getAndIncrement();
                            }
                        } else {
                            MCUtil.onEntityResurrect(entity);
                            nonPower.clear();
                            nonPower.givePower(ModPowers.ZOMBIE.get());
                            spawned.getAndIncrement();
                        }
                    });
                }
            }
        });
    }
}
