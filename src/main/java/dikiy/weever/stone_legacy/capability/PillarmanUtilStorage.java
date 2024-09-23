package dikiy.weever.stone_legacy.capability;

import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.INBT;
import net.minecraft.util.Direction;
import net.minecraftforge.common.capabilities.Capability;

public class PillarmanUtilStorage implements Capability.IStorage<PillarmanUtilCap> {

    @Override
    public INBT writeNBT(Capability<PillarmanUtilCap> capability, PillarmanUtilCap instance, Direction side) {
        return instance.serializeNBT();
    }

    @Override
    public void readNBT(Capability<PillarmanUtilCap> capability, PillarmanUtilCap instance, Direction side, INBT nbt) {
        instance.deserializeNBT((CompoundNBT) nbt);
    }
}