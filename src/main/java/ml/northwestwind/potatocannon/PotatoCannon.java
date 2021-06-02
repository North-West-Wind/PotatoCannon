package ml.northwestwind.potatocannon;

import ml.northwestwind.potatocannon.entities.PotatoEntity;
import ml.northwestwind.potatocannon.items.CannonComponentItem;
import ml.northwestwind.potatocannon.items.PotatoCannonItem;
import net.minecraft.client.renderer.entity.SpriteRenderer;
import net.minecraft.entity.EntityClassification;
import net.minecraft.entity.EntityType;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.util.DamageSource;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.client.registry.RenderingRegistry;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;

import javax.annotation.Nonnull;

@Mod(PotatoCannon.MOD_ID)
public class PotatoCannon {
    public static final String MOD_ID = "potatocannon";
    public static final DamageSource POTATO = new DamageSource("potato").setExplosion();

    public static final Item POTATO_CANNON = new PotatoCannonItem();
    public static final Item CANNON_TUBE = new CannonComponentItem("cannon_tube");
    public static final Item CANNON_BODY = new CannonComponentItem("cannon_body");
    public static final Item CANNON_HANDLE = new CannonComponentItem("cannon_handle");

    public static final EntityType<PotatoEntity> POTATO_ENTITY_TYPE = (EntityType<PotatoEntity>) EntityType.Builder.<PotatoEntity>create(PotatoEntity::new, EntityClassification.MISC).size(0.25f, 0.25f).build("potato_entity").setRegistryName(MOD_ID, "potato_entity");
    public static final SoundEvent POTATO_FIRING = new SoundEvent(new ResourceLocation(MOD_ID, "potato_cannon_firing"));
    public static final SoundEvent POTATO_EXPLOSION = new SoundEvent(new ResourceLocation(MOD_ID, "potato_cannon_explosion"));

    public static class PotatoCannonItemGroup extends ItemGroup {
        public static final ItemGroup INSTANCE = new PotatoCannonItemGroup(ItemGroup.GROUPS.length, "potatocannon");

        private PotatoCannonItemGroup(int index, String label) {
            super(index, label);
        }

        @Nonnull
        @Override
        public ItemStack createIcon() {
            return new ItemStack(POTATO_CANNON);
        }
    }

    @Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
    public static class RegistryEvents {
        @SubscribeEvent
        public static void registerItems(final RegistryEvent.Register<Item> event) {
            event.getRegistry().registerAll(POTATO_CANNON, CANNON_BODY, CANNON_TUBE, CANNON_HANDLE);
        }

        @SubscribeEvent
        public static void registerEntities(final RegistryEvent.Register<EntityType<?>> event) {
            event.getRegistry().register(POTATO_ENTITY_TYPE);
        }
    }

    @Mod.EventBusSubscriber(value = Dist.CLIENT, bus = Mod.EventBusSubscriber.Bus.MOD)
    public static class ClientEvents {
        @SubscribeEvent
        public static void clientSetup(final FMLClientSetupEvent event) {
            RenderingRegistry.registerEntityRenderingHandler(POTATO_ENTITY_TYPE, renderManager -> new SpriteRenderer<>(renderManager, event.getMinecraftSupplier().get().getItemRenderer()));
        }
    }
}
