package merry.keypad.datagen;

import com.mojang.math.Quadrant;
import merry.keypad.blocks.KeypadBlock;
import merry.keypad.blocks.ModBlocks;
import net.fabricmc.fabric.api.client.datagen.v1.provider.FabricModelProvider;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.minecraft.client.data.*;
import net.minecraft.client.data.models.BlockModelGenerators;
import net.minecraft.client.data.models.ItemModelGenerators;
import net.minecraft.client.data.models.blockstates.MultiVariantGenerator;
import net.minecraft.client.data.models.blockstates.PropertyDispatch;
import net.minecraft.client.data.models.blockstates.PropertyValueList;
import net.minecraft.client.renderer.block.model.Variant;
import net.minecraft.client.renderer.block.model.VariantMutator;
import net.minecraft.core.Direction;
import net.minecraft.data.BlockFamily;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Rotation;

import java.util.Collections;

public class KeypadModelProvider extends FabricModelProvider {

    public static final VariantMutator NOP = variant -> variant;
    public static final VariantMutator UV_LOCK = VariantMutator.UV_LOCK.withValue(true);
    public static final VariantMutator X_ROT_90 = VariantMutator.X_ROT.withValue(Quadrant.R90);
    public static final VariantMutator X_ROT_180 = VariantMutator.X_ROT.withValue(Quadrant.R180);
    public static final VariantMutator X_ROT_270 = VariantMutator.X_ROT.withValue(Quadrant.R270);
    public static final VariantMutator Y_ROT_90 = VariantMutator.Y_ROT.withValue(Quadrant.R90);
    public static final VariantMutator Y_ROT_180 = VariantMutator.Y_ROT.withValue(Quadrant.R180);
    public static final VariantMutator Y_ROT_270 = VariantMutator.Y_ROT.withValue(Quadrant.R270);

    public KeypadModelProvider(FabricDataOutput output) {
        super(output);
    }

    @Override
    public void generateBlockStateModels(BlockModelGenerators blockStateModelGenerator) {
        blockStateModelGenerator.blockStateOutput.accept(MultiVariantGenerator.dispatch(ModBlocks.KEYPAD).with(PropertyDispatch
                .initial(KeypadBlock.FACE,KeypadBlock.FACING,KeypadBlock.POWERED,KeypadBlock.PASSWORD_SET)
                .generate((face, facing, powered,passwordSet)-> {
                    String model = powered? "block/keypad_on" : "block/keypad_off";
                    model = passwordSet ? model : "block/keypad";
                    VariantMutator yRotation = NOP;
                    VariantMutator xRotation = NOP;


                    switch (face) {
                        case CEILING -> {
                            xRotation = X_ROT_90;
                            yRotation = getRotation(facing, yRotation);
                        }
                        case WALL -> {
                            xRotation = NOP;
                            switch(facing) {
                                case NORTH -> yRotation = NOP;
                                case SOUTH -> yRotation = Y_ROT_180;
                                case WEST -> yRotation = Y_ROT_270;
                                case EAST -> yRotation = Y_ROT_90;
                            }
                        }
                        case FLOOR -> {
                            {
                                xRotation = X_ROT_270;
                                yRotation = getRotation(facing, yRotation);
                            }
                        }
                    }
//                    return VariantMutator.MODEL.withValue(ResourceLocation.fromNamespaceAndPath("keypad", model)).then(VariantMutator.Y_ROT.withValue(yRotation));
//                    new Variant(ResourceLocation.fromNamespaceAndPath("keypad", model)).with(yRotation).with(xRotation);
                    return new Variant(ResourceLocation.fromNamespaceAndPath("keypad", model)).with(yRotation).with(xRotation);
                })));
    }

    private VariantMutator getRotation(Direction facing, VariantMutator yRotation) {
        switch(facing) {
            case NORTH -> yRotation = Y_ROT_180;
            case SOUTH -> yRotation = NOP;
            case WEST -> yRotation = Y_ROT_90;
            case EAST -> yRotation = Y_ROT_270;
        }
        return yRotation;
    }

    @Override
    public void generateItemModels(ItemModelGenerators itemModelGenerator) {
    }

    @Override
    public String getName() {
        return "FabricDocsReference Model Provider";
    }
}
