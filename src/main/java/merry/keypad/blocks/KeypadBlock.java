package merry.keypad.blocks;

import com.mojang.serialization.MapCodec;
import merry.keypad.Keypad;
import merry.keypad.blockEntities.KeypadBlockEntity;
import merry.keypad.ui.CustomPlayerInterface;
import net.minecraft.block.*;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.enums.BlockFace;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.BooleanProperty;
import net.minecraft.state.property.EnumProperty;
import net.minecraft.state.property.Properties;
import net.minecraft.text.Text;
import net.minecraft.util.ActionResult;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.random.Random;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import net.minecraft.world.block.OrientationHelper;
import net.minecraft.world.block.WireOrientation;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;

public class KeypadBlock extends BlockWithEntity {
    @Override
    protected MapCodec<? extends BlockWithEntity> getCodec() {
        return createCodec(KeypadBlock::new);
    }
    public static final BooleanProperty POWERED = Properties.POWERED;
    public static final EnumProperty<BlockFace> FACE = Properties.BLOCK_FACE;
    public static final EnumProperty<Direction> FACING = HorizontalFacingBlock.FACING;
    public static final BooleanProperty PASSWORD_SET = BooleanProperty.of("password_set");

    public KeypadBlock(Settings settings) {
        super(settings);
        this.setDefaultState(this.getDefaultState().with(POWERED, true)
                .with(FACING,Direction.NORTH).with(FACE, BlockFace.WALL).with(PASSWORD_SET, false));
    }

    @Override
    protected void onBlockAdded(BlockState state, World world, BlockPos pos, BlockState oldState, boolean notify) {
        this.updateTargets(world, pos);
    }

