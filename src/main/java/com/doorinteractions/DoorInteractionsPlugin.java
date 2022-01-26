package com.doorinteractions;

import javax.inject.Inject;

import lombok.extern.slf4j.Slf4j;
import net.runelite.api.*;
import net.runelite.api.events.ChatMessage;
import net.runelite.api.events.GameStateChanged;
import net.runelite.client.eventbus.Subscribe;
import net.runelite.client.plugins.Plugin;
import net.runelite.client.plugins.PluginDescriptor;

import net.runelite.api.coords.LocalPoint;
import net.runelite.api.events.ClientTick;
import net.runelite.api.events.MenuOptionClicked;
import net.runelite.client.callback.ClientThread;

import java.util.Map;


@Slf4j
@PluginDescriptor(
	name = "Door Interactions",
		description = "Headbang Doors and Wave Goodbye"
)
public class DoorInteractionsPlugin extends Plugin
{
	@Inject
	private Client client;

	@Inject
	private ClientThread thread;

	@Inject
	private ChatMessage chatMessage;

	private LocalPoint doorLocation;
	private Boolean door = null; //When opening door = true  :  When closing = false
	Player player = null;

	@Override
	protected void startUp() throws Exception
	{
		log.info("Client started!");
	}

	@Override
	protected void shutDown() throws Exception
	{
		log.info("Client stopped!");
	}

	//Used by onChatMessage to parse string, if int, try to emote.
	public static boolean isInteger(String str) {
		if (str == null) {
			return false;
		}
		int length = str.length();
		if (length == 0) {
			return false;
		}
		int i = 0;
		if (str.charAt(0) == '-') {
			if (length == 1) {
				return false;
			}
			i = 1;
		}
		for (; i < length; i++) {
			char c = str.charAt(i);
			if (c < '0' || c > '9') {
				return false;
			}
		}
		return true;
	}

	@Subscribe
	public void onChatMessage(ChatMessage chatMessage){
		Player player = client.getLocalPlayer();
		MessageNode messageNode = chatMessage.getMessageNode();
		String line = messageNode.getValue();
		//System.out.println(line + ": "+isInteger(line));
		//System.out.println(player.getName());
		if (isInteger(line)){
			//System.out.println(Integer.parseInt(line));
			player.setAnimation(Integer.parseInt(line));
			player.setActionFrame(0);
		}
	}

	@Subscribe
	public void onMenuOptionClicked(MenuOptionClicked choice){
		if (choice.getMenuOption().equals("Open") && choice.getMenuTarget().contains("Door")){
			thread.invokeLater(() -> doorLocation = client.getLocalDestinationLocation());
			door = true;
		}
		else if(choice.getMenuOption().equals("Close") && choice.getMenuTarget().contains("Door")) {
			thread.invokeLater(() -> doorLocation = client.getLocalDestinationLocation());
			door = false;
		}
		else{
			thread.invokeLater(()->doorLocation=null);
			door = null;
		}
	}

	@Subscribe
	public void onClientTick(ClientTick tick){
		if (doorLocation != null){
			Player player = client.getLocalPlayer();

			if (door == true){ //Opening Door
				if(doorLocation.distanceTo(player.getLocalLocation())<=Perspective.LOCAL_TILE_SIZE){
					player.setAnimation(729);
					player.setActionFrame(0);
					doorLocation=null;
					door=null;
				}
			}

			if (door == false){ //Closing Door
				if(doorLocation.distanceTo(player.getLocalLocation())<=Perspective.LOCAL_TILE_SIZE){
					player.setAnimation(863);
					player.setActionFrame(0);
					doorLocation=null;
					door=null;
				}
			}
		}
	}
}
