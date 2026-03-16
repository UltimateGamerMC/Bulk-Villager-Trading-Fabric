package computer.brads.bulktrade;

import net.minecraft.client.MinecraftClient;
import net.minecraft.network.packet.c2s.play.SelectMerchantTradeC2SPacket;
import net.minecraft.screen.MerchantScreenHandler;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.village.TradeOffer;
import net.minecraft.village.TradeOfferList;

public final class ClientRestockHelper {
    private ClientRestockHelper() {}

    public static boolean canRestock() {
        try {
            MinecraftClient c = MinecraftClient.getInstance();
            return c != null && c.getNetworkHandler() != null;
        } catch (Throwable e) {
            return false;
        }
    }

    public static void sendSelectTrade(int index) {
        try {
            MinecraftClient c = MinecraftClient.getInstance();
            if (c != null && c.getNetworkHandler() != null) {
                c.getNetworkHandler().sendPacket(new SelectMerchantTradeC2SPacket(index));
            }
        } catch (Throwable ignored) {
        }
    }

    public static void scheduleRestock(int slot) {
        MinecraftClient c = MinecraftClient.getInstance();
        if (c == null) return;
        c.execute(() -> performRestock(slot));
    }

    public static void performRestock(int slot) {
        try {
            MinecraftClient c = MinecraftClient.getInstance();
            if (c == null || c.player == null || !canRestock()) return;
            ScreenHandler h = c.player.currentScreenHandler;
            if (!(h instanceof MerchantScreenHandler m)) return;
            TradeOfferList offers = m.getRecipes();
            if (slot < 0 || slot >= offers.size()) return;
            TradeOffer offer = offers.get(slot);
            if (offer.isDisabled()) return;
            m.setRecipeIndex(slot);
            m.switchTo(slot);
            sendSelectTrade(slot);
        } catch (Throwable ignored) {
        }
    }
}
