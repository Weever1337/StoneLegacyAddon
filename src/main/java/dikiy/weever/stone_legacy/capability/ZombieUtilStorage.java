package dikiy.weever.stone_legacy.capability;

import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.INBT;
import net.minecraft.util.Direction;
import net.minecraftforge.common.capabilities.Capability;

public class ZombieUtilStorage implements Capability.IStorage<ZombieUtilCap> {

    @Override
    public INBT writeNBT(Capability<ZombieUtilCap> capability, ZombieUtilCap instance, Direction side) {
        return instance.serializeNBT();
    }

    @Override
    public void readNBT(Capability<ZombieUtilCap> capability, ZombieUtilCap instance, Direction side, INBT nbt) {
        instance.deserializeNBT((CompoundNBT) nbt);
    }
}