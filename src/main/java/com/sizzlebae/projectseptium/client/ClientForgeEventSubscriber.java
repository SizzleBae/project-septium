package com.sizzlebae.projectseptium.client;

import com.sizzlebae.projectseptium.ProjectSeptium;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;

/**
 * Subscribe to events from the FORGE EventBus that should be handled on the PHYSICAL CLIENT side in this class
 */
@EventBusSubscriber(modid = ProjectSeptium.MODID, bus = EventBusSubscriber.Bus.FORGE, value = Dist.CLIENT)
public class ClientForgeEventSubscriber {

}
