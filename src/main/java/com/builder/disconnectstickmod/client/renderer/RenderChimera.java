package com.builder.disconnectstickmod.client.renderer;

import com.builder.disconnectstickmod.tileentities.TileEntityChimera;
import cpw.mods.fml.client.registry.ISimpleBlockRenderingHandler;
import net.minecraft.block.Block;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.IBlockAccess;

import static com.builder.disconnectstickmod.CommonProxy.renderId;

public class RenderChimera implements ISimpleBlockRenderingHandler {
    public RenderChimera() {
    }

    @Override
    public void renderInventoryBlock(Block block, int metadata, int modelId, RenderBlocks renderer) {
    }

    @Override
    public boolean renderWorldBlock(IBlockAccess world, int x, int y, int z, Block block, int modelId, RenderBlocks renderer) {
        // よくわからんけどフィルタするっぽい
        if (modelId != this.getRenderId()) return false;

        TileEntity targetTileEntity = world.getTileEntity(x, y, z);
        // なんとなくそのままキャストするのが不安
        if (!(targetTileEntity instanceof TileEntityChimera targetTileEntityChimera)) return false;
0
        byte targetVisibilityMask = targetTileEntityChimera.getVisibilityMask();
        Block[] targetRenderBlocks = targetTileEntityChimera.getRenderBlocks();

        for (int i = 0; i < 8; i++) {
            if ((targetVisibilityMask & (1 << i)) == 0) continue;
            // targetVisibilityMaskの下からi番目のビットが1のとき


            double xMin = (i & 1) * 0.5;
            double yMin = ((i >> 1) & 1) * 0.5;
            double zMin = ((i >> 2) & 1) * 0.5;

            renderer.setRenderBounds(xMin, yMin, zMin, xMin + 0.5, yMin + 0.5, zMin + 0.5);

            Block targetRenderBlock = targetRenderBlocks[i];
            if (targetRenderBlock != null) {
                renderer.overrideBlockTexture = targetRenderBlock.getIcon(0, 0);
            }

            renderer.renderStandardBlock(block, x, y, z);
        }

        // 描画範囲を設定する
//        renderer.setRenderBounds(0.0D, 0.0D, 0.0D, 0.5D, 0.5D, 0.5D);
        // ブロックのテクスチャを上書きする
//        renderer.overrideBlockTexture = Blocks.stone.getIcon(0, 0);
        // 直方体をその場所に描画
//        renderer.renderStandardBlock(block, x, y, z);


        // 上書きしたブロックのテクスチャを戻す
        renderer.overrideBlockTexture = null;

        return true;
    }

    @Override
    public boolean shouldRender3DInInventory(int modelId) {
        return false;
    }

    @Override
    public int getRenderId() {
        return renderId;
    }
}
