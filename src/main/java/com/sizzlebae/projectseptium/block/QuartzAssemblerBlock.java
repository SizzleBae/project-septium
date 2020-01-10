package com.sizzlebae.projectseptium.block;

import com.sizzlebae.projectseptium.init.ModTileEntityTypes;

import com.sizzlebae.projectseptium.tileentity.QuartzAssemblerTileEntity;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.HorizontalBlock;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.inventory.InventoryHelper;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.state.StateContainer.Builder;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.Hand;
import net.minecraft.util.Mirror;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.IWorld;
import net.minecraft.world.World;
import net.minecraftforge.fml.network.NetworkHooks;
import net.minecraftforge.items.ItemStackHandler;

import javax.annotation.Nullable;

public class QuartzAssemblerBlock extends HorizontalBlock {

    public QuartzAssemblerBlock(final Properties properties) {
        super(properties);

        this.setDefaultState(this.getDefaultState().with(HORIZONTAL_FACING, Direction.NORTH));
    }

    /**
     * Called on the logical server when a BlockState with a TileEntity is replaced by another BlockState.
     * We use this method to drop all the items from our tile entity's inventory and update comparators near our block.
     *
     * Implementing/overriding is fine.
     */
    @Override
    public void onReplaced(BlockState oldState, World worldIn, BlockPos pos, BlockState newState, boolean isMoving) {
        if(oldState.getBlock() != newState.getBlock()) {
            TileEntity tileEntity = worldIn.getTileEntity(pos);
            if(tileEntity instanceof QuartzAssemblerTileEntity) {
                final ItemStackHandler inventory = ((QuartzAssemblerTileEntity) tileEntity).inventory;

                for(int slot = 0; slot < inventory.getSlots(); slot++) {
                    InventoryHelper.spawnItemStack(worldIn, pos.getX(), pos.getY(), pos.getZ(), inventory.getStackInSlot(slot));
                }
            }
        }

        super.onReplaced(oldState, worldIn, pos, newState, isMoving);
    }

    /**
     * Called when a player right clicks our block.
     * We use this method to open our gui.
     *
     * @deprecated Call via {@link BlockState#onBlockActivated(World, PlayerEntity, Hand, BlockRayTraceResult)} whenever possible.
     * Implementing/overriding is fine.
     */
    @Override
    public boolean onBlockActivated(BlockState state, World worldIn, BlockPos pos, PlayerEntity player, Hand handIn, BlockRayTraceResult hit) {
        if(!worldIn.isRemote) {
            final TileEntity tileEntity = worldIn.getTileEntity(pos);
            if(tileEntity instanceof QuartzAssemblerTileEntity) {
                NetworkHooks.openGui((ServerPlayerEntity) player, (QuartzAssemblerTileEntity) tileEntity, pos);
            }
        }
        return true;
    }

    @Nullable
    @Override
    public BlockState getStateForPlacement(BlockItemUseContext context) {
        return this.getDefaultState().with(HORIZONTAL_FACING, context.getPlacementHorizontalFacing().getOpposite());
    }

    @Override
    public BlockState rotate(BlockState state, Rotation rot) {
        return state.with(HORIZONTAL_FACING, rot.rotate(state.get(HORIZONTAL_FACING)));
    }

    @Override
    public BlockState mirror(BlockState state, Mirror mirrorIn) {
        return state.rotate(mirrorIn.toRotation(state.get(HORIZONTAL_FACING)));
    }

    @Override
    public boolean hasTileEntity(final BlockState state) {
        return true;
    }

    @Override
    public TileEntity createTileEntity(final BlockState state, final IBlockReader world) {
        return ModTileEntityTypes.QUARTZ_ASSEMBLER.create();
    }

    @Override
    protected void fillStateContainer(Builder<Block, BlockState> builder) {
        super.fillStateContainer(builder);
        builder.add(HORIZONTAL_FACING);
    }

}
