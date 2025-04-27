package dikiy.weever.stone_legacy.mixin;

import com.github.standobyte.jojo.init.power.JojoCustomRegistries;
import com.github.standobyte.jojo.power.IPowerType;
import com.github.standobyte.jojo.power.impl.nonstand.TypeSpecificData;
import com.github.standobyte.jojo.power.impl.nonstand.type.NonStandPowerType;
import com.github.standobyte.jojo.power.impl.nonstand.type.zombie.ZombieData;
import dikiy.weever.stone_legacy.mixin_helper.IZombieDataMixinHelper;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.registries.IForgeRegistry;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

import javax.annotation.Nullable;

@Mixin(value = ZombieData.class, remap = false)
public abstract class ZombieDataMixin extends TypeSpecificData implements IZombieDataMixinHelper {
    @Unique
    private TypeSpecificData stoneLegacyAddon$oldData;
    @Unique
    private NonStandPowerType<?> stoneLegacyAddon$previousPowerType;

    @Inject(method = "onPowerGiven", at = @At("HEAD"), remap = false)
    public void saveOldData(NonStandPowerType<?> oldType, TypeSpecificData oldData, CallbackInfo ci) {
        this.stoneLegacyAddon$previousPowerType = oldType;
        this.stoneLegacyAddon$oldData = oldData;
    }

    @Inject(method = "writeNBT", at = @At(value = "INVOKE", shift = At.Shift.AFTER, target = "Lnet/minecraft/nbt/CompoundNBT;putBoolean(Ljava/lang/String;Z)V"), locals = LocalCapture.CAPTURE_FAILEXCEPTION, remap = false)
    public void writeOldData(CallbackInfoReturnable<CompoundNBT> ci, CompoundNBT nbt) {
        if (stoneLegacyAddon$previousPowerType != null)
            nbt.putString("PreviousPowerType", JojoCustomRegistries.NON_STAND_POWERS.getKeyAsString(stoneLegacyAddon$previousPowerType));
        if (stoneLegacyAddon$oldData != null) nbt.put("OldData", stoneLegacyAddon$getPreviousDataNbt());
    }

    @Inject(method = "readNBT", at = @At(value = "TAIL"), remap = false)
    public void readOldData(CompoundNBT nbt, CallbackInfo ci) {
        IForgeRegistry<NonStandPowerType<?>> powerTypeRegistry = JojoCustomRegistries.NON_STAND_POWERS.getRegistry();
        String powerName = nbt.getString("PreviousPowerType");
        if (!powerName.equals(IPowerType.NO_POWER_NAME)) {
            stoneLegacyAddon$previousPowerType = powerTypeRegistry.getValue(new ResourceLocation(powerName));
            if (stoneLegacyAddon$previousPowerType != null) {
                CompoundNBT oldDataNBT = nbt.getCompound("PreviousData");
                stoneLegacyAddon$oldData = stoneLegacyAddon$previousPowerType.newSpecificDataInstance();
                stoneLegacyAddon$oldData.readNBT(oldDataNBT);
            }
        }
    }

    @Override
    @Unique
    public CompoundNBT stoneLegacyAddon$getPreviousDataNbt() {
        if (this.stoneLegacyAddon$oldData != null) {
            return stoneLegacyAddon$oldData.writeNBT();
        }
        return null;
    }

    @Override
    @Nullable
    @Unique
    public TypeSpecificData stoneLegacyAddon$getPreviousData() {
        return stoneLegacyAddon$oldData;
    }

    @Override
    @Unique
    public NonStandPowerType<?> stoneLegacyAddon$getPreviousPowerType() {
        return this.stoneLegacyAddon$previousPowerType;
    }
}
