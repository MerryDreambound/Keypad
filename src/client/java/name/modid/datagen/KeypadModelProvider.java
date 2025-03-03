package name.modid.datagen;

import name.modid.Keypad;
import name.modid.blocks.KeypadBlock;
import name.modid.blocks.ModBlocks;
import net.fabricmc.fabric.api.client.datagen.v1.provider.FabricModelProvider;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.minecraft.client.data.*;
import net.minecraft.state.property.Properties;
import net.minecraft.util.math.Direction;

import java.util.Collections;

public class KeypadModelProvider extends FabricModelProvider {
    public KeypadModelProvider(FabricDataOutput output) {
        super(output);
    }

    @Override
    public void generateBlockStateModels(BlockStateModelGenerator blockStateModelGenerator) {
//        blockStateModelGenerator.registerRotatable(ModBlocks.KEYPAD);
        blockStateModelGenerator.blockStateCollector.accept(VariantsBlockStateSupplier.create(ModBlocks.KEYPAD).coordinate(BlockStateVariantMap
                .create(KeypadBlock.FACE,KeypadBlock.FACING,KeypadBlock.POWERED)
                .registerVariants((face, facing, powered)-> Collections.singletonList(BlockStateVariant.create()))));

//        blockStateModelGenerator.blockStateCollector.accept(MultipartBlockStateSupplier.create(ModBlocks.KEYPAD).with(When.create().set(Properties.FACING, Direction.NORTH),BlockStateVariant.create().put(VariantSettings.X,VariantSettings.Rotation.R0)));
    }


    @Override
    public void generateItemModels(ItemModelGenerator itemModelGenerator) {
    }

    @Override
    public String getName() {
        return "FabricDocsReference Model Provider";
    }
}
