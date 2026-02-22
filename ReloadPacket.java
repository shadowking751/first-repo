package com.yourname.gunmod.network;

import com.yourname.gunmod.item.GunItem;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class ReloadPacket {

    public ReloadPacket() {}

    public ReloadPacket(FriendlyByteBuf buf) {}

    public void toBytes(FriendlyByteBuf buf) {}

    public boolean handle(Supplier<NetworkEvent.Context> supplier) {

        NetworkEvent.Context ctx = supplier.get();
        ctx.enqueueWork(() -> {

            ServerPlayer player = ctx.getSender();
            if (player == null) return;

            ItemStack held = player.getMainHandItem();

            if (held.getItem() instanceof GunItem) {
                GunItem.tryManualReload(player, held);
            }
        });

        return true;
    }
}
