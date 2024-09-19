package dikiy.weever.stone_legacy.capability;

import dikiy.weever.stone_legacy.network.AddonPackets;
import dikiy.weever.stone_legacy.network.server.TrSetOwnerUUIDPacket;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraftforge.common.util.INBTSerializable;

import java.util.UUID;

public class ZombieUtilCap implements INBTSerializable<CompoundNBT> {
    private final LivingEntity entity;
    private UUID ownerUUID;

    public ZombieUtilCap(LivingEntity entity) {
        this.entity = entity;
    }


    public void setOwnerUUID(UUID ownerUUID) {
        this.ownerUUID = ownerUUID;
        if (!entity.level.isClientSide()) {
            AddonPackets.sendToClientsTrackingAndSelf(new TrSetOwnerUUIDPacket(entity.getId(), ownerUUID), entity);
        }
    }

    public UUID getOwnerUUID() {
        return this.ownerUUID;
    }

    public void syncWithAnyPlayer(ServerPlayerEntity player) {
        if (this.ownerUUID != null)
            AddonPackets.sendToClient(new TrSetOwnerUUIDPacket(entity.getId(), ownerUUID), player);
    }

    public void syncWithEntityOnly(ServerPlayerEntity player) {
//        AddonPackets.sendToClient(new TrSetOwnerUUIDPacket(entity.getId(), ownerUUID), player);
    }

    @Override
    public CompoundNBT serializeNBT() {
        CompoundNBT nbt = new CompoundNBT();
        if(this.ownerUUID != null)
            nbt.putUUID("ownerUUID", this.ownerUUID);
        return nbt;
    }

    @Override
    public void deserializeNBT(CompoundNBT compoundNBT) {
        if (compoundNBT.hasUUID("ownerUUID"))
            this.ownerUUID = compoundNBT.getUUID("ownerUUID");
    }
}
