package dikiy.weever.stone_legacy.mixin;

import com.github.standobyte.jojo.init.power.non_stand.ModPowers;
import com.github.standobyte.jojo.power.impl.nonstand.TypeSpecificData;
import com.github.standobyte.jojo.power.impl.nonstand.type.NonStandPowerType;
import com.github.standobyte.jojo.power.impl.nonstand.type.hamon.HamonData;
import com.github.standobyte.jojo.power.impl.nonstand.type.pillarman.PillarmanData;
import dikiy.weever.stone_legacy.capability.PillarmanUtilProvider;
import dikiy.weever.stone_legacy.network.AddonPackets;
import dikiy.weever.stone_legacy.network.server.TrPillarmanDataPacket;
import net.minecraft.entity.LivingEntity;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(PillarmanData.class)
public abstract class PillarmanDataMixin extends TypeSpecificData {
    @Unique
    private boolean hamonUser = false;
    
    @Inject(method = "onPowerGiven", at = @At("HEAD"))
    public void onPowerGiven(NonStandPowerType<?> oldType, TypeSpecificData oldData, CallbackInfo ci) {
        LivingEntity user = power.getUser();
        if (user.level.isClientSide()) {
            power.clUpdateHud();
        }
    }

    @Unique
    private void stoneLegacyAddon$setHamonUser(boolean set) {
        if (this.hamonUser == set) {
            return;
        }
        hamonUser = set;

        LivingEntity user = power.getUser();
        if (!user.level.isClientSide()) {
            user.getCapability(PillarmanUtilProvider.CAPABILITY).ifPresent(cap -> cap.sethamonUser(hamonUser));
        }
        if (user.level.isClientSide()) {
            power.clUpdateHud();
        }
    }
}
