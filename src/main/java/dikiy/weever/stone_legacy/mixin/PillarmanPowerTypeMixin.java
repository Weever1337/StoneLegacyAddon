package dikiy.weever.stone_legacy.mixin;

import com.github.standobyte.jojo.action.Action;
import com.github.standobyte.jojo.action.non_stand.PillarmanAction;
import com.github.standobyte.jojo.client.controls.ControlScheme;
import com.github.standobyte.jojo.init.power.non_stand.pillarman.ModPillarmanActions;
import com.github.standobyte.jojo.power.impl.nonstand.INonStandPower;
import com.github.standobyte.jojo.power.impl.nonstand.type.NonStandPowerType;
import com.github.standobyte.jojo.power.impl.nonstand.type.pillarman.PillarmanData;
import com.github.standobyte.jojo.power.impl.nonstand.type.pillarman.PillarmanPowerType;
import dikiy.weever.stone_legacy.capability.PillarmanUtilCap;
import dikiy.weever.stone_legacy.capability.PillarmanUtilProvider;
import dikiy.weever.stone_legacy.init.InitActions;
import net.minecraft.entity.LivingEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;

import java.util.function.Supplier;

@Mixin(PillarmanPowerType.class)
public abstract class PillarmanPowerTypeMixin extends NonStandPowerType<PillarmanData> {
    public PillarmanPowerTypeMixin(Action<INonStandPower>[] startingAttacks, Action<INonStandPower>[] startingAbilities, Action<INonStandPower> defaultQuickAccess, Supplier<PillarmanData> dataFactory) {
        super(startingAttacks, startingAbilities, defaultQuickAccess, dataFactory);
    }

//    @Inject(method = "clAddMissingActions*", at = @At("HEAD"))
/**
 * @author
 * @reason
 */
    @Overwrite
    public void clAddMissingActions(ControlScheme controlScheme, INonStandPower power) {
        super.clAddMissingActions(controlScheme, power);
        LivingEntity user = power.getUser();
        if (user.getCapability(PillarmanUtilProvider.CAPABILITY).map(PillarmanUtilCap::getHamonUser).orElse(false)) {
            controlScheme.addIfMissing(ControlScheme.Hotbar.RIGHT_CLICK, InitActions.PILLARMAN_HAMON_SUICIDE.get());
        }
        PillarmanData pillarman = power.getTypeSpecificData(this).get();

        if (pillarman.getEvolutionStage() > 1) {
            controlScheme.addIfMissing(ControlScheme.Hotbar.LEFT_CLICK, ModPillarmanActions.PILLARMAN_ABSORPTION.get());
            controlScheme.addIfMissing(ControlScheme.Hotbar.LEFT_CLICK, ModPillarmanActions.PILLARMAN_HORN_ATTACK.get());
            controlScheme.addIfMissing(ControlScheme.Hotbar.LEFT_CLICK, ModPillarmanActions.PILLARMAN_RIBS_BLADES.get());
            controlScheme.addIfMissing(ControlScheme.Hotbar.RIGHT_CLICK, ModPillarmanActions.PILLARMAN_REGENERATION.get());
            controlScheme.addIfMissing(ControlScheme.Hotbar.RIGHT_CLICK, ModPillarmanActions.PILLARMAN_ENHANCED_SENSES.get());
            controlScheme.addIfMissing(ControlScheme.Hotbar.RIGHT_CLICK, ModPillarmanActions.PILLARMAN_UNNATURAL_AGILITY.get());
        }
        switch (pillarman.getMode()) {
            case WIND:
                controlScheme.addIfMissing(ControlScheme.Hotbar.LEFT_CLICK, ModPillarmanActions.PILLARMAN_SMALL_SANDSTORM.get());
                controlScheme.addIfMissing(ControlScheme.Hotbar.LEFT_CLICK, ModPillarmanActions.PILLARMAN_ATMOSPHERIC_RIFT.get());
                controlScheme.addIfMissing(ControlScheme.Hotbar.RIGHT_CLICK, ModPillarmanActions.PILLARMAN_WIND_CLOAK.get());
                break;
            case HEAT:
                controlScheme.addIfMissing(ControlScheme.Hotbar.LEFT_CLICK, ModPillarmanActions.PILLARMAN_ERRATIC_BLAZE_KING.get());
                controlScheme.addIfMissing(ControlScheme.Hotbar.LEFT_CLICK, ModPillarmanActions.PILLARMAN_GIANT_CARTHWHEEL_PRISON.get());
                controlScheme.addIfMissing(ControlScheme.Hotbar.LEFT_CLICK, ModPillarmanActions.PILLARMAN_SELF_DETONATION.get());
                break;
            case LIGHT:
                controlScheme.addIfMissing(ControlScheme.Hotbar.RIGHT_CLICK, ModPillarmanActions.PILLARMAN_LIGHT_FLASH.get());
                controlScheme.addIfMissing(ControlScheme.Hotbar.LEFT_CLICK, ModPillarmanActions.PILLARMAN_BLADE_DASH_ATTACK.get());
                controlScheme.addIfMissing(ControlScheme.Hotbar.LEFT_CLICK, ModPillarmanActions.PILLARMAN_BLADE_BARRAGE.get());
                break;
            default:
                break;
        }
    }

    /**
     * @author
     * @reason
     */
    @Overwrite
    public boolean isActionLegalInHud(Action<INonStandPower> action, INonStandPower power) {
        if (super.isActionLegalInHud(action, power)) {
            return true;
        }

        if (action instanceof PillarmanAction) {
            PillarmanAction pmAction = (PillarmanAction) action;
            PillarmanData pillarman = power.getTypeSpecificData(this).get();
            return (pmAction.getPillarManStage() == -1 || pmAction.getPillarManStage() <= pillarman.getEvolutionStage())
                    && (pmAction.getPillarManMode() == PillarmanData.Mode.NONE || pmAction.getPillarManMode() == pillarman.getMode()
            || (pmAction == InitActions.PILLARMAN_HAMON_SUICIDE.get() && power.getUser().getCapability(PillarmanUtilProvider.CAPABILITY).map(PillarmanUtilCap::getHamonUser).orElse(false)));
        }

        return false;
    }
}
