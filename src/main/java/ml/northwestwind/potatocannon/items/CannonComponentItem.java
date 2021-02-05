package ml.northwestwind.potatocannon.items;

import ml.northwestwind.potatocannon.PotatoCannon;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Rarity;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import javax.annotation.Nullable;
import java.util.List;

public class CannonComponentItem extends Item {
    public CannonComponentItem(String name) {
        super(new Properties().group(PotatoCannon.PotatoCannonItemGroup.INSTANCE).rarity(Rarity.RARE));
        setRegistryName(PotatoCannon.MOD_ID, name);
    }

    @OnlyIn(Dist.CLIENT)
    @Override
    public void addInformation(ItemStack stack, @Nullable World worldIn, List<ITextComponent> tooltip, ITooltipFlag flagIn) {
        tooltip.add(new TranslationTextComponent("tooltip.component"));
    }
}
