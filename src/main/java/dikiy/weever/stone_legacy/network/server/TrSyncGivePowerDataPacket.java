package dikiy.weever.stone_legacy.network.server;

import com.github.standobyte.jojo.client.ClientUtil;
import com.github.standobyte.jojo.init.power.JojoCustomRegistries;
import com.github.standobyte.jojo.network.packets.IModPacketHandler;
import com.github.standobyte.jojo.power.impl.nonstand.INonStandPower;
import com.github.standobyte.jojo.power.impl.nonstand.type.NonStandPowerType;
import dikiy.weever.stone_legacy.mixin_helper.INonStandPowerMixinHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.function.Supplier;

public class TrSyncGivePowerDataPacket {
    private final int entityId;
    private final ResourceLocation powerType;
    private final boolean force;

    public TrSyncGivePowerDataPacket(int entityId, ResourceLocation powerType, boolean force) {
        this.entityId = entityId;
        this.powerType = powerType;
        this.force = force;
    }

    public static class Handler implements IModPacketHandler<TrSyncGivePowerDataPacket> {

        @Override
        public void encode(TrSyncGivePowerDataPacket msg, PacketBuffer buf) {
            buf.writeInt(msg.entityId);
            buf.writeResourceLocation(msg.powerType);
            buf.writeBoolean(msg.force);
        }

        @Override
        public TrSyncGivePowerDataPacket decode(PacketBuffer buf) {
            int entityId = buf.readInt();
            return new TrSyncGivePowerDataPacket(entityId, buf.readResourceLocation(), buf.readBoolean());
        }

        @Override
        public void handle(TrSyncGivePowerDataPacket msg, Supplier<NetworkEvent.Context> ctx) {
            Entity entity = ClientUtil.getEntityById(msg.entityId);
            if (entity instanceof LivingEntity) {
                LivingEntity livingEntity = (LivingEntity) entity;
                INonStandPower.getNonStandPowerOptional(livingEntity).ifPresent(power -> {
                    NonStandPowerType<?> pipower = JojoCustomRegistries.NON_STAND_POWERS.getValue(msg.powerType);
                    if (pipower != null && power instanceof INonStandPowerMixinHelper) {
                        ((INonStandPowerMixinHelper) power).stoneLegacyAddon$givePower(pipower, msg.force);
                    }
                });
            }
        }

        @Override
        public Class<TrSyncGivePowerDataPacket> getPacketClass() {
            return TrSyncGivePowerDataPacket.class;
        }
    }
}
