package fracture.mod.util.handlers;

import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class SlideCancelPacket implements IMessage {

    public SlideCancelPacket() {}

    @Override
    public void fromBytes(ByteBuf buf) {}

    @Override
    public void toBytes(ByteBuf buf) {}

    public static class Handler implements IMessageHandler<SlideCancelPacket, IMessage> {
        @Override
        public IMessage onMessage(SlideCancelPacket message, MessageContext ctx) {
            EntityPlayerMP player = ctx.getServerHandler().player;
            player.server.addScheduledTask(() -> {
                String currentState = player.getEntityData().getString("diveState");
                
                if ("SLIDING".equals(currentState)) {
                    player.getEntityData().setString("diveState", "NONE");
                    player.getEntityData().setInteger("diveCount", 0);
                }
            });
            return null;
        }
    }
}