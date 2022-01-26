/*
 * Copyright (c) 2022, strrules105 <camobot3@gmail.com>
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 * 1. Redistributions of source code must retain the above copyright notice, this
 *    list of conditions and the following disclaimer.
 * 2. Redistributions in binary form must reproduce the above copyright notice,
 *    this list of conditions and the following disclaimer in the documentation
 *    and/or other materials provided with the distribution.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND
 * ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
 * WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE LIABLE FOR
 * ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
 * LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
 * ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
package com.doorinteractions;

import javax.inject.Inject;

import lombok.extern.slf4j.Slf4j;
import net.runelite.api.*;
import net.runelite.api.events.ChatMessage;
import net.runelite.client.eventbus.Subscribe;
import net.runelite.client.plugins.Plugin;
import net.runelite.client.plugins.PluginDescriptor;
import net.runelite.api.coords.LocalPoint;
import net.runelite.api.events.ClientTick;
import net.runelite.api.events.MenuOptionClicked;
import net.runelite.client.callback.ClientThread;


@Slf4j
@PluginDescriptor(
	name = "Open Sesame",
		description = "Blow doors open and Wave Goodbye"
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

	/*
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
	 */

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
