package com.sizzlebae.projectseptium.tileentity;

import java.util.Optional;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.sizzlebae.projectseptium.init.ModBlocks;
import com.sizzlebae.projectseptium.init.ModTileEntityTypes;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Inventory;
import net.minecraft.inventory.InventoryHelper;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.INamedContainerProvider;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.AbstractCookingRecipe;
import net.minecraft.item.crafting.FurnaceRecipe;
import net.minecraft.item.crafting.IRecipeType;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandlerModifiable;
import net.minecraftforge.items.ItemStackHandler;
import net.minecraftforge.items.wrapper.RangedWrapper;

public class QuartzAssemblerTileEntity extends TileEntity implements ITickableTileEntity, INamedContainerProvider {

    public static final int INPUT_SLOT = 0;
    public static final int OUTPUT_SLOT = 1;

    private static final String INVENTORY_TAG = "inventory";

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

    private final LazyOptional<IItemHandlerModifiable> inventoryCapabilityExternalUpAndSides = LazyOptional.of(() -> new RangedWrapper(this.inventory, INPUT_SLOT, INPUT_SLOT + 1));

    private final LazyOptional<IItemHandlerModifiable> inventoryCapabilityExternalDown = LazyOptional.of(() -> new RangedWrapper(this.inventory, OUTPUT_SLOT, OUTPUT_SLOT + 1));

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
        final boolean canInsertContainerItemIntoSlot = inventory.insertItem(slot, stack, true).isEmpty();

        // TODO: Fill up space instead of ignoring stacks that are not completely
        // inserted
        if (canInsertContainerItemIntoSlot) {
            inventory.insertItem(slot, stack, false);
        } else {
            InventoryHelper.spawnItemStack(world, pos.getX(), pos.getY(), pos.getZ(), stack);
        }
    }

    /**
     * Mimics the code inAbstractFurnaceTileEntity#func_214005_h()
     *
     * @return The custom smelt time or 200 if there is no recipe for the input
     */
    private short getSmeltTime(final ItemStack input) {
        return getRecipe(input)
                .map(AbstractCookingRecipe::getCookTime)
                .orElse(200)
                .shortValue();
    }

    @Nonnull
    @Override
    public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> cap, @Nullable Direction side) {
        if(cap == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY) {
            if(side == null) {
                return inventoryCapabilityExternal.cast();
            }

            switch(side) {
                case DOWN:
                    return inventoryCapabilityExternalDown.cast();
                case UP:
                case NORTH:
                case SOUTH:
                case EAST:
                case WEST:
                    return inventoryCapabilityExternalUpAndSides.cast();
            }
        }

        return super.getCapability(cap, side);
    }

    @Override
    public void onLoad() {

    }

    @Override
    public void read(CompoundNBT compound) {
        super.read(compound);

        this.inventory.deserializeNBT(compound.getCompound(INVENTORY_TAG));
    }

    @Override
    public CompoundNBT write(CompoundNBT compound) {
        super.write(compound);

        compound.put(INVENTORY_TAG, this.inventory.serializeNBT());

        return compound;
    }

    /**
     * Get an NBT compound to sync to the client with SPacketChunkData, used for initial loading of the
     * chunk or when many blocks change at once.
     * This compound comes back to you client-side in {@link #handleUpdateTag}
     * The default implementation ({@link TileEntity#handleUpdateTag}) calls {@link #writeInternal)}
     * which doesn't save any of our extra data so we override it to call {@link #write} instead
     */
    @Override
    public CompoundNBT getUpdateTag() {
        return this.write(new CompoundNBT());
    }

    @Override
    public void remove() {
        super.remove();
        inventoryCapabilityExternal.invalidate();
    }

    @Override
    public ITextComponent getDisplayName() {
        return new TranslationTextComponent(ModBlocks.QUARTZ_ASSEMBLER.getTranslationKey());
    }

    @Nullable
    @Override
    public Container createMenu(int p_createMenu_1_, PlayerInventory p_createMenu_2_, PlayerEntity p_createMenu_3_) {
        // TODO: Create menu..
        return null;
    }
}
