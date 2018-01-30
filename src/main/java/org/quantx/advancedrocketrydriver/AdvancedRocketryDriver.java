package org.quantx.advancedrocketrydriver;

import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import li.cil.oc.api.Driver;
import org.quantx.advancedrocketrydriver.driver.controller.MiningLaserDriver;

@Mod(
        modid = AdvancedRocketryDriver.MODID,
        version = AdvancedRocketryDriver.VERSION,
        guiFactory = AdvancedRocketryDriver.GUI_FACTORY,
        dependencies = "required-after:opencomputers;required-after:advancedrocketry",
        acceptedMinecraftVersions = "[1.12,1.13)"
)

public class AdvancedRocketryDriver {
    public static final String MODID = "advancedrocketrydriver";
    public static final String VERSION = "1.0";
    public static final String GUI_FACTORY = "org.quantx.advancedrocketrydriver.config.GuiFactory";

    @Mod.Instance
    public static AdvancedRocketryDriver instance;

    @EventHandler
    public void preInit(FMLPreInitializationEvent event) {
        Lager.setLogger(event.getModLog());
    }

    @EventHandler
    public void init(FMLInitializationEvent event) {
        Driver.add(new MiningLaserDriver());
    }
}
