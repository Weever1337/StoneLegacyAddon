package dikiy.weever.stone_legacy.mixin;

import com.github.standobyte.jojo.power.impl.nonstand.TypeSpecificData;
import com.github.standobyte.jojo.power.impl.nonstand.type.hamon.HamonData;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(HamonData.class)
public abstract class HamonDataMixin extends TypeSpecificData {
    @Inject(method = "setExerciseTicks", at = @At("HEAD"), cancellable = true)
    public void setExerciseTicks(int[] ticks, boolean clientSide, CallbackInfo ci) {
        if (power == null) ci.cancel();
    }
}
