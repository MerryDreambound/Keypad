package name.modid.blockEntities;

import name.modid.Keypad;
import name.modid.blocks.ModBlocks;
import net.fabricmc.fabric.api.object.builder.v1.block.entity.FabricBlockEntityTypeBuilder;
import net.minecraft.block.Block;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

public class ModBlockEntities {
    public static final BlockEntityType<KeypadBlockEntity> COUNTER_BLOCK_ENTITY =
            register("counter", KeypadBlockEntity::new, ModBlocks.KEYPAD);

    private static <T extends BlockEntity> BlockEntityType<T> register(
            String name,
            FabricBlockEntityTypeBuilder.Factory<? extends T> entityFactory,
            Block... blocks
    ) {
        Identifier id = Identifier.of(Keypad.MOD_ID, name);
        return Registry.register(Registries.BLOCK_ENTITY_TYPE, id, FabricBlockEntityTypeBuilder.<T>create(entityFactory, blocks).build());
    }
    public static void initialize() {}
}
