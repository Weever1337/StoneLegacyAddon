package dikiy.weever.stone_legacy.network.server;

import com.github.standobyte.jojo.client.ClientUtil;
import com.github.standobyte.jojo.network.packets.IModPacketHandler;
import dikiy.weever.stone_legacy.capability.ZombieUtilProvider;
import net.minecraft.entity.Entity;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.UUID;
import java.util.function.Supplier;

public class TrSetOwnerUUIDPacket {
    private final int entityId;
    private final UUID set;

    public TrSetOwnerUUIDPacket(int entityId, UUID set) {
        this.entityId = entityId;
        this.set = set;
    }

    public static class Handler implements IModPacketHandler<TrSetOwnerUUIDPacket> {

        @Override
        public void encode(TrSetOwnerUUIDPacket msg, PacketBuffer buf) {
            buf.writeInt(msg.entityId);
            buf.writeUUID(msg.set);
        }

        @Override
        public TrSetOwnerUUIDPacket decode(PacketBuffer buf) {
            int entityId = buf.readInt();
            return new TrSetOwnerUUIDPacket(entityId, buf.readUUID());
        }

        @Override
        public void handle(TrSetOwnerUUIDPacket msg, Supplier<NetworkEvent.Context> ctx) {
            Entity entity = ClientUtil.getEntityById(msg.entityId);
            if (entity != null) {
                entity.getCapability(ZombieUtilProvider.CAPABILITY).ifPresent(cap -> cap.setOwnerUUID(msg.set));
            }
        }

        @Override
        public Class<TrSetOwnerUUIDPacket> getPacketClass() {
            return TrSetOwnerUUIDPacket.class;
        }
    }
}
