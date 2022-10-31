package com.bjrushworth29.lambdaminigames.utils;

import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


public class InventoryLoadout {
	private final ItemStack[] hotbar;
	private final ItemStack[] inventory;
	private final ItemStack[] armor;

	public InventoryLoadout(HashMap<Integer, ItemStack> hotbar) {
		this(hotbar, null, null);
	}

	public InventoryLoadout(HashMap<Integer, ItemStack> hotbar, HashMap<Integer, ItemStack> armor) {
		this(hotbar, null, armor);
	}

	public InventoryLoadout(HashMap<Integer, ItemStack> hotbar, HashMap<Integer, ItemStack> inventory, HashMap<Integer, ItemStack> armor) {
		this.hotbar = toItemStackArray(hotbar, 9);
		this.inventory = toItemStackArray(inventory, 27);
		this.armor = toItemStackArray(armor, 4);
	}

	private ItemStack[] toItemStackArray(HashMap<Integer, ItemStack> itemStacks, int length) {
		ItemStack[] array = new ItemStack[length];

		if (itemStacks == null) {
			return array;
		}

		for (Map.Entry<Integer, ItemStack> entry : itemStacks.entrySet()) {
			array[entry.getKey()] = entry.getValue();
		}

		return array;
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
