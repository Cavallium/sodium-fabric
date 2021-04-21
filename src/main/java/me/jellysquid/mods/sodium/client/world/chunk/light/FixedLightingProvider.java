package me.jellysquid.mods.sodium.client.world.chunk.light;

import java.util.function.IntSupplier;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.world.ClientChunkManager;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.util.math.ChunkSectionPos;
import net.minecraft.world.LightType;
import net.minecraft.world.World;
import net.minecraft.world.WorldView;
import net.minecraft.world.chunk.ChunkNibbleArray;
import net.minecraft.world.chunk.light.ChunkLightingView;
import net.minecraft.world.chunk.light.LightingProvider;
import org.jetbrains.annotations.Nullable;

public class FixedLightingProvider extends LightingProvider {

    private final FixedChunkLightingView fixedChunkLightingView;

    public FixedLightingProvider(ClientChunkManager sodiumChunkManager, boolean hasBlockLight, boolean hasSkyLight) {
        super(sodiumChunkManager, hasBlockLight, hasSkyLight);
        this.fixedChunkLightingView = new FixedChunkLightingView(sodiumChunkManager);
    }

    private int getFixedLightLevel() {
        return (int) (MinecraftClient.getInstance().options.gamma * 15.0d);
    }

    private class FixedChunkLightingView implements net.minecraft.world.chunk.light.ChunkLightingView {

        private final ClientChunkManager sodiumChunkManager;

        public FixedChunkLightingView(ClientChunkManager sodiumChunkManager) {
            this.sodiumChunkManager = sodiumChunkManager;
        }

        @Nullable
        @Override
        public ChunkNibbleArray getLightSection(ChunkSectionPos pos) {
            return null;
        }

        @Override
        public int getLightLevel(BlockPos blockPos) {
            WorldView w = (WorldView) sodiumChunkManager.getWorld();
            return getFixedLightLevel() - w.getAmbientDarkness();
        }

        @Override
        public void setSectionStatus(ChunkSectionPos pos, boolean notReady) {

        }
    }

    public void checkBlock(BlockPos pos) {

    }

    @Override
    public ChunkLightingView get(LightType lightType) {
        if (lightType == LightType.BLOCK) {
            return ChunkLightingView.Empty.INSTANCE;
        } else {
            return fixedChunkLightingView;
        }
    }

    @Override
    public int getLight(BlockPos pos, int ambientDarkness) {
        return getFixedLightLevel() - ambientDarkness;
    }

    @Override
    public int doLightUpdates(int maxUpdateCount, boolean doSkylight, boolean skipEdgeLightPropagation) {
        return maxUpdateCount;
    }

    @Override
    public void addLightSource(BlockPos pos, int level) {

    }

    @Override
    public boolean hasUpdates() {
        return false;
    }

    @Override
    public String displaySectionLevel(LightType lightType, ChunkSectionPos chunkSectionPos) {
        return "" + getFixedLightLevel();
    }

    @Override
    public void enqueueSectionData(LightType lightType,
                                   ChunkSectionPos pos,
                                   @Nullable ChunkNibbleArray nibbles,
                                   boolean bl) {
    }

    @Override
    public void setColumnEnabled(ChunkPos pos, boolean lightEnabled) {
    }

    @Override
    public void setRetainData(ChunkPos pos, boolean retainData) {

    }

    @Override
    public void setSectionStatus(BlockPos pos, boolean notReady) {

    }

    @Override
    public void setSectionStatus(ChunkSectionPos pos, boolean notReady) {

    }
}
