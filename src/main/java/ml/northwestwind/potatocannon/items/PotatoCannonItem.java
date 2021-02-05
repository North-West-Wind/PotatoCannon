package ml.northwestwind.potatocannon.items;

import ml.northwestwind.potatocannon.PotatoCannon;
import ml.northwestwind.potatocannon.entities.PotatoEntity;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.item.Rarity;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import javax.annotation.Nullable;
import java.util.List;

public class PotatoCannonItem extends Item {
    public PotatoCannonItem() {
        super(new Properties().maxStackSize(1).group(PotatoCannon.PotatoCannonItemGroup.INSTANCE).rarity(Rarity.EPIC));
        setRegistryName(PotatoCannon.MOD_ID, "potato_cannon");
    }

    @OnlyIn(Dist.CLIENT)
    @Override
    public void addInformation(ItemStack stack, @Nullable World worldIn, List<ITextComponent> tooltip, ITooltipFlag flagIn) {
        tooltip.add(new TranslationTextComponent("tooltip.potatocannon"));
    }

    @Override
    public ActionResult<ItemStack> onItemRightClick(World worldIn, PlayerEntity playerIn, Hand handIn) {
        if (!handIn.equals(Hand.MAIN_HAND)) return super.onItemRightClick(worldIn, playerIn, handIn);
        ItemStack stack = getPotatoes(playerIn);
        if (stack.isEmpty()) return super.onItemRightClick(worldIn, playerIn, handIn);
        shootPotato(worldIn, playerIn, stack);
        playerIn.getCooldownTracker().setCooldown(this, 24);
        return ActionResult.resultConsume(playerIn.getHeldItemMainhand());
    }

    private void shootPotato(World world, PlayerEntity shooter, ItemStack stack) {
        if (!world.isRemote) {
            PotatoEntity potato = new PotatoEntity(shooter, world);
            potato.setShooter(shooter);
            potato.setItem(stack);

            potato.func_234612_a_(shooter, shooter.rotationPitch, shooter.rotationYaw, 0.0F, 2, 0.0F);
            world.addEntity(potato);
        }
        if (!shooter.abilities.isCreativeMode) {
            stack.shrink(1);
        }
        world.playSound(null, shooter.getPosX(), shooter.getPosY(), shooter.getPosZ(), PotatoCannon.POTATO_FIRING, SoundCategory.PLAYERS, 1.0F, getRandomSoundPitch());
    }

    private static float getRandomSoundPitch() {
        return 1 / (random.nextFloat() + 0.8f);
    }

    private static ItemStack getPotatoes(PlayerEntity player) {
        for (ItemStack stack : player.inventory.mainInventory) {
            if (stack.getItem().equals(Items.POTATO)) return stack;
        }
        for (ItemStack stack : player.inventory.offHandInventory) {
            if (stack.getItem().equals(Items.POTATO)) return stack;
        }
        for (ItemStack stack : player.inventory.armorInventory) {
            if (stack.getItem().equals(Items.POTATO)) return stack;
        }
        return ItemStack.EMPTY;
    }
}
