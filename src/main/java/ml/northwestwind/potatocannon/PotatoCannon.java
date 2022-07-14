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
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import org.apache.logging.log4j.LogManager;

import javax.annotation.Nonnull;

@Mod(PotatoCannon.MOD_ID)
public class PotatoCannon {
    public static final String MOD_ID = "potatocannon";
    public static final DamageSource POTATO = new DamageSource("potato").setExplosion();
    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, MOD_ID);

    public static final RegistryObject<Item> POTATO_CANNON = ITEMS.register("potato_cannon", PotatoCannonItem::new);
    public static final RegistryObject<Item> CANNON_TUBE = ITEMS.register("cannon_tube", CannonComponentItem::new);
    public static final RegistryObject<Item> CANNON_BODY = ITEMS.register("cannon_body", CannonComponentItem::new);
    public static final RegistryObject<Item> CANNON_HANDLE = ITEMS.register("cannon_handle", CannonComponentItem::new);

    public static final DeferredRegister<EntityType<?>> ENTITY_TYPES = DeferredRegister.create(ForgeRegistries.ENTITIES, MOD_ID);
    public static final RegistryObject<EntityType<ThrownPotato>> POTATO_ENTITY_TYPE = ENTITY_TYPES.register("potato", () -> EntityType.Builder.<ThrownPotato>of(ThrownPotato::new, MobCategory.MISC).sized(0.25f, 0.25f).clientTrackingRange(4).updateInterval(10).build("potato"));
    public static final SoundEvent POTATO_FIRING = new SoundEvent(new ResourceLocation(MOD_ID, "potato_cannon_firing"));
    public static final SoundEvent POTATO_EXPLOSION = new SoundEvent(new ResourceLocation(MOD_ID, "potato_cannon_explosion"));

    public PotatoCannon() {
        IEventBus bus = FMLJavaModLoadingContext.get().getModEventBus();
        ITEMS.register(bus);
        ENTITY_TYPES.register(bus);
    }

    public static class PotatoCannonItemGroup extends CreativeModeTab {
        public static final CreativeModeTab INSTANCE = new PotatoCannonItemGroup(CreativeModeTab.TABS.length, "potatocannon");

        private PotatoCannonItemGroup(int index, String label) {
            super(index, label);
        }

        @Nonnull
        @Override
        public ItemStack makeIcon() {
            return new ItemStack(POTATO_CANNON.get());
        }
    }

    @Mod.EventBusSubscriber(value = Dist.CLIENT, bus = Mod.EventBusSubscriber.Bus.MOD)
    public static class ClientEvents {
        @SubscribeEvent
        public static void clientSetup(final FMLClientSetupEvent event) {
            EntityRenderers.register(POTATO_ENTITY_TYPE.get(), ThrownItemRenderer::new);
            LogManager.getLogger().info("Registered Renderer for POTATO");
        }
    }
}
