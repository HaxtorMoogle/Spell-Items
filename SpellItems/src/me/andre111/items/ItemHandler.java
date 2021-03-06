package me.andre111.items;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import me.andre111.items.item.enchant.CustomEnchant;
import me.andre111.items.volatileCode.DynamicClassFunctions;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.inventory.meta.ItemMeta;

public class ItemHandler {
	private static final Random random = new Random();
	
	public static ItemStack decodeItem(String str, Player player) {
		while(str.startsWith(" ")) {
			str = str.substring(1);
		}
		while(str.endsWith(" ")) {
			str = str.substring(0, str.length()-1);
		}
		
		if(str.startsWith("!")) {
			return decodeNewItem(str.substring(1), player);
		} else {
			return decodeOldItem(str);
		}
	}
	
	private static ItemStack decodeNewItem(String str, Player player) {
		ItemStack item = null;
		Material material = null;
		int damage = 0;
		int countmin = 1;
		int countmax = -1;
		double chance = 100;
		String permission = ".";
		String dataTag = "";
		
		boolean exception = false;
		
		String[] geteilt = str.split(" ");
		try {
			//Item Material or Special Item
			if(geteilt.length>0) {
				if(geteilt[0].toLowerCase().startsWith("minecraft:")) {
					material = Material.matchMaterial(geteilt[0]);
	
					if (material == null) {
						material = Bukkit.getUnsafe().getMaterialFromInternalName(geteilt[0]);
					}
				} else if(geteilt[0].toLowerCase().startsWith("spellitems:")) {
					String[] iname = geteilt[0].split(":");
					item = SpellItems.itemManager.getItemStackByName(iname[1]);
				}
			}
			//damage 
			if(geteilt.length>1) {
				damage = Integer.parseInt(geteilt[1]);
				if(!(random.nextDouble()*100<chance)) return null;
			}
			//count
			if(geteilt.length>2) {
				String[] counts = geteilt[2].split(":");
				if(counts.length>0) countmin = Integer.parseInt(counts[0]);
				if(counts.length>1) countmax = Integer.parseInt(counts[1]);
			}
			//chance
			if(geteilt.length>3) {
				chance = Double.parseDouble(geteilt[3]);
			}
			//permission
			if(geteilt.length>5) {
				permission = geteilt[5];
				if(!permission.equals(".")) {
					if(player==null) return null;
					if(!player.hasPermission(permission)) return null;
				}
			}
			//dataTag
			if(geteilt.length>6) {
				for(int i=6; geteilt.length>i; i++) {
					dataTag = dataTag + geteilt[i];
				}
			}
			
			//item erstellen
			int count = countmin;
			if(countmax!=-1) count = countmin + random.nextInt(countmax-countmin+1);
			if(material!=null || item==null) {
				item = new ItemStack(material, count, (short) damage);
			} else if(item!=null) {
				item.setAmount(count);
			}
			
			//dataTag einf�gen
			if(!dataTag.equals("")) {
				try {
					item = Bukkit.getUnsafe().modifyItemStack(item, dataTag);
				} catch(Exception e) {
					e.printStackTrace();
				}
			}
			
			//custom enchantments
			if(geteilt.length>4) {
				boolean addGlow = false;
				String[] enchants = geteilt[4].split(",");
				for(int i=0; i<enchants.length; i++) {
					String[] split_e = enchants[i].split(":");
					int elevel = 0;
	
					if(split_e.length>0) {
						if(split_e.length>1) elevel = Integer.parseInt(split_e[1]);
		
						if(split_e[0].equals("-1")) {
							
						} else if(split_e[0].equals("-10")) {
							addGlow = true;
						} else {
							CustomEnchant ce = SpellItems.enchantManager.getEnchantmentByName(split_e[0]);
							if(ce!=null) {
								item = ce.enchantItem(item, elevel);
							}
						}
					}
				}
				
				if(addGlow) {
					item = DynamicClassFunctions.addGlow(item);
				}
			}
			
			return item;
		} catch (NumberFormatException e) {
			exception = true;
		}
		
		if((item==null && !str.equals("0")) || exception) { 
			SpellItems.log("Could not decode Itemstring: "+str);
		}
		return null;
	}
	
