package merry.keypad.blocks;

import com.mojang.serialization.MapCodec;
import merry.keypad.Keypad;
import merry.keypad.blockEntities.KeypadBlockEntity;
import merry.keypad.ui.CustomPlayerInterface;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.HorizontalDirectionalBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.AttachFace;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.level.redstone.ExperimentalRedstoneUtils;
import net.minecraft.world.level.redstone.Orientation;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;

public class KeypadBlock extends BaseEntityBlock {
    @Override
    protected MapCodec<? extends BaseEntityBlock> codec() {
        return simpleCodec(KeypadBlock::new);
    }
    public static final BooleanProperty POWERED = BlockStateProperties.POWERED;
    public static final EnumProperty<AttachFace> FACE = BlockStateProperties.ATTACH_FACE;
    public static final EnumProperty<Direction> FACING = HorizontalDirectionalBlock.FACING;
    public static final BooleanProperty PASSWORD_SET = BooleanProperty.create("password_set");

    public KeypadBlock(Properties settings) {
        super(settings);
        this.registerDefaultState(this.defaultBlockState().setValue(POWERED, true)
                .setValue(FACING,Direction.NORTH).setValue(FACE, AttachFace.WALL).setValue(PASSWORD_SET, false));
    }

    @Override
    protected void onPlace(BlockState state, Level world, BlockPos pos, BlockState oldState, boolean notify) {
        this.updateTargets(world, pos);
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new KeypadBlockEntity(pos, state);
    }

    @Override
    protected boolean isSignalSource(BlockState state) {
        return true;
    }

    @Override
    protected int getSignal(BlockState state, BlockGetter world, BlockPos pos, Direction direction) {
        return state.getValue(POWERED) && getDirection(state) == direction ? 15 : 0;
    }

    @Override
    protected int getDirectSignal(BlockState state, BlockGetter world, BlockPos pos, Direction direction) {
        return getSignal(state, world, pos, direction);
    }

    protected static Direction getDirection(BlockState state) {
        return switch ((AttachFace) state.getValue(FACE)) {
            case CEILING -> Direction.DOWN;
            case FLOOR -> Direction.UP;
            default -> state.getValue(FACING);
        };
    }

    @Override
    protected InteractionResult useWithoutItem(BlockState state, Level world, BlockPos pos, Player player, BlockHitResult hit) {
        if (!(world.getBlockEntity(pos) instanceof KeypadBlockEntity keypadBlockEntity)) {
            return super.useWithoutItem(state, world, pos, player, hit);
        }
        BlockEntity blockEntity = world.getBlockEntity(pos);
        if (world.isClientSide()) {
            if(state.getValue(PASSWORD_SET) && state.getValue(POWERED)) {
                player.displayClientMessage(Component.translatable("chat."+ Keypad.MOD_ID+".active"),false);
            }else{
                ((CustomPlayerInterface) player).openKeypadScreen(blockEntity);
            }
        }else{
            world.setBlock(pos, state.setValue(POWERED, Objects.equals(keypadBlockEntity.getPassword(), keypadBlockEntity.getPasswordSet())),1);
            updateTargets(world,pos);
        }
        return InteractionResult.SUCCESS;
    }

    @Override
    protected void onRemove(BlockState state, Level world, BlockPos pos, BlockState newState, boolean moved) {
        if (moved || state.is(newState.getBlock())) return;
        super.onRemove(state, world, pos, newState, moved);
        this.updateTargets(world, pos);
    }

    public void updateTarget(Level world, BlockPos pos, Direction direction) {
        BlockPos blockPos = pos.relative(direction);
        Orientation wireOrientation = ExperimentalRedstoneUtils.initialOrientation(world, direction, Direction.UP);
        world.neighborChanged(blockPos, this, wireOrientation);
        world.updateNeighborsAtExceptFromFacing(blockPos, this, direction.getOpposite(), wireOrientation);
    }

    public void updateTargets(Level world, BlockPos pos) {
        for (Direction direction : Direction.values()) {
            updateTarget(world, pos, direction);
        }
    }

