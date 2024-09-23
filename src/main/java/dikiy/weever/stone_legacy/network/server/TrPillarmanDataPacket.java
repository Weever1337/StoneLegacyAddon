package dikiy.weever.stone_legacy.network.server;

import com.github.standobyte.jojo.client.ClientUtil;
import com.github.standobyte.jojo.network.packets.IModPacketHandler;
import dikiy.weever.stone_legacy.capability.PillarmanUtilProvider;
import net.minecraft.entity.Entity;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.function.Supplier;

public class TrPillarmanDataPacket {
    private final int entityId;
    private final boolean hamonUserState;

    public TrPillarmanDataPacket(int entityId, boolean hamonUserState) {
        this.entityId = entityId;
        this.hamonUserState = hamonUserState;
    }

    public static class Handler implements IModPacketHandler<TrPillarmanDataPacket> {

        @Override
        public void encode(TrPillarmanDataPacket msg, PacketBuffer buf) {
            buf.writeInt(msg.entityId);
            buf.writeBoolean(msg.hamonUserState);
        }

        @Override
        public TrPillarmanDataPacket decode(PacketBuffer buf) {
            int entityId = buf.readInt();
            return new TrPillarmanDataPacket(entityId, buf.readBoolean());
        }

        @Override
        public void handle(TrPillarmanDataPacket msg, Supplier<NetworkEvent.Context> ctx) {
            Entity entity = ClientUtil.getEntityById(msg.entityId);
            if (entity != null) {

                entity.getCapability(PillarmanUtilProvider.CAPABILITY).ifPresent(cap -> cap.setHamonUser(msg.hamonUserState));
            }
        }

        @Override
        public Class<TrPillarmanDataPacket> getPacketClass() {
            return TrPillarmanDataPacket.class;
        }
    }
}
