package ml.northwestwind.potatocannon;

import ml.northwestwind.potatocannon.entities.ThrownPotato;
import ml.northwestwind.potatocannon.items.CannonComponentItem;
import ml.northwestwind.potatocannon.items.PotatoCannonItem;
import net.minecraft.client.renderer.entity.EntityRenderers;
import net.minecraft.client.renderer.entity.ThrownItemRenderer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import org.apache.logging.log4j.LogManager;

import javax.annotation.Nonnull;

@Mod(PotatoCannon.MOD_ID)
public class PotatoCannon {
    public static final String MOD_ID = "potatocannon";
    public static final DamageSource POTATO = new DamageSource("potato").setExplosion();

    public static final Item POTATO_CANNON = new PotatoCannonItem();
    public static final Item CANNON_TUBE = new CannonComponentItem("cannon_tube");
    public static final Item CANNON_BODY = new CannonComponentItem("cannon_body");
    public static final Item CANNON_HANDLE = new CannonComponentItem("cannon_handle");

    public static final EntityType<ThrownPotato> POTATO_ENTITY_TYPE = (EntityType<ThrownPotato>) EntityType.Builder.<ThrownPotato>of(ThrownPotato::new, MobCategory.MISC).sized(0.25f, 0.25f).clientTrackingRange(4).updateInterval(10).build("potato").setRegistryName(MOD_ID, "potato");
    public static final SoundEvent POTATO_FIRING = new SoundEvent(new ResourceLocation(MOD_ID, "potato_cannon_firing"));
    public static final SoundEvent POTATO_EXPLOSION = new SoundEvent(new ResourceLocation(MOD_ID, "potato_cannon_explosion"));

    public static class PotatoCannonItemGroup extends CreativeModeTab {
        public static final CreativeModeTab INSTANCE = new PotatoCannonItemGroup(CreativeModeTab.TABS.length, "potatocannon");

        private PotatoCannonItemGroup(int index, String label) {
            super(index, label);
        }

        @Nonnull
        @Override
        public ItemStack makeIcon() {
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
            EntityRenderers.register(POTATO_ENTITY_TYPE, ThrownItemRenderer::new);
            LogManager.getLogger().info("Registered Renderer for POTATO");
        }
    }
}
