package dikiy.weever.stone_legacy.action.non_stand.pillarman;

import com.github.standobyte.jojo.action.ActionTarget;
import com.github.standobyte.jojo.action.non_stand.PillarmanAction;
import com.github.standobyte.jojo.client.sound.ClientTickingSoundsHelper;
import com.github.standobyte.jojo.init.ModStatusEffects;
import com.github.standobyte.jojo.power.impl.nonstand.INonStandPower;
import com.github.standobyte.jojo.power.impl.nonstand.type.hamon.HamonUtil;
import com.github.standobyte.jojo.util.mc.damage.DamageUtil;
import net.minecraft.entity.LivingEntity;
import net.minecraft.potion.EffectInstance;
import net.minecraft.world.World;

public class PillarmanHamonSuicide extends PillarmanAction {
    public PillarmanHamonSuicide(Builder builder) {
        super(builder);
    }

    @Override
    public void startedHolding(World world, LivingEntity user, INonStandPower power, ActionTarget target, boolean requirementsFulfilled) {
        if (world.isClientSide()) {
            ClientTickingSoundsHelper.playHamonEnergyConcentrationSound(user, 1.0F, this);
        }
    }

    @Override
    protected void holdTick(World world, LivingEntity user, INonStandPower power, int ticksHeld, ActionTarget target, boolean requirementsFulfilled) {
        if (!world.isClientSide()) {
            if (ticksHeld % 10 == 5) {
                DamageUtil.dealHamonDamage(user, 4, user, null);
            }
            if (ticksHeld == 30) {
                user.addEffect(new EffectInstance(ModStatusEffects.HAMON_SPREAD.get(), 100, 1, false, false, true));
            }
        }
    }

    @Override
    protected void perform(World world, LivingEntity user, INonStandPower power, ActionTarget target) {
        if (!world.isClientSide()) {
            DamageUtil.dealHamonDamage(user, 200, user, null);
            HamonUtil.hamonExplosion(world, user, null,
                    user.getBoundingBox().getCenter(), 6, 6);
        }
    }
}
