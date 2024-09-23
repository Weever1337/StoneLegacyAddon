package dikiy.weever.stone_legacy.capability;

import net.minecraft.entity.LivingEntity;
import net.minecraft.nbt.INBT;
import net.minecraft.util.Direction;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;
import net.minecraftforge.common.util.LazyOptional;

public class PillarmanUtilProvider implements ICapabilitySerializable<INBT> {
    @CapabilityInject(PillarmanUtilCap.class)
    public static Capability<PillarmanUtilCap> CAPABILITY = null;
    private LazyOptional<PillarmanUtilCap> instance;

    public PillarmanUtilProvider(LivingEntity player) {
        this.instance = LazyOptional.of(() -> new PillarmanUtilCap(player));
    }

    @Override
    public <T> LazyOptional<T> getCapability(Capability<T> cap, Direction side) {
        return CAPABILITY.orEmpty(cap, instance);
    }

    @Override
    public INBT serializeNBT() {
        return CAPABILITY.getStorage().writeNBT(CAPABILITY, instance.orElseThrow(
                () -> new IllegalArgumentException("Player capability LazyOptional is not attached.")), null);
    }

    @Override
    public void deserializeNBT(INBT nbt) {
        CAPABILITY.getStorage().readNBT(CAPABILITY, instance.orElseThrow(
                () -> new IllegalArgumentException("Player capability LazyOptional is not attached.")), null, nbt);
    }
}