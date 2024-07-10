package cn.ksmcbrigade.cuf.mixin;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.ChatComponent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.*;

@Mixin(ChatComponent.class)
public class ChatMixin {

    @Unique
    public int chatUpForge$get(){
        Minecraft MC = Minecraft.getInstance();
        if(MC.player==null || MC.player.isCreative() || MC.player.isSpectator()) return 0;
        return ((MC.player.getAbsorptionAmount()>0)?((MC.player.getArmorValue()>0?10:0)+10):(MC.player.getArmorValue()>0?10:0));
    }

    @ModifyArg(method = "render",index = 1,at = @At(value = "INVOKE", target = "Lcom/mojang/blaze3d/vertex/PoseStack;translate(FFF)V",ordinal = 0))
    public float getY(float y){
        return y - chatUpForge$get();
    }

    @ModifyConstant(method = "screenToChatY",constant = @Constant(doubleValue = 40.0D))
    public double chatY(double constant){
        return constant + chatUpForge$get();
    }

    @ModifyConstant(method = "addMessage(Lnet/minecraft/network/chat/Component;Lnet/minecraft/network/chat/MessageSignature;ILnet/minecraft/client/GuiMessageTag;Z)V",constant = @Constant(intValue = 100))
    public int chatY(int constant){
        return constant*2*2; // = 100 * 2 * 2 = 400
    }
}
