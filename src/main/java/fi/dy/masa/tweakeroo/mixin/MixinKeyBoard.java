package fi.dy.masa.tweakeroo.mixin;

import fi.dy.masa.tweakeroo.Tweakeroo;
import fi.dy.masa.tweakeroo.config.FeatureToggle;
import fi.dy.masa.tweakeroo.util.InventoryUtils;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.GameMenuScreen;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;


@Mixin(net.minecraft.client.Keyboard.class)
public abstract class MixinKeyBoard {
    @Final
    @Shadow
    private MinecraftClient client;

    @Shadow
    protected abstract boolean processF3(int key);

    @Inject(method = "onKey", at = @At(value = "HEAD"))
    private void onKey(long window, int key, int scancode, int action, int modifiers, CallbackInfo ci) {
        if (window == this.client.getWindow().getHandle()) {
            boolean bl = InputUtil.isKeyPressed(MinecraftClient.getInstance().getWindow().getHandle(), 292);
            InputUtil.Key key2;
            boolean bl3;
            boolean var10000;
            label201:
            {
                key2 = InputUtil.fromKeyCode(key, scancode);
                bl3 = this.client.currentScreen == null;
                if (!bl3) {
                    label197:
                    {
                        Screen var13 = this.client.currentScreen;
                        if (var13 instanceof GameMenuScreen gameMenuScreen) {
                            if (!gameMenuScreen.shouldShowMenu()) {
                                break label197;
                            }
                        }

                        var10000 = false;
                        break label201;
                    }
                }

                var10000 = true;
            }

            boolean bl4 = var10000;
            if (action != 0) {
                boolean bl5 = false;
                if (bl4) {
                    if (key == 256) {
                        bl5 |= bl;
                    }
                    bl5 |= bl && this.processF3(key);
                }

                if (bl3 && !bl5) {
                    //inject start
                    /* As for we inject here, we need bl3 and bl5, so I just copy all sources describing them from genSources and remove things not related which cause bugs*/
                    KeyBinding keyBinding = (KeyBinding) net.minecraft.client.option.KeyBinding.KEY_TO_BINDINGS.get(key2);
                    if (keyBinding != null) {
                        if (!keyBinding.isPressed()) {//means this is the first call of this press event
                            if (keyBinding.equals(Tweakeroo.jumpKey)) {
                                if (FeatureToggle.TWEAK_AUTO_SWITCH_ELYTRA.getBooleanValue() && Tweakeroo.player.input.playerInput.forward()) {
                                    // PlayerEntity#checkFallFlying
                                    if (!Tweakeroo.player.isOnGround() && !Tweakeroo.player.isGliding() && !Tweakeroo.player.isInFluid() && !Tweakeroo.player.isClimbing() && !Tweakeroo.player.hasStatusEffect(StatusEffects.LEVITATION)) {
                                        if (!Tweakeroo.player.getEquippedStack(EquipmentSlot.CHEST).isOf(Items.ELYTRA) ||
                                                Tweakeroo.player.getEquippedStack(EquipmentSlot.CHEST).getDamage() > Tweakeroo.player.getEquippedStack(EquipmentSlot.CHEST).getMaxDamage() - 10) {
                                            InventoryUtils.equipBestElytra(Tweakeroo.player);
                                        }
                                    }
                                } else {
                                    // reset auto switch item if the feature is disabled.
                                    Tweakeroo.autoSwitchElytraChestplate = ItemStack.EMPTY;
                                }
                            }
                        }
                    }
                    //inject end
                }
            }
        }
    }
}
