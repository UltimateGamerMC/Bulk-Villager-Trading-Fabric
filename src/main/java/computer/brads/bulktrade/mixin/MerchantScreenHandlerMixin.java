package computer.brads.bulktrade.mixin;

import computer.brads.bulktrade.BulkTrade;
import net.minecraft.screen.MerchantScreenHandler;
import net.minecraft.village.MerchantInventory;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.concurrent.TimeUnit;

@Mixin(MerchantScreenHandler.class)
public abstract class MerchantScreenHandlerMixin {
    @Shadow
    private MerchantInventory merchantInventory;

    @Inject(method = "playYesSound", at = @At("TAIL"))
    private void onPlayYesSound(CallbackInfo ci) {
        try {
            BulkTrade.isVillagerScreenOpen = true;
            BulkTrade.selectedVillagerSlot = ((MerchantInventoryAccessor) merchantInventory).getOfferIndex();
            if (BulkTrade.triggerRestock || !invokeCanRestock()) return;
            BulkTrade.triggerRestock = true;
            int slot = BulkTrade.selectedVillagerSlot;
            BulkTrade.getScheduler().schedule(() -> {
                try {
                    if (invokeCanRestock()) invokeScheduleRestock(slot);
                } finally {
                    BulkTrade.triggerRestock = false;
                }
            }, 150, TimeUnit.MILLISECONDS);
        } catch (Throwable ignored) {
        }
    }

    @Inject(method = "onClosed", at = @At("TAIL"))
    private void onClosed(CallbackInfo ci) {
        try {
            BulkTrade.isVillagerScreenOpen = false;
        } catch (Throwable ignored) {
        }
    }

    private static boolean invokeCanRestock() {
        try {
            Class<?> h = Class.forName("computer.brads.bulktrade.ClientRestockHelper");
            return Boolean.TRUE.equals(h.getMethod("canRestock").invoke(null));
        } catch (Throwable e) {
            return false;
        }
    }

    private static void invokeScheduleRestock(int slot) {
        try {
            Class<?> h = Class.forName("computer.brads.bulktrade.ClientRestockHelper");
            h.getMethod("scheduleRestock", int.class).invoke(null, slot);
        } catch (Throwable ignored) {
        }
    }
}
