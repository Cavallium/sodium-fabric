package me.jellysquid.mods.sodium.client.world.chunk.light;

import java.util.function.IntSupplier;

import me.jellysquid.mods.sodium.client.world.SodiumChunkManager;
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

    private final IntSupplier lightLevelSupplier;
    private final FixedChunkLightingView fixedChunkLightingView;

    public FixedLightingProvider(SodiumChunkManager sodiumChunkManager, boolean hasBlockLight, boolean hasSkyLight, IntSupplier lightLevelSupplier) {
        super(sodiumChunkManager, hasBlockLight, hasSkyLight);
        this.lightLevelSupplier = lightLevelSupplier;
        this.fixedChunkLightingView = new FixedChunkLightingView(sodiumChunkManager, lightLevelSupplier);
    }

    private static class FixedChunkLightingView implements net.minecraft.world.chunk.light.ChunkLightingView {

        private final SodiumChunkManager sodiumChunkManager;
        private final IntSupplier lightLevelSupplier;

        public FixedChunkLightingView(SodiumChunkManager sodiumChunkManager, IntSupplier lightLevelSupplier) {
            this.sodiumChunkManager = sodiumChunkManager;
            this.lightLevelSupplier = lightLevelSupplier;
        }

        @Nullable
        @Override
        public ChunkNibbleArray getLightSection(ChunkSectionPos pos) {
            return null;
        }

        @Override
        public int getLightLevel(BlockPos blockPos) {
            WorldView w = (WorldView) sodiumChunkManager.getWorld();
            return lightLevelSupplier.getAsInt() - w.getAmbientDarkness();
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
        return lightLevelSupplier.getAsInt() - ambientDarkness;
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
        return "" + lightLevelSupplier.getAsInt();
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
