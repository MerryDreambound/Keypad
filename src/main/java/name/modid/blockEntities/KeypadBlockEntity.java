package name.modid.blockEntities;

import name.modid.blocks.KeypadBlock;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.util.math.BlockPos;

import java.util.Objects;

public class KeypadBlockEntity extends BlockEntity {
    private String password = "";
    private String passwordSet ="";

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
        markDirty();
    }
    public void setPassword(String password) {
        this.password = password;
        markDirty();

        assert world != null;
        BlockState state = world.getBlockState(pos);
        if (state.getBlock() instanceof KeypadBlock keypadBlock) {
            if(!this.passwordSet.isEmpty()){
                world.setBlockState(pos, state.with(KeypadBlock.POWERED, Objects.equals(this.password, this.passwordSet)), 3);
                keypadBlock.updateTargets(world, pos);
                world.scheduleBlockTick(pos,keypadBlock,100);
            }
        }
    }
    @Override
    protected void writeNbt(NbtCompound nbt, RegistryWrapper.WrapperLookup registryLookup) {
        super.writeNbt(nbt, registryLookup);
        nbt.putString("passwordSet", passwordSet);
        nbt.putString("password", password);
    }

    @Override
    protected void readNbt(NbtCompound nbt, RegistryWrapper.WrapperLookup registryLookup) {
        super.readNbt(nbt, registryLookup);
        password = nbt.getString("password");
        passwordSet = nbt.getString("passwordSet");

    }

}

