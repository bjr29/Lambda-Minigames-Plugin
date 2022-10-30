package com.bjrushworth29.lambdaminigames.utils;

import org.bukkit.inventory.ItemStack;

import java.security.InvalidParameterException;
import java.util.ArrayList;


public class InventoryLoadout {
	private final ItemStack[] hotbar;
	private final ItemStack[] inventory;
	private final ItemStack[] armor;

	public InventoryLoadout() {
		this(new ItemStack[9], new ItemStack[27], new ItemStack[4]);
	}

	/**
     * @param hotbar Must be of length 9
     */
	public InventoryLoadout(ItemStack[] hotbar) {
		this(hotbar, new ItemStack[27], new ItemStack[4]);
	}

	/**
     * @param hotbar Must be of length 9
     * @param armor  Must be of length 4
     */
	public InventoryLoadout(ItemStack[] hotbar,  ItemStack[] armor) throws InvalidParameterException {
		this(hotbar, new ItemStack[27], armor);
	}

	/**
	 * @param hotbar Must be of length 9
	 * @param inventory Must be of length 27
	 * @param armor Must be of length 4
	 */
	public InventoryLoadout(ItemStack[] hotbar, ItemStack[] inventory, ItemStack[] armor) throws InvalidParameterException {
		this.hotbar = hotbar;
		this.inventory = inventory;
		this.armor = armor;

		if (hotbar.length != 9) {
			throw new InvalidParameterException(String.format("Hotbar length should be 9 but is %d!", hotbar.length));
		}

		if (inventory.length != 27) {
			throw new InvalidParameterException(String.format("Inventory length should be 27 but is %d!", inventory.length));
		}

		if (armor.length != 4) {
			throw new InvalidParameterException(String.format("Armor length should be 4 but is %d!", armor.length));
		}
	}

	@SuppressWarnings({"UseBulkOperation", "ManualArrayToCollectionCopy"})
	public ItemStack[] getInventory() {
		ArrayList<ItemStack> arrayList = new ArrayList<>();

		for (ItemStack itemStack : hotbar) {
			arrayList.add(itemStack);
		}

		for (ItemStack itemStack : inventory) {
			arrayList.add(itemStack);
		}

		return arrayList.toArray(new ItemStack[0]);
	}

	public ItemStack[] getArmor() {
		return armor;
	}
}
