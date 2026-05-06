package dikiy.weever.stone_legacy.mixin;

import com.github.standobyte.jojo.init.power.JojoCustomRegistries;
import com.github.standobyte.jojo.power.impl.nonstand.TypeSpecificData;
import com.github.standobyte.jojo.power.impl.nonstand.type.hamon.skill.CharacterHamonTechnique;
import com.github.standobyte.jojo.power.impl.nonstand.type.vampirism.VampirismData;
import com.github.standobyte.jojo.util.mc.MCUtil;
import dikiy.weever.stone_legacy.mixin_helper.IZombiesReminder;
import net.minecraft.entity.LivingEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.StringNBT;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.registries.IForgeRegistry;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Mixin(value = VampirismData.class, remap = false)
public abstract class VampirismZombiesReminderMixin extends TypeSpecificData implements IZombiesReminder {
    @Shadow boolean vampireFullPower;
    @Shadow int curingTicks;
    @Shadow boolean vampireHamonUser;
    @Shadow float hamonStrengthLevel;
    @Shadow Optional<CharacterHamonTechnique> hamonTechnique;

    @Unique
    private List<UUID> zombies = new ArrayList<>();
    @Unique
    public List<UUID> getOwnedZombies() {
        return zombies;
    }
    @Unique
    public void setOwnedZombies(List<UUID> list) {
        zombies = list;
    }
    @Unique
    public void addZombie(LivingEntity zombie) {
        zombies.add(zombie.getUUID());
    }
    @Unique
    public void removeZombie(LivingEntity zombie) {
        zombies.remove(zombie.getUUID());
    }
    @Unique
    public void removeZombie(UUID zombie) {
        zombies.remove(zombie);
    }

    /**
     * @author dikiytechies
     * @reason forge 1.16.5 sucks
     */
    @Overwrite
    public CompoundNBT writeNBT() {
        CompoundNBT nbt = new CompoundNBT();
        nbt.putBoolean("VampireFullPower", vampireFullPower);
        nbt.putInt("CuringTicks", curingTicks);

        nbt.putBoolean("VampireHamonUser", vampireHamonUser);
        if (vampireHamonUser) {
            nbt.putFloat("HamonStrength", hamonStrengthLevel);
            if (nbt.contains("CharacterTechnique", MCUtil.getNbtId(StringNBT.class))) {
                ResourceLocation techniqueId = new ResourceLocation(nbt.getString("CharacterTechnique"));
                IForgeRegistry<CharacterHamonTechnique> registry = JojoCustomRegistries.HAMON_CHARACTER_TECHNIQUES.getRegistry();
                if (registry.containsKey(techniqueId)) {
                    this.hamonTechnique = Optional.ofNullable(registry.getValue(techniqueId));
                }
            }
        }
        CompoundNBT zombiesNBT = new CompoundNBT();
        zombies.forEach(z -> zombiesNBT.putUUID("UUID", z));
        nbt.put("OwnedZombies", zombiesNBT);
        return nbt;
    }

    @Inject(method = "readNBT", at = @At("TAIL"))
    public void readNBT(CompoundNBT nbt, CallbackInfo ci) {
        for (int i = 0; i < nbt.getCompound("OwnedZombies").size(); i++)
            zombies.add(nbt.getCompound("OwnedZombies").getUUID("UUID"));
    }
}
