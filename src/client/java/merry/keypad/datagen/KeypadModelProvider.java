package merry.keypad.datagen;

import com.mojang.math.Quadrant;
import merry.keypad.blocks.KeypadBlock;
import merry.keypad.blocks.ModBlocks;
import net.fabricmc.fabric.api.client.datagen.v1.provider.FabricModelProvider;
import net.fabricmc.fabric.api.datagen.v1.FabricPackOutput;
import net.minecraft.client.data.*;
import net.minecraft.client.data.models.BlockModelGenerators;
import net.minecraft.client.data.models.ItemModelGenerators;
import net.minecraft.client.data.models.blockstates.MultiVariantGenerator;
import net.minecraft.client.data.models.blockstates.PropertyDispatch;
import net.minecraft.client.data.models.model.ModelLocationUtils;
import net.minecraft.client.renderer.block.dispatch.VariantMutator;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.state.properties.AttachFace;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import static net.minecraft.client.data.models.BlockModelGenerators.plainVariant;

public class KeypadModelProvider extends FabricModelProvider {

    public static final VariantMutator NOP = variant -> variant;
    public static final VariantMutator X_ROT_90 = VariantMutator.X_ROT.withValue(Quadrant.R90);
    public static final VariantMutator X_ROT_270 = VariantMutator.X_ROT.withValue(Quadrant.R270);
    public static final VariantMutator Y_ROT_90 = VariantMutator.Y_ROT.withValue(Quadrant.R90);
    public static final VariantMutator Y_ROT_180 = VariantMutator.Y_ROT.withValue(Quadrant.R180);
    public static final VariantMutator Y_ROT_270 = VariantMutator.Y_ROT.withValue(Quadrant.R270);

    public KeypadModelProvider(FabricPackOutput output) {
        super(output);
    }

    @Override
    public void generateBlockStateModels(BlockModelGenerators blockStateModelGenerator) {
        blockStateModelGenerator.blockStateOutput.accept(MultiVariantGenerator.dispatch(ModBlocks.KEYPAD).with(
                PropertyDispatch.initial(KeypadBlock.PASSWORD_SET,KeypadBlock.POWERED).generate((passwordSet,powered) ->{
                    if(passwordSet){
                        if (powered){
                            return plainVariant(ModelLocationUtils.getModelLocation(ModBlocks.KEYPAD,"_on"));
                        }else{
                            return plainVariant(ModelLocationUtils.getModelLocation(ModBlocks.KEYPAD,"_off"));
                        }
                    }else {
                        return plainVariant(ModelLocationUtils.getModelLocation(ModBlocks.KEYPAD));
                    }
                })
        ).with(
                PropertyDispatch.modify(BlockStateProperties.ATTACH_FACE, BlockStateProperties.HORIZONTAL_FACING)
                        .select(AttachFace.CEILING, Direction.NORTH, X_ROT_90.then(Y_ROT_180))
                        .select(AttachFace.CEILING, Direction.EAST, X_ROT_90.then(Y_ROT_270))
                        .select(AttachFace.CEILING, Direction.SOUTH, X_ROT_90)
                        .select(AttachFace.CEILING, Direction.WEST, X_ROT_90.then(Y_ROT_90))
                        .select(AttachFace.FLOOR, Direction.NORTH, X_ROT_270.then(Y_ROT_180))
                        .select(AttachFace.FLOOR, Direction.EAST, X_ROT_270.then(Y_ROT_270))
                        .select(AttachFace.FLOOR, Direction.SOUTH, X_ROT_270)
                        .select(AttachFace.FLOOR, Direction.WEST, X_ROT_270.then(Y_ROT_90))
                        .select(AttachFace.WALL, Direction.NORTH, NOP)
                        .select(AttachFace.WALL, Direction.EAST, NOP.then(Y_ROT_90))
                        .select(AttachFace.WALL, Direction.SOUTH, NOP.then(Y_ROT_180))
                        .select(AttachFace.WALL, Direction.WEST, NOP.then(Y_ROT_270))
        ));
    }

    @Override
    public void generateItemModels(ItemModelGenerators itemModelGenerators) {

    }

    @Override
    public String getName() {
        return "FabricDocsReference Model Provider";
    }
}
