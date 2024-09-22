package dikiy.weever.stone_legacy.mixin;

import com.github.standobyte.jojo.action.Action;
import com.github.standobyte.jojo.action.non_stand.PillarmanAction;
import com.github.standobyte.jojo.client.controls.ControlScheme;
import com.github.standobyte.jojo.power.impl.nonstand.INonStandPower;
import com.github.standobyte.jojo.power.impl.nonstand.type.NonStandPowerType;
import com.github.standobyte.jojo.power.impl.nonstand.type.pillarman.PillarmanData;
import com.github.standobyte.jojo.power.impl.nonstand.type.pillarman.PillarmanPowerType;
import dikiy.weever.stone_legacy.capability.PillarmanUtilCap;
import dikiy.weever.stone_legacy.capability.PillarmanUtilProvider;
import dikiy.weever.stone_legacy.init.InitActions;
import net.minecraft.entity.LivingEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

import java.util.function.Supplier;

@Mixin(PillarmanPowerType.class)
public abstract class PillarmanPowerTypeMixin extends NonStandPowerType<PillarmanData> {
    public PillarmanPowerTypeMixin(Action<INonStandPower>[] startingAttacks, Action<INonStandPower>[] startingAbilities, Action<INonStandPower> defaultQuickAccess, Supplier<PillarmanData> dataFactory) {
        super(startingAttacks, startingAbilities, defaultQuickAccess, dataFactory);
    }

    @Inject(method = "clAddMissingActions(Lcom/github/standobyte/jojo/client/controls/ControlScheme;Lcom/github/standobyte/jojo/power/impl/nonstand/INonStandPower;)V", at = @At(value = "INVOKE", target = "Lcom/github/standobyte/jojo/power/impl/nonstand/INonStandPower;getTypeSpecificData(Lcom/github/standobyte/jojo/power/impl/nonstand/type/NonStandPowerType;)Ljava/util/Optional;"))
    public void clAddMissingActions(ControlScheme controlScheme, INonStandPower power, CallbackInfo ci) {
        LivingEntity user = power.getUser();
        if (user.getCapability(PillarmanUtilProvider.CAPABILITY).map(PillarmanUtilCap::getHamonUser).orElse(false)) {
            System.out.println("miss actions");
            controlScheme.addIfMissing(ControlScheme.Hotbar.RIGHT_CLICK, InitActions.PILLARMAN_HAMON_SUICIDE.get());
        }
    }

    @Inject(method="isActionLegalInHud(Lcom/github/standobyte/jojo/action/Action;Lcom/github/standobyte/jojo/power/impl/nonstand/INonStandPower;)Z", at = @At(value = "INVOKE", target = "Lcom/github/standobyte/jojo/power/impl/nonstand/INonStandPower;getTypeSpecificData(Lcom/github/standobyte/jojo/power/impl/nonstand/type/NonStandPowerType;)Ljava/util/Optional;"), locals = LocalCapture.CAPTURE_FAILSOFT, cancellable = true)
    public void isActionLegalInHud(Action<INonStandPower> action, INonStandPower power, CallbackInfoReturnable<Boolean> cir, PillarmanAction pmAction) {
        if (pmAction == InitActions.PILLARMAN_HAMON_SUICIDE.get()) {
            System.out.println(power.getUser().getCapability(PillarmanUtilProvider.CAPABILITY).map(PillarmanUtilCap::getHamonUser).orElse(false));
            cir.setReturnValue(power.getUser().getCapability(PillarmanUtilProvider.CAPABILITY).map(PillarmanUtilCap::getHamonUser).orElse(false));
        }
    }

    @Inject(method="onClear", at = @At("HEAD"))
    public void onClear(INonStandPower power, CallbackInfo ci) {
        LivingEntity user = power.getUser();
        System.out.println("soutAmerica");
        user.getCapability(PillarmanUtilProvider.CAPABILITY).ifPresent(cap -> cap.sethamonUser(false));
    }
}
