package dikiy.weever.stone_legacy.mixin;

import com.github.standobyte.jojo.advancements.ModCriteriaTriggers;
import com.github.standobyte.jojo.init.ModSounds;
import com.github.standobyte.jojo.init.power.non_stand.ModPowers;
import com.github.standobyte.jojo.potion.BleedingEffect;
import com.github.standobyte.jojo.potion.IApplicableEffect;
import com.github.standobyte.jojo.power.impl.nonstand.INonStandPower;
import com.github.standobyte.jojo.power.impl.nonstand.type.pillarman.PillarmanData;
import com.github.standobyte.jojo.util.mc.damage.DamageUtil;
import dikiy.weever.stone_legacy.items.PredictedMaskItem;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.Effect;
import net.minecraft.potion.EffectType;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Optional;

@Mixin(value = BleedingEffect.class, remap = false)
public class BleedingEffectMixin extends Effect implements IApplicableEffect {
    protected BleedingEffectMixin(EffectType type, int amp) { super(type, amp); }
    @Shadow @Override
    public boolean isApplicable(LivingEntity entity) { return false; }
    @Shadow private static void applyMaskEffect(LivingEntity entity, ItemStack headStack) {}

    @Inject(method = "applyStoneMask", at = @At(value = "INVOKE", target = "Lcom/github/standobyte/jojo/power/impl/nonstand/INonStandPower;getNonStandPowerOptional(Lnet/minecraft/entity/LivingEntity;)Lnet/minecraftforge/common/util/LazyOptional;"), cancellable = true, remap = false)
    private static void applyPredictedMask(LivingEntity entity, ItemStack headStack, CallbackInfoReturnable<Boolean> cir) {
        INonStandPower.getNonStandPowerOptional(entity).ifPresent(power -> {
            Optional<PillarmanData> pillarmanOptional = power.getTypeSpecificData(ModPowers.PILLAR_MAN.get());
            if (headStack.getItem() instanceof PredictedMaskItem) {
                if (!pillarmanOptional.isPresent()) {
                    if (entity instanceof ServerPlayerEntity) {
                        ModCriteriaTriggers.MASK_SUICIDE.get().trigger((ServerPlayerEntity) entity);
                    }
                    entity.hurt(DamageUtil.STONE_MASK, entity.getMaxHealth());
                    cir.setReturnValue(true);
                    cir.cancel();
                } else {
                    PillarmanData pillarman = pillarmanOptional.get();
                    if (pillarmanOptional.get().getEvolutionStage() < 3) {
                        pillarman.setEvolutionStage(3);
                        //Gives a random Mode
                        switch (((PredictedMaskItem) headStack.getItem()).getMode()) {
                            case WIND:
                                pillarman.setMode(PillarmanData.Mode.WIND);
                                entity.level.playSound(null, entity, ModSounds.PILLAR_MAN_WIND_MODE.get(), entity.getSoundSource(), 1.0F, 1.0F);
                                break;
                            case HEAT:
                                pillarman.setMode(PillarmanData.Mode.HEAT);
                                entity.level.playSound(null, entity, ModSounds.PILLAR_MAN_HEAT_MODE.get(), entity.getSoundSource(), 1.0F, 1.0F);
                                break;
                            case LIGHT:
                                pillarman.setMode(PillarmanData.Mode.LIGHT);
                                entity.level.playSound(null, entity, ModSounds.PILLAR_MAN_LIGHT_MODE.get(), entity.getSoundSource(), 1.0F, 1.0F);
                                break;
                        }
                        applyMaskEffect(entity, headStack);
                        cir.setReturnValue(true);
                        cir.cancel();
                    }
                }
            }
        });
    }
}
