package dikiy.weever.stone_legacy.items;

import dikiy.weever.stone_legacy.StoneLegacyAddon;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.Hand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;

import java.util.List;

public class FruitItem extends Item {
    public FruitItem(Properties properties) {

        super(properties);
    }


    public byte getStage(ItemStack stack) {
        return stack.getOrCreateTag().getByte("Stonefied");
    }

    @Override
    public void inventoryTick(ItemStack stack, World world, Entity entity, int itemSlot, boolean isSelected) {
        LivingEntity livingEntity = (LivingEntity) entity;
        ItemStack currentStack = null;
        ItemStack currentStackOptional = null;
        FruitItem item = (FruitItem) stack.getItem();
        for (int i = 0; i < 2; i++) {
            if (livingEntity.getItemInHand(Hand.MAIN_HAND).getItem() instanceof FruitItem && i != 0) {
                item = (FruitItem) livingEntity.getItemInHand(Hand.MAIN_HAND).getItem();
                currentStack = livingEntity.getItemInHand(Hand.MAIN_HAND);
            }
            if (livingEntity.getItemInHand(Hand.OFF_HAND).getItem() instanceof FruitItem) {
                item = (FruitItem) livingEntity.getItemInHand(Hand.OFF_HAND).getItem();
                if (currentStack == null) {
                    currentStack = livingEntity.getItemInHand(Hand.OFF_HAND);
                } else {
                    currentStackOptional = livingEntity.getItemInHand(Hand.OFF_HAND);
                }
            }
            if (stack != currentStack && stack != currentStackOptional) {
                byte stage = getStage(stack);
                if (stage > 0) {
                    stage--;
                    stack.getTag().putByte("Stonefied", stage);
                }
            }
            int eatDuration;
            eatDuration = item.getUseDuration(new ItemStack(item));
            int lightLevel = livingEntity.level.getLightEmission(livingEntity.getEntity().blockPosition());
            float lightMultiplier = lightLevel == 0 ? eatDuration - 1 : (float) ((eatDuration - 2) / lightLevel) / 2;
            if (currentStack != null) {
                byte stage = getStage(currentStack);
                if (stage < 21) {
                    stage++;
                    currentStack.getTag().putByte("Stonefied", stage);
                }
            } else if (currentStackOptional != null) {
                byte stage = getStage(currentStack);
                if (stage < 21) {
                    stage++;
                    currentStack.getTag().putByte("Stonefied", stage);
                }
            }
        }
    }
//        CompoundNBT tag = stack.getTag();
//        StoneLegacyAddon.getLogger().debug(tag.getByte("Stonefied"));
//        byte textureTicks = tag.getByte("Stonefied");
//        if ((entity instanceof LivingEntity &&
//                ((LivingEntity) entity).getItemInHand(Hand.MAIN_HAND).getItem() instanceof FruitItem ||
//                ((LivingEntity) entity).getItemInHand(Hand.OFF_HAND).getItem() instanceof FruitItem) &&
//        textureTicks < 23) {
//            tag.putByte("Stonefied", textureTicks);
//            ++textureTicks;
//        } else if (textureTicks > 0) {
//            tag.putByte("Stonefied", textureTicks);
//            --textureTicks;
//        }
//    }
}
