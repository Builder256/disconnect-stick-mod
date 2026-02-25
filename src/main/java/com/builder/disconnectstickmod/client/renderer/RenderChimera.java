package com.builder.disconnectstickmod.client.renderer;

import com.builder.disconnectstickmod.DisconnectStickMod;
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
        // 同じチャンク内に同じブロックが置かれるたびに、既存のブロックでもそれぞれ実行される
//        DisconnectStickMod.LOG.info("renderWorldBlock is called!!");

        // よくわからんけどフィルタするっぽい
        if (modelId != this.getRenderId()) return false;

        TileEntity targetTileEntity = world.getTileEntity(x, y, z);
        if (!(targetTileEntity instanceof TileEntityChimera targetTileEntityChimera)) return false;

        Block[] targetCellBlocks = targetTileEntityChimera.getCellBlocks();

        for (int i = 0; i < 8; i++) {
            Block cellBlock = targetCellBlocks[i];

            boolean isEastSide = (i & 1) != 0;
            boolean isSouthSide = (i & 2) != 0;
            boolean isTopSIde = (i & 4) != 0;

            double xMin = isEastSide ? 0.5 : 0;
            double yMin = isTopSIde ? 0.5 : 0;
            double zMin = isSouthSide ? 0.5 : 0;

            renderer.setRenderBounds(xMin, yMin, zMin, xMin + 0.5, yMin + 0.5, zMin + 0.5);

            if (cellBlock == null) continue;

            // TODO: 面やメタデータによって異なるテクスチャに対応する
            renderer.setOverrideBlockTexture(cellBlock.getIcon(0, 0));
            renderer.renderStandardBlock(block, x, y, z);
        }

        // 上書きしたブロックのテクスチャを戻す
        renderer.clearOverrideBlockTexture();

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
