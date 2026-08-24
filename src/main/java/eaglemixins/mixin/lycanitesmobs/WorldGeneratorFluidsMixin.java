package eaglemixins.mixin.lycanitesmobs;

import com.lycanitesmobs.core.worldgen.WorldGeneratorFluids;
import eaglemixins.compat.LycanitesGenerationFilter;
import net.minecraft.world.World;
import net.minecraft.world.chunk.IChunkProvider;
import net.minecraft.world.gen.IChunkGenerator;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Random;

@Mixin(value = WorldGeneratorFluids.class, remap = false)
public class WorldGeneratorFluidsMixin {
    @Inject(method = "generate", at = @At("HEAD"), cancellable = true)
    private void eagleMixins$skipDisabledBiomes(Random random, int chunkX, int chunkZ, World world, IChunkGenerator chunkGenerator, IChunkProvider chunkProvider, CallbackInfo ci) {
        if (LycanitesGenerationFilter.isChunkBiomeDisabled(world, chunkX, chunkZ)) {
            ci.cancel();
        }
    }
}
