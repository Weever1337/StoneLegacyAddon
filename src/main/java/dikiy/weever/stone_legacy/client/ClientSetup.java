package dikiy.weever.stone_legacy.client;

import com.github.standobyte.jojo.JojoMod;
import dikiy.weever.stone_legacy.StoneLegacyAddon;
import dikiy.weever.stone_legacy.init.InitItems;
import dikiy.weever.stone_legacy.items.FruitItem;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemModelsProperties;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Hand;
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
            ItemModelsProperties.register(InitItems.FRUIT.get(), new ResourceLocation(StoneLegacyAddon.MOD_ID, "stage"), (itemStack, clientWorld, livingEntity) -> {
                StoneLegacyAddon.getLogger().debug(itemStack.getTag().getByte("Stonefied")); // wtf it is not working
                // FIXME!!!!! Null pointer exception on trying to get Byte Value
                return itemStack.getTag().getByte("Stonefied");
            });
        });
    }
}
