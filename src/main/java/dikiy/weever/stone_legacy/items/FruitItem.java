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
    public static final String NBT_ACTIVATION_KEY = "Stonefied";
    public FruitItem(Properties properties) {
        super(properties);
    }

    public static void setStoneTexture(ItemStack stack) {
        stack.getTag().putByte(NBT_ACTIVATION_KEY, (byte)22);
    }

    @Override
    public void inventoryTick(ItemStack stack, World world, Entity entity, int itemSlot, boolean isSelected) {
        CompoundNBT tag = stack.getTag();
        System.out.println(world.isClientSide());
        if (tag != null) {
            byte textureTicks = tag.getByte(NBT_ACTIVATION_KEY);
            if ((entity instanceof LivingEntity &&
                    ((LivingEntity) entity).getItemInHand(Hand.MAIN_HAND).getItem() instanceof FruitItem ||
                    ((LivingEntity) entity).getItemInHand(Hand.OFF_HAND).getItem() instanceof FruitItem) &&
                    textureTicks < 23) {
                tag.putByte(NBT_ACTIVATION_KEY, textureTicks);
                ++textureTicks;
            } else if (textureTicks > 0) {
                tag.putByte(NBT_ACTIVATION_KEY, textureTicks);
                --textureTicks;
            }
        }
    }
}
