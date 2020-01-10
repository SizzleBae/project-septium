package com.sizzlebae.projectseptium.client.gui;

import com.mojang.blaze3d.platform.GlStateManager;
import com.sizzlebae.projectseptium.ProjectSeptium;
import com.sizzlebae.projectseptium.container.QuartzAssemblerContainer;
import com.sizzlebae.projectseptium.tileentity.QuartzAssemblerTileEntity;
import net.minecraft.client.gui.screen.inventory.ContainerScreen;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;

public class QuartzAssemblerScreen extends ContainerScreen<QuartzAssemblerContainer> {

    private static final ResourceLocation BACKGROUND_TEXTURE = new ResourceLocation(ProjectSeptium.MODID, "textures/gui/container/quartz_assembler.png");

    public QuartzAssemblerScreen(QuartzAssemblerContainer screenContainer, PlayerInventory inv, ITextComponent titleIn) {
        super(screenContainer, inv, titleIn);
    }

    @Override
    public void render(final int mouseX, final int mouseY, final float partialTicks) {
        this.renderBackground();
        super.render(mouseX, mouseY, partialTicks);
        this.renderHoveredToolTip(mouseX, mouseY);


    }

    @Override
    protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY) {
        super.drawGuiContainerForegroundLayer(mouseX, mouseY);

        String s = this.title.getFormattedText();
        this.font.drawString(s,(float) (this.xSize / 2 - this.font.getStringWidth(s) / 2), 6.0F, 0x404040);
        this.font.drawString("HELLO FROM QUARTZ ASSEMBLER", 8, (float) (this.ySize - 96 + 2), 0x404040);
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY) {
        GlStateManager.color4f(1.0F, 1.0F, 0.0F, 1.0F);

        getMinecraft().getTextureManager().bindTexture(BACKGROUND_TEXTURE);
        int startX = this.guiLeft;
        int startY = this.guiTop;

        // Screen#blit draws a part of the current texture (assumed to be 256x256) to the screen
        // The parameters are (x, y, u, v, width, height)
        this.blit(startX, startY, 0, 0, this.xSize, this.ySize);

        final QuartzAssemblerTileEntity tileEntity = container.tileEntity;


    }
}
