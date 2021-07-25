package ml.northwestwind.potatocannon.items;

import ml.northwestwind.potatocannon.PotatoCannon;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import javax.annotation.Nullable;
import java.util.List;

public class CannonComponentItem extends Item {
    public CannonComponentItem(String name) {
        super(new Properties().tab(PotatoCannon.PotatoCannonItemGroup.INSTANCE).rarity(Rarity.RARE));
        setRegistryName(PotatoCannon.MOD_ID, name);
    }

    @OnlyIn(Dist.CLIENT)
    @Override
    public void appendHoverText(ItemStack stack, @Nullable Level worldIn, List<Component> tooltip, TooltipFlag flagIn) {
        tooltip.add(new TranslatableComponent("tooltip.component"));
    }
}
