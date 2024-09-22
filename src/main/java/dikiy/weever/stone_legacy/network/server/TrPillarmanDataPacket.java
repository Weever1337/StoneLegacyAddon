package dikiy.weever.stone_legacy.network.server;

import com.github.standobyte.jojo.client.ClientUtil;
import com.github.standobyte.jojo.init.power.non_stand.ModPowers;
import com.github.standobyte.jojo.network.packets.IModPacketHandler;
import com.github.standobyte.jojo.power.impl.nonstand.INonStandPower;
import dikiy.weever.stone_legacy.capability.PillarmanUtilProvider;
import dikiy.weever.stone_legacy.capability.ZombieUtilProvider;
import dikiy.weever.stone_legacy.mixin.PillarmanDataMixin;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
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

                entity.getCapability(PillarmanUtilProvider.CAPABILITY).ifPresent(cap -> cap.sethamonUser(msg.hamonUserState));
            }
        }

        @Override
        public Class<TrPillarmanDataPacket> getPacketClass() {
            return TrPillarmanDataPacket.class;
        }
    }
}
