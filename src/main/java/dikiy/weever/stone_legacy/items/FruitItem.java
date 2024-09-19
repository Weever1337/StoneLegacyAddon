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


    public static void setStoneTexture(ItemStack stack) {
        stack.getTag().putByte("Stonefied", (byte)22);
    }

    @Override
    public void inventoryTick(ItemStack stack, World world, Entity entity, int itemSlot, boolean isSelected) {
        CompoundNBT tag = stack.getTag();
        StoneLegacyAddon.getLogger().debug(tag.getByte("Stonefied"));
        // FIXME related to ClientSetup
        byte textureTicks = tag.getByte("Stonefied");
        if ((entity instanceof LivingEntity &&
                ((LivingEntity) entity).getItemInHand(Hand.MAIN_HAND).getItem() instanceof FruitItem ||
                ((LivingEntity) entity).getItemInHand(Hand.OFF_HAND).getItem() instanceof FruitItem) &&
        textureTicks < 23) {
            tag.putByte("Stonefied", textureTicks);
            ++textureTicks;
        } else if (textureTicks > 0) {
            tag.putByte("Stonefied", textureTicks);
            --textureTicks;
        }
    }
}
