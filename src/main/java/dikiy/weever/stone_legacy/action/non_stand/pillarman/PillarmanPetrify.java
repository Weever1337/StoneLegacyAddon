package dikiy.weever.stone_legacy.action.non_stand.pillarman;

import com.github.standobyte.jojo.action.ActionConditionResult;
import com.github.standobyte.jojo.action.ActionTarget;
import com.github.standobyte.jojo.action.non_stand.PillarmanStoneForm;
import com.github.standobyte.jojo.power.impl.nonstand.INonStandPower;
import dikiy.weever.stone_legacy.util.PetrifyHandler;
import dikiy.weever.stone_legacy.util.StoneLegacyUtil;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;

import java.util.List;

public class PillarmanPetrify extends PillarmanStoneForm {
    public PillarmanPetrify(Builder builder) {
        super(builder);
    }

    @Override
    public ActionConditionResult checkSpecificConditions(LivingEntity user, INonStandPower power, ActionTarget target) {
        return ActionConditionResult.noMessage(StoneLegacyUtil.checkStoneFormationBehind(user));
    }

    @Override
    public void appendWarnings(List<ITextComponent> warnings, INonStandPower power, PlayerEntity clientPlayerUser) {
        warnings.add(new TranslationTextComponent("stone_legacy.pillarman_petrify.warning"));
    }

    @Override
    protected void perform(World world, LivingEntity user, INonStandPower power, ActionTarget target) {
        if (user instanceof ServerPlayerEntity && !world.isClientSide() && world instanceof ServerWorld) {
            ServerPlayerEntity player = (ServerPlayerEntity) user;
            ServerWorld serverWorld = (ServerWorld) world;

            StoneLegacyUtil.replaceStoneWithSlumberingPillarman(player);

            double x = player.getX();
            double y = player.getY() + player.getBbHeight() / 2.0;
            double z = player.getZ();
            serverWorld.sendParticles(ParticleTypes.CAMPFIRE_COSY_SMOKE, x, y, z, 40, 0.4, 0.8, 0.4, 0.02);

            StoneLegacyUtil.resetToRespawnPoint(player);
            PetrifyHandler.addPetrifyUser(player.getUUID(), System.currentTimeMillis() + 1000);
        }
    }

    @Override
    public boolean greenSelection(INonStandPower power, ActionConditionResult conditionCheck) {
        return false;
    }
}