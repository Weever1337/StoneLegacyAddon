package dikiy.weever.stone_legacy.items;

import com.github.standobyte.jojo.init.ModStatusEffects;
import com.github.standobyte.jojo.init.power.non_stand.ModPowers;
import com.github.standobyte.jojo.power.impl.nonstand.INonStandPower;
import com.github.standobyte.jojo.power.impl.nonstand.type.hamon.HamonPowerType;
import com.github.standobyte.jojo.power.impl.nonstand.type.pillarman.PillarmanPowerType;
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

public class FruitItem extends Item {
    public static final DamageSource ASPHYXIA = new DamageSource("asphyxia").bypassArmor().bypassMagic().bypassInvul();

    public FruitItem(Properties properties) {
        super(properties);
    }

    public static int getStage(ItemStack stack) {
        return stack.getOrCreateTag().getInt("Stonefied");
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
                    entity.getCapability(PillarmanUtilProvider.CAPABILITY).ifPresent(pilla -> pilla.setHamonUser(power.getType() == hamon));
                    power.clear();
                    power.givePower(pillarman);
                });
                return super.finishUsingItem(stack, world, entity);
            }
        }
        return stack;
    }

    @Override
    public void inventoryTick(ItemStack stack, World world, Entity entity, int itemSlot, boolean isSelected) {
        if (!world.isClientSide() && entity instanceof LivingEntity) {
            LivingEntity livingEntity = (LivingEntity) entity;

            boolean isInHand = isSelected || livingEntity.getItemInHand(Hand.OFF_HAND) == stack;
            int stage = getStage(stack);

            if (isInHand) {
                int eatDuration = this.getUseDuration(stack);
                float lightLevel = world.dimensionType().hasSkyLight() && !world.dimensionType().hasCeiling() ? livingEntity.getBrightness() : 0.0F;

                if (lightLevel > 0) {
                    float lightDelay = (eatDuration - 2) / lightLevel / 6;
                    if (lightDelay > 0) {
                        stage = Math.min(stage + (int) (30 / lightDelay * 6), 630);
                    }
                }

                stack.getOrCreateTag().putInt("Stonefied", stage);

            } else {
                if (stage > 0) {
                    stage = Math.max(0, stage - 30);
                    stack.getOrCreateTag().putInt("Stonefied", stage);
                }
            }
        }
    }
}