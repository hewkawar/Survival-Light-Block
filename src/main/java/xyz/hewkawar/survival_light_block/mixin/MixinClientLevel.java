package xyz.hewkawar.survival_light_block.mixin;

import xyz.hewkawar.survival_light_block.SurvivalLightBlockClient;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Block;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ClientLevel.class)
public class MixinClientLevel {
    @Shadow @Final private Minecraft minecraft;

    @Inject(method = "getMarkerParticleTarget", at = @At("HEAD"), cancellable = true)
    public void getMarkerParticleTarget(CallbackInfoReturnable<Block> info) {
        if (info.getReturnValue() != null) return;

        if (this.minecraft.player.isHolding(Items.LIGHT) || SurvivalLightBlockClient.ModConfig.getToggleLightBlockOption()) {
            info.setReturnValue(Block.byItem(Items.LIGHT));
        }
    }
}