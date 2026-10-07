package computer.brads.bulktrade.mixin;

import net.minecraft.world.inventory.MerchantContainer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(MerchantContainer.class)
public interface MerchantInventoryAccessor {
    @Accessor("selectionHint")
    int getOfferIndex();
}
