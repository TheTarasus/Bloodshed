package ru.thetarasus.bloodshed;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.blockrenderlayer.v1.BlockRenderLayerMap;
import net.fabricmc.fabric.api.client.rendering.v1.EntityRendererRegistry;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.entity.FlyingItemEntityRenderer;

public class BloodshedClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        EntityRendererRegistry.register(Bloodshed.BLOOD_ENTITY, FlyingItemEntityRenderer::new);
        BlockRenderLayerMap.INSTANCE.putBlock(Bloodshed.BLOOD_BLOCK, RenderLayer.getTranslucent());
    }
}