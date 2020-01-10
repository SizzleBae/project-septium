package com.sizzlebae.projectseptium;

import javax.annotation.Nonnull;

import com.google.common.base.Preconditions;
import com.sizzlebae.projectseptium.block.QuartzAssemblerBlock;
import com.sizzlebae.projectseptium.container.QuartzAssemblerContainer;
import com.sizzlebae.projectseptium.init.ModBlocks;
import com.sizzlebae.projectseptium.init.ModItemGroups;
import com.sizzlebae.projectseptium.tileentity.QuartzAssemblerTileEntity;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.inventory.container.ContainerType;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.extensions.IForgeContainerType;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.IForgeRegistry;
import net.minecraftforge.registries.IForgeRegistryEntry;

@EventBusSubscriber(modid = ProjectSeptium.MODID, bus = EventBusSubscriber.Bus.MOD)
public class ModEventSubscriber {

    /**
     * This method will be called by Forge when it is time for the mod to register its Blocks.
     * This method will always be called before the Item registry method.
     */
    @SubscribeEvent
    public static void onRegisterBlocks(RegistryEvent.Register<Block> event) {
        event.getRegistry().registerAll(
                setup(new Block(Block.Properties.create(Material.ROCK).hardnessAndResistance(3.0F, 3.0F)),
                        "septium_ore"),
                setup(new QuartzAssemblerBlock(Block.Properties.create(Material.ROCK).hardnessAndResistance(3.5F)),
                        "quartz_assembler"));
    }

    /**
     * This method will be called by Forge when it is time for the mod to register its Items.
     * This method will always be called after the Block registry method.
     */
    @SubscribeEvent
    public static void onRegisterItems(RegistryEvent.Register<Item> event) {
        final IForgeRegistry<Item> registry = event.getRegistry();

        registry.registerAll(
                setup(new Item(new Item.Properties().group(ModItemGroups.PROJECT_SEPTIUM_GROUP)), "sepith_item"));

        ForgeRegistries.BLOCKS.getValues().stream()
                .filter(block -> block.getRegistryName().getNamespace().equals(ProjectSeptium.MODID)).forEach(block -> {
                    // Make the properties, and make it so that the item will be on our ItemGroup
                    // (CreativeTab)
                    final Item.Properties properties = new Item.Properties().group(ModItemGroups.PROJECT_SEPTIUM_GROUP);
                    // Create the new BlockItem with the block and it's properties
                    final BlockItem blockItem = new BlockItem(block, properties);
                    // Setup the new BlockItem with the block's registry name and register it
                    registry.register(setup(blockItem, block.getRegistryName()));
                });
    }

    /**
     * This method will be called by Forge when it is time for the mod to register its TileEntityType.
     * This method will always be called after the Block and Item registry methods.
     */
    @SubscribeEvent
    public static void onRegisterTileEntityTypes(RegistryEvent.Register<TileEntityType<?>> event) {
        event.getRegistry().registerAll(setup(
                TileEntityType.Builder.create(QuartzAssemblerTileEntity::new, ModBlocks.QUARTZ_ASSEMBLER).build(null),
                "quartz_assembler"));
    }

    /**
     * This method will be called by Forge when it is time for the mod to register its ContainerTypes.
     * This method will always be called after the Block and Item registry methods.
     */
    @SubscribeEvent
    public static void onRegisterContainerTypes(@Nonnull final RegistryEvent.Register<ContainerType<?>> event) {
    event.getRegistry().registerAll(
            setup(IForgeContainerType.create(QuartzAssemblerContainer::new), "quartz_assembler"));

    }


    /**
     * Performs setup on a registry entry
     *
     * @param name The path of the entry's name. Used to make a name who's domain is our mod's modid
     */
    @Nonnull
    private static <T extends IForgeRegistryEntry<T>> T setup(@Nonnull final T entry, @Nonnull final String name) {
        Preconditions.checkNotNull(name, "Name to assign to entry cannot be null!");
        return setup(entry, new ResourceLocation(ProjectSeptium.MODID, name));
    }

    /**
     * Performs setup on a registry entry
     *
     * @param registryName The full registry name of the entry
     */
    @Nonnull
    private static <T extends IForgeRegistryEntry<T>> T setup(@Nonnull final T entry,
            @Nonnull final ResourceLocation registryName) {
        Preconditions.checkNotNull(entry, "Entry cannot be null!");
        Preconditions.checkNotNull(registryName, "Registry name to assign to entry cannot be null!");
        entry.setRegistryName(registryName);
        return entry;
    }

}