    @Override
    public void tick(BlockState state, ServerLevel world, BlockPos pos, RandomSource random){
        KeypadBlockEntity entity = (KeypadBlockEntity) world.getBlockEntity(pos);
        if (state.getValue(BlockStateProperties.POWERED)) {
            assert entity != null;
            entity.setPassword("",false);
        }
        super.tick(state,world,pos,random);
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext ctx) {
//        Direction direction = ctx.getPlayerLookDirection().getOpposite();
        for (Direction direction : ctx.getNearestLookingDirections()) {
            BlockState blockState;
            if (direction.getAxis() == Direction.Axis.Y) {
                blockState = this.defaultBlockState()
                        .setValue(FACE, direction == Direction.UP ? AttachFace.CEILING : AttachFace.FLOOR)
                        .setValue(FACING, ctx.getHorizontalDirection());
            } else {
                blockState = this.defaultBlockState().setValue(FACE, AttachFace.WALL).setValue(FACING, direction.getOpposite());
            }

            if (blockState.canSurvive(ctx.getLevel(), ctx.getClickedPos())) {
                return blockState;
            }
        }

        return null;
    }

    protected static final VoxelShape NORTH_WALL_SHAPE = Block.box(3, 1, 14, 13, 14, 16);
    protected static final VoxelShape SOUTH_WALL_SHAPE = Block.box(3, 1, 0, 13, 14, 2);
    protected static final VoxelShape EAST_WALL_SHAPE = Block.box(0, 1, 3, 2, 14, 13);
    protected static final VoxelShape WEST_WALL_SHAPE = Block.box(14, 1, 3, 16, 14, 13);

    protected static final VoxelShape NORTH_CEILING_SHAPE = Block.box(3, 14, 1, 13, 16.0, 14);
    protected static final VoxelShape SOUTH_CEILING_SHAPE = Block.box(3, 14, 2, 13, 16.0, 15);
    protected static final VoxelShape EAST_CEILING_SHAPE = Block.box(2, 14, 3, 15, 16.0, 13);
    protected static final VoxelShape WEST_CEILING_SHAPE = Block.box(1, 14, 3, 14, 16.0, 13);

    protected static final VoxelShape NORTH_FLOOR_SHAPE = Block.box(3, 0.0, 2, 13, 2, 15);
    protected static final VoxelShape SOUTH_FLOOR_SHAPE = Block.box(3, 0.0, 1, 13, 2, 14);
    protected static final VoxelShape EAST_FLOOR_SHAPE = Block.box(1, 0.0, 3, 14, 2, 13);
    protected static final VoxelShape WEST_FLOOR_SHAPE = Block.box(2, 0.0, 3, 15, 2, 13);

    @Override
    protected VoxelShape getShape(BlockState state, BlockGetter world, BlockPos pos, CollisionContext context) {
        return switch ((AttachFace) state.getValue(FACE)) {
            case FLOOR -> switch (((Direction) state.getValue(FACING))) {
                case EAST -> EAST_FLOOR_SHAPE;
                case WEST -> WEST_FLOOR_SHAPE;
                case SOUTH -> SOUTH_FLOOR_SHAPE;
                default -> NORTH_FLOOR_SHAPE;
            };
            case WALL -> switch ((Direction) state.getValue(FACING)) {
                case EAST -> EAST_WALL_SHAPE;
                case WEST -> WEST_WALL_SHAPE;
                case SOUTH -> SOUTH_WALL_SHAPE;
                default -> NORTH_WALL_SHAPE;
            };
            default -> switch (((Direction) state.getValue(FACING))) {
                case EAST -> EAST_CEILING_SHAPE;
                case WEST -> WEST_CEILING_SHAPE;
                case SOUTH -> SOUTH_CEILING_SHAPE;
                default -> NORTH_CEILING_SHAPE;
            };
        };
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder){
        builder.add(POWERED)
                .add(FACING)
                .add(FACE)
                .add(PASSWORD_SET);
    }
}
