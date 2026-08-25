package eaglemixins.mixin.iceandfire;

import com.github.alexthe666.iceandfire.world.village.MapGenPixieVillage;
import eaglemixins.EagleMixins;
import eaglemixins.compat.BiomeTagBlacklist;
import eaglemixins.config.ForgeConfigHandler;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

/**
 * canSpawnStructureAtCoords is never actually called by IceAndFire (see
 * StructureGeneratorPixieVillageMixin) - this inject stays as a diagnostic so the debug log
 * confirms whether that's still true when a new IceAndFire version is used.
 */
@Mixin(value = MapGenPixieVillage.class, remap = false)
public class MapGenPixieVillageMixin {
    @Inject(method = "canSpawnStructureAtCoords", at = @At("HEAD"), cancellable = true)
    private void eagleMixins$blacklistBiomes(World world, int chunkX, int chunkZ, CallbackInfoReturnable<Boolean> cir) {
        boolean blacklisted = BiomeTagBlacklist.isChunkBiomeBlacklisted(world, chunkX, chunkZ, ForgeConfigHandler.server.pixieVillageDisabledBiomeTags);
        EagleMixins.LOGGER.info("[EagleMixins DEBUG] MapGenPixieVillage#canSpawnStructureAtCoords fired at chunk {},{} blacklisted={} (if you never see this line, IceAndFire isn't calling this method)",
                chunkX, chunkZ, blacklisted);
        if (blacklisted) {
            cir.setReturnValue(false);
        }
    }
}
