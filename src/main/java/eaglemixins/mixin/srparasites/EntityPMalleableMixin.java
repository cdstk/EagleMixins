package eaglemixins.mixin.srparasites;

import com.dhanantry.scapeandrunparasites.entity.ai.misc.EntityPMalleable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(EntityPMalleable.class)
public class EntityPMalleableMixin {

    // Matches the damage type string of PaleBloom's PotionPaleLung.PALE_LUNG_DAMAGE.
    // Kept as a literal (rather than a compile-time dependency on PaleBloom) so this mixin
    // stays harmless (never matches, never triggers) when PaleBloom isn't installed.
    private static final String PALE_LUNG_DAMAGE_TYPE = "dynamictreespalebloom.pale_lung";

    @Inject(method = "hasResistance", at = @At("HEAD"), cancellable = true, remap = false)
    private void eagleMixins_blockPaleLungAdaptation(String name, byte type, CallbackInfoReturnable<Integer> cir) {
        //Parasites can never adapt/build resistance against Pale Lung's damage tick
        if (PALE_LUNG_DAMAGE_TYPE.equals(name)) {
            cir.setReturnValue(0);
        }
    }
}
