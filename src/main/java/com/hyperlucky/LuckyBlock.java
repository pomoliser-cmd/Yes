package com.hyperlucky;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.TntEntity;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.mob.CreeperEntity;
import net.minecraft.entity.passive.ChickenEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.Random;

public class LuckyBlock extends Block {
    private static final Random RNG = new Random();

    public LuckyBlock(Settings settings) {
        super(settings);
    }

    @Override
    public void afterBreak(World world, PlayerEntity player, BlockPos pos, BlockState state, @Nullable BlockEntity blockEntity, ItemStack tool) {
        super.afterBreak(world, player, pos, state, blockEntity, tool);

        if (!world.isClient() && world instanceof ServerWorld serverWorld) {
            triggerLuckyEvent(serverWorld, pos, player);
        }
    }

    private void triggerLuckyEvent(ServerWorld world, BlockPos pos, PlayerEntity player) {
        int event = RNG.nextInt(7);

        switch (event) {
            case 0 -> { // Jackpot
                dropItem(world, pos, new ItemStack(Items.DIAMOND, 6));
                dropItem(world, pos, new ItemStack(Items.EMERALD, 12));
                dropItem(world, pos, new ItemStack(Items.ENCHANTED_GOLDEN_APPLE, 1));
                world.playSound(null, pos, SoundEvents.ENTITY_PLAYER_LEVELUP, SoundCategory.BLOCKS, 1.0f, 1.0f);
            }
            case 1 -> { // Troll Bomb
                TntEntity tnt = new TntEntity(EntityType.TNT, world);
                tnt.setPosition(pos.getX() + 0.5, pos.getY(), pos.getZ() + 0.5);
                tnt.setFuse(30);
                world.spawnEntity(tnt);
                world.playSound(null, pos, SoundEvents.ENTITY_TNT_PRIMED, SoundCategory.BLOCKS, 1.0f, 1.0f);
            }
            case 2 -> { // Anvil Guillotine
                BlockPos anvilPos = player.getBlockPos().up(7);
                world.setBlockState(anvilPos, Blocks.DAMAGED_ANVIL.getDefaultState());
                world.playSound(null, anvilPos, SoundEvents.BLOCK_ANVIL_FALL, SoundCategory.BLOCKS, 1.0f, 1.0f);
            }
            case 3 -> { // Creeper Ambush
                for (int i = 0; i < 3; i++) {
                    CreeperEntity creeper = EntityType.CREEPER.create(world, null, null, null, null, false, false);
                    if (creeper != null) {
                        creeper.refreshPositionAndAngles(pos.getX() + (RNG.nextDouble() * 2 - 1), pos.getY(), pos.getZ() + (RNG.nextDouble() * 2 - 1), 0, 0);
                        world.spawnEntity(creeper);
                    }
                }
            }
            case 4 -> { // Gambler's Blade
                dropItem(world, pos, new ItemStack(Items.DIAMOND_SWORD));
                player.addStatusEffect(new StatusEffectInstance(StatusEffects.SPEED, 400, 1));
                player.addStatusEffect(new StatusEffectInstance(StatusEffects.STRENGTH, 400, 1));
                world.playSound(null, pos, SoundEvents.ITEM_ARMOR_EQUIP_DIAMOND, SoundCategory.PLAYERS, 1.0f, 1.0f);
            }
            case 5 -> { // Yeet Trap
                player.addStatusEffect(new StatusEffectInstance(StatusEffects.LEVITATION, 100, 2));
                world.playSound(null, player.getBlockPos(), SoundEvents.ENTITY_FIREWORK_ROCKET_LAUNCH, SoundCategory.PLAYERS, 1.0f, 1.0f);
            }
            case 6 -> { // Chicken Swarm
                for (int i = 0; i < 12; i++) {
                    ChickenEntity chicken = EntityType.CHICKEN.create(world, null, null, null, null, false, false);
                    if (chicken != null) {
                        chicken.refreshPositionAndAngles(pos.getX() + 0.5, pos.getY(), pos.getZ() + 0.5, 0, 0);
                        world.spawnEntity(chicken);
                    }
                }
                world.playSound(null, pos, SoundEvents.ENTITY_CHICKEN_EGG, SoundCategory.NEUTRAL, 1.0f, 1.0f);
            }
        }
    }

    private void dropItem(ServerWorld world, BlockPos pos, ItemStack stack) {
        ItemEntity entity = new ItemEntity(world, pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5, stack);
        entity.setToDefaultPickupDelay();
        world.spawnEntity(entity);
    }
                          }
