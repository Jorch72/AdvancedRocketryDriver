package org.quantx.advancedrocketrydriver.driver.controller;

import li.cil.oc.api.network.ManagedEnvironment;
import li.cil.oc.api.prefab.DriverSidedBlock;
import zmaster587.advancedRocketry.api.IMiningDrill;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import zmaster587.advancedRocketry.tile.multiblock.TileSpaceLaser;

public class MiningLaserDriver extends DriverSidedBlock {
    @Override
    public boolean worksWith(World world, BlockPos pos, EnumFacing side) {
        if(!world.getBlockState(pos).getBlock().getRegistryName().toString().equals("advancedrocketry:spacelaser")) {
            return false;
        }

        TileEntity te = world.getTileEntity(pos);
        if(!(te instanceof TileSpaceLaser)) {
            return false;
        }

        return true;
    }

    @Override
    public ManagedEnvironment createEnvironment(World world, BlockPos pos, EnumFacing side) {
        return new EnvironmentMiningLaserController(world.getTileEntity(pos));
    }
}
