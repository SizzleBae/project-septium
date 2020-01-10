package com.sizzlebae.projectseptium;

import net.minecraftforge.fml.common.Mod;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Mod(ProjectSeptium.MODID)
public class ProjectSeptium {
    public static final String MODID = "project-septium";

    public static final Logger LOGGER = LogManager.getLogger(ProjectSeptium.MODID);

    public ProjectSeptium() {
        ProjectSeptium.LOGGER.debug("Hello from Project Septium!");
    }

}
