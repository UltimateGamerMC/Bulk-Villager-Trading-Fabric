package computer.brads.bulktrade;

import net.minecraft.client.Minecraft;
import net.minecraft.network.protocol.game.ServerboundSelectTradePacket;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.MerchantMenu;
import net.minecraft.world.item.trading.MerchantOffer;
import net.minecraft.world.item.trading.MerchantOffers;

public final class ClientRestockHelper {
    private ClientRestockHelper() {}

    public static boolean canRestock() {
        try {
            Minecraft c = Minecraft.getInstance();
            return c != null && c.getConnection() != null;
        } catch (Throwable e) {
            return false;
        }
    }

    public static void sendSelectTrade(int index) {
        try {
            Minecraft c = Minecraft.getInstance();
            if (c != null && c.getConnection() != null) {
                c.getConnection().send(new ServerboundSelectTradePacket(index));
            }
        } catch (Throwable ignored) {
        }
    }

    public static void scheduleRestock(int slot) {
        Minecraft c = Minecraft.getInstance();
        if (c == null) return;
        c.execute(() -> performRestock(slot));
    }

    public static void performRestock(int slot) {
        try {
            Minecraft c = Minecraft.getInstance();
            if (c == null || c.player == null || !canRestock()) return;
            AbstractContainerMenu h = c.player.containerMenu;
            if (!(h instanceof MerchantMenu m)) return;
            MerchantOffers offers = m.getOffers();
            if (slot < 0 || slot >= offers.size()) return;
            MerchantOffer offer = offers.get(slot);
            if (offer.isOutOfStock()) return;
            m.setSelectionHint(slot);
            m.tryMoveItems(slot);
            sendSelectTrade(slot);
        } catch (Throwable ignored) {
        }
    }
}
