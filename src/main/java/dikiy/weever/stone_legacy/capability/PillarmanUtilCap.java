package dikiy.weever.stone_legacy.capability;

import dikiy.weever.stone_legacy.network.AddonPackets;
import dikiy.weever.stone_legacy.network.server.TrPillarmanDataPacket;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraftforge.common.util.INBTSerializable;


public class PillarmanUtilCap implements INBTSerializable<CompoundNBT> {
    private final LivingEntity entity;
    private boolean hamonUser = false;

    public PillarmanUtilCap(LivingEntity entity) {
        this.entity = entity;
    }

    public void sethamonUser(boolean hamonUser) {
        this.hamonUser = hamonUser;
        if (!entity.level.isClientSide()) {
            AddonPackets.sendToClientsTrackingAndSelf(new TrPillarmanDataPacket(entity.getId(), hamonUser), entity);
        }
    }

    public boolean getHamonUser() {
        return this.hamonUser;
    }

    public void syncWithAnyPlayer(ServerPlayerEntity player) {
        AddonPackets.sendToClient(new TrPillarmanDataPacket(entity.getId(), hamonUser), player);
    }

    public void onTracking(ServerPlayerEntity tracking) {
        AddonPackets.sendToClient(new TrPillarmanDataPacket(entity.getId(), hamonUser), tracking);
    }

    public void syncWithClient() {
        if (entity instanceof ServerPlayerEntity) {
            ServerPlayerEntity player = (ServerPlayerEntity) entity;
            AddonPackets.sendToClient(new TrPillarmanDataPacket(entity.getId(), hamonUser), player);
        }
    }

    @Override
    public CompoundNBT serializeNBT() {
        CompoundNBT nbt = new CompoundNBT();
        nbt.putBoolean("hamonUser", this.hamonUser);
        return nbt;
    }

    @Override
    public void deserializeNBT(CompoundNBT compoundNBT) {
        this.hamonUser = compoundNBT.getBoolean("hamonUser");
    }
}
