package me.jellysquid.mods.sodium.client.world.chunk.light;

import me.jellysquid.mods.sodium.client.SodiumClientMod;
import me.jellysquid.mods.sodium.client.world.SodiumChunkManager;
import net.minecraft.client.MinecraftClient;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.util.math.ChunkSectionPos;
import net.minecraft.world.LightType;
import net.minecraft.world.chunk.ChunkNibbleArray;
import net.minecraft.world.chunk.ChunkProvider;
import net.minecraft.world.chunk.light.ChunkLightingView;
import net.minecraft.world.chunk.light.LightingProvider;
import net.minecraft.world.chunk.light.LightingView;
import org.jetbrains.annotations.Nullable;

public class DynamicLightingProvider extends LightingProvider {
    private final LightingProvider lightingProvider;
    private final FixedLightingProvider fixedLightingProvider;

    public DynamicLightingProvider(SodiumChunkManager chunkProvider,
                                   boolean hasBlockLight,
                                   boolean hasSkyLight) {
        super(chunkProvider, hasBlockLight, hasSkyLight);
        this.lightingProvider = new LightingProvider(chunkProvider, true, hasSkyLight);

        this.fixedLightingProvider = new FixedLightingProvider(chunkProvider, true, hasSkyLight);
    }

    private boolean isLightEnabled() {
        return SodiumClientMod.options().quality.enableLights;
    }

    private LightingProvider getLightingProvider() {
        return isLightEnabled() ? lightingProvider : fixedLightingProvider;
    }

    @Override
    public void setSectionStatus(BlockPos pos, boolean notReady) {
        getLightingProvider().setSectionStatus(pos, notReady);
    }

    @Override
    public String displaySectionLevel(LightType lightType, ChunkSectionPos chunkSectionPos) {
        return getLightingProvider().displaySectionLevel(lightType, chunkSectionPos);
    }

    @Override
    public int getLight(BlockPos pos, int ambientDarkness) {
        return getLightingProvider().getLight(pos, ambientDarkness);
    }

    @Override
    public ChunkLightingView get(LightType lightType) {
        return getLightingProvider().get(lightType);
    }

    @Override
    public boolean hasUpdates() {
        return getLightingProvider().hasUpdates();
    }

    @Override
    public int doLightUpdates(int maxUpdateCount, boolean doSkylight, boolean skipEdgeLightPropagation) {
        return getLightingProvider().doLightUpdates(maxUpdateCount, doSkylight, skipEdgeLightPropagation);
    }

    @Override
    public void addLightSource(BlockPos pos, int level) {
        getLightingProvider().addLightSource(pos, level);
    }

    @Override
    public void checkBlock(BlockPos pos) {
        getLightingProvider().checkBlock(pos);
    }

    @Override
    public void enqueueSectionData(LightType lightType,
                                   ChunkSectionPos pos,
                                   @Nullable ChunkNibbleArray nibbles,
                                   boolean bl) {
        getLightingProvider().enqueueSectionData(lightType, pos, nibbles, bl);
    }

    @Override
    public void setColumnEnabled(ChunkPos pos, boolean lightEnabled) {
        getLightingProvider().setColumnEnabled(pos, lightEnabled);
    }

    @Override
    public void setRetainData(ChunkPos pos, boolean retainData) {
        getLightingProvider().setRetainData(pos, retainData);
    }

    @Override
    public void setSectionStatus(ChunkSectionPos pos, boolean notReady) {
        getLightingProvider().setSectionStatus(pos, notReady);
    }
}