	private static ItemStack decodeOldItem(String str) {
		while(str.startsWith(" ")) {
			str = str.substring(1);
		}
		while(str.endsWith(" ")) {
			str = str.substring(0, str.length()-1);
		}
		
		int id = 0;
		int damage = 0;
		int countmin = 1;
		int countmax = -1;
		double chance = 100;
		boolean addGlow = false;
		boolean exception = false;
		ItemStack item = null;
		String[] geteilt = str.split(" ");
		try {
			//id + damage
			if(geteilt.length>0) {
				String[] id_d = geteilt[0].split(":");
				if(id_d.length>0) {
					try {
						id = Integer.parseInt(id_d[0]);
					} catch (NumberFormatException e) {
						item = SpellItems.itemManager.getItemStackByName(id_d[0]);
					}
				}
				if(id_d.length>1) damage = Integer.parseInt(id_d[1]);
			}
			//count
			if(geteilt.length>1) {
				String[] counts = geteilt[1].split(":");
				if(counts.length>0) countmin = Integer.parseInt(counts[0]);
				if(counts.length>1) countmax = Integer.parseInt(counts[1]);
			}
			//chance
			if(geteilt.length>2) {
				chance = Double.parseDouble(geteilt[2]);
			}

			if((id!=0 || item!=null) && random.nextDouble()*100<chance) {
				int count = countmin;
				if(countmax!=-1) count = countmin + random.nextInt(countmax-countmin+1);
				if(item==null) {
					item = new ItemStack(id, count, (short)damage);
				} else {
					item.setAmount(count);
				}

				//enchantments
				if(geteilt.length>3) {
					String[] enchants = geteilt[3].split(",");
					for(int i=0; i<enchants.length; i++) {
						String[] split_e = enchants[i].split(":");
						int eid = -1;
						int elevel = 0;

						try {
							if(split_e.length>0) eid = Integer.parseInt(split_e[0]);
							if(split_e.length>1) elevel = Integer.parseInt(split_e[1]);
	
							if(eid>-1) {
								item.addUnsafeEnchantment(Enchantment.getById(eid), elevel);
							}
							//glow only(-10)
							else if(eid==-10) {
								addGlow = true;
							}
						}
						//custom enchant
						catch(NumberFormatException ex) {
							CustomEnchant ce = SpellItems.enchantManager.getEnchantmentByName(split_e[0]);
							if(ce!=null) {
								item = ce.enchantItem(item, elevel);
							}
						}
						
					}
				}

				//name+lore
				if(geteilt.length>4) {
					ItemMeta itemM = item.getItemMeta();

					//rest wieder zusammensetzen(weil leerzeichen im namen sein k�nnen)
					String added = "";
					int j = 4;
					while(geteilt.length>j) {
						if(added.equals("")) added = geteilt[j].intern();
						else added = added + " " + geteilt[j].intern();
						j++;
					}

					String[] names = added.split(",");

					//name
					if(names.length>0) itemM.setDisplayName(names[0]);
					//lore 1+2+3
					if(names.length>1) {
						List<String> lore = new ArrayList<String>();

						for(int i=0; i<3; i++) {
							if(names.length>1+i) {
								lore.add(names[1+i]);
							}
						}

						itemM.setLore(lore);
					}

					item.setItemMeta(itemM);
				}
				
				if(addGlow) {
					item = DynamicClassFunctions.addGlow(item);
				}

				return item;
			}
		} catch (NumberFormatException e) {
			exception = true;
		}
		
		if(((id==0 && item==null) && !str.equals("0")) || exception) { 
			SpellItems.log("Could not decode Itemstring: "+str);
		}
		return null;
	}
	
	//TODO - recode for new Itemformat Version
	/*public static int decodeItemId(String str) {
		while(str.startsWith(" ")) {
			str = str.substring(1);
		}
		while(str.endsWith(" ")) {
			str = str.substring(0, str.length()-1);
		}
		
		int id = -1;
		String[] geteilt = str.split(" ");
		//id
		if(geteilt.length>0) {
			String[] id_d = geteilt[0].split(":");
			if(id_d.length>0) id = Integer.parseInt(id_d[0]); {
				try {
					id = Integer.parseInt(id_d[0]);
				} catch (NumberFormatException e) {
					//custom items
					if(SpellItems.itemManager.getItemStackByName(id_d[0])!=null)
						id = SpellItems.itemManager.getItemStackByName(id_d[0]).getTypeId();
				}
			}
		}
		
		return id;
	}*/
	
	public static void clearInv(Player player, boolean enderChest) {
		PlayerInventory inv = player.getInventory();
		inv.clear();
		inv.clear(inv.getSize() + 0);
		inv.clear(inv.getSize() + 1);
		inv.clear(inv.getSize() + 2);
		inv.clear(inv.getSize() + 3);
		if(enderChest)
			player.getEnderChest().clear();
	}
	
	public static boolean isInvEmpty(Player player, boolean enderChest) {
		for(ItemStack item : player.getInventory().getContents())
		{
		    if(item != null)
		    if(item.getAmount()>0)
		      return false;
		}
		for(ItemStack item : player.getInventory().getArmorContents())
		{
		    if(item != null)
		    if(item.getAmount()>0)
		      return false;
		}
		if(enderChest) {
			for(ItemStack item : player.getEnderChest().getContents())
			{
			    if(item != null)
			    if(item.getAmount()>0)
			      return false;
			}
		}
		
		return true;
	}
	
	public static boolean isArmorEmpty(Player player) {
		for(ItemStack item : player.getInventory().getArmorContents())
		{
		    if(item != null)
		    if(item.getAmount()>0)
		      return false;
		}
		
		return true;
	}
	
	//###################################
	//Inventory Helpers
	//###################################
	public static int removeItems(Player player, int type, int data, int remaining) {
		int itemsExchanged = 0;
		for (ItemStack i : player.getInventory()){
			if (i != null && i.getTypeId() == type && i.getData().getData() == data){
				if (i.getAmount() > remaining){
					i.setAmount(i.getAmount() - remaining);
					itemsExchanged += remaining;
					remaining = 0;
				}else{
					itemsExchanged += i.getAmount();
					remaining -= i.getAmount();
					player.getInventory().remove(i);
				}
				if(remaining==0) break;
			}
		}
		return itemsExchanged;
	}

	public static int countItems(Player player, int type, int data) {
		int items = 0;
		for (ItemStack i : player.getInventory()){
			if (i != null && i.getTypeId() == type && i.getData().getData() == data){
				items += i.getAmount();
			}
		}
		return items;
	}
	
	//TODO - remove temporary workaround
	@SuppressWarnings("deprecation")
	public static void updateInventory(Player player) {
		player.updateInventory();
	}
}
