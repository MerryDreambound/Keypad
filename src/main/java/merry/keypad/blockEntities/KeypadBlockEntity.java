package merry.keypad.blockEntities;

import merry.keypad.blocks.KeypadBlock;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;

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
    protected void saveAdditional(ValueOutput output) {
        super.saveAdditional(output);
        output.putString("passwordSet",String.valueOf(passwordSet));
        output.putString("password", String.valueOf(password));
    }

    @Override
    protected void loadAdditional(ValueInput input) {
        super.loadAdditional(input);
        password = input.getString("password").toString();
        passwordSet = input.getString("passwordSet").toString();
    }

}

