package xyz.hewkawar.survival_light_block.mixin;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.LightBlock;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(LightBlock.class)
public abstract class MixinLightBlock extends Block {
    public MixinLightBlock(Properties properties) {
        super(properties);
    }

    @Override
    public void attack(BlockState state, Level level, BlockPos pos, Player player) {
        if (state.getBlock() instanceof LightBlock && !player.isCreative()) {
            if (player.hasCorrectToolForDrops(state)) {
                level.destroyBlock(pos, true, player);
            }
        }
    }
}
