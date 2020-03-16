package com.unclecat.vanillaextended.utils;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.server.MinecraftServer;

public class ApplyEditAndContinueMinecraftLevelCommand extends CommandBase
{
	@Override
	public String getName()
	{
		return "ap";
	}

	@Override
	public List<String> getAliases()
	{
		ArrayList<String> list = new ArrayList<String>();
		list.add("applyedits");
		return list;
	}	
	
	@Override
	public String getUsage(ICommandSender sender)
	{
		return "Calls special function that applies Minecraft abstraction level 'edit & continue' edits.";
	}

	@Override
	public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException
	{
		
	}
}
