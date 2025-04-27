package dikiy.weever.stone_legacy.init;

import com.github.standobyte.jojo.init.ModItems;
import com.github.standobyte.jojo.item.ModArmorMaterials;
import com.github.standobyte.jojo.power.impl.nonstand.type.pillarman.PillarmanData;
import dikiy.weever.stone_legacy.StoneLegacyAddon;
import dikiy.weever.stone_legacy.items.FruitItem;
import dikiy.weever.stone_legacy.items.PredictedMaskItem;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.Food;
import net.minecraft.item.Item;
import net.minecraft.item.Rarity;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class InitItems {
    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, StoneLegacyAddon.MOD_ID);

    public static final RegistryObject<FruitItem> FRUIT = ITEMS.register("fruit",
            () -> new FruitItem(new Item.Properties().stacksTo(1).tab(ModItems.MAIN_TAB).rarity(Rarity.RARE)
                    .food(new Food.Builder().saturationMod(6).nutrition(4).alwaysEat().build())));
    public static final RegistryObject<PredictedMaskItem> PREDICTED_MASK_HEAT = ITEMS.register("aja_stone_mask_predicted_heat",
            () -> new PredictedMaskItem(ModArmorMaterials.STONE_MASK, EquipmentSlotType.HEAD,
                    (PredictedMaskItem.Properties) new PredictedMaskItem.Properties().prediction(PillarmanData.Mode.HEAT)
                            .rarity(Rarity.RARE).tab(ModItems.MAIN_TAB), InitBlocks.AJA_STONE_MASK_PREDICTED_HEAT.get()));
    public static final RegistryObject<PredictedMaskItem> PREDICTED_MASK_LIGHT = ITEMS.register("aja_stone_mask_predicted_light",
            () -> new PredictedMaskItem(ModArmorMaterials.STONE_MASK, EquipmentSlotType.HEAD,
                    (PredictedMaskItem.Properties) new PredictedMaskItem.Properties().prediction(PillarmanData.Mode.LIGHT)
                            .rarity(Rarity.RARE).tab(ModItems.MAIN_TAB), InitBlocks.AJA_STONE_MASK_PREDICTED_LIGHT.get()));
    public static final RegistryObject<PredictedMaskItem> PREDICTED_MASK_WIND = ITEMS.register("aja_stone_mask_predicted_wind",
            () -> new PredictedMaskItem(ModArmorMaterials.STONE_MASK, EquipmentSlotType.HEAD,
                    (PredictedMaskItem.Properties) new PredictedMaskItem.Properties().prediction(PillarmanData.Mode.WIND)
                            .rarity(Rarity.RARE).tab(ModItems.MAIN_TAB), InitBlocks.AJA_STONE_MASK_PREDICTED_WIND.get()));
}
