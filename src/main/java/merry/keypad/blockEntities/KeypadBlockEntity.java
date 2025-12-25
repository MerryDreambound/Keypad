package merry.keypad.blockEntities;

import merry.keypad.blocks.KeypadBlock;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import java.util.Objects;

public class KeypadBlockEntity extends BlockEntity {
    private String password = "";
    private String passwordSet = "";

    public KeypadBlockEntity(BlockPos pos, BlockState state) {
        super(ModBlockEntities.COUNTER_BLOCK_ENTITY, pos, state);
    }

    public String getPassword() {
        return password;
    }
    public String getPasswordSet() {
        return passwordSet;
    }

    public void setPasswordSet(String passwordSet) {
        this.passwordSet = passwordSet;
        this.password = "";
        setChanged();
    }
    public void setPassword(String password, Boolean scheduleTick) {
        if (password == null){
            return;
        }
        this.password = password;
        setChanged();

        assert level != null;
        BlockState state = level.getBlockState(worldPosition);
        if (state.getBlock() instanceof KeypadBlock keypadBlock) {
            if(!this.passwordSet.isEmpty()){
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
        nbt.putString("passwordSet", passwordSet);
        nbt.putString("password", password);
    }

    @Override
    protected void loadAdditional(CompoundTag nbt, HolderLookup.Provider registryLookup) {
        super.loadAdditional(nbt, registryLookup);
        password = nbt.getStringOr("password","");
        passwordSet = nbt.getStringOr("passwordSet","");

    }

}

