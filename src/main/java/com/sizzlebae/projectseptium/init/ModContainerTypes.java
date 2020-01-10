package com.sizzlebae.projectseptium.init;

import com.sizzlebae.projectseptium.ModUtil;
import com.sizzlebae.projectseptium.ProjectSeptium;
import com.sizzlebae.projectseptium.container.QuartzAssemblerContainer;
import net.minecraft.inventory.container.ContainerType;
import net.minecraftforge.registries.ObjectHolder;

@ObjectHolder(ProjectSeptium.MODID)
public class ModContainerTypes {

    public static final ContainerType<QuartzAssemblerContainer> QUARTZ_ASSEMBLER = ModUtil._null();

}
