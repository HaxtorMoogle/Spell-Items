package me.andre111.items;

import me.andre111.items.item.enchant.CustomEnchant;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class SpellCommandExecutor implements CommandExecutor {

	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		//GIVE COMMAND
		if(command.getName().equalsIgnoreCase("siGive")) {
			if(!sender.hasPermission("spellitems.give")) {
				sender.sendMessage("You don't have the Permission to do that!");
				return false;
			}
			
			//get the player
			if(args.length>0) {
				Player player = Bukkit.getServer().getPlayerExact(args[0]);
				
				if(player!=null) {
					//recombine all other arguments
					String itemSt = "";
					int ii = 1;
					while(args.length>ii) {
						itemSt = itemSt + " " + args[ii];
						ii++;
					}
					
					//get the tem
					ItemStack it = ItemHandler.decodeItem(itemSt);
					if(it!=null) {
						player.getInventory().addItem(it);
						
						return true;
					} else {
						sender.sendMessage("Could not decode Itemstring: "+itemSt);
						return false;
					}
				} else {
					sender.sendMessage("Player "+args[0]+" not found!");
					return false;
				}
			} else {
				sender.sendMessage("Please specify a player to give the item to!");
				return false;
			}
		}
		//ENCHANT COMMAND
		if(command.getName().equalsIgnoreCase("siEnchant")) {
			if(!sender.hasPermission("spellitems.enchant")) {
				sender.sendMessage("You don't have the Permission to do that!");
				return false;
			}
			if (!(sender instanceof Player)) {
				sender.sendMessage("This command can only be used by a Player!");
				return true;
			}
			Player player = (Player)sender;
			
			if(args.length>0) {
				CustomEnchant ce = SpellItems.enchantManager.getEnchantmentByName(args[0]);
				if(ce!=null) {
					ItemStack it = player.getItemInHand();
					it = ce.enchantItem(it, 1);
					player.setItemInHand(it);
					
					return true;
				} else {
					sender.sendMessage("Could not find Enchantment: "+args[0]);
					return false;
				}
			} else {
				sender.sendMessage("Please specify an enchantment!");
				return false;
			}
		}
		//HELP COMMAND
		if(command.getName().equalsIgnoreCase("siHelp")) {
			if(!sender.hasPermission("spellitems.help")) {
				sender.sendMessage("You don't have the Permission to do that!");
				return false;
			}
			
			if(args.length>0) {
				if(args[0].equalsIgnoreCase("siGive")) {
					if(sender.hasPermission("spellitems.give")) {
						sender.sendMessage("Give Items to Players using the SpellItems Syntax");
						sender.sendMessage("For info on the syntax please use /siHelp syntax");
						return true;
					}
				}
				if(args[0].equalsIgnoreCase("siEnchant")) {
					if(sender.hasPermission("spellitems.enchant")) {
						sender.sendMessage("Enchants the Item you are currently holding with a custom Enchantment");
						sender.sendMessage("This command can only be used as a Player");
						if(sender.hasPermission("spellitems.give")) {
							sender.sendMessage("But you can use /siGive to give allready enchanted Items");
						}
						return true;
					}
				}
				if(args[0].equalsIgnoreCase("Syntax")) {
					sender.sendMessage("Itemsyntax: ");
					sender.sendMessage("id_OR_customItemName:<damagevalue> <min_count>:<max_count> <chance_to_get_item> <enchant1_id_OR_customEnchantmentName>:<enchant1_level>,... <name>,<lore1>,<lore2>,<lore3>");
					sender.sendMessage("Name and Lore1,2,3 can contain spaces");
					sender.sendMessage("Enchantmentids: ");
					sender.sendMessage("id of a default enchantment or the internalname of a custom one");
					sender.sendMessage("-1 to completly ignore enchants(if you want to set a name)");
					sender.sendMessage("-10 to only get the glowing Effect without an enchantment");
					sender.sendMessage("WARNING: Do not change name or lore of custom Items or they will not work!");
					return true;
				}
			} else {
				sender.sendMessage("Please specify what you wnat more Info about!");
				return false;
			}
		}
		return false;
	}

}
