package merry.keypad.datagen;

import merry.keypad.blocks.KeypadBlock;
import merry.keypad.blocks.ModBlocks;
import net.fabricmc.fabric.api.client.datagen.v1.provider.FabricModelProvider;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.minecraft.client.data.*;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Direction;

import java.util.Collections;

public class KeypadModelProvider extends FabricModelProvider {
    public KeypadModelProvider(FabricDataOutput output) {
        super(output);
    }

    @Override
    public void generateBlockStateModels(BlockStateModelGenerator blockStateModelGenerator) {
        blockStateModelGenerator.blockStateCollector.accept(VariantsBlockStateSupplier.create(ModBlocks.KEYPAD).coordinate(BlockStateVariantMap
                .create(KeypadBlock.FACE,KeypadBlock.FACING,KeypadBlock.POWERED,KeypadBlock.PASSWORD_SET)
                .registerVariants((face, facing, powered,passwordSet)-> {
                    String model = powered? "block/keypad_on" : "block/keypad_off";
                    model = passwordSet ? model : "block/keypad";

                    VariantSettings.Rotation yRotation = null;
                    VariantSettings.Rotation xRotation = null;

                    switch (face) {
                        case CEILING -> {
                            xRotation = VariantSettings.Rotation.R90;
                            yRotation = getRotation(facing, yRotation);
                        }
                        case WALL -> {
                            xRotation = VariantSettings.Rotation.R0;
                            switch(facing) {
                                case NORTH -> yRotation = VariantSettings.Rotation.R0;
                                case SOUTH -> yRotation = VariantSettings.Rotation.R180;
                                case WEST -> yRotation = VariantSettings.Rotation.R270;
                                case EAST -> yRotation = VariantSettings.Rotation.R90;
                            }
                        }
                        case FLOOR -> {
                            {
                                xRotation = VariantSettings.Rotation.R270;
                                yRotation = getRotation(facing, yRotation);
                            }
                        }
                    }
                    return Collections.singletonList(BlockStateVariant.create().put(VariantSettings.MODEL, Identifier.of("keypad", model)).put(VariantSettings.Y, yRotation).put(VariantSettings.X, xRotation));
                })));
    }

    private VariantSettings.Rotation getRotation(Direction facing, VariantSettings.Rotation yRotation) {
        switch(facing) {
            case NORTH -> yRotation = VariantSettings.Rotation.R180;
            case SOUTH -> yRotation = VariantSettings.Rotation.R0;
            case WEST -> yRotation = VariantSettings.Rotation.R90;
            case EAST -> yRotation = VariantSettings.Rotation.R270;
        }
        return yRotation;
    }

    @Override
    public void generateItemModels(ItemModelGenerator itemModelGenerator) {
    }

    @Override
    public String getName() {
        return "FabricDocsReference Model Provider";
    }
}
