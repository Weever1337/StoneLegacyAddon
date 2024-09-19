package dikiy.weever.stone_legacy.client;

import com.github.standobyte.jojo.JojoMod;
import dikiy.weever.stone_legacy.StoneLegacyAddon;
import dikiy.weever.stone_legacy.init.InitItems;
import dikiy.weever.stone_legacy.items.FruitItem;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemModelsProperties;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
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
                if (livingEntity != null) {
                    if (!itemStack.hasTag()) {
                        CompoundNBT nbt = new CompoundNBT();
                        itemStack.setTag(nbt);
                        nbt.putByte(FruitItem.NBT_ACTIVATION_KEY, (byte)0);
                        System.out.println("BBBBB");
                        return nbt.getByte(FruitItem.NBT_ACTIVATION_KEY);
                    } else {
                        CompoundNBT nbt = itemStack.getTag();
                        if (!nbt.contains(FruitItem.NBT_ACTIVATION_KEY)) {
                            nbt.putByte(FruitItem.NBT_ACTIVATION_KEY, (byte)0);
                        }
                        System.out.println("CCCCC");
                        return nbt.getByte(FruitItem.NBT_ACTIVATION_KEY);
                    }
//                    if (itemStack.getTag() != null && itemStack.getTag().getByte(FruitItem.NBT_ACTIVATION_KEY) > 0) {
//                        System.out.println("XXXXXX");
//                        return 20;
//                    }
                }
                System.out.println("AAAAA");
//                return itemStack.getTag().getByte(FruitItem.NBT_ACTIVATION_KEY) > 0 ? 20 : 0;
                return 20;
            });
        });
    }
}