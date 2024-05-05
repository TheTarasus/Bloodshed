package ru.thetarasus.bloodshed;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricEntityTypeBuilder;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Material;
import net.minecraft.entity.EntityDimensions;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnGroup;
import net.minecraft.item.Item;
import net.minecraft.sound.BlockSoundGroup;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

public class Bloodshed implements ModInitializer {

	public static String MOD_ID = "bloodshed";

	public static BloodBlock BLOOD_BLOCK = new BloodBlock(AbstractBlock.Settings.of(Material.ORGANIC_PRODUCT).noCollision().nonOpaque().blockVision((blockState, blockView, blockPos) -> {
		return false;
	}).suffocates((blockState, blockView, blockPos)->{
		return false;
	}).sounds(BlockSoundGroup.SLIME).velocityMultiplier(0.75f));

	public static Item BLOOD_ITEM = new Item(new FabricItemSettings());

	public static EntityType<BloodEntity> BLOOD_ENTITY = Registry.register(Registry.ENTITY_TYPE, new Identifier(MOD_ID, "blood_entity"), FabricEntityTypeBuilder.create(SpawnGroup.AMBIENT, BloodEntity::new).dimensions(EntityDimensions.fixed(0.25f, 0.25f)).build());

	@Override
	public void onInitialize() {
		Registry.register(Registry.ITEM, new Identifier(MOD_ID, "blood"), BLOOD_ITEM);
		Registry.register(Registry.BLOCK, new Identifier(MOD_ID, "blood"), BLOOD_BLOCK);

	}
}
