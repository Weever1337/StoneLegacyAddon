package dikiy.weever.stone_legacy.items;

import com.github.standobyte.jojo.init.ModStatusEffects;
import com.github.standobyte.jojo.init.power.non_stand.ModPowers;
import com.github.standobyte.jojo.power.impl.nonstand.INonStandPower;
import com.github.standobyte.jojo.power.impl.nonstand.TypeSpecificData;
import com.github.standobyte.jojo.power.impl.nonstand.type.NonStandPowerType;
import com.github.standobyte.jojo.power.impl.nonstand.type.hamon.HamonPowerType;
import com.github.standobyte.jojo.power.impl.nonstand.type.pillarman.PillarmanData;
import com.github.standobyte.jojo.power.impl.nonstand.type.pillarman.PillarmanPowerType;
import dikiy.weever.stone_legacy.StoneLegacyAddon;
import dikiy.weever.stone_legacy.capability.PillarmanUtilProvider;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.UseAction;
import net.minecraft.util.DamageSource;
import net.minecraft.util.Hand;
import net.minecraft.world.World;

import java.util.Optional;

public class FruitItem extends Item {
    public FruitItem(Properties properties) {
        super(properties);
    }

    @Override
    public UseAction getUseAnimation(ItemStack stack) {
        return UseAction.EAT;
    }



    @Override
    public ItemStack finishUsingItem(ItemStack stack, World world, LivingEntity entity) {
        if (!world.isClientSide()) {
            if (entity instanceof PlayerEntity) {
                if (entity.getEffect(ModStatusEffects.BLEEDING.get()) != null &&
                        entity.getHealth() <= (entity.getMaxHealth() + (entity.getEffect(ModStatusEffects.BLEEDING.get()).getAmplifier() * 2)) / 2 &&
                        !((PlayerEntity) entity).isCreative()) {
                    entity.hurt(ASPHYXIA, entity.getHealth());
                    return stack;
                }
                if (!((PlayerEntity) entity).isCreative()) {
                    entity.hurt(ASPHYXIA, entity.getMaxHealth() / 2);
                    stack.shrink(1);
                }
                INonStandPower.getNonStandPowerOptional(entity).ifPresent(power -> {
                    PillarmanPowerType pillarman = ModPowers.PILLAR_MAN.get();
                    HamonPowerType hamon = ModPowers.HAMON.get();
                    entity.getCapability(PillarmanUtilProvider.CAPABILITY).ifPresent(pilla -> pilla.sethamonUser(power.getType() == hamon));
                    power.clear();
                    power.givePower(pillarman);
                });
                return super.finishUsingItem(stack, world, entity);
            }
        }
        return stack;
    }

    public static int getStage(ItemStack stack) {
        return stack.getOrCreateTag().getInt("Stonefied");
    }

    @Override
    public void inventoryTick(ItemStack stack, World world, Entity entity, int itemSlot, boolean isSelected) {
        if (!world.isClientSide()) {
        LivingEntity livingEntity = (LivingEntity) entity;
        ItemStack handStack = livingEntity.getItemInHand(Hand.MAIN_HAND);
        ItemStack offHandStack = livingEntity.getItemInHand(Hand.OFF_HAND);
        FruitItem item = (FruitItem) stack.getItem();
        if (stack != handStack && stack != offHandStack) {
            int stage = getStage(stack);
            if (stage > 0) {
                stage -= 30;
                stack.getTag().putInt("Stonefied", stage);
            }
        }
        int eatDuration = item.getUseDuration(stack);
        float lightLevel = 6.0F;
        if (world.dimensionType().hasSkyLight()
                && !world.dimensionType().hasCeiling()
                && world.isDay()
                && !world.isRaining()
                && !world.isThundering()) {
            lightLevel = 15 * livingEntity.getBrightness() >= 6? 15 * livingEntity.getBrightness(): 6.0F;
        }
//        System.out.println(lightLevel);
        float lightDelay = lightLevel == 0 ? eatDuration - 2 : ((eatDuration - 2) / lightLevel) / 2;
        ItemStack[] hands = {handStack, offHandStack};
            for (int i = 0; i < 2; i++) {
                if (hands[i] != null) {
                    int stage = getStage(hands[i]);
                    stage = (stage >= 630) ? 630 : stage + (int) ((30 / lightDelay) * 6);
                    hands[i].getTag().putInt("Stonefied", stage);
                }
            }
        }
    }
    public static final DamageSource ASPHYXIA = new DamageSource("asphyxia").bypassArmor().bypassMagic().bypassInvul();
}
