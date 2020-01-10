package com.sizzlebae.projectseptium.tileentity;

import java.util.Optional;

import javax.annotation.Nonnull;

import com.sizzlebae.projectseptium.init.ModTileEntityTypes;

import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Inventory;
import net.minecraft.inventory.InventoryHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.FurnaceRecipe;
import net.minecraft.item.crafting.IRecipeType;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.ItemStackHandler;

public class QuartzAssemblerTileEntity extends TileEntity implements ITickableTileEntity {

    public static final int INPUT_SLOT = 0;
    public static final int OUTPUT_SLOT = 1;

    public final ItemStackHandler inventory = new ItemStackHandler(2) {
        @Override
        public boolean isItemValid(final int slot, @Nonnull final ItemStack stack) {
            switch (slot) {
            case INPUT_SLOT:
                return isInput(stack);
            case OUTPUT_SLOT:
                return isOutput(stack);
            default:
                return false;
            }
        }

        @Override
        public void onContentsChanged(final int slot) {
            super.onContentsChanged(slot);

            QuartzAssemblerTileEntity.this.markDirty();
        }
    };

    private final LazyOptional<ItemStackHandler> inventoryCapabilityExternal = LazyOptional.of(() -> this.inventory);

    public QuartzAssemblerTileEntity() {
        super(ModTileEntityTypes.QUARTZ_ASSEMBLER);
    }

    /**
     * @return If the stack is not empty and has a smelting recipe associated with
     *         it
     */
    private boolean isInput(final ItemStack stack) {
        if (stack.isEmpty()) {
            return false;
        }
        return getRecipe(stack).isPresent();
    }

    /**
     * @return If the stack's item is equal to the result of smelting our input
     */
    private boolean isOutput(final ItemStack stack) {
        final Optional<ItemStack> result = getResult(inventory.getStackInSlot(INPUT_SLOT));
        return result.isPresent() && ItemStack.areItemsEqual(result.get(), stack);
    }

    /**
     * @return The smelting recipe for the inventory
     */
    private Optional<FurnaceRecipe> getRecipe(final ItemStack input) {
        return getRecipe(new Inventory(input));
    }

    /**
     * @return The smelting recipe for the input stack
     */
    private Optional<FurnaceRecipe> getRecipe(final IInventory inventory) {
        return world.getRecipeManager().getRecipe(IRecipeType.SMELTING, inventory, world);
    }

    /**
     * @return The result of smelting the input stack
     */
    private Optional<ItemStack> getResult(final ItemStack input) {
        final Inventory dummyInventory = new Inventory(input);
        return getRecipe(dummyInventory).map(recipe -> recipe.getCraftingResult(dummyInventory));
    }

    @Override
    public void tick() {
        // Only do logic on server
        if (world == null || world.isRemote) {
            return;
        }

        // TODO: Do stuff with inventory to get output!

    }

    /**
     * Tries to insert the stack into the given slot or drops the stack on the
     * ground if it can't insert it.
     *
     * @param slot  The slot to try to insert the container item into
     * @param stack The stack to try to insert
     */
    private void insertOrDropStack(final int slot, final ItemStack stack) {
        final boolean canInsertContainterItemIntoSlot = inventory.insertItem(slot, stack, true).isEmpty();

        // TODO: Fill up space instead of ignoring stacks that are not completely
        // inserted
        if (canInsertContainterItemIntoSlot) {
            inventory.insertItem(slot, stack, false);
        } else {
            InventoryHelper.spawnItemStack(world, pos.getX(), pos.getY(), pos.getZ(), stack);
        }
    }

    @Override
    public void onLoad() {

    }

    @Override
    public void remove() {
        super.remove();

    }

}
