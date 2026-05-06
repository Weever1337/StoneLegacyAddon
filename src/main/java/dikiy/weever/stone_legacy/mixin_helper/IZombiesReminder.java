package dikiy.weever.stone_legacy.mixin_helper;

import net.minecraft.entity.LivingEntity;
import org.spongepowered.asm.mixin.Unique;

import java.util.List;
import java.util.UUID;

public interface IZombiesReminder {
    @Unique List<UUID> getOwnedZombies();
    @Unique void setOwnedZombies(List<UUID> list);
    @Unique void addZombie(LivingEntity living);
    @Unique void removeZombie(LivingEntity living);
    @Unique void removeZombie(UUID living);
}
