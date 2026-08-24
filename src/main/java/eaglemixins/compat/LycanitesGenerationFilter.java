package eaglemixins.compat;

import eaglemixins.config.ForgeConfigHandler;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraftforge.common.BiomeDictionary;

public class LycanitesGenerationFilter {

    public static boolean isChunkBiomeDisabled(World world, int chunkX, int chunkZ) {
        String[] disabledTags = ForgeConfigHandler.server.lycanitesGenerationDisabledBiomeTags;
        if (disabledTags == null || disabledTags.length == 0) return false;

        Biome biome = world.getBiome(new BlockPos((chunkX << 4) + 8, 0, (chunkZ << 4) + 8));
        if (biome == null) return false;

        for (String tag : disabledTags) {
            if (tag == null) continue;
            String trimmed = tag.trim();
            if (trimmed.isEmpty()) continue;

            if (BiomeDictionary.hasType(biome, BiomeDictionary.Type.getType(trimmed))) {
                return true;
            }
        }
        return false;
    }
}
