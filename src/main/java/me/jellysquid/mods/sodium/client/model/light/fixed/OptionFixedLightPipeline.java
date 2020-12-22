package me.jellysquid.mods.sodium.client.model.light.fixed;

import me.jellysquid.mods.sodium.client.SodiumClientMod;
import me.jellysquid.mods.sodium.client.model.light.LightPipeline;
import me.jellysquid.mods.sodium.client.model.light.data.QuadLightData;
import me.jellysquid.mods.sodium.client.model.quad.ModelQuadView;
import net.minecraft.client.render.LightmapTextureManager;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;

import java.util.Arrays;

/**
 * A light pipeline which implements "classic-style" lighting through simply using the light value of the adjacent
 * block to a face.
 */
public class OptionFixedLightPipeline implements LightPipeline {

    private static float valueFloat = -1.0f;

    public OptionFixedLightPipeline() {
    }

    /**
     *
     * @param value 0 to 100
     */
    public static void setValue(int value) {
        OptionFixedLightPipeline.valueFloat = ((float) value) / 100.0f;
    }

    @Override
    public void calculate(ModelQuadView quad, BlockPos pos, QuadLightData out, Direction face, boolean shade) {
        if (valueFloat == -1.0f) {
            setValue(SodiumClientMod.options().quality.fixedLightLevel);
        }
        Arrays.fill(out.lm, LightmapTextureManager.pack(0, 15));
        Arrays.fill(out.br, valueFloat);
    }
}
