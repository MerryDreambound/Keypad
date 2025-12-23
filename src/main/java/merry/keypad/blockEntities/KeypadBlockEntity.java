package merry.keypad.blockEntities;

import merry.keypad.blocks.KeypadBlock;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import java.util.Objects;
import java.util.Optional;

public class KeypadBlockEntity extends BlockEntity {
    private Optional<String> password = Optional.empty();
    private Optional<String> passwordSet =Optional.empty();

    public KeypadBlockEntity(BlockPos pos, BlockState state) {
        super(ModBlockEntities.COUNTER_BLOCK_ENTITY, pos, state);
    }

    public Optional<String> getPassword() {
        return password;
    }
    public Optional<String> getPasswordSet() {
        return passwordSet;
    }

    public void setPasswordSet(Optional<String> passwordSet) {
        this.passwordSet = passwordSet;
        this.password = Optional.empty();
        setChanged();
    }
    public void setPassword(Optional<String> password, Boolean scheduleTick) {
        this.password = password;
        setChanged();

        assert level != null;
        BlockState state = level.getBlockState(worldPosition);
        if (state.getBlock() instanceof KeypadBlock keypadBlock) {
            if(this.passwordSet.isPresent()){
                level.setBlock(worldPosition, state.setValue(KeypadBlock.POWERED, Objects.equals(this.password, this.passwordSet)), 3);
                keypadBlock.updateTargets(level, worldPosition);
                if(scheduleTick){
                    level.scheduleTick(worldPosition,keypadBlock,100);
                }
            }
        }
    }

    @Override
    protected void saveAdditional(CompoundTag nbt, HolderLookup.Provider registryLookup) {
        super.saveAdditional(nbt, registryLookup);
        nbt.putString("passwordSet", String.valueOf(passwordSet));
        nbt.putString("password", String.valueOf(password));
    }

    @Override
    protected void loadAdditional(CompoundTag nbt, HolderLookup.Provider registryLookup) {
        super.loadAdditional(nbt, registryLookup);
        password = nbt.getString("password");
        passwordSet = nbt.getString("passwordSet");

    }

}

