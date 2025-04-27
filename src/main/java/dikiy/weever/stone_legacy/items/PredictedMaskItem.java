package dikiy.weever.stone_legacy.items;

import com.github.standobyte.jojo.block.StoneMaskBlock;
import com.github.standobyte.jojo.item.StoneMaskItem;
import com.github.standobyte.jojo.power.impl.nonstand.type.pillarman.PillarmanData;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.IArmorMaterial;
import net.minecraft.item.Item;

public class PredictedMaskItem extends StoneMaskItem {
    private final PillarmanData.Mode PREDICTED_MODE;
    public PredictedMaskItem(IArmorMaterial material, EquipmentSlotType slot, Properties builder, StoneMaskBlock block) {
        super(material, slot, builder, block);
        this.PREDICTED_MODE = builder.mode;
    }
    public static class Properties extends Item.Properties {
        private PillarmanData.Mode mode = PillarmanData.Mode.NONE;
        public Properties prediction(PillarmanData.Mode mode) {
            this.mode = mode;
            return this;
        }
    }
}
