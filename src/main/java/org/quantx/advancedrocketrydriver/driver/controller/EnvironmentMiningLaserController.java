package org.quantx.advancedrocketrydriver.driver.controller;

import li.cil.oc.api.Network;
import li.cil.oc.api.driver.NamedBlock;
import li.cil.oc.api.machine.Arguments;
import li.cil.oc.api.machine.Callback;
import li.cil.oc.api.machine.Context;
import li.cil.oc.api.network.Visibility;
import li.cil.oc.api.prefab.AbstractManagedEnvironment;
import zmaster587.advancedRocketry.api.IMiningDrill;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import zmaster587.advancedRocketry.tile.multiblock.TileSpaceLaser;
import zmaster587.libVulpes.inventory.modules.ModuleNumericTextbox;
import zmaster587.libVulpes.network.PacketHandler;
import zmaster587.libVulpes.network.PacketMachine;

import java.util.HashMap;

public class EnvironmentMiningLaserController extends AbstractManagedEnvironment implements NamedBlock {

    protected final TileEntity tileEntity;
    protected final BlockPos controllerPos;
    protected final World controllerWorld;

    public EnvironmentMiningLaserController(TileEntity tileEntity) {
        this.tileEntity = tileEntity;
        this.controllerWorld = this.tileEntity.getWorld();
        this.controllerPos = this.tileEntity.getPos();



        this.setNode(Network.newNode(this, Visibility.Network).withComponent("advancedrocketry", Visibility.Network).create());
    }

    @Callback(doc = "function( ):boolean -- Returns whether or not the laser is firing")
    public Object[] isRunning(final Context context, final Arguments args) {
        return new Object[]{ ((TileSpaceLaser)tileEntity).isRunning() };
    }

    @Callback(doc = "function( ):boolean -- Returns whether or not the laser is jammed")
    public Object[] isJammed(final Context context, final Arguments args) {
        return new Object[]{ ((TileSpaceLaser)tileEntity).isJammed() };
    }

    @Callback(doc = "function( ):boolean -- Returns whether or not the laser is finished drilling")
    public Object[] isFinished(final Context context, final Arguments args) {
        return new Object[]{ ((TileSpaceLaser)tileEntity).isFinished() };
    }

    @Callback(doc = "function( ):number, number -- Returns the X and Z coordinate of where the laser is aiming")
    public Object[] getPosition(final Context context, final Arguments args) {
        return new Object[]{
            ((TileSpaceLaser)tileEntity).laserX,
            ((TileSpaceLaser)tileEntity).laserZ
        };
    }

    @Callback(doc = "function( ):number -- Returns how much power is stored in the device")
    public Object[] getPower(final Context context, final Arguments args) {
        return new Object[]{ ((TileSpaceLaser)tileEntity).getBatteries().getEnergyStored() };
    }

    @Callback(doc = "function( ):number -- Returns how much power the device can store")
    public Object[] getMaxPower(final Context context, final Arguments args) {
        return new Object[]{ ((TileSpaceLaser)tileEntity).getBatteries().getMaxEnergyStored() };
    }

    @Callback(doc = "function(posX:number, posZ:number):boolean -- Trys to set the X and Z coordinate of where the laser is aiming")
    public Object[] setPosition(final Context context, final Arguments args) throws Exception {
        TileSpaceLaser spaceLaser = (TileSpaceLaser)tileEntity;

        if (spaceLaser.isRunning()) return new Object[]{ false };

        spaceLaser.laserX = args.checkInteger( 0 );
        PacketHandler.sendToServer(new PacketMachine(spaceLaser, (byte)10));

        spaceLaser.laserX = args.checkInteger( 1 );
        PacketHandler.sendToServer(new PacketMachine(spaceLaser, (byte)11));

        return new Object[]{ true };
    }

    /*
    @Callback(doc = "function( ):boolean -- Trys to switch to the next firing mode")
    public Object[] nextMode(final Context context, final Arguments args) {
        TileSpaceLaser spaceLaser = (TileSpaceLaser)tileEntity;

        if ( spaceLaser.isRunning() ) return new Object[]{ false };

        // Kind of a hacky way to do it, but Advanced Rocketry doesn't play nice
        // and we need to update the GUI
        spaceLaser.onInventoryButtonPressed(0);

        return new Object[]{ true };
    }

    @Callback(doc = "function( ):boolean -- Trys to switch to the previous firing mode")
    public Object[] prevMode(final Context context, final Arguments args) {
        TileSpaceLaser spaceLaser = (TileSpaceLaser)tileEntity;

        if ( spaceLaser.isRunning() ) return new Object[]{ false };

        // Kind of a hacky way to do it, but Advanced Rocketry doesn't play nice
        // and we need to update the GUI
        spaceLaser.onInventoryButtonPressed(1);

        return new Object[]{ true };
    }
    */

    @Callback(doc = "function(mode:string):boolean -- Trys to set the firing mode of the laser")
    public Object[] setMode(final Context context, final Arguments args) throws Exception {
        TileSpaceLaser spaceLaser = (TileSpaceLaser) tileEntity;

        if (spaceLaser.isRunning()) return new Object[]{false};

        String mode = args.checkString(0);

        if (mode.equalsIgnoreCase("SINGLE")) {
            spaceLaser.setMode(TileSpaceLaser.MODE.SINGLE);
        } else if (mode.equalsIgnoreCase("LINE_X")) {
            spaceLaser.setMode(TileSpaceLaser.MODE.LINE_X);
        } else if (mode.equalsIgnoreCase("LINE_Z")) {
            spaceLaser.setMode(TileSpaceLaser.MODE.LINE_Z);
        } else if (mode.equalsIgnoreCase("SPIRAL")) {
            spaceLaser.setMode(TileSpaceLaser.MODE.SPIRAL);
        } else {
            return new Object[]{null, "Unknown mode '" + mode + "'"};
        }

        PacketHandler.sendToServer(new PacketMachine(spaceLaser, (byte)13));

        return new Object[]{true};
    }



    @Override
    public String preferredName() {
        return "space_laser";
    }

    @Override
    public int priority() {
        return 1;
    }
}
