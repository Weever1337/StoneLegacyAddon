package dikiy.weever.stone_legacy.items;

import com.github.standobyte.jojo.init.ModStatusEffects;
import com.github.standobyte.jojo.init.power.non_stand.ModPowers;
import com.github.standobyte.jojo.power.impl.nonstand.INonStandPower;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.UseAction;
import net.minecraft.util.DamageSource;
import net.minecraft.util.Hand;
import net.minecraft.world.World;

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
                    power.clear();
                    power.givePower(ModPowers.PILLAR_MAN.get());
                });
                return super.finishUsingItem(stack, world, entity);
            }
        }
        return stack;
    }

    public static byte getStage(ItemStack stack) {
        return stack.getOrCreateTag().getByte("Stonefied");
    }

    @Override
    public void inventoryTick(ItemStack stack, World world, Entity entity, int itemSlot, boolean isSelected) {
        LivingEntity livingEntity = (LivingEntity) entity;
        ItemStack handStack = livingEntity.getItemInHand(Hand.MAIN_HAND);
        ItemStack offHandStack = livingEntity.getItemInHand(Hand.OFF_HAND);
        FruitItem item = (FruitItem) stack.getItem();
        ItemStack currentStack = null;
        if (handStack.getItem() instanceof FruitItem) {
            currentStack = handStack;
        }
        if (offHandStack.getItem() instanceof FruitItem) {
            currentStack = offHandStack;
        }
        if (stack != handStack && stack != offHandStack) {
            byte stage = getStage(stack);
            if (stage > 0) {
                stage--;
                stack.getTag().putByte("Stonefied", stage);
            }
        }
        int eatDuration = item.getUseDuration(stack);
        int lightLevel = livingEntity.level.getLightEmission(livingEntity.getEntity().blockPosition());
        float lightMultiplier = lightLevel == 0 ? eatDuration - 1 : (float) ((eatDuration - 2) / lightLevel) / 2;
        ItemStack[] hands = {handStack, offHandStack};
        for (int i = 0; i < 2; i++) {
            if (hands[i] != null) {
                byte stage = getStage(hands[i]);
                if (stage < 21) {
                    stage++;
                    hands[i].getTag().putByte("Stonefied", stage);
                }
            }
        }
    }
    public static final DamageSource ASPHYXIA = new DamageSource("asphyxia").bypassArmor().bypassMagic().bypassInvul();
}
