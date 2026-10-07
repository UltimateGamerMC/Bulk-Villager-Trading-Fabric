package computer.brads.bulktrade;

import net.fabricmc.api.ClientModInitializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

public class BulkTrade implements ClientModInitializer {
    public static final String MODID = "bulktrade";
    public static final Logger LOGGER = LoggerFactory.getLogger(MODID);
    public static Boolean isVillagerScreenOpen = false;
    public static Integer selectedVillagerSlot = 0;
    public static Boolean triggerRestock = false;
    private static ScheduledExecutorService scheduler;

    public static ScheduledExecutorService getScheduler() {
        if (scheduler == null) {
            // Daemon thread so it never keeps the game process alive after quitting.
            scheduler = Executors.newSingleThreadScheduledExecutor(runnable -> {
                Thread thread = new Thread(runnable, "BulkTrade restock");
                thread.setDaemon(true);
                return thread;
            });
        }
        return scheduler;
    }

    @Override
    public void onInitializeClient() {
        LOGGER.info("Bulk Villager Trading loaded");
    }
}
