package merry.keypad.datagen;

import merry.keypad.blocks.KeypadBlock;
import merry.keypad.blocks.ModBlocks;
import net.fabricmc.fabric.api.client.datagen.v1.provider.FabricModelProvider;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.minecraft.client.data.*;
import net.minecraft.client.data.models.BlockModelGenerators;
import net.minecraft.client.data.models.ItemModelGenerators;
import net.minecraft.client.data.models.blockstates.MultiVariantGenerator;
import net.minecraft.client.data.models.blockstates.PropertyDispatch;
import net.minecraft.client.data.models.blockstates.Variant;
import net.minecraft.client.data.models.blockstates.VariantProperties;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;

import java.util.Collections;

public class KeypadModelProvider extends FabricModelProvider {
    public KeypadModelProvider(FabricDataOutput output) {
        super(output);
    }

    @Override
    public void generateBlockStateModels(BlockModelGenerators blockStateModelGenerator) {
        blockStateModelGenerator.blockStateOutput.accept(MultiVariantGenerator.multiVariant(ModBlocks.KEYPAD).with(PropertyDispatch
                .properties(KeypadBlock.FACE,KeypadBlock.FACING,KeypadBlock.POWERED,KeypadBlock.PASSWORD_SET)
                .generate((face, facing, powered,passwordSet)-> {
                    String model = powered? "block/keypad_on" : "block/keypad_off";
                    model = passwordSet ? model : "block/keypad";

                    VariantProperties.Rotation yRotation = null;
                    VariantProperties.Rotation xRotation = null;

                    switch (face) {
                        case CEILING -> {
                            xRotation = VariantProperties.Rotation.R90;
                            yRotation = getRotation(facing, yRotation);
                        }
                        case WALL -> {
                            xRotation = VariantProperties.Rotation.R0;
                            switch(facing) {
                                case NORTH -> yRotation = VariantProperties.Rotation.R0;
                                case SOUTH -> yRotation = VariantProperties.Rotation.R180;
                                case WEST -> yRotation = VariantProperties.Rotation.R270;
                                case EAST -> yRotation = VariantProperties.Rotation.R90;
                            }
                        }
                        case FLOOR -> {
                            {
                                xRotation = VariantProperties.Rotation.R270;
                                yRotation = getRotation(facing, yRotation);
                            }
                        }
                    }
                    return Variant.variant().with(VariantProperties.MODEL, ResourceLocation.fromNamespaceAndPath("keypad", model)).with(VariantProperties.Y_ROT, yRotation).with(VariantProperties.X_ROT, xRotation);
                })));
    }

    private VariantProperties.Rotation getRotation(Direction facing, VariantProperties.Rotation yRotation) {
        switch(facing) {
            case NORTH -> yRotation = VariantProperties.Rotation.R180;
            case SOUTH -> yRotation = VariantProperties.Rotation.R0;
            case WEST -> yRotation = VariantProperties.Rotation.R90;
            case EAST -> yRotation = VariantProperties.Rotation.R270;
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
