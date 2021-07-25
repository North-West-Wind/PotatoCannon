package ml.northwestwind.potatocannon;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import ml.northwestwind.potatocannon.items.PotatoCannonItem;
import net.minecraft.client.model.PlayerModel;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.client.renderer.entity.player.PlayerRenderer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RenderPlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = PotatoCannon.MOD_ID, value = Dist.CLIENT)
public class Handler {
    @SubscribeEvent
    public static void preRender(RenderPlayerEvent.Pre event) {
        Player player = event.getPlayer();
        PlayerRenderer render = event.getRenderer();
        PlayerModel<AbstractClientPlayer> model = render.getModel();
        if(player != null && player.getMainHandItem().getItem() instanceof PotatoCannonItem){
            model.leftArm.visible = false;
            model.rightArm.visible = false;
        }
    }

    @SubscribeEvent
    public static void postRender(final RenderPlayerEvent.Post event) {
        Player player = event.getPlayer();
        ItemStack stack = player.getMainHandItem();
        if (!(stack.getItem() instanceof PotatoCannonItem)) return;
        PlayerModel<AbstractClientPlayer> model = event.getRenderer().getModel();
        renderAiming(model, player, event);
    }

    private static void renderAiming(PlayerModel<AbstractClientPlayer> model, Player player, RenderPlayerEvent event)
    {
        PoseStack matrix = event.getMatrixStack();
        VertexConsumer buffer = event.getBuffers().getBuffer(model.renderType(((AbstractClientPlayer) player).getSkinTextureLocation()));
        int light = event.getLight();
        int texture = OverlayTexture.NO_OVERLAY;

        model.leftArm.x = -Mth.cos((float) Math.toRadians(player.xOld)) * -5.5F;
        model.leftArm.y = player.isCrouching() ? 17.5F : 20.5F;
        model.leftArm.z = -Mth.sin((float) Math.toRadians(player.xOld)) * -5.5F;
        model.leftArm.xRot = -1.7F - (model.leftArm.xRot + (float) (Math.PI / 2.0f))*2.0F;
        model.leftArm.yRot =  (float) Math.toRadians(player.xOld) + (float) Math.toRadians(60);
        model.leftArm.zRot = (float) -Math.PI + model.leftArm.zRot;

        model.rightArm.z = -Mth.sin((float) Math.toRadians(player.xOld)) * 5.5F;
        model.rightArm.y = player.isCrouching() ? 17.5F : 20.5F;
        model.rightArm.x = -Mth.cos((float) Math.toRadians(player.xOld)) * 5.5F;
        model.rightArm.xRot = -1.7F - (model.leftArm.xRot + (float) (Math.PI / 2.0f));
        model.rightArm.yRot = (float) Math.toRadians(player.xOld);
        model.rightArm.zRot = (float) -Math.PI + model.rightArm.zRot;

        model.rightArm.visible=true;
        model.leftArm.visible=true;

        model.rightArm.render(matrix, buffer, light, texture);
        model.leftArm.render(matrix, buffer, light, texture);
    }
}
