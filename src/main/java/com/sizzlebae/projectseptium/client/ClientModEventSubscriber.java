package com.sizzlebae.projectseptium.client;

import com.sizzlebae.projectseptium.client.gui.QuartzAssemblerScreen;
import com.sizzlebae.projectseptium.init.ModContainerTypes;
import net.minecraft.client.gui.ScreenManager;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.ModContainer;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;

/**
 * Subscribe to events from the MOD EventBus that should be handled on the PHYSICAL CLIENT side in this class
 */
public class ClientModEventSubscriber {

    /**
     * We need to register our renderers on the client because rendering code does not exist on the server
     * and trying to use it on a dedicated server will crash the game.
     * This method will be called by Forge when it is time for the mod to do its client-side setup
     * This method will always be called after the Registry events.
     * This means that all Blocks, Items, TileEntityTypes, etc. will all have been registered already
     */
    @SubscribeEvent
    public static void onFMLClientSetupEvent(final FMLClientSetupEvent event) {
        //TODO: Register TileEntity Renderers

        // Register ContainerType Screens
        ScreenManager.registerFactory(ModContainerTypes.QUARTZ_ASSEMBLER, QuartzAssemblerScreen::new);
    }

}
