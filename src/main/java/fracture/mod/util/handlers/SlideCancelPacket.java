package fracture.mod.util.handlers;

import io.netty.buffer.ByteBuf;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraft.entity.player.EntityPlayerMP;

/**
 * Empty packet used to tell the server "stop sliding now".
 */
public class SlideCancelPacket implements IMessage {
    public SlideCancelPacket() {}

    @Override
    public void fromBytes(ByteBuf buf) { }
    @Override
    public void toBytes(ByteBuf buf) { }

    public static class Handler implements IMessageHandler<SlideCancelPacket, IMessage> {
        @Override
        public IMessage onMessage(SlideCancelPacket msg, MessageContext ctx) {
            // Must schedule on main server thread
            ctx.getServerHandler().player.server.addScheduledTask(() -> {
                EntityPlayerMP player = ctx.getServerHandler().player;
                if (player == null) return;
                // Clear slide state and any slide timers
                player.getEntityData().setString("diveState", "NONE");
                player.getEntityData().setInteger("diveCount", 0);
                player.getEntityData().removeTag("slideTicks");
            });
            return null;
        }
    }
}