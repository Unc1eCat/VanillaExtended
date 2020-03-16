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
//public class BlockLaser2 extends FacedBlock
//{	
//	public BlockLaser2() 
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
//		for (int length = 0; flag && length < max; length++)
//		{
//			flag = tryToCreate(worldIn, facing, pos = pos.offset(facing));
//		}
//	}
//	
//	
//	/* Returns true if ray block is successfully placed */
//	public static boolean tryToCreate(World worldIn, EnumFacing facing, BlockPos createOnPos)
//	{
//		IBlockState stateToReplace = worldIn.getBlockState(createOnPos);
//		Block blockToReplace = stateToReplace.getBlock();
//	
//		if (blockToReplace.getExplosionResistance(worldIn, createOnPos, null, null) < 20.0F || blockToReplace == Blocks.AIR)
//		{
//			worldIn.destroyBlock(createOnPos, true);
//			worldIn.setBlockState(createOnPos, Main.REDSTONE_LASER_RAY.getDefaultState().withProperty(Main.REDSTONE_LASER_RAY.FACING, facing), 3);
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
//		while (worldIn.getBlockState(fromPos).getBlock() == Main.REDSTONE_LASER_RAY)
//		{
//			worldIn.setBlockToAir(fromPos);
//			fromPos = fromPos.offset(facing);
//		}
//		return fromPos;
//	}
//	
//	
//	@Override
//	public void breakBlock(World worldIn, BlockPos pos, IBlockState state)
//	{
//		removeRayFrom(worldIn, state.getValue(FACING), pos.offset(state.getValue(FACING)));
//	}
//	
//	@Override
//	public void neighborChanged(IBlockState state, World worldIn, BlockPos pos, Block blockIn, BlockPos fromPos)
//    {
//		//if (worldIn.isRemote) return;
//			
//		System.out.println("kill");
//		EnumFacing facing = state.getValue(FACING);		
//		Block blockFrom = worldIn.getBlockState(fromPos).getBlock();
//		
//		//if (blockFrom == Main.REDSTONE_LASER_RAY || blockFrom == Blocks.AIR)
//		if (pos.offset(facing).equals(fromPos))
//		{
//			System.out.println("gay");
//			return;
//		}
//		
//		removeRayFrom(worldIn, facing, pos.offset(facing));
//		createRay(worldIn, state, pos);
//    }
//	
//	
//	
//	
//	public static class BlockLaserRay extends FacedBlock
//	{		
//		public static final AxisAlignedBB THIS_AABB = createAABB(0, 0, 0, 0, 0 ,0);
//		
//		
//		
//		public BlockLaserRay() 
//		{
//			super("block_laser_ray", null, null, Material.CIRCUITS, null, 0.0f, 0.0f, false);
//			
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
//			removeRayFrom(worldIn, state.getValue(FACING), pos);
//		}
//		
//		
//	    public void neighborChanged(IBlockState state, World worldIn, BlockPos pos, Block blockIn, BlockPos fromPos)
//	    {
//			//if (worldIn.isRemote) return;
//
//	    	EnumFacing facing = state.getValue(FACING);
//	    	Block blockFrom = worldIn.getBlockState(pos.offset(facing)).getBlock();
//	    		
//	    	if (blockFrom == this || blockFrom == Blocks.AIR) return;
//	    	
//    		BlockPos pos1 = removeRayFrom(worldIn, facing.getOpposite(), pos);
//    		createRay(worldIn, worldIn.getBlockState(pos1), pos1);
//	    }
//		
//	    
//
//		@SideOnly(Side.CLIENT)
//		 public void randomDisplayTick(IBlockState stateIn, World worldIn, BlockPos pos, Random rand)
//		 {
//			double d0 = (double)pos.getX() + 0.5D;
//			double d1 = (double)pos.getY() + 0.5D;
//			double d2 = (double)pos.getZ() + 0.5D;
//			double d3 = (double)(0.4F - (rand.nextFloat() + rand.nextFloat()) * 0.3F);
//			
//			switch (stateIn.getValue(FACING).getAxis())
//			{
//			case X:
//				d0 += (double)(rand.nextFloat());
//				break;
//				
//			case Y:
//				d1 += (double)(rand.nextFloat());
//				break;
//				
//			case Z:
//		        d2 += (double)(rand.nextFloat());
//				break;
//				
//			default:
//				break;
//			
//			}
//
//			worldIn.spawnParticle(EnumParticleTypes.REDSTONE, d0, d1, d2, (double)d3, (double)d3, (double)d3);	 
//		}
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
