package com.unclecat.vanillaextended.content.worldgen;

import java.util.Random;

import com.unclecat.vanillaextended.content.blocks.MultiDimensionalOre;
import com.unclecat.vanillaextended.main.Main;

import net.minecraft.block.state.IBlockState;
import net.minecraft.block.state.pattern.BlockMatcher;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.chunk.IChunkProvider;
import net.minecraft.world.gen.IChunkGenerator;
import net.minecraft.world.gen.feature.WorldGenMinable;
import net.minecraft.world.gen.feature.WorldGenerator;
import net.minecraftforge.fml.common.IWorldGenerator;

/* 
 * Stores all generators, methods describing how to run the generators and generate method 
 * that runs required generators depending on world state 
*/
public class GeneratorBase implements IWorldGenerator
{
	protected WorldGenerator expOreE;
	protected WorldGenerator expOreO;
	protected WorldGenerator expOreN;
	// N - nether // O - overworld // E - end

	public GeneratorBase()
	{
		expOreN = new WorldGenMinable(Main.ORE_XP.getDefaultState().withProperty(MultiDimensionalOre.DIMENSION,
				MultiDimensionalOre.Dimension.NETHER), 1, BlockMatcher.forBlock(Blocks.NETHERRACK));
		expOreO = new WorldGenMinable(Main.ORE_XP.getDefaultState().withProperty(MultiDimensionalOre.DIMENSION,
				MultiDimensionalOre.Dimension.OVERWORLD), 3, BlockMatcher.forBlock(Blocks.STONE));
		expOreE = new WorldGenMinable(Main.ORE_XP.getDefaultState().withProperty(MultiDimensionalOre.DIMENSION,
				MultiDimensionalOre.Dimension.END), 2, BlockMatcher.forBlock(Blocks.END_STONE));
	}


	public void runOreGenerator(WorldGenerator generator, World world, int chunkX, int chunkZ, int chance, int minHeight, int maxHeight, Random rand)
	{
		if ((minHeight < 0 || minHeight > 255) && (minHeight < 0 || minHeight > 255))
			throw new IllegalArgumentException("Argument is out of world height borders");

		int deltaY = maxHeight - minHeight;
		for (int i = 0; i <= chance; i++)
		{
			int x = chunkX * 16 + rand.nextInt(16);
			int z = chunkZ * 16 + rand.nextInt(16);
			int y = minHeight + rand.nextInt(deltaY);
			
			generator.generate(world, rand, new BlockPos(x, y, z)); // Generate

			/*} else
			{ // If yes
				int y = minHeight;
				for (; y <= maxHeight; y++) // Finding ceiling Y at given X and Z
				{
					IBlockState downState = world.getBlockState(new BlockPos(x, y - 1, z));
					IBlockState currentState = world.getBlockState(new BlockPos(x, y, z));
					if ((!downState.getBlock().isFullCube(downState))
							&& currentState.getBlock().isFullCube(currentState)) // If ceiling is detected
					{
						generator.generate(world, rand, new BlockPos(x, y, z)); // Generate
						break;
					}
				}
			}*/
		}
	}

	@Override
	public void generate(Random random, int chunkX, int chunkZ, World world, IChunkGenerator chunkGenerator,
			IChunkProvider chunkProvider)
	{
		switch (world.provider.getDimension())
		{

		case 1: // E
			runOreGenerator(expOreE, world, chunkX, chunkZ, 15, 1, 50, random);
			break;

		case 0: // O
			runOreGenerator(expOreO, world, chunkX, chunkZ, 30, 1, 60, random);
			break;

		case -1: // N
			runOreGenerator(expOreN, world, chunkX, chunkZ, 35, 1, 250, random);
			break;

		}
	}

}
