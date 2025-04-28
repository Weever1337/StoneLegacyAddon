package dikiy.weever.stone_legacy.items;

import com.github.standobyte.jojo.block.StoneMaskBlock;
import com.github.standobyte.jojo.init.ModItems;
import com.github.standobyte.jojo.item.StoneMaskItem;
import com.github.standobyte.jojo.power.impl.nonstand.type.pillarman.PillarmanData;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.IArmorMaterial;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.world.World;

import java.util.List;

public class PredictedMaskItem extends StoneMaskItem {
    private final PillarmanData.Mode PREDICTED_MODE;
    public PredictedMaskItem(IArmorMaterial material, EquipmentSlotType slot, Properties builder, StoneMaskBlock block) {
        super(material, slot, builder, block);
        this.PREDICTED_MODE = builder.mode;
    }

    @Override
    public void inventoryTick(ItemStack stack, World world, Entity entity, int itemSlot, boolean isSelected) {
        CompoundNBT tag = stack.getTag();
        byte textureTicks = tag.getByte(NBT_ACTIVATION_KEY);
        if (textureTicks > 0) {
            tag.putByte(NBT_ACTIVATION_KEY, --textureTicks);
            if (textureTicks == 0) {
                ItemStack mask = new ItemStack(ModItems.STONE_MASK.get());
                mask.setTag(stack.getTag());

                Iterable<ItemStack> armor = entity.getArmorSlots();
                if (armor instanceof List) {
                    List<ItemStack> armorList = ((List<ItemStack>) armor);
                    int index = EquipmentSlotType.HEAD.getIndex();
                    if (armorList.get(index) == stack) {
                        armorList.set(index, ItemStack.EMPTY);
                        entity.spawnAtLocation(mask);
                    } else ((PlayerEntity) entity).inventory.setItem(itemSlot, mask);
                }
            }
        }
    }

    public PillarmanData.Mode getMode() { return PREDICTED_MODE; }
    public static class Properties extends Item.Properties {
        private PillarmanData.Mode mode = PillarmanData.Mode.NONE;
        public Properties prediction(PillarmanData.Mode mode) {
            this.mode = mode;
            return this;
        }
    }
}
