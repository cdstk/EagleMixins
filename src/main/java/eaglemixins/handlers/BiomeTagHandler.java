package eaglemixins.handlers;

import eaglemixins.EagleMixins;
import eaglemixins.config.ForgeConfigHandler;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.biome.Biome;
import net.minecraftforge.common.BiomeDictionary;
import net.minecraftforge.fml.common.registry.ForgeRegistries;

public class BiomeTagHandler {

    public static void init() {
        String[] entries = ForgeConfigHandler.server.biomeDictionaryTagList;
        if (entries == null) return;

        for (String raw : entries) {
            if (raw == null) continue;
            String s = raw.trim();
            if (s.isEmpty()) continue;

            int sep = s.indexOf('=');
            if (sep <= 0 || sep >= s.length() - 1) {
                EagleMixins.LOGGER.error("[EagleMixins] BiomeTag Invalid entry '{}', expected <biome_id>=<tag>", s);
                continue;
            }

            String biomeIdStr = s.substring(0, sep).trim();
            String tagStr = s.substring(sep + 1).trim();

            try {
                Biome biome = ForgeRegistries.BIOMES.getValue(new ResourceLocation(biomeIdStr));
                if (biome == null) {
                    EagleMixins.LOGGER.warn("[EagleMixins] BiomeTag: biome '{}' not found, skipping tag '{}'", biomeIdStr, tagStr);
                    continue;
                }

                BiomeDictionary.addTypes(biome, BiomeDictionary.Type.getType(tagStr));
            } catch (Exception e) {
                EagleMixins.LOGGER.error("[EagleMixins] BiomeTag Failed to parse '{}': {}", s, e.toString());
            }
        }
    }
}
