package ml.northwestwind.potatocannon.items;

import ml.northwestwind.potatocannon.PotatoCannon;
import ml.northwestwind.potatocannon.entities.ThrownPotato;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.contents.TranslatableContents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.*;
import net.minecraft.world.level.Level;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Random;

public class PotatoCannonItem extends Item {
    private static final Random random = new Random();

    public PotatoCannonItem() {
        super(new Properties().stacksTo(1).tab(PotatoCannon.PotatoCannonItemGroup.INSTANCE).rarity(Rarity.EPIC));
    }

    @OnlyIn(Dist.CLIENT)
    @Override
    public void appendHoverText(ItemStack stack, @Nullable Level world, List<Component> tooltip, TooltipFlag flag) {
        tooltip.add(MutableComponent.create(new TranslatableContents("tooltip.potatocannon")));
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level worldIn, Player playerIn, InteractionHand handIn) {
        if (!handIn.equals(InteractionHand.MAIN_HAND)) return super.use(worldIn, playerIn, handIn);
        ItemStack stack = getPotatoes(playerIn);
        if (stack.isEmpty()) return super.use(worldIn, playerIn, handIn);
        shootPotato(worldIn, playerIn, stack);
        playerIn.getCooldowns().addCooldown(this, 24);
        return InteractionResultHolder.consume(playerIn.getMainHandItem());
    }

    private void shootPotato(Level world, Player shooter, ItemStack stack) {
        if (!world.isClientSide) {
            ThrownPotato potato = new ThrownPotato(shooter, world);
            potato.setOwner(shooter);
            potato.setItem(stack);

            potato.shootFromRotation(shooter, shooter.getXRot(), shooter.getYRot(), 0.0F, 2, 0.0F);
            world.addFreshEntity(potato);
        }
        if (!shooter.isCreative()) stack.shrink(1);
        world.playSound(null, shooter.blockPosition(), PotatoCannon.POTATO_FIRING, SoundSource.PLAYERS, 1.0F, getRandomSoundPitch());
    }

    private static float getRandomSoundPitch() {
        return 1 / (random.nextFloat() + 0.8f);
    }

    private static ItemStack getPotatoes(Player player) {
        for (ItemStack stack : player.getInventory().items) {
            if (stack.getItem().equals(Items.POTATO)) return stack;
        }
        for (ItemStack stack : player.getInventory().offhand) {
            if (stack.getItem().equals(Items.POTATO)) return stack;
        }
        for (ItemStack stack : player.getInventory().armor) {
            if (stack.getItem().equals(Items.POTATO)) return stack;
        }
        return ItemStack.EMPTY;
    }
}
