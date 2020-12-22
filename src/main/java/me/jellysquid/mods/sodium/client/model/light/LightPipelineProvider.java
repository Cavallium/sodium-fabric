package me.jellysquid.mods.sodium.client.model.light;

import me.jellysquid.mods.sodium.client.SodiumClientMod;
import me.jellysquid.mods.sodium.client.gui.SodiumGameOptions;
import me.jellysquid.mods.sodium.client.model.light.data.LightDataAccess;
import me.jellysquid.mods.sodium.client.model.light.fixed.FixedLightPipeline;
import me.jellysquid.mods.sodium.client.model.light.fixed.OptionFixedLightPipeline;
import me.jellysquid.mods.sodium.client.model.light.flat.FlatLightPipeline;
import me.jellysquid.mods.sodium.client.model.light.smooth.SmoothLightPipeline;

import java.util.EnumMap;

public class LightPipelineProvider {
    private final EnumMap<LightMode, LightPipeline> lighters = new EnumMap<>(LightMode.class);

    public LightPipelineProvider(LightDataAccess cache) {
        this.lighters.put(LightMode.SMOOTH, new SmoothLightPipeline(cache));
        this.lighters.put(LightMode.FLAT, new FlatLightPipeline(cache));
        this.lighters.put(LightMode.BRIGHT, new OptionFixedLightPipeline());
    }

    public LightPipeline getLighter(LightMode type) {
        SodiumGameOptions opts = SodiumClientMod.options();
        if (!opts.quality.enableLights) {
            type = LightMode.BRIGHT;
        }

        LightPipeline pipeline = this.lighters.get(type);

        if (pipeline == null) {
            throw new NullPointerException("No lighter exists for mode: " + type.name());
        }

        return pipeline;
    }
}
