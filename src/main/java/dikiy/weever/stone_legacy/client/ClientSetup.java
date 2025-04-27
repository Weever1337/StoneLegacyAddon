package dikiy.weever.stone_legacy.client;

import com.github.standobyte.jojo.client.render.armor.ArmorModelRegistry;
import com.github.standobyte.jojo.client.render.armor.model.StoneMaskModel;
import com.github.standobyte.jojo.init.ModBlocks;
import com.github.standobyte.jojo.init.ModItems;
import dikiy.weever.stone_legacy.StoneLegacyAddon;
import dikiy.weever.stone_legacy.init.InitBlocks;
import dikiy.weever.stone_legacy.init.InitItems;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.RenderTypeLookup;
import net.minecraft.item.ItemModelsProperties;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;

@Mod.EventBusSubscriber(modid = StoneLegacyAddon.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class ClientSetup {
    @SubscribeEvent
    public static void onFMLClientSetup(FMLClientSetupEvent event) {
        ArmorModelRegistry.registerArmorModel(StoneMaskModel::new, InitItems.PREDICTED_MASK_HEAT.get());
        ArmorModelRegistry.registerArmorModel(StoneMaskModel::new, InitItems.PREDICTED_MASK_LIGHT.get());
        ArmorModelRegistry.registerArmorModel(StoneMaskModel::new, InitItems.PREDICTED_MASK_WIND.get());
        event.enqueueWork(() -> {
            ItemModelsProperties.register(InitItems.FRUIT.get(), new ResourceLocation(StoneLegacyAddon.MOD_ID, "stage"),
                    (itemStack, clientWorld, livingEntity) -> livingEntity != null ?
                            itemStack.getOrCreateTag().getInt("Stonefied") : 21);

            RenderTypeLookup.setRenderLayer(InitBlocks.AJA_STONE_MASK_PREDICTED_HEAT.get(), RenderType.cutoutMipped());
            RenderTypeLookup.setRenderLayer(InitBlocks.AJA_STONE_MASK_PREDICTED_LIGHT.get(), RenderType.cutoutMipped());
            RenderTypeLookup.setRenderLayer(InitBlocks.AJA_STONE_MASK_PREDICTED_WIND.get(), RenderType.cutoutMipped());
        });
    }
}
