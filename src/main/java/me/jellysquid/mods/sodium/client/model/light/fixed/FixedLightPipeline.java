package me.jellysquid.mods.sodium.client.model.light.fixed;

import static me.jellysquid.mods.sodium.client.model.light.data.LightDataAccess.unpackLM;

import java.util.Arrays;

import me.jellysquid.mods.sodium.client.model.light.LightPipeline;
import me.jellysquid.mods.sodium.client.model.light.data.LightDataAccess;
import me.jellysquid.mods.sodium.client.model.light.data.QuadLightData;
import me.jellysquid.mods.sodium.client.model.quad.ModelQuadView;
import me.jellysquid.mods.sodium.client.model.quad.properties.ModelQuadFlags;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;

/**
 * A light pipeline which implements "classic-style" lighting through simply using the light value of the adjacent
 * block to a face.
 */
public class FixedLightPipeline implements LightPipeline {

    private final int value;

    public FixedLightPipeline(int value) {
        this.value = value;
    }

    @Override
    public void calculate(ModelQuadView quad, BlockPos pos, QuadLightData out, Direction face, boolean shade) {
        Arrays.fill(out.lm, 15728640);
        Arrays.fill(out.br, value);
    }
}
