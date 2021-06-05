package ml.northwestwind.potatocannon;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import ml.northwestwind.potatocannon.items.PotatoCannonItem;
import net.minecraft.client.entity.player.AbstractClientPlayerEntity;
import net.minecraft.client.renderer.entity.PlayerRenderer;
import net.minecraft.client.renderer.entity.model.PlayerModel;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RenderPlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = PotatoCannon.MOD_ID, value = Dist.CLIENT)
public class Handler {
    @SubscribeEvent
    public static void preRender(RenderPlayerEvent.Pre event) {
        PlayerEntity player = event.getPlayer();
        PlayerRenderer render = event.getRenderer();
        PlayerModel<AbstractClientPlayerEntity> model = render.getEntityModel();
        if(player != null && player.getHeldItemMainhand().getItem() instanceof PotatoCannonItem){
            model.bipedLeftArm.showModel=false;
            model.bipedRightArm.showModel=false;
        }
    }

    @SubscribeEvent
    public static void postRender(final RenderPlayerEvent.Post event) {
        PlayerEntity player = event.getPlayer();
        ItemStack stack = player.getHeldItemMainhand();
        if (!(stack.getItem() instanceof PotatoCannonItem)) return;
        PlayerModel<AbstractClientPlayerEntity> model = event.getRenderer().getEntityModel();
        renderAiming(model, player, event);
    }

    private static void renderAiming(PlayerModel<AbstractClientPlayerEntity> model, PlayerEntity player, RenderPlayerEvent event)
    {
        MatrixStack matrix = event.getMatrixStack();
        IVertexBuilder buffer = event.getBuffers().getBuffer(model.getRenderType(((AbstractClientPlayerEntity) player).getLocationSkin()));
        int light = event.getLight();
        int texture = OverlayTexture.NO_OVERLAY;

        model.bipedLeftArm.rotationPointX = -MathHelper.cos((float) Math.toRadians(player.renderYawOffset)) * -5.5F;
        model.bipedLeftArm.rotationPointY = player.isCrouching() ? 17.5F : 20.5F;
        model.bipedLeftArm.rotationPointZ = -MathHelper.sin((float) Math.toRadians(player.renderYawOffset)) * -5.5F;
        model.bipedLeftArm.rotateAngleX = -1.7F - (model.bipedLeftArm.rotateAngleX + (float) (Math.PI / 2.0f))*2.0F;
        model.bipedLeftArm.rotateAngleY =  (float) Math.toRadians(player.renderYawOffset) + (float) Math.toRadians(60);
        model.bipedLeftArm.rotateAngleZ = (float) -Math.PI + model.bipedLeftArm.rotateAngleZ;

        model.bipedRightArm.rotationPointZ = -MathHelper.sin((float) Math.toRadians(player.renderYawOffset)) * 5.5F;
        model.bipedRightArm.rotationPointY = player.isCrouching() ? 17.5F : 20.5F;
        model.bipedRightArm.rotationPointX = -MathHelper.cos((float) Math.toRadians(player.renderYawOffset)) * 5.5F;
        model.bipedRightArm.rotateAngleX = -1.7F - (model.bipedLeftArm.rotateAngleX + (float) (Math.PI / 2.0f));
        model.bipedRightArm.rotateAngleY = (float) Math.toRadians(player.renderYawOffset);
        model.bipedRightArm.rotateAngleZ = (float) -Math.PI + model.bipedRightArm.rotateAngleZ;

        model.bipedRightArm.showModel=true;
        model.bipedLeftArm.showModel=true;

        model.bipedRightArm.render(matrix, buffer, light, texture);
        model.bipedLeftArm.render(matrix, buffer, light, texture);
    }
}
