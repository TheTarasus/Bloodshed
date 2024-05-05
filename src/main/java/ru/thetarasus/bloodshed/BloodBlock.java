package ru.thetarasus.bloodshed;

import net.minecraft.block.*;
import net.minecraft.entity.Entity;
import net.minecraft.item.BoneMealItem;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundEvents;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.Properties;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.shape.SimpleVoxelShape;
import net.minecraft.util.shape.VoxelSet;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import net.minecraft.world.WorldAccess;

import java.util.Random;

public class BloodBlock extends Block {

    @Override
    public boolean hasRandomTicks(BlockState state) {
        return true;
    }

    @Override
    public VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
        return VoxelShapes.cuboid(0, 0, 0, 1, 0.03125, 1);
    }


    @Override
    public void onEntityCollision(BlockState state, World world, BlockPos pos, Entity entity) {
        boolean isMoved = entity.prevX != entity.getX() || entity.prevY != entity.getY() || entity.prevZ != entity.getZ();
        if(!isMoved) return;
        if (world.random.nextInt(5) == 0) {
            entity.playSound(SoundEvents.BLOCK_HONEY_BLOCK_SLIDE, 1.0F, 1.0F);
        }
    }

    public BloodBlock(Settings settings) {
        super(settings);
        this.setDefaultState(this.getDefaultState()
                .with(Properties.UP, false)
                .with(Properties.DOWN, false)
                .with(Properties.NORTH, false)
                .with(Properties.EAST, false)
                .with(Properties.SOUTH, false)
                .with(Properties.WEST, false).with(Properties.AGE_3, 0));
    }

    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        builder.add(Properties.UP).add(Properties.DOWN)
                .add(Properties.NORTH).add(Properties.EAST)
                .add(Properties.SOUTH).add(Properties.WEST)
                .add(Properties.AGE_3);
    }

    @Override
    public void onBlockAdded(BlockState state, World world, BlockPos pos, BlockState oldState, boolean notify) {
        boolean up = world.getBlockState(pos.up()).isFullCube(world, pos.up());
        boolean down = world.getBlockState(pos.down()).isFullCube(world, pos.down());
        boolean north = world.getBlockState(pos.north()).isFullCube(world, pos.north());
        boolean east = world.getBlockState(pos.east()).isFullCube(world, pos.east());
        boolean south = world.getBlockState(pos.south()).isFullCube(world, pos.south());
        boolean west = world.getBlockState(pos.west()).isFullCube(world, pos.west());

        world.setBlockState(pos, state
                .with(Properties.UP, up)
                .with(Properties.DOWN, down)
                .with(Properties.NORTH, north)
                .with(Properties.EAST, east)
                .with(Properties.SOUTH, south)
                .with(Properties.WEST, west));

        boolean isEmpty = !(up|| down || north || east || south || west);
        if(isEmpty) world.setBlockState(pos, Blocks.AIR.getDefaultState());
    }

    @Override
    public void randomTick(BlockState state, ServerWorld world, BlockPos pos, Random random) {
        super.randomTick(state, world, pos, random);
        Random r = new Random();
        if(r.nextInt(3) != 0) return;

        int power = state.get(Properties.AGE_3) - 1;
        if(power < 0) {
            world.setBlockState(pos, Blocks.AIR.getDefaultState());
            return;
        }
        world.setBlockState(pos, state.with(Properties.AGE_3, power));
        BlockState fert = world.getBlockState(pos.down());
        if(!(fert.getBlock() instanceof Fertilizable)) return;
        ((Fertilizable)fert.getBlock()).grow(world, random, pos.down(), fert);
    }

    @Override
    public BlockState getStateForNeighborUpdate(BlockState state, Direction direction, BlockState neighborState, WorldAccess world, BlockPos pos, BlockPos neighborPos) {
        boolean up = world.getBlockState(pos.up()).isFullCube(world, pos.up());
        boolean down = world.getBlockState(pos.down()).isFullCube(world, pos.down());
        boolean north = world.getBlockState(pos.north()).isFullCube(world, pos.north());
        boolean east = world.getBlockState(pos.east()).isFullCube(world, pos.east());
        boolean south = world.getBlockState(pos.south()).isFullCube(world, pos.south());
        boolean west = world.getBlockState(pos.west()).isFullCube(world, pos.west());

        world.setBlockState(pos, state
                .with(Properties.UP, up)
                .with(Properties.DOWN, down)
                .with(Properties.NORTH, north)
                .with(Properties.EAST, east)
                .with(Properties.SOUTH, south)
                .with(Properties.WEST, west), 0);

        boolean isEmpty = !(up|| down || north || east || south || west);
        if(isEmpty) world.setBlockState(pos, Blocks.AIR.getDefaultState(), 0);
        return state;
    }
}
