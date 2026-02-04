package com.builder.disconnectstickmod.energy;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.tileentity.TileEntity;

import com.builder.disconnectstickmod.energy.redstoneflux.RFEnergyProvider;

/**
 * 利用可能なエネルギープロバイダを管理するレジストリ。
 * TileEntityに対応するプロバイダを検索し、エネルギー情報を取得する。
 */
public class EnergyInfoProviderRegistry {

    private static final List<IEnergyInfoProvider> providers = new ArrayList<>();
    private static boolean initialized = false;

    /**
     * レジストリを初期化する。Modのロード時に一度だけ呼び出す。
     */
    public static void init() {
        if (initialized) return;
        initialized = true;

        // RFプロバイダを登録（CoFHCoreの存在チェックはRFUtil内で行う）
        providers.add(new RFEnergyProvider());

        // 将来的に他の電力規格を追加する場合はここに追加
        // 例: providers.add(new IC2EnergyProvider());
    }

    /**
     * TileEntityからエネルギー情報を取得する。
     *
     * @param tileEntity 対象のTileEntity
     * @param side       クリックされた面
     * @return エネルギー情報、対応するプロバイダがない場合はnull
     */
    public static EnergyInfo getEnergyInfo(TileEntity tileEntity, int side) {
        for (IEnergyInfoProvider provider : providers) {
            if (provider.isAvailable() && provider.canHandle(tileEntity)) {
                return provider.getInfo(tileEntity, side);
            }
        }
        return null;
    }
}
