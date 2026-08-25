package eaglemixins.compat;

import eaglemixins.EagleMixins;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraftforge.common.BiomeDictionary;

public class BiomeTagBlacklist {

    public static boolean isChunkBiomeBlacklisted(World world, int chunkX, int chunkZ, String[] tags) {
        if (tags == null || tags.length == 0) return false;

        Biome biome = world.getBiome(new BlockPos((chunkX << 4) + 8, 0, (chunkZ << 4) + 8));
        if (biome == null) return false;

        for (String tag : tags) {
            if (tag == null) continue;
            String trimmed = tag.trim();
            if (trimmed.isEmpty()) continue;

            if (BiomeDictionary.hasType(biome, BiomeDictionary.Type.getType(trimmed))) {
                return true;
            }
        }

        EagleMixins.LOGGER.info("[EagleMixins DEBUG] Biome {} at chunk {},{} did not match any configured tag {} - its actual tags are {}",
                biome.getRegistryName(), chunkX, chunkZ, java.util.Arrays.toString(tags), java.util.Arrays.toString(BiomeDictionary.getTypes(biome).toArray()));
        return false;
    }
}
