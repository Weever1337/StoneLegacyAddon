package dikiy.weever.stone_legacy.command;

import com.github.standobyte.jojo.init.power.non_stand.ModPowers;
import com.github.standobyte.jojo.power.impl.nonstand.INonStandPower;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.exceptions.SimpleCommandExceptionType;
import dikiy.weever.stone_legacy.capability.ZombieUtilProvider;
import dikiy.weever.stone_legacy.mixin_helper.IWaitableEntity;
import dikiy.weever.stone_legacy.mixin_helper.IZombiesReminder;
import net.minecraft.command.CommandSource;
import net.minecraft.command.Commands;
import net.minecraft.command.arguments.EntityArgument;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.util.text.TranslationTextComponent;

import java.util.Collection;

public class ZombieOwnerCommand {
    private static SimpleCommandExceptionType ERROR_ENTITY_FAIL = new SimpleCommandExceptionType(new TranslationTextComponent("commands.jojozombie.entity.failed"));

    public static void register(CommandDispatcher<CommandSource> dispatcher) {
        dispatcher.register(Commands.literal("jojozombie").requires(ctx -> ctx.hasPermission(2))
                .then(Commands.argument("targets", EntityArgument.entities())
                .then(Commands.literal("setowner")
                .then(Commands.argument("owner", EntityArgument.player())
                    .executes(ctx -> setOwner(ctx.getSource(), EntityArgument.getEntities(ctx, "targets"), EntityArgument.getPlayer(ctx, "owner")))))));
    }

    private static int setOwner(CommandSource source, Collection<? extends Entity> targets, ServerPlayerEntity owner) throws CommandSyntaxException {
        int i = 0;
        for (Entity entity : targets) {
            if (entity instanceof LivingEntity) {
                if (entity instanceof IWaitableEntity) {
                    LivingEntity oldOwner = ((IWaitableEntity) entity).getOwner();
                    INonStandPower.getNonStandPowerOptional(oldOwner).ifPresent(power -> power.getTypeSpecificData(ModPowers.VAMPIRISM.get()).ifPresent(data -> {
                        if (data instanceof IZombiesReminder) {
                            ((IZombiesReminder) data).removeZombie((LivingEntity) entity);
                        }
                    }));
                    ((IWaitableEntity) entity).setOwner(owner);
                    INonStandPower.getNonStandPowerOptional(owner).ifPresent(power -> power.getTypeSpecificData(ModPowers.VAMPIRISM.get()).ifPresent(data -> {
                        if (data instanceof IZombiesReminder) {
                            ((IZombiesReminder) data).addZombie((LivingEntity) entity);
                        }
                    }));
                    ++i;
                } else {
                    entity.getCapability(ZombieUtilProvider.CAPABILITY).ifPresent(cap -> {
                        LivingEntity oldOwner = (LivingEntity) source.getLevel().getEntity(cap.getOwnerUUID());
                        if (oldOwner != null) {
                            INonStandPower.getNonStandPowerOptional(oldOwner).ifPresent(power -> power.getTypeSpecificData(ModPowers.VAMPIRISM.get()).ifPresent(data -> {
                                if (data instanceof IZombiesReminder) {
                                    ((IZombiesReminder) data).removeZombie((LivingEntity) entity);
                                }
                            }));
                        }
                        cap.setOwnerUUID(owner.getUUID());
                        INonStandPower.getNonStandPowerOptional(owner).ifPresent(power -> power.getTypeSpecificData(ModPowers.VAMPIRISM.get()).ifPresent(data -> {
                            if (data instanceof IZombiesReminder) {
                                ((IZombiesReminder) data).addZombie((LivingEntity) entity);
                            }
                        }));
                    });
                    ++i;
                }
            }
        }

        if (i == 0) {
            throw ERROR_ENTITY_FAIL.create();
        } else {
            if (i == 1) {
                source.sendSuccess(new TranslationTextComponent("commands.jojozombie.setowner.success.single", owner.getDisplayName(), targets.iterator().next().getDisplayName()), true);
            } else {
                source.sendSuccess(new TranslationTextComponent("commands.jojozombie.setowner.success.multiple", owner.getDisplayName(), i), true);
            }
        }

        return i;
    }
}
