package dikiy.weever.stone_legacy.util;

import dikiy.weever.stone_legacy.StoneLegacyAddon;
import dikiy.weever.stone_legacy.items.FruitItem;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.item.ItemStack;
import net.minecraftforge.event.entity.EntityEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = StoneLegacyAddon.MOD_ID)
public class FruitHandler {
    @SubscribeEvent(priority = EventPriority.HIGHEST)
    public static void onEntity(EntityEvent event) {
        Entity entity = event.getEntity();

        if (entity instanceof ItemEntity) {
            ItemStack itemStack = ((ItemEntity) entity).getItem();
            if (itemStack.getItem() instanceof FruitItem) {
                int stage = FruitItem.getStage(itemStack);
                if (stage > 0) {
                    stage--;
                    itemStack.getTag().putInt("Stonefied", stage);
                }
            }
        }
    }
}
