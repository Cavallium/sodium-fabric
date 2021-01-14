package me.jellysquid.mods.sodium.client.model.light.fixed;

import me.jellysquid.mods.sodium.client.model.light.LightPipeline;
import me.jellysquid.mods.sodium.client.model.light.data.QuadLightData;
import me.jellysquid.mods.sodium.client.model.quad.ModelQuadView;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.LightmapTextureManager;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;

import java.util.Arrays;

/**
 * A light pipeline which implements "classic-style" lighting through simply using the light value of the adjacent
 * block to a face.
 */
public class OptionFixedLightPipeline implements LightPipeline {

    private static float gameGamma = getValueFromGammaSetting();

    public OptionFixedLightPipeline() {

    }

    public static float getValueFromGammaSetting() {
        return getValueFromGamma(MinecraftClient.getInstance().options.gamma);
    }

    public static float getValueFromGamma(double gammaValue) {
        return (float) gammaValue;
    }

    public static void setValue(float valueFloat) {
        OptionFixedLightPipeline.gameGamma = valueFloat;
    }

    @Override
    public void calculate(ModelQuadView quad, BlockPos pos, QuadLightData out, Direction face, boolean shade) {
        Arrays.fill(out.lm, LightmapTextureManager.pack(0, 15));
        float faceGammaWeight = 0.2f;
        float heightGammaWeight = 0.7f;
        float gameGammaWeight = 0.7f;

        // Faces 0 to 5 have a different light value
        float faceGamma = (getFaceGamma(face.getAxis(), face.getDirection()));
        float heightThreshold = 60;
        float perBlockReduction = 0.02f;
        float maximumReduction = 1.0f;
        float baseGamma = 0.2f;
        float weightedBlockGameGamma = ((1.0f - gameGammaWeight)) + (gameGamma * gameGammaWeight);
        float weightedBlockHeightGamma = (1.0f - heightGammaWeight) + ((pos.getY() > heightThreshold ? 1.0f : (1.0f - Math.min(maximumReduction, ((60 - pos.getY()) * perBlockReduction)))) * heightGammaWeight);
        float weightedBlockFaceGamma = (1.0f - faceGammaWeight) + (faceGamma * faceGammaWeight);
        float blockGamma = weightedBlockGameGamma * weightedBlockHeightGamma * weightedBlockFaceGamma;
        Arrays.fill(out.br, baseGamma + (blockGamma * (1.0f - baseGamma)));
    }

    private float getFaceGamma(Direction.Axis axis, Direction.AxisDirection axisDirection) {
        switch (axis) {
            case Y:
                switch (axisDirection) {
                    case POSITIVE:
                        return 1.0f;
                    case NEGATIVE:
                        return 0.0f;
                }
            case X:
                switch (axisDirection) {
                    case POSITIVE:
                        return 0.16f;
                    case NEGATIVE:
                        return 0.5f;
                }
            case Z:
                switch (axisDirection) {
                    case POSITIVE:
                        return 0.33f;
                    case NEGATIVE:
                        return 0.66f;
                }
        }
        // Error
        return 0;
    }
}
