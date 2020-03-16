//package com.unclecat.vanillaextended.content.blocks.content;
//
//import java.util.Random;
//
//import com.unclecat.vanillaextended.content.blocks.FacedBlock;
//import com.unclecat.vanillaextended.main.Main;
//
//import net.minecraft.block.Block;
//import net.minecraft.block.SoundType;
//import net.minecraft.block.material.MapColor;
//import net.minecraft.block.material.Material;
//import net.minecraft.block.state.IBlockState;
//import net.minecraft.creativetab.CreativeTabs;
//import net.minecraft.entity.Entity;
//import net.minecraft.entity.item.EntityItem;
//import net.minecraft.init.Blocks;
//import net.minecraft.util.BlockRenderLayer;
//import net.minecraft.util.EnumFacing;
//import net.minecraft.util.EnumParticleTypes;
//import net.minecraft.util.math.AxisAlignedBB;
//import net.minecraft.util.math.BlockPos;
//import net.minecraft.world.IBlockAccess;
//import net.minecraft.world.World;
//import net.minecraftforge.fml.relauncher.Side;
//import net.minecraftforge.fml.relauncher.SideOnly;
//
//public class BlockLaser extends FacedBlock
//{	
//	public BlockLaser() 
//	{
//		super("redstone_laser", CreativeTabs.REDSTONE, SoundType.METAL, Material.IRON, MapColor.IRON, 5.0f, 12.0f, true);
//		setHarvestLevel("pickaxe", 1);
//	}
//	
//		
//	public static void createRay(World worldIn, IBlockState state, BlockPos pos)
//	{
//		EnumFacing facing = state.getValue(FACING);
//		int max = getRedstonePower(worldIn, pos);
//		boolean flag = true;
//		
//		for (int length = 1; flag && length <= max; length++)
//		{
//			flag = tryToCreate(worldIn, length >= 15 ? Main.REDSTONE_LASER_RAY_LAST : Main.REDSTONE_LASER_RAY, facing, pos = pos.offset(facing));
//		}
//		
////		if (max > 0)
////		{
////			worldIn.setBlockState(pos.offset(facing.getOpposite()), Main.REDSTONE_LASER_RAY_LAST.getDefaultState().withProperty(FACING, facing));
////		}
//	}
//	
//	
//	/* Returns true if ray block is successfully placed */
//	public static boolean tryToCreate(World worldIn, BlockLaserRay blockToCreate, EnumFacing facing, BlockPos createOnPos)
//	{
//		IBlockState stateToReplace = worldIn.getBlockState(createOnPos);
//		Block blockToReplace = stateToReplace.getBlock();
//	
//		if (blockToReplace.getExplosionResistance(worldIn, createOnPos, null, null) < 20.0F || blockToReplace == Blocks.AIR)
//		{
//			worldIn.destroyBlock(createOnPos, true);
//			worldIn.setBlockState(createOnPos, blockToCreate.getDefaultState().withProperty(FACING, facing), 2);
//			return true;
//		} else
//		{
//			return false;
//		}
//	}
//	
//	
//	public static BlockPos removeRayFrom(World worldIn, EnumFacing facing, BlockPos fromPos)
//	{
//		while (worldIn.getBlockState(fromPos).getBlock() instanceof BlockLaserRay)
//		{
//			//worldIn.setBlockToAir(fromPos);
//			fromPos = fromPos.offset(facing);
//		}
//		return fromPos;
//	}
//	
//	
//	@Override
//	public void breakBlock(World worldIn, BlockPos pos, IBlockState state)
//	{
//		pos = pos.offset(state.getValue(FACING));
//		
//		if (worldIn.getBlockState(pos).getBlock() instanceof BlockLaserRay)
//		{
//			worldIn.setBlockToAir(pos);
//		}
//	}
//	
//	@Override
//	public void neighborChanged(IBlockState state, World worldIn, BlockPos pos, Block blockIn, BlockPos fromPos)
//    {
//		//if (worldIn.isRemote) return;
//			
//		System.out.println("kill");
//		EnumFacing facing = state.getValue(FACING);		
//
//		if (pos.offset(facing).equals(fromPos))
//		{
//			System.out.println("gay");
//			return;
//		}
//		
//		worldIn.setBlockToAir(pos.offset(facing));
//		createRay(worldIn, state, pos);
//    }
//	
//	
//	
//	
//	public static class BlockLaserRay extends FacedBlock
//	{		
//		public static final AxisAlignedBB THIS_AABB = createAABB(0, 0, 0, 0, 0 ,0);
//		protected boolean lastInRayType;
//		
//		
//		
//		public BlockLaserRay(String name, boolean lastInRayType) 
//		{
//			super(name, null, null, Material.CIRCUITS, null, 0.0f, 0.0f, false);
//			
//			this.lastInRayType = lastInRayType;
//			this.setBlockUnbreakable();
//		}
//		
//		
//		public boolean isOpaqueCube(IBlockState state)
//		{
//		    return false;
//		}
//		public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos)
//	    {
//	        return THIS_AABB;
//	    }
//
//	    public boolean isFullCube(IBlockState state)
//	    {
//	        return false;
//	    }
//		@SideOnly(Side.CLIENT)
//	    public BlockRenderLayer getBlockLayer()
//	    {
//	        return BlockRenderLayer.CUTOUT;
//	    }
//		public boolean isReplaceable(IBlockAccess worldIn, BlockPos pos)
//	    {
//	        return true;
//	    }
//		
//		public boolean canCollideCheck(IBlockState state, boolean hitIfLiquid)
//	    {
//	        return false;
//	    }
//		
//		@Override
//		public void breakBlock(World worldIn, BlockPos pos, IBlockState state)
//		{
//			EnumFacing facing = state.getValue(FACING);
//			pos = pos.offset(facing);
//			
//			if (worldIn.getBlockState(pos).getBlock() instanceof BlockLaserRay)
//			{
//				worldIn.setBlockToAir(pos);
//			}
//		}
//		
//		
//	    public void neighborChanged(IBlockState state, World worldIn, BlockPos pos, Block blockIn, BlockPos fromPos)
//	    {
//	    	System.out.println("tried to xecute smth");
//	    	
//	    	if (state.getBlock() == Main.REDSTONE_LASER_RAY_LAST) return;
//	    	
//	    	System.out.println("executed smth");
//	    	
//	       	EnumFacing facing = state.getValue(FACING);
//	    		
//	    	//if (blockFrom == this || blockFrom == Blocks.AIR) return;
//	    	if (pos.offset(facing).equals(fromPos) && !(worldIn.getBlockState(fromPos).getBlock() instanceof BlockLaserRay))
//	    	{
//	    		BlockPos pos1 = removeRayFrom(worldIn, facing.getOpposite(), pos);
//	    		createRay(worldIn, worldIn.getBlockState(pos1), pos1);
//	    	}
//	    }	
//	    
//		
//		
//		public void onEntityCollidedWithBlock(World worldIn, BlockPos pos, IBlockState state, Entity entityIn)
//	    {
//			//entityIn.attackEntityFrom(DamageSource.ON_FIRE, 4);
//			if (entityIn == null) return;
//			if (entityIn instanceof EntityItem) return;
//			entityIn.setFire(8);
//	    }
//	}
//}
//
