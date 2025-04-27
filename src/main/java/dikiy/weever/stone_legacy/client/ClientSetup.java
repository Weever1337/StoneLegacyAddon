package dikiy.weever.stone_legacy.client;

import dikiy.weever.stone_legacy.StoneLegacyAddon;
import dikiy.weever.stone_legacy.init.InitItems;
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
        event.enqueueWork(() -> {
            ItemModelsProperties.register(InitItems.FRUIT.get(), new ResourceLocation(StoneLegacyAddon.MOD_ID, "stage"),
                    (itemStack, clientWorld, livingEntity) -> livingEntity != null ?
                            itemStack.getOrCreateTag().getInt("Stonefied") : 21);
        });
    }
}
