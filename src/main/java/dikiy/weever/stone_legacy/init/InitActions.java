package dikiy.weever.stone_legacy.init;

import com.github.standobyte.jojo.action.Action;
import com.github.standobyte.jojo.action.non_stand.HamonLifeMagnetism;
import com.github.standobyte.jojo.action.non_stand.NonStandAction;
import com.github.standobyte.jojo.action.non_stand.PillarmanAction;
import com.github.standobyte.jojo.action.non_stand.PillarmanRibsBlades;
import com.github.standobyte.jojo.init.power.non_stand.pillarman.ModPillarmanActions;
import com.github.standobyte.jojo.power.impl.nonstand.type.NonStandPowerType;
import dikiy.weever.stone_legacy.StoneLegacyAddon;
import dikiy.weever.stone_legacy.action.non_stand.pillarman.PillarmanHamonSuicide;
import dikiy.weever.stone_legacy.action.non_stand.pillarman.PillarmanPetrify;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;

public class InitActions {
    public static final DeferredRegister<NonStandPowerType<?>> NON_STAND_POWERS = DeferredRegister.create(
            (Class<NonStandPowerType<?>>) ((Class<?>) NonStandPowerType.class), StoneLegacyAddon.MOD_ID);
    public static final DeferredRegister<Action<?>> ACTIONS = DeferredRegister.create(
            (Class<Action<?>>) ((Class<?>) Action.class), StoneLegacyAddon.MOD_ID);

    public static final RegistryObject<PillarmanAction> PILLARMAN_PETRIFY = ACTIONS.register("pillarman_petrify",
            () -> new PillarmanPetrify(new NonStandAction.Builder()
                    .holdToFire(40, false)
                    .heldWalkSpeed(0.0F)
                    .shiftVariationOf(ModPillarmanActions.PILLARMAN_STONE_FORM)
                    .ignoresPerformerStun()));

    public static final RegistryObject<PillarmanAction> PILLARMAN_HAMON_SUICIDE = ACTIONS.register("pillarman_hamon_suicide",
            () -> new PillarmanHamonSuicide(new NonStandAction.Builder().holdToFire(100, false).ignoresPerformerStun()));
}
