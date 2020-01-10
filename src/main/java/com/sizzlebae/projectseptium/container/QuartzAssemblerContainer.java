package com.sizzlebae.projectseptium.container;

import com.sizzlebae.projectseptium.init.ModBlocks;
import com.sizzlebae.projectseptium.init.ModContainerTypes;
import com.sizzlebae.projectseptium.tileentity.QuartzAssemblerTileEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.Slot;
import net.minecraft.network.PacketBuffer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.IWorldPosCallable;

import javax.annotation.Nonnull;
import java.util.Objects;

public class QuartzAssemblerContainer extends Container {

    public final QuartzAssemblerTileEntity tileEntity;
    private final IWorldPosCallable canInteractWithCallable;

    /**
     * Logical-client-side constructor, called from {ContainerType#create(IContainerFactory)}
     * Calls the logical-server-side constructor with the TileEntity at the pos in the PacketBuffer
     */
    public QuartzAssemblerContainer(final int windowId, final PlayerInventory playerInventory, final PacketBuffer data) {
        this(windowId, playerInventory, getTileEntity(playerInventory, data));
    }

    /**
     * Constructor called logical-server-side from {ElectricFurnaceTileEntity#createMenu}
     * and logical-client-side from {#ElectricFurnaceContainer(int, PlayerInventory, PacketBuffer)}
     */
    public QuartzAssemblerContainer(final int windowId, final PlayerInventory playerInventory, final QuartzAssemblerTileEntity tileEntity) {
        super(ModContainerTypes.QUARTZ_ASSEMBLER, windowId);
        this.tileEntity = tileEntity;
        this.canInteractWithCallable = IWorldPosCallable.of(tileEntity.getWorld(), tileEntity.getPos());

        // Add all the slots for the tileEntity's inventory and the playerInventory to this container

        // Tile inventory slot(s)
        // TODO: Design quartz inventory assembling layout instead
        // this.addSlot(new SlotItemHandler(tileEntity.inventory, HeatCollectorTileEntity.FUEL_SLOT, 80, 35));

        final int playerInventoryStartX = 8;
        final int playerInventoryStartY = 84;
        final int slotSizePlus2 = 18; // slots are 16x16, plus 2 (for spacing/borders) is 18x18

        // Player Top Inventory slots
        for (int row = 0; row < 3; ++row) {
            for (int column = 0; column < 9; ++column) {
                this.addSlot(new Slot(playerInventory, 9 + (row * 9) + column, playerInventoryStartX + (column * slotSizePlus2), playerInventoryStartY + (row * slotSizePlus2)));
            }
        }

        final int playerHotbarY = playerInventoryStartY + slotSizePlus2 * 3 + 4;
        // Player Hotbar slots
        for (int column = 0; column < 9; ++column) {
            this.addSlot(new Slot(playerInventory, column, playerInventoryStartX + (column * slotSizePlus2), playerHotbarY));
        }
    }

    private static QuartzAssemblerTileEntity getTileEntity(final PlayerInventory playerInventory, final PacketBuffer data) {
        Objects.requireNonNull(playerInventory, "playerInventory cannot be null!");
        Objects.requireNonNull(data, "data cannot be null!");

        final TileEntity tileAtPos = playerInventory.player.world.getTileEntity(data.readBlockPos());
        if(tileAtPos instanceof  QuartzAssemblerTileEntity) {
            return (QuartzAssemblerTileEntity) tileAtPos;
        }
        throw new IllegalStateException("Tile entity is not correct! " + tileAtPos);
    }

    @Override
    public boolean canInteractWith(@Nonnull final PlayerEntity playerIn) {
        return isWithinUsableDistance(canInteractWithCallable, playerIn, ModBlocks.QUARTZ_ASSEMBLER);
    }
}
