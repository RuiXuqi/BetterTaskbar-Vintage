package net.morimori0317.bettertaskbar;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.*;
import net.minecraft.client.multiplayer.GuiConnecting;
import net.minecraftforge.client.event.GuiOpenEvent;
import net.minecraftforge.fml.client.GuiAccessDenied;
import net.minecraftforge.fml.client.GuiBackupFailed;
import net.minecraftforge.fml.client.GuiConfirmation;
import net.minecraftforge.fml.client.GuiSortingProblem;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.morimori0317.bettertaskbar.api.BetterTaskbarAPI;
import org.jetbrains.annotations.NotNull;

@Mod.EventBusSubscriber
public class TaskbarHandler {
    @SubscribeEvent
    public static void screenTick(@NotNull TickEvent.RenderTickEvent event) {
        var api = BetterTaskbarAPI.getInstance();
        GuiScreen screen = Minecraft.getMinecraft().currentScreen;
        if (screen instanceof GuiScreenWorking loadingScreen) {
            // Seems not working since it is always 0...
            api.setProgress(Math.max(loadingScreen.progress, 1), 100);
        } else if (screen instanceof GuiDownloadTerrain || screen instanceof GuiConnecting) {
            api.setState(BetterTaskbarAPI.State.WAIT);
        } else if (screen instanceof GuiYesNo || screen instanceof GuiConfirmation) {
            api.setState(BetterTaskbarAPI.State.PAUSE);
        } else if (screen instanceof GuiDisconnected || screen instanceof GuiErrorScreen || screen instanceof GuiMemoryErrorScreen || screen instanceof GuiAccessDenied || screen instanceof GuiBackupFailed || screen instanceof GuiSortingProblem) {
            api.setState(BetterTaskbarAPI.State.ERROR);
        }
    }

    @SubscribeEvent
    public static void screenChange(@NotNull GuiOpenEvent event) {
        var api = BetterTaskbarAPI.getInstance();
        if (api.isUpdated()) {
            api.setState(BetterTaskbarAPI.State.NO_PROGRESS);
            api.setProgress(0);
            api.setUpdated(false);
        }
    }
}
