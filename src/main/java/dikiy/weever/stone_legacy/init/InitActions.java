package dikiy.weever.stone_legacy.init;

import com.github.standobyte.jojo.action.Action;
import com.github.standobyte.jojo.action.non_stand.NonStandAction;
import com.github.standobyte.jojo.action.non_stand.PillarmanAction;
import com.github.standobyte.jojo.action.non_stand.VampirismAction;
import com.github.standobyte.jojo.init.power.non_stand.pillarman.ModPillarmanActions;
import com.github.standobyte.jojo.init.power.non_stand.vampirism.ModVampirismActions;
import com.github.standobyte.jojo.power.impl.nonstand.type.NonStandPowerType;
import dikiy.weever.stone_legacy.StoneLegacyAddon;
import dikiy.weever.stone_legacy.action.non_stand.pillarman.PillarmanHamonSuicide;
import dikiy.weever.stone_legacy.action.non_stand.pillarman.PillarmanPetrify;
import dikiy.weever.stone_legacy.action.non_stand.vampirism.VampirismRecallZombies;
import dikiy.weever.stone_legacy.action.non_stand.vampirism.VampirismWaitOrder;
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


    public static final RegistryObject<VampirismAction> VAMPIRISM_RECALL = ACTIONS.register("vampirism_recall",
            () -> new VampirismRecallZombies(new NonStandAction.Builder()
                    .cooldown(60)
                    .shiftVariationOf(ModVampirismActions.VAMPIRISM_ZOMBIE_SUMMON)));

    public static final RegistryObject<VampirismAction> VAMPIRISM_WAIT_ORDER = ACTIONS.register("vampirism_wait_order",
            () -> new VampirismWaitOrder(new NonStandAction.Builder()
                    .cooldown(30)
                    .shiftVariationOf(ModVampirismActions.VAMPIRISM_DARK_AURA)));
}
