package com.hyperlucky;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroups;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.sound.BlockSoundGroup;
import net.minecraft.util.Identifier;

public class HyperLuckyMod implements ModInitializer {
    public static final String MOD_ID = "hyperlucky";

    public static final Block LUCKY_BLOCK = new LuckyBlock(
            AbstractBlock.Settings.create()
                    .strength(1.2f)
                    .sounds(BlockSoundGroup.STONE)
    );

    public static final BlockItem LUCKY_BLOCK_ITEM = new BlockItem(
            LUCKY_BLOCK,
            new Item.Settings()
    );

    @Override
    public void onInitialize() {
        Identifier id = Identifier.of(MOD_ID, "lucky_block");

        Registry.register(Registries.BLOCK, id, LUCKY_BLOCK);
        Registry.register(Registries.ITEM, id, LUCKY_BLOCK_ITEM);

        ItemGroupEvents.modifyEntriesEvent(ItemGroups.BUILDING_BLOCKS).register(entries -> {
            entries.add(LUCKY_BLOCK_ITEM);
        });
    }
}
