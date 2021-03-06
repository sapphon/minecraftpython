package org.sapphon.minecraft.modding.minecraftpython.command;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.JsonToNBT;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTException;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.WorldServer;

public class CommandMPSpawnItem extends CommandMPServer {
	private double x;
	public double y;
	private double z;
	private String name;
	private int numberOfItemsToSpawn;
	private String nbtString;
	private NBTTagCompound nbtData;

	public CommandMPSpawnItem(double x, double y, double z, String name, int numberOfItemsToSpawn, String nbtString) {
		this.x = x;
		this.y = y;
		this.z = z;
		this.name = name;
		this.numberOfItemsToSpawn = numberOfItemsToSpawn;
		this.nbtString = nbtString;
	}

	public CommandMPSpawnItem(int x, int y, int z, String name, int numberOfItemsToSpawn, String nbtString) {
		this((double) x, (double) y, (double) z, name, numberOfItemsToSpawn,nbtString);
	}
	
	public CommandMPSpawnItem(String[] commandAndArgsToDeserialize) {
		this(Double.parseDouble(commandAndArgsToDeserialize[1]),
				Double.parseDouble(commandAndArgsToDeserialize[2]),
				Double.parseDouble(commandAndArgsToDeserialize[3]), commandAndArgsToDeserialize[4], Integer.parseInt(commandAndArgsToDeserialize[5]), commandAndArgsToDeserialize[6]);
		try {
			NBTBase possibleNBT;
			possibleNBT = JsonToNBT.func_150315_a(nbtString);
			if(possibleNBT instanceof NBTTagCompound){
				nbtData=(NBTTagCompound)possibleNBT;
			}
			else{
				nbtData = new NBTTagCompound();
			}
		} catch (NBTException e) {
			nbtData = new NBTTagCompound();
		}
	
	}

	
	public void doWork() {
		WorldServer worldserver = MinecraftServer.getServer()
				.worldServerForDimension(0);// TODO ONLY WORKS IN OVERWORLD FOR NOW
		Item item = ItemLookup.getItemByName(name, worldserver);
		EntityItem entityWrapperForTheItemWithoutAHandToHoldIt = new EntityItem(worldserver, x, y, z);
		ItemStack theStack = new ItemStack(item, numberOfItemsToSpawn);
		if(nbtData!=null){theStack.setTagCompound(nbtData);}
		entityWrapperForTheItemWithoutAHandToHoldIt.setEntityItemStack(theStack);
		worldserver.spawnEntityInWorld(entityWrapperForTheItemWithoutAHandToHoldIt);
	}

	@Override
	public String serialize() {
		return CommandMPServer.SPAWNITEM_NAME+ CommandMPAbstract.SERIAL_DIV + x + CommandMPAbstract.SERIAL_DIV + y + CommandMPAbstract.SERIAL_DIV + z + CommandMPAbstract.SERIAL_DIV +name + CommandMPAbstract.SERIAL_DIV + numberOfItemsToSpawn + CommandMPAbstract.SERIAL_DIV + nbtString;
	}
}
