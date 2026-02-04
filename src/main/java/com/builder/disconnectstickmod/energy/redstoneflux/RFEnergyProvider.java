package com.builder.disconnectstickmod.energy.redstoneflux;

import cofh.api.energy.IEnergyConnection;
import cofh.api.energy.IEnergyProvider;
import cofh.api.energy.IEnergyReceiver;
import com.builder.disconnectstickmod.energy.EnergyInfo;
import com.builder.disconnectstickmod.energy.IEnergyInfoProvider;
import cpw.mods.fml.common.Loader;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.common.util.ForgeDirection;

/**
 * CoFH Redstone Flux (RF) 用のエネルギー情報プロバイダ。
 * CoFH APIの型はこのクラス内に閉じ込める。
 */
public class RFEnergyProvider implements IEnergyInfoProvider {
    private static final String COFH_CORE_MOD_ID = "CoFHCore";
    private static final String UNIT = "RF";

    // Mod存在チェックの結果をキャッシュ（パフォーマンス向上）
    private static final boolean isCoFHCoreLoaded;

    static {
        isCoFHCoreLoaded = Loader.isModLoaded(COFH_CORE_MOD_ID);
    }

    @Override
    public boolean isAvailable() {
        return isCoFHCoreLoaded;
    }

    @Override
    public boolean canHandle(TileEntity tileEntity) {
        return tileEntity instanceof IEnergyConnection;
    }

    @Override
    public EnergyInfo getInfo(TileEntity tileEntity, int side) {
        ForgeDirection direction = ForgeDirection.getOrientation(side);

        if (tileEntity instanceof IEnergyReceiver receiver) {
            // IEnergyReceiverは、電力を受け取る工作機械など
            int energy = receiver.getEnergyStored(direction);
            int maxEnergy = receiver.getMaxEnergyStored(direction);
            return new EnergyInfo(energy, maxEnergy, UNIT);
        } else if (tileEntity instanceof IEnergyProvider provider) {
            // IEnergyProviderは、電力を渡す発電機など
            int energy = provider.getEnergyStored(direction);
            int maxEnergy = provider.getMaxEnergyStored(direction);
            return new EnergyInfo(energy, maxEnergy, UNIT);
        }

        return null;
    }
}
