package com.builder.disconnectstickmod.client.renderer;

import com.builder.disconnectstickmod.DisconnectStickMod;
import com.builder.disconnectstickmod.tileentities.TileEntityChimera;
import cpw.mods.fml.client.registry.ISimpleBlockRenderingHandler;
import net.minecraft.block.Block;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.IIcon;
import net.minecraft.world.IBlockAccess;

import java.util.Arrays;
import java.util.List;

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
        int[] targetMetadatas = targetTileEntityChimera.getCellMetadatas();

        for (int i = 0; i < 8; i++) {
            Block cellBlock = targetCellBlocks[i];
            int metadata = targetMetadatas[i];
            if (cellBlock == null) continue;

            boolean isEastSide = (i & 1) != 0;
            boolean isSouthSide = (i & 2) != 0;
            boolean isTopSIde = (i & 4) != 0;

            double xMin = isEastSide ? 0.5 : 0;
            double yMin = isTopSIde ? 0.5 : 0;
            double zMin = isSouthSide ? 0.5 : 0;

            renderer.setRenderBounds(xMin, yMin, zMin, xMin + 0.5, yMin + 0.5, zMin + 0.5);

            // TODO: 面やメタデータによって異なるテクスチャに対応する

            Tessellator tessellator = Tessellator.instance;
//            tessellator.setBrightness(block.getMixedBrightnessForBlock(world, x, y, z));
            tessellator.setColorOpaque_F(1.0F, 1.0F, 1.0F);

//            renderer.setRenderAllFaces(true);

            // 不変なのでList.ofを使いたいがシンボルが見つからないとエラーになる
            List<PentaConsumer<Block, Integer, Integer, Integer, IIcon>> renderFaces = Arrays.asList(
                renderer::renderFaceYNeg,
                renderer::renderFaceYPos,
                renderer::renderFaceZNeg,
                renderer::renderFaceZPos,
                renderer::renderFaceXNeg,
                renderer::renderFaceXPos
            );

            for (int j = 0; j < renderFaces.size(); j++) {
                IIcon texture = cellBlock.getIcon(j, metadata);
//                renderer.setOverrideBlockTexture(texture);
//                DisconnectStickMod.LOG.info(block);
//                DisconnectStickMod.LOG.info(texture);
                renderFaces.get(j).accept(block, x, y, z, texture);
            }
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

    @FunctionalInterface
    interface PentaConsumer<A, B, C, D, E> {
        void accept(A a, B b, C c, D d, E e);
    }
}
