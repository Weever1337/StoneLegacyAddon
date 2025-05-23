package dikiy.weever.stone_legacy.network;

import com.github.standobyte.jojo.network.packets.IModPacketHandler;
import dikiy.weever.stone_legacy.StoneLegacyAddon;
import dikiy.weever.stone_legacy.network.server.TrPillarmanDataPacket;
import dikiy.weever.stone_legacy.network.server.TrResetDeathTimePacket;
import dikiy.weever.stone_legacy.network.server.TrSetOwnerUUIDPacket;
import dikiy.weever.stone_legacy.network.server.TrSyncGivePowerDataPacket;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.util.FakePlayer;
import net.minecraftforge.fml.network.NetworkDirection;
import net.minecraftforge.fml.network.NetworkRegistry;
import net.minecraftforge.fml.network.PacketDistributor;
import net.minecraftforge.fml.network.simple.SimpleChannel;

import java.util.Optional;

public class AddonPackets {
    private static final String PROTOCOL_VERSION = "1";
    private static SimpleChannel channel;
    private static int packetIndex;

    public static void init() {
        channel = NetworkRegistry.ChannelBuilder
                .named(new ResourceLocation(StoneLegacyAddon.MOD_ID, "main_channel"))
                .clientAcceptedVersions(PROTOCOL_VERSION::equals)
                .serverAcceptedVersions(PROTOCOL_VERSION::equals)
                .networkProtocolVersion(() -> PROTOCOL_VERSION)
                .simpleChannel();

        registerMessage(channel, new TrSetOwnerUUIDPacket.Handler(), Optional.of(NetworkDirection.PLAY_TO_CLIENT));
        registerMessage(channel, new TrPillarmanDataPacket.Handler(), Optional.of(NetworkDirection.PLAY_TO_CLIENT));
        registerMessage(channel, new TrSyncGivePowerDataPacket.Handler(), Optional.of(NetworkDirection.PLAY_TO_CLIENT));
        registerMessage(channel, new TrResetDeathTimePacket.Handler(), Optional.of(NetworkDirection.PLAY_TO_CLIENT));
    }

    private static <MSG> void registerMessage(SimpleChannel channel, IModPacketHandler<MSG> handler, Optional<NetworkDirection> networkDirection) {
        if (packetIndex > 127) {
            throw new IllegalStateException("Too many packets (> 127) registered for a single channel!");
        }
        channel.registerMessage(packetIndex++, handler.getPacketClass(), handler::encode, handler::decode, handler::enqueueHandleSetHandled, networkDirection);
    }

    public static void sendToClient(Object msg, ServerPlayerEntity player) {
        if (!(player instanceof FakePlayer)) {
            channel.send(PacketDistributor.PLAYER.with(() -> player), msg);
        }
    }

    public static void sendToServer(Object msg) {
        channel.sendToServer(msg);
    }

    public static void sendToClientsTracking(Object msg, Entity entity) {
        channel.send(PacketDistributor.TRACKING_ENTITY.with(() -> entity), msg);
    }

    public static void sendToClientsTrackingAndSelf(Object msg, Entity entity) {
        channel.send(PacketDistributor.TRACKING_ENTITY_AND_SELF.with(() -> entity), msg);
    }
}
