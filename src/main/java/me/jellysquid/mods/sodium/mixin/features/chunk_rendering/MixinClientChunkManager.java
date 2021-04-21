package me.jellysquid.mods.sodium.mixin.features.chunk_rendering;

import java.util.function.Supplier;

import me.jellysquid.mods.sodium.client.world.ChunkStatusListener;
import me.jellysquid.mods.sodium.client.world.ChunkStatusListenerManager;
import me.jellysquid.mods.sodium.client.world.chunk.light.DynamicLightingProvider;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.client.render.WorldRenderer;
import net.minecraft.client.world.ClientChunkManager;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.profiler.Profiler;
import net.minecraft.util.registry.RegistryKey;
import net.minecraft.world.World;
import net.minecraft.world.biome.source.BiomeArray;
import net.minecraft.world.chunk.WorldChunk;
import net.minecraft.world.chunk.light.LightingProvider;
import net.minecraft.world.dimension.DimensionType;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.concurrent.atomic.AtomicReferenceArray;

@Mixin(ClientChunkManager.class)
public abstract class MixinClientChunkManager implements ChunkStatusListenerManager {
    private ChunkStatusListener listener;
    @Shadow
    @Final
    private LightingProvider lightingProvider;

    @Inject(method = "<init>", at = @At("RETURN"))
    private void init(ClientWorld world, int loadDistance, CallbackInfo ci) {
        //noinspection ConstantConditions
        this.lightingProvider = new DynamicLightingProvider((ClientChunkManager) (Object) this, true, world.getDimension().hasSkyLight());
    }

    @Inject(method = "loadChunkFromPacket", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/world/ClientWorld;resetChunkColor(II)V", shift = At.Shift.AFTER))
    private void afterLoadChunkFromPacket(int x, int z, BiomeArray biomes, PacketByteBuf buf, CompoundTag tag, int verticalStripBitmask, boolean complete, CallbackInfoReturnable<WorldChunk> cir) {
        if (this.listener != null) {
            this.listener.onChunkAdded(x, z);
        }
    }

    @Inject(method = "unload", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/world/ClientChunkManager$ClientChunkMap;compareAndSet(ILnet/minecraft/world/chunk/WorldChunk;Lnet/minecraft/world/chunk/WorldChunk;)Lnet/minecraft/world/chunk/WorldChunk;", shift = At.Shift.AFTER))
    private void afterUnloadChunk(int x, int z, CallbackInfo ci) {
        if (this.listener != null) {
            this.listener.onChunkRemoved(x, z);
        }
    }

    @Override
    public void setListener(ChunkStatusListener listener) {
        this.listener = listener;
    }

    @Mixin(targets = "net/minecraft/client/world/ClientChunkManager$ClientChunkMap")
    public static class MixinClientChunkMap {
        @Mutable
        @Shadow
        @Final
        private AtomicReferenceArray<WorldChunk> chunks;

        @Mutable
        @Shadow
        @Final
        private int diameter;

        @Mutable
        @Shadow
        @Final
        private int radius;

        private int factor;

        @Inject(method = "<init>", at = @At("RETURN"))
        private void reinit(ClientChunkManager outer, int loadDistance, CallbackInfo ci) {
            // This re-initialization is a bit expensive on memory, but it only happens when either the world is
            // switched or the render distance is changed;
            this.radius = loadDistance;

            // Make the diameter a power-of-two so we can exploit bit-wise math when computing indices
            this.diameter = MathHelper.smallestEncompassingPowerOfTwo(loadDistance * 2 + 1);

            // The factor is used as a bit mask to replace the modulo in getIndex
            this.factor = this.diameter - 1;

            this.chunks = new AtomicReferenceArray<>(this.diameter * this.diameter);
        }

        /**
         * @reason Avoid expensive modulo
         * @author JellySquid
         */
        @Overwrite
        private int getIndex(int chunkX, int chunkZ) {
            return (chunkZ & this.factor) * this.diameter + (chunkX & this.factor);
        }
    }
}