    @Nullable
    @Override
    public BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
        return new KeypadBlockEntity(pos, state);
    }

    @Override
    protected boolean emitsRedstonePower(BlockState state) {
        return true;
    }

    @Override
    protected int getWeakRedstonePower(BlockState state, BlockView world, BlockPos pos, Direction direction) {
        return state.get(POWERED) && getDirection(state) == direction ? 15 : 0;
    }

    @Override
    protected int getStrongRedstonePower(BlockState state, BlockView world, BlockPos pos, Direction direction) {
        return getWeakRedstonePower(state, world, pos, direction);
    }

    protected static Direction getDirection(BlockState state) {
        return switch ((BlockFace) state.get(FACE)) {
            case CEILING -> Direction.DOWN;
            case FLOOR -> Direction.UP;
            default -> state.get(FACING);
        };
    }

    @Override
    protected ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, BlockHitResult hit) {
        if (!(world.getBlockEntity(pos) instanceof KeypadBlockEntity keypadBlockEntity)) {
            return super.onUse(state, world, pos, player, hit);
        }
        BlockEntity blockEntity = world.getBlockEntity(pos);
        if (world.isClient()) {
            if(state.get(PASSWORD_SET) && state.get(POWERED)) {
                player.sendMessage(Text.translatable("chat."+ Keypad.MOD_ID+".active"),false);
            }else{
                ((CustomPlayerInterface) player).openKeypadScreen(blockEntity);
            }
        }else{
            world.setBlockState(pos, state.with(POWERED, Objects.equals(keypadBlockEntity.getPassword(), keypadBlockEntity.getPasswordSet())),1);
            updateTargets(world,pos);
        }
        return ActionResult.SUCCESS;
    }

    @Override
    protected void onStateReplaced(BlockState state, World world, BlockPos pos, BlockState newState, boolean moved) {
        if (moved || state.isOf(newState.getBlock())) return;
        super.onStateReplaced(state, world, pos, newState, moved);
        this.updateTargets(world, pos);
    }

    public void updateTarget(World world, BlockPos pos, Direction direction) {
        BlockPos blockPos = pos.offset(direction);
        WireOrientation wireOrientation = OrientationHelper.getEmissionOrientation(world, direction, Direction.UP);
        world.updateNeighbor(blockPos, this, wireOrientation);
        world.updateNeighborsExcept(blockPos, this, direction.getOpposite(), wireOrientation);
    }

    public void updateTargets(World world, BlockPos pos) {
        for (Direction direction : Direction.values()) {
            updateTarget(world, pos, direction);
        }
    }

    @Override
    public void scheduledTick(BlockState state, ServerWorld world, BlockPos pos, Random random){
        KeypadBlockEntity entity = (KeypadBlockEntity) world.getBlockEntity(pos);
        if (state.get(Properties.POWERED)) {
            assert entity != null;
            entity.setPassword("",false);
        }
        super.scheduledTick(state,world,pos,random);
    }

    @Override
    public BlockState getPlacementState(ItemPlacementContext ctx) {
//        Direction direction = ctx.getPlayerLookDirection().getOpposite();
        for (Direction direction : ctx.getPlacementDirections()) {
            BlockState blockState;
            if (direction.getAxis() == Direction.Axis.Y) {
                blockState = this.getDefaultState()
                        .with(FACE, direction == Direction.UP ? BlockFace.CEILING : BlockFace.FLOOR)
                        .with(FACING, ctx.getHorizontalPlayerFacing());
            } else {
                blockState = this.getDefaultState().with(FACE, BlockFace.WALL).with(FACING, direction.getOpposite());
            }

            if (blockState.canPlaceAt(ctx.getWorld(), ctx.getBlockPos())) {
                return blockState;
            }
        }

        return null;
    }

    protected static final VoxelShape NORTH_WALL_SHAPE = Block.createCuboidShape(3, 1, 14, 13, 14, 16);
    protected static final VoxelShape SOUTH_WALL_SHAPE = Block.createCuboidShape(3, 1, 0, 13, 14, 2);
    protected static final VoxelShape EAST_WALL_SHAPE = Block.createCuboidShape(0, 1, 3, 2, 14, 13);
    protected static final VoxelShape WEST_WALL_SHAPE = Block.createCuboidShape(14, 1, 3, 16, 14, 13);

    protected static final VoxelShape NORTH_CEILING_SHAPE = Block.createCuboidShape(3, 14, 1, 13, 16.0, 14);
    protected static final VoxelShape SOUTH_CEILING_SHAPE = Block.createCuboidShape(3, 14, 2, 13, 16.0, 15);
    protected static final VoxelShape EAST_CEILING_SHAPE = Block.createCuboidShape(2, 14, 3, 15, 16.0, 13);
    protected static final VoxelShape WEST_CEILING_SHAPE = Block.createCuboidShape(1, 14, 3, 14, 16.0, 13);

    protected static final VoxelShape NORTH_FLOOR_SHAPE = Block.createCuboidShape(3, 0.0, 2, 13, 2, 15);
    protected static final VoxelShape SOUTH_FLOOR_SHAPE = Block.createCuboidShape(3, 0.0, 1, 13, 2, 14);
    protected static final VoxelShape EAST_FLOOR_SHAPE = Block.createCuboidShape(1, 0.0, 3, 14, 2, 13);
    protected static final VoxelShape WEST_FLOOR_SHAPE = Block.createCuboidShape(2, 0.0, 3, 15, 2, 13);

    @Override
    protected VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
        return switch ((BlockFace) state.get(FACE)) {
            case FLOOR -> switch (((Direction) state.get(FACING))) {
                case EAST -> EAST_FLOOR_SHAPE;
                case WEST -> WEST_FLOOR_SHAPE;
                case SOUTH -> SOUTH_FLOOR_SHAPE;
                default -> NORTH_FLOOR_SHAPE;
            };
            case WALL -> switch ((Direction) state.get(FACING)) {
                case EAST -> EAST_WALL_SHAPE;
                case WEST -> WEST_WALL_SHAPE;
                case SOUTH -> SOUTH_WALL_SHAPE;
                default -> NORTH_WALL_SHAPE;
            };
            default -> switch (((Direction) state.get(FACING))) {
                case EAST -> EAST_CEILING_SHAPE;
                case WEST -> WEST_CEILING_SHAPE;
                case SOUTH -> SOUTH_CEILING_SHAPE;
                default -> NORTH_CEILING_SHAPE;
            };
        };
    }

    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> builder){
        builder.add(POWERED)
                .add(FACING)
                .add(FACE)
                .add(PASSWORD_SET);
    }
}
