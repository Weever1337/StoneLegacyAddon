package dikiy.weever.stone_legacy.util;

import com.github.standobyte.jojo.entity.stand.StandEntity;
import dikiy.weever.stone_legacy.StoneLegacyAddon;
import dikiy.weever.stone_legacy.capability.ZombieUtilCap;
import dikiy.weever.stone_legacy.capability.ZombieUtilProvider;
import dikiy.weever.stone_legacy.items.FruitItem;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.DamageSource;
import net.minecraftforge.event.entity.EntityEvent;
import net.minecraftforge.event.entity.item.ItemEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.Objects;

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
